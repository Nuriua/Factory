package com.example.factory.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.factory.R;
import com.example.factory.databinding.ActivityModelBinding;

public class ModelActivity extends AppCompatActivity {
    ActivityModelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null){
            String name = intent.getStringExtra("name");
            binding.nameModel.setText(name);
        }
    }
}
