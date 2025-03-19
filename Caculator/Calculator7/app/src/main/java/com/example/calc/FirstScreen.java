package com.example.calc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent next = new Intent(FirstScreen.this, MainActivity.class);
                startActivity(next);
                finish();
            }
        },1000);
    }
}