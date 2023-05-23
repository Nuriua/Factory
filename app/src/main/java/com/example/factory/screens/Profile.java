package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.factory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button btnSignOut,btnDelete,btnEdit;
    TextView name, mail;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;
    String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        btnSignOut = findViewById(R.id.button_logout);
        btnDelete = findViewById(R.id.button_delete);
        btnEdit = findViewById(R.id.button_edit);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserId = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        if (user != null) {
            String displayName = user.getDisplayName();
            name.setText(displayName);
            String email = user.getEmail();
            mail.setText(email);
        }

        if (mUserId.equals("oWDTtAKLJVTNiegemh7Lkyawvig2")){
            btnDelete.setVisibility(View.GONE);
        }
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
//                    case R.id.models:
//                        startActivity(new Intent(getApplicationContext(),ForTech.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.users:
                        startActivity(new Intent(getApplicationContext(),Users.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
    private void signOutUser(){
        Intent mainActivity = new Intent(Profile.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

}
