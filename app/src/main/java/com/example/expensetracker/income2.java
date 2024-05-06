package com.example.expensetracker;

import android.content.Intent;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class income2 extends AppCompatActivity {

    EditText bs;
    EditText hra;
    EditText sa;
    EditText lta;
    TextView T1,T2,T3,T4,T5;

    TextView tx1;
    Button b1;
    Button con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income2);
        con=findViewById(R.id.next);
        bs=findViewById(R.id.edit1);
        hra=findViewById(R.id.edit2);
        sa=findViewById(R.id.edit3);
        lta=findViewById(R.id.edit4);
        T1=findViewById(R.id.t1);
        T2=findViewById(R.id.t2);
        T3=findViewById(R.id.t3);
        T4=findViewById(R.id.t4);
        T5=findViewById(R.id.t5);


        tx1=findViewById(R.id.text3);
        b1=findViewById(R.id.go);
        // Using lambda expressions
        con.setOnClickListener(v -> calculate());

        b1.setOnClickListener(v -> {
            try {
                int HRA = Integer.parseInt(hra.getText().toString());
                int LTAc = Integer.parseInt(lta.getText().toString());
                double calcHRA = 0.36 * HRA;
                double calcLTA = 0.12 * LTAc;

                String bsText = getString(R.string.text_bs, bs.getText().toString());
                String calcHRAText = getString(R.string.text_calc_hra, String.valueOf(calcHRA));
                String saText = getString(R.string.text_sa, sa.getText().toString());
                String calcLTAText = getString(R.string.text_calc_lta, String.valueOf(calcLTA));
                String t5Text = getString(R.string.text_t5, "50000");

                T1.setText(bsText);
                T2.setText(calcHRAText);
                T3.setText(saText);
                T4.setText(calcLTAText);
                T5.setText(t5Text);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "An Error Occurred, Please try again!", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void calculate()
    {
        Intent i=new Intent(income2.this,NextIncome.class);
        i.putExtra("baseincome",bs.getText().toString());
        i.putExtra("HRA",T2.getText().toString());
        i.putExtra("SA",sa.getText().toString());
        i.putExtra("LTA",lta.getText().toString());
        startActivity(i);

    }
}