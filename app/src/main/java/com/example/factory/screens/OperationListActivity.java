package com.example.factory.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

//ДОБАВИТЬ ФАЙЛ в манифест
public class OperationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OperationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_list);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Operation> options =
                new FirebaseRecyclerOptions.Builder<Operation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(), Operation.class)
                        .build();

        adapter = new OperationAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}