package com.example.expensetracker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference mUserInfoDatabase = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object emailObject = dataSnapshot.child("Email").getValue();
                    if (emailObject != null) {
                        String userEmail = emailObject.toString();
                        TextView email = findViewById(R.id.email_profile);
                        email.setText(userEmail);
                    } else {
                        // Handle the case when the email value is null
                        TextView email = findViewById(R.id.email_profile);
                        email.setText(R.string.email_placeholder);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mUserInfoDatabase.addListenerForSingleValueEvent(valueEventListener);
        }else{
            Toast.makeText(this, "User is not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, home_screen.class));
            finish();

        }

        Button changepass = findViewById(R.id.btn_changepass);
        Button deleteaccount = findViewById(R.id.btn_deleteaccount);

        changepass.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, change_password.class);
            startActivity(intent);
        });

        deleteaccount.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this account will result in completely removing your account from Spendee and you will no longer be able to access this account. " +
                    "In future if you wish to use the same email then you need to register again.");
            dialog.setPositiveButton("DELETE", (dialogInterface, which) -> {
                if(mUser!=null){
                    mUser.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Account Deleted Successfully..", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Profile.this, home_screen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Toast.makeText(Profile.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Profile.this, "An error occurred.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(Profile.this, "User is not authenticated.", Toast.LENGTH_LONG).show();

                }

            });
            dialog.setNegativeButton("NO", (dialogInterface, which) -> dialogInterface.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });

        ImageView back_arrow = findViewById(R.id.back);
        back_arrow.setOnClickListener(v -> finish());
    }
}
