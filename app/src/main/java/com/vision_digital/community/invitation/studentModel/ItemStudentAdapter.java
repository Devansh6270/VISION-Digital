package com.vision_digital.community.invitation.studentModel;

import static android.content.Context.MODE_PRIVATE;
import static com.vision_digital.community.invitation.SelectStudentActivity.add_public_invitation;
import static com.vision_digital.community.invitation.SelectStudentActivity.added_public_invitation;
import static com.vision_digital.community.invitation.SelectStudentActivity.txt_public_invitation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemStudentAdapter extends RecyclerView.Adapter<ItemStudentAdapter.ViewHolder> {
    ArrayList<ItemStudent> studentArrayList;
    //    int listSize = studentArrayList.size();
    Context context;
    String stuName;
    boolean isSelectedMode = false;
    ArrayList<ItemStudent> selectedItems = new ArrayList<>();
    int currentPosition;
    public static ArrayList<String> invitedStudId = new ArrayList<>();
    public static ArrayList<String> invitedStudName = new ArrayList<>();
    String studentUserId;

    String[] selectedStudentId;
    public static String selectedAdmin = "";
    int clicked = 100;
    boolean ispublicInvitAation = false;

    public ItemStudentAdapter(ArrayList<ItemStudent> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    @NonNull
    @Override
    public ItemStudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_invitation_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemStudentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        currentPosition = position;
        studentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences studDetails = context.getSharedPreferences("CNB", MODE_PRIVATE);
        stuName = studDetails.getString("profileName", "NO_NAME");


        holder.student_select.setVisibility(View.GONE);
        holder.studentNameInvitation.setText(studentArrayList.get(position).getStudent_username());
//                Glide.with(getApplicationContext())
//                        .load(model.get())
//                        .into(holder.chat_image);
        Log.e("enter", studentArrayList.get(position).getStudent_username() + "");


        if (studentArrayList.get(position).isSelected()) {

            holder.student_select.setVisibility(View.VISIBLE);
            holder.student_select_rectangle.setVisibility(View.GONE);
        } else {
            holder.student_select.setVisibility(View.GONE);
            holder.student_select_rectangle.setVisibility(View.VISIBLE);
        }

        txt_public_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ispublicInvitAation) {

                    for (int i = 0; i < studentArrayList.size(); i++) {
//                        holder.student_select.setVisibility(View.GONE);
//                        holder.student_select_rectangle.setVisibility(View.VISIBLE);
                        selectedItems.remove(studentArrayList.get(i));
                        studentArrayList.get(i).setSelected(false);
                    }

                    add_public_invitation.setVisibility(View.VISIBLE);
                    added_public_invitation.setVisibility(View.GONE);
                    Log.e("invitedStud", String.valueOf(selectedItems));
                    invitedStudId.clear();
                    invitedStudName.clear();
                    ispublicInvitAation = false;
                    notifyDataSetChanged();

                } else {
                    invitedStudName.clear();
                    invitedStudId.clear();
                    invitedStudId.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    invitedStudName.add(stuName);
                    for (int i = 0; i < studentArrayList.size(); i++) {
                        selectedItems.add(studentArrayList.get(i));

                        invitedStudId.add(studentArrayList.get(i).student_id);

                        invitedStudName.add(studentArrayList.get(i).student_username);

//                        holder.student_select.setVisibility(View.VISIBLE);
//                        holder.student_select_rectangle.setVisibility(View.GONE);
                        studentArrayList.get(i).setSelected(true);
                        selectedAdmin = studentArrayList.get(i).getStudent_id();


                    }

                    Log.e("invitedStud", String.valueOf(invitedStudId));
                    add_public_invitation.setVisibility(View.GONE);
                    added_public_invitation.setVisibility(View.VISIBLE);
                    ispublicInvitAation = true;
                    notifyDataSetChanged();
                }

            }
        });

//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (selectedTime.length()>=3) {
//                    if (invitedStudId.size()>0) {
//                        if (!milestone_id.equals("")){
//                            Intent invitation = new Intent(context, CommunityChatPageActivity.class);
//                            invitation.putExtra("activity", fromActivitySelectStuAct);
//                            invitation.putExtra("milestone_id", milestone_id);
//                            invitation.putExtra("milestone_name", milestone_name);
//                            invitation.putExtra("videoPos", videoPosition);
//                            invitation.putExtra("community_id", community_id_select_student);
//                            invitation.putExtra("community_name", community_name_select_student);
//                            invitation.putExtra("community_logo", community_logo_select_student);
//                            invitation.putExtra("invitedStudId", invitedStudId);
//                            invitation.putExtra("invitedStudName", invitedStudName);
//                            Log.e("invitedStudId", String.valueOf(invitedStudId));
//                            invitation.putExtra("invitedTime", selectedTime);
//                            invitation.putExtra("selectedTimeStringLanguage", selectedTimeStringLanguage);
//                            invitation.putExtra("userNameString", userNameString);
//                            invitation.putExtra("timeInSting", timeInSting);
//                            Log.e("community_logo", community_logo_select_student);
//                            Log.e("invitedTime", selectedTime);
//                            context.startActivity(invitation);
//                        }else{
//                            Toast.makeText(context, "Go back and select video!...", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(context, "Please select your buddy!...", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(context, "Please select time!...", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }


    public void updateList(ArrayList<ItemStudent> list) {
        studentArrayList = list;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameInvitation;
        CircleImageView studentPicInvitation;
        ImageView student_select, student_select_rectangle;
        CardView card;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.e("enter", "entermethod" +
                    "HOLDER");

            student_select = itemView.findViewById(R.id.student_select);
            studentPicInvitation = itemView.findViewById(R.id.studentPicInvitation);
            studentNameInvitation = itemView.findViewById(R.id.studentNameInvitation);
            student_select_rectangle = itemView.findViewById(R.id.student_select_rectangle);
            card = itemView.findViewById(R.id.card);
            linearLayout = itemView.findViewById(R.id.linearLayout);


            itemView.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    if (studentArrayList.get(getAdapterPosition()).getActivity().equals("selectStudent")) {
                        isSelectedMode = true;
                        if (invitedStudId.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Log.e("contains", String.valueOf(invitedStudId));

                        } else {
                            invitedStudId.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            invitedStudName.add(stuName);
                        }

                        if (selectedItems.contains(studentArrayList.get(getAdapterPosition()))) {
//                        itemView.setBackgroundColor(Color.TRANSPARENT);
//                            student_select.setVisibility(View.GONE);
//                            student_select_rectangle.setVisibility(View.VISIBLE);
                            studentArrayList.get(getAdapterPosition()).setSelected(false);
                            selectedItems.remove(studentArrayList.get(getAdapterPosition()));
                            invitedStudId.remove(studentArrayList.get(getAdapterPosition()).student_id);
                            invitedStudName.remove(studentArrayList.get(getAdapterPosition()).student_username);
                            Log.e("invitedStud", String.valueOf(invitedStudId));

                        } else {
//                        itemView.setBackgroundResource(Color.parseColor("#000000"));
                            selectedItems.add(studentArrayList.get(getAdapterPosition()));
                            invitedStudId.add(studentArrayList.get(getAdapterPosition()).student_id);
                            invitedStudName.add(studentArrayList.get(getAdapterPosition()).student_username);
                            Log.e("invitedStud", String.valueOf(invitedStudId));
//                            student_select.setVisibility(View.VISIBLE);
//                            student_select_rectangle.setVisibility(View.GONE);
                            studentArrayList.get(getAdapterPosition()).setSelected(true);
                            selectedAdmin = studentArrayList.get(getAdapterPosition()).getStudent_id();

                        }

                    } else {

                        if (clicked != getAdapterPosition()) {
                            for (int i = 0; i < studentArrayList.size(); i++) {
                                if (i == getAdapterPosition()) {
                                    studentArrayList.get(i).setSelected(true);
                                    clicked = getAdapterPosition();
                                    Log.e("aaaaa", studentArrayList.get(i).getStudent_id());
                                    selectedItems.add(studentArrayList.get(getAdapterPosition()));
                                    invitedStudId.add(studentArrayList.get(getAdapterPosition()).student_id);
                                    invitedStudName.add(studentArrayList.get(getAdapterPosition()).student_username);
                                    selectedAdmin = studentArrayList.get(getAdapterPosition()).getStudent_id();
                                } else {
                                    studentArrayList.get(i).setSelected(false);
                                    selectedItems.remove(studentArrayList.get(getAdapterPosition()));
                                }
                            }
                            notifyDataSetChanged();
                        } else {
                            studentArrayList.get(getAdapterPosition()).setSelected(false);
                            notifyDataSetChanged();
                        }
                    }


                    if (selectedItems.size() == 0)
                        student_select.setVisibility(View.GONE);
                    isSelectedMode = false;

                    notifyDataSetChanged();
                }
            });


        }
    }
}
