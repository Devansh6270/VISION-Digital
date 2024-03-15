package com.vision_classes.activities.videoPlayer;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.exoplayer.source.TrackGroupArray;
import androidx.media3.exoplayer.trackselection.TrackSelectionArray;
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.extractor.Extractor;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vision_classes.R;
import com.vision_classes.activities.DashboardActivity;
import com.vision_classes.activities.MileStoneVideoPlayerActivity;
import com.vision_classes.helperClasses.JSONParser;
import com.vision_classes.model.chapters.ItemChapter;
import com.vision_classes.model.milestone.ItemMileStone;
import com.vision_classes.model.searchQueryMilestone.ItemSearchQueryAdapter;
import com.vision_classes.model.suggestedMileStone.ItemSuggestedMileStoneAdapter;
import com.vision_classes.model.suggestedMileStone.ItemSuggestedMileStones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExoPlayer3Activity extends AppCompatActivity {


//    PlayerView mileStoneVideoView;
//    SimpleExoPlayer player;
//    public static String chapter_id_from = "";
//    long startTime = 0;
//    boolean questionFlag = true;
//
    TextView exo_position, exo_duration;
//    ImageButton exo_prev, exo_rew, exo_play, exo_pause, exo_ffwd, exo_next, exo_vr;
//    String courseDetailsUrl;
//    int mediaIndex, chapter_id, studId, seekTo;
//    String courseIdM, courseNameM, courseLogoM, subscription, mileUpdateType, from_chapter, to_milestone_id, from_course_id, milestone_id, from_milestone;
//    String from_search_keyword, from_search_id, to_course_id, to_chapter_id,chap_id;
//    Boolean isNext, isPrev;
public ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();
    public ArrayList<String> searchedGetIntentMileStoneList = new ArrayList<>();
    int mediaIndex;
    int seekTo;
    public static int chapter_id;
    int studId;
    public static String courseIdM = "";
    private String uid = "";

    public static String cid = "";

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

//    ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();

    @OptIn(markerClass = UnstableApi.class) @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player3);

        mileStoneVideoView = findViewById(R.id.pvVideoPLayer);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.exo_player_3_layout, null);
//        exo_position = layout.findViewById(R.id.exo_position);
//        exo_duration = layout.findViewById(R.id.exo_duration);
//        exo_prev = layout.findViewById(R.id.exo_prev);
//        exo_rew = layout.findViewById(R.id.exo_rew);
//        exo_play = layout.findViewById(R.id.exo_play);
//        exo_ffwd = layout.findViewById(R.id.exo_ffwd);
//        exo_pause = layout.findViewById(R.id.exo_pause);
//        exo_next = layout.findViewById(R.id.exo_next);
//        exo_vr = layout.findViewById(R.id.exo_vr);

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
        videoController = findViewById(R.id.videoController);
        innerMileStoneList = findViewById(R.id.innerMileStoneList);

        courseDetailsUrl = getString(R.string.apiURL) + "getCourseDetails";

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

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

        Log.e("MediaIndex", mediaIndex + "");
        Log.e("chapter_id", chapter_id + "");
        mileStoneArrayList = (ArrayList<ItemMileStone>) getIntent().getSerializableExtra("mileStonesList");
        chap_id = String.valueOf(getIntent().getIntExtra("chapter_id", 0));

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);

//        exo_next.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                isNext = true;
////                new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////                Log.e("chapter_id", String.valueOf(chapter_id));
////
////            }
////        });
////
////        exo_prev.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                isPrev = true;
////
////                new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////                Log.e("chapter_id", String.valueOf(chapter_id));
////
////            }
////        });




        player = new SimpleExoPlayer.Builder(ExoPlayer3Activity.this).build();
        mileStoneVideoView.setPlayer(player);

        topicName = findViewById(R.id.topicName);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

// Creating a DataSource.Factory
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(
                ExoPlayer3Activity.this,
                new DefaultHttpDataSource.Factory().setUserAgent(Util.getUserAgent(ExoPlayer3Activity.this, "CNB"))
        );

// Looping through your list and adding MediaSources to the player
        for (ItemMileStone itemMileStone : mileStoneArrayList) {
            if (!itemMileStone.getVideoUrl().equals("")) {
                Uri uri = Uri.parse(itemMileStone.getVideoUrl());
                Log.e("lastUrl", itemMileStone.getVideoUrl());

                // Creating a MediaSource for Media3 library
                MediaSource tempMediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));

                player.addMediaSource(tempMediaSource);  // Adding the MediaSource to the player
            }
        }

// Prepare the player (you only need to call this once, after adding all media sources)
        player.prepare();




        SharedPreferences mileStoneDetails = getSharedPreferences("mileStoneId_" + mileStoneArrayList.get(mediaIndex).getId(), MODE_PRIVATE);
        seekTo = mileStoneDetails.getInt("playbackTime", 0);

        Log.e("seekTo", "mileStoneId_" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "hello" + seekTo);

        boolean fromStart = getIntent().getBooleanExtra("fromMile", false);
        if (fromStart) {
            player.seekTo(mediaIndex, 0);
        } else {
            player.seekTo(mediaIndex, seekTo);

        }

//        player.prepare();
        player.play();
        milestone_id = mileStoneArrayList.get(player.getCurrentWindowIndex()).getId();
        chapter_id_from = String.valueOf(mileStoneArrayList.get(player.getCurrentWindowIndex()).getChapterId());
        startTime = player.getCurrentPosition();

        Log.e("start time", String.valueOf(startTime));


//        new MileStoneVideoPlayerActivity.GetSuggestedMileStones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        // Adding event listener to the player
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                // Media item transition occurred
                questionFlag = true;
//                new GetSuggestedMileStones().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
                // Timeline changed event
                Log.e("Timeline", timeline.toString());
                Log.e("Timeline", String.valueOf(reason));
            }

            @Override
            public void onTracksChanged(Tracks tracks) {
                timerforgetlastposition.removeCallbacksAndMessages(null);
                a = 0;
                updatePlayingStVideoComplete();
                timerForDuration.removeCallbacksAndMessages(null);
                duration = 0;
                totalDurationSpent();
                startTime = player.getCurrentPosition();
//                itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                String topicsName = mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle();
                topicName.setText(topicsName);
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                // Playback state changed event
                if (isPlaying && isPause) {
                    innerMileStoneList.setVisibility(View.GONE);
                    doubtSearch.setVisibility(View.GONE);
                    opacityLayout.setVisibility(View.GONE);
                    isPause = false;
                    isPlaying = false;
                } else {
                    innerMileStoneList.setVisibility(View.VISIBLE);
                    doubtSearch.setVisibility(View.VISIBLE);
                    opacityLayout.setVisibility(View.VISIBLE);
//                    itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                    isPause = true;
                    isPlaying = true;
                }
                long videoWatchedTime = player.getCurrentPosition() / 1000;
                if (!suggestedMileStones.isEmpty()) {
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
            public void onPlayerError(@NonNull PlaybackException error) {
                // Player error event
                new AlertDialog.Builder(ExoPlayer3Activity.this)
                        .setTitle("Playback Error")
                        .setMessage("Your device's memory is full. It will not support further videos")
                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                Log.e("PLAYER ERROR EXO_3", error.toString());
            }
        });

        mileStoneVideoView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                // Handle controller visibility changes

                if (visibility == View.VISIBLE) {
                    // Controllers are visible
                    hideControllsHandler.removeCallbacksAndMessages(null);
                    hideControllsHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Hide controllers after 3 seconds
                            if (controllerOn) {
                                hideControllers();
                            }
                        }
                    }, player.getDuration());
                } else {
                    // Controllers are hidden
                    // Do nothing in this case
                }
            }
        });

        mileStoneVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle controller visibility and handle related actions
                toggleControllerVisibility();
            }
        });



        getProfileData();



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
    private void updatePlayingStVideoComplete() {

        final Runnable runnableTimer = new Runnable() {
            @OptIn(markerClass = UnstableApi.class) @Override
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
//                        new MileStoneVideoPlayerActivity.UpdateSearchedQueryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                } else {
                    timerforgetlastposition.postDelayed(this, 1000);
                }

            }
        };
        timerforgetlastposition.postDelayed(runnableTimer, 1000);


    }

    private void totalDurationSpent() {
        // Create a Runnable to handle the timer logic
        final Runnable runnableTimer = new Runnable() {
            @OptIn(markerClass = UnstableApi.class) @Override
            public void run() {
                Log.e("duration", String.valueOf(duration));

                // Check if the player is playing
                if (player.isPlaying()) {
                    // If player is playing, increment the duration counter
                    duration = duration + 1;
                }

                // Post the runnable again after 1 second
                timerForDuration.postDelayed(this, 1000);
            }
        };

        // Start the timer by posting the initial runnable
        timerForDuration.postDelayed(runnableTimer, 1000);
    }

    // Method to hide controllers
    @OptIn(markerClass = UnstableApi.class) private void hideControllers() {
        videoController.setVisibility(View.GONE);
        goToCommunityBtn.setVisibility(View.GONE);
        top_constrain.setVisibility(View.GONE);
        sendInvitationBtn.setVisibility(View.GONE);
        playerSettingBtn.setVisibility(View.GONE);
        mileStoneVideoView.hideController();
        innerMileStoneList.setVisibility(View.GONE);
        doubtSearch.setVisibility(View.GONE);
        controllerOn = false;
    }

    // Method to toggle controller visibility and related actions
    @OptIn(markerClass = UnstableApi.class) private void toggleControllerVisibility() {
        if (controllerOn) {
            hideControllers();
        } else {
            videoController.setVisibility(View.VISIBLE);
            goToCommunityBtn.setVisibility(View.GONE);
            top_constrain.setVisibility(View.VISIBLE);
            sendInvitationBtn.setVisibility(View.GONE);
            playerSettingBtn.setVisibility(View.GONE);
            mileStoneVideoView.showController();
            if (isPause) {
                innerMileStoneList.setVisibility(View.VISIBLE);
                doubtSearch.setVisibility(View.VISIBLE);
//                itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                isPause = true;
            }
            controllerOn = true;
        }
    }
    @OptIn(markerClass = UnstableApi.class) @Override
    protected void onStart() {
        super.onStart();
        Log.e("StartOn", "App background se wapas aane ke baad ");

        // Check if the player is playing
        if (player.isPlaying()) {
            // If playing, pause the player
            player.pause();
        } else {
            // If not playing, no action needed
        }
    }

    @OptIn(markerClass = UnstableApi.class) protected void onStop() {
        super.onStop();
        isStopPressed = true;
//        searchedMileStonesList.clear();
//        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (!mileUpdateType.equals("search_mile_click")) {
            new GetChapterIdAndUpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new MileStoneVideoPlayerActivity.UpdateSearchedQueryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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

    @OptIn(markerClass = UnstableApi.class) @Override
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
//                new MileStoneVideoPlayerActivity.UpdateSearchedQueryData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    @OptIn(markerClass = UnstableApi.class) @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onresume: onpause" + isActivityPause);
        Log.e(TAG, "onresume: wapas aane pr" + isActivityPause);

        if (isActivityPause) {
            // If activity was paused, reset counters and resume playback
            a = 0;
//            updatePlayingStVideoComplete();
            duration = 0;
            totalDurationSpent();

            isActivityPause = false;
        }

        Log.e(TAG, "onResume: " + endTime);

        // Check if there's a pending question, then resume playback
        if (questionFlag) {
            player.play();
        }

        // Popup any pending question
//        popupQuestion();

        // Update start time
        startTime = player.getCurrentPosition();

        // Pause the player if it was playing when the activity was paused
        if (player.isPlaying()){
            player.pause();
            innerMileStoneList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String myStr = data.getStringExtra("MyData");
                if (myStr.equals("cancel")) {
                    // Handle cancellation
                } else {
                    // Handle other results
                }
            }
        }
    }

    @OptIn(markerClass = UnstableApi.class) public void onBackPressed() {
        if (mileUpdateType.equals("search_mile_click")) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExoPlayer3Activity.this);
            // ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = ExoPlayer3Activity.this.getLayoutInflater();
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
                    ExoPlayer3Activity.super.onBackPressed();
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
                    ExoPlayer3Activity.super.onBackPressed();
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

    class GetChapterIdAndUpdatePlayingStatus extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

//            dialog.setMessage("Please wait");
//            dialog.show();

            JSONParser jsonParser = new JSONParser(ExoPlayer3Activity.this);
            int versionCode = com.vision_classes.BuildConfig.VERSION_CODE;
            String param = "uid=" + DashboardActivity.uid + "&app_version=" + versionCode + "&student_id=" + studId + "&course_id=" + courseIdM;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(courseDetailsUrl, "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {

            }
            return "";
        }

        @OptIn(markerClass = UnstableApi.class) @Override
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

                                Toast.makeText(ExoPlayer3Activity.this, "You have not subscribed", Toast.LENGTH_SHORT).show();

                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExoPlayer3Activity.this);
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
                            Toast.makeText(ExoPlayer3Activity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ExoPlayer3Activity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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

    class UpdatePlayingStatus extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait");
            dialog.show();
        }

        // student_id=160&course_id=17&milestone_id=320&play_duration=17
        @OptIn(markerClass = UnstableApi.class) @Override
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


            JSONParser jsonParser = new JSONParser(ExoPlayer3Activity.this);

            Log.i("param update", param);
            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL) + "updatePlayingStatus", "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
                return jsonObject.toString();

            }
            return "";
        }

        @OptIn(markerClass = UnstableApi.class) @Override
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

            dialog.dismiss();
        }
    }


}