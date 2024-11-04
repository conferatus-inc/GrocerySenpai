from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from typing import List
from pydantic import BaseModel


app = FastAPI()

origins = [
    'http://localhost',
]

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

@app.post('/recommendations')
async def createRecomendationsTask(products: ProductsHistory):
    print(products)
    return 5

@app.get('/result/{taskId}')
async def returnResult(taskId: int, limit: int = 100):
    print(f'taskId is {taskId}')
    print(f'limit is {limit}')

    return {
        "items": [
            {
                "category": "Пиво",
                "nextBuy": "22-12-2023"
            },
            {
                "category": "Ром",
                "nextBuy": "28-12-2023"
            }
        ]
    }
