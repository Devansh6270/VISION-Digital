package com.vision_classes.model.CoursePackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.R;
import com.vision_classes.TestSeries.AllTestPageActivity;
import com.vision_classes.TestSeries.OngoingTestActivity;
import com.vision_classes.activities.CourseDetailsActivity;
import com.vision_classes.liveClass.LiveDetailsActivity;

import java.util.ArrayList;

public class ItemTypesPackageAdapter extends RecyclerView.Adapter<ItemTypesPackageAdapter.ViewHolder> {


    ArrayList<ItemTypesPackage> itemList;
    Context context;

    public ItemTypesPackageAdapter(ArrayList<ItemTypesPackage> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_package, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") final int position) {
        try {
            holder.topic.setText(itemList.get(position).getProductName());
            holder.subTitle.setText(itemList.get(position).getProductType());





            Glide.with(context).load(itemList.get(position).getProductImage())
                    .into(holder.image);

            if (itemList.get(position).getStatus()=="lock"){
                holder.lockedBtn.setVisibility(View.VISIBLE);
                holder.openBtn.setVisibility(View.GONE);
            }else{
                holder.lockedBtn.setVisibility(View.GONE);
                holder.openBtn.setVisibility(View.VISIBLE);
            }
            holder.openBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click","click");
                    String type=itemList.get(position).getProductType();
                    if(type.equals("course")){
                        Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
                        courseIntent.putExtra("id", itemList.get(position).getProductId());
                        courseIntent.putExtra("image",itemList.get(position).getProductImage());
                        courseIntent.putExtra("fromActivity","homePage");
                        courseIntent.putExtra("forTask","learn");
                        courseIntent.putExtra("packageId",itemList.get(position).getPackageId());
                        context.startActivity(courseIntent);
                    } else if (type.equals("test")) {
                        Intent courseIntent = new Intent(context, OngoingTestActivity.class);
                        courseIntent.putExtra("id", itemList.get(position).getProductId());
                        courseIntent.putExtra("testType", itemList.get(position).getProductId());
                        courseIntent.putExtra("price", itemList.get(position).getProductId());
                        courseIntent.putExtra("desc", itemList.get(position).getPackageId());
                        courseIntent.putExtra("packageId",itemList.get(position).getPackageId());
                        context.startActivity(courseIntent);
                    }
                    else if (type.equals("testseries")) {
                        Intent courseIntent = new Intent(context, AllTestPageActivity.class);
                        courseIntent.putExtra("id", itemList.get(position).getProductId());
                        courseIntent.putExtra("testType", itemList.get(position).getProductId());
                        courseIntent.putExtra("price", itemList.get(position).getPackageId());
                        courseIntent.putExtra("subscriptionValidity","");
                        courseIntent.putExtra("packageId",itemList.get(position).getPackageId());
                        context.startActivity(courseIntent);
                    }
                    else if (type.equals("live")) {
                        Intent courseIntent = new Intent(context, LiveDetailsActivity.class);
                        courseIntent.putExtra("id", itemList.get(position).getProductId());
                        courseIntent.putExtra("packageId",itemList.get(position).getPackageId());
                        context.startActivity(courseIntent);
                    }

                }

            });

            holder.lockedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "This is not Purchased!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
         return itemList != null ? itemList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image,lockedBtn;
        TextView   topic , subTitle , openBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image =itemView.findViewById(R.id.itemImage);
            topic=itemView.findViewById(R.id.itemTopicName);
            subTitle=itemView.findViewById(R.id.itemSubTitle);
            openBtn=itemView.findViewById(R.id.openBtn);
            lockedBtn=itemView.findViewById(R.id.lockedBtn);
        }
    }
}