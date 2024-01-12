package com.vision_digital.community.communitymodels;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

public class ItemCommunityViewHolder extends RecyclerView.ViewHolder {

    public TextView communityTitle, lastMessage, lastMessageTime;
    public ImageView communityImage, lastMsgImage;

    public ItemCommunityViewHolder(@NonNull View itemView) {
        super(itemView);

        communityTitle = itemView.findViewById(R.id.communityTitle);
        communityImage = itemView.findViewById(R.id.communityImage);
        lastMessage = itemView.findViewById(R.id.last_chat);
        lastMessageTime = itemView.findViewById(R.id.last_chat_time);
        lastMsgImage = itemView.findViewById(R.id.last_chat_type);



    }
}
