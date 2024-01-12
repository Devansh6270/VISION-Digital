package com.vision_digital.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemTransactionAdapter extends RecyclerView.Adapter<ItemTransactionViewHolder> {
    Context context;
    List<ItemTansaction> tansactionList;

    public ItemTransactionAdapter(List<ItemTansaction> tansactionList) {
        this.tansactionList = tansactionList;
    }

    @NonNull
    @Override
    public ItemTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemTransactionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemTransactionViewHolder holder, final int position) {
        Date orderDate;
        String orderDateString = tansactionList.get(position).getDate();
        holder.orderId.setText(tansactionList.get(position).getOrderId());
        final String orderIdString = tansactionList.get(position).getOrderId();
        holder.amount.setText("Rs." + tansactionList.get(position).getAmount() + "/-");

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
//
        try {
            orderDate = inputFormatter.parse(orderDateString);
            String  orderTime = outputFormatter.format(orderDate);
            holder.date.setText(orderTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (tansactionList.get(position).getStatus().equals("success") || tansactionList.get(position).getStatus().equals("Success") || tansactionList.get(position).getStatus().equals("SUCCESS") ){
            holder.status.setTextColor(Color.parseColor("#03980A"));
            holder.status.setText(tansactionList.get(position).getStatus());
        }else{
            holder.status.setTextColor(Color.parseColor("#D30404"));
            holder.status.setText(tansactionList.get(position).getStatus());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent td = new Intent(context, TransactionDetails.class);
                td.putExtra("sid", TransactionHistory.studId);
                td.putExtra("order_id",orderIdString);
                context.startActivity(td);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tansactionList != null ? tansactionList.size() : 0;
    }
}
