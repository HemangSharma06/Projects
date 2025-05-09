import streamlit as st
import numpy as np
<<<<<<< HEAD
import joblib 
from pathlib import Path

=======
import joblib
from pathlib import Path


>>>>>>> bd941b4 (Updated files)
@st.cache_resource
def load_model():
    try:
        model_path = Path("fraud_rf_model.pkl")
        if not model_path.exists():
            st.error("Model file not found!")
            st.stop()
        return joblib.load(model_path)
    except Exception as e:
        st.error(f"Model loading failed: {str(e)}")
        st.stop()

<<<<<<< HEAD
=======

>>>>>>> bd941b4 (Updated files)
model = load_model()

st.title("Credit Card Fraud Detection")
st.markdown("Enter transaction details below to predict if it's fraudulent.")

n_features = model.n_features_in_ - 1
input_data = []
for i in range(1, n_features + 1):
    val = st.number_input(f"V{i}", value=0.0, format="%.5f")
    input_data.append(val)

amount = st.number_input("Amount", value=0.0, format="%.2f")
input_data.append(amount)

if st.button("Predict"):
    features = np.array(input_data).reshape(1, -1)
<<<<<<< HEAD
    
    try:
        prediction = model.predict(features)[0]
        prob = model.predict_proba(features)[0][1]
        
=======

    try:
        prediction = model.predict(features)[0]
        prob = model.predict_proba(features)[0][1]

>>>>>>> bd941b4 (Updated files)
        if prediction == 1:
            st.error(f"Fraudulent Transaction Detected! (Probability: {prob:.2%})")
        else:
            st.success(f"Legitimate Transaction (Probability of Fraud: {prob:.2%})")
    except Exception as e:
<<<<<<< HEAD
        st.error(f"Prediction failed: {str(e)}")
=======
        st.error(f"Prediction failed: {str(e)}")
>>>>>>> bd941b4 (Updated files)
