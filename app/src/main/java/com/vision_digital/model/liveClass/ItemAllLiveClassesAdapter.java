package com.vision_digital.model.liveClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;

import java.util.List;

public class ItemAllLiveClassesAdapter extends RecyclerView.Adapter<ItemAllLiveClassViewHolder> {

    List<ItemLiveClass> liveClassList;
    Context context;

    public ItemAllLiveClassesAdapter(List<ItemLiveClass> liveClassList) {
        this.liveClassList = liveClassList;
    }

    @NonNull
    @Override
    public ItemAllLiveClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemAllLiveClassViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_live_courses_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAllLiveClassViewHolder holder, @SuppressLint("RecyclerView") final int position) {
 //       try {

//
//            holder.courseTitle.setText(liveClassList.get(position).getTitle());
//            holder.courseStartEndDate.setText(liveClassList.get(position).getSessionStartDate() +" "+ liveClassList.get(position).getSessionEndDate());
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
//                    Intent courseIntent = new Intent(context, LiveDetailsActivity.class);
//                    courseIntent.putExtra("id", liveClassList.get(position).getId());
//                    context.startActivity(courseIntent);
//
//                }
//            });
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return liveClassList != null ? liveClassList.size() : 0;
    }
}
