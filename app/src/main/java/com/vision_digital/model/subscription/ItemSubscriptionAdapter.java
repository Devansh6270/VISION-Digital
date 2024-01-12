package com.vision_digital.model.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

import java.util.List;

public class ItemSubscriptionAdapter extends RecyclerView.Adapter<ItemSubscriptionViewHolder> {
    List<ItemSubscription> contentList;
    Context context;

    //
//    public ItemSubscriptionAdapter(List<ItemSubscription> contentList, Context context) {
//        this.contentList = contentList;
//
//    }
    public ItemSubscriptionAdapter(List<ItemSubscription> contentList) {
        this.contentList = contentList;
    }

//
//    public ItemSubscriptionAdapter(ArrayList<ItemSubscription> contentList) {
//    }

        @NonNull
        @Override
        public ItemSubscriptionViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType)
        {
            context = parent.getContext();
            return new ItemSubscriptionViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subscription_content, parent, false));
        }

        @Override
        public void onBindViewHolder (@NonNull ItemSubscriptionViewHolder holder,int position){
            int a=contentList.get(position).getNumber();
            holder.contentName.setText(a+". "+contentList.get(position).getTitle());

        }

        @Override
        public int getItemCount () {
            return contentList != null ? contentList.size() : 0;
        }
    }
