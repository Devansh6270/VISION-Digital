package com.vision_classes.model.liveClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;


import java.util.List;

public class ItemLiveClassesAdapter extends RecyclerView.Adapter<ItemLiveClassViewHolder> {

    List<ItemLiveClass> liveClassList;
    Context context;

    public ItemLiveClassesAdapter(List<ItemLiveClass> liveClassList) {
        this.liveClassList = liveClassList;
    }

    @NonNull
    @Override
    public ItemLiveClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemLiveClassViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_live_courses_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemLiveClassViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {


//            holder.courseTitle.setText(liveClassList.get(position).getTitle());
//            holder.courseLanguage.setText(liveClassList.get(position).getLanguage());
//
//
//
//            Glide.with(context).load(liveClassList.get(position).getImage())
//                    .into(holder.courseImage);
//            holder.courseCard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("click","click");
//
//                      Intent courseIntent = new Intent(context, LiveDetailsActivity.class);
//                        courseIntent.putExtra("id", liveClassList.get(position).getId());
//                        courseIntent.putExtra("image",liveClassList.get(position).getImage());
//                        courseIntent.putExtra("fromActivity","homePage");
//                        courseIntent.putExtra("forTask","explore");
//                        context.startActivity(courseIntent);
//
//                }
//            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return liveClassList != null ? liveClassList.size() : 0;
    }
}