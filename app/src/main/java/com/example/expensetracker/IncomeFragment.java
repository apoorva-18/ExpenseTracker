package com.example.expensetracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.example.expensetracker.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class IncomeFragment extends Fragment {

    private DatabaseReference mIncomeDatabase;
    private RecyclerView recyclerView;
    private TextView incomeTotalSum;

    ///Update edit text.

    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;
    //button for update and delete
    //Data item value
    private String type;
    private String note;
    private int amount;
    private  String post_key;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_income, container, false);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mUser!=null) {
            String uid = mUser.getUid();

            mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        }

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);
        recyclerView=myview.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalValue = 0.0; // Initialize total value as a double
                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    if (data != null) { // Check if data and amount are not null
                        totalValue += data.getAmount(); // Add the amount to total value
                    }
                }
                // Format total value with two decimal places
                String stTotalValue = String.format(Locale.getDefault(), "%.2f", totalValue);
                // Create a string resource for the income total sum with a placeholder for the total value
                String totalIncomeText = getString(R.string.income_total_sum, stTotalValue);
                // Set the formatted total income text to the incomeTotalSum TextView
                incomeTotalSum.setText(totalIncomeText);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return  myview;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the layout for each item in the RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false);
                // Return a new instance of MyViewHolder
                return new MyViewHolder(view);
            }

            public void onBindViewHolder(@NonNull MyViewHolder myViewHolder,final int position,@NonNull Data model) {

                myViewHolder.setType(model.getType());
                myViewHolder.setNote(model.getNote());
                myViewHolder.setDate(model.getDate());
                myViewHolder.setAmount(model.getAmount());

                myViewHolder.mView.setOnClickListener(v -> {
                     post_key = getRef(position).getKey();
                     type = model.getType();
                     note = model.getNote();
                     amount = model.getAmount();
                    updateDataItem();
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void setAmount(int amount){
            TextView mAmount=mView.findViewById(R.id.amount_txt_income);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }

    private void updateDataItem()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmount=myview.findViewById(R.id.amount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);

        //Set data to edit text..
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        Button btnUpdate=myview.findViewById(R.id.btn_upd_Update);
        Button btnDelete=myview.findViewById(R.id.btnuPD_Delete);

        AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(v -> {
            type = edtType.getText().toString().trim();
            note = edtNote.getText().toString().trim();

            String mdAmount = edtAmount.getText().toString().trim();


            int myAmount = Integer.parseInt(mdAmount);

            String mDate = DateFormat.getDateInstance().format(new Date());

            Data data = new Data(myAmount, type, note, post_key, mDate);

            mIncomeDatabase.child(post_key).setValue(data);

            dialog.dismiss();
        });


        btnDelete.setOnClickListener(v -> {
            mIncomeDatabase.child(post_key).removeValue();
            dialog.dismiss();
        });

        dialog.show();
    }
}