package com.vision_digital.TestSeries.model.testResultNew;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

import java.util.ArrayList;

public class ItemTopicWiseModelAdapter extends RecyclerView.Adapter<ItemTopicWiseModelAdapter.ViewHolder> {
    ArrayList<ItemTopicWiseModel> topicWiseModelArrayList;
    Context context;


    public ItemTopicWiseModelAdapter(ArrayList<ItemTopicWiseModel> topicWiseModelArrayList, Context context) {
        this.topicWiseModelArrayList = topicWiseModelArrayList;
        this.context = context;
    }

    public ItemTopicWiseModelAdapter(ArrayList<ItemTopicWiseModel> topicWiseModelArrayList) {
        this.topicWiseModelArrayList = topicWiseModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topicwise_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTopicName.setText(topicWiseModelArrayList.get(position).getTopicName());
        holder.itemCorrect.setText(topicWiseModelArrayList.get(position).correctTopic+"%");
        Log.e("topicwiseAdapter", topicWiseModelArrayList.get(position).getTopicName());

         }

    @Override
    public int getItemCount() {
        return topicWiseModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTopicName, itemCorrect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTopicName = itemView.findViewById(R.id.itemTopicName);
            itemCorrect = itemView.findViewById(R.id.itemCorrect);
        }
    }
}
