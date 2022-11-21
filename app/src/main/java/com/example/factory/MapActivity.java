package com.example.factory;
import com.example.factory.modules.Operation;
import com.example.factory.modules.User;
import com.example.factory.MainActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    EditText price, price2, price3, price4, price5;
    EditText amount, amount2, amount3, amount4, amount5;
    TextView tvResult, tvResult2, tvResult3, tvResult4, tvResult5, textView3, textView5;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;//ссылка на данные в бд
    private List<Operation> Operations;//список объектов клвсса Operation

    ListView ListUserTasks;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    FirebaseListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        ListUserTasks = (ListView) findViewById(R.id.discr_for_task);
        // находим элементы
        price = (EditText) findViewById(R.id.price);
        amount = (EditText) findViewById(R.id.amount);
        tvResult = (TextView) findViewById(R.id.tvResult);
        price2 = (EditText) findViewById(R.id.price2);
        amount2 = (EditText) findViewById(R.id.amount2);
        tvResult2 = (TextView) findViewById(R.id.tvResult2);
        price3 = (EditText) findViewById(R.id.price3);
        amount3 = (EditText) findViewById(R.id.amount3);
        tvResult3 = (TextView) findViewById(R.id.tvResult3);
        price4 = (EditText) findViewById(R.id.price4);
        amount4 = (EditText) findViewById(R.id.amount4);
        tvResult4 = (TextView) findViewById(R.id.tvResult4);
        price5 = (EditText) findViewById(R.id.price5);
        amount5 = (EditText) findViewById(R.id.amount5);
        tvResult5 = (TextView) findViewById(R.id.tvResult5);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView5 = (TextView) findViewById(R.id.textView5);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            String uid = user.getUid();
//            textView5.setText(name);
//        }
//        textView5.setText(User.getName());

    }

    public void clickButton(View v) {
        // Объявим числовые переменные
        double a, b, c, a2, b2, c2, a3, b3, c3, a4, b4, c4, a5, b5, c5, c6;

        // Считаем с editText и editText2 текстовые значения
        String s1 = price.getText().toString();
        String s2 = amount.getText().toString();
        String s12 = price2.getText().toString();
        String s22 = amount2.getText().toString();
        String s13 = price3.getText().toString();
        String s23 = amount3.getText().toString();
        String s14 = price4.getText().toString();
        String s24 = amount4.getText().toString();
        String s15 = price5.getText().toString();
        String s25 = amount5.getText().toString();

        // Преобразуем текстовые переменные в числовые значения
        if (s1.length() < 1)
            a = 0;
        else
            a = Double.parseDouble(s1);
        if (s2.length() < 1)
            b = 0;
        else
            b = Double.parseDouble(s2);
        if (s12.length() < 1)
            a2 = 0;
        else
            a2 = Double.parseDouble(s12);
        if (s22.length() < 1)
            b2 = 0;
        else
            b2 = Double.parseDouble(s22);
        if (s13.length() < 1)
            a3 = 0;
        else
            a3 = Double.parseDouble(s13);
        if (s23.length() < 1)
            b3 = 0;
        else
            b3 = Double.parseDouble(s23);
        if (s14.length() < 1)
            a4 = 0;
        else
            a4 = Double.parseDouble(s14);
        if (s24.length() < 1)
            b4 = 0;
        else
            b4 = Double.parseDouble(s24);
        if (s15.length() < 1)
            a5 = 0;
        else
            a5 = Double.parseDouble(s15);
        if (s25.length() < 1)
            b5 = 0;
        else
            b5 = Double.parseDouble(s25);

        // Проведем с числовыми переменными нужные действия
        c = a * b;
        c2 = a2 * b2;
        c3 = a3 * b3;
        c4 = a4 * b4;
        c5 = a5 * b5;
        c6 = c + c2 + c3 + c4 + c5;

        // Преобразуем ответ в число
        String s = Double.toString(c);
        String ss2 = Double.toString(c2);
        String ss3 = Double.toString(c3);
        String ss4 = Double.toString(c4);
        String ss5 = Double.toString(c5);
        String ss6 = Double.toString(c6);

        // Выведем текст в textView
        tvResult.setText(s);
        tvResult2.setText(ss2);
        tvResult3.setText(ss3);
        tvResult4.setText(ss4);
        tvResult5.setText(ss5);
        textView3.setText(ss6);
    }

}