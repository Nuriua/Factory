package com.example.factory.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.factory.R;
import com.example.factory.databinding.ActivityUserBinding;
import com.example.factory.modules.Item;
import com.example.factory.modules.Operation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    ActivityUserBinding binding;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (intent != null){
            String name = intent.getStringExtra("name");
            binding.nameProfile.setText(name);
        }

        ListView listView = (ListView) findViewById(R.id.listView);//лучше ииспользовать binding но пока не до этого
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        final EditText text = (EditText) findViewById(R.id.todoText);//поле ввода
        final Button button = (Button) findViewById(R.id.addButton);

        button.setOnClickListener(new View.OnClickListener() { //
            public void onClick(View v) {//при нажатии на кнопку
//                Operation operation = new Operation(text.getText().toString(),);//создается новый объект item
//                mDatabase.child("users").child(mUserId).child("items").push().setValue(item);//как получить uid не текущего пользователя??
                text.setText("");//чистим поле ввода
            }
        });

    }
}
