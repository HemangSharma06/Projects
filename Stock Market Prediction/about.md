# 📈 Stock Market Prediction Web App

## 🧠 About the Project

This is a **Stock Market Prediction Web Application** developed using **Machine Learning algorithms** such as **Linear Regression**. It allows users to input stock symbols and date ranges, fetches historical market data, and predicts future stock prices.

The backend is powered by **Python and Flask**, while the frontend is built using **HTML5 and CSS3**.

---

## 🚀 Features

- 📊 Fetches historical stock data from Yahoo Finance
- 🧮 Predicts future stock prices using a trained Linear Regression model
- 🌐 Clean and interactive web interface using Flask
- 🎨 Custom-designed UI with HTML and CSS
- 📈 Displays original and predicted price data for comparison

---

## ⚙️ How It Works

1. **User Input**  
   Users enter the stock symbol (e.g., `AAPL`, `TSLA`) and choose a date range.

2. **Data Fetching**  
   Real-time historical data is fetched using the `yfinance` API.

3. **Feature Extraction & Encoding**  
   Features like opening price, closing price, volume, etc., are extracted and preprocessed.

4. **Prediction**  
   A trained Linear Regression model (`model.pkl`) predicts future prices.

5. **Display Results**  
   Results are shown on a new page with key values and simple visual feedback.

---

## 🛠️ Tech Stack

- **Frontend**: HTML5, CSS3
- **Backend**: Python, Flask
- **Machine Learning**: Scikit-learn (Linear Regression)
- **Data Source**: Yahoo Finance (via `yfinance` Python library)
- **Deployment**: Flask local server, `.pkl` model loading

---

## 📂 Project Structure

- 📁 stock-market-prediction/
- ├── app.py # Main Flask application
- ├── templates/
- │ ├── home.html # Homepage
- │ ├── result.html # Results page
- │ └── error.html # Error handling
- ├── static/
- │ └── style.css # CSS file
- ├── model.pkl # Trained ML model
- ├── label_encoder.pkl # Optional: Label encoder for stock symbols
- └── about.md # Project documentation

---

## 📌 Future Enhancements

- Integrate deep learning models like LSTM for time series
- Visualize results using Plotly or Matplotlib
- Add sentiment analysis based on financial news
- Enable multi-stock comparison and dashboard view

---
