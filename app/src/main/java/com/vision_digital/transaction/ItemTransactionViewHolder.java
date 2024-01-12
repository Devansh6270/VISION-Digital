package com.vision_digital.transaction;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemTransactionViewHolder extends RecyclerView.ViewHolder {
    TextView orderId, amount, status, date;

    public ItemTransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.order_id);
        amount = itemView.findViewById(R.id.amount);
        status = itemView.findViewById(R.id.status);
        date = itemView.findViewById(R.id.order_date);


    }
}
