package com.vision_digital.model.ImageSlider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.vision_digital.CoursePackage.CoursePackageActivity;
import com.vision_digital.R;
import com.vision_digital.TestSeries.AllTestPageActivity;
import com.vision_digital.TestSeries.OngoingTestActivity;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.liveClass.LiveDetailsActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    ArrayList<SliderModel> sliderModelArrayList;

    Context context;


    public SliderAdapter(ArrayList<SliderModel> sliderModelArrayList, Context context) {
        this.sliderModelArrayList = sliderModelArrayList;
        this.context = context;
    }



    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slider, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {







            Glide.with(context).load(sliderModelArrayList.get(position).getImage())
                    .into(holder.mySlider);



            holder.mySlider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click","click");
                    if (sliderModelArrayList.get(position).getCourseId().equals("0")){

                    }else{
                        if(sliderModelArrayList.get(position).getCourseType().equals("course")) {
                            Intent courseIntent = new Intent(context, CourseDetailsActivity.class);
                            courseIntent.putExtra("id", sliderModelArrayList.get(position).getCourseId());
                            courseIntent.putExtra("image", sliderModelArrayList.get(position).getImage());
                            courseIntent.putExtra("fromActivity", "Dashboard");
                            courseIntent.putExtra("forTask", "Explore");
                            context.startActivity(courseIntent);
                        }else if(sliderModelArrayList.get(position).getCourseType().equals("live")){
                            Intent courseIntent = new Intent(context, LiveDetailsActivity.class);
                            courseIntent.putExtra("id", sliderModelArrayList.get(position).getCourseId());
                            context.startActivity(courseIntent);
                        }
                        else if(sliderModelArrayList.get(position).getCourseType().equals("package")){
                            Intent courseIntent = new Intent(context, CoursePackageActivity.class);
                            courseIntent.putExtra("id", sliderModelArrayList.get(position).getCourseId());
                            courseIntent.putExtra("image", sliderModelArrayList.get(position).getImage());
                            context.startActivity(courseIntent);
                        }

                        else if(sliderModelArrayList.get(position).getCourseType().equals("testseries")){
                            Intent courseIntent = new Intent(context, AllTestPageActivity.class);
                            courseIntent.putExtra("id", sliderModelArrayList.get(position).getCourseId());
                            courseIntent.putExtra("subscriptionValidity", " ");
                            context.startActivity(courseIntent);
                        }
                    }
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
        return sliderModelArrayList.size();
    }

    public class SliderViewHolder extends ViewHolder{

        ImageView mySlider;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            mySlider=itemView.findViewById(R.id.mySlider);
        }
    }
}
