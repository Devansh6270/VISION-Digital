package com.vision_digital.community.communitymodels;

import static android.content.Context.MODE_PRIVATE;

import static com.vision_digital.community.CommunitiesListActivity.profileImageUrlChatList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.community.CommunityChatPageActivity;
import com.vision_digital.community.studentModel.ItemStudents;
import com.vision_digital.model.myCourses.ItemMyCourse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemCommunityAdapter extends RecyclerView.Adapter<ItemCommunityAdapter.myviewholder> {

    Context context;
    ArrayList<CommunityItem> communityItemArrayList;
    String community_id;
    String studentName;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    int pos;


    public ItemCommunityAdapter(ArrayList<CommunityItem> communityItemArrayList) {
        this.communityItemArrayList = communityItemArrayList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_layout, parent, false);
        context = parent.getContext();

        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {

        pos=position;
        holder.lastMsgImage.setVisibility(View.GONE);
        holder.communityTitle.setText(communityItemArrayList.get(position).getCommunity_name());
        community_id = communityItemArrayList.get(position).getCommunity_id();
        Glide.with(context).load(communityItemArrayList.get(position).getImage_url()).into(holder.communityImage);

    }

    @Override
    public int getItemCount() {
        return communityItemArrayList.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        public TextView communityTitle, lastMessage, lastMessageTime;
        public ImageView communityImage, lastMsgImage;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            communityTitle = itemView.findViewById(R.id.communityTitle);
            communityImage = itemView.findViewById(R.id.communityImage);
            lastMessage = itemView.findViewById(R.id.last_chat);
            lastMessageTime = itemView.findViewById(R.id.last_chat_time);
            lastMsgImage = itemView.findViewById(R.id.last_chat_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    askToJoin();

                }
            });
        }
    }

    private void askToJoin() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        // ...Irrelevant code for customizing the buttons and title
//        LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.exit_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        final AlertDialog alertDialog = dialogBuilder.create();
//                TextView priceDetails = dialogView.findViewById(R.id.priceDetails);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
        Button yesBtn = dialogView.findViewById(R.id.yesButton);
        TextView message = dialogView.findViewById(R.id.message);
        TextView title = dialogView.findViewById(R.id.title);
        title.setText("Community Joining Confirmation");
        message.setText("Dou you want to join this Community?");


        cancelBtn.setText("No");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences studDetails = context.getSharedPreferences("CNB", MODE_PRIVATE);
                studentName = studDetails.getString("profileName", "NO_NAME");
                ArrayList<ItemMyCourse> myCoursesList = new ArrayList<>();
//                for (int i = 0; i < myCoursesList.size(); i++) {

                    String communityId = communityItemArrayList.get(pos).getCommunity_id();
                    joinStudentInGroup(communityId, studentName);
//                }
            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);


    }

    private void joinStudentInGroup(String communityId, String studentName) {

        DocumentReference reference = db.collection("Communities").document(communityId).collection("Students").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ItemStudents studentDetails = new ItemStudents(FirebaseAuth.getInstance().getCurrentUser().getUid(), studentName,profileImageUrlChatList);


        reference.set(studentDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent communityIntent = new Intent(context, CommunityChatPageActivity.class);
                communityIntent.putExtra("community_id", community_id);
                context.startActivity(communityIntent);
                Log.e("Student saved", "student saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Student saved failed", "student not saved");

            }
        });

    }
}
