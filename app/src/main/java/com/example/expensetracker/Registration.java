package com.example.expensetracker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPass;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressBar);

        registration();
    }
    private void registration() {
        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.password_reg);
        TextView mSignin = findViewById(R.id.signin_here);

        mSignin.setOnClickListener(v -> {
            Intent intent = new Intent(Registration.this, home_screen.class);
            startActivity(intent);
            finish();
        });


        Button btnReg = findViewById(R.id.btn_reg);  // Declared here and used inside OnClickListener
        btnReg.setOnClickListener(view -> {
            String email = mEmail.getText().toString().trim();
            String pass = mPass.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email required..", null);
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                mPass.setError("Password required..", null);
                return;
            }
            if (pass.length() < 6) {
                mPass.setError("Must contain at least 6 characters..", null);
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mProgressBar.setVisibility(View.GONE);
                    sendEmailVerification();
                    mEmail = findViewById(R.id.email_reg);
                    String newEmail = mEmail.getText().toString().trim();
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    if (mUser != null) {
                        String uid = mUser.getUid();
                        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
                        DatabaseReference userNameRef = myRootRef.child("Email");
                        userNameRef.setValue(newEmail);
                    }
                } else {
                    mProgressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Registration failed..", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Registration.this, "Registration Successful. Verification mail sent successfully..", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    startActivity(new Intent(Registration.this, home_screen.class));
                    finish();
                } else {
                    Toast.makeText(Registration.this, "Error occurred sending verification mail..", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}