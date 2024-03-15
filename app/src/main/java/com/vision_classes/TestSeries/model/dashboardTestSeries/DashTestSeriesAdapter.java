package com.vision_classes.TestSeries.model.dashboardTestSeries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.R;
import com.vision_classes.TestSeries.AllTestPageActivity;

import java.util.List;

public class DashTestSeriesAdapter extends RecyclerView.Adapter<DashTestSeriesAdapter.ViewHolder>{


    List<DashTestSeriesModel> dashTestSeriesModelList;
    Context context;

    public DashTestSeriesAdapter(List<DashTestSeriesModel> dashTestSeriesModelList, Context context) {
        this.dashTestSeriesModelList = dashTestSeriesModelList;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_package_course__card, parent, false);
        return new DashTestSeriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(dashTestSeriesModelList.get(position).getTitle());
        Glide.with(context).load(dashTestSeriesModelList.get(position).getImage())
                .into(holder.imageView);

        holder.describeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AllTestPageActivity.class);
                i.putExtra("id",dashTestSeriesModelList.get(position).getId());
                i.putExtra("subscriptionValidity","");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dashTestSeriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView  imageView;
        TextView title ;
        LinearLayout describeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.packageImg);
            title=itemView.findViewById(R.id.packageTitle);
            describeLayout=itemView.findViewById(R.id.describeLayout);


        }
    }
}
