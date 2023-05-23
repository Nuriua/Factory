package com.example.factory.screens;
import com.example.factory.R;
import com.example.factory.modules.Item;
import com.example.factory.modules.Operation;
import com.example.factory.modules.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OperationAdapter adapter;
    FirebaseUser user;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference myRef;
    private String mUserId;
    private List<Operation> Operations;
    RelativeLayout root;
    ListView ListUserTasks;
    TextView textView3;
    BottomNavigationView bottomNavigationView;

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

        if (user != null) {
            String displayName = user.getDisplayName();
            myRef.child(displayName).child("operations").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    myRef.child(displayName).child("income").child("month").setValue(income);
                    textView3.setText(income.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException(); // never ignore errors
                }
            });

            FirebaseRecyclerOptions<Operation> options =
                    new FirebaseRecyclerOptions.Builder<Operation>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child(displayName).child("operations"), Operation.class)
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
//                    case R.id.users:
//                        startActivity(new Intent(getApplicationContext(),Users.class));
//                        overridePendingTransition(0,0);
//                        return true;
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
