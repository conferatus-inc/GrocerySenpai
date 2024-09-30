import pandas as pd

data = pd.read_csv("./files/out2.csv")


print(data.groupby(['user_id'])['user_id'].count())
print(data.groupby(['user_id', 'order_number'])['user_id'].count())
print(data.groupby(['user_id', 'order_number']).count())