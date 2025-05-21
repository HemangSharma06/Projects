# 📧 Email/Chat/SMS Spam Detector

This project is a **full-stack machine learning application** designed to detect spam messages across platforms such as **email, SMS, and chat**. It combines **Natural Language Processing (NLP)** for text preprocessing, the powerful **XGBoost** classifier for message classification, and a web interface built using **Flask, HTML, and CSS**.

---

## 💡 Project Overview

Spam messages are not only a nuisance but also a potential security risk in modern communication systems. This project aims to create a **lightweight yet effective spam detection tool** using machine learning.

The model is trained on a labeled dataset of real-world messages and learns to detect patterns, keywords, and features that typically indicate spam content.

---

## ✨ Features

### 🧠 Machine Learning & NLP

#### 🗂️ Natural Language Processing:

- Text normalization  
- Lowercasing  
- Punctuation and stopword removal  
- Tokenization and stemming  
- Text vectorization using **TF-IDF**

#### 🤖 Model:

- Trained using **XGBoost (Extreme Gradient Boosting)**
- Tuned for high **accuracy**, **precision**, and **recall**
- Performs **binary classification**: `spam` or `not spam`

---

### 🎨 Frontend

- Built using **HTML5** and **CSS3**
- Clean and minimal UI for ease of use
- Simple message input field
- Real-time spam classification display

---

### 🖥️ Backend

- Developed with **Flask** (Python micro web framework)
- Handles HTTP requests and form data
- Interfaces with the trained ML model for prediction
- Displays results directly in the web app

---

## 🛠️ Technologies Used

| Area             | Tools / Libraries               |
|------------------|---------------------------------|
| Programming      | Python, HTML, CSS               |
| Web Framework    | Flask                           |
| Machine Learning | XGBoost, Scikit-learn           |
| NLP              | NLTK / spaCy, Scikit-learn      |
| Deployment       | Flask local server (cloud-ready)|

---

## 🔄 How It Works

1. User enters a message into the input form.
2. Flask backend receives the input.
3. NLP pipeline processes the text.
4. Preprocessed text is sent to the **XGBoost** model.
5. The model returns a classification: `Spam` or `Not Spam`.
6. The result is rendered instantly on the web interface.

---

## 📊 Evaluation Metrics

- **Accuracy**
- **Precision**
- **Recall**
- **F1 Score**
- Visualization tools used during training:
  - **Confusion Matrix**
  - **ROC Curve**

---

## 📁 Repository

Explore the full source code here:  
🔗 [GitHub Repository](https://github.com/HemangSharma06/Projects/tree/main/Email%20Spam%20Detector)

### Includes:

- `app.py` – Flask backend logic  
- `templates/` – HTML frontend templates  
- `static/` – CSS styling and assets  
- `model.pkl` – Trained XGBoost model  
- Dataset & preprocessing scripts

---

## 🚀 Future Improvements

- 🌐 Support for multilingual message detection  
- ☁️ Deployment on platforms like Render, Railway, or Heroku  
- 🔁 REST API integration  
- 📄 File upload support for bulk message classification  
- 📱 Mobile-responsive interface
