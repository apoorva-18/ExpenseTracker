package com.example.expensetracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class change_password extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        Button changePass = findViewById(R.id.btnChange);


        EditText oldPass=findViewById(R.id.old_pass);
        EditText NewPass=findViewById(R.id.new_pass);

        changePass.setOnClickListener(v -> {
            String oldString = oldPass.getText().toString().trim();
            String newString = NewPass.getText().toString().trim();

            if (TextUtils.isEmpty(oldString)) {
                oldPass.setError("Required Field..", null);
                return;
            }
            if (TextUtils.isEmpty(newString)) {
                NewPass.setError("Required Field..", null);
                return;
            }

            if (newString.length() < 6) {
                NewPass.setError("Must contain at least 6 characters..", null);
                return;
            }


            if (user != null && user.getEmail() != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldString);
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newString).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(change_password.this, "Password changed successfully..", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(change_password.this, home_screen.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(change_password.this, "Something went wrong. Please try again later..", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });


        ImageView back_arrow=findViewById(R.id.back);
        back_arrow.setOnClickListener(v -> finish());

    }
}