package com.vision_digital.community.studentModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemStudentCommDetailsAdapter extends RecyclerView.Adapter<ItemStudentCommDetailsAdapter.ViewHolder>{
    ArrayList<ItemStudents> studentArrayList;
    //    int listSize = studentArrayList.size();
    Context context;

    public ItemStudentCommDetailsAdapter(ArrayList<ItemStudents> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    @NonNull
    @Override
    public ItemStudentCommDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_invitation_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemStudentCommDetailsAdapter.ViewHolder holder, int position) {
        holder.student_select.setVisibility(View.GONE);
        holder.student_select_rectangle.setVisibility(View.GONE);


        holder.student_select.setVisibility(View.GONE);
        holder.studentNameInvitation.setText(studentArrayList.get(position).getStudent_username());
        holder.studentPicInvitation.setVisibility(View.VISIBLE);
        holder.studentUserNameInvitation.setVisibility(View.VISIBLE);
//                holder.studentUserNameInvitation.setText(model.getStudent_username());
        if (!studentArrayList.get(position).getUser_image().equals("")){
            Glide.with(context)
                    .load(studentArrayList.get(position).getUser_image())
                    .into(holder.studentPicInvitation);
        }else{
            Glide.with(context)
                    .load(R.drawable.user_profile)
                    .into(holder.studentPicInvitation);
        }

        holder.studentUserNameInvitation.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();    }


    public void updateList(ArrayList<ItemStudents> list){
        studentArrayList = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameInvitation, studentUserNameInvitation;
        CircleImageView studentPicInvitation;
        ImageView student_select,student_select_rectangle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            student_select = itemView.findViewById(R.id.student_select);
            studentPicInvitation = itemView.findViewById(R.id.studentPicInvitation);
            studentNameInvitation = itemView.findViewById(R.id.studentNameInvitation);
            studentUserNameInvitation = itemView.findViewById(R.id.studentUserNameInvitation);
            student_select_rectangle = itemView.findViewById(R.id.student_select_rectangle);

        }
    }
}
