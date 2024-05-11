package com.example.expensetracker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class home_screen extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPass;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth=FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressBar);


        loginDetails();
    }

    private void loginDetails() {
        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView mforget_password = findViewById(R.id.forgot_password);
        TextView mSignUpHere = findViewById(R.id.signup_reg);
        CheckBox remember=findViewById(R.id.checkBox2);

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        if(checkbox.equals("true"))
        {
            Intent intent=new Intent(home_screen.this,first_home_page.class);
            startActivity(intent);
            finish();

        }

        remember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", isChecked ? "true" : "false");
            editor.apply();
            Toast.makeText(home_screen.this, isChecked ? "Remember me Checked.." : "Remember me Unchecked..", Toast.LENGTH_SHORT).show();
        });

        mSignUpHere.setOnClickListener(v -> {
            Intent intent = new Intent(home_screen.this, Registration.class);
            startActivity(intent);
        });

        mforget_password.setOnClickListener(v -> {
            Intent intent = new Intent(home_screen.this, resetpassword.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String pass = mPass.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email Required..", null);
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                mPass.setError("Password Required..", null);
                return;
            }


            mProgressBar.setVisibility(View.VISIBLE);


            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    checkEmailVerification();
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed..", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        });


    }
    private void checkEmailVerification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            boolean emailVerified = firebaseUser.isEmailVerified();
            if (emailVerified) {
                Toast.makeText(getApplicationContext(), "Login Successful..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(home_screen.this, first_home_page.class));
                finish();// Assuming FirstHomePage is the correct activity
            } else {
                Toast.makeText(this, "Please verify your email..", Toast.LENGTH_LONG).show();
                mAuth.signOut();
            }
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

}
