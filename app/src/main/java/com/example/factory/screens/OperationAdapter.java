package com.example.factory.screens;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class OperationAdapter extends FirebaseRecyclerAdapter<Operation, OperationAdapter.OperationViewHolder>{
    public Context context;
    FirebaseUser user;
    String mUserId;
    FirebaseAuth mAuth;
    Calendar calendar;
    SimpleDateFormat monthFormat;
    String currentMonth;

    public OperationAdapter(@NonNull FirebaseRecyclerOptions<Operation> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull OperationViewHolder holder, int position, @NonNull Operation operation) {
        int itemPosition = holder.getBindingAdapterPosition();
        calendar = Calendar.getInstance();
        monthFormat = new SimpleDateFormat("MMMM");
        currentMonth = monthFormat.format(calendar.getTime());

        holder.name.setText("Операция: " + operation.getName());
        holder.model.setText("Модель: " + operation.getModel());
        holder.size.setText("Размер: " + operation.getSize());
        holder.pack.setText("Номер пачки: " + operation.getPack());
        holder.price.setText("Цена: " + operation.getPrice());
        holder.amount.setText("Количество: " + operation.getAmount());
        holder.amountOfDone.setText("Выполнено: " + operation.getAmountOfDone());
        holder.sum.setText("Сумма: " + operation.getSum());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50, 0, 50, 0)
                        .setContentHolder(new ViewHolder(R.layout.content))
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                View holderView = (LinearLayout) dialog.getHolderView();
                EditText amount = holderView.findViewById(R.id.amountOfDoneField);
                amount.setText(operation.getAmountOfDone());
                Button plus = holderView.findViewById(R.id.buttonPlus);
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueStr = amount.getText().toString();
                        int value = Integer.parseInt(valueStr);
                        value++;
                        amount.setText(String.valueOf(value));
                    }
                });
                Button update = holderView.findViewById(R.id.update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Long p = Long.parseLong(operation.getPrice().toString());
                        Long a = Long.parseLong(amount.getText().toString());
                        Long s = p * a;
                        mAuth = FirebaseAuth.getInstance();
                        user = mAuth.getCurrentUser();
                        mUserId = user.getUid();
                        FirebaseDatabase.getInstance().getReference()
                                .child(mUserId).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(itemPosition).getKey()))
                                .child("amountOfDone")
                                .setValue(amount.getText().toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child(mUserId).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(holder.getBindingAdapterPosition()).getKey()))
                                .child("sum")
                                .setValue(s.toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.operation, parent, false);
        return new OperationViewHolder(view);
    }

    class OperationViewHolder extends RecyclerView.ViewHolder{
        TextView name, model, size, price, amount, amountOfDone, sum, pack;
        ImageView edit, delete;

        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameOperation);
            model = itemView.findViewById(R.id.nameModel);
            size = itemView.findViewById(R.id.size);
            pack = itemView.findViewById(R.id.pack);
            price = itemView.findViewById(R.id.price);
            amount = itemView.findViewById(R.id.amount);
            amountOfDone = itemView.findViewById(R.id.amountOfDone);
            sum = itemView.findViewById(R.id.sum);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            delete.setVisibility(View.GONE);
        }
    }
}
