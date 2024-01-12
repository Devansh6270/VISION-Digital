package com.vision_digital.model.suggestionTxtMsgs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class SuggestionTxtMsgsViewHolder extends RecyclerView.ViewHolder {

    public TextView msgContet;

    public SuggestionTxtMsgsViewHolder(@NonNull View itemView) {
        super(itemView);
        msgContet = itemView.findViewById(R.id.SuggestionTxtMsg);
    }
}
