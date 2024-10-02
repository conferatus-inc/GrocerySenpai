from selenium import webdriver
from selenium.webdriver import ActionChains
from selenium.webdriver import ChromeOptions
from fastapi import FastAPI
import time
from fastapi.middleware.cors import CORSMiddleware
from selenium.webdriver.common.by import By
txt = """1  Ананасы
2  Апельсины
3  Базилик
4  Бананы
5  Баранина
6  Брокколи
7  Булка
8  Вареники
9  Вино
10  Виноград
11  Виски
12  Витамины
13  Вода
14  Водка
15  Газировка
16  Говядина
17  Голубика
18  Горох
19  Горчица
20  Гранат
21  Грейпфрут
22  Гречка
23  Груши
24  Доширак
25  Зеленый чай
26  Индейка
27  Йогурт
28  Кальмары
29  Картошка
30  Квас
31  Кетчуп
32  Киви
33  Киноа
34  Клубника
35  Коктейль
36  Колбаса
37  Консервы
38  Конфеты
39  Кофе
40  Креветки
41  Кролик
42  Курица
43  Лайм
44  Лапша
45  Ликер
46  Лимон
47  Лимонад
48  Лосось
49  Лук
50  Макароны
51  Малина
52  Манго
53  Масло
54  Масло сливочное
55  Мидии
56  Минеральная вода
57  Молоко
58  Морковь
59  Мороженое
60  Морс
61  Мука
62  Мюсли
63  Мята
64  Овсянка
65  Огурцы
66  Оливковое масло
67  Орехи
68  Пельмени
69  Перец
70  Перловка
71  Петрушка
72  Печенье
73  Пиво
74  Пирог
75  Плавленный сыр
76  Помидоры
77  Приправы
78  Протеин
79  Пшено
80  Растительное масло
81  Рис
82  Ром
83  Руккола
84  Рыба
85  Свинина
86  Сельдь
87  Сметана
88  Соевый соус
89  Сок
90  Сосиски
91  Специи
92  Сыр
93  Творог
94  Торт
95  Торты
96  Тофу
97  Тунец
98  Укроп
99  Устрицы
100  Фасоль
101  Форель
102  Хлеб
103  Хлопья
104  Чай
105  Черника
106  Черный чай
107  Чеснок
108  Чечевица
109  Шоколад
110  Шпинат
111  Энергетический напиток
112  Яблоки
""".split("\n")
categories = list(map(lambda k: " ".join(k.split()[1:]), txt))
print(categories)

def subStr(word:str, categ:str):
    word = word.lower()
    categ = categ.lower()
    res = 0
    res += (word in categ) + (categ in word)
    res*=50
    word_len = len(word)
    categ_len = len(categ)
    if word_len>1:
        res += (word[::len(word)-1] in categ)
    if categ_len>1:
        res += (categ[::len(categ)-1] in word)
    res*=5
    if word_len>2:
        res += (word[::len(word) - 2] in categ)
    if categ_len>2:
        res += (categ[::len(categ) - 2] in word)
    return res

def maybeCategory(word: str):
    ls = list(map(lambda categ: (subStr(word, categ),categ), categories))
    sorted = max(ls, key=lambda k: k[0])
    return sorted

def maybeStrCategory(words: str):
    words = words.split()
    ls = list(map(maybeCategory, words))
    mx = max(ls, key=lambda k: k[0])
    val, categ = mx
    return categ


def getProducts(raw_qr_code, browser_driver, text_field, button_element):
    ActionChains(browser_driver) \
        .scroll_to_element(text_field) \
        .scroll_by_amount(0, 300) \
        .perform()
    text_field.clear()
    text_field.send_keys(raw_qr_code)
    time.sleep(0.4)
    button_element.click()
    time.sleep(2)
    products = browser_driver.find_elements(By.XPATH, "//div[@class='b-check_place']//tr[@class='b-check_item']")
    date = browser_driver.find_element(By.XPATH, "//div[@class='b-check_place']//tr[5]/td").text
    product_list = []
    for product in products:
        product_props = product.find_elements(By.XPATH, ".//td")
        tmp = [y for y in (map(lambda x: x.text, product_props))]
        lst = tmp[-1]
        name = tmp[1]
        answer = {"category":maybeStrCategory(name), "amount":float(tmp[-2])}
        if any(map(lambda k: k.isdigit(), lst)):
            pass
        product_list.append(answer)
    return product_list, date


class QrCodeResolver:
    def __init__(self):
        print("creating")
        options = ChromeOptions()
        # options.add_argument("--headless=new")
        self.driver = webdriver.Chrome(options=options)
        self.driver.get('https://proverkacheka.com/')
        self.rawQrCodeTab = self.driver.find_element(By.XPATH, "//ul[@class='b-checkform_nav nav nav-tabs']/li[4]")
        self.rawQrCodeTab.click()
        time.sleep(0.4)
        self.textField = self.driver.find_element(By.ID, "b-checkform_qrraw")

        self.button = \
            self.driver.find_element(By.XPATH,
                                     "//div[@class='b-checkform_tab-qrraw tab-pane fade active in']//div[@class='b-checkform_btn col-sm-12']"
                                     "/button[@class='b-checkform_btn-send btn btn-primary btn-sm pull-right']")
        time.sleep(2)

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.driver.close()


resolver = QrCodeResolver()
app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*'],
)

"""
http://127.0.0.1:8000/api/qr_code/
http://127.0.0.1:8000/api/qr_code/t=20201017T1923&s=1498.00&fn=9282440300669857&i=25151&fp=1186123459&n=1
http://127.0.0.1:8000/api/qr_code/t=20230915T141418&s=160.00&fn=7281440500848943&i=10145&fp=2768303418&n=1
http://127.0.0.1:8000/api/qr_code/t=20231102T1300&s=1600.17&fn=7281440500391730&i=28454&fp=4269454003&n=1
"""


@app.get('/api/qr_code/{raw_qr}')
def getQrData(raw_qr: str):
    print("qwe", raw_qr)
    prods, date = getProducts(raw_qr, resolver.driver, resolver.textField, resolver.button)
    return {'products': prods, 'date': date}
