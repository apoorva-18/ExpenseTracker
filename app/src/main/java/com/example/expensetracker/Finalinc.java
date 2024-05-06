package com.example.expensetracker;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Finalinc extends AppCompatActivity {
    TextView T1,T2,T3,T4,T6,T7;
    Button back,backmain;
    String base,LTA,HRA,SA, A,B,C;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finalinc);
        back = findViewById(R.id.goback); // Remove the unnecessary casting
        backmain = findViewById(R.id.backtomain);
        backmain.setOnClickListener(v -> {
            Intent i = new Intent(Finalinc.this, first_home_page.class);
            startActivity(i);
        });

        back.setOnClickListener(v -> {
            Intent i = new Intent(Finalinc.this, income2.class);
            startActivity(i);
        });

        T1 = findViewById(R.id.t1);
        T2 = findViewById(R.id.t2);
        T3 = findViewById(R.id.t3);
        T4 = findViewById(R.id.t4);
        T6 = findViewById(R.id.t6);
        T7 = findViewById(R.id.t7);
        Intent i=getIntent();
        HRA=i.getStringExtra("hra");
        LTA=i.getStringExtra("lta");
        base=i.getStringExtra("base");
        SA=i.getStringExtra("sa");
        A=i.getStringExtra("80A");
        B=i.getStringExtra("80B");
        C=i.getStringExtra("80C");
        int baseInt=Integer.parseInt(base);
        double HRAdouble=Double.parseDouble(HRA);
        double LTAdouble=Double.parseDouble(LTA);
        double SAdouble=Double.parseDouble(SA);
        double Adouble=Double.parseDouble(A);
        double Bdouble=Double.parseDouble(B);
        double Cdouble=Double.parseDouble(C);
        double totalIncome = baseInt + HRAdouble + LTAdouble + SAdouble;
        T1.setText(String.valueOf(base));

        int forT2 = (int) (HRAdouble + LTAdouble + SAdouble);
        T2.setText(String.valueOf(forT2));

        int totalG = baseInt + (int) (HRAdouble + LTAdouble + SAdouble);
        T3.setText(String.valueOf(totalG));

        double ded = Adouble + Bdouble + Cdouble;
        T4.setText(String.valueOf(ded));

        double grossdeduction = ded + 50000;
        int totaltaxable = (int) (totalIncome - grossdeduction);
        T6.setText(String.valueOf(totaltaxable));

        double tax = 0;
        if (totaltaxable < 250000) {
            tax = 0;
        } else if (totaltaxable > 250000 && totaltaxable < 500000) {
            tax = 0.05 * totaltaxable;

        } else if (totaltaxable > 500000 && totaltaxable < 1000000) {
            tax = 0.20 * totaltaxable;
        } else if (totaltaxable > 1000000) {
            tax = 0.30 * totaltaxable;
        }
        T7.setText(getString(R.string.total_tax, tax));
    }
}
