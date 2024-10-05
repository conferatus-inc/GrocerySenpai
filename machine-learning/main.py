import random

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from typing import List
from pydantic import BaseModel
import datetime

tz = datetime.timezone.utc
dt = datetime.datetime(1970, 1, 1, tzinfo=datetime.timezone.utc)

app = FastAPI()

origins = [
    'http://localhost',
]
taskResults = {}

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*']
)


class Product(BaseModel):
    category: str
    boughtOn: str


class ProductsHistory(BaseModel):
    items: List[Product]


@app.get('/result/{taskId}')
async def returnResult(taskId: int, limit: int = 100):
    print(f'taskId is {taskId}')
    print(f'limit is {limit}')
    # res = taskResults[taskId]
    print(taskResults[taskId])
    answer = {"items": list(map(lambda k: {"category": k[0], "nextBuy": (k[1].strftime("%d-%m-%Y"))}, taskResults[taskId].items()))}
    return answer
    # return \
    #     {
    #         "items": [
    #             {
    #                 "category": "Пиво",
    #                 "nextBuy": "22-11-2023"
    #             },
    #             {
    #                 "category": "Пиво",
    #                 "nextBuy": "29-11-2023"
    #             },
    #             {
    #                 "category": "Пиво",
    #                 "nextBuy": "15-11-2023"
    #             },
    #             {
    #                 "category": "Ром",
    #                 "nextBuy": "28-11-2023"
    #             }
    #         ]
    #     }


from statsmodels.tsa.deterministic import CalendarFourier, DeterministicProcess
from sklearn.linear_model import LinearRegression
import pandas as pd
from itertools import accumulate


def machineLearning(category, days, lastDate, firstDate):
    forecastRange = 20
    by_days = [0] * (lastDate + 1)
    for day in days:
        by_days[day] = 1
    by_days = list(accumulate(by_days))
    dp = DeterministicProcess(
        index=pd.date_range(f"{firstDate.year}-{firstDate.month}-{firstDate.day}", freq="D",
                            periods=len(by_days) + forecastRange),
        # maybe transform firstDate to "2000-1-1"
        constant=True,  # dummy feature for bias (y-intercept)
        order=1,  # trend (order 1 means linear)
        seasonal=True,  # weekly seasonality (indicators)
        drop=True,  # drop terms to avoid collinearity
    )
    X = dp.in_sample()
    # create features for dates in tunnel.index

    X["day"] = X.index.dayofweek
    X["week"] = X.index.to_period().week
    # X["dayofyear"] = X.index.dayofyear
    X["year"] = X.index.year
    X_pred = X[len(by_days):]
    X = X[:len(by_days)]

    y = by_days.copy()
    X_train = X.copy()
    y_train = y.copy()
    model = LinearRegression()
    # model = GradientBoostingRegressor(n_estimators=60, learning_rate=0.1, max_depth=4, random_state=666)
    model.fit(X_train, y_train)

    lst = model.predict(X_train[len(X_train)-1:len(X_train)])
    y_pred = model.predict(X_pred)
    for i in range(0, len(y_pred)):
        if (y_pred[i] - lst) > 0.9:
            return 1 + i + len(by_days)
    return len(by_days) + forecastRange + 10


def predictNextBuy(category, days, lastDate, firstDate):
    days.sort()
    buyDay = lastDate
    if len(days) < 2:
        if len(days) == 1:
            oneday = days[0]
        else:
            oneday = 0
        if lastDate - oneday < 7:
            return 7 + random.randint(-1, 1)
        elif lastDate - oneday < 30:
            return 30 + random.randint(-2, 2)

        return 365 + random.randint(-5, 1)
    if len(days) < 4:
        summary = 0
        for i in range(1, len(days)):
            summary += days[i] - days[i - 1]
        summary /= len(days)
        return round(days[-1] + summary)

    buyDay = machineLearning(category, days, lastDate, firstDate)

    return buyDay


@app.post('/recommendations')
async def createRecomendationsTask(products: ProductsHistory):
    rn = random.randint(2 ** 50, 2 ** 63)
    category_to_buys = dict()
    min_date = datetime.datetime.now(tz=tz)
    last_date = datetime.datetime(1970, 1, 1, tzinfo=tz)
    for product in products.items:
        categ = product.category
        str_date = product.boughtOn
        if categ not in category_to_buys:
            category_to_buys[categ] = []
        splttd = list(map(int, str(str_date).split("-")))
        date = datetime.datetime(splttd[2], splttd[1], splttd[0], tzinfo=tz)
        min_date = min(date, min_date)
        last_date = max(date, last_date)
        category_to_buys[categ].append(date)
    last_day = (last_date - min_date).days
    answer = dict()
    for category, times in category_to_buys.items():
        times = list(map(lambda tm: (tm - min_date).days, times))
        dayFromStart = predictNextBuy(category, times, last_day, min_date)
        nextBuy = datetime.timedelta(dayFromStart) + min_date
        answer[category] = nextBuy
        print(nextBuy)
    taskResults[rn] = answer
    return rn
