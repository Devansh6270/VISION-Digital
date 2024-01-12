package com.vision_digital.model.searchQueryMilestone.jump_to_searched_video;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JumpModelAdapter extends RecyclerView.Adapter<JumpModelAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mileStoneName;
        TextView mileStoneDuration;
        TextView ownedBy;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
