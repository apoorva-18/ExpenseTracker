package com.example.expensetracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {

    public resetpassword() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        EditText passwordEmail=findViewById(R.id.pass_email);
        Button resetpassword=findViewById(R.id.reset_pass);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(v -> {
            String useremail = passwordEmail.getText().toString().trim();

            if (useremail.equals("")) {
                passwordEmail.setError("Email Required...");

            } else {
                firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(resetpassword.this, "Email sent successfully..", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(resetpassword.this, home_screen.class));
                    } else {
                        Toast.makeText(resetpassword.this, "Error sending email..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
