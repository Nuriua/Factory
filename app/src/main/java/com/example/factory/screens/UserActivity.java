package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.factory.R;
import com.example.factory.databinding.ActivityUserBinding;
import com.example.factory.modules.Item;
import com.example.factory.modules.Operation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        // Set up ListView final
        ListView listView = (ListView) findViewById(R.id.listView);//лучше ииспользовать binding но пока не до этого
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);
        // Add items via the Button and EditText at the bottom of the view.
        final EditText text = (EditText) findViewById(R.id.todoText);//поле ввода
        final Button button = (Button) findViewById(R.id.addButton);//кнопка
        String name = intent.getStringExtra("name");

//        mDatabase.child("users").orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        button.setOnClickListener(new View.OnClickListener() { //
            public void onClick(View v) {//при нажатии на кнопку
                Operation operation = new Operation(text.getText().toString(), "", "", "", "", "");//создается новый объект operation
//                mDatabase.child("Users").orderByChild("name").equalTo(name).child("operations").push().setValue(operation);//как получить uid не текущего пользователя??
                text.setText("");//чистим поле ввода
            }
        });
        // Use Firebase to populate the list.
//        mDatabase.child("users").orderByChild("name").equalTo(name).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                adapter.add((String) dataSnapshot.child("operations").child("name").getValue());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                adapter.remove((String) dataSnapshot.child("operations").child("name").getValue());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

    }
}
