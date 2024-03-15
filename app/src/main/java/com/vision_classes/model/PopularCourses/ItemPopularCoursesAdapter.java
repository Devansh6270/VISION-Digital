package com.vision_classes.model.PopularCourses;

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
import com.vision_classes.R;
import com.vision_classes.TestSeries.OngoingTestActivity;
import com.vision_classes.TestSeries.model.testResultNew.NewResultActivity;
import com.vision_classes.activities.CourseDetailsActivity;
import com.vision_classes.model.myCourses.ItemMyCourse;

import java.util.List;

public class ItemPopularCoursesAdapter extends RecyclerView.Adapter<PopularCoursesViewHolder> {

    List<ItemMyCourse> myCourseList;
    Context context;

    public ItemPopularCoursesAdapter(List<ItemMyCourse> myCourseList) {
        this.myCourseList = myCourseList;
    }

    @NonNull
    @Override
    public PopularCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PopularCoursesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_courses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularCoursesViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            holder.teacherNameTV.setText(myCourseList.get(position).getOwner_name());
            holder.teacherQualification.setText("");
            //            holder.teacherQualification.setText(myCourseList.get(position).getOwner_qualification());


            Glide.with(context).load(myCourseList.get(position).getImage())
                    .into(holder.teacherImage);
            holder.classTitle.setText(myCourseList.get(position).getTitle());
            holder.price.setText("\u20B9"+myCourseList.get(position).getPrice());


            Log.e("Imagee URL ---------",""+myCourseList.get(position).getImage().toString());


            long second = Long.parseLong(myCourseList.get(position).getDuration());
            int hours = (int) second / 3600;
            int remainder = (int) second - hours * 3600;
            int mins = remainder / 60;
//            holder.startedAt.setText("Duration: " + Integer.parseInt(myCourseList.get(position).getDuration()) / 3600 + " Hrs");

            if (myCourseList.get(position).getDuration().equals("")){
                holder.startedAt.setVisibility(View.GONE);

            }else{
                holder.startedAt.setVisibility(View.GONE);


                if (hours < 10 && mins < 10) {
                    holder.startedAt.setText("Duration: 0" + hours + ":0" + mins + " Hrs");
                } else if (hours == 0 && mins < 10) {
                    holder.startedAt.setText("Duration: 00" + ":0" + mins + " Hrs");

                } else if (hours == 0 && mins > 10) {
                    holder.startedAt.setText("Duration: 00" + ":" + mins + " Hrs");

                } else if (hours > 10 && mins < 10) {
                    holder.startedAt.setText("Duration: " + hours + ":0" + mins + " Hrs");

                } else if (hours < 10 && mins > 10) {
                    holder.startedAt.setText("Duration: 0" + hours + ":" + mins + " Hrs");

                } else {
                    holder.startedAt.setText("Duration: " + hours + ":" + mins + " Hrs");
                }
            }



            holder.itemLiveClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","click");

                    if(myCourseList.get(position).getType().equals("TestSeries")) {
                        Intent courseIntent = new Intent(context, OngoingTestActivity.class);
                        courseIntent.putExtra("desc", "");
                        courseIntent.putExtra("price", "");
                        courseIntent.putExtra("testType", "single");
                        courseIntent.putExtra("id", myCourseList.get(position).getId());
                        context.startActivity(courseIntent);
                    }else if(myCourseList.get(position).getType().equals("PostTestSeries")){
                        Intent courseIntent = new Intent(context, NewResultActivity.class);
                        courseIntent.putExtra("id", myCourseList.get(position).getId());
                        context.startActivity(courseIntent);
                    }
                    else {
                        Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
                        courseIntent.putExtra("id", myCourseList.get(position).getId());
                        courseIntent.putExtra("image",myCourseList.get(position).getImage());
                        courseIntent.putExtra("fromActivity","homePage");
                        courseIntent.putExtra("forTask","explore");
                        courseIntent.putExtra("packageId"," ");
                        context.startActivity(courseIntent);
                    }
                }

            });
//            holder.itemLiveClass.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.e("click","click");
//
//                    if(myCourseList.get(position).getType().equals("TestSeries")) {
//                        Intent courseIntent = new Intent(context, TestDetailsActivity.class);
//                        courseIntent.putExtra("desc", "");
//                        courseIntent.putExtra("price", "");
//                        courseIntent.putExtra("testType", "single");
//                        courseIntent.putExtra("id", myCourseList.get(position).getId());
//                        context.startActivity(courseIntent);
//                    }else if(myCourseList.get(position).getType().equals("PostTestSeries")){
//                        Intent courseIntent = new Intent(context, NewResultActivity.class);
//                        courseIntent.putExtra("id", myCourseList.get(position).getId());
//                        context.startActivity(courseIntent);
//                    }
//                    else {
//                        Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
//                        courseIntent.putExtra("id", myCourseList.get(position).getId());
//                        courseIntent.putExtra("image",myCourseList.get(position).getImage());
//                        courseIntent.putExtra("fromActivity","homePage");
//                        context.startActivity(courseIntent);
//                    }
//                }
//            });

//        if(!model.getTeacherImage().equals(""))
//            Glide.with(DashboardActivity.this).load(model.getTeacherImage()).into(holder.teacherImage);
//            if (position % 2 == 0) {
//                holder.blueCard.setVisibility(View.GONE);
//            }else{
//                holder.blueCard.setVisibility(View.VISIBLE);
//            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myCourseList != null ? myCourseList.size() : 0;
    }
}
