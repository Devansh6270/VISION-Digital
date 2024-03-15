package com.vision_classes.model.suggestedMileStone;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;

class ItemSuggestedMileStoneViewHolder extends RecyclerView.ViewHolder {

TextView mileStoneName,mileStoneDuration;
View itemView;
    ItemSuggestedMileStoneViewHolder(@NonNull View itemView) {
       super(itemView);
       this.itemView = itemView;
       mileStoneName = itemView.findViewById(R.id.mileStoneName);
       mileStoneDuration = itemView.findViewById(R.id.mileStoneDuration);
   }
}
