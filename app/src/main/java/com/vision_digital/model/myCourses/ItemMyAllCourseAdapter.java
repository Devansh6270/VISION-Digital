package com.vision_digital.model.myCourses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.liveClass.LiveDetailsActivity;

import java.util.List;

public class ItemMyAllCourseAdapter extends RecyclerView.Adapter<ItemMyAllCourseAdapter.ItemMyCourseViewHolder> {

    List<ItemSubscribedCourse> myCourseList;
    Context context;

    public ItemMyAllCourseAdapter(List<ItemSubscribedCourse> myCourseList, Context context) {
        this.myCourseList = myCourseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemMyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemMyCourseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package_course__card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMyCourseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            holder.packageTitle.setText(myCourseList.get(position).getName());

            Glide.with(context).load(myCourseList.get(position).getImage()).into(holder.packageImage);

            String type=myCourseList.get(position).getType();


            holder.packagePrice.setVisibility(View.GONE);


            holder.describeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event for the forwordBtn here
                    // You can start a new activity or perform any other action
                    Log.e("click", "forwardBtn clicked");

                    if (type.equals("live")){
                        Intent courseIntent = new Intent(context, LiveDetailsActivity.class);
                        courseIntent.putExtra("id", myCourseList.get(position).getId());
                        context.startActivity(courseIntent);
                    }else if (type.equals("recorded")){
                        Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
                        courseIntent.putExtra("id", myCourseList.get(position).getId());
                        courseIntent.putExtra("title", myCourseList.get(position).getName());
                        context.startActivity(courseIntent);
                    }


                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myCourseList != null ? myCourseList.size() : 0;
    }

    public class ItemMyCourseViewHolder extends RecyclerView.ViewHolder {


        TextView packagePrice , packageDescription, packageTitle;
        ImageView packageImage;
        ImageView forwordBtn;
        LinearLayout describeLayout;
        public ItemMyCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            packageTitle=itemView.findViewById(R.id.packageTitle);
            packageDescription=itemView.findViewById(R.id.packageDescription);
            packagePrice=itemView.findViewById(R.id.packageDate);
            packageImage=itemView.findViewById(R.id.packageImg);


            describeLayout = itemView.findViewById(R.id.describeLayout);

        }
    }

}

