package com.vision_digital.model.analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.activities.AnalyticsActivity;

import java.util.ArrayList;

public class ItemAllAnalyticsAdapter extends RecyclerView.Adapter<ItemAllAnalyticsAdapter.ItemAllAnalyticsViewholder> {
    Context context;
    ArrayList<ItemAnalytics> arrayList;

    public ItemAllAnalyticsAdapter(Context context, ArrayList<ItemAnalytics> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ItemAllAnalyticsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_analytics, parent, false);
//        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//       View view=inflater.inflate(R.layout.item_all_analytics, parent,false);
        return new ItemAllAnalyticsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAllAnalyticsViewholder holder, @SuppressLint("RecyclerView") final int position) {

        holder.courseTitle.setText(arrayList.get(position).getCourseTitle());
        holder.courseOwner.setText(arrayList.get(position).getCourseOwner());
        Log.e("image analytics",arrayList.get(position).getImage());
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

    public class ItemAllAnalyticsViewholder extends RecyclerView.ViewHolder {

        TextView courseTitle, courseOwner;
        ImageView image;
        CardView itemAnalytics;

        public ItemAllAnalyticsViewholder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.classTitle);
            courseOwner = itemView.findViewById(R.id.teacherName);
            image = itemView.findViewById(R.id.analyticsImage);
            itemAnalytics=itemView.findViewById(R.id.itemAnalytics);


        }

    }
}

