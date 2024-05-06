package com.example.expensetracker;

import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.HashMap;

public class feedback extends AppCompatActivity {

    private DatabaseReference mFeedbackDatabase;

    String s1;

    RatingBar ratingBar;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser!=null) {
            String uid = mUser.getUid();

            mFeedbackDatabase = FirebaseDatabase.getInstance().getReference().child("Feedback").child(uid);
        }

        ratingBar=findViewById(R.id.rating_bar);
        btnSubmit=findViewById(R.id.rating_btn);

        btnSubmit.setOnClickListener(v -> {
            s1 = String.valueOf(ratingBar.getRating());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Rating", s1);

            if (ratingBar.getRating() != 0.0) {
                mFeedbackDatabase.child("Feedback").updateChildren(hashMap)
                        .addOnSuccessListener(o -> {
                            Toast.makeText(getApplicationContext(), "Thanks for Rating us..", Toast.LENGTH_SHORT).show();
                            ratingBar.setRating(0);
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please Rate us!", Toast.LENGTH_SHORT).show();
            }
        });


        ImageView back_arrow=findViewById(R.id.back);
        back_arrow.setOnClickListener(v -> finish());


    }
}
