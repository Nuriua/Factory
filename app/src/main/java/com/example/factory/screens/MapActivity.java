package com.example.factory.screens;
import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OperationAdapter adapter;
    FirebaseUser user;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String mUserId;
    RelativeLayout root;
    TextView textView3;
    BottomNavigationView bottomNavigationView;
    Calendar calendar;
    SimpleDateFormat monthFormat;
    String currentMonth;
    ImageView imageView;

    FirebaseListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        recyclerView = findViewById(R.id.recycler);
        textView3 = findViewById(R.id.textView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();
        calendar = Calendar.getInstance();
        monthFormat = new SimpleDateFormat("MMMM");
        currentMonth = monthFormat.format(calendar.getTime());
        imageView = findViewById(R.id.imageView);

        if (user != null) {
            myRef.child("Users").child(mUserId).child("photoUrl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String image = snapshot.getValue().toString();
                    Picasso.get()
                            .load(image)
                            .into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("Ошибка чтения данных");
                }
            });

            String email = user.getEmail();
            myRef.child(mUserId).child(currentMonth).child("operations").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer income = 0;
                    String s = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("sum").getValue(String.class) != null) {
                            s = ds.child("sum").getValue(String.class);
                            income = income + Integer.parseInt(s);
                        }
                    }
                    myRef.child(mUserId).child(currentMonth).child("income").setValue(income);//исправить месяц!!!
                    textView3.setText(income.toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException(); // never ignore errors
                }
            });

            FirebaseRecyclerOptions<Operation> options =
                    new FirebaseRecyclerOptions.Builder<Operation>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child(mUserId).child(currentMonth).child("operations"), Operation.class)
                            .build();
            adapter = new OperationAdapter(options, this);
            recyclerView.setAdapter(adapter);
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.operations:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile_seamstress.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
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
