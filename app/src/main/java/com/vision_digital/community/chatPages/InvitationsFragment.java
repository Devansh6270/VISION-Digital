package com.vision_digital.community.chatPages;

import static com.vision_digital.activities.DashboardActivity.sid;
import static com.vision_digital.community.CommunityChatPageActivity.communityLogo;
import static com.vision_digital.community.CommunityChatPageActivity.communityName;
import static com.vision_digital.community.chatPages.ChatsFragment.community_id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vision_digital.BuildConfig;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.R;
import com.vision_digital.community.GroupStudyVideoPlayerActivity;
import com.vision_digital.community.chatModels.ItemChat;
import com.vision_digital.community.invitation.SelectMilestoneInvitationActivity;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class InvitationsFragment extends Fragment {
    Button btnSendInvitation;
    String communityId;
    String student_id;
    final int MSG_TYPE_LEFT = 0;
    final int MSG_TYPE_RIGHT = 1;
    private RecyclerView invitation_recyclerview;
    private LinearLayoutManager linearLayoutManager;

    FirestoreRecyclerAdapter firestoreRecyclerChatAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String courseDetailsUrl = "";
    int chapter_id;
    String courseDescription;
    int videoPos = 0;
    int videoPosTemp = 0;
    String chap_id = "";
    String lastMilestoneId = "";
    String subscriptionStatus = "";
    String courseDuration = "";
    String courseOwnerName = "", courseOwnerQuali = "";
    List<Long> forMonths = new ArrayList<>();
    Uri previewVideoURI;
    ArrayList<Uri> courseMilestoneList = new ArrayList<>();
    ArrayList<ItemMileStone> mileStonesArrayList = new ArrayList<>();
    ArrayList<ItemChapter> chaptersList = new ArrayList<>();


    //profile


    private String userNameString = "";
    private String profileImageUrlString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invitations, container, false);
        btnSendInvitation = view.findViewById(R.id.btnSendInvitation);
        student_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        invitation_recyclerview = view.findViewById(R.id.invitation_recyclerview);
        communityId = community_id;
        courseDetailsUrl = getActivity().getString(R.string.apiURL) + "getCourseDetails";


        btnSendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectMilestoneInvitationActivity.class);
                intent.putExtra("id", communityId);
                intent.putExtra("community_name", communityName);
                intent.putExtra("community_logo", communityLogo);
                Log.e("community_logo", communityLogo);
                startActivity(intent);
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        invitation_recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);

        getChatList();

        return view;
    }

    private void getChatList() {
        Query query = db.collection("Communities").document(ChatsFragment.community_id).collection("Chats").orderBy("msgTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemChat> response = new FirestoreRecyclerOptions.Builder<ItemChat>()
                .setQuery(query, ItemChat.class)
                .build();

        firestoreRecyclerChatAdapter = new FirestoreRecyclerAdapter<ItemChat, InvitationHolder>(response) {
            @Override
            public void onBindViewHolder(InvitationHolder holder, @SuppressLint("RecyclerView") int position, ItemChat model) {


                if (model.getMsgType().equals("invitation")) {
                    holder.chat_layout.setVisibility(View.VISIBLE);
                    holder.chat.setVisibility(View.VISIBLE);
                    holder.incoming_chat_user.setVisibility(View.VISIBLE);
                    holder.chat_time.setVisibility(View.VISIBLE);
                    holder.chat_type.setVisibility(View.VISIBLE);
                    holder.communityChatImage.setVisibility(View.VISIBLE);


                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_keyboard_arrow_right_black_24dp)
                            .into(holder.chat_type);
                    if (model.getUserId().equals(student_id)) {
                        holder.incoming_chat_user.setText("");

                    } else {
                        holder.incoming_chat_user.setText(model.getUserName());

                    }

                    if (!model.getUserImage().equals("")){
                        Glide.with(getActivity().getApplicationContext())
                                .load(model.getUserImage())
                                .into(holder.communityChatImage);
                    }


                    holder.chat.setText(model.getMessage());

                    if (model.getMsgCategory().equals("image")) {
                        if (!model.getChatImage().equals("")) {
                            holder.chat_image.setVisibility(View.VISIBLE);
                            Glide.with(getActivity().getApplicationContext())
                                    .load(model.getChatImage())
                                    .into(holder.chat_image);

                        } else {
                            holder.chat_image.setVisibility(View.GONE);


                        }
                    }
                    if (model.getMsgCategory().equals("pdf")) {
                        if (!model.getChatImage().equals("")) {
                            holder.chat_image.setVisibility(View.GONE);
                            Log.e("msgCategory", model.getMsgCategory());
                            holder.pdf_icon.setVisibility(View.VISIBLE);
//                        holder.chat.setText("PDF file: \n" + model.getChatImage() + "\n" + model.getMessage());


                        } else {
                            holder.pdf_icon.setVisibility(View.GONE);


                        }
                    }


                } else {
                    holder.chat_layout.setVisibility(View.GONE);
                    holder.chat.setVisibility(View.GONE);
                    holder.incoming_chat_user.setVisibility(View.GONE);
                    holder.chat_time.setVisibility(View.GONE);
                    holder.chat_type.setVisibility(View.GONE);
                    holder.communityChatImage.setVisibility(View.GONE);


                    Log.e("No Doubts", "No Doubts");
                }


                if (!model.getReplyOfUser().equals("")) {
                    holder.replied_msg_card.setVisibility(View.VISIBLE);
                    holder.replied_by_user.setText(model.getReplyOfUser());
                    holder.replied_of_msg.setText(model.getReplyOfMsg());

                } else {
                    holder.replied_msg_card.setVisibility(View.GONE);
                }


//                ---------------------time manage----------------


//                -------------- for current time-------
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                Date dateNow = new Date();
                String currentServerTime = (formatter.format(dateNow));
                Log.e("current time", currentServerTime);


//                ------------chat time------------
                String time = String.valueOf(model.getMsgTime());
                Log.e("chat time", time);

                if (!time.equals("null")) {
                    Log.e("if enter time", time);
                    DateFormat inputFormat = new SimpleDateFormat(
                            "E MMM dd HH:mm:ss 'GMT'z yyyy", Locale.ENGLISH);

                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa", Locale.US);
                    SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm aaa", Locale.US);

                    String time_chat = time;

                    Date date = null;
                    try {
                        date = inputFormat.parse(time_chat);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String chatTiming = formatter.format(date);

                    if (chatTiming.equals(currentServerTime)) {
                        holder.chat_time.setText(formatter2.format(date));

                    } else {
                        holder.chat_time.setText(formatter1.format(date));

                    }


                } else {
                    Log.e("chat time", time);

                }


                if (model.getMsgType().equals("doubt")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_keyboard_arrow_right_black_24dp)
                            .into(holder.chat_type);
                }

                ////reply of chat

                if (!model.getReplyOfUser().equals("")) {
                    holder.replied_msg_card.setVisibility(View.VISIBLE);
                    holder.replied_by_user.setText(model.getReplyOfUser());
                    holder.replied_of_msg.setText(model.getReplyOfMsg());

                } else {
                    holder.replied_msg_card.setVisibility(View.GONE);
                }

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.exit_popup, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                        Button yesBtn = dialogView.findViewById(R.id.yesButton);
                        TextView message = dialogView.findViewById(R.id.message);
                        TextView title = dialogView.findViewById(R.id.title);


                        message.setText("Choose your Option");
                        title.setVisibility(View.GONE);

                        yesBtn.setText("Join");
                        cancelBtn.setText("Share");

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();


                            }
                        });
                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new GetCourseDetailsDash().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                alertDialog.dismiss();
                            }
                        });


                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                        return false;
                    }
                });
                holder.chat_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

//                        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                        ImageView optionImageTrans = dialogView.findViewById(R.id.optionImageTrans);
                        Glide.with(getActivity().getApplicationContext())
                                .load(model.getChatImage())
                                .into(optionImageTrans);
                        ImageView closeBtnn = dialogView.findViewById(R.id.closeBtnn);
                        closeBtnn.setVisibility(View.GONE);


                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                    }

                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        community_id = model.getCommunity_id();


                    }
                });

            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position).getUserId().equals(student_id))
                    return MSG_TYPE_RIGHT;
                else
                    return MSG_TYPE_LEFT;
            }


            @Override
            public InvitationHolder onCreateViewHolder(ViewGroup group, int viewType) {

                if (viewType == MSG_TYPE_RIGHT) {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.single_normal_chat_outgoing_layout, group, false);
                    return new InvitationHolder(view);


                } else {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.single_normal_chat_incoming_layout, group, false);
                    return new InvitationHolder(view);

                }


            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };
        invitation_recyclerview.smoothScrollToPosition(invitation_recyclerview.getBottom());
        invitation_recyclerview.smoothScrollToPosition(invitation_recyclerview.getBottom());
        firestoreRecyclerChatAdapter.notifyDataSetChanged();
        invitation_recyclerview.setAdapter(firestoreRecyclerChatAdapter);
    }

    public class InvitationHolder extends RecyclerView.ViewHolder {
        TextView incoming_chat_user, chat, chat_time, replied_by_user, replied_of_msg;
        ImageView communityChatImage, chat_type, chat_image, pdf_icon;
        LinearLayoutCompat replied_msg_card;
        LinearLayout chat_layout;

        public InvitationHolder(View itemView) {
            super(itemView);
            incoming_chat_user = itemView.findViewById(R.id.incoming_chat_user);
            communityChatImage = itemView.findViewById(R.id.communityChatImage);
            chat = itemView.findViewById(R.id.chat);
            chat_time = itemView.findViewById(R.id.chat_time);
            chat_type = itemView.findViewById(R.id.chat_type);
            chat_image = itemView.findViewById(R.id.chat_image);
            pdf_icon = itemView.findViewById(R.id.chat_pdf);
            replied_by_user = itemView.findViewById(R.id.replied_by_user);
            replied_of_msg = itemView.findViewById(R.id.replied_of_msg);
            replied_msg_card = itemView.findViewById(R.id.replied_msg_card);
            chat_layout = itemView.findViewById(R.id.chat_layout);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerChatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firestoreRecyclerChatAdapter.stopListening();
    }

    class GetCourseDetailsDash extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(getActivity().getApplicationContext());

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(getActivity().getApplicationContext());
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "uid=" + DashboardActivity.uid + "&app_version=" + versionCode + "&student_id=" + sid + "&course_id=" + communityId;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(courseDetailsUrl, "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result : ", s);

                    //Do work-----------------------------
                    String status = jsonObject.getString("status");
                    final JSONObject dataObj = jsonObject.getJSONObject("data");

                    String available_subscription_months, subscribed_months;
                    switch (status) {
                        case "success":
                            //Running Fine


                            int positionNumber = 0;

                            //Setting course Details for subscribed course--------------------------
                            subscriptionStatus = dataObj.getString("subscription_status");


                            String courseName = dataObj.getString("title");
                            String courseDesc = dataObj.getString("description");
                            courseDuration = dataObj.getString("duration");
                            courseOwnerName = dataObj.getJSONObject("owner_details").getString("owned_name");
                            courseOwnerQuali = dataObj.getJSONObject("owner_details").getString("owner_qualification");

                            if (subscriptionStatus.equals("subscribed")) {

                                int totalMonths = dataObj.getInt("available_subscription_months");
                                int currentSubsMonths = dataObj.getInt("subscribed_months");
                                if (totalMonths <= currentSubsMonths) {
                                } else {
                                    JSONArray subscriptionJsonArray = dataObj.getJSONArray("available_subscriptions");
                                    forMonths.clear();
                                }


                                courseDescription = courseDesc;
                                previewVideoURI = Uri.parse(dataObj.getString("promo_video_link"));
                                MediaItem mediaItem = MediaItem.fromUri(previewVideoURI);


                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");
                                JSONArray monthContent = dataObj.getJSONArray("course_content");

                                chaptersList.clear();
                                courseMilestoneList.clear();
                                mileStonesArrayList.clear();

//                                ProgressDialog dialogForIntent = new ProgressDialog(getActivity().getApplicationContext());
//                                dialogForIntent.setMessage("Please wait");
//                                dialogForIntent.show();
                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));

                                    int subMonths = Integer.parseInt(chapterObj.getString("min_month"));

                                    JSONArray notestones = chapterObj.getJSONArray("notes");

                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
//                                        dialogForIntent.show();
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);
                                        mileStone.setId(mileStoneObj.getString("id"));
//                                        mileStone.setVideoPosition(positionNumber++);
                                        videoPosTemp = positionNumber++;
                                        mileStone.setVideoPosition(videoPosTemp);
                                        Log.e("Topic name", "" + mileStoneObj.getString("title"));
                                        mileStone.setTitle(mileStoneObj.getString("title"));
                                        mileStone.setDuration(mileStoneObj.getString("duration"));
                                        mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                        mileStone.setVideoUrl(mileStoneObj.getString("video_link"));
                                        mileStone.setActivityType("courseDetails");
                                        mileStone.setSelected(false);

//                                        String video = mileStoneObj.getString("video_link");
//                                        String lastThree = video.substring(video.length() - 3, video.length());

                                        if (!mileStoneObj.getString("video_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(mileStoneObj.getString("video_link")));
                                        }
                                        if (!mileStoneObj.getString("duration").equals("1")) {
                                            mileStonesArrayList.add(mileStone);

                                        }
                                        Log.e("Milestone Id", lastMilestoneId);
                                        if (mileStoneObj.getString("id").equals(lastMilestoneId)) {
                                            videoPos = videoPosTemp;
                                            Log.e("videoPos", String.valueOf(videoPos));

                                            chap_id = chapterObj.getString("id");
                                            Log.e("chap_id", String.valueOf(chap_id));

                                            chapter_id = Integer.parseInt(chapterObj.getString("id"));
                                            Log.e("chapter_id", String.valueOf(chapter_id));

                                        } else {
                                            Log.e("No video Pos", "no video pos");
                                        }

                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
                                    }

                                    chaptersList.add(chapter);

                                }
//                                dialogForIntent.dismiss();
//                                dialog.dismiss();

                                Intent mileStonePlayer = new Intent(getActivity().getApplicationContext(), GroupStudyVideoPlayerActivity.class);
                                mileStonePlayer.putExtra("videoPosition", videoPos);
                                mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                mileStonePlayer.putExtra("id", communityId);
                                Log.e("putextracourseId", communityId);
                                mileStonePlayer.putExtra("chapter_id", chapter_id);
                                getActivity().startActivity(mileStonePlayer);


                            } else if (subscriptionStatus.equals("unsubscribed")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Subscribe this Course Again!..", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), CourseDetailsActivity.class);
                                intent.putExtra("id", communityId);
                                getActivity().startActivity(intent);


                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            LayoutInflater inflater = getActivity().getApplicationContext().getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((Activity) getActivity().getApplicationContext()).finish();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
            dialog.dismiss();
        }
    }


}