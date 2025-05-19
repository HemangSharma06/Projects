import warnings
warnings.filterwarnings("ignore")
from flask import Flask, render_template, request, url_for, redirect
import joblib
import numpy as np
from datetime import datetime as dt
import yfinance as yf
import pandas as pd 
import os
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

app = Flask(__name__)
model = joblib.load('model1.pkl')
le = joblib.load('label_encoder_model.pkl')
def get_items(name, date, end):
    data = yf.download(name, start=date, end=end)
    if data.empty:
        return None
    
    row = data.iloc[-1]
    return [float(row['Open']), float(row['High']), float(row['Low']), float(row['Volume'])]

@app.route('/home')
def home():
    return render_template('home.html')

@app.route('/plot', methods=['POST'])
def plot():
    syb = request.form['symbol']
    start = (request.form['start'])
    end = (request.form['end'])
    start_date = dt.strptime(start, '%Y-%m-%d')
    end_date = dt.strptime(end, '%Y-%m-%d')
    
    start_year = start_date.year
    end_year = end_date.year
    
    if syb not in le.classes_:
        return render_template('error.html')

    encoded = le.transform([syb])[0]
    
    future_dates = pd.date_range(start=f'{start_year}-01-01', end=f'{end_year}-12-31', freq='YS')
    predictions = []
    years = []

    for date in future_dates:
        data = yf.download(syb, start=date, end=(date + pd.Timedelta(days=10)))
        data = data.dropna()
        if data.empty:
            continue
        
        row = data.iloc[-1]
        open_, high, low, volume = row['Open'].item(), row['High'].item(), row['Low'].item(), row['Volume'].item()
        day, month, year = date.day, date.month, date.year

        features = np.array([[open_, high, low, volume, encoded, year, month, day]])
        pred = model.predict(features)
        predictions.append(pred[0])
        years.append(year)

    plt.figure(figsize=(15, 8))
    plt.plot(years, predictions, marker='o', linestyle='-', color='red')
    plt.title(f"{syb} Stock Predictions ({start_year} to {end_year})")
    plt.xlabel("Year")
    plt.ylabel("Predicted Price")
    plt.grid(True)
    plot_path = os.path.join('static', 'plot.png')
    plt.savefig(plot_path)
    plt.close()

    return render_template('plot.html', name=syb, plot_url=plot_path)

@app.route('/predict', methods = ['POST'])
def predict():
    syb = request.form['symbol']
    st = request.form['start']
    end = request.form['end']
    
    date_obj = dt.strptime(st, '%Y-%m-%d')
    features = get_items(syb, st, end)
    
    if syb not in le.classes_ or features is None:
       return render_template('error.html')

    
    name = syb
    day = date_obj.day
    month = date_obj.month
    year = date_obj.year
    encoded = le.transform([name])[0]
    
    features = np.array([features + [encoded, year, month, day]])
    result = model.predict(features)
    return render_template('result.html', result = result, end = end, name = syb)


if __name__ == "__main__":
    app.run(debug=True)