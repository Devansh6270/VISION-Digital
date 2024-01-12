package com.vision_digital.community.invitation;

import static com.vision_digital.community.invitation.studentModel.ItemStudentAdapter.invitedStudId;
import static com.vision_digital.community.invitation.studentModel.ItemStudentAdapter.invitedStudName;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_digital.R;
import com.vision_digital.community.CommunityChatPageActivity;
import com.vision_digital.community.invitation.studentModel.ItemStudent;
import com.vision_digital.community.invitation.studentModel.ItemStudentAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectStudentActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private RecyclerView student_recyclerview;
    CollectionReference reference;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    ItemStudentAdapter itemStudentAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayoutManager linearLayoutManager;
    int clicked = 1000000000;
    private TextView selectTime;
    ArrayList<String> studentNameList = new ArrayList<>();
    ArrayList<String> studentIdList = new ArrayList<>();
    int hour;
    int minutes;
    public static String selectedTime = "";
    public  static String selectedTimeStringLanguage = "";
    public static String timeInSting = "";
    public static String videoPosition = "";
    public static TextView txt_public_invitation;
    public static ImageView add_public_invitation, added_public_invitation;
    public static boolean isPublicInvitation = false;
    public static Button nextButton, cancelButton;
    public static String milestone_id, milestone_name;
    public static String community_id_select_student = "", community_name_select_student = "", community_logo_select_student = "";

    public static String userNameString = "";
    String studUserIdFireStore = "";
    public static String fromActivitySelectStuAct = "";


//    public static ArrayList<String> invitedStudId = new ArrayList<>();
//    public static ArrayList<String> invitedStudName = new ArrayList<>();

    ArrayList<ItemStudent> studentArrayList = new ArrayList<>();

    //search functionality

    private EditText searchEdt;
    private CardView search_card;
    private ImageView search_icon, clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);
        student_recyclerview = findViewById(R.id.student_recyclerview);
        selectTime = findViewById(R.id.selectTime);
        txt_public_invitation = findViewById(R.id.txt_public_invitation);
        add_public_invitation = findViewById(R.id.add_public_invitation);
        added_public_invitation = findViewById(R.id.added_public_invitation);
        search_icon = findViewById(R.id.search_icon);
        clearBtn = findViewById(R.id.clearBtn);
        searchEdt = findViewById(R.id.searchEdt);
        search_card = findViewById(R.id.search_card);

        nextButton = findViewById(R.id.nextButton);
        cancelButton = findViewById(R.id.cancelButton);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SelectStudentActivity.this, LinearLayoutManager.VERTICAL, false);
        student_recyclerview.setLayoutManager(layoutManager);
        milestone_id = getIntent().getStringExtra("milestone_id");
        Log.e("milestone_id", milestone_id);
        milestone_name = getIntent().getStringExtra("milestone_name");
        community_id_select_student = getIntent().getStringExtra("community_id");
        videoPosition = getIntent().getStringExtra("videoPos");
        community_name_select_student = getIntent().getStringExtra("community_name");
        community_logo_select_student = getIntent().getStringExtra("community_logo");
        userNameString = getIntent().getStringExtra("userNameString");
        fromActivitySelectStuAct = getIntent().getStringExtra("fromActivity");
        Log.e("community_id_se", community_id_select_student);
        Log.e("fromActivityStuAct", fromActivitySelectStuAct);

        studUserIdFireStore = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!selectedTime.equals("")) {
//                    if(!invitedStudId.isEmpty()){
//                        Intent invitation = new Intent(SelectStudentActivity.this, CommunityChatPageActivity.class);
//                        invitation.putExtra("activity", "select_student");
//                        invitation.putExtra("milestone_id", milestone_id);
//                        invitation.putExtra("milestone_name", milestone_name);
//                        invitation.putExtra("videoPos", videoPosition);
//                        invitation.putExtra("community_id", community_id_select_student);
//                        invitation.putExtra("community_name", community_name_select_student);
//                        invitation.putExtra("community_logo", community_logo_select_student);
//                        invitation.putExtra("invitedStudId", invitedStudId);
//                        invitation.putExtra("invitedStudName", invitedStudName);
//                        Log.e("invitedStudId", String.valueOf(invitedStudId));
//
//                        invitation.putExtra("invitedTime", selectedTime);
//                        invitation.putExtra("selectedTimeStringLanguage", selectedTimeStringLanguage);
//                        invitation.putExtra("userNameString", userNameString);
//                        Log.e("community_logo", community_logo_select_student);
//
//                        startActivity(invitation);
//                    }
//
//
//                } else {
//                    Toast.makeText(SelectStudentActivity.this, "Please select time!...", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTime.length()>=3) {
                    if (invitedStudId.size()>0) {
                        if (!milestone_id.equals("")){
                            Intent invitation = new Intent(SelectStudentActivity.this, CommunityChatPageActivity.class);
                            invitation.putExtra("activity", fromActivitySelectStuAct);
                            invitation.putExtra("milestone_id", milestone_id);
                            invitation.putExtra("milestone_name", milestone_name);
                            invitation.putExtra("videoPos", videoPosition);
                            invitation.putExtra("community_id", community_id_select_student);
                            invitation.putExtra("community_name", community_name_select_student);
                            invitation.putExtra("community_logo", community_logo_select_student);
                            invitation.putExtra("invitedStudId", invitedStudId);
                            invitation.putExtra("invitedStudName", invitedStudName);
                            Log.e("invitedStudId", String.valueOf(invitedStudId));
                            invitation.putExtra("invitedTime", selectedTime);
                            invitation.putExtra("selectedTimeStringLanguage", selectedTimeStringLanguage);
                            invitation.putExtra("userNameString", userNameString);
                            invitation.putExtra("timeInSting", timeInSting);
                            Log.e("community_logo", community_logo_select_student);
                            Log.e("invitedTime", selectedTime);
                           startActivity(invitation);
                        }else{
                            Toast.makeText(SelectStudentActivity.this, "Go back and select video!...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SelectStudentActivity.this, "Please select your buddy!...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SelectStudentActivity.this, "Please select time!...", Toast.LENGTH_SHORT).show();
                }

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });

//        reference = db.collection("Communities").document(community_id_select_student).collection("Students");

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SelectStudentActivity.this, v);
                popup.setOnMenuItemClickListener(SelectStudentActivity.this);
                popup.inflate(R.menu.select_time_chat);
                popup.show();
            }
        });

        txt_public_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPublicInvitation) {

                    isPublicInvitation = false;
                    added_public_invitation.setVisibility(View.GONE);
                    add_public_invitation.setVisibility(View.VISIBLE);
                    Log.e("true", "true");


                } else {

                    isPublicInvitation = true;
                    added_public_invitation.setVisibility(View.VISIBLE);
                    add_public_invitation.setVisibility(View.GONE);
                    Log.e("true", "false");

                }
            }
        });


        fetchStudentJoinedList();

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
                filter(searchEdt.getText().toString().trim());

            }
        });
    }


    private void filter(String text) {
        ArrayList<ItemStudent> temp = new ArrayList();
        for (ItemStudent d : studentArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getStudent_username().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        if (!temp.isEmpty()){
            itemStudentAdapter.updateList(temp);
            student_recyclerview.setAdapter(itemStudentAdapter);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.immediate:
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
                Date dateNow = new Date();
                String localTime = (formatter.format(dateNow));
                Log.e("current time", localTime);

                selectedTime = localTime;
                selectTime.setText("immediate");
                timeInSting = "immediate";



                return true;


//            case R.id.fifteenMin:
//
//                Calendar calendarFif = Calendar.getInstance();
//                Date today = calendarFif.getTime();
//                SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
//                System.out.println("Current Time: " + sdf.format(today));
//                calendarFif.add(Calendar.MINUTE, 15);
//                Date addMinutes = calendarFif.getTime();
//                String AfterFifteen = sdf.format(addMinutes);
//
//                selectedTime = AfterFifteen;
//
//                selectedTimeStringLanguage = "After 15 min";
//                selectTime.setText(selectedTimeStringLanguage);
//                timeInSting = "afterFifteen";
//                Log.e("fifteen",sdf.format(addMinutes));
//
//                return true;
//
//            case R.id.twentyMin:
//
//                Calendar calendarTwenty = Calendar.getInstance();
//                Date todayTwenty = calendarTwenty.getTime();
//                SimpleDateFormat sdfTwenty = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
//                System.out.println("Current Time: " + sdfTwenty.format(todayTwenty));
//                calendarTwenty.add(Calendar.MINUTE, 20);
//                Date addMinutesTwenty = calendarTwenty.getTime();
//                String afterTwenty = sdfTwenty.format(addMinutesTwenty);
//
//                selectedTime = afterTwenty;
//
//                selectedTimeStringLanguage = "After 20 min";
//                timeInSting = "afterTwenty";
//                selectTime.setText(selectedTimeStringLanguage);
//
//                Log.e("twenty",afterTwenty);
//
//
//                return true;
//
//            case R.id.thirtyMin:
//
//
//
//                Calendar calendarThirty = Calendar.getInstance();
//                Date todayThirty = calendarThirty.getTime();
//                SimpleDateFormat sdfThirty = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
//                System.out.println("Current Time: " + sdfThirty.format(todayThirty));
//                calendarThirty.add(Calendar.MINUTE, 20);
//                Date addMinutesThirty = calendarThirty.getTime();
//                String afterThirty= sdfThirty.format(addMinutesThirty);
//
//                selectedTime = afterThirty;
//
//                selectedTimeStringLanguage = "After 30 min";
//                selectTime.setText(selectedTimeStringLanguage);
//                timeInSting = "afterThirty";
//                Log.e("Thirty",afterThirty);
//
//
//                return true;


        }


        return false;
    }

//    public class StudentsHolder extends RecyclerView.ViewHolder {
//        TextView studentNameInvitation;
//        CircleImageView studentPicInvitation;
//        ImageView student_select;
//
//        public StudentsHolder(View itemView) {
//            super(itemView);
//            Log.e("enter", "entermethod" +
//                    "HOLDER");
//
//            student_select = itemView.findViewById(R.id.student_select);
//            studentPicInvitation = itemView.findViewById(R.id.studentPicInvitation);
//            studentNameInvitation = itemView.findViewById(R.id.studentNameInvitation);
//
//
//        }
//    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        firestoreRecyclerAdapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        firestoreRecyclerAdapter.stopListening();
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        fetchStudentJoinedList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void fetchStudentJoinedList() {

        Log.e("community Id", community_id_select_student);
        db.collection("Communities").document(community_id_select_student).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());

                studentArrayList.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        String sid = documentChange.getDocument().getData().get("student_id").toString();
                        String userName = documentChange.getDocument().getData().get("student_username").toString();
                        if (!studUserIdFireStore.equals(sid)){
                            ItemStudent itemStudent = new ItemStudent();
                            itemStudent.setStudent_id(sid);
                            itemStudent.setStudent_username(userName);
                            itemStudent.setSelected(false);
                            itemStudent.setActivity("selectStudent");
                            Log.e("studentIdLoop11", userName);
                            studentArrayList.add(itemStudent);
                        }
                        else{

                        }



                    }
                    List<ItemStudent> types = queryDocumentSnapshots.toObjects(ItemStudent.class);
                    Log.e("onSuccess: ", String.valueOf(studentArrayList));
                    itemStudentAdapter = new ItemStudentAdapter(studentArrayList);
                    student_recyclerview.setAdapter(itemStudentAdapter);


                } else {

                }
            }

        });
    }

}