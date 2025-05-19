from flask import Flask, render_template, redirect, request
import joblib
import pandas as pd
import numpy as np

app = Flask(__name__)

model = joblib.load('model.pkl')
le = joblib.load('label_encoder.pkl')
vect = joblib.load('text_clean.pkl')

@app.route('/home')
def home():
    return render_template('home.html')

@app.route('/predict', methods = ['POST'])
def predict():
    text = request.form['chat']
    text = vect.transform([text])
    result = model.predict(text)
    if result[0] not in [0, 1]:
        return render_template('error.html')
    result = (result[0] == 0)
    if not result:
        result = "Invalid"
    else:
        result = "Valid"
        
    return render_template('result.html', result = result)

if __name__ == "__main__":
    app.run(debug=True, port= 1000)
    