package com.example.factory.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.factory.R;
import com.example.factory.modules.Item;
import com.example.factory.modules.Operation;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class ForTech extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;//состояние аутентификации
    private FirebaseUser mFirebaseUser;
    RelativeLayout root;
    Button button;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);
        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        root = findViewById(R.id.root_element);
        button = findViewById(R.id.addButton);//кнопка

        //нижнее меню
        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setSelectedItemId(R.id.models);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
//                    case R.id.models:
//                        return true;
                    case R.id.users:
                        startActivity(new Intent(getApplicationContext(), Users.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        mUserId = mFirebaseUser.getUid();
        ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        mDatabase.child("users").child(mUserId).child("items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(dataSnapshot.getValue().toString());
                key = dataSnapshot.getKey();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.remove(dataSnapshot.getValue().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ForTech.this);
                dialog.setTitle("Добавить модель");
                dialog.setMessage("Введите данные модели");
                LayoutInflater inflater = LayoutInflater.from(ForTech.this);
                View add_window = inflater.inflate(R.layout.add_window, null);
                dialog.setView(add_window);

                final MaterialEditText name_model = add_window.findViewById(R.id.nameModelField);
                final MaterialEditText name_operation = add_window.findViewById(R.id.nameOperationField);
                final MaterialEditText price = add_window.findViewById(R.id.priceField);
                final MaterialEditText size = add_window.findViewById(R.id.sizeField);
                final MaterialEditText amount = add_window.findViewById(R.id.amountField);
                final MaterialEditText seamstress = add_window.findViewById(R.id.seamstressField);

                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {//здесь проверяем роль
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(name_model.getText().toString())) {
                            Snackbar.make(root, "Введите название модели", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(name_operation.getText().toString())) {
                            Snackbar.make(root, "Введите название операции", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(price.getText().toString())) {
                            Snackbar.make(root, "Введите цену", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(size.getText().toString())) {
                            Snackbar.make(root, "Введите размер изделия", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(amount.getText().toString())) {
                            Snackbar.make(root, "Введите количество", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(seamstress.getText().toString())) {
                            Snackbar.make(root, "Швея", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        Operation operation;
                        if (name_operation.getText() != null && name_model.getText() != null && price.getText() != null && size.getText() != null && amount.getText() != null && seamstress.getText() != null) {
                            operation = new Operation(name_operation.getText().toString(), name_model.getText().toString(), size.getText().toString(), price.getText().toString(), amount.getText().toString(), "0", "0", seamstress.getText().toString());
                            ArrayList<Operation> list = new ArrayList<>();
                            list.add(operation);

                            //сохранение модели в бд
                            Item item = new Item(name_model.getText().toString());
                            DatabaseReference newRef = mDatabase.child("Users").child(mUserId).child("items").push();
                            String key = newRef.getKey();
                            newRef.setValue(item);
                            mDatabase.child("Users").child(mUserId).child("items").child(key).child("operations").push().setValue(operation);
                            mDatabase.child(seamstress.getText().toString()).child("operations").push().setValue(operation);//новое изменение!!!!!

                            mDatabase.child("users").child(mUserId).child("items").push().setValue(item.getTitle());
                        }
                    }
                });
                dialog.show();

            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ForTech.this, ModelActivity.class);
                intent.putExtra("name", adapter.getItem(i));
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });
    }
}
