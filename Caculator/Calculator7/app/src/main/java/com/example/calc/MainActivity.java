package com.example.calc;

import static com.example.calc.R.*;
import java.lang.Math;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result,solution;
    MaterialButton add,subtract,divide,multiply,dot,clr,del,lbkt,rbkt,eql,btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        result = findViewById(id.result);
        solution = findViewById(id.solution);


        assign(add,R.id.add);
        assign(subtract,R.id.subtract);
        assign(multiply,R.id.multiply);
        assign(divide,R.id.divide);
        assign(dot,R.id.dot);
        assign(clr,R.id.clear);
        assign(del,R.id.delete);
        assign(lbkt,R.id.left);
        assign(rbkt,R.id.right);
        assign(eql, id.zerozero);
        assign(btn0,R.id.btn_0);
        assign(btn1,R.id.btn_1);
        assign(btn2,R.id.btn_2);
        assign(btn3,R.id.btn_3);
        assign(btn4,R.id.btn_4);
        assign(btn5,R.id.btn_5);
        assign(btn6,R.id.btn_6);
        assign(btn7,R.id.btn_7);
        assign(btn8,R.id.btn_8);
        assign(btn9,R.id.btn_9);
    }

    void assign(MaterialButton btn,int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        TextView result = findViewById(id.result),solution = findViewById(id.solution);
        MaterialButton button = (MaterialButton) view;
        String s = button.getText().toString();
        String data = solution.getText().toString();
        if (s.equals("AC")){
            solution.setText("");
            result.setText("0");
            return;
        }
        if (s.equals("del")){
            if (data.length() == 1){
                solution.setText("");
                result.setText("0");
                return;
            }
            data = data.substring(0,data.length()-1);

        }
        else{
            data = data + s;
        }
        solution.setText(data);
        String f = getResult(data);
        if (!f.equals("Err")){
            result.setText(f);
        }
    }
    String getResult(String data){
        try {
            Context c = Context.enter();
            c.setOptimizationLevel(-1);
            Scriptable s = c.initStandardObjects();
            String fin = c.evaluateString(s,data,"Javascript",1,null).toString();
            if (fin.endsWith(".0")){
                fin = fin.replace(".0","");
            }
            return  fin;
        }catch (Exception e){
            return "Err";
        }
    }
}