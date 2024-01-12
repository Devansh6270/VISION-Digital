package com.vision_digital.model.popularTeachers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.activities.TeachersDetailsActivity;

import java.util.List;

public class ItemTeacherAdapter extends RecyclerView.Adapter<ItemTeacherViewHolder> {

    List<ItemTeacher> itemTeacherList;
    Context context;

    public ItemTeacherAdapter(List<ItemTeacher> itemTeacherList) {
        this.itemTeacherList = itemTeacherList;
    }

    @NonNull
    @Override
    public ItemTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        if (viewType == 0) {
            return new ItemTeacherViewHolder(LayoutInflater.from(context).inflate(R.layout.item_popular_teacher, parent, false));

        } else {
            return new ItemTeacherViewHolder(LayoutInflater.from(context).inflate(R.layout.item_popular_institute, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemTeacherList.get(position).getType().equals("teacher")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTeacherViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.subject.setText(itemTeacherList.get(position).getQualification());
        holder.instituteName.setText(itemTeacherList.get(position).getCity() + ", " + itemTeacherList.get(position).getCountry());
        holder.name.setText(itemTeacherList.get(position).getName());
        Glide.with(context).load(itemTeacherList.get(position).getImage()).into(holder.image);
        holder.subscriber.setText(itemTeacherList.get(position).getSubscriber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemTeacherList.get(position).getType().equals("teacher")) {
                    Intent teacherIntent = new Intent(context, TeachersDetailsActivity.class);
                    teacherIntent.putExtra("id", String.valueOf(itemTeacherList.get(position).getId()));

                    context.startActivity(teacherIntent);
                } else {
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemTeacherList.size();
    }
}
