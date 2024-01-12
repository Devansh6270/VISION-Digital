package com.vision_digital.model.analytics;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vision_digital.R;

import java.util.List;

public class ItemStrongWeakAdapter extends RecyclerView.Adapter<ItemStrongWeakAdapter.ViewHolder> {
    List<ItemStrongWeakOnEdge> itemStrongWeakOnEdgeList;
    Context context;

    public ItemStrongWeakAdapter(List<ItemStrongWeakOnEdge> itemStrongWeakOnEdgeList) {
        this.itemStrongWeakOnEdgeList = itemStrongWeakOnEdgeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_analytics_strong_weak_layout, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(itemStrongWeakOnEdgeList.get(position).getType().equals("groupPosition")){
            holder.percentage.setVisibility(View.VISIBLE);
            holder.percentage.setText(itemStrongWeakOnEdgeList.get(position).getRanking());
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.bullet_point.setVisibility(View.GONE);

        }else if(itemStrongWeakOnEdgeList.get(position).getType().equals("accuracy")){
            holder.percentage.setVisibility(View.VISIBLE);
            holder.percentage.setText(itemStrongWeakOnEdgeList.get(position).getRank()+"%");
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setProgress((int) (itemStrongWeakOnEdgeList.get(position).getRank()));
//            if ((int) itemStrongWeakOnEdgeList.get(position).getProgress()<50){
//                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
//            }
//            else {
//                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
//            }

            holder.bullet_point.setVisibility(View.GONE);

        }else if (itemStrongWeakOnEdgeList.get(position).getType().equals("status")) {
            holder.percentage.setVisibility(View.VISIBLE);
            holder.percentage.setText(itemStrongWeakOnEdgeList.get(position).getStatus());
            holder.progressBar.setVisibility(View.INVISIBLE);
//            if ((int) itemStrongWeakOnEdgeList.get(position).getProgress()<50){
//                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
//            } else {
//                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
//            }
            holder.bullet_point.setVisibility(View.GONE);
        } else{
            holder.percentage.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.bullet_point.setVisibility(View.VISIBLE);


        }

        double progress = itemStrongWeakOnEdgeList.get(position).getProgress();
        if (progress<50){
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        } else {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
        holder.chapter_name.setText(itemStrongWeakOnEdgeList.get(position).getChapter_name());


    }

    @Override
    public int getItemCount() {
        return itemStrongWeakOnEdgeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bullet_point;
        TextView chapter_name;
        TextView percentage;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bullet_point = itemView.findViewById(R.id.bullet_point);
            chapter_name = itemView.findViewById(R.id.chapter_name);
            percentage = itemView.findViewById(R.id.percentage);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }
}
