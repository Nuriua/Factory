package com.example.factory.screens;

import android.content.Context;
import android.util.Log;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TechOperationAdapter extends FirebaseRecyclerAdapter<Operation, TechOperationAdapter.OperationViewHolder>{
    public Context context;
    Calendar calendar;
    SimpleDateFormat monthFormat;
    String currentMonth;

    public TechOperationAdapter(@NonNull FirebaseRecyclerOptions<Operation> options, Context context) {
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
                        .setContentHolder(new ViewHolder(R.layout.update_operation_window))
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                View holderView = (CardView) dialog.getHolderView();
                EditText nameModel = holderView.findViewById(R.id.nameModelField);
                EditText nameOperation = holderView.findViewById(R.id.nameOperationField);
                EditText price = holderView.findViewById(R.id.priceField);
                EditText size = holderView.findViewById(R.id.sizeField);
                EditText amount = holderView.findViewById(R.id.amountField);

                nameModel.setText(operation.getModel());
                nameOperation.setText(operation.getName());
                price.setText(operation.getPrice());
                size.setText(operation.getSize());
                amount.setText(operation.getAmount());

                Button update = holderView.findViewById(R.id.update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference()
                                .child(operation.getUid()).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(itemPosition).getKey()))
                                .child("model")
                                .setValue(nameModel.getText().toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child(operation.getUid()).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(itemPosition).getKey()))
                                .child("name")
                                .setValue(nameOperation.getText().toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child(operation.getUid()).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(itemPosition).getKey()))
                                .child("price")
                                .setValue(price.getText().toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child(operation.getUid()).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(itemPosition).getKey()))
                                .child("size")
                                .setValue(size.getText().toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child(operation.getUid()).child(currentMonth)
                                .child("operations")
                                .child(Objects.requireNonNull(getRef(itemPosition).getKey()))
                                .child("amount")
                                .setValue(amount.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String objectId = getRef(itemPosition).getKey();//
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50, 0, 50, 0)
                        .setContentHolder(new ViewHolder(R.layout.question))
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();

                View holderView = (LinearLayout) dialog.getHolderView();
                Button yes = holderView.findViewById(R.id.yes);
                Button no = holderView.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child(operation.getUid()).child(currentMonth).child("operations").child(objectId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Объект успешно удален
                                Log.d("TechOperationAdapter", "Объект удален из базы данных");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Произошла ошибка при удалении объекта
                                Log.d("TechOperationAdapter", "Ошибка при удалении объекта из базы данных: " + e.getMessage());
                            }
                        });
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
        }
    }
}
