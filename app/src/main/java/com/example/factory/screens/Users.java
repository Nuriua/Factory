package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.factory.R;
import com.example.factory.databinding.ActivityMainBinding;
import com.example.factory.modules.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Users extends AppCompatActivity {//
    BottomNavigationView bottomNavigationView;
    ListView listview_users;
    private DatabaseReference mDatabase;//ссылка на бд
    ArrayList<String> myArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(Users.this, android.R.layout.simple_list_item_1,myArrayList);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listview_users = (ListView)findViewById(R.id.listview_users);
        listview_users.setAdapter(myArrayAdapter);

        // Use Firebase to populate the list.
        mDatabase.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.child("name").getValue(String.class);
                myArrayList.add(value);
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        listview_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Users.this, UserActivity.class);
                intent.putExtra("name", myArrayAdapter.getItem(i));
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.users);
        //нижнее меню
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.operations:
                        startActivity(new Intent(getApplicationContext(), Operations.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.models:
                        startActivity(new Intent(getApplicationContext(),ForTech.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.users:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}
