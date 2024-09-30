import numpy as np
from pathlib import Path
from warnings import simplefilter

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

from sklearn.linear_model import LinearRegression
from sklearn.ensemble import GradientBoostingRegressor
from statsmodels.tsa.deterministic import CalendarFourier, DeterministicProcess

simplefilter("ignore")

# Set Matplotlib defaults
plt.style.use("seaborn-whitegrid")
plt.rc("figure", autolayout=True, figsize=(11, 5))
plt.rc(
    "axes",
    labelweight="bold",
    labelsize="large",
    titleweight="bold",
    titlesize=16,
    titlepad=10,
)
plot_params = dict(
    color="0.75",
    style=".-",
    markeredgecolor="0.25",
    markerfacecolor="0.25",
    legend=False,
)


def seasonal_plot(X, y, period, freq, ax=None):
    if ax is None:
        _, ax = plt.subplots()
    palette = sns.color_palette("husl", n_colors=X[period].nunique(),)
    ax = sns.lineplot(
        x=freq,
        y=y,
        hue=period,
        data=X,
        ci=False,
        ax=ax,
        palette=palette,
        legend=False,
    )
    ax.set_title(f"Seasonal Plot ({period}/{freq})")
    for line, name in zip(ax.lines, X[period].unique()):
        y_ = line.get_ydata()[-1]
        ax.annotate(
            name,
            xy=(1, y_),
            xytext=(6, 0),
            color=line.get_color(),
            xycoords=ax.get_yaxis_transform(),
            textcoords="offset points",
            size=14,
            va="center",
        )
    return ax


# def plot_periodogram(ts, detrend='linear', ax=None):
#     from scipy.signal import periodogram
#     fs = pd.Timedelta("365D") / pd.Timedelta("1D")
#     freqencies, spectrum = periodogram(
#         ts,
#         fs=fs,
#         detrend=detrend,
#         window="boxcar",
#         scaling='spectrum',
#     )
#     if ax is None:
#         _, ax = plt.subplots()
#     ax.step(freqencies, spectrum, color="purple")
#     ax.set_xscale("log")
#     ax.set_xticks([1, 2, 4, 6, 12, 26, 52, 104])
#     ax.set_xticklabels(
#         [
#             "Annual (1)",
#             "Semiannual (2)",
#             "Quarterly (4)",
#             "Bimonthly (6)",
#             "Monthly (12)",
#             "Biweekly (26)",
#             "Weekly (52)",
#             "Semiweekly (104)",
#         ],
#         rotation=30,
#     )
#     ax.ticklabel_format(axis="y", style="sci", scilimits=(0, 0))
#     ax.set_ylabel("Variance")
#     ax.set_title("Periodogram")
#     return ax


data_dir = Path("./examples")
tunnel = pd.read_csv(data_dir / "tunnel.csv", parse_dates=["Day"])
tunnel = tunnel.set_index("Day").to_period("D")
# Compute Fourier features to the 4th order (8 new features) for a
# series y with daily observations and annual seasonality:
#

X = tunnel.copy()

# days within a week
X["day"] = X.index.dayofweek  # the x-axis (freq)
X["week"] = X.index.week  # the seasonal period (period)

# days within a year
X["dayofyear"] = X.index.dayofyear
X["year"] = X.index.year

from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor
from statsmodels.tsa.deterministic import CalendarFourier, DeterministicProcess
from sklearn.metrics import r2_score
fourier = CalendarFourier(freq="A", order=15)  # 10 sin/cos pairs for "A"nnual seasonality
from sklearn import svm



dp = DeterministicProcess(
    index=tunnel.index,
    constant=True,               # dummy feature for bias (y-intercept)
    order=1,                     # trend (order 1 means linear)
    seasonal=True,               # weekly seasonality (indicators)
    additional_terms=[fourier],  # annual seasonality (fourier)
    drop=True,                   # drop terms to avoid collinearity
)
print(X)

X = dp.in_sample()  # create features for dates in tunnel.index
print(X)
y = tunnel["NumVehicles"]


amn = 500
X_train = X[:amn]
y_train = y[:amn]
X_pred = X[amn:]
y_other = y[amn:]
# 0.8325841632097799 0.6451343808135148
# 0.96974650974769 0.34581561598959276
from sklearn.preprocessing import StandardScaler
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_pred)
print(list(X_train))
print(list(y_train))
print("?"*10)
model = svm.SVR()
_ = model.fit(X_train_scaled, y_train)



y_pred = pd.Series(model.predict(X_test_scaled), index=y_other.index)
y_train_pred = pd.Series(model.predict(X_train_scaled), index=y_train.index)
print(r2_score(y_train, y_train_pred), r2_score(y_other, y_pred))

ax = y.plot(color='0.25', style='.', title="Tunnel Traffic - Seasonal Forecast")
ax = y_train_pred.plot(ax=ax, label="Seasonal")
ax = y_pred.plot(ax=ax, label="SeasonalPred", color='C3')
_ = ax.legend()
plt.show()

5
10
15