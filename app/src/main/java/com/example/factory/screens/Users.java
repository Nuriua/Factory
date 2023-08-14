package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.factory.R;
import com.example.factory.modules.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Users extends AppCompatActivity {//
    BottomNavigationView bottomNavigationView;//меню
    ListView listview_users;//список пользователей
    private DatabaseReference mDatabase;//ссылка на бд
    ArrayList<User> myArrayList = new ArrayList<>();//массив пользователей в виде списка
    String mUserId;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();
        ListAdapter listAdapter = new ListAdapter(Users.this, myArrayList);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listview_users = (ListView)findViewById(R.id.listview_users);
        listview_users.setAdapter(listAdapter);
        listview_users.setClickable(true);

        mDatabase.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = dataSnapshot.getKey();
                if (!uid.equals(mUserId)){
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String surname = dataSnapshot.child("surname").getValue(String.class);
                    String mail = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    String photoUrl = dataSnapshot.child("photoUrl").getValue(String.class);
                    User user = new User(uid, name, surname, mail, "pass", phone, role, photoUrl);
                    myArrayList.add(user);
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listAdapter.notifyDataSetChanged();
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
                intent.putExtra("uid", listAdapter.getItem(i).getUid());
                intent.putExtra("name", listAdapter.getItem(i).getName());
                intent.putExtra("surname", listAdapter.getItem(i).getSurname());
                intent.putExtra("mail", listAdapter.getItem(i).getEmail());
                intent.putExtra("photo", listAdapter.getItem(i).getPhotoUrl());
                startActivity(intent);
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.users);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
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
