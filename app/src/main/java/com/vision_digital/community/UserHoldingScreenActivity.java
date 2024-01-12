package com.vision_digital.community;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vision_digital.R;
import com.vision_digital.model.milestone.ItemMileStone;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserHoldingScreenActivity extends AppCompatActivity {
    private String videoPos = "";
    private int chapter_id;
    private int mediaIndex;
    private String courseId = "";
    private String chat_id = "";
    private String videoStatusId = "";
    private String communityLogo = "";
    private String communityName = "";
    private String isAdmin = "";
    private String profileImageUrlString = "";
    private ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();
    private String isAdminLive = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    LottieAnimationView animationView, goAnimationView;
    TextView holding_text;
    private long timeLimit=1800;
    final Handler timer = new Handler(Looper.getMainLooper());
    private String timeString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_holding_screen);

        goAnimationView = findViewById(R.id.goAnimationView);
        animationView = findViewById(R.id.animationView);
        holding_text = findViewById(R.id.holding_text);

        goAnimationView.setVisibility(View.GONE);


        mileStoneArrayList = (ArrayList<ItemMileStone>) getIntent().getSerializableExtra("mileStonesList");
        mediaIndex = getIntent().getIntExtra("videoPosition", 0);
        chapter_id = getIntent().getIntExtra("chapter_id", 0);
        courseId = getIntent().getStringExtra("id");
        chat_id = getIntent().getStringExtra("chatId");
        videoStatusId = getIntent().getStringExtra("videoStatusId");
        communityLogo = getIntent().getStringExtra("community_logo");
        communityName = getIntent().getStringExtra("community_name");
        isAdmin = getIntent().getStringExtra("isAdmin");
        profileImageUrlString = getIntent().getStringExtra("userImage");
        timeString  = getIntent().getStringExtra("timeString");



        if (timeString.equals("immediate")){

            timeLimit = 00;
        }else if (timeString.equals("afterFifteen")){
            timeLimit = 900;

        }else if (timeString.equals("afterTwenty")){

            timeLimit = 1200;
        }else if (timeString.equals("afterThirty")){

            timeLimit = 1800;
        }


        timer();

        db = FirebaseFirestore.getInstance();
        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (e != null) {

                        }

                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                            isAdminLive = documentChange.getDocument().getData().get("liveUnliveStatus").toString();

                            if (isAdminLive.equals("live")){
                                animationView.setVisibility(View.GONE);
                                goAnimationView.setVisibility(View.VISIBLE);
                                holding_text.setText("Go live....");


                            }else{
                                animationView.setVisibility(View.VISIBLE);
                                goAnimationView.setVisibility(View.GONE);
                                holding_text.setText("Please wait your admin has not started yet..");
                            }
                            Log.e("isAdminLive", isAdminLive);
                            break;



                        }




                    }


                });


        // go live button jab admin live hoga tbhi clickable hoga aur ispr click krne pr Group study group player pr chala jayega yser

          goAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminLive.equals("live")) {
                    if (timeLimit==0 || timeLimit<0){
                        Intent mileStonePlayer = new Intent(UserHoldingScreenActivity.this, GroupStudyVideoPlayerActivity.class);
                        mileStonePlayer.putExtra("videoPosition", mediaIndex);
                        mileStonePlayer.putExtra("mileStonesList", mileStoneArrayList);
                        mileStonePlayer.putExtra("id", courseId);
                        mileStonePlayer.putExtra("chapter_id", chapter_id);
                        mileStonePlayer.putExtra("chatId", chat_id);
                        mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                        mileStonePlayer.putExtra("community_name", communityName);
                        mileStonePlayer.putExtra("community_logo", communityLogo);
                        mileStonePlayer.putExtra("isAdmin", "false");
                        mileStonePlayer.putExtra("userImage", profileImageUrlString);
                        mileStonePlayer.putExtra("fromActivity", "userHolding");
                        startActivity(mileStonePlayer);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }

                }else{
                    Toast.makeText(UserHoldingScreenActivity.this, "Group study start on time!..", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

//    agar admin live hai aur timelimit 0 ke equal hai to next Activity me chala jayega .
    @Override
    protected void onResume() {
        super.onResume();
        if (isAdminLive.equals("live")) {
            if (timeLimit==0 || timeLimit<0){
                Intent mileStonePlayer = new Intent(UserHoldingScreenActivity.this, GroupStudyVideoPlayerActivity.class);
                mileStonePlayer.putExtra("videoPosition", mediaIndex);
                mileStonePlayer.putExtra("mileStonesList", mileStoneArrayList);
                mileStonePlayer.putExtra("id", courseId);
                mileStonePlayer.putExtra("chapter_id", chapter_id);
                mileStonePlayer.putExtra("chatId", chat_id);
                mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                mileStonePlayer.putExtra("community_name", communityName);
                mileStonePlayer.putExtra("community_logo", communityLogo);
                mileStonePlayer.putExtra("isAdmin", "false");
                mileStonePlayer.putExtra("userImage", profileImageUrlString);
                mileStonePlayer.putExtra("fromActivity", "userHolding");
                startActivity(mileStonePlayer);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        }
    }


    // ye timer chalane ke lye hai
    private void timer() {
        final Runnable runnableTimer = new Runnable() {
            @Override
            public void run() {
                if (timeLimit >0){
                    timeLimit--;
                    long hours = timeLimit / 3600;
                    long remainder = timeLimit - hours * 3600;
                    long min = remainder / 60;
//                min = timeLimit / 60;
                    long sec = timeLimit % 60;

                    String second = String.format("%02d", sec);
                    String minut = String.format("%02d", min);
                    String hour = String.format("%02d", hours);

                    if (hours == 0) {
                        holding_text.setText("This group study will start after "+minut + ":" + second+ " !...");

                    } else {
                        holding_text.setText("This group study will start after "+hours + ":" + minut + ":" + second+ " !...");

                    }


                    Log.e("timmer", "" + timeLimit);

                    if (timeLimit != 0) {
                        timer.postDelayed(this, 1000);


                    }
                    if (timeLimit == 0) {
                        // Submit test result

                        onResume();
                        timer.removeCallbacksAndMessages(0);

                    }
                }

            }
        };
        timer.postDelayed(runnableTimer, 1000);
    }
}