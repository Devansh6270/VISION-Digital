package com.vision_digital.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.R;
import com.vision_digital.community.chatModels.ItemChat;
import com.vision_digital.community.communitymodels.CommunityItem;
import com.vision_digital.community.communitymodels.ItemCommunityAdapter;
import com.vision_digital.community.studentModel.ItemStudents;
import com.vision_digital.model.myCourses.ItemMyCourse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CommunitiesListActivity extends AppCompatActivity {
    RecyclerView communityRecyclerView, recommendedCommunityRecyclerView;
    Context context;
    ArrayList<CommunityItem> communityItemArrayList;
    List<ItemStudents> studentsArrayList;
    ItemCommunityAdapter itemCommunityAdapter;

    //firestore ui
    FirestoreRecyclerAdapter firestoreRecyclerAdapter, studentAdapter, recommendedFirestoreAdapter, firestoreRecyclerChatAdapter;
    LinearLayoutManager linearLayoutManager;
    String community_id;
    String community_id_recommendation = "";
    String community_name;
    String studentName;
    String community_logo;
    int pos;
    String studentUserId;
    AlertDialog alertDialog;
    String firestoreSid = "";
    ImageView backBtn;
    String firestoreStudentId = "";
    boolean isStudentAvailable = false;
    private String lastChat = "";
    private String lastChatTime = "";
    private String fromActivity = "";
    private String id = "";
    private int sid;
    public static String profileImageUrlChatList = "";
    private TextView my_community_txt, recommended_txt;

    View below_recom_group;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Communities");
    private String userNameString = "";

    //reference for last message
    private DocumentReference lastMsgReference;
    private HashMap<String, Object> lastChatDetails = new HashMap<>();
    ProgressDialog dialog;
    Timestamp timeForArrList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communities_list);
        communityRecyclerView = findViewById(R.id.communityRecyclerView);
        communityItemArrayList = new ArrayList<>();
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendedCommunityRecyclerView = findViewById(R.id.recommendedCommunityRecyclerView);
        itemCommunityAdapter = new ItemCommunityAdapter(communityItemArrayList);
        communityRecyclerView.setAdapter(itemCommunityAdapter);
        recommendedCommunityRecyclerView.setItemAnimator(null);

        backBtn = findViewById(R.id.backBtn);
        my_community_txt = findViewById(R.id.my_community_txt);
        studentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        below_recom_group = findViewById(R.id.below_recom_group);
        recommended_txt = findViewById(R.id.recommended_txt);


        dialog = new ProgressDialog(CommunitiesListActivity.this);
        dialog.setMessage("Please wait..");
        dialog.show();
//        fetchCommunityList();


        id = getIntent().getStringExtra("id");
        fromActivity = getIntent().getStringExtra("fromActivity");
        LinearLayoutManager layoutManager = new LinearLayoutManager(CommunitiesListActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recommendedCommunityRecyclerView.setLayoutManager(layoutManager);
        recommendedCommunityRecyclerView.setItemAnimator(null);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();
        getCommunityList();
        getRecommendedCommunityList();
        getChatList("17");

        if (studentUserId != null) {
            SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
            boolean registered = userIsRegisteredSuccessful.getBoolean("registered", true);
            sid = userIsRegisteredSuccessful.getInt("sid", 0);
            Log.e("sid", String.valueOf(sid));

        }


        getProfileData();
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        communityRecyclerView.setLayoutManager(linearLayoutManager);
        communityRecyclerView.setItemAnimator(null);
        db = FirebaseFirestore.getInstance();
    }

    private void getCommunityList() {

        dialog.show();
        Query query = db.collection("Communities").orderBy("lastChatTime", Query.Direction.DESCENDING);
//                .orderBy("community_name", Query.Direction.ASCENDING)


        FirestoreRecyclerOptions<ItemCommunityModel> response = new FirestoreRecyclerOptions.Builder<ItemCommunityModel>()
                .setQuery(query, ItemCommunityModel.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<ItemCommunityModel, CommunityHolder>(response) {
            @Override
            public void onBindViewHolder(CommunityHolder holder, @SuppressLint("RecyclerView") int position, ItemCommunityModel model) {

                ProgressDialog progressDialog = new ProgressDialog(CommunitiesListActivity.this);
                progressDialog.setMessage("Please wait!..");
                progressDialog.show();
                pos = position;


                community_id = model.getCommunity_id();
//                getChatList(community_id);

//                Log.e("community Id get com", community_id);
                community_name = model.getCommunity_name();
                community_logo = model.getCommunity_image_url();
                holder.communityTitle.setText(model.getCommunity_name());


                holder.lastMsgImage.setVisibility(View.GONE);
                Glide.with(getApplicationContext())
                        .load(model.getCommunity_image_url())
                        .into(holder.communityImage);


                DocumentReference docRef = db.collection("Communities").document(community_id).collection("LastChat").document("last_chat");

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {


                                String lastChat = document.getString("lastChat");

//                                 -------------- for current Date------
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                                Date dateNow = new Date();
                                String currentServerDate = (formatter.format(dateNow));
                                Log.e("current time", currentServerDate);

//                                -------for current time formate
                                SimpleDateFormat currentTimeFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
                                Date dateNowCurrenTime = new Date();
                                String currentServerTime = (currentTimeFormatter.format(dateNowCurrenTime));
                                Log.e("current time", currentServerTime);

                                if (document.getTimestamp("lastChatTime") != null) {
                                    holder.lastMessageTime.setVisibility(View.VISIBLE);
                                    //                ------------chat time------------
                                    String time = document.getTimestamp("lastChatTime").toDate().toString();

                                    Log.e("last chat time", time);
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
                                            Log.e("date", String.valueOf(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String chatTiming = formatter.format(date);
                                        String chatTimingForCurrent = formatter1.format(date);


                                        if (chatTiming.equals(currentServerDate)) {
                                            if (chatTimingForCurrent.equals(currentServerTime)){
                                                holder.lastMessageTime.setText(formatter2.format(date));
                                                holder.lastMessageTime.setTextColor(Color.parseColor("#fe6463"));
                                            }else{
                                                holder.lastMessageTime.setText(formatter2.format(date));
                                                holder.lastMessageTime.setTextColor(Color.parseColor("#000000"));
                                            }



                                        } else {
                                            holder.lastMessageTime.setText(formatter1.format(date));
//                                            holder.lastMessageTime.setTextColor(Color.parseColor("#000000"));
                                        }
                                    } else {
                                        Log.e("last chat time", time);

                                    }

                                    holder.lastMessage.setText(lastChat);
                                    if (lastChat.equals("")) {
                                        holder.lastMessage.setVisibility(View.GONE);
                                    } else {
                                        holder.lastMessage.setVisibility(View.VISIBLE);

                                    }

                                } else {
                                    holder.lastMessageTime.setVisibility(View.GONE);
                                }


                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }
                    }
                });



                db.collection("Communities").document(community_id).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                        Log.e("List", studentList.toString());

                        my_community_txt.setVisibility(View.VISIBLE);


                        if (!queryDocumentSnapshots.isEmpty() && !studentList.isEmpty()) {
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                String sids = documentChange.getDocument().getData().get("student_id").toString();
                                Log.e("studentIdloop12", sids);

                                if (studentUserId.equals(sids)) {
                                    Log.e("hai", model.getCommunity_id());
                                    holder.communityImage.setVisibility(View.VISIBLE);
                                    holder.community_layout.setVisibility(View.VISIBLE);
                                    my_community_txt.setVisibility(View.VISIBLE);
                                    holder.layout_cardview.setVisibility(View.VISIBLE);
                                    Log.e("isStudentAvailable", String.valueOf(isStudentAvailable));
                                    break;

                                } else {
                                    holder.communityImage.setVisibility(View.GONE);
                                    holder.community_layout.setVisibility(View.GONE);
                                    holder.layout_cardview.setVisibility(View.GONE);
                                    Log.e("Nhi hai", model.getCommunity_id());

                                }
                            }

                        } else {
                            holder.communityImage.setVisibility(View.GONE);
                            holder.community_layout.setVisibility(View.GONE);
                            holder.layout_cardview.setVisibility(View.GONE);

                        }


                    }
                });

                if (fromActivity.equals("dashboardLink")){
                    if (id.equals(model.getCommunity_id())){
                        fromActivity = "dashboard";
                        community_id = model.getCommunity_id();
                        community_name = model.getCommunity_name();
                        community_logo = model.getCommunity_image_url();
                        Intent communityIntent = new Intent(CommunitiesListActivity.this, CommunityChatPageActivity.class);
                        communityIntent.putExtra("community_id", community_id);
                        communityIntent.putExtra("community_name", community_name);
                        communityIntent.putExtra("community_logo", community_logo);
                        communityIntent.putExtra("activity", "community_list");
                        startActivity(communityIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                }
                else{
                    Log.e("fromActivity","dashboard");
                }


                holder.linear_lay_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        community_id = model.getCommunity_id();
                        community_name = model.getCommunity_name();
                        community_logo = model.getCommunity_image_url();
                        Intent communityIntent = new Intent(CommunitiesListActivity.this, CommunityChatPageActivity.class);
                        communityIntent.putExtra("community_id", community_id);
                        communityIntent.putExtra("community_name", community_name);
                        communityIntent.putExtra("community_logo", community_logo);
                        communityIntent.putExtra("activity", "community_list");
                        startActivity(communityIntent);

                    }
                });

                holder.communityImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater = getLayoutInflater();

//                        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                        ImageView optionImageTrans = dialogView.findViewById(R.id.optionImageTrans);
                        Glide.with(getApplicationContext())
                                .load(model.getCommunity_image_url())
                                .into(optionImageTrans);
                        ImageView closeBtnn = dialogView.findViewById(R.id.closeBtnn);
                        closeBtnn.setVisibility(View.GONE);


                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                    }

                });
                progressDialog.dismiss();

            }

            @Override
            public CommunityHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.community_item_layout, group, false);

                return new CommunityHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        firestoreRecyclerAdapter.notifyDataSetChanged();
        communityRecyclerView.setAdapter(firestoreRecyclerAdapter);
        dialog.dismiss();

    }

    public class CommunityHolder extends RecyclerView.ViewHolder {
        TextView communityTitle, lastMessage, lastMessageTime;
        ImageView communityImage, lastMsgImage;
        LinearLayout community_layout,linear_lay_txt;
        CardView layout_cardview;

        public CommunityHolder(View itemView) {
            super(itemView);
            communityTitle = itemView.findViewById(R.id.communityTitle);
            communityImage = itemView.findViewById(R.id.communityImage);
            lastMessage = itemView.findViewById(R.id.last_chat);
            lastMessageTime = itemView.findViewById(R.id.last_chat_time);
            lastMsgImage = itemView.findViewById(R.id.last_chat_type);
            community_layout = itemView.findViewById(R.id.community_layout);
            linear_lay_txt = itemView.findViewById(R.id.linear_lay_txt);
            layout_cardview = itemView.findViewById(R.id.layout_cardview);
        }
    }


    private void getRecommendedCommunityList() {
        dialog.show();
        Query query = db.collection("Communities").orderBy("community_name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemCommunityModel> response = new FirestoreRecyclerOptions.Builder<ItemCommunityModel>()
                .setQuery(query, ItemCommunityModel.class)
                .build();

        recommendedFirestoreAdapter = new FirestoreRecyclerAdapter<ItemCommunityModel, RecommendedCommunityHolder>(response) {
            @Override
            public void onBindViewHolder(RecommendedCommunityHolder holder, @SuppressLint("RecyclerView") int position, ItemCommunityModel model) {
                pos = position;

                String communityIdTemp = model.getCommunity_id();
                identifyRecommendedCommunity(communityIdTemp);

                db.collection("Communities").document(communityIdTemp).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                        Log.e("List", studentList.toString());

                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                String sids = documentChange.getDocument().getData().get("student_id").toString();
                                Log.e("studentIdloop12", sids);


                                if (studentUserId.equals(sids)) {

                                    holder.communityImage.setVisibility(View.GONE);
                                    holder.communityTitle.setVisibility(View.GONE);
                                    holder.layout_cardview.setVisibility(View.GONE);
                                    holder.itemRecommendedCommunity.setVisibility(View.GONE);

                                    Log.e("isStudentAvailable", String.valueOf(isStudentAvailable));

                                    break;

                                } else {


                                    holder.communityImage.setVisibility(View.VISIBLE);
                                    holder.communityTitle.setVisibility(View.VISIBLE);
                                    holder.layout_cardview.setVisibility(View.VISIBLE);
                                    holder.itemRecommendedCommunity.setVisibility(View.VISIBLE);
                                    Log.e("isStudentAvailableFalse", String.valueOf(isStudentAvailable));

                                }
                            }

                        } else {
                            holder.communityImage.setVisibility(View.VISIBLE);
                            holder.communityTitle.setVisibility(View.VISIBLE);
                            holder.layout_cardview.setVisibility(View.VISIBLE);
                            holder.itemRecommendedCommunity.setVisibility(View.VISIBLE);

                        }


                    }
                });


                Log.e("firestoreStudentId", firestoreStudentId);
                Log.e("student user id", studentUserId);


                holder.communityTitle.setText(model.getCommunity_name());

                Glide.with(getApplicationContext())
                        .load(model.getCommunity_image_url())
                        .into(holder.communityImage);

//                if (studentUserId.equals(firestoreStudentId)) {
//                    holder.communityImage.setVisibility(View.GONE);
//                    holder.communityTitle.setVisibility(View.GONE);
//
//
//                } else {
//                    holder.communityImage.setVisibility(View.VISIBLE);
//                    holder.communityTitle.setVisibility(View.VISIBLE);
//
//
//                }

//                for (int i=0; i<myCoursesList.size(); i++){
//                    String courseCommunityId = myCoursesList.get(i).getId();
//                    if (model.getCommunity_id().equals(courseCommunityId)) {
//
//                        Log.e("hai",model.getCommunity_id());
//                        holder.communityImage.setVisibility(View.VISIBLE);
//                        holder.communityTitle.setVisibility(View.VISIBLE);
//                        break;
//                    }else{
//                        holder.communityImage.setVisibility(View.GONE);
//                        holder.communityTitle.setVisibility(View.GONE);
//                        Log.e("Nhi hai",model.getCommunity_id());
//
//                    }
//
//                }


                holder.communityImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        community_id_recommendation = model.getCommunity_id();
                        String community_name_recommendation = model.getCommunity_name();
                        String community_logo_recommendation = model.getCommunity_image_url();

                        askToJoin(community_id_recommendation,community_name_recommendation,community_logo_recommendation);
                    }
                });

            }

            @Override
            public RecommendedCommunityHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recommended_community_layout_item, group, false);

                return new RecommendedCommunityHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        recommendedFirestoreAdapter.notifyDataSetChanged();

        recommendedCommunityRecyclerView.setAdapter(recommendedFirestoreAdapter);


        dialog.dismiss();

    }

    public class RecommendedCommunityHolder extends RecyclerView.ViewHolder {
        TextView communityTitle;
        ImageView communityImage;
        LinearLayout itemRecommendedCommunity,linear_lay;
        CardView image_cardview, layout_cardview;

        public RecommendedCommunityHolder(View itemView) {
            super(itemView);
            communityTitle = itemView.findViewById(R.id.communityTitleH);
            communityImage = itemView.findViewById(R.id.communityImageH);
            itemRecommendedCommunity = itemView.findViewById(R.id.itemRecommendedCommunity);
            image_cardview = findViewById(R.id.image_cardview);
            linear_lay = itemView.findViewById(R.id.linear_lay);
            layout_cardview = itemView.findViewById(R.id.layout_cardview);

        }
    }

    private String identifyRecommendedCommunity(String communityIdTemp) {

//        Log.e("community Id", communityIdTemp);
        db.collection("Communities").document(communityIdTemp).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        String sid = documentChange.getDocument().getData().get("student_id").toString();
                        Log.e("studentIdloop12", sid);


                        if (studentUserId.equals(sid)) {
                            Log.e("studentIdloop13", sid);
                            firestoreStudentId = sid;
                            isStudentAvailable = true;
                            Log.e("isStudentAvailable", String.valueOf(isStudentAvailable));

                            break;

                        } else {
                            isStudentAvailable = false;
                            Log.e("isStudentAvailableFalse", String.valueOf(isStudentAvailable));

                            firestoreStudentId = "";
                        }
                    }

                } else {

                }


            }
        });

        return firestoreSid;

    }


    private void askToJoin(String id,String community_name_recommendation, String community_logo_recommendation) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommunitiesListActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = CommunitiesListActivity.this.getLayoutInflater();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.exit_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        alertDialog = dialogBuilder.create();
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
                SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
                studentName = studDetails.getString("profileName", "NO_NAME");
                ArrayList<ItemMyCourse> myCoursesList = new ArrayList<>();

                String communityId = id;
                joinStudentInGroup(id,studentName,community_name_recommendation,community_logo_recommendation);
                alertDialog.dismiss();

            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);


    }

    private void joinStudentInGroup(String communityId, String studentName, String community_name_recommendation, String community_logo_recommendation) {

        DocumentReference reference = db.collection("Communities").document(communityId).collection("Students").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ItemStudents studentDetails = new ItemStudents(studentUserId, userNameString, profileImageUrlChatList);


        reference.set(studentDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent communityIntent = new Intent(CommunitiesListActivity.this, CommunityChatPageActivity.class);
                communityIntent.putExtra("community_id", communityId);
                communityIntent.putExtra("community_name", community_name_recommendation);
                communityIntent.putExtra("community_logo", community_logo_recommendation);
                communityIntent.putExtra("activity", "community_list");
                startActivity(communityIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Log.e("Student saved", "student saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Student saved failed", "student not saved");

            }
        });

    }



    private void getProfileData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("StudentProfile");
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        DocumentReference uidRef = db.collection("StudentProfile").document(String.valueOf(sid));
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        userNameString = document.getString("userName");
                                        profileImageUrlChatList = document.getString("profileImage");
                                        Log.e("userNameString",profileImageUrlChatList);


                                    } else {
                                        Log.d("No such document", "No such document");
                                    }
                                } else {
                                    Log.d("get failed with", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("Error getting documents", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getJoinedStudentList() {
        Log.e("Enter in method", "Enter in method");

        Query query = db.collection("Communities").document(community_id).collection("Students");
        Log.e("query", query.toString());

        FirestoreRecyclerOptions<ItemStudents> response = new FirestoreRecyclerOptions.Builder<ItemStudents>()
                .setQuery(query, ItemStudents.class)
                .build();
        Log.e("response", response.toString());


        studentAdapter = new FirestoreRecyclerAdapter<ItemStudents, StudentHolder>(response) {
            @Override
            public void onBindViewHolder(StudentHolder holder, int position, ItemStudents model) {

                Log.e("studentId", model.getStudent_id());
                ArrayList<ItemStudents> studentsArrayList = new ArrayList<>();

                for (int i = 0; i < studentsArrayList.size(); i++) {
                    if (studentUserId.equals(studentsArrayList.get(i).getStudent_id())) {
                        Log.e("studentIdloop", studentsArrayList.get(i).getStudent_id());
                        Intent communityIntent = new Intent(CommunitiesListActivity.this, CommunityChatPageActivity.class);
                        communityIntent.putExtra("community_id", community_id);
                        startActivity(communityIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
//                        askToJoin(community_id_recommendation, community_name_recommendation, community_id);
                    }
                }

            }


            @Override
            public StudentHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.community_item_layout, group, false);


                return new StudentHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };


    }

    public class StudentHolder extends RecyclerView.ViewHolder {


        public StudentHolder(View itemView) {
            super(itemView);

        }
    }

    private void getChatList(String community_id) {
        Log.e("getChatlist", "getchatlist");
        Query query = db.collection("Communities").document(community_id).collection("Chats").orderBy("msgTime", Query.Direction.DESCENDING).limit(1);

        FirestoreRecyclerOptions<ItemChat> response = new FirestoreRecyclerOptions.Builder<ItemChat>()
                .setQuery(query, ItemChat.class)
                .build();

        firestoreRecyclerChatAdapter = new FirestoreRecyclerAdapter<ItemChat, LastChatHolder>(response) {
            @Override
            protected void onBindViewHolder(@NonNull LastChatHolder holder, int position, @NonNull ItemChat model) {

                Log.e("position", String.valueOf(position));

                lastChat = model.getMessage();

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
                        Log.e("date", String.valueOf(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String chatTiming = formatter.format(date);

                    if (chatTiming.equals(currentServerTime)) {
                        holder.chat_time.setText(formatter2.format(date));
                        lastChatTime = formatter2.format(date);
                    } else {
                        lastChatTime = formatter1.format(date);

                    }
                } else {
                    Log.e("chat time", time);

                }
            }

            @Override
            public LastChatHolder onCreateViewHolder(ViewGroup group,
                                                     int viewType) {


                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_normal_chat_outgoing_layout, group, false);
                return new LastChatHolder(view);


            }


            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };
        firestoreRecyclerChatAdapter.notifyDataSetChanged();
    }

    public class LastChatHolder extends RecyclerView.ViewHolder {
        TextView incoming_chat_user, chat, chat_time, replied_by_user, replied_of_msg;
        ImageView communityChatImage, chat_type, chat_image, pdf_icon;
        LinearLayoutCompat replied_msg_card;
        LinearLayout chat_layout;
        CardView chat_image_card;


        public LastChatHolder(View itemView) {
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
            chat_image_card = itemView.findViewById(R.id.chat_image_card);


        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
        recommendedFirestoreAdapter.startListening();
        firestoreRecyclerChatAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
        recommendedFirestoreAdapter.stopListening();
        firestoreRecyclerChatAdapter.stopListening();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CommunitiesListActivity.this, DashboardActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}