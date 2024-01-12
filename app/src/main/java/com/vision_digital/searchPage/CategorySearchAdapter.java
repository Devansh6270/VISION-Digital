package com.vision_digital.searchPage;

import static com.vision_digital.activities.SearchActivity.coursesList;
import static com.vision_digital.activities.SearchActivity.otherResultList;
import static com.vision_digital.activities.SearchActivity.teachersList;
import static com.vision_digital.activities.SearchActivity.topResultList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;

import java.util.List;

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.ViewHolder> {
    @NonNull
    List<CategorySearchModel> categorySearchModelList;
    int clicked = 100;


    public CategorySearchAdapter(List<CategorySearchModel> categorySearchModelList) {
        this.categorySearchModelList = categorySearchModelList;
    }

    @NonNull
    @Override
    public CategorySearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_category_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorySearchAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(categorySearchModelList.get(position).getCategoryName());


        for (int a=0; a<categorySearchModelList.size(); a++){
            if (categorySearchModelList.get(position).isSelected) {

                holder.item_red_layout.setVisibility(View.VISIBLE);
                holder.name.setTextColor(Color.parseColor("#ffffff"));
                loadData(position);
            }
            else{
                holder.item_red_layout.setVisibility(View.GONE);
                holder.name.setTextColor(Color.parseColor("#1893C5"));



            }

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (clicked != position) {
                for (int i = 0; i < categorySearchModelList.size(); i++) {
                    if (i == position) {
                        categorySearchModelList.get(i).setSelected(true);
                        loadData(position);
                        clicked = position;
                        Log.e("isSelected Category", "true");


                    } else {

                        categorySearchModelList.get(i).setSelected(false);
                    }
                }

                notifyDataSetChanged();


            }
        });
    }

    public void loadData(int position) {
        if (categorySearchModelList.get(position).getCategoryName().equals("Milestone"))
//        if (position==0)

        {
            topResultList.setVisibility(View.VISIBLE);
            otherResultList.setVisibility(View.GONE);
            coursesList.setVisibility(View.GONE);
            teachersList.setVisibility(View.GONE);

        }
        if (categorySearchModelList.get(position).getCategoryName().equals("Course")) {
            topResultList.setVisibility(View.GONE);
            otherResultList.setVisibility(View.GONE);
            coursesList.setVisibility(View.VISIBLE);
            teachersList.setVisibility(View.GONE);
            topResultList.setVisibility(View.GONE);
            topResultList.setVisibility(View.GONE);


        }
        if (categorySearchModelList.get(position).getCategoryName().equals("Teacher")) {
            topResultList.setVisibility(View.GONE);
            otherResultList.setVisibility(View.GONE);
            coursesList.setVisibility(View.GONE);
            teachersList.setVisibility(View.VISIBLE);
            topResultList.setVisibility(View.GONE);



        }
        if (categorySearchModelList.get(position).getCategoryName().equals("School")) {
            topResultList.setVisibility(View.GONE);
            otherResultList.setVisibility(View.VISIBLE);
            coursesList.setVisibility(View.GONE);
            teachersList.setVisibility(View.GONE);
            topResultList.setVisibility(View.GONE);



        }

    }

    @Override
    public int getItemCount() {
        return categorySearchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        RelativeLayout item_linear_layout;
        LinearLayout item_red_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            item_linear_layout = itemView.findViewById(R.id.item_linear_layout);
            item_red_layout = itemView.findViewById(R.id.item_red_layout);


        }

    }
}
