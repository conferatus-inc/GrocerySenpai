from pathlib import Path
from warnings import simplefilter

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

simplefilter("ignore")  # ignore warnings to clean up output cells

# Set Matplotlib defaults
plt.style.use("seaborn-whitegrid")
plt.rc("figure", autolayout=True, figsize=(11, 4))
plt.rc(
    "axes",
    labelweight="bold",
    labelsize="large",
    titleweight="bold",
    titlesize=14,
    titlepad=10,
)
plot_params = dict(
    color="0.75",
    style=".-",
    markeredgecolor="0.25",
    markerfacecolor="0.25",
    legend=False,
)
plot_paramsPred = dict(
    color="red",
    style=".-",
    # markeredgecolor="0.4",
    # markerfacecolor="0.4",
    legend=False,
)


# Load Tunnel Traffic dataset
data_dir = Path("./examples")
tunnel = pd.read_csv(data_dir / "tunnel.csv", parse_dates=["Day"])

# Create a time series in Pandas by setting the index to a date
# column. We parsed "Day" as a date type by using `parse_dates` when
# loading the data.
tunnel = tunnel.set_index("Day")

# By default, Pandas creates a `DatetimeIndex` with dtype `Timestamp`
# (equivalent to `np.datetime64`, representing a time series as a
# sequence of measurements taken at single moments. A `PeriodIndex`,
# on the other hand, represents a time series as a sequence of
# quantities accumulated over periods of time. Periods are often
# easier to work with, so that's what we'll use in this course.
tunnel = tunnel.to_period()

df = tunnel.copy()
df['Time'] = np.arange(len(tunnel.index))

df['Lag_1'] = df['NumVehicles'].shift(1)
# df['Lag_2'] = df['NumVehicles'].shift(5)
# df['Lag_3'] = df['NumVehicles'].shift(20)
# df['Lag_4'] = df['NumVehicles'].shift(50)

from sklearn.linear_model import LinearRegression
from sklearn.ensemble import GradientBoostingRegressor

X = df.loc[:, ['Lag_1']]
X.dropna(inplace=True)  # drop missing values in the feature set
print(X.head())
y = df.loc[:, 'NumVehicles']  # create the target
y, X = y.align(X, join='inner')  # drop corresponding values in target
print(X.head())

X_train = X
y_train = y

tr = 100
pr = 150
X_train = X[:tr]
y_train = y[:tr]
model = LinearRegression()
model.fit(X_train, y_train)

y_pred = pd.Series(model.predict(X[1:pr+1]), index=X[:pr].index)
fig, ax = plt.subplots()

ax = y[:pr].plot(**plot_params)
ax = y_pred[:tr].plot()
ax = y_pred[tr:pr].plot(**plot_paramsPred)
print(X.iloc[pr])
plt.show()
# print(df.head())