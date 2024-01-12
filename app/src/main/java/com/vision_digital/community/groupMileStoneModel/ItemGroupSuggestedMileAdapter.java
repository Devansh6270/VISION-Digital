package com.vision_digital.community.groupMileStoneModel;

import static com.vision_digital.community.GroupStudyVideoPlayerActivity.chapter_id;
import static com.vision_digital.community.GroupStudyVideoPlayerActivity.chat_id;
import static com.vision_digital.community.GroupStudyVideoPlayerActivity.communityLogoG;
import static com.vision_digital.community.GroupStudyVideoPlayerActivity.communityNameG;
import static com.vision_digital.community.GroupStudyVideoPlayerActivity.courseId;
import static com.vision_digital.community.GroupStudyVideoPlayerActivity.profileImageUrlString;
import static com.vision_digital.community.GroupStudyVideoPlayerActivity.videoStatusId;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.vision_digital.community.GroupStudyVideoPlayerActivity;
import com.vision_digital.model.milestone.ItemMileStone;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ItemGroupSuggestedMileAdapter extends RecyclerView.Adapter<ItemGroupSuggestedMileViewHolder> {

    List<ItemMileStone> milestoneList;
    Context context;
    private String admin = "";
    private String studentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String isAdmin = "false";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ItemGroupSuggestedMileAdapter(List<ItemMileStone> milestoneList) {
        this.milestoneList = milestoneList;
    }

    @NonNull
    @Override
    public ItemGroupSuggestedMileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemGroupSuggestedMileViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inner_milestone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGroupSuggestedMileViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (milestoneList.get(position).getActivityType().equals("adda")) {
            db.collection("Communities").document(courseId).collection("Chats")
                    .document(chat_id).collection("invitedStudent").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            List<DocumentSnapshot> studentList = documentSnapshots.getDocuments();
                            if (e != null) {

                            }


                            if (!documentSnapshots.isEmpty()) {
                                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                    String invitedStuId = documentChange.getDocument().getData().get("invStudId").toString();
                                    String invitedUserType = documentChange.getDocument().getData().get("userType").toString();


                                    if (invitedUserType.equals("admin")) {
                                        admin = documentChange.getDocument().getData().get("invStudId").toString();
                                    }


                                    if (admin.equals(studentUId)) {
                                        isAdmin = "true";
                                    } else {
                                        isAdmin = "false";
                                    }

                                    Log.e("studentIdloop11", invitedStuId);

                                }

                            } else {

                            }


                        }
                    });

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (milestoneList.get(position).getActivityType().equals("adda")) {
                    if (isAdmin.equals("true")) {
                        String positionMile = String.valueOf(position);
                        db = FirebaseFirestore.getInstance();
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).
                                collection("SuggestedMilestone").document(videoStatusId).update("itemViewPos", positionMile);

                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).
                                collection("SuggestedMilestone").document(videoStatusId).update("mileId", milestoneList.get(position).getId().toString());


                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("suggestedMile", "clicked");


                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("suggestedMileId", milestoneList.get(position).getId());


                        Log.e("mileId", milestoneList.get(position).getId());
                        Log.e("itemViewPos", positionMile);

                        Intent mileStonePlayer = new Intent(context, GroupStudyVideoPlayerActivity.class);
                        mileStonePlayer.putExtra("videoPosition", 0);
                        mileStonePlayer.putExtra("seekTo", 0);
                        mileStonePlayer.putExtra("id", courseId);
                        mileStonePlayer.putExtra("fromMile", true);
                        mileStonePlayer.putExtra("subscriptionStatus", "subscribed");
                        mileStonePlayer.putExtra("community_name", communityNameG);
                        mileStonePlayer.putExtra("community_logo", communityLogoG);
                        ArrayList<ItemMileStone> singleMileStoneList = new ArrayList<>();
                        singleMileStoneList.add(milestoneList.get(position));
                        mileStonePlayer.putExtra("mileStonesList", singleMileStoneList);
                        mileStonePlayer.putExtra("chapter_id", chapter_id);
                        mileStonePlayer.putExtra("chatId", chat_id);
                        mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                        mileStonePlayer.putExtra("isAdmin", "true");
                        mileStonePlayer.putExtra("userImage", profileImageUrlString);
                        mileStonePlayer.putExtra("fromActivity", "groupPlayer");
                        context.startActivity(mileStonePlayer);
                    } else {
                        Toast.makeText(context, "Only admin can control this!..", Toast.LENGTH_SHORT).show();
                    }

                } else {

                }

            }
        });

        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (e != null) {

                        }

                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {

                            String suggMile = documentChange.getDocument().getData().get("suggestedMile").toString();

                            String suggMileId = documentChange.getDocument().getData().get("suggestedMileId").toString();

                            String docId = documentChange.getDocument().getId();
                            if (isAdmin.equals("false")) {
                                Log.e("isAdmin", isAdmin);

                                if (suggMile.equals("clicked")) {
                                    Log.e("isAdmin1", isAdmin);

                                    if (!suggMileId.equals("") && suggMileId.equals(milestoneList.get(position).getId())) {

                                        db.collection("Communities").document(courseId).collection("Chats")
                                                .document(chat_id).collection("invitedVideoPlayingStatus").document(docId).update("suggestedMile", "unClicked");

                                        db.collection("Communities").document(courseId).collection("Chats")
                                                .document(chat_id).collection("invitedVideoPlayingStatus").document(docId).update("suggestedMileId", "");

                                        Log.e("suggMileId3", suggMileId);
                                        Intent mileStonePlayer = new Intent(context, GroupStudyVideoPlayerActivity.class);
                                        mileStonePlayer.putExtra("videoPosition", 0);
                                        mileStonePlayer.putExtra("seekTo", 0);
                                        mileStonePlayer.putExtra("id", courseId);
                                        mileStonePlayer.putExtra("fromMile", true);
                                        mileStonePlayer.putExtra("subscriptionStatus", "subscribed");
                                        mileStonePlayer.putExtra("name", communityNameG);
                                        mileStonePlayer.putExtra("logo", communityLogoG);
                                        ArrayList<ItemMileStone> singleMileStoneList = new ArrayList<>();
                                        singleMileStoneList.add(milestoneList.get(position));
                                        mileStonePlayer.putExtra("mileStonesList", singleMileStoneList);
                                        mileStonePlayer.putExtra("chapter_id", chapter_id);
                                        mileStonePlayer.putExtra("chatId", chat_id);
                                        mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                                        Log.e("videoStatusIdAda", suggMileId);

                                        mileStonePlayer.putExtra("isAdmin", "false");
                                        mileStonePlayer.putExtra("userImage", profileImageUrlString);
                                        mileStonePlayer.putExtra("fromActivity", "groupPlayer");
                                        context.startActivity(mileStonePlayer);
                                    }

                                }
                                break;

                            }

                        }
                    }
                });

        holder.mileStoneName.setText(milestoneList.get(position).getTitle());
        try {
            int duration = Integer.parseInt(milestoneList.get(position).getDuration());
            int min = duration / 60;
            int sec = duration % 60;
            holder.mileStoneDuration.setText(min + ":" + sec + " mins");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return milestoneList != null ? milestoneList.size() : 0;
    }
}
