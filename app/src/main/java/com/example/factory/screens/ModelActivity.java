package com.example.factory.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.factory.R;
import com.example.factory.databinding.ActivityModelBinding;
import com.example.factory.modules.Item;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModelActivity extends AppCompatActivity {
    ActivityModelBinding binding;
    private OperationAdapter adapter;
    private DatabaseReference mDatabase;
    private String mUserId;//id пользователя
    private FirebaseAuth mFirebaseAuth;//состояние аутентификации
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //получаем uid пользователя
        mUserId = mFirebaseUser.getUid();

        binding = ActivityModelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null){
            String name = intent.getStringExtra("name");
            String key = intent.getStringExtra("key");
            System.out.println(key);
            binding.nameModel.setText(name);
            binding.recycler.setLayoutManager(new LinearLayoutManager(this));
            FirebaseRecyclerOptions<Operation> options =
                    new FirebaseRecyclerOptions.Builder<Operation>()
                            .setQuery(mDatabase.child("Анна Валерьевна").child("operations"), Operation.class)
                            .build();
            adapter = new OperationAdapter(options, this);
            binding.recycler.setAdapter(adapter);
        }

        final EditText text = (EditText) findViewById(R.id.todoText);
        final Button button = (Button) findViewById(R.id.addButton);
        String name = intent.getStringExtra("name");

//        button.setOnClickListener(new View.OnClickListener() { //
//            public void onClick(View v) {//при нажатии на кнопку
//                Operation operation = new Operation(text.getText().toString(), "", "", "", "", "");//создается новый объект operation
////                mDatabase.child("Users").child(mUserId).child("items").child("operation").push().setValue(operation);//записываем объект в бд
//                mDatabase.child("Users").child(mUserId).child("items").child("operation").setValue(operation);
//                text.setText("");//чистим поле ввода
//            }
//        });
    }
}
