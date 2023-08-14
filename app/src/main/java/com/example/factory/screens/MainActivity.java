package com.example.factory.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.factory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.factory.modules.User;
import com.google.firebase.storage.FirebaseStorage;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    FirebaseStorage storage;
    String mUserId;

    RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.root_element);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("pass", null);

        if (userId != null && email != null && password != null) {
            // Восстанавливаем сеанс аутентификации в Firebase
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Пользователь успешно восстановлен
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                // Действия после восстановления пользователя
                                if (email.equals("nyriya36@gmail.com") && password.equals("privet")) {
                                    startActivity(new Intent(MainActivity.this, Users.class));
                                    finish();
                                }
                                else{
                                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                                    finish();
                                }
                            } else {
                                // Восстановление не удалось
                                Snackbar.make(root, "Восстановление не удалось", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Нет сохраненных данных аутентификации
            Snackbar.make(root, "Нет сохраненных данных аутентификации", Snackbar.LENGTH_SHORT).show();
        }
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showRegisterWindow(); }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showSignInWindow(); }
        });
    }

    private void showSignInWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для входа");
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        final MaterialEditText email = sign_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass = sign_in_window.findViewById(R.id.passField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль, который более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Сохраняем userId в SharedPreferences
                                mUserId = auth.getCurrentUser().getUid();
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", mUserId);
                                editor.putString("email", email.getText().toString());
                                editor.putString("pass", pass.getText().toString());
                                editor.apply();
                                if (email.getText().toString().equals("nyriya36@gmail.com") && pass.getText().toString().equals("privet")) {
                                    startActivity(new Intent(MainActivity.this, Users.class));
                                    finish();
                                }
                                else{
                                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка авторизации" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void showRegisterWindow(){// показать окно регистрации
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите все данные для регистрации");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        storage = FirebaseStorage.getInstance();

        final MaterialEditText email = register_window.findViewById(R.id.emailField);
        final MaterialEditText pass = register_window.findViewById(R.id.passField);
        final MaterialEditText name = register_window.findViewById(R.id.nameField);
        final MaterialEditText surname = register_window.findViewById(R.id.surnameField);
        final MaterialEditText phone = register_window.findViewById(R.id.phoneField);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {//сюда запихнуть чекбокс
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(surname.getText().toString())){
                    Snackbar.make(root, "Введите вашу фамилию", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().length() < 5){
                    Snackbar.make(root, "Введите пароль, который более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())//создать пользователя с почтой и паролем
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {//при успехе
                                User user = new User();
                                user.setEmail(email.getText().toString());//установить почту
                                user.setName(name.getText().toString());
                                user.setSurname(surname.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());
                                user.setPhotoUrl("https://firebasestorage.googleapis.com/v0/b/factory-12da5.appspot.com/o/images%2Ffacebook_avatar.png?alt=media&token=a7ec613d-4eb9-48e2-9435-9a40769b98d8&_gl=1*18u6jre*_ga*MjEyMTU0Mjc0NS4xNjc1MzE1NzA0*_ga_CW55HF8NVT*MTY4NTY3MjgyNS43NS4xLjE2ODU2NzY5OTQuMC4wLjA.");
                                user.setRole("seamstress");
                                user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                            }

                                        });
                                String newDisplayName = name.getText().toString();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(newDisplayName)
                                        .build();
                                FirebaseUser mUser = auth.getCurrentUser();
                                mUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Имя отображения успешно обновлено
                                                    Toast.makeText(MainActivity.this, "Имя отображения успешно обновлено", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Произошла ошибка при обновлении имени отображения
                                                    Toast.makeText(MainActivity.this, "Произошла ошибка при обновлении имени отображения", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка регистрации. " +e.getMessage(), Snackbar.LENGTH_LONG).show();
                        // Удаление аккаунта пользователя
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Успешное удаление аккаунта
                                        Toast.makeText(MainActivity.this, "Попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Обработка ошибок при удалении аккаунта
                                        Toast.makeText(MainActivity.this, "Ошибка при удалении почты: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        finish();
                    }
                });
            }
        });
        dialog.show();
    }
}
