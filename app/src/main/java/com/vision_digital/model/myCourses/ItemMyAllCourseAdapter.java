package com.vision_digital.model.myCourses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.TestSeries.OngoingTestActivity;
import com.vision_digital.TestSeries.model.testResultNew.NewResultActivity;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.R;

import java.util.List;

public class ItemMyAllCourseAdapter extends RecyclerView.Adapter<ItemMyAllCourseAdapter.ItemMyCourseViewHolder> {

    List<ItemMyCourse> myCourseList;
    Context context;

    public ItemMyAllCourseAdapter(List<ItemMyCourse> myCourseList) {
        this.myCourseList = myCourseList;
    }

    @NonNull
    @Override
    public ItemMyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemMyCourseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_live_classes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMyCourseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            holder.teacherNameTV.setText(myCourseList.get(position).getOwner_name());
            holder.teacherQualification.setText("");
            //            holder.teacherQualification.setText(myCourseList.get(position).getOwner_qualification());

            holder.classTitle.setText(myCourseList.get(position).getTitle());

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


            Glide.with(context).load(myCourseList.get(position).getImage())
                    .into(holder.teacherImage);

            holder.letsLearn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                        Log.e("CourseID",myCourseList.get(position).getId());
                        courseIntent.putExtra("image",myCourseList.get(position).getImage());
                        courseIntent.putExtra("fromActivity","homePage");
                        courseIntent.putExtra("forTask","explore");
                        courseIntent.putExtra("forTask","explore");
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myCourseList != null ? myCourseList.size() : 0;
    }

    public class ItemMyCourseViewHolder extends RecyclerView.ViewHolder {

        public TextView teacherNameTV,classTitle,teacherQualification,startedAt;
        public ImageView blueCard, teacherImage;
        public CardView itemLiveClass;
        public Button letsLearn;
        public ItemMyCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherImage = itemView.findViewById(R.id.teacherImg);
            teacherNameTV = itemView.findViewById(R.id.teacherName);
            classTitle = itemView.findViewById(R.id.classTitle);
            teacherQualification = itemView.findViewById(R.id.techQuali);
            startedAt = itemView.findViewById(R.id.startedAt);
            letsLearn=itemView.findViewById(R.id.letsLearnBtn);

            itemLiveClass = itemView.findViewById(R.id.itemLiveClass);

        }
    }

}

