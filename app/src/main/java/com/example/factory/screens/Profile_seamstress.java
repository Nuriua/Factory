package com.example.factory.screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile_seamstress extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button btnSignOut,btnDelete,btnEdit;
    TextView name, mail;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseUser user;
    String mUserId;
    ImageView profileImage;
    ConstraintLayout root;
    String displayName;

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
        if (mAuth.getCurrentUser() != null) {
            mUserId = mAuth.getCurrentUser().getUid();
        }
        user = mAuth.getCurrentUser();
        profileImage = findViewById(R.id.profile_image);

        mDatabase.child("Users").child(mUserId).child("photoUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String image = snapshot.getValue().toString();
                    Picasso.get()
                            .load(image)
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Ошибка чтения данных");
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            }
        });

        if (user != null) {
            displayName = user.getDisplayName();
            mDatabase.child("Users").child(mUserId).child("surname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        String newName = displayName + " " + snapshot.getValue().toString();
                        name.setText(newName);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
            String email = user.getEmail();
            mail.setText(email);
        }

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userId");
                editor.remove("email");
                editor.remove("pass");
                editor.apply();
                mAuth.signOut();
                signOutUser();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Profile_seamstress.this);
                dialog.setTitle("Изменить имя фамилию");
                dialog.setMessage("Введите новые данные");
                LayoutInflater inflater = LayoutInflater.from(Profile_seamstress.this);
                View add_window = inflater.inflate(R.layout.fragment_change, null);
                dialog.setView(add_window);
                EditText name = add_window.findViewById(R.id.settings_input_name);
                EditText surname = add_window.findViewById(R.id.settings_input_surname);

                dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {//здесь проверяем роль
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(name.getText().toString())) {
                            Snackbar.make(root, "Введите имя", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(surname.getText().toString())) {
                            Snackbar.make(root, "Введите фамилия", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        Operation operation;
                        if (name.getText() != null && surname.getText() != null) {
                            mDatabase.child("Users").child(mUserId).child("name").setValue(name.getText().toString());
                            mDatabase.child("Users").child(mUserId).child("surname").setValue(surname.getText().toString());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name.getText().toString())
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Обновление отображаемого имени выполнено успешно
                                                Log.d("Profile_seamstress", "Отображаемое имя обновлено");
                                            } else {
                                                // Возникла ошибка при обновлении отображаемого имени
                                                Log.d("Profile_seamstress", "Ошибка при обновлении отображаемого имени");
                                            }
                                        }
                                    });
                        }
                    }
                });
                dialog.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_seamstress.this);
                builder.setTitle("Удаление аккаунта");
                builder.setMessage("Вы действительно хотите удалить аккаунт?");
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Вызов метода для удаления аккаунта пользователя из Firebase
                        deleteUserAccount();
                    }
                });
                builder.setNegativeButton("Отмена", null);
                builder.show();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.operations:
                        startActivity(new Intent(getApplicationContext(),MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }

    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Uri imageUri = data.getData();
                    // Handle the selected image URI here
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
                    UploadTask uploadTask = imageRef.putFile(imageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Фотография успешно загружена
                            // Получите URL загруженного изображения для сохранения в базе данных или дальнейшего использования
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    String imageUrl = downloadUri.toString();
                                    mDatabase.child("Users").child(mUserId).child("photoUrl").setValue(imageUrl);
                                    Picasso.get()
                                            .load(imageUrl)
                                            .into(profileImage);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Возникла ошибка при загрузке фотографии
                        }
                    });
                }
            }
    );

    private void signOutUser(){
        Intent mainActivity = new Intent(Profile_seamstress.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

    private void deleteUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mDatabase.child(mUserId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Объект успешно удален из Firebase
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Обработка ошибок при удалении объекта
                        }
                    });
            mDatabase.child("Users").child(mUserId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Объект успешно удален из Firebase
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Обработка ошибок при удалении объекта
                        }
                    });
            // Удаление аккаунта пользователя
            user.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Успешное удаление аккаунта
                            Toast.makeText(Profile_seamstress.this, "Аккаунт успешно удален", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Обработка ошибок при удалении аккаунта
                            Toast.makeText(Profile_seamstress.this, "Ошибка при удалении аккаунта: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            Intent mainActivity = new Intent(Profile_seamstress.this, MainActivity.class);
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivity);
            finish();
        }
    }
}
