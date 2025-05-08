import streamlit as st
import numpy as np
try:
    import joblib
except ImportError:
    import os
    os.system('pip install joblib')
    import joblib
model = joblib.load("fraud_rf_model.pkl")

st.title("Credit Card Fraud Detection")
st.markdown("Enter transaction details below to predict if it's fraudulent.")

input_data = []
for i in range(1, 30):
    val = st.number_input(f"V{i}", value=0.0, format="%.5f")
    input_data.append(val)

amount = st.number_input("Amount", value=0.0, format="%.2f")
input_data.append(amount)

if st.button("Predict"):
    features = np.array(input_data).reshape(1, -1)
    prediction = model.predict(features)[0]
    prob = model.predict_proba(features)[0][1]

    if prediction == 1:
        st.error(f"Fraudulent Transaction Detected! (Probability: {prob:.2%})")
    else:
        st.success(f"Legitimate Transaction (Probability of Fraud: {prob:.2%})")
