package com.vision_digital.model.InstallmentOrderList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

import java.util.ArrayList;

public class ItemInstallmentOrderAdapter extends RecyclerView.Adapter<ItemInstallmentOrderAdapter.ViewHolder> {

    Context context;
    ArrayList<ItemInstallmentOrderList> itemInstallmentOrderLists;

    public ItemInstallmentOrderAdapter(Context context, ArrayList<ItemInstallmentOrderList> itemInstallmentOrderLists){
        this.context = context;
        this.itemInstallmentOrderLists = itemInstallmentOrderLists;
    }
    @NonNull
    @Override
    public ItemInstallmentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_installment_order_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemInstallmentOrderAdapter.ViewHolder holder, int position) {

        holder.tvOrderId.setText(itemInstallmentOrderLists.get(position).getOrder_id());
        holder.tvPaidDate.setText(itemInstallmentOrderLists.get(position).getPaid_date());
        holder.tvPaymentType.setText(itemInstallmentOrderLists.get(position).getPayment_type());
        holder.tvAmount.setText("Amount: "+itemInstallmentOrderLists.get(position).getAmount()+" Rs");
    }

    @Override
    public int getItemCount() {
        return itemInstallmentOrderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvPaidDate, tvPaymentType, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvPaidDate = itemView.findViewById(R.id.tvPaidDate);
            tvPaymentType = itemView.findViewById(R.id.tvPaymentType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
