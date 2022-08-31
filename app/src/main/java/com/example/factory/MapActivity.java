package com.example.factory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.EditText;

import android.widget.TextView;

public class MapActivity extends AppCompatActivity {

    EditText price;
    EditText amount;
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // находим элементы
        price = (EditText) findViewById(R.id.price);
        amount = (EditText) findViewById(R.id.amount);
        tvResult = (TextView) findViewById(R.id.tvResult);
    }

}