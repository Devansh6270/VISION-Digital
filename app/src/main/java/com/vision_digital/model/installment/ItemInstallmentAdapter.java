package com.vision_digital.model.installment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

import java.util.ArrayList;

public class ItemInstallmentAdapter extends RecyclerView.Adapter<ItemInstallmentAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemInstallmentList> itemInstallmentLists;

    public ItemInstallmentAdapter(Context context, ArrayList<ItemInstallmentList> itemInstallmentLists){
        this.context = context;
        this.itemInstallmentLists = itemInstallmentLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_installment_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvInstallmentNumber.setText(itemInstallmentLists.get(position).getInstallmentNumber());
        holder.tvPendingAmount.setText(itemInstallmentLists.get(position).getPendingAmount());
        holder.tvDueDate.setText(itemInstallmentLists.get(position).getDueDate());
        holder.tvDate.setText(itemInstallmentLists.get(position).getDate());
        holder.tvAmount.setText(itemInstallmentLists.get(position).getAmount());

        if (itemInstallmentLists.get(position).getPayment_status().equals("paid")){
            holder.btnPay.setText("Paid");
            holder.btnPay.setBackgroundResource(R.drawable.button_grey);
            holder.btnPay.setTextColor(Color.WHITE);
            holder.btnPay.setActivated(false);
        }

    }

    @Override
    public int getItemCount() {
        return itemInstallmentLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInstallmentNumber, tvPendingAmount, tvDueDate, tvDate, tvAmount;
        Button btnPay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInstallmentNumber = itemView.findViewById(R.id.tvInstallmentNumber);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvPendingAmount = itemView.findViewById(R.id.tvPendingAmount);
            btnPay = itemView.findViewById(R.id.btnPay);
        }
    }
}
