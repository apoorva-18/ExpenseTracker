package com.example.expensetracker;

import androidx.core.content.ContextCompat;

import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;

import android.graphics.Color;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensetracker.databinding.ActivityMainBinding;
//import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemsClick {
    private ActivityMainBinding binding;
    private ExpenseAdapter expenseAdapter;
    Intent intent;
    private long income=0,expense=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        expenseAdapter=new ExpenseAdapter(this,this);
        binding.recycler.setAdapter(expenseAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
         intent=new Intent(MainActivity.this, AddExpenseActivity.class);

        binding.addIncome.setOnClickListener(view -> {
            // Add your onClick logic here
            intent.putExtra("type", "Income");
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create and configure the AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Please Wait");
        alertDialogBuilder.setMessage("Loading...");
        alertDialogBuilder.setCancelable(false);

        // Create the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show the AlertDialog
        alertDialog.show();


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            // Perform anonymous sign-in
            FirebaseAuth.getInstance()
                    .signInAnonymously()
                    .addOnSuccessListener(authResult -> {
                        // Dismiss the AlertDialog on successful sign-in
                        alertDialog.dismiss();
                    })

                    .addOnFailureListener(e -> {
                        // Dismiss the AlertDialog on failure and display error message
                        alertDialog.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        }
        else {
            // If the user is already signed in, dismiss the AlertDialog immediately
            alertDialog.dismiss();
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        income=0;expense=0;
        getData();
    }
    private  void getData()
    {
        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    expenseAdapter.clear();
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot ds : dsList) {
                        ExpenseModel expenseModel = ds.toObject(ExpenseModel.class);
                        if (expenseModel != null) {
                            if ("Income".equals(expenseModel.getType())) {
                                income += expenseModel.getAmount();
                            } else {
                                expense += expenseModel.getAmount();
                            }
                            expenseAdapter.add(expenseModel);
                        }
                    }
                    setUpGraph();
                });


    }
    private void setUpGraph(){
        List<PieEntry>pieEntryList=new ArrayList<>();
        List<Integer>colorsList=new ArrayList<>();
        if(income!=0){
            pieEntryList.add(new PieEntry(income,"Income"));
            colorsList.add(ContextCompat.getColor(getApplicationContext(), R.color.teal_700));


        }
        if(expense!=0){
            pieEntryList.add(new PieEntry(expense,"Expense"));
            colorsList.add(ContextCompat.getColor(getApplicationContext(), R.color.pink));

        }
        PieDataSet pieDataSet=new PieDataSet(pieEntryList,String.valueOf(income-expense));
        pieDataSet.setColors(colorsList);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData=new PieData(pieDataSet);

        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();


    }


    @Override
    public void onClick(ExpenseModel expenseModel) {
        Intent intent=new Intent(MainActivity.this,AddExpenseActivity.class);
        intent.putExtra("model",expenseModel);
        startActivity(intent);


    }
}
