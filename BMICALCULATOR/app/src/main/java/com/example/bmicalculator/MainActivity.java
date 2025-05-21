package com.example.bmicalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    EditText heightf, heighti, weight;
    Button bmicalculate, seeBMI;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        weight = findViewById(R.id.weight);
        heightf = findViewById(R.id.heightft);
        heighti = findViewById(R.id.heightin);
        bmicalculate = findViewById(R.id.bmicalculate);
        result = findViewById(R.id.result);
        seeBMI = findViewById(R.id.seeBMI);
    }

    void assign(Button btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View view) {
        int wt = Integer.parseInt(weight.getText().toString());
        int ft = Integer.parseInt(heightf.getText().toString());
        int in = Integer.parseInt(heighti.getText().toString());
        int totalin = ft * 12 + in;
        double totalcm = totalin * 2.54;
        double totalm = totalcm / 100;

        double bmi = wt / (totalm * totalm);
        String BMI = String.valueOf(bmi);

        Button button = (Button) view;
        String s = button.getText().toString();

        if (s.equals("Calculate")) {
            if (bmi <= 18) {
                result.setText("Under-Weight");
            }
            else if (bmi > 18 && bmi <= 25) {
                result.setText("You're Healthy");
            }
            else if (bmi > 25 && bmi <= 30) {
                result.setText("You're Over-weignt");
            }
            else if (bmi > 30 && bmi <= 35) {
                result.setText("You reached to the Obesity -> Class I");
            }
            else if (bmi > 35 && bmi <= 40) {
                result.setText("You're in Class II of Obesity");
            }
            else if (bmi > 40) {
                result.setText("You reached to Class III obesity (severe Obesity)");
            }
        }
        if (s.equals("SEE")) {
            result.setText(BMI);
        }
    }
}