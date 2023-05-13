package com.example.factory.screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.factory.R;
import com.example.factory.modules.Operation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class OperationAdapter extends FirebaseRecyclerAdapter<Operation, OperationAdapter.OperationViewHolder>{

    public Context context;

    public OperationAdapter(@NonNull FirebaseRecyclerOptions<Operation> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull OperationViewHolder holder, int position, @NonNull Operation operation) {
        holder.name.setText(operation.getName());
        holder.model.setText(operation.getModel());
        holder.size.setText(operation.getSize());
        holder.amount.setText(operation.getAmount());
        holder.amountOfDone.setText(operation.getAmountOfDone());
        holder.sum.setText(operation.getSum());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.content))
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
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

        TextView name, model, size, amount, amountOfDone, sum;
        ImageView edit;

        public OperationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameOperation);
            model = itemView.findViewById(R.id.nameModel);
            size = itemView.findViewById(R.id.size);
            amount = itemView.findViewById(R.id.amount);
            amountOfDone = itemView.findViewById(R.id.amountOfDone);
            sum = itemView.findViewById(R.id.sum);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}
