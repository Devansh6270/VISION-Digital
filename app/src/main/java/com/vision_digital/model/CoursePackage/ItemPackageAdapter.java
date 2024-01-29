package com.vision_digital.model.CoursePackage;

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
import com.vision_digital.CoursePackage.CoursePackageActivity;
import com.vision_digital.R;


import java.util.List;

public class ItemPackageAdapter  extends RecyclerView.Adapter<ItemPackageViewHolder> {

    List<ItemPackageList> packageList;
    Context context;

    public ItemPackageAdapter(List<ItemPackageList> packageList, Context context) {
        this.packageList = packageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemPackageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPackageViewHolder holder,@SuppressLint("RecyclerView") final int position) {
        try {
            holder.teacherNameTV.setText(packageList.get(position).getOwnerName());

            holder.classTitle.setText(packageList.get(position).getTitle());


            Glide.with(context).load(packageList.get(position).getImage())
                    .into(holder.teacherImage);

            holder.itemLiveClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click","click");

                    Intent courseIntent = new Intent(context, CoursePackageActivity.class);
                    courseIntent.putExtra("id", packageList.get(position).getId());
                    courseIntent.putExtra("image",packageList.get(position).getImage());
                    courseIntent.putExtra("fromActivity","homePage");
                    courseIntent.putExtra("forTask","learn");
                    courseIntent.putExtra("packageId",packageList.get(position).getId());
                    context.startActivity(courseIntent);
                    }

            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return packageList != null ? packageList.size() : 0;
    }
}
