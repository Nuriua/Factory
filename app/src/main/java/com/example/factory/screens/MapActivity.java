package com.example.factory.screens;
import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.example.factory.modules.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    EditText price, price2, price3, price4, price5;
    EditText amount, amount2, amount3, amount4, amount5;
    TextView tvResult, tvResult2, tvResult3, tvResult4, tvResult5, textView3, textView5;
    TextView textView;
    User user;
    String name;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference myRef;
    private String mUserId;
    private List<Operation> Operations;
    RelativeLayout root;
    ListView ListUserTasks;

    FirebaseListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        ListView listView = (ListView) findViewById(R.id.listViewOperations);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();
        name = mFirebaseUser.getDisplayName();

        myRef.child("Анна Валерьевна").child("operations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = "";
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.child("name").getValue(String.class) != null) {
                        s = s + "\n" + "Название операции : " + ds.child("name").getValue().toString() + "\n" +
                                "Размер изделия : " + ds.child("size").getValue().toString() + "\n" +
                                "Цена за единицу операции : " + ds.child("price").getValue().toString() + "\n" +
                                "Необходимо выполнить : " + ds.child("amount").getValue().toString() + "\n" +
                                "Выполнено : " + ds.child("sum").getValue().toString() + "\n";
                        adapter.add(s);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void clickButton(View v) {
//        tvResult.setText(s);
//        tvResult2.setText(ss2);
//        tvResult3.setText(ss3);
//        tvResult4.setText(ss4);
//        tvResult5.setText(ss5);
//        textView3.setText(ss6);
    }

}