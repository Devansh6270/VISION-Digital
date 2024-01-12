package com.vision_digital.activities;

//import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;
import static com.vision_digital.activities.DashboardActivity.instituteForProfile;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.BuildConfig;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vision_digital.R;
import com.vision_digital.community.CommunityChatPageActivity;
import com.vision_digital.community.invitation.SelectStudentActivity;
import com.vision_digital.community.studentModel.ItemStudents;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.vision_digital.model.myCourses.ItemMyCourse;
import com.vision_digital.model.searchQueryMilestone.ItemSearchQueryAdapter;
import com.vision_digital.model.suggestedMileStone.ItemSuggestedMileStoneAdapter;
import com.vision_digital.model.suggestedMileStone.ItemSuggestedMileStones;
import com.vision_digital.profile.EditProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MileStoneVideoPlayerActivity extends AppCompatActivity {

    //    ArrayList<Uri> mediaList = new ArrayList<>();
    public ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();
    public ArrayList<String> searchedGetIntentMileStoneList = new ArrayList<>();
    int mediaIndex;
    int seekTo;
    public static int chapter_id;
    int studId;
    public static String courseIdM = "";
    private String uid = "";

    public static String cid = "";

    ImageButton expo_prev, exo_next;
    boolean isNext = false;
    boolean isPrev = false;

    //Layout-----------------------------
    LinearLayout opacityLayout;
    LinearLayout doubtSearch;
    PlayerView mileStoneVideoView;
    SimpleExoPlayer player;

    ImageView backBtn;
    TextView topicName;

    RelativeLayout videoController;
    boolean controllerOn = true, isPause = true;


    //fetching milestones-------------
    String getSuggestedMilestones = "";
    RecyclerView innerMileStoneList;
    ArrayList<ItemSuggestedMileStones> suggestedMileStones = new ArrayList<>();
    ArrayList<ItemMileStone> suggestedMileStoneList = new ArrayList<>();
    ItemSuggestedMileStoneAdapter itemSuggestedMileStoneAdapter;
    final Handler hideControllsHandler = new Handler(Looper.getMainLooper());

    SharedPreferences.Editor editor, courseEditor;

    Handler popupQuestionChecker = new Handler(Looper.getMainLooper());
    boolean questionFlag = true;
    String question, option1, option2, option3, option4, answer, questionId;
    long timeLimit, questionTimeLimit;
    List<String> options = new ArrayList<>();
    String answerResult = "";
    private ImageView goToCommunityBtn, sendInvitationBtn, playerSettingBtn;
    public static String courseNameM = "";
    public static String courseLogoM = "";
    private String subscription = "";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String isInCommunity = "";
    AlertDialog alertDialog;
    private String studentName;
    private String userNameString = "";
    private String profileImageUrlString = "";

    //this chapter id is using in update playing status and SaveQuestionResponse api
    String chap_id = "";

    String mileUpdateType = "";
    final Handler timer = new Handler(Looper.getMainLooper());
    final Handler timerforgetlastposition = new Handler(Looper.getMainLooper());
    final Handler timerForDuration = new Handler(Looper.getMainLooper());
    long duration = 0;
    //Timmer------------------------
    long min, sec;

    // for getting chapter id of suggested milestone
    String courseDetailsUrl = "";
    int suggested_milestone_id;
    int suggested_chapter_id;


    long startTime = 0;
    long endTime = 0;
    int a = 1;

    boolean isStopPressed = false;
    boolean isActivityPause = false;
    DefaultTimeBar exo_progress;

    //search query
    EditText searchEdt;
    ImageView searchClearBtn;

    ImageView closeSearchRecycler;

    public static String searchKeyword = "";
    public static String searchId = "";

    String isDoubtSolved = "0";

    RecyclerView searchMileStoneRecyclerView;

    ItemSearchQueryAdapter itemSearchQueryAdapter;
    ArrayList<ItemMileStone> searchedMileStonesList = new ArrayList<>();

    private ProgressDialog dialog;


    //search data update
    String from_course_id = "";
    String from_chapter = "0";
    String from_milestone = "";
    String from_search_keyword = "";
    String from_search_id = "";
    String to_course_id = "";
    String to_milestone_id = "";
    String to_chapter_id = "1";

    public static String chapter_id_from = "";

    public static String milestone_id = "";
    int resultcode = 1;

    ConstraintLayout top_constrain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mile_stone_video_player);


        uid=DashboardActivity.uid;
        dialog = new ProgressDialog(MileStoneVideoPlayerActivity.this);
        top_constrain = findViewById(R.id.top_constrain);
        goToCommunityBtn = findViewById(R.id.goToCommunityBtn);
        sendInvitationBtn = findViewById(R.id.sendInvitationBtn);
        opacityLayout = findViewById(R.id.opacityLayout);
        opacityLayout.setVisibility(View.GONE);
        playerSettingBtn = findViewById(R.id.playerSettingBtn);
        doubtSearch = findViewById(R.id.doubtSearch);
        searchClearBtn = findViewById(R.id.searchClearBtn);
        searchEdt = findViewById(R.id.searchEdt);
        closeSearchRecycler = findViewById(R.id.closeSearchRecycler);
        searchMileStoneRecyclerView = findViewById(R.id.searchMileStoneList);
        searchMileStoneRecyclerView.setVisibility(View.GONE);

        courseDetailsUrl = getString(R.string.apiURL) + "getCourseDetails";

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        topicName = findViewById(R.id.topicName);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        // searchQuery

        closeSearchRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMileStoneRecyclerView.setVisibility(View.GONE);
                closeSearchRecycler.setVisibility(View.GONE);
            }
        });

        searchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdt.setText("");
            }
        });
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager keyboard = (InputMethodManager) MileStoneVideoPlayerActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(searchEdt.getWindowToken(), 0);
                    new GetSearchedMileStones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    return true;
                }

                return false;
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKeyword = searchEdt.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchKeyword = searchEdt.getText().toString();
            }
        });
        searchKeyword = searchEdt.getText().toString().trim();

        expo_prev = findViewById(R.id.exo_prev);
        exo_next = findViewById(R.id.exo_next);
        exo_progress = findViewById(R.id.exo_progress);


        exo_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNext = true;
                new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Log.e("chapter_id", String.valueOf(chapter_id));
//                new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        expo_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrev = true;

                new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Log.e("chapter_id", String.valueOf(chapter_id));
//                new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        videoController = findViewById(R.id.videoController);
        innerMileStoneList = findViewById(R.id.innerMileStoneList);

//        mediaList = getIntent().getParcelableArrayListExtra("mediaList");
        mediaIndex = getIntent().getIntExtra("videoPosition", 0);
        chapter_id = getIntent().getIntExtra("chapter_id", 0);
        courseIdM = getIntent().getStringExtra("id");
        courseNameM = getIntent().getStringExtra("name");
        courseLogoM = getIntent().getStringExtra("logo");
        subscription = getIntent().getStringExtra("subscriptionStatus");
        Log.e("courseIdMilestone", courseIdM);
        Log.e("courseLogoM", courseLogoM);

        mileUpdateType = getIntent().getStringExtra("mileUpdateType");
        if (mileUpdateType.equals("search_mile_click")) {
            from_chapter = getIntent().getStringExtra("from_chapter_id");
            to_milestone_id = getIntent().getStringExtra("to_milestone");
            from_course_id = getIntent().getStringExtra("from_course_id");
            milestone_id = getIntent().getStringExtra("from_course_id");
            from_milestone = getIntent().getStringExtra("from_milestone");
            from_search_keyword = getIntent().getStringExtra("search_Keyword");
            from_search_id = getIntent().getStringExtra("search_id");
            to_course_id = getIntent().getStringExtra("course_id");
            to_chapter_id = getIntent().getStringExtra("chapter_id");

        }

        mileStoneArrayList = (ArrayList<ItemMileStone>) getIntent().getSerializableExtra("mileStonesList");
        chap_id = String.valueOf(getIntent().getIntExtra("chapter_id", 0));
        goToCommunityBtn.setVisibility(View.GONE);
        top_constrain.setVisibility(View.GONE);
        sendInvitationBtn.setVisibility(View.GONE);


        Log.e("MediaIndex", mediaIndex + "");
        Log.e("chapter_id", chapter_id + "");
//        Log.e("courseIdMilestone",courseId);


        goToCommunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userNameString.equals("")) {
                    communityEnterVerification();

                } else {
                    popupForCompleteProfile();
                }


            }
        });

        sendInvitationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userNameString.equals("")) {
//                    if (subscription.equals("subscribed")){
                    Intent invitationIntent = new Intent(MileStoneVideoPlayerActivity.this, SelectStudentActivity.class);
                    invitationIntent.putExtra("community_id", courseIdM);
                    invitationIntent.putExtra("community_name", courseNameM);
                    invitationIntent.putExtra("community_logo", courseLogoM);
                    invitationIntent.putExtra("userNameString", userNameString);
                    invitationIntent.putExtra("milestone_id", mileStoneArrayList.get(player.getCurrentWindowIndex()).getId());
                    invitationIntent.putExtra("milestone_name", mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle());
                    invitationIntent.putExtra("videoPos", String.valueOf(mediaIndex));
                    invitationIntent.putExtra("fromActivity", "milestonePlayer");
                    Log.e("mediaIndex", String.valueOf(mediaIndex));

                    startActivity(invitationIntent);
//                    }else{
//                        Toast.makeText(MileStoneVideoPlayerActivity.this, "Subscribe this course for the sending invitation! ", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    popupForCompleteProfile();
                }
            }
        });

        cid = courseIdM;


        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);

        mileStoneVideoView = findViewById(R.id.mileStoneVideoView);

        player = new SimpleExoPlayer.Builder(MileStoneVideoPlayerActivity.this).build();
        mileStoneVideoView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(MileStoneVideoPlayerActivity.this, Util.getUserAgent(MileStoneVideoPlayerActivity.this, "CNB"), new DefaultBandwidthMeter());

        for (ItemMileStone itemMileStone : mileStoneArrayList) {
            if (!itemMileStone.getVideoUrl().equals("")) {
                Uri uri = Uri.parse(itemMileStone.getVideoUrl());
                Log.e("lastUrl", itemMileStone.getVideoUrl());
                MediaSource tempmediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                player.addMediaSource(tempmediaSource);
            }
        }


        SharedPreferences mileStoneDetails = getSharedPreferences("mileStoneId_" + mileStoneArrayList.get(mediaIndex).getId(), MODE_PRIVATE);
        seekTo = mileStoneDetails.getInt("playbackTime", 0);

        Log.e("seekTo", "mileStoneId_" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "hello" + seekTo);

        boolean fromStart = getIntent().getBooleanExtra("fromMile", false);
        if (fromStart) {
            player.seekTo(mediaIndex, 0);

        } else {
            player.seekTo(mediaIndex, seekTo);

        }
//        player.seekTo(mediaIndex, seekTo);
        player.prepare();
        player.play();
        milestone_id = mileStoneArrayList.get(player.getCurrentWindowIndex()).getId();
        chapter_id_from = String.valueOf(mileStoneArrayList.get(player.getCurrentWindowIndex()).getChapterId());
        startTime = player.getCurrentPosition();
        Log.e("start time", String.valueOf(startTime));


        new GetSuggestedMileStones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        player.addListener(new Player.EventListener() {
            @Override
            public void onMediaItemTransition(MediaItem mediaItem, int reason) {
                questionFlag = true;
                new GetSuggestedMileStones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }


            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
                Log.e("Timeline", timeline.toString());
                Log.e("Timeline", String.valueOf(reason));
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

//                jab video complete hoga tb data update krne ke liye
                timerforgetlastposition.removeCallbacksAndMessages(null);
                a = 0;
                updatePlayingStVideoComplete();

//                video ka total duration nikalne ke liye
                timerForDuration.removeCallbacksAndMessages(null);

                duration = 0;
                totalDurationSpent();


                startTime = player.getCurrentPosition();

                itemSuggestedMileStoneAdapter.notifyDataSetChanged();
//                new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Log.e("topic name", mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle());
                String topicsName = mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle();

                topicName.setText(topicsName);


            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying && isPause) {
                    innerMileStoneList.setVisibility(View.GONE);
                    doubtSearch.setVisibility(View.GONE);
                    opacityLayout.setVisibility(View.GONE);
                    isPause = false;
                    isPlaying = false;

                }
//                if (controllerOn==false && isPlaying==false){
//                    innerMileStoneList.setVisibility(View.VISIBLE);
//                    isPlaying=true;
//
//
//                }
                else {
                    innerMileStoneList.setVisibility(View.VISIBLE);
                    doubtSearch.setVisibility(View.VISIBLE);
                    opacityLayout.setVisibility(View.VISIBLE);
                    itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                    isPause = true;
                    isPlaying = true;
                }
                //Do your work
                long videoWatchedTime = player.getCurrentPosition() / 1000;
                Log.e("time", "" + videoWatchedTime + suggestedMileStones.isEmpty());
                if (!suggestedMileStones.isEmpty()) {
                    Log.e("ploting", "fff");

                    for (ItemSuggestedMileStones itemSuggestedMileStones : suggestedMileStones) {
                        if (itemSuggestedMileStones.getStartedAt() < videoWatchedTime && itemSuggestedMileStones.getEndAt() > videoWatchedTime) {
                            for (ItemMileStone itemMileStone : itemSuggestedMileStones.getMileStoneArrayList()) {
                                if (!suggestedMileStoneList.contains(itemMileStone)) {
                                    suggestedMileStoneList.add(itemMileStone);
                                }
                            }
                        } else if (itemSuggestedMileStones.getStartedAt() < videoWatchedTime || itemSuggestedMileStones.getEndAt() > videoWatchedTime) {
                            for (ItemMileStone itemMileStone : itemSuggestedMileStones.getMileStoneArrayList()) {
                                if (suggestedMileStoneList.contains(itemMileStone)) {
                                    suggestedMileStoneList.remove(itemMileStone);
                                }
                                if (suggestedMileStoneList.isEmpty()) {
                                    innerMileStoneList.setVisibility(View.GONE);
                                    doubtSearch.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                } else {
                    innerMileStoneList.setVisibility(View.GONE);
                    doubtSearch.setVisibility(View.GONE);
                }


            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                new AlertDialog.Builder(MileStoneVideoPlayerActivity.this)
                        .setTitle("Playback Error")
                        .setMessage("Your devices memory is full. It will not support further videos")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                onBackPressed();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }


        });

        //fetching suggested milestones--------------
        getSuggestedMilestones = getApplicationContext().getString(R.string.apiURL) + "getSuggestedMilestones";

        updateListOfSuggestedMileStones();

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        innerMileStoneList.setLayoutManager(layoutManager);
        itemSuggestedMileStoneAdapter = new ItemSuggestedMileStoneAdapter(suggestedMileStoneList);
        innerMileStoneList.setAdapter(itemSuggestedMileStoneAdapter);


        // searched Query
//        LinearLayoutManager layoutManagerTwo = new LinearLayoutManager(MileStoneVideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
//        searchMileStoneRecyclerView.setLayoutManager(layoutManagerTwo);
//        itemSearchQueryAdapter = new ItemSearchQueryAdapter(searchedMileStonesList);
//        searchMileStoneRecyclerView.setAdapter(itemSearchQueryAdapter);


        mileStoneVideoView.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideControllsHandler.removeCallbacksAndMessages(null);
                hideControllsHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //invisible after 3 sec
                        if (controllerOn) {
                            videoController.setVisibility(View.GONE);
                            top_constrain.setVisibility(View.GONE);
                            goToCommunityBtn.setVisibility(View.GONE);
                            sendInvitationBtn.setVisibility(View.GONE);
                            playerSettingBtn.setVisibility(View.GONE);
//                            opacityLayout.setVisibility(View.GONE);
                            mileStoneVideoView.hideController();
                           // innerMileStoneList.setVisibility(View.GONE);
                            // doubtSearch.setVisibility(View.GONE);

                            controllerOn = false;

                        }
//                        else if (controllerOn && isRun){
//                            videoController.setVisibility(View.GONE);
//                            mileStoneVideoView.hideController();
//                            innerMileStoneList.setVisibility(View.GONE);
//                            controllerOn = false;
//                            isRun=false;
//
//                        }
                    }
                }, player.getDuration());
                //4000 hereis duration is given

                //Do your work
                long videoWatchedTime = player.getCurrentPosition() / 1000;
                Log.e("time", "" + videoWatchedTime + suggestedMileStones.isEmpty());
                if (!suggestedMileStones.isEmpty()) {
                    Log.e("ploting", "fff");
                    suggestedMileStoneList.clear();

                    for (ItemSuggestedMileStones itemSuggestedMileStones : suggestedMileStones) {
                        if (itemSuggestedMileStones.getStartedAt() < videoWatchedTime && itemSuggestedMileStones.getEndAt() > videoWatchedTime) {
                            for (ItemMileStone itemMileStone : itemSuggestedMileStones.getMileStoneArrayList()) {
                                if (!suggestedMileStoneList.contains(itemMileStone)) {
                                    suggestedMileStoneList.add(itemMileStone);
                                }
                            }
                        } else if (itemSuggestedMileStones.getStartedAt() < videoWatchedTime || itemSuggestedMileStones.getEndAt() > videoWatchedTime) {
                            for (ItemMileStone itemMileStone : itemSuggestedMileStones.getMileStoneArrayList()) {
                                if (suggestedMileStoneList.contains(itemMileStone)) {
                                    suggestedMileStoneList.remove(itemMileStone);
                                }
                                if (suggestedMileStoneList.isEmpty()) {
                                    innerMileStoneList.setVisibility(View.GONE);
                                    doubtSearch.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                } else {
                    innerMileStoneList.setVisibility(View.GONE);
                    doubtSearch.setVisibility(View.GONE);
                }


                if (controllerOn) {
                    videoController.setVisibility(View.GONE);
                    goToCommunityBtn.setVisibility(View.GONE);
                    top_constrain.setVisibility(View.GONE);
                    sendInvitationBtn.setVisibility(View.GONE);
                    playerSettingBtn.setVisibility(View.GONE);
//                    opacityLayout.setVisibility(View.GONE);
                    mileStoneVideoView.hideController();
                    innerMileStoneList.setVisibility(View.GONE);
                    doubtSearch.setVisibility(View.GONE);
                    controllerOn = false;
                } else {
                    videoController.setVisibility(View.VISIBLE);
                    goToCommunityBtn.setVisibility(View.GONE);
                    top_constrain.setVisibility(View.VISIBLE);
                    sendInvitationBtn.setVisibility(View.GONE);
                    playerSettingBtn.setVisibility(View.GONE);
//                    opacityLayout.setVisibility(View.VISIBLE);
                    mileStoneVideoView.showController();
                    if (isPause) {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                        doubtSearch.setVisibility(View.VISIBLE);
                        itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                        isPause = true;
                    }
                    controllerOn = true;
                }
            }
        });


        getProfileData();

//        updatePlayingStatusInInterval();
    }

    private void totalDurationSpent() {
        final Runnable runnableTimer = new Runnable() {
            @Override
            public void run() {
                Log.e("duration", String.valueOf(duration));
                if (player.isPlaying()) {
                    duration = duration + 1;
                    timerForDuration.postDelayed(this, 1000);
                } else {
                    timerForDuration.postDelayed(this, 1000);
                }


            }
        };
        timerForDuration.postDelayed(runnableTimer, 1000);


    }

    private void updatePlayingStVideoComplete() {

        final Runnable runnableTimer = new Runnable() {
            @Override
            public void run() {
                a = a + 1;
                Log.e("a", "in If:" + a);
                if ((player.getDuration() - player.getCurrentPosition()) / 1000 == 0) {
                    Log.e("lastpointer", "in If:" + player.getCurrentPosition() / 1000);
                    Log.e("a2", "in If2:" + a);
                    if (!mileUpdateType.equals("search_mile_click")) {
                        new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } else {
                        isDoubtSolved = "1";
                        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new UpdateSearchedQueryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                } else {
                    timerforgetlastposition.postDelayed(this, 1000);
                }

            }
        };
        timerforgetlastposition.postDelayed(runnableTimer, 1000);


    }

    private void popupForCompleteProfiles() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
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


        title.setText("Choose your username");
        message.setText("And more details about you");
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
                Intent community = new Intent(MileStoneVideoPlayerActivity.this, EditProfileActivity.class);
                community.putExtra("ForCommunity", "yes");
                startActivity(community);
                alertDialog.dismiss();


            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

    }

    private void communityEnterVerification() {

        db.collection("Communities").document(courseIdM).collection("Students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        String sids = documentChange.getDocument().getData().get("student_id").toString();
                        Log.e("studentIdloop12", sids);


                        if (uid.equals(sids)) {

                            isInCommunity = "yes";

                            break;

                        } else {
                            isInCommunity = "no";

                        }
                    }

                } else {
                    isInCommunity = "no";
                }
                Log.e("isInCommunity", isInCommunity);
                if (!userNameString.equals("")) {
                    if (isInCommunity.equals("yes")) {
                        Intent communityIntent = new Intent(MileStoneVideoPlayerActivity.this, CommunityChatPageActivity.class);
                        communityIntent.putExtra("activity", "milestone");
                        communityIntent.putExtra("milestone_id", mileStoneArrayList.get(player.getCurrentWindowIndex()).getId());
                        communityIntent.putExtra("community_id", courseIdM);
                        communityIntent.putExtra("community_name", courseNameM);
                        communityIntent.putExtra("videoPos", mediaIndex);
                        communityIntent.putExtra("community_logo", courseLogoM);
                        Log.e("Student saved", "student saved");
                        startActivity(communityIntent);
                    } else {
                        askToJoin(courseIdM, courseNameM, courseLogoM);
                    }
                } else {
                    popupForCompleteProfile();
                }


            }
        });


    }

    private void popupForCompleteProfile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
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


        title.setText("Choose your username");
        message.setText("Ad bd more details about you");
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
                Intent community = new Intent(MileStoneVideoPlayerActivity.this, EditProfileActivity.class);
                community.putExtra("ForCommunity", "yes");
                community.putExtra("instituteForProfile", instituteForProfile);
                community.putExtra("fromActivity", "homePage");
                startActivity(community);
                alertDialog.dismiss();


            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

    }

    private void askToJoin(String id, String community_name_recommendation, String community_logo_recommendation) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
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
                joinStudentInGroup(courseIdM, studentName, courseNameM, courseLogoM);
                alertDialog.dismiss();

            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);


    }

    private void joinStudentInGroup(String communityId, String studentName, String community_name_recommendation, String community_logo_recommendation) {

        DocumentReference reference = db.collection("Communities").document(communityId).collection("Students").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ItemStudents studentDetails = new ItemStudents(uid, studentName, profileImageUrlString);


        reference.set(studentDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent communityIntent = new Intent(MileStoneVideoPlayerActivity.this, CommunityChatPageActivity.class);
                communityIntent.putExtra("community_id", courseIdM);
                communityIntent.putExtra("community_name", courseNameM);
                communityIntent.putExtra("community_logo", courseLogoM);
                communityIntent.putExtra("activity", "community_list");
                startActivity(communityIntent);
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

                        DocumentReference uidRef = db.collection("StudentProfile").document(String.valueOf(studId));
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        userNameString = document.getString("userName");

                                        profileImageUrlString = document.getString("profileImage");


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

    private void popupQuestion() {

        popupQuestionChecker.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do your work
                if ((player.getDuration() - player.getCurrentPosition()) / 1000 == 0 && questionFlag == true) {

                    Log.e("popupquestion", "run: " + player.getDuration() + "  minus  " + (player.getDuration() - player.getCurrentPosition()) / 1000);
                    final Dialog dialog = new Dialog(MileStoneVideoPlayerActivity.this, android.R.style.Theme_Light);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_question_btw_milestones);
                    dialog.setCanceledOnTouchOutside(false);
                    //Plotting questions
                    TextView questionView = dialog.findViewById(R.id.question);
                    questionView.setText(question);
                    final TextView option1View = dialog.findViewById(R.id.option1);
                    option1View.setText(option1);
                    final TextView option2View = dialog.findViewById(R.id.option2);
                    option2View.setText(option2);
                    final TextView option3View = dialog.findViewById(R.id.option3);
                    option3View.setText(option3);
                    final TextView option4View = dialog.findViewById(R.id.option4);
                    option4View.setText(option4);

                    //Plotting Timer--------------
                    final TextView timerTextView = dialog.findViewById(R.id.timmer);

                    final Handler timer = new Handler(Looper.getMainLooper());

                    timeLimit = questionTimeLimit;

                    final Runnable runnableTimer = new Runnable() {
                        @Override
                        public void run() {
                            timeLimit--;
                            min = timeLimit / 60;
                            sec = timeLimit % 60;
                            String minut = String.format("%02d", min);
                            String second = String.format("%02d", sec);
                            timerTextView.setText(minut + ":" + second);

                            Log.e("timmer", "" + timeLimit);

                            if (timeLimit != 0) {
                                timer.postDelayed(this, 1000);
                            }
                            if (timeLimit == 0) {
                                final Dialog timeOverDialog = new Dialog(MileStoneVideoPlayerActivity.this);
                                timeOverDialog.setContentView(R.layout.popup_result_question_btw_milestones);
                                timeOverDialog.setCanceledOnTouchOutside(false);
                                Window window = timeOverDialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                TextView learnAgainBtn = timeOverDialog.findViewById(R.id.learnAgainBtn);
                                TextView letsProceed = timeOverDialog.findViewById(R.id.letsProceed);
                                TextView resultMsg = timeOverDialog.findViewById(R.id.resultMgs);
                                resultMsg.setText("Hey Buddy.. Are you there?");
                                learnAgainBtn.setVisibility(View.VISIBLE);
                                letsProceed.setVisibility(View.GONE);
                                learnAgainBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.hide();
                                        timeOverDialog.hide();
                                        player.seekTo(0);
                                        startTime = player.getCurrentPosition();
                                        Log.d("startTimeLearnAg", String.valueOf(startTime));
                                        questionFlag = true;
                                        player.play();
                                    }
                                });

                                timeOverDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                        if (i == KeyEvent.KEYCODE_BACK) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                });
                                timeOverDialog.show();

                            }

                        }
                    };
                    timer.postDelayed(runnableTimer, 1000);


                    //Implementing Click events
                    final CardView option1Card, option2Card, option3Card, option4Card;
                    option1Card = dialog.findViewById(R.id.option1Card);
                    option1Card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.removeCallbacksAndMessages(null);
                            checkAnswer(options.get(0), option1Card, option1View, dialog);
                        }
                    });
                    option2Card = dialog.findViewById(R.id.option2Card);
                    option2Card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.removeCallbacksAndMessages(null);
                            checkAnswer(options.get(1), option2Card, option2View, dialog);
                        }
                    });
                    option3Card = dialog.findViewById(R.id.option3Card);
                    option3Card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.removeCallbacksAndMessages(null);
                            checkAnswer(options.get(2), option3Card, option3View, dialog);
                        }
                    });
                    option4Card = dialog.findViewById(R.id.option4Card);
                    option4Card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.removeCallbacksAndMessages(null);
                            checkAnswer(options.get(3), option4Card, option4View, dialog);
                        }
                    });


                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            if (i == KeyEvent.KEYCODE_BACK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    dialog.show();
                    player.pause();
                    questionFlag = false;
                }
                popupQuestionChecker.postDelayed(this, 1000);
            }
        }, 1000);

    }

    private void checkAnswer(String option, CardView cardview, TextView textView, Dialog dialog) {
        if (option.equals(answer)) {
            cardview.setBackgroundColor(Color.GREEN);
            textView.setTextColor(Color.WHITE);
            popupResult(true, dialog);
            answerResult = "correct";

        } else {
            cardview.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
            popupResult(false, dialog);
            answerResult = "incorrect";
        }

//        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new SaveQuestionResponse().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void popupResult(boolean result, final Dialog questionDialog) {
        final Dialog dialog = new Dialog(MileStoneVideoPlayerActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.popup_result_question_btw_milestones);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView learnAgainBtn = dialog.findViewById(R.id.learnAgainBtn);
        TextView letsProceed = dialog.findViewById(R.id.letsProceed);
        TextView resultMsg = dialog.findViewById(R.id.resultMgs);

        if (result) {
            resultMsg.setText("Congratulations ! It's Right.");
            learnAgainBtn.setVisibility(View.GONE);
            letsProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    questionDialog.hide();
                    player.play();
                }
            });


        } else {
            resultMsg.setText("Oops ! You are wrong.");
            learnAgainBtn.setVisibility(View.VISIBLE);
            learnAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.hide();
                    questionDialog.hide();
                    player.seekTo(0);
                    questionFlag = true;
                    duration = 0;
                    player.play();
                }
            });
            letsProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    questionDialog.hide();
                    player.play();
                }
            });

        }

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();
    }

    private void updateListOfSuggestedMileStones() {

//        final Handler suggestionUpdater = new Handler(Looper.getMainLooper());
//        suggestionUpdater.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do your work
//                long videoWatchedTime = player.getCurrentPosition() / 1000;
//                Log.e("time", "" + videoWatchedTime);
//                if (!suggestedMileStones.isEmpty()) {
//                    innerMileStoneList.setVisibility(View.VISIBLE);
//                    for (ItemSuggestedMileStones itemSuggestedMileStones : suggestedMileStones) {
//                        if (itemSuggestedMileStones.getStartedAt() < videoWatchedTime && itemSuggestedMileStones.getEndAt() > videoWatchedTime) {
//                            for (ItemMileStone itemMileStone : itemSuggestedMileStones.getMileStoneArrayList()) {
//                                if (!suggestedMileStoneList.contains(itemMileStone)) {
//                                    suggestedMileStoneList.add(itemMileStone);
//                                }
//                            }
//                        } else if (itemSuggestedMileStones.getStartedAt() < videoWatchedTime || itemSuggestedMileStones.getEndAt() > videoWatchedTime) {
//                            for (ItemMileStone itemMileStone : itemSuggestedMileStones.getMileStoneArrayList()) {
//                                if (suggestedMileStoneList.contains(itemMileStone)) {
//                                    suggestedMileStoneList.remove(itemMileStone);
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    innerMileStoneList.setVisibility(View.GONE);
//                }

//            }
//        }, 1000);

    }

    class GetSuggestedMileStones extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
            editor = getSharedPreferences("mileStoneId_" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId(), MODE_PRIVATE).edit();

        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(MileStoneVideoPlayerActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "";
//            Log.e("topic name", mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle());
//            String topicsName = mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle();
//
//            topicName.setText(topicsName);
            if (!mileUpdateType.equals("search_mile_click")) {
                param = "milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId();
            } else {
                param = "milestone_id=" + to_milestone_id;


            }
            player.setThrowsWhenUsingWrongThread(false);
            Log.e("param", param);


            JSONObject jsonObject = jsonParser.makeHttpRequest(getSuggestedMilestones, "POST", param);
            if (jsonObject != null) {
                Log.d(TAG, "doInBackground: " + jsonObject);
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
                    switch (status) {
                        case "success":
                            //Running Fine----
                            JSONArray milestonesArray = dataObj.getJSONArray("milestones");
                            suggestedMileStones.clear();
                            for (int i = 0; i < milestonesArray.length(); i++) {
                                JSONObject mileStone = milestonesArray.getJSONObject(i);
                                ItemSuggestedMileStones itemSuggestedMileStones = new ItemSuggestedMileStones();
                                itemSuggestedMileStones.setStartedAt(mileStone.getLong("startTime"));
                                itemSuggestedMileStones.setEndAt(mileStone.getLong("endTime"));
                                JSONArray linksArray = mileStone.getJSONArray("links");
                                ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();
                                for (int j = 0; j < linksArray.length(); j++) {
                                    ItemMileStone itemMileStone = new ItemMileStone();
                                    JSONObject mileStoneObj = linksArray.getJSONObject(j);
                                    itemMileStone.setId(mileStoneObj.getString("id"));
                                    itemMileStone.setTitle(mileStoneObj.getString("title"));
                                    itemMileStone.setDuration(mileStoneObj.getString("duration"));
                                    itemMileStone.setVideoUrl(mileStoneObj.getString("video_link"));
                                    itemMileStone.setActivityType("milestone_player");
                                    itemMileStone.setMilestoneType("suggested_milestone");

                                    Log.e("suggested_chapter_id11", String.valueOf(suggested_chapter_id));
                                    itemMileStone.setChapterId(Integer.parseInt(chap_id));
                                    mileStoneArrayList.add(itemMileStone);
                                }
                                itemSuggestedMileStones.setMileStoneArrayList(mileStoneArrayList);
                                suggestedMileStones.add(itemSuggestedMileStones);
                                Log.e("epttyhai", "" + suggestedMileStones.isEmpty());
                            }


                            JSONArray questionsArray = dataObj.getJSONArray("question");

                            options.clear();
                            options.add("option1");
                            options.add("option2");
                            options.add("option3");
                            options.add("option4");
                            Collections.shuffle(options);

                            for (int i = 0; i < questionsArray.length(); i++) {
                                JSONObject questionObj = questionsArray.getJSONObject(i);
                                questionId = questionObj.getString("id");
                                question = questionObj.getString("question");
                                option1 = questionObj.getString(options.get(0));
                                option2 = questionObj.getString(options.get(1));
                                option3 = questionObj.getString(options.get(2));
                                option4 = questionObj.getString(options.get(3));
                                answer = questionObj.getString("answer");
                                questionTimeLimit = Long.parseLong(questionObj.getString("time_limit"));

                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
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
                                    finishAndRemoveTask();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

    class GetSearchedMileStones extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait!..");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser(MileStoneVideoPlayerActivity.this);

            long from_mile_duration = player.getCurrentPosition() / 1000;
            String param;
            if (!mileUpdateType.equals("search_mile_click")) {
                param = "sid=" + studId + "&course_id=" + courseIdM + "&chapter_id=" +
                        chapter_id + "&milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "&search_keyword=" + searchKeyword + "&from_milestone_duration=" + from_mile_duration;
            } else {

                param = "sid=" + studId + "&course_id=" + courseIdM + "&chapter_id=" +
                        chapter_id + "&milestone_id=" + to_milestone_id + "&search_keyword=" + searchKeyword + "&from_milestone_duration=" + from_mile_duration;


            }

            player.setThrowsWhenUsingWrongThread(false);
            Log.e("param", param);


            String url1 = getApplicationContext().getString(R.string.apiURL)+"getSearchData";
            Log.e("url", url1);


            JSONObject jsonObject = jsonParser.makeHttpRequest(url1, "POST", param);
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

                    switch (status) {
                        case "true":
                            //Running Fine----
                            final JSONObject dataObj = jsonObject.getJSONObject("data");
                            searchId = dataObj.getString("search_id");
                            searchKeyword = dataObj.getString("search_keyword");

                            JSONArray milestonesArray = dataObj.getJSONArray("lists");
                            ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();
                            searchedMileStonesList.clear();
                            if (milestonesArray.length() != 0) {

                                for (int i = 0; i < milestonesArray.length(); i++) {
                                    JSONObject mileStone = milestonesArray.getJSONObject(i);
                                    ItemMileStone itemSearchQuery = new ItemMileStone();
                                    itemSearchQuery.setSearchMileId(mileStone.getString("milestone_id"));
                                    itemSearchQuery.setCourseId(mileStone.getString("course_id"));
                                    itemSearchQuery.setChapterId(Integer.parseInt(mileStone.getString("chapter_id")));
                                    itemSearchQuery.setChapterIdString(mileStone.getString("chapter_id"));
                                    itemSearchQuery.setTitle(mileStone.getString("milestone_title"));
                                    itemSearchQuery.setDuration(mileStone.getString("duration"));
                                    itemSearchQuery.setVideoUrl(mileStone.getString("uploaded_url"));
                                    itemSearchQuery.setActivityType("milestone_player");
                                    itemSearchQuery.setMilestoneType("search_mile_click");
                                    itemSearchQuery.setSearchId(searchId);
                                    itemSearchQuery.setSearchKeyword(searchKeyword);
                                    itemSearchQuery.setCourseLogo(courseLogoM);


                                    searchedMileStonesList.add(itemSearchQuery);

                                    Log.e("epttyhai", "" + suggestedMileStones.isEmpty());
                                }


                                // searched Query
                                LinearLayoutManager layoutManagerTwo = new LinearLayoutManager(MileStoneVideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
                                searchMileStoneRecyclerView.setLayoutManager(layoutManagerTwo);
                                Log.d(TAG, "onPostExecute: " + searchedMileStonesList);
                                itemSearchQueryAdapter = new ItemSearchQueryAdapter(searchedMileStonesList);
                                searchMileStoneRecyclerView.setAdapter(itemSearchQueryAdapter);
                                itemSearchQueryAdapter.notifyDataSetChanged();
                                innerMileStoneList.setVisibility(View.GONE);
                                searchMileStoneRecyclerView.setVisibility(View.VISIBLE);
                                closeSearchRecycler.setVisibility(View.VISIBLE);
                                doubtSearch.setVisibility(View.GONE);
                                mileStoneVideoView.hideController();
                                searchEdt.setText("");
                            } else {
                                Toast.makeText(MileStoneVideoPlayerActivity.this, "We have saved your doubt and we will solved it soon!..", Toast.LENGTH_SHORT).show();

                            }

                            dialog.dismiss();
                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
//                            String msgContent = dataObj.getString("message");
//                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishAndRemoveTask();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            dialog.dismiss();
                            break;
                        case "failure":
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            break;
                        default:
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }

    class UpdateSearchedQueryData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait!..");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser(MileStoneVideoPlayerActivity.this);
            String param;
            if (mileUpdateType.equals("search_mile_click")) {
                param = "sid=" + studId + "&from_course_id=" + from_course_id + "&from_chapter_id=" +
                        from_chapter + "&from_milestone_id=" + milestone_id + "&search_keyword=" + from_search_keyword +
                        "&search_id=" + from_search_id + "&to_course_id=" + to_course_id + "&to_chapter_id=" + to_chapter_id +
                        "&to_milestone_id=" + to_milestone_id + "&doubt_solved=" + isDoubtSolved + "&spent_time=" + duration;
            } else {
                param = "sid=" + studId + "&from_course_id=" + from_course_id + "&from_chapter_id=" +
                        from_chapter + "&from_milestone_id=" + milestone_id + "&search_keyword=" + from_search_keyword +
                        "&search_id=" + from_search_id + "&to_course_id=" + to_course_id + "&to_chapter_id=" + to_chapter_id +
                        "&to_milestone_id=" + to_milestone_id + "&doubt_solved=" + isDoubtSolved + "&spent_time=" + duration;

            }

            player.setThrowsWhenUsingWrongThread(false);
            Log.e("param", param);

            String url1 = getApplicationContext().getString(R.string.apiURL)+"saveUpdateQuestionMilestone";
            Log.e("url", url1);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url1, "POST", param);
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

                    switch (status) {
                        case "true":
                            //Running Fine----
//                            final JSONObject dataObj = jsonObject.getJSONObject("data");

//                            Log.d(TAG, "UpdateSearch: " + dataObj.getString("message"));

                            dialog.dismiss();
                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
//                            String msgContent = dataObj.getString("message");
//                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishAndRemoveTask();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            dialog.dismiss();
                            break;
                        case "failure":
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            break;
                        default:
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onresume: onpause" + isActivityPause);
        Log.e(TAG, "onresume: wapas aane pr" + isActivityPause);

        if (isActivityPause) {
            a = 0;
            updatePlayingStVideoComplete();
            duration = 0;
            totalDurationSpent();

//            searchedMileStonesList.clear();
//            searchMileStoneRecyclerView.setVisibility(View.GONE);
//            closeSearchRecycler.setVisibility(View.GONE);
            isActivityPause = false;
        }

        Log.e(TAG, "onResume: " + endTime);
        if (questionFlag) {
            player.play();
        }
        popupQuestion();
        startTime = player.getCurrentPosition();
        if (player.isPlaying()){
            player.pause();
            innerMileStoneList.setVisibility(View.GONE);
        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String myStr=data.getStringExtra("MyData");
                if (myStr.equals("cancel")){

                }else{

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mileUpdateType.equals("search_mile_click")) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
            // ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = MileStoneVideoPlayerActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.exit_popup, null);
            dialogBuilder.setView(dialogView);

            //Alert Dialog Layout work
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
            Button yesBtn = dialogView.findViewById(R.id.yesButton);
            TextView message = dialogView.findViewById(R.id.message);
            TextView title = dialogView.findViewById(R.id.title);
            message.setText("Is Your Doubt Solved?");
            cancelBtn.setText("No");

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    isDoubtSolved = "0";
                    Log.e("onback", "onBack");
                    editor.putInt("playbackTime", (int) player.getCurrentPosition());
                    editor.apply();
                    courseEditor = getSharedPreferences("courseId_" + courseIdM, MODE_PRIVATE).edit();
                    courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
                    courseEditor.apply();
                    player.pause();
                    player.release();
                    popupQuestionChecker.removeCallbacksAndMessages(null);
                    String data ="cancel";
                    Intent intent = new Intent();
                    intent.putExtra("MyData", data);
                    setResult(resultcode, intent);
                    MileStoneVideoPlayerActivity.super.onBackPressed();
                }
            });
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDoubtSolved = "1";
                    Log.e("onback", "onBack");
                    editor.putInt("playbackTime", (int) player.getCurrentPosition());
                    editor.apply();
                    courseEditor = getSharedPreferences("courseId_" + courseIdM, MODE_PRIVATE).edit();
                    courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
                    courseEditor.apply();
                    player.pause();
                    player.release();
                    popupQuestionChecker.removeCallbacksAndMessages(null);
                    MileStoneVideoPlayerActivity.super.onBackPressed();
                }
            });

            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(true);


        } else {
            Log.e("onback", "onBack");
            editor.putInt("playbackTime", (int) player.getCurrentPosition());
            editor.apply();
            courseEditor = getSharedPreferences("courseId_" + courseIdM, MODE_PRIVATE).edit();
            courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
            courseEditor.apply();
            player.pause();
            player.release();
            popupQuestionChecker.removeCallbacksAndMessages(null);
            super.onBackPressed();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        isStopPressed = true;
//        searchedMileStonesList.clear();
//        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (!mileUpdateType.equals("search_mile_click")) {
            new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UpdateSearchedQueryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        }
        timerForDuration.removeCallbacksAndMessages(null);
        timerforgetlastposition.removeCallbacksAndMessages(null);
        Log.e("onstop", "onstop");

        editor.putInt("playbackTime", (int) player.getCurrentPosition());
        editor.apply();
        courseEditor = getSharedPreferences("courseId_" + courseIdM, MODE_PRIVATE).edit();
        courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
        courseEditor.apply();
        player.pause();
        popupQuestionChecker.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ondestroy", "ondestroy");
//        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (isStopPressed) {
            Log.e(TAG, "onDestroy: " + isStopPressed);
        } else {
            timerforgetlastposition.removeCallbacksAndMessages(null);
            timerForDuration.removeCallbacksAndMessages(null);
            if (!mileUpdateType.equals("search_mile_click")) {
                new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                new UpdateSearchedQueryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        }

        editor.putInt("playbackTime", (int) player.getCurrentPosition());
        editor.apply();
        courseEditor = getSharedPreferences("courseId_" + courseIdM, MODE_PRIVATE).edit();
        courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
        courseEditor.apply();
        player.pause();
        player.release();
        popupQuestionChecker.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPause = true;
        Log.e(TAG, "onPause: onpause" + isActivityPause);

    }

    class UpdatePlayingStatus extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        // student_id=160&course_id=17&milestone_id=320&play_duration=17
        @Override
        protected String doInBackground(String... params) {

            endTime = player.getCurrentPosition() / 1000;
            Log.e("endTime", String.valueOf(endTime));

            long startTimeDouble = startTime / 1000;
            long totalDuration = endTime - startTimeDouble;

            Log.e("startTimeDouble", String.valueOf(startTimeDouble));
            Log.e("endTimeDouble", String.valueOf(endTime));
            Log.e("totalDuration", String.valueOf(totalDuration));

            String param;
            if (mileUpdateType.equals("search_mile_click")) {
                param = "student_id=" + studId + "&course_id=" + courseIdM + "&milestone_id=" + to_milestone_id + "&play_duration=" + duration + "&chapter_id=" +
                        chap_id + "&mileUpdateType=" + mileUpdateType + "&startTime=" + startTimeDouble + "&endTime=" + endTime + "&doubt_solved=" + isDoubtSolved;
                Log.e("playingParam", param);
            } else {
                param = "student_id=" + studId + "&course_id=" + courseIdM + "&milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "&play_duration=" + duration + "&chapter_id=" +
                        chap_id + "&mileUpdateType=" + mileUpdateType + "&startTime=" + startTimeDouble + "&endTime=" + endTime + "&doubt_solved=" + isDoubtSolved;
                Log.e("playingParam", param);
            }


            JSONParser jsonParser = new JSONParser(MileStoneVideoPlayerActivity.this);

            Log.i("param update", param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL) + "updatePlayingStatus", "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
                return jsonObject.toString();

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json updat", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result : update", s);

                    //Do work-----------------------------

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (isNext) {
                isNext = false;
                mediaIndex = mediaIndex + 1;
                player.next();
                startTime = player.getCurrentPosition();
                Log.e("startTimeNext", String.valueOf(startTime));
            } else if (isPrev) {
                isPrev = false;
                mediaIndex = mediaIndex - 1;
                player.previous();
            } else {
                Log.e("data update", "data updated");
            }
        }
    }

    class SaveQuestionResponse extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(MileStoneVideoPlayerActivity.this);
            String param = "student_id=" + studId + "&course_id=" + courseIdM + "&milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "&question_id=" + questionId + "&answered_option=" + answer + "&answer=" + answerResult + "&chapter_id=" + chap_id;

            Log.i("param", param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL) + "saveQuestionResponse", "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
                return jsonObject.toString();

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


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class GetChapterIdAndUpdatePlayingStatus extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

//            dialog.setMessage("Please wait");
//            dialog.show();

            JSONParser jsonParser = new JSONParser(MileStoneVideoPlayerActivity.this);
            int versionCode = com.vision_digital.BuildConfig.VERSION_CODE;
            String param = "uid=" + DashboardActivity.uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseIdM;

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

                    switch (status) {
                        case "success":
                            //Running Fine

                            int positionNumber = 0;

                            //Setting course Details for subscribed course--------------------------
                            String subscriptionStatus = dataObj.getString("subscription_status");

                            if (subscriptionStatus.equals("subscribed")) {

                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");

//                                ProgressDialog dialogForIntent = new ProgressDialog(MileStoneVideoPlayerActivity.this);
//                                dialogForIntent.setMessage("Please wait");
//                                dialogForIntent.show();
                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter_id = Integer.parseInt(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));
                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
//                                        dialogForIntent.show();
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);
                                        mileStone.setId(mileStoneObj.getString("id"));
                                        int videoPosTemp = positionNumber++;
                                        mileStone.setVideoPosition(videoPosTemp);
                                        Log.e("Topic name", "" + mileStoneObj.getString("title"));
                                        mileStone.setTitle(mileStoneObj.getString("title"));
                                        mileStone.setDuration(mileStoneObj.getString("duration"));
                                        mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                        mileStone.setVideoUrl(mileStoneObj.getString("video_link"));
                                        mileStone.setMilestoneType("chap_mile_click");
                                        mileStone.setChapterId(chapter_id);

//                                        Log.e("current MilestoneId", String.valueOf(mileStoneArrayList.get(player.getCurrentWindowIndex()).getId()));
//                                        Log.e("checking MilestoneId", String.valueOf(mileStoneObj.getString("id")));

                                        if (mileStoneObj.getString("id").equals(mileStoneArrayList.get(player.getCurrentWindowIndex()).getId())) {
                                            int videoPos = videoPosTemp;


//                                            Log.e("videoPos", String.valueOf(videoPos));

                                            chap_id = chapterObj.getString("id");
//                                            Log.e("chap_id", String.valueOf(chap_id));

                                            break;


                                        } else {
                                            Log.e("No matching", "No matching");
                                        }

                                    }

                                }

                                new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                dialogForIntent.dismiss();
//                                dialog.dismiss();


                            } else if (subscriptionStatus.equals("unsubscribed")) {


                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MileStoneVideoPlayerActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MileStoneVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("StartOn","App background se wapas aane ke baad ");

        if (player.isPlaying()){
            player.pause();
        }else{

        }
    }
}
