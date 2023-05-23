package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.factory.R;
import com.example.factory.modules.Item;
import com.example.factory.modules.Operation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class Profile_seamstress extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button btnSignOut,btnDelete,btnEdit;
    TextView name, mail;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;
    String mUserId;
    ConstraintLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_seamstress);

        root = findViewById(R.id.root_element);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation_seamstress);
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

//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(Profile_seamstress.this);
//                dialog.setTitle("Обновить данные");
//                dialog.setMessage("Введите новые данные");
//                LayoutInflater inflater = LayoutInflater.from(Profile_seamstress.this);
//                View add_window = inflater.inflate(R.layout.fragment_change, null);
//                dialog.setView(add_window);
//
//                final MaterialEditText name = add_window.findViewById(R.id.settings_input_name);
//                final MaterialEditText mail = add_window.findViewById(R.id.settings_input_mail);
//
//                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {//здесь проверяем роль
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (TextUtils.isEmpty(name.getText().toString())) {
//                            Snackbar.make(root, "Введите новые имя и фамилию", Snackbar.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if (TextUtils.isEmpty(mail.getText().toString())) {
//                            Snackbar.make(root, "Введите новую почту", Snackbar.LENGTH_SHORT).show();
//                            return;
//                        }
//                        Operation operation;
//                        if (name.getText() != null && mail.getText() != null) {
//
//                            mDatabase.child("Users").child(mUserId).child("name").setValue(name.getText().toString());
//                            mDatabase.child("Users").child(mUserId).child("mail").setValue(mail.getText().toString());
//                        }
//                    }
//                });
//            }
//        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.operations:
                        startActivity(new Intent(getApplicationContext(),MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
//                    case R.id.users:
//                        startActivity(new Intent(getApplicationContext(),Users.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
    private void signOutUser(){
        Intent mainActivity = new Intent(Profile_seamstress.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

}
