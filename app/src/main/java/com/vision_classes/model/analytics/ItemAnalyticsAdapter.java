package com.vision_classes.model.analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.R;
import com.vision_classes.activities.AnalyticsActivity;

import java.util.ArrayList;

public class ItemAnalyticsAdapter extends RecyclerView.Adapter<ItemAnalyticsViewholder> {
    Context context;
    ArrayList<ItemAnalytics> arrayList;

    public ItemAnalyticsAdapter(Context context, ArrayList<ItemAnalytics> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ItemAnalyticsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_analytics,parent,false);
        return new ItemAnalyticsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAnalyticsViewholder holder,@SuppressLint("RecyclerView") final int position) {

        holder.courseTitle.setText(arrayList.get(position).getCourseTitle());
        holder.courseOwner.setText(arrayList.get(position).getCourseOwner());
        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.image);
       // holder.itemAnalytics.setLayoutParams(new ViewGroup.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (arrayList.get(position).getType().equals("analytics")) {
                    Intent analyticsIntent = new Intent(context, AnalyticsActivity.class);
                analyticsIntent.putExtra("id", String.valueOf(arrayList.get(position).getId()));
                analyticsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(analyticsIntent);
//                } else {
////                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                }
            }
        });



    }





    @Override
    public int getItemCount() {
       // return arrayList != null ? arrayList.size() : 0;
        return arrayList.size();
    }

//        public class ItemAnalyticsViewholder extends RecyclerView.ViewHolder {
//
//            TextView courseTitle, courseOwner;
//
//            public ItemAnalyticsViewholder(@NonNull View itemView) {
//                super(itemView);
//                courseTitle = itemView.findViewById(R.id.classTitle);
//                courseOwner = itemView.findViewById(R.id.teacherName);
//
//            }
//        }
//
        }
