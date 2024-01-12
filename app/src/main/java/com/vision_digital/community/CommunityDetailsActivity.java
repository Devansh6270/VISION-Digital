package com.vision_digital.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.community.invitation.studentModel.ItemStudent;
import com.vision_digital.community.studentModel.ItemStudentCommDetailsAdapter;
import com.vision_digital.community.studentModel.ItemStudents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityDetailsActivity extends AppCompatActivity {

    private String communityId = "", communityName = "", communityLogo = "";
    private RecyclerView community_students_list;
    private TextView community_participants, community_name,exit_group ,report_group,add_members,member_txt;
    private ImageView community_image, community_back_btn;

    ArrayList<String> studentNameList = new ArrayList<>();
    private String user;
    private String userNameString;

    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<ItemStudents> studentArrayList = new ArrayList<>();

    //search functionality

    private EditText searchEdt;
    private CardView search_card;
    private ImageView search_icon, clearBtn;
    ItemStudentCommDetailsAdapter itemStudentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communit_details);
        community_students_list = findViewById(R.id.community_students_list);
        community_participants = findViewById(R.id.community_members);
        community_name = findViewById(R.id.community_name);
        community_students_list = findViewById(R.id.community_students_list);
        community_image = findViewById(R.id.community_image);
        community_back_btn = findViewById(R.id.community_back_btn);
        report_group = findViewById(R.id.report_group);
        exit_group = findViewById(R.id.exit_group);
        add_members = findViewById(R.id.add_members);
        search_icon = findViewById(R.id.search_icon);
        clearBtn = findViewById(R.id.clearBtn);
        searchEdt = findViewById(R.id.searchEdt);
        search_card = findViewById(R.id.search_card);
        member_txt = findViewById(R.id.members_txt);



        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("uid", String.valueOf(user));


        member_txt.setVisibility(View.VISIBLE);
        search_card.setVisibility(View.GONE);

        community_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        exit_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGroup();

            }
        });
        add_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

//                String shareBody = userNameString +  " send you invitation to join community "+ communityName+ "\n"+" \nhttps://chalksnboard.com/downloadAppChalksnboard?data/community/" + communityId;
                String shareBody = userNameString +  " send you invitation to join community "+ communityName+ "\n"+" \nhttps://dlink.chalksnboard.com/"+ communityId;

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zeal");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        report_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"developer@chalksnboard.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Reporting Group "+communityName+" "+ "of ChalksNBoard App");
                i.putExtra(Intent.EXTRA_TEXT, "Hi Developer,\n\n");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CommunityDetailsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        communityId = getIntent().getStringExtra("community_id");
        communityName = getIntent().getStringExtra("community_name");
        communityLogo = getIntent().getStringExtra("community_logo");
        userNameString = getIntent().getStringExtra("userNameString");

        community_name.setText(communityName);

        Glide.with(getApplicationContext())
                .load(communityLogo)
                .into(community_image);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CommunityDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        community_students_list.setLayoutManager(layoutManager);
//        getStudentList();
        fetchStudentJoinedList();


        db.collection("Communities").document(communityId).collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", task.getResult().size() + "");
                    Log.e("count","count");
                    String no_of_Stud = task.getResult().size() + "";
                    community_participants.setText("ADDA- " + no_of_Stud + " participants");


                } else {
                    Log.d("errorCount", "Error getting documents: ", task.getException());
                }
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdt.setText("");
            }
        });
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                member_txt.setVisibility(View.GONE);
                search_card.setVisibility(View.VISIBLE);
            }
        });


    }

    private void filter(String text){
        ArrayList<ItemStudents> temp = new ArrayList();
        for(ItemStudents d: studentArrayList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getStudent_username().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        itemStudentAdapter.updateList(temp);
        community_students_list.setAdapter(itemStudentAdapter);

    }

    private void exitGroup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommunityDetailsActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = CommunityDetailsActivity.this.getLayoutInflater();
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


        title.setText("Hold on!");
        message.setText("Do you  really want to exit group..");
        yesBtn.setText("Yes");
        cancelBtn.setText("Not now");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Communities").document(communityId).collection("Students").document(user).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CommunityDetailsActivity.this, "Group exit.", Toast.LENGTH_SHORT).show();
                        Intent community = new Intent(CommunityDetailsActivity.this, CommunitiesListActivity.class);
                        community.putExtra("id", "id");
                        community.putExtra("fromActivity","dashboard");
                        startActivity(community);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
                alertDialog.dismiss();


            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

    }

    private void getStudentList() {
        Log.e("communityID", communityId);
        Log.e("enter", "entermethod1");

        Query query = db.collection("Communities").document(communityId).collection("Students");

        Log.e("enter", "entermethoDB");

        FirestoreRecyclerOptions<ItemStudents> response = new FirestoreRecyclerOptions.Builder<ItemStudents>()
                .setQuery(query, ItemStudents.class)
                .build();
        Log.e("enter", "entermethodFIRESTORE RECYCLER");

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<ItemStudents, StudentsHolder>(response) {
            @Override
            protected void onBindViewHolder(@NonNull StudentsHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ItemStudents model) {
                Log.e("enter", "entermethodBIND");


                holder.student_select.setVisibility(View.GONE);
                holder.student_select_rectangle.setVisibility(View.GONE);

                studentNameList.add(model.getStudent_id());

                holder.student_select.setVisibility(View.GONE);
                holder.studentNameInvitation.setText(model.getStudent_username());
                holder.studentPicInvitation.setVisibility(View.VISIBLE);
                holder.studentUserNameInvitation.setVisibility(View.VISIBLE);
//                holder.studentUserNameInvitation.setText(model.getStudent_username());



                if (model.getUser_image().equals("")){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.user_profile)
                            .into(holder.studentPicInvitation);
                }else{
                    Glide.with(getApplicationContext())
                            .load(model.getUser_image())
                            .into(holder.studentPicInvitation);
                }

                Log.e("enter", model.getStudent_username());



            }


            @NonNull
            @Override
            public StudentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.student_list_invitation_item, parent, false);
                Log.e("enter", "entermethodONCREAT");

                return new StudentsHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };
        firestoreRecyclerAdapter.startListening();

        Log.e("enter", "entermethodADAPTER");

        firestoreRecyclerAdapter.notifyDataSetChanged();
        community_students_list.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.notifyDataSetChanged();

//        Log.e("studentlist", String.valueOf(studentNameList));
//        String noOfPart = String.valueOf(firestoreRecyclerAdapter.getItemCount());

    }


    public class StudentsHolder extends RecyclerView.ViewHolder {
        TextView studentNameInvitation, studentUserNameInvitation;
        CircleImageView studentPicInvitation;
        ImageView student_select,student_select_rectangle;


        public StudentsHolder(View itemView) {
            super(itemView);
            Log.e("enter", "entermethod" +
                    "HOLDER");

            student_select = itemView.findViewById(R.id.student_select);
            studentPicInvitation = itemView.findViewById(R.id.studentPicInvitation);
            studentNameInvitation = itemView.findViewById(R.id.studentNameInvitation);
            studentUserNameInvitation = itemView.findViewById(R.id.studentUserNameInvitation);
            student_select_rectangle = itemView.findViewById(R.id.student_select_rectangle);

        }
    }

    private void fetchStudentJoinedList() {

        Log.e("community Id", communityId);
        db.collection("Communities").document(communityId).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());

                studentArrayList.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        ItemStudents itemStudent = new ItemStudents();
                        String sid = "";
                        String userImage = "";
                        String userName = "";
                        if (!documentChange.getDocument().getData().get("student_id").toString().equals(null)){
                            sid = documentChange.getDocument().getData().get("student_id").toString();

                        }

                        if (!documentChange.getDocument().getData().get("user_image").toString().equals(null)){
                            userImage =  documentChange.getDocument().getData().get("user_image").toString();
                        }

                        if (!documentChange.getDocument().getData().get("student_username").toString().equals(null)){
                            userName = documentChange.getDocument().getData().get("student_username").toString();
                        }


                        itemStudent.setStudent_id(sid);
                        itemStudent.setUser_image(userImage);
                        itemStudent.setStudent_username(userName);

                        Log.e("studentIdLoop11", userName);
                        Log.e("studentIdLoop12", userImage);
                        Log.e("studentIdLoop13", sid);

                        studentArrayList.add(itemStudent);

                    }
                    List<ItemStudent> types = queryDocumentSnapshots.toObjects(ItemStudent.class);
                    Log.e("onSuccess: ", String.valueOf(studentArrayList));
                    itemStudentAdapter = new ItemStudentCommDetailsAdapter(studentArrayList);
                    community_students_list.setAdapter(itemStudentAdapter);


                } else {

                }
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent communityIntent = new Intent(CommunityDetailsActivity.this, CommunityChatPageActivity.class);
        communityIntent.putExtra("community_id", communityId);
        communityIntent.putExtra("community_name", communityName);
        communityIntent.putExtra("community_logo", communityLogo);
        communityIntent.putExtra("activity", "community_list");
        startActivity(communityIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}