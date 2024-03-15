package com.vision_classes.model.subscription;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

public class ItemSubscriptionViewHolder extends RecyclerView.ViewHolder {
    TextView contentName;
    public ItemSubscriptionViewHolder(@NonNull View itemView) {
        super(itemView);
        contentName = itemView.findViewById(R.id.content);
    }
}
