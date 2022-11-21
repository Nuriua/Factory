package com.example.factory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.factory.MainActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.factory.modules.User;

public class ForTech extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_technologist_main);

    }
}
