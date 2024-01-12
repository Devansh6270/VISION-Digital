package com.vision_digital.community.groupMileStoneModel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemGroupSuggestedMileViewHolder extends RecyclerView.ViewHolder {

    TextView mileStoneName,mileStoneDuration;
    View itemView;
    public ItemGroupSuggestedMileViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        mileStoneName = itemView.findViewById(R.id.mileStoneName);
        mileStoneDuration = itemView.findViewById(R.id.mileStoneDuration);
    }
}
