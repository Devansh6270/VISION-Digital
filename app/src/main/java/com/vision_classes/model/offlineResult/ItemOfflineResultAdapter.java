package com.vision_classes.model.offlineResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

import java.util.ArrayList;

public class ItemOfflineResultAdapter extends RecyclerView.Adapter<ItemOfflineResultAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemOfflineResultList> itemOfflineResultLists;
    public ItemOfflineResultAdapter(Context context, ArrayList<ItemOfflineResultList> itemOfflineResultLists){
        this.context = context;
        this.itemOfflineResultLists = itemOfflineResultLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offline_result_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvBatchNumber.setText(itemOfflineResultLists.get(position).getBatchNumber());
        holder.tvTestDate.setText(itemOfflineResultLists.get(position).getTestDate());
        holder.tvPhysics.setText(itemOfflineResultLists.get(position).getPhysics());
        holder.tvChemistry.setText(itemOfflineResultLists.get(position).getChemistry());
        holder.tvBiology.setText(itemOfflineResultLists.get(position).getBiology());
        holder.tvMaths.setText(itemOfflineResultLists.get(position).getMaths());
        holder.tvPercentage.setText(itemOfflineResultLists.get(position).getPercentage());
        holder.tvRank.setText(itemOfflineResultLists.get(position).getRank());
        holder.tvTotalMarks.setText(itemOfflineResultLists.get(position).getTotalMarks());
    }

    @Override
    public int getItemCount() {
        return itemOfflineResultLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBatchNumber, tvTestDate, tvPhysics, tvChemistry, tvBiology, tvMaths, tvRank, tvTotalMarks, tvPercentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBatchNumber = itemView.findViewById(R.id.tvBatchNumber);
            tvTestDate = itemView.findViewById(R.id.tvTestDate);
            tvPhysics = itemView.findViewById(R.id.tvPhysics);
            tvChemistry = itemView.findViewById(R.id.tvChemistry);
            tvBiology = itemView.findViewById(R.id.tvBiology);
            tvMaths = itemView.findViewById(R.id.tvMaths);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvTotalMarks = itemView.findViewById(R.id.tvTotalMarks);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
        }
    }
}
