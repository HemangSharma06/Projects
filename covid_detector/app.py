from flask import Flask, render_template, request, redirect
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns 
import joblib

app = Flask(__name__)
le = joblib.load('label_encoder.pkl')
model = joblib.load('model.pkl')


def feature(Breathing_Problem = 0,	Fever = 0,	Dry_Cough = 0,	
            Sore_throat = 0, Running_Nose = 0, Asthma = 0, Chronic_Lung_Disease = 0,
            Headache = 0,Heart_Disease = 0, Diabetes = 0, Hyper_Tension = 0,
            Fatigue = 0, Gastrointestinal = 0,	Abroad_travel = 0,
            Contact_with_COVID_Patient = 0,	Attended_Large_Gathering = 0,
            Visited_Public_Exposed_Places = 0, Family_working_in_Public_Exposed_Places = 0,
            Wearing_Masks=0, Sanitization_from_Market = 0):

    Features = [[Breathing_Problem, Fever, Dry_Cough, Sore_throat, Running_Nose,
            Asthma, Chronic_Lung_Disease, Headache, Heart_Disease, Diabetes,
            Hyper_Tension, Fatigue, Gastrointestinal, Abroad_travel, 
            Contact_with_COVID_Patient, Attended_Large_Gathering, 
            Visited_Public_Exposed_Places, Family_working_in_Public_Exposed_Places, 
            Wearing_Masks, Sanitization_from_Market]]
    
    prediction = model.predict(Features)
    result = "Negative" if prediction == 0 else "Positive"
    return result

@app.route('/')
def home():
    return render_template('home.html')



@app.route('/predict', methods=['POST'])
def predict():    
    result = feature(Breathing_Problem=int(request.form["Q1"]), 
                    Fever=int(request.form["Q2"]),
                    Dry_Cough=int(request.form["Q3"]), 
                    Sore_throat=int(request.form["Q4"]),
                    Running_Nose=int(request.form["Q5"]), 
                    Asthma=int(request.form["Q6"]),
                    Chronic_Lung_Disease=int(request.form["Q7"]), 
                    Headache=int(request.form["Q8"]),
                    Heart_Disease=int(request.form["Q9"]), 
                    Diabetes=int(request.form["Q10"]),
                    Hyper_Tension=int(request.form["Q11"]), 
                    Fatigue=int(request.form["Q12"]),
                    Gastrointestinal=int(request.form["Q13"]), 
                    Abroad_travel=int(request.form["Q14"]),
                    Contact_with_COVID_Patient=int(request.form["Q15"]), 
                    Attended_Large_Gathering=int(request.form["Q16"]),
                    Visited_Public_Exposed_Places=int(request.form["Q17"]), 
                    Family_working_in_Public_Exposed_Places=int(request.form["Q18"]),
                    Wearing_Masks=int(request.form["Q19"]), 
                    Sanitization_from_Market=int(request.form["Q20"]))
    
    return render_template('result.html', result = result)



if __name__ == "__main__":
    app.run(debug=True, port=7670)