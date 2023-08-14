package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.factory.R;
import com.example.factory.databinding.ActivityUserBinding;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserActivity extends AppCompatActivity {
    ActivityUserBinding binding;
    private DatabaseReference mDatabase;
    private TechOperationAdapter adapter;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Calendar calendar;
    SimpleDateFormat monthFormat;
    String currentMonth;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        calendar = Calendar.getInstance();
        monthFormat = new SimpleDateFormat("MMMM");
        currentMonth = monthFormat.format(calendar.getTime());
        root = findViewById(R.id.root_element);

        if (intent != null){
            String uid = intent.getStringExtra("uid");
            String name = intent.getStringExtra("name");
            String surname = intent.getStringExtra("surname");
            String photoUrl = intent.getStringExtra("photo");
            Picasso.get().load(photoUrl).into(binding.profilePic);
            binding.nameProfile.setText(name + " " + surname);
            mDatabase.child(uid).child(currentMonth).child("operations").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer income = 0;
                    String s = "";
                    for (DataSnapshot ds : snapshot.getChildren()){
                        if (ds.child("sum").getValue(String.class) != null) {
                            s = ds.child("sum").getValue(String.class);
                            income = income + Integer.parseInt(s);
                        }
                    }
                    mDatabase.child(uid).child(currentMonth).child("income").setValue(income);
                    binding.income.setText(income.toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
            binding.recycler.setLayoutManager(new LinearLayoutManager(this));
            FirebaseRecyclerOptions<Operation> options =
                    new FirebaseRecyclerOptions.Builder<Operation>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child(uid).child(currentMonth).child("operations"), Operation.class)
                            .build();
            adapter = new TechOperationAdapter(options, this);
            binding.recycler.setAdapter(adapter);
        }
        String uid = intent.getStringExtra("uid");
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity.this);
                LayoutInflater inflater = LayoutInflater.from(UserActivity.this);
                View add_window = inflater.inflate(R.layout.add_operation_window, null);
                dialog.setView(add_window);

                final MaterialEditText name_model = add_window.findViewById(R.id.nameModelField);
                final MaterialEditText name_operation = add_window.findViewById(R.id.nameOperationField);
                final MaterialEditText price = add_window.findViewById(R.id.priceField);
                final MaterialEditText size = add_window.findViewById(R.id.sizeField);
                final MaterialEditText amount = add_window.findViewById(R.id.amountField);
                final MaterialEditText pack = add_window.findViewById(R.id.pack);

                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {//здесь проверяем роль
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(name_model.getText().toString())) {
                            Snackbar.make(root, "Введите название модели", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(name_operation.getText().toString())) {
                            Snackbar.make(root, "Введите название операции", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(price.getText().toString())) {
                            Snackbar.make(root, "Введите цену", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(size.getText().toString())) {
                            Snackbar.make(root, "Введите размер изделия", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(amount.getText().toString())) {
                            Snackbar.make(root, "Введите количество", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(pack.getText().toString())) {
                            Snackbar.make(root, "Введите количество", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        Operation operation;
                        if (name_operation.getText() != null && name_model.getText() != null && price.getText() != null && size.getText() != null && amount.getText() != null) {
                            operation = new Operation(name_operation.getText().toString(), name_model.getText().toString(), size.getText().toString(), price.getText().toString(), amount.getText().toString(), "0", "0", binding.nameProfile.getText().toString(), uid, pack.getText().toString());
                            mDatabase.child(uid).child(currentMonth).child("operations").push().setValue(operation);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
