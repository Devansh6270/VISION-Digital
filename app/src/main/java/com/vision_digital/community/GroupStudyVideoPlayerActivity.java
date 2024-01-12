package com.vision_digital.community;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.vision_digital.activities.PdfRenderActivity;
import com.vision_digital.R;
import com.vision_digital.community.chatModels.AudioModel;
import com.vision_digital.community.chatModels.ImageModel;
import com.vision_digital.community.chatModels.ItemChat;
import com.vision_digital.community.chooseadminModel.ItemChooseModel;
import com.vision_digital.community.groupMicOnOff.ItemStudentMic;
import com.vision_digital.community.groupMileStoneModel.ItemGroupSuggestedMileAdapter;
import com.vision_digital.community.groupMileStoneModel.ItemGroupSuggestedMileItem;
import com.vision_digital.community.invitation.studentModel.ItemStudent;
import com.vision_digital.community.invitation.studentModel.ItemStudentAdapter;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class GroupStudyVideoPlayerActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 200;

    public ArrayList<ItemMileStone> mileStoneArrayList = new ArrayList<>();
    int mediaIndex;
    int seekTo;
    public static int chapter_id;
    int studId;
    public static String courseId = "";
    public static String chat_id = "";
    public static String videoStatusId = "";

    public static String cid = "";

    ImageButton expo_prev;

    //Layout-----------------------------
    PlayerView mileStoneVideoView;
    SimpleExoPlayer player;

    ImageView backBtn;
    TextView topicName;

    RelativeLayout videoController;
    boolean controllerOn = true, isPause = true;


    //fetching milestones-------------
    String getSuggestedMilestones = "";
    RecyclerView innerMileStoneList;
    ArrayList<ItemGroupSuggestedMileItem> suggestedMileStones = new ArrayList<>();
    ArrayList<ItemMileStone> suggestedMileStoneList = new ArrayList<>();
    ItemGroupSuggestedMileAdapter itemSuggestedMileStoneAdapter;
    final Handler hideControllsHandler = new Handler(Looper.getMainLooper());

    SharedPreferences.Editor editor, courseEditor;

    Handler popupQuestionChecker = new Handler(Looper.getMainLooper());
    boolean questionFlag = true;
    String question, option1, option2, option3, option4, answer, questionId;
    long timeLimit, questionTimeLimit;
    List<String> options = new ArrayList<>();
    String answerResult = "";

    //Timmer------------------------
    long min, sec;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ConstraintLayout live_chat_layout;
    private ImageView micBtn, micOffBtn, messageBtn;
    private CardView messageBtnCard;


    //chat Functionality
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private String currentPhotoPath;
    private String fileName = "";
    private Boolean isReply = false;
    private ImageView buttonPlus, chat_send_image, btndoubtSms, chat_send_image_close;
    private EditText msgEdt;
    private TextView btnSend;
    private FirestoreRecyclerAdapter firestoreRecyclerChatAdapter;
    private String userId;
    private String chatImage = "";
    private String timestamp;
    private Boolean isChat = true;
    private String msgCategory = "";
    private Boolean isImage = false;
    public static String community_id;
    public static String communityLogoG = "";
    private String chat_id_group = "";
    private String chatIdForVideo = "";
    public static String communityNameG = "";
    private RecyclerView chat_recyclerview;
    final int MSG_TYPE_LEFT = 0;
    final int MSG_TYPE_RIGHT = 1;
    private LinearLayoutManager linearLayoutManager;
    private Uri selectedImageUri;
    private ImageModel imageModel;
    private String replyOfUserString = "";
    private String replyOfMsgString = "";
    private String replyOfChatIDString = "";

    private LinearLayoutCompat edtTxtReplyLayout;
    private TextView edtTxtReplyUser, edtTxtReply;
    private ImageView chat_reply_cancel_image;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatImage");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private HashMap<String, Object> chatdetails = new HashMap<>();
    private DocumentReference documentReference;
    private boolean showMsgLayout = true;
    private String admin = "";
    String studentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String invitedUserType = "";
    String isAdminLive = "";
    int firestoreDurtion = 0;
    String playPauseStatus = "";
    //profile

    private String userNameString = "";
    public static String profileImageUrlString = "";


    //Voice channel

    // Fill the App ID of your project generated on Agora Console.
    private String appId = "";
    // Fill the channel name.
    private String channelName = "Test";
    // Fill the temp token generated on Agora Console.
    private String token = "";
    private RtcEngine mRtcEngine;

    private String uid;
    private FirebaseUser user;
    private int sid;

    String isAdmin = "false";
    boolean isMicOn = true;

    // player layout Button
    ImageButton exo_prev, exo_rew, exo_ffwd, exo_next, exo_play, exo_pause;
    String nextBtnString = "notClicked", preBtnString = "notClicked", seekBackBtnString = "notClicked", seekForwardBtnString = "notClicked";


    //Attachment after clicking + 'plus' Button

    LinearLayout attachment_file_layout;
    ImageView chat_camera, chat_gallery, chat_doc, chat_mic, chat_invitation, chat_mic_off;
    private boolean isAttachmentLayoutOpen = false;
    boolean isAudioPlaying = false;
    private RecyclerView chooseAdminList;
    ArrayList<ItemStudent> studentArrayListLive = new ArrayList<>();
    ItemStudentAdapter itemStudentAdapter;
    private CardView choose_admin_card;
    private Button choose_admin_btn;
    private ImageView close_choose_admin, live_chat_close;
    private TextView students_number, students_no;
    int studentsNumber;
    int mediaIndexForUser;
    int count = 0;
    boolean isFetchingStudentNoFirstTime = true;

    //For Audio
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private Boolean isAudio = false;
    private AudioModel audioModel;
    private String chatAudio = "";
    String chatMsg = "";
    String finalChat = "";

    // for Admin mic handle
    private CardView mic_card;
    private ImageView close_mic_popup, mic_control;
    private RecyclerView micUserList;
    private FirestoreRecyclerAdapter firestoreMicRecyclerAdapter;
    private FirestoreRecyclerAdapter firestoreAdminRecyclerAdapter;
    int clicked = 100;
    int selected_position = 100;
    private String selectedAdmin = "";
    private String selectedAdminName = "";
    String fromActivity = "";

    private String courseDetailsUrl = "";
    private ArrayList<ItemMileStone> mileStonesArrayLists = new ArrayList<>();
    String suggMileId = "";
    private int videoPos;
    private String chap_id = "";

    //timer for the fetchlive student
    private long timeLimits = 1800;
    final Handler timer = new Handler(Looper.getMainLooper());
    private String timeString = "";

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.e("agora", "Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        // Listen for the onUserOffline callback.
        // This callback occurs when the host leaves the channel or drops offline.
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("agora", "User offline, uid: " + (uid & 0xFFFFFFFFL));
//                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Session Ended", Toast.LENGTH_SHORT).show();
//                    finish();
//                     onRemoteUserLeft();

                }
            });
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group_study_video_player);

        topicName = findViewById(R.id.topicName);
        backBtn = findViewById(R.id.backBtn);

        exo_prev = findViewById(R.id.exo_prev);
        exo_rew = findViewById(R.id.exo_rew);
        exo_ffwd = findViewById(R.id.exo_ffwd);
        exo_next = findViewById(R.id.exo_next);
        exo_pause = findViewById(R.id.exo_pause);
        exo_play = findViewById(R.id.exo_play);

        mic_control = findViewById(R.id.mic_control);
        mic_card = findViewById(R.id.mic_card);
        close_mic_popup = findViewById(R.id.close_mic_popup);
        micUserList = findViewById(R.id.micUserList);

        attachment_file_layout = findViewById(R.id.attachment_file_layout);
        chat_camera = findViewById(R.id.chat_camera_live);
        chat_gallery = findViewById(R.id.chat_gallery_live);
        chat_doc = findViewById(R.id.chat_doc_live);
        chat_mic = findViewById(R.id.chat_mic_live);
        chooseAdminList = findViewById(R.id.chooseAdminList);
        choose_admin_card = findViewById(R.id.choose_admin_card);
        choose_admin_btn = findViewById(R.id.choose_admin_btn);
        close_choose_admin = findViewById(R.id.close_choose_admin);
        live_chat_close = findViewById(R.id.live_chat_close);
        students_number = findViewById(R.id.students_number);
        students_no = findViewById(R.id.students_no);
        buttonPlus = findViewById(R.id.btnPlus);

        videoController = findViewById(R.id.videoController);
        innerMileStoneList = findViewById(R.id.innerMileStoneList);
        innerMileStoneList.setVisibility(View.VISIBLE);
        live_chat_layout = findViewById(R.id.live_chat_layout);
        micBtn = findViewById(R.id.micBtn);
        micOffBtn = findViewById(R.id.micOffBtn);
        messageBtn = findViewById(R.id.messageBtn);
        messageBtnCard = findViewById(R.id.messageBtnCard);
//        membersBtn = findViewById(R.id.membersBtn);
//        membersBtn.setVisibility(View.GONE);
        mileStoneVideoView = findViewById(R.id.mileStoneVideoView);

        mileStoneArrayList = (ArrayList<ItemMileStone>) getIntent().getSerializableExtra("mileStonesList");
        mediaIndex = getIntent().getIntExtra("videoPosition", 0);
        chapter_id = getIntent().getIntExtra("chapter_id", 0);
        courseId = getIntent().getStringExtra("id");
        chat_id = getIntent().getStringExtra("chatId");
        videoStatusId = getIntent().getStringExtra("videoStatusId");
        communityLogoG = getIntent().getStringExtra("community_logo");
        communityNameG = getIntent().getStringExtra("community_name");
        isAdmin = getIntent().getStringExtra("isAdmin");
        profileImageUrlString = getIntent().getStringExtra("userImage");
        fromActivity = getIntent().getStringExtra("fromActivity");

        Log.e("chatId", chat_id);
        Log.e("videoStatusId", videoStatusId);
        Log.e("courseIdMilestone", courseId);
        Log.e("MediaIndex", mediaIndex + "");
        Log.e("chapter_id", chapter_id + "");

        cid = courseId;
        courseDetailsUrl = getString(R.string.apiURL) + "getCourseDetails";

        LinearLayoutManager layoutManagerAdmin = new LinearLayoutManager(GroupStudyVideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
        chooseAdminList.setLayoutManager(layoutManagerAdmin);

        LinearLayoutManager layoutManagerStudentMic = new LinearLayoutManager(GroupStudyVideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
        micUserList.setLayoutManager(layoutManagerStudentMic);

        choose_admin_card.setVisibility(View.GONE);
        mic_card.setVisibility(View.GONE);

        choose_admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateAdmin();

            }
        });


        live_chat_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchingCurrentLive();
                live_chat_layout.setVisibility(View.GONE);
                mic_card.setVisibility(View.GONE);
            }
        });

        close_choose_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_admin_card.setVisibility(View.GONE);
            }
        });


        mic_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMicStudentList();
                Log.e(TAG, "onClick: clickedMic");
                mic_card.setVisibility(View.VISIBLE);

            }
        });

        close_mic_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mic_card.setVisibility(View.GONE);
            }
        });


        isAdmin = getIntent().getStringExtra("isAdmin");

        db = FirebaseFirestore.getInstance();
        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (e != null) {

                        }

                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                            String firestoreDurtionString = documentChange.getDocument().getData().get("videoPlayingStatus").toString();
                            firestoreDurtion = Integer.parseInt(firestoreDurtionString);

                            Log.e("firestoreDurtion", String.valueOf(firestoreDurtion));


                        }
                    }
                });


        live_chat_layout.setVisibility(View.GONE);
        messageBtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayingStatusInInterval();

                fetchingCurrentLive();
                if (showMsgLayout) {
                    live_chat_layout.setVisibility(View.VISIBLE);
                    messageBtnCard.setVisibility(View.GONE);

                    showMsgLayout = false;
                } else {
                    live_chat_layout.setVisibility(View.GONE);
                    messageBtnCard.setVisibility(View.VISIBLE);
                    showMsgLayout = true;
                }
            }
        });

        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                micOffBtn.setVisibility(View.VISIBLE);
                micBtn.setVisibility(View.GONE);
                mRtcEngine.muteLocalAudioStream(true);
                mRtcEngine.muteRemoteAudioStream(studId, true);
                String offOn = "off";
                String trueFalse = "true";

                updateMicStatus(studentUId, offOn, trueFalse);

            }
        });

        micOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micBtn.setVisibility(View.VISIBLE);
                micOffBtn.setVisibility(View.GONE);
                mRtcEngine.muteLocalAudioStream(false);
                mRtcEngine.muteRemoteAudioStream(studId, false);
                String offOn = "on";
                String trueFalse = "false";

                updateMicStatus(studentUId, offOn, trueFalse);


            }
        });


        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedStudent").addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        List<DocumentSnapshot> studentList = documentSnapshots.getDocuments();

                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {

                                if (studentUId.equals(documentChange.getDocument().getData().get("invStudId"))) {
                                    if (documentChange.getDocument().getData().get("micStatus").toString().equals("on")) {

                                        micBtn.setVisibility(View.VISIBLE);
                                        micOffBtn.setVisibility(View.GONE);

                                    } else {
                                        micBtn.setVisibility(View.GONE);
                                        micOffBtn.setVisibility(View.VISIBLE);

                                    }
                                }


                            }

                        } else {


                        }

                    }
                });


//        fetchStudentJoinedList();


        // for fetching live student
        fetchingCurrentLive();


        Handler statusUpdater = new Handler(Looper.getMainLooper());
        statusUpdater.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do your work

                db = FirebaseFirestore.getInstance();
                db.collection("Communities").document(courseId).collection("Chats")
                        .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                Log.e("updatevideofirestore", String.valueOf(player.getCurrentPosition()));

            }
        }, 10000);


        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);


        mileStoneVideoView.hideController();
        Log.e("isAdminEnter", String.valueOf(isAdmin));

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                chat_send_image.setVisibility(View.GONE);
//                chat_send_image_close.setVisibility(View.GONE);

                if (isAttachmentLayoutOpen) {
                    attachment_file_layout.setVisibility(View.GONE);
                    isAttachmentLayoutOpen = false;

                    chat_send_image.setVisibility(View.GONE);
                    chat_send_image_close.setVisibility(View.GONE);
                } else {
                    attachment_file_layout.setVisibility(View.VISIBLE);
                    isAttachmentLayoutOpen = true;
                    chat_send_image.setVisibility(View.GONE);
                    chat_send_image_close.setVisibility(View.GONE);
                }
            }
        });


        if (isAdmin.equals("true")) {

            //player button update

            mileStoneVideoView.showController();


            chat_mic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("mic", "clicked");
                    if (isMicrophonePresent()) {
                        getMicrophonePermission();

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        audioMessageRecord();
                    }
                }
            });
            chat_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("gallery", "clicked");
                    if (checkPermission()) {
                        isImage = true;
                        Log.e("isImage", String.valueOf(isImage));
                        isAttachmentLayoutOpen = false;
                        attachment_file_layout.setVisibility(View.GONE);
                        player.prepare();
                        player.pause();
                        if (isAdmin.equals("true")) {
                            db.collection("Communities").document(courseId).collection("Chats")
                                    .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "live");

                        } else {

                        }
                        ImagePicker.Companion.with(GroupStudyVideoPlayerActivity.this)
                                .galleryOnly()
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start();

                    } else {
                        requestPermission();
                    }
                }
            });

            chat_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("camera", "clicked");
                    if (checkPermission()) {
                        isImage = true;
                        Log.e("isImage", String.valueOf(isImage));

                        isAttachmentLayoutOpen = false;
                        attachment_file_layout.setVisibility(View.GONE);
                        player.prepare();
                        player.pause();
                        if (isAdmin.equals("true")) {
                            db.collection("Communities").document(courseId).collection("Chats")
                                    .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "live");

                        } else {

                        }
                        ImagePicker.Companion.with(GroupStudyVideoPlayerActivity.this)
                                .cameraOnly()
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start();
                    } else {
                        requestPermission();
                    }
                }
            });

            // After Clicking on this we will be
            // redirected to choose pdf
            chat_doc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAttachmentLayoutOpen = false;
                    Log.e("doc", "clicked");
                    player.prepare();
                    player.pause();
                    attachment_file_layout.setVisibility(View.GONE);
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    // We will be redirected to choose pdf
                    galleryIntent.setType("application/pdf");
                    startActivityForResult(galleryIntent, 3);

                }
            });
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();


                }
            });


            exo_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("nextBtn", "clicked");

                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                    mediaIndex = mediaIndex + 1;

                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPosition", mediaIndex);

                    player.next();

                }
            });


            exo_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mediaIndex = mediaIndex + 1;

                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPosition", mediaIndex);

                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("previousBtn", "clicked");


                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                    player.previous();


                }
            });

            exo_rew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("seekBwrd", "clicked");


                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                    player.seekTo(player.getCurrentPosition() - 10);

                }
            });

            exo_ffwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("seekFrwd", "clicked");

                    db = FirebaseFirestore.getInstance();
                    db.collection("Communities").document(courseId).collection("Chats")
                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                    player.seekTo(player.getCurrentPosition() + 10);


                }
            });


            db = FirebaseFirestore.getInstance();
            db.collection("Communities").document(courseId).collection("Chats")
                    .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "live");


            db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                    Log.e("List", studentList.toString());


                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            String sids = documentChange.getDocument().getData().get("invStudId").toString();


                            if (studentUId.equals(sids)) {


                                String documentId = documentChange.getDocument().getId();
                                db.collection("Communities").document(courseId).collection("Chats")
                                        .document(chat_id).collection("invitedStudent").document(documentId).update("status", "live");

                                break;

                            } else {

                                Log.e("Not live status", "Not live status");

                            }
                        }


                    } else {


                    }


                }
            });


            mileStoneVideoView.showController();
            messageBtnCard.setVisibility(View.VISIBLE);

            mileStoneVideoView.setEnabled(true);
            Log.e("admin", "ha ji admin hi hai");

            mileStoneVideoView = findViewById(R.id.mileStoneVideoView);

            player = new SimpleExoPlayer.Builder(GroupStudyVideoPlayerActivity.this).build();
            mileStoneVideoView.setPlayer(player);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(GroupStudyVideoPlayerActivity.this, Util.getUserAgent(GroupStudyVideoPlayerActivity.this, "CNB"), new DefaultBandwidthMeter());
            for (ItemMileStone itemMileStone : mileStoneArrayList) {
                if (!itemMileStone.getVideoUrl().equals("")) {
                    Uri uri = Uri.parse(itemMileStone.getVideoUrl());
                    Log.e("lastUrl", itemMileStone.getVideoUrl());
                    MediaSource tempmediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                    player.addMediaSource(tempmediaSource);
                }
            }
            player.seekTo(mediaIndex, 1);
            player.prepare();
            player.play();


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
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                    new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    Log.e("topic name", mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle());
                    String topicsName = mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle();

                    topicName.setText(topicsName);


                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying) {
                        innerMileStoneList.setVisibility(View.GONE);

                        db = FirebaseFirestore.getInstance();

                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoStatus", "play");


                        Log.e("updatevideofirestore", String.valueOf(player.getCurrentPosition()));

                        isPause = false;
                        isPlaying = false;


                    } else {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                        db = FirebaseFirestore.getInstance();

                        db = FirebaseFirestore.getInstance();
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoStatus", "pause");



                        Log.e("updatevideofiresefalse", String.valueOf(player.getCurrentPosition()));
                        itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                        isPause = true;
                        isPlaying = true;
                    }
                    //Do your work
                    long videoWatchedTime = player.getCurrentPosition() / 1000;
                    Log.e("time", "" + videoWatchedTime + suggestedMileStones.isEmpty());
                    if (!suggestedMileStones.isEmpty()) {
                        Log.e("ploting", "fff");

                        for (ItemGroupSuggestedMileItem itemSuggestedMileStones : suggestedMileStones) {
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
                                        innerMileStoneList.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    } else {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this)
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

//          updateListOfSuggestedMileStones();
            LinearLayoutManager layoutManager = new LinearLayoutManager(GroupStudyVideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
            innerMileStoneList.setLayoutManager(layoutManager);
            itemSuggestedMileStoneAdapter = new ItemGroupSuggestedMileAdapter(suggestedMileStoneList);
            innerMileStoneList.setAdapter(itemSuggestedMileStoneAdapter);


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
                                mileStoneVideoView.hideController();
                                messageBtnCard.setVisibility(View.GONE);
                                innerMileStoneList.setVisibility(View.VISIBLE);
                                controllerOn = false;

                            }

                        }
                    }, 4000);
                    fetchingCurrentLive();

                    //Do your work
                    long videoWatchedTime = player.getCurrentPosition() / 1000;
                    Log.e("time", "" + videoWatchedTime + suggestedMileStones.isEmpty());
                    if (!suggestedMileStones.isEmpty()) {
                        Log.e("ploting", "fff");
                        suggestedMileStoneList.clear();

                        for (ItemGroupSuggestedMileItem itemSuggestedMileStones : suggestedMileStones) {
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
                                        innerMileStoneList.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    } else {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                    }


                    if (controllerOn) {
                        videoController.setVisibility(View.GONE);
                        micBtn.setVisibility(View.GONE);
                        messageBtn.setVisibility(View.GONE);
                        micOffBtn.setVisibility(View.GONE);
//                        membersBtn.setVisibility(View.GONE);

                        mileStoneVideoView.hideController();
                        messageBtnCard.setVisibility(View.GONE);

                        innerMileStoneList.setVisibility(View.VISIBLE);
                        controllerOn = false;
                    } else {
                        videoController.setVisibility(View.VISIBLE);
                        micBtn.setVisibility(View.VISIBLE);
                        micOffBtn.setVisibility(View.VISIBLE);
                        messageBtn.setVisibility(View.VISIBLE);
//                        membersBtn.setVisibility(View.VISIBLE);
                        messageBtnCard.setVisibility(View.VISIBLE);

                        mileStoneVideoView.showController();
                        if (isPause) {
                            innerMileStoneList.setVisibility(View.VISIBLE);

                            itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                            isPause = true;
                        }
                        controllerOn = true;
                    }
                }
            });
            liveGroupChatMethod();
        }


        if (isAdmin.equals("false")) {


            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                chat_send_image.setVisibility(View.GONE);
//                chat_send_image_close.setVisibility(View.GONE);

                    if (isAttachmentLayoutOpen) {
                        attachment_file_layout.setVisibility(View.GONE);
                        isAttachmentLayoutOpen = false;

                        chat_send_image.setVisibility(View.GONE);
                        chat_send_image_close.setVisibility(View.GONE);
                    } else {
                        attachment_file_layout.setVisibility(View.VISIBLE);
                        isAttachmentLayoutOpen = true;
                        chat_send_image.setVisibility(View.GONE);
                        chat_send_image_close.setVisibility(View.GONE);
                    }
                }
            });

            chat_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("gallery", "clicked");
                    if (checkPermission()) {
                        isImage = true;
                        Log.e("isImage", String.valueOf(isImage));

                        isAttachmentLayoutOpen = false;
                        attachment_file_layout.setVisibility(View.GONE);
                        ImagePicker.Companion.with(GroupStudyVideoPlayerActivity.this)
                                .galleryOnly()
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start();

                    } else {
                        requestPermission();
                    }
                }
            });

            chat_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("camera", "clicked");
                    if (checkPermission()) {
                        isImage = true;
                        Log.e("isImage", String.valueOf(isImage));

                        isAttachmentLayoutOpen = false;
                        attachment_file_layout.setVisibility(View.GONE);
                        ImagePicker.Companion.with(GroupStudyVideoPlayerActivity.this)
                                .cameraOnly()
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start();
                    } else {
                        requestPermission();
                    }
                }
            });

            // After Clicking on this we will be
            // redirected to choose pdf
            chat_doc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAttachmentLayoutOpen = false;
                    Log.e("doc", "clicked");
                    attachment_file_layout.setVisibility(View.GONE);
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    // We will be redirected to choose pdf
                    galleryIntent.setType("application/pdf");
                    startActivityForResult(galleryIntent, 3);

                }
            });

            chat_mic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("mic", "clicked");
                    if (isMicrophonePresent()) {
                        getMicrophonePermission();

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        audioMessageRecord();
                    }
                }
            });


            db = FirebaseFirestore.getInstance();
            db.collection("Communities").document(courseId).collection("Chats")
                    .document(chat_id).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (e != null) {

                            }

                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                isAdminLive = documentChange.getDocument().getData().get("liveUnliveStatus").toString();

                                if (isAdminLive.equals("unlive")) {
                                    Intent communityIntent = new Intent(GroupStudyVideoPlayerActivity.this, CommunityChatPageActivity.class);


                                    communityIntent.putExtra("community_id", courseId);
                                    communityIntent.putExtra("community_name", communityNameG);
                                    communityIntent.putExtra("community_logo", communityLogoG);
                                    communityIntent.putExtra("activity", "groupPlayer");
                                    startActivity(communityIntent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                Log.e("isAdminLive", isAdminLive);

                            }
                        }
                    });

            db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                    Log.e("List", studentList.toString());


                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            String sids = documentChange.getDocument().getData().get("invStudId").toString();


                            if (studentUId.equals(sids)) {


                                String documentId = documentChange.getDocument().getId();
                                db.collection("Communities").document(courseId).collection("Chats")
                                        .document(chat_id).collection("invitedStudent").document(documentId).update("status", "live");

                                break;

                            } else {

                                Log.e("Not live status", "Not live status");

                            }
                        }


                    } else {


                    }


                }
            });


            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Now admin is controlling this Button!..", Toast.LENGTH_SHORT).show();

                }
            });


            exo_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Only admin can control the Player", Toast.LENGTH_SHORT).show();

                }
            });
            exo_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Only admin can control the Player", Toast.LENGTH_SHORT).show();

                }
            });

            exo_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Only admin can control the Player", Toast.LENGTH_SHORT).show();

                }
            });
            exo_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Only admin can control the Player", Toast.LENGTH_SHORT).show();


                }
            });
            exo_rew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Only admin can control the Player", Toast.LENGTH_SHORT).show();

                }
            });

            exo_ffwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


            mileStoneVideoView.hideController();
            db = FirebaseFirestore.getInstance();
            db.collection("Communities").document(courseId).collection("Chats")
                    .document(chat_id).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (e != null) {

                            }

                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                mediaIndexForUser = Integer.parseInt(documentChange.getDocument().getData().get("videoPosition").toString());

                                String nextBtn = documentChange.getDocument().getData().get("nextBtn").toString();
                                nextBtnString = nextBtn;


                                String prevBtn = documentChange.getDocument().getData().get("previousBtn").toString();
                                preBtnString = prevBtn;


                                String seekBack = documentChange.getDocument().getData().get("seekBwrd").toString();
                                seekBackBtnString = seekBack;


                                String seekForward = documentChange.getDocument().getData().get("seekFrwd").toString();
                                seekForwardBtnString = seekForward;

                                String suggMile = documentChange.getDocument().getData().get("suggestedMile").toString();

                                suggMileId = documentChange.getDocument().getData().get("suggestedMileId").toString();


                                String backBtnStatus = documentChange.getDocument().getData().get("backButton").toString();


                                if (backBtnStatus.equals("clicked")) {
                                    Log.e(TAG, "onSuccess: Check");


                                    db.collection("Communities").document(courseId).collection("Chats")
                                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("backButton", "unClicked").
                                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    fromActivity = "groupPlayer";
                                                    Log.e(TAG, "onSuccess: working");
                                                    onBackPressed();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Failureupdate" + e.getMessage());
                                                }
                                            });

                                }

                                Log.e("firestoreDurtion", String.valueOf(firestoreDurtion));



                                if (nextBtnString.equals("clicked")) {
                                    player.prepare();
//                                    player.next();
//                                    player.play();
                                    player.seekTo(mediaIndexForUser, firestoreDurtion);
                                    player.play();
                                    db.collection("Communities").document(courseId).collection("Chats")
                                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("nextBtn", "unClicked");
                                    Log.e(TAG, "onSuccess: working" + videoStatusId);


                                }
                                if (preBtnString.equals("clicked")) {
                                    player.prepare();
//                                    player.previous();
//                                    player.play();
                                    player.seekTo(mediaIndexForUser, firestoreDurtion);
                                    player.play();
                                    db.collection("Communities").document(courseId).collection("Chats")
                                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("previousBtn", "unClicked");
                                    Log.e(TAG, "onSuccess: working" + videoStatusId);


                                }
                                if (seekBackBtnString.equals("clicked")) {
                                    player.seekTo(player.getCurrentPosition() - 10);
                                    db.collection("Communities").document(courseId).collection("Chats")
                                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("seekBwrd", "unClicked");
                                    Log.e(TAG, "onSuccess: working" + videoStatusId);


                                }
                                if (seekForwardBtnString.equals("clicked")) {
                                    player.seekTo(player.getCurrentPosition() + 10);
                                    db.collection("Communities").document(courseId).collection("Chats")
                                            .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("seekFrwd", "unClicked");

                                    Log.e(TAG, "onSuccess: working" + videoStatusId);

                                }

                            }
                        }
                    });

            db = FirebaseFirestore.getInstance();
            db.collection("Communities").document(courseId).collection("Chats")
                    .document(chat_id).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (e != null) {

                            }

                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                String firestoreDurtionString = documentChange.getDocument().getData().get("videoStatus").toString();
                                playPauseStatus = firestoreDurtionString;

                                if (playPauseStatus.equals("pause")) {
                                    player.seekTo(mediaIndexForUser, firestoreDurtion);
                                    player.pause();


                                } else {
                                    player.seekTo(mediaIndexForUser, firestoreDurtion);
                                    player.play();

                                }
                                Log.e("playpausemember", String.valueOf(playPauseStatus));


                            }
                        }
                    });


//            mileStoneVideoView.showController();
//            mileStoneVideoView.setEnabled(false);
            Log.e("admin", "notAdmin");


            mileStoneVideoView = findViewById(R.id.mileStoneVideoView);
            mileStoneVideoView.showController();
            messageBtnCard.setVisibility(View.VISIBLE);

            player = new SimpleExoPlayer.Builder(GroupStudyVideoPlayerActivity.this).build();
            mileStoneVideoView.setPlayer(player);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(GroupStudyVideoPlayerActivity.this, Util.getUserAgent(GroupStudyVideoPlayerActivity.this, "CNB"), new DefaultBandwidthMeter());
            for (ItemMileStone itemMileStone : mileStoneArrayList) {
                if (!itemMileStone.getVideoUrl().equals("")) {
                    Uri uri = Uri.parse(itemMileStone.getVideoUrl());
                    Log.e("lastUrl", itemMileStone.getVideoUrl());
                    MediaSource tempmediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                    player.addMediaSource(tempmediaSource);
//                    player.setPlayWhenReady(true);
//                    player.getPlayWhenReady();
////                    player.prepare(tempmediaSource,false,false);
//                    player.play();

                }
            }

            player.seekTo(mediaIndex, 2);
            player.prepare();

            player.play();
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
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                    new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    Log.e("topic name", mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle());
                    String topicsName = mileStoneArrayList.get(player.getCurrentWindowIndex()).getTitle();

                    topicName.setText(topicsName);


                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying && isPause) {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                        db = FirebaseFirestore.getInstance();
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoStatus", "play");


                        db = FirebaseFirestore.getInstance();
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                        Log.e("updatevideofirestore", String.valueOf(player.getCurrentPosition()));

                        isPause = false;
                        isPlaying = false;

                    } else {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                        db = FirebaseFirestore.getInstance();
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoStatus", "pause");


                        db = FirebaseFirestore.getInstance();
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("videoPlayingStatus", player.getCurrentPosition());

                        Log.e("updatevideofirestore", String.valueOf(player.getCurrentPosition()));
                        itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                        isPause = true;
                        isPlaying = true;
                    }
                    //Do your work
                    long videoWatchedTime = player.getCurrentPosition() / 1000;
                    Log.e("time", "" + videoWatchedTime + suggestedMileStones.isEmpty());
                    if (!suggestedMileStones.isEmpty()) {
                        Log.e("ploting", "fff");

                        for (ItemGroupSuggestedMileItem itemSuggestedMileStones : suggestedMileStones) {
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
                                        innerMileStoneList.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    } else {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this)
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

//          updateListOfSuggestedMileStones();
            LinearLayoutManager layoutManager = new LinearLayoutManager(GroupStudyVideoPlayerActivity.this, LinearLayoutManager.VERTICAL, false);
            innerMileStoneList.setLayoutManager(layoutManager);
            itemSuggestedMileStoneAdapter = new ItemGroupSuggestedMileAdapter(suggestedMileStoneList);
            innerMileStoneList.setAdapter(itemSuggestedMileStoneAdapter);


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
                                mileStoneVideoView.hideController();
                                messageBtnCard.setVisibility(View.GONE);

                                innerMileStoneList.setVisibility(View.VISIBLE);
                                controllerOn = false;

                            }

                        }
                    }, 3000);

                    //Do your work
                    fetchingCurrentLive();
                    long videoWatchedTime = player.getCurrentPosition() / 1000;
                    Log.e("time", "" + videoWatchedTime + suggestedMileStones.isEmpty());
                    if (!suggestedMileStones.isEmpty()) {
                        Log.e("ploting", "fff");
                        suggestedMileStoneList.clear();

                        for (ItemGroupSuggestedMileItem itemSuggestedMileStones : suggestedMileStones) {
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
                                        innerMileStoneList.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    } else {
                        innerMileStoneList.setVisibility(View.VISIBLE);
                    }


                    if (controllerOn) {
                        videoController.setVisibility(View.GONE);
                        micBtn.setVisibility(View.GONE);
                        messageBtn.setVisibility(View.GONE);
                        micOffBtn.setVisibility(View.GONE);
//                        membersBtn.setVisibility(View.GONE);
                        mileStoneVideoView.hideController();
                        messageBtnCard.setVisibility(View.GONE);

                        innerMileStoneList.setVisibility(View.VISIBLE);
                        controllerOn = false;
                    } else {
                        videoController.setVisibility(View.GONE);
                        micBtn.setVisibility(View.VISIBLE);
                        micOffBtn.setVisibility(View.VISIBLE);
                        messageBtn.setVisibility(View.VISIBLE);
                        messageBtnCard.setVisibility(View.VISIBLE);

//                        membersBtn.setVisibility(View.VISIBLE);

                        mileStoneVideoView.showController();
                        if (isPause) {
                            innerMileStoneList.setVisibility(View.VISIBLE);

                            itemSuggestedMileStoneAdapter.notifyDataSetChanged();
                            isPause = true;
                        }
                        controllerOn = true;
                    }
                }
            });
            liveGroupChatMethod();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)) {
                initializeAndJoinChannel();
            }
        }


        // for mic on off
//        fetchMicStatus();

        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedStudent").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        List<DocumentSnapshot> studentList = documentSnapshots.getDocuments();
                        if (e != null) {

                        }

                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                String sids = documentChange.getDocument().getData().get("invStudId").toString();


                                if (studentUId.equals(sids)) {
                                    String micStatusString = documentChange.getDocument().getData().get("micStatus").toString();
                                    Log.e("micStatusString", "micStatusString");

                                    if (micStatusString.equals("on")) {
                                        mRtcEngine.muteRemoteAudioStream(studId, false);

                                        mRtcEngine.muteLocalAudioStream(false);
                                        mRtcEngine.enableLocalAudio(true);
                                        Log.e("micStatus", "true");


                                    }
                                    if (micStatusString.equals("off")) {
                                        mRtcEngine.muteRemoteAudioStream(studId, true);
                                        mRtcEngine.muteLocalAudioStream(true);
//                                        mRtcEngine.enableLocalAudio(false);
                                        Log.e("micStatus", "true");


                                    }

                                    break;

                                } else {

                                    Log.e("No mic Status", "No mic status");

                                }
                            }


                        } else {

                        }
                    }
                });

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

    private void fetchMicStatus() {
        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedStudent").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        List<DocumentSnapshot> studentList = documentSnapshots.getDocuments();
                        if (e != null) {

                        }

                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                String sids = documentChange.getDocument().getData().get("invStudId").toString();


                                if (studentUId.equals(sids)) {
                                    String micStatusString = documentChange.getDocument().getData().get("micStatus").toString();
                                    Log.e("micStatusString", "micStatusString");

                                    if (micStatusString.equals("on")) {
                                        mRtcEngine.muteRemoteAudioStream(studId, false);

                                        mRtcEngine.muteLocalAudioStream(false);
                                        mRtcEngine.enableLocalAudio(true);


                                    }
                                    if (micStatusString.equals("off")) {
                                        mRtcEngine.muteRemoteAudioStream(studId, true);
                                        mRtcEngine.muteLocalAudioStream(true);
                                        mRtcEngine.enableLocalAudio(false);


                                    }

                                    break;

                                } else {

                                    Log.e("No mic Status", "No mic status");

                                }
                            }


                        } else {

                        }
                    }
                });


    }

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    // Java
    private void initializeAndJoinChannel() {
//        channelName = chat_id;
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        Log.e("uid", uid);
        try {


            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            mRtcEngine.setChannelProfile(Constants.CLIENT_ROLE_BROADCASTER);


//            if (user.equals(admin)) {
            mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
//            } else {
//                mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
//            }

        } catch (Exception e) {
            Log.e("exption", "exception");
            throw new RuntimeException(e.toString());
        }

        mRtcEngine.joinChannel("", channelName, "1234", studId);
        Toast.makeText(this, "Channel joined", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, mRtcEngine.getCallId(), Toast.LENGTH_SHORT).show();


        //listview will be hidden for normal user.
//        mRtcEngine.muteRemoteAudioStream(456, false);

        mRtcEngine.enableAudio();

    }


    private void updateAdmin() {
        choose_admin_card.setVisibility(View.GONE);

        Log.e("selectedAdmin", selectedAdmin);
        db = FirebaseFirestore.getInstance();
        db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());


                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        String invitedStuId = documentChange.getDocument().getData().get("invStudId").toString();
                        invitedUserType = documentChange.getDocument().getData().get("userType").toString();


                        if (invitedStuId.equals(studentUId)) {
                            String idAdmin = documentChange.getDocument().getId();
                            Log.e("idAdmin", String.valueOf(idAdmin));

                            db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").document(idAdmin).update("userType", "member");
                            db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").document(idAdmin).update("status", "live");


                        }

                        if (invitedStuId.equals(selectedAdmin)) {

                            String id = documentChange.getDocument().getId();

                            Log.e("id", String.valueOf(id));

                            db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").document(id).update("userType", "admin");
                            db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").document(id).update("status", "live");


                        } else {

                        }

                        Toast.makeText(GroupStudyVideoPlayerActivity.this, "Now new admin is " + selectedAdminName, Toast.LENGTH_SHORT).show();

//                        Intent communityIntent = new Intent(GroupStudyVideoPlayerActivity.this, CommunityChatPageActivity.class);
//
//
//                        communityIntent.putExtra("community_id", courseId);
//                        communityIntent.putExtra("community_name", communityName);
//                        communityIntent.putExtra("community_logo", communityLogo);
//                        communityIntent.putExtra("activity", "community_list");
//
//                        startActivity(communityIntent);


                    }
                }
            }
        });

    }


    private void updatePlayingStatusInInterval() {

        Handler statusUpdater = new Handler(Looper.getMainLooper());
        statusUpdater.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do your work
                if ((player.getDuration() - player.getCurrentPosition()) / 1000 < 11) {
                    new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }, 10000);


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

            JSONParser jsonParser = new JSONParser(GroupStudyVideoPlayerActivity.this);
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "";

            param = "milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId();
            player.setThrowsWhenUsingWrongThread(false);
            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getSuggestedMilestones, "POST", param);
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
                            //Running Fine----
                            JSONArray milestonesArray = dataObj.getJSONArray("milestones");
                            suggestedMileStones.clear();
                            for (int i = 0; i < milestonesArray.length(); i++) {
                                JSONObject mileStone = milestonesArray.getJSONObject(i);
                                ItemGroupSuggestedMileItem itemSuggestedMileStones = new ItemGroupSuggestedMileItem();
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
                                    itemMileStone.setActivityType("adda");
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
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = GroupStudyVideoPlayerActivity.this.getLayoutInflater();
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
                            Toast.makeText(GroupStudyVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(GroupStudyVideoPlayerActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
        }
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
            String param = "student_id=" + studId + "&course_id=" + courseId + "&milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "&play_duration=" + player.getCurrentPosition() / 1000 + "&chapter_id=" + chapter_id;
            Log.e("playingParam", param);

            JSONParser jsonParser = new JSONParser(GroupStudyVideoPlayerActivity.this);

            Log.i("param", param);
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

    class SaveQuestionResponse extends AsyncTask<String, Void, String> {
        // private ProgressDialog dialog = new ProgressDialog(NewResultActivity.this);

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(GroupStudyVideoPlayerActivity.this);
            String param = "student_id=" + studId + "&course_id=" + courseId + "&milestone_id=" + mileStoneArrayList.get(player.getCurrentWindowIndex()).getId() + "&question_id=" + questionId + "&answered_option=" + answer + "&answer=" + answerResult;

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

    private void popupQuestion() {

        popupQuestionChecker.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do your work
                if ((player.getDuration() - player.getCurrentPosition()) / 1000 == 0 && questionFlag == true) {

                    final Dialog dialog = new Dialog(GroupStudyVideoPlayerActivity.this, android.R.style.Theme_Light);
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
                                final Dialog timeOverDialog = new Dialog(GroupStudyVideoPlayerActivity.this);
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

        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new SaveQuestionResponse().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void popupResult(boolean result, final Dialog questionDialog) {
        final Dialog dialog = new Dialog(GroupStudyVideoPlayerActivity.this);
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

    private void liveGroupChatMethod() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnSend = findViewById(R.id.btnSendSms);
        edtTxtReplyLayout = findViewById(R.id.edtTxtReplyLayout);
        edtTxtReplyUser = findViewById(R.id.edtTxtReplyUser);
        edtTxtReply = findViewById(R.id.edtTxtReply);
        chat_reply_cancel_image = findViewById(R.id.chat_reply_cancel_image);

        chat_send_image = findViewById(R.id.chat_send_image);
        chat_send_image.setVisibility(View.GONE);
        chat_send_image_close = findViewById(R.id.chat_send_image_close);
        chat_send_image_close.setVisibility(View.GONE);
        msgEdt = findViewById(R.id.edtTxtSms);
        chat_recyclerview = findViewById(R.id.chat_recyclerview);
        attachment_file_layout.setVisibility(View.GONE);

        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
//        chat_id_group = userId + timestamp;


        msgEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                attachment_file_layout.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                chatMsg = msgEdt.getText().toString().trim();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSend.setVisibility(View.VISIBLE);
                chatMsg = msgEdt.getText().toString().trim();
            }
        });

        Log.e("chat_id_group", chat_id_group);

        edtTxtReplyLayout.setVisibility(View.GONE);
        chat_reply_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTxtReplyLayout.setVisibility(View.GONE);
            }
        });


//        chat_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkAndRequestPermissions()) {
//                    isImage = true;
//                    Log.e("isImage", String.valueOf(isImage));
//                    isAttachmentLayoutOpen = false;
//                    attachment_file_layout.setVisibility(View.GONE);
//                    ImagePicker.Companion.with(GroupStudyVideoPlayerActivity.this)
//                            .cameraOnly()
//                            .crop()
//                            .compress(1024)
//                            .maxResultSize(1080, 1080)
//                            .start();
//                }
//            }
//        });
//
//
//        // After Clicking on this we will be
//        // redirected to choose pdf
//        chat_doc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isAttachmentLayoutOpen = false;
//                attachment_file_layout.setVisibility(View.GONE);
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//                // We will be redirected to choose pdf
//                galleryIntent.setType("application/pdf");
//                startActivityForResult(galleryIntent, 3);
//
//            }
//        });
//
//
//        chat_mic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isMicrophonePresent()) {
//                    getMicrophonePermission();
//
//                }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    audioMessageRecord();
//                }
//            }
//        });


        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        chat_recyclerview.setLayoutManager(linearLayoutManager);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("isImage", String.valueOf(isImage));
                if (isImage) {
                    msgCategory = "image";
                    sendImage();
                    edtTxtReplyLayout.setVisibility(View.GONE);
                    live_chat_layout.setVisibility(View.VISIBLE);
                    chat_send_image.setVisibility(View.GONE);
                    chat_send_image_close.setVisibility(View.GONE);
                } else if (isAudio) {
                    msgCategory = "audio";
                    sendAudio();

                } else {
                    String msgUtils = msgEdt.getText().toString();
                    if (!msgUtils.equals("")) {
                        msgCategory = "txt";
                        sendMessage();
                    } else {

                    }
                }
            }
        });

        chat_send_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_send_image_close.setVisibility(View.GONE);
                chat_send_image.setVisibility(View.GONE);
            }
        });

        getChatList();

    }

    private void sendAudio() {
        Log.e("sendAudio", "enter");

        Uri audioURI = null;
        File audioFile;
        audioFile = getRecordedAudioFile();
        Log.e("audioFile", String.valueOf(audioFile));

        // Continue only if the File was successfully created
        if (audioFile != null) {
            audioURI = FileProvider.getUriForFile(getApplicationContext(),
                    "com.chalksnboard.android.fileprovider",
                    audioFile);
            Log.e("audioURI", String.valueOf(audioURI));


        }


        if (audioFile != null) {
            Log.e("enter if audio ", "enter if audio");

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Sending Audio...");
            progressDialog.show();
//            StorageReference fileRef = storageReference.child(fileName + "." + getFileExtension(selectedImageUri));

            StorageReference fileRef = storageReference.child("ChatAudio").child(audioURI.getLastPathSegment());
            fileRef.putFile(audioURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            edtTxtReplyLayout.setVisibility(View.GONE);

                            audioModel = new AudioModel(uri.toString());
                            Log.e("chatAudio", audioModel.getChatAudioUri());
                            chatAudio = audioModel.getChatAudioUri();

                            chat_send_image.setVisibility(View.GONE);
                            chat_send_image_close.setVisibility(View.GONE);

                            Log.e("chatAudio", chatAudio + "        ABD");

                            sendMessage();
                            isAudio = false;
                            chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                            firestoreRecyclerChatAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.show();

                    edtTxtReplyLayout.setVisibility(View.GONE);
                    isAudio = false;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();

                    Log.e("uploading Exception", e.toString());
                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Uploading Failed..", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    edtTxtReplyLayout.setVisibility(View.GONE);
                    isAudio = false;
                }
            });


        } else {

            edtTxtReplyLayout.setVisibility(View.GONE);
            isAudio = false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void audioMessageRecord() {

        try {
            audioRecordingPopup();
            isAudio = true;
            Log.e("audio record hora", "ha hora");
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaRecorder.setOutputFile(getRecordedAudioFile());
            }
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private File getRecordedAudioFile() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "audio_cnb_message" + timestamp + ".mp3");
        Log.e("getfilerecordedfile", String.valueOf(file));
        return file;
    }

    private void audioRecordingPopup() {
        Log.e("audioRecordingPopup", "audioRecordingPopup");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = GroupStudyVideoPlayerActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.audio_recording_layout_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LottieAnimationView micAnimationView = dialogView.findViewById(R.id.micAnimationView);


        micAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Stop recording and save file
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(GroupStudyVideoPlayerActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
                msgEdt.setText("audio_cnb_message" + timestamp + ".mp3");
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }


    // send image in group chat
    private void sendImage() {
        Log.e("method call sendImage", "method call");

        if (selectedImageUri != null) {
            Log.e("enter if ", "enter if");

            ProgressDialog progressDialog = new ProgressDialog(GroupStudyVideoPlayerActivity.this);
            if (msgCategory.equals("pdf")) {
                progressDialog.setTitle("Sending PDF...");

            } else {
                progressDialog.setTitle("Sending Image...");
            }

            progressDialog.show();
//            StorageReference fileRef = storageReference.child(fileName + "." + getFileExtension(selectedImageUri));

            StorageReference fileRef = storageReference.child("LiveGroupChat").child(fileName);
            fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            edtTxtReplyLayout.setVisibility(View.GONE);
                            isImage = false;
                            imageModel = new ImageModel(uri.toString());
                            Log.e("chatImage", imageModel.getChatImageUrl());
                            chatImage = imageModel.getChatImageUrl();


                            chat_send_image.setVisibility(View.GONE);
                            chat_send_image_close.setVisibility(View.GONE);

                            sendMessage();

                            chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                            firestoreRecyclerChatAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.show();

                    edtTxtReplyLayout.setVisibility(View.GONE);
                    isImage = false;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();

                    Log.e("uploading Exception", e.toString());
                    Toast.makeText(GroupStudyVideoPlayerActivity.this, "Uploading Failed..", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    edtTxtReplyLayout.setVisibility(View.GONE);
                    isImage = false;
                }
            });


        } else {

            edtTxtReplyLayout.setVisibility(View.GONE);
            isImage = false;
        }
    }

    //send chat in group chat
    private void sendMessage() {
        Log.e("chatmsg", chatMsg);
        Log.e("userId", userId);
        Log.e("chat", "chat");
        if (msgCategory.equals("image") || msgCategory.equals("pdf")) {

            finalChat = fileName;
        } else {
            finalChat = chatMsg;
        }

        chatdetails.put("chatAudio", chatAudio);
        Log.e("chatAudioType", chatAudio);
        chatdetails.put("message", finalChat);
        chatdetails.put("userId", userId);
        chatdetails.put("userName", userNameString);
        chatdetails.put("userImage", profileImageUrlString);


        chatdetails.put("msgType", "chat");
        chatdetails.put("doubtWeight", 0);

        Long tsLong = System.currentTimeMillis() / 1000;
        String timestampChat = tsLong.toString();
        chat_id_group = userId + timestampChat;

        Log.e("chat_id", chat_id_group);

        chatdetails.put("chatId", chat_id_group);
        chatdetails.put("isReply", isReply);
        chatdetails.put("replyOfMsg", replyOfMsgString);
        chatdetails.put("replyOfUser", replyOfUserString);
        chatdetails.put("repliedOfChatId", replyOfChatIDString);
        chatdetails.put("msgTime", FieldValue.serverTimestamp());
        chatdetails.put("chatImage", chatImage);
        chatdetails.put("msgCategory", msgCategory);

        documentReference = db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("liveGroupChat").document(chat_id_group);
//        documentReference = db.collection("Communities").document(community_id).collection("Chats").document(chat_id).collection("invitedStudent");

        documentReference.set(chatdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


                msgEdt.setText("");
//                msgEdt.setFocusable(false);
                chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                firestoreRecyclerChatAdapter.notifyDataSetChanged();
                Log.e("Chat saved", "Chat saved");
                msgCategory = "";
                isChat = true;
                isReply = false;
                edtTxtReplyLayout.setVisibility(View.GONE);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Chat saved failed", e.toString());

                isChat = true;
                isReply = false;
                edtTxtReplyLayout.setVisibility(View.GONE);
            }
        });


    }

    // for image selection for group chat
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(GroupStudyVideoPlayerActivity.this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(GroupStudyVideoPlayerActivity.this, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//        } else
//            Toast.makeText(GroupStudyVideoPlayerActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "enter");
        live_chat_layout.setVisibility(View.VISIBLE);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 3) {

                selectedImageUri = data.getData();
                msgCategory = "pdf";
                fileName = selectedImageUri.getLastPathSegment();
                Log.e("selectedImageUri", selectedImageUri.toString());
                Log.e("fileName", fileName);
                isImage = true;
                sendImage();
            } else {
                //            ---------Image URi-----------
                isImage = true;
                selectedImageUri = data.getData();
                chat_send_image.setVisibility(View.VISIBLE);
                chat_send_image_close.setVisibility(View.VISIBLE);
                Log.e("selectedImageUri", selectedImageUri.toString());
                fileName = selectedImageUri.getLastPathSegment();
                Log.e("fileName", fileName.toString());

                chat_send_image.setImageURI(selectedImageUri);
                chat_send_image.setVisibility(View.VISIBLE);
                messageBtnCard.setVisibility(View.GONE);
//                sendImage();
            }


        }
        if (isAdmin.equals("true")) {
            player.prepare();
            player.play();
        } else {

        }

    }


    //get chat list of Group chat

    private void getChatList() {
        Log.e("courseIdGetchTList", courseId);
        Log.e("ChatIdGetchatlist", chat_id);
//        Query query = db.collection("Communities").document(courseId).collection("Chats").orderBy("msgTime", Query.Direction.ASCENDING);
        Query query = db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("liveGroupChat").orderBy("msgTime", Query.Direction.ASCENDING);

        if (!query.equals(null)) {
            FirestoreRecyclerOptions<ItemChat> response = new FirestoreRecyclerOptions.Builder<ItemChat>()
                    .setQuery(query, ItemChat.class)
                    .build();

            firestoreRecyclerChatAdapter = new FirestoreRecyclerAdapter<ItemChat, ChatHolder>(response) {
                @Override
                public void onBindViewHolder(ChatHolder holder, @SuppressLint("RecyclerView") int position, ItemChat model) {
                    holder.replied_msg_card.setVisibility(View.GONE);
                    replyOfChatIDString = "";
                    replyOfMsgString = "";
                    replyOfUserString = "";


                    Log.e("this", "this");
                    if (!model.getMessage().equals("")) {
                        holder.chat.setVisibility(View.VISIBLE);

                        holder.chat.setText(model.getMessage());

                    } else {
                        holder.chat.setVisibility(View.INVISIBLE);

                    }

                    String replyMessage = model.getReplyOfMsg();

                    if (model.getUserId().equals(userId)) {
                        holder.incoming_chat_user.setText("");

                    } else {
                        holder.incoming_chat_user.setVisibility(View.VISIBLE);
                        holder.incoming_chat_user.setText(model.getUserName());

                    }


                    if (!model.getUserImage().equals("")) {
                        Glide.with(GroupStudyVideoPlayerActivity.this)
                                .load(model.getUserImage())
                                .into(holder.communityChatImage);
                    } else {
                        Log.e("no image", "No image");
                    }
                    if (model.getMsgCategory().equals("image") || model.getMsgCategory().equals("audio") || model.getMsgCategory().equals("pdf")) {

                        if (model.getMsgCategory().equals("image")) {
                            if (!model.getChatImage().equals("")) {
                                holder.chat_image_card.setVisibility(View.VISIBLE);
                                holder.chat_image.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(model.getChatImage())
                                        .into(holder.chat_image);

                            } else {
                                holder.chat_image_card.setVisibility(View.GONE);
                                holder.chat_image.setVisibility(View.VISIBLE);


                            }
                        }

                        holder.audio_linear.setVisibility(View.GONE);
                        holder.audio_chat_animation.setVisibility(View.GONE);
                        if (model.getMsgCategory().equals("audio")) {
                            if (!model.getChatAudio().equals("")) {
                                holder.audio_linear.setVisibility(View.VISIBLE);
                                holder.chat_image_card.setVisibility(View.GONE);
                                holder.chat.setVisibility(View.GONE);


                            } else {
                                holder.chat_image_card.setVisibility(View.GONE);
                                holder.audio_linear.setVisibility(View.GONE);


                            }
                            holder.chat.setVisibility(View.GONE);
                            holder.audio_chat_animation.setVisibility(View.VISIBLE);

                        } else {
                            holder.audio_linear.setVisibility(View.GONE);

                        }

                        if (model.getMsgCategory().equals("pdf")) {
                            if (!model.getChatImage().equals("")) {
                                holder.chat_image_card.setVisibility(View.GONE);
                                Log.e("msgCategory", model.getMsgCategory());
                                holder.pdf_icon.setVisibility(View.VISIBLE);
//                        holder.chat.setText("PDF file: \n" + model.getChatImage() + "\n" + model.getMessage());

                            } else {
                                holder.pdf_icon.setVisibility(View.GONE);


                            }
                        } else {
                            holder.pdf_icon.setVisibility(View.GONE);

                        }

                    } else {
                        holder.pdf_icon.setVisibility(View.GONE);
                        holder.audio_linear.setVisibility(View.GONE);
                        holder.chat_image_card.setVisibility(View.GONE);
                        holder.chat_image.setVisibility(View.GONE);
                    }

//                    if (model.getMsgCategory().equals("image")) {
//                        if (!model.getChatImage().equals("")) {
//                            holder.chat_image.setVisibility(View.VISIBLE);
//                            holder.chat_image_card.setVisibility(View.VISIBLE);
//                            Glide.with(getApplicationContext())
//                                    .load(model.getChatImage())
//                                    .into(holder.chat_image);
//                        } else {
//                            holder.chat_image.setVisibility(View.GONE);
//                            holder.chat_image_card.setVisibility(View.GONE);
//
//
//                        }
//                    }

//                    if (model.getMsgCategory().equals("pdf")) {
//                        if (!model.getChatImage().equals("")) {
//                            holder.chat_image.setVisibility(View.GONE);
//                            Log.e("msgCategory", model.getMsgCategory());
//                            holder.pdf_icon.setVisibility(View.VISIBLE);
////                        holder.chat.setText("PDF file: \n" + model.getChatImage() + "\n" + model.getMessage());
//
//
//                        } else {
//                            holder.pdf_icon.setVisibility(View.GONE);
//
//
//                        }
//                    }

                    holder.audio_linear.setVisibility(View.GONE);
                    holder.audio_chat_animation.setVisibility(View.GONE);
                    if (model.getMsgCategory().equals("audio")) {
                        if (!model.getChatAudio().equals("")) {
                            holder.audio_linear.setVisibility(View.VISIBLE);

                            holder.chat_image_card.setVisibility(View.GONE);

                        } else {
                            holder.chat_image_card.setVisibility(View.GONE);
                            holder.audio_linear.setVisibility(View.GONE);


                        }
                    }

//                    holder.audio_chat_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Uri uri = Uri.parse(model.getChatAudio());
//                            if (isAudioPlaying) {
//                                try {
//                                    mediaPlayer.stop();
//                                    isAudioPlaying = false;
//                                    holder.audio_chat_animation.setVisibility(View.GONE);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                try {
//                                    mediaPlayer = new MediaPlayer();
//                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                                    mediaPlayer.setDataSource(GroupStudyVideoPlayerActivity.this, uri);
//                                    mediaPlayer.prepare();
//                                    mediaPlayer.start();
//                                    holder.audio_chat_animation.setVisibility(View.VISIBLE);
//                                    isAudioPlaying = true;
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }
//                    });

                    holder.audio_chat_pause_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                mediaPlayer.stop();
                                isAudioPlaying = false;
                                holder.audio_chat_animation.setVisibility(View.INVISIBLE);
                                holder.audio_chat_pause_btn.setVisibility(View.GONE);
                                holder.audio_chat_btn.setVisibility(View.VISIBLE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    holder.audio_chat_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(model.getChatAudio());
                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setDataSource(GroupStudyVideoPlayerActivity.this, uri);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                holder.audio_chat_animation.setVisibility(View.VISIBLE);
                                isAudioPlaying = true;
                                holder.audio_chat_pause_btn.setVisibility(View.VISIBLE);
                                holder.audio_chat_btn.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });


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
                            Log.e("date", String.valueOf(date));
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


                    if (!model.getReplyOfUser().equals("")) {
                        holder.replied_msg_card.setVisibility(View.VISIBLE);
                        holder.replied_by_user.setText(model.getReplyOfUser());
                        holder.replied_of_msg.setText(model.getReplyOfMsg());

                    } else {
                        holder.replied_msg_card.setVisibility(View.GONE);
                    }
                    holder.chat_type.setVisibility(View.GONE);
                    holder.doubt_no.setVisibility(View.GONE);


                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.exit_popup, null);
                            dialogBuilder.setView(dialogView);

                            //Alert Dialog Layout work
                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                            Button yesBtn = dialogView.findViewById(R.id.yesButton);
                            TextView message = dialogView.findViewById(R.id.message);
                            TextView title = dialogView.findViewById(R.id.title);


                            message.setText("Do you want to reply this Message?");
                            title.setVisibility(View.GONE);

                            if (model.getMsgType().equals("doubt")) {
                                yesBtn.setText("Answer");
                                cancelBtn.setText("Raise Doubt");

                            } else if (model.getMsgType().equals("invitation")) {
                                yesBtn.setText("join");
                                cancelBtn.setText("share");


                            } else {
                                yesBtn.setText("Reply");
                                cancelBtn.setText("No");
                            }

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    if (model.getMsgType().equals("doubt")) {

                                        alertDialog.dismiss();


                                    } else if (model.getMsgType().equals("invitation")) {


                                    } else {
                                        alertDialog.dismiss();

                                    }
                                }
                            });
                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    isReply = true;
                                    if (model.getMsgType().equals("doubt")) {
                                        edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                        edtTxtReplyUser.setText(model.getUserId());
                                        edtTxtReplyUser.setMaxLines(1);
                                        edtTxtReply.setMaxLines(1);


                                        isChat = false;
                                        isReply = true;

                                    } else if (model.getMsgType().equals("invitation")) {
                                        Log.e("enter invtation", "true");
                                        isReply = false;
                                        chatIdForVideo = model.getChatId();

                                    } else {
                                        edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                        edtTxtReplyUser.setText(model.getUserId());
                                        edtTxtReplyUser.setMaxLines(1);
                                        edtTxtReply.setMaxLines(1);

                                        isChat = true;

                                        isReply = true;
                                    }


                                    edtTxtReply.setText(model.getMessage());

                                    replyOfChatIDString = model.getChatId();
                                    replyOfMsgString = edtTxtReply.getText().toString();
                                    replyOfUserString = edtTxtReplyUser.getText().toString();
                                    Log.e("replyOfUserString", replyOfUserString);
                                    Log.e("replyOfMsgString", replyOfMsgString);


                                    alertDialog.dismiss();
//                                    msgEdt.requestFocus();
//                                    msgEdt.setFocusable(true);
//                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                    imm.showSoftInput(msgEdt, InputMethodManager.SHOW_IMPLICIT);

                                }
                            });


                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(true);
                            return false;
                        }
                    });

                    holder.pdf_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.getMsgCategory().equals("pdf")) {
                                if (!model.getChatImage().equals("")) {
                                    Intent mileStonePlayer = new Intent(GroupStudyVideoPlayerActivity.this, PdfRenderActivity.class);
                                    mileStonePlayer.putExtra("pdfLink", model.getChatImage());
                                    startActivity(mileStonePlayer);

                                } else {


                                }
                            }

                        }
                    });


                    holder.chat_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inflater = getLayoutInflater();

//                        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
                            dialogBuilder.setView(dialogView);

                            //Alert Dialog Layout work
                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                            ImageView optionImageTrans = dialogView.findViewById(R.id.optionImageTrans);
                            Glide.with(getApplicationContext())
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
                    if (getItem(position).getUserId().equals(userId))
                        return MSG_TYPE_RIGHT;
                    else
                        return MSG_TYPE_LEFT;
                }


                @Override
                public ChatHolder onCreateViewHolder(ViewGroup group, int viewType) {

                    if (viewType == MSG_TYPE_RIGHT) {
                        View view = LayoutInflater.from(group.getContext())
                                .inflate(R.layout.single_normal_chat_outgoing_layout, group, false);
                        return new ChatHolder(view);


                    } else {
                        View view = LayoutInflater.from(group.getContext())
                                .inflate(R.layout.single_normal_chat_incoming_layout, group, false);
                        return new ChatHolder(view);

                    }
//                View view = LayoutInflater.from(group.getContext())
//                        .inflate(R.layout.single_normal_chat_incoming_layout, group, false);
//                return new ChatHolder(view);


                }

                @Override
                public void onError(FirebaseFirestoreException e) {
                    Log.e("error", e.getMessage());
                }


            };


            firestoreRecyclerChatAdapter.notifyDataSetChanged();
            chat_recyclerview.setAdapter(firestoreRecyclerChatAdapter);
            firestoreRecyclerChatAdapter.notifyDataSetChanged();

            chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
        } else {
            Log.e("No chat data", "No chat Data");
        }


    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView incoming_chat_user, chat, chat_time, replied_by_user, replied_of_msg, doubt_no;
        ImageView communityChatImage, chat_type, chat_image, pdf_icon, audio_chat_btn, audio_chat_pause_btn;
        LinearLayoutCompat replied_msg_card;
        CardView chat_image_card;
        ConstraintLayout audio_linear;
        LottieAnimationView audio_chat_animation;

        public ChatHolder(View itemView) {
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
            audio_chat_btn = itemView.findViewById(R.id.audio_chat_btn);
            audio_linear = itemView.findViewById(R.id.audio_linear);
            audio_chat_animation = itemView.findViewById(R.id.audio_chat_animation);
            chat_image_card = itemView.findViewById(R.id.chat_image_card);
            audio_chat_pause_btn = itemView.findViewById(R.id.audio_chat_pause_btn);
            doubt_no = itemView.findViewById(R.id.doubt_weight);


        }
    }


    private void getMicStudentList() {
        Log.e("isFetchingFirstTime", String.valueOf(isFetchingStudentNoFirstTime));

        Query query = db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent");

        Log.e("enter", "entermethoDB");

        FirestoreRecyclerOptions<ItemStudentMic> response = new FirestoreRecyclerOptions.Builder<ItemStudentMic>()
                .setQuery(query, ItemStudentMic.class)
                .build();
        Log.e("enter", "entermethodFIRESTORE RECYCLER");

        firestoreMicRecyclerAdapter = new FirestoreRecyclerAdapter<ItemStudentMic, StudentsMicHolder>(response) {
            @Override
            protected void onBindViewHolder(@NonNull StudentsMicHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ItemStudentMic model) {
                Log.e("enter", "entermethodBIND");

                if (model.getStatus().equals("live")) {


                    holder.user_mic_list_txt.setText(model.getInvStudUsername());
                    holder.user_mic_list_txt.setVisibility(View.VISIBLE);
                    if (isAdmin.equals("true")) {
                        if (model.getIsSelected().equals("true")) {
                            holder.user_mic_on.setVisibility(View.GONE);
                            holder.user_mic_off.setVisibility(View.VISIBLE);
                        } else {
                            holder.user_mic_on.setVisibility(View.VISIBLE);
                            holder.user_mic_off.setVisibility(View.GONE);
                        }

                        holder.user_mic_on.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isMicOn = false;

                                String micStudentId = model.getInvStudId();
                                String offOn = "off";
                                String trueFalse = "true";
                                holder.user_mic_on.setVisibility(View.GONE);
                                holder.user_mic_off.setVisibility(View.VISIBLE);

                                updateMicStatus(micStudentId, offOn, trueFalse);


                            }
                        });

                        holder.user_mic_off.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isMicOn = true;

                                String micStudentId = model.getInvStudId();
                                String offOn = "on";
                                String trueFalse = "false";
                                holder.user_mic_on.setVisibility(View.VISIBLE);
                                holder.user_mic_off.setVisibility(View.GONE);
                                updateMicStatus(micStudentId, offOn, trueFalse);

                            }
                        });
                    } else {
                        holder.user_mic_off.setVisibility(View.GONE);
                        holder.user_mic_on.setVisibility(View.GONE);
                    }

                } else {
                    holder.user_mic_off.setVisibility(View.GONE);
                    holder.user_mic_on.setVisibility(View.GONE);
                    holder.user_mic_list_txt.setVisibility(View.GONE);

                }
            }

            @NonNull
            @Override
            public StudentsMicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mic_cotroller_group_study, parent, false);
                Log.e("enter", "entermethodONCREAT");

                return new StudentsMicHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

        };
        firestoreMicRecyclerAdapter.startListening();

        Log.e("enter", "entermethodADAPTER");

        firestoreMicRecyclerAdapter.notifyDataSetChanged();
        micUserList.setAdapter(firestoreMicRecyclerAdapter);

    }

    public class StudentsMicHolder extends RecyclerView.ViewHolder {
        TextView user_mic_list_txt;
        ImageView user_mic_on, user_mic_off;


        public StudentsMicHolder(View itemView) {
            super(itemView);
            Log.e("enter", "entermethod" +
                    "HOLDER");

            user_mic_list_txt = itemView.findViewById(R.id.user_mic_list);
            user_mic_on = itemView.findViewById(R.id.user_mic_on);
            user_mic_off = itemView.findViewById(R.id.user_mic_off);


        }
    }

    private void updateMicStatus(String micStudentId, String offOn, String trueFalse) {
        db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());

                count = 1;

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        String sids = documentChange.getDocument().getData().get("invStudId").toString();


                        if (micStudentId.equals(sids)) {


                            String documentId = documentChange.getDocument().getId();
                            db.collection("Communities").document(courseId).collection("Chats")
                                    .document(chat_id).collection("invitedStudent").document(documentId).update("micStatus", offOn);

                            db.collection("Communities").document(courseId).collection("Chats")
                                    .document(chat_id).collection("invitedStudent").document(documentId).update("isSelected", trueFalse);

                            break;

                        } else {

                            Log.e("Not live status", "Not live status");

                        }
                    }


                } else {


                }


            }
        });

    }

    private void fetchLiveStudentListForAdminChoose() {


        Log.e("enter", "entermethod1");

        Query query = db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent");

        Log.e("enter", "entermethoDB");

        FirestoreRecyclerOptions<ItemChooseModel> response = new FirestoreRecyclerOptions.Builder<ItemChooseModel>()
                .setQuery(query, ItemChooseModel.class)
                .build();
        Log.e("enter", "entermethodFIRESTORE RECYCLER");

        firestoreAdminRecyclerAdapter = new FirestoreRecyclerAdapter<ItemChooseModel, StudentsAdminHolder>(response) {
            @Override
            protected void onBindViewHolder(@NonNull StudentsAdminHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ItemChooseModel model) {
                Log.e("enter", "entermethodBIND");


                holder.studentNameInvitation.setText(model.getInvStudUsername());


//                if (model.getIsSelected().equals("true")) {
//                    holder.student_select.setVisibility(View.GONE);
//                    holder.student_select_rectangle.setVisibility(View.VISIBLE);
//
//                } else {
//                    holder.student_select.setVisibility(View.VISIBLE);
//                    holder.student_select_rectangle.setVisibility(View.GONE);
//                }

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (clicked != position) {
//                            for (int i = 0; i < firestoreAdminRecyclerAdapter.getItemCount(); i++) {
//                                if (i == position) {
//                                    holder.student_select.setVisibility(View.VISIBLE);
//                                    holder.student_select_rectangle.setVisibility(View.GONE);
//                                    model.setIsSelected("true");
//                                    clicked = position;
//                                    selectedAdmin = model.getInvStudId();
//                                    Log.e("selectedAdmin",selectedAdmin);
//                                } else {
//                                    model.setIsSelected("false");
//                                    holder.student_select.setVisibility(View.GONE);
//                                    holder.student_select_rectangle.setVisibility(View.VISIBLE);
//                                    selectedAdmin = "";
//
//
//                                }
//                            }
//                            notifyDataSetChanged();
//                        } else {
//                            holder.student_select.setVisibility(View.GONE);
//                            holder.student_select_rectangle.setVisibility(View.VISIBLE);
//                            model.setIsSelected("false");
//                            selectedAdmin = "";
//                            clicked = 100;
//                            notifyDataSetChanged();
//                        }
//                    }
//
//
//                });

                if (position == selected_position) {
                    model.setSelected(true);
                } else {
                    model.setSelected(false);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_position = position;
                        selectedAdmin = model.getInvStudId();
                        selectedAdminName = model.getInvStudUsername();
                        notifyDataSetChanged();
                    }
                });
                if (model.isSelected()) {
                    holder.student_select.setVisibility(View.VISIBLE);
                    holder.student_select_rectangle.setVisibility(View.GONE);

                } else {
                    holder.student_select.setVisibility(View.GONE);
                    holder.student_select_rectangle.setVisibility(View.VISIBLE);
                }


            }


            @NonNull
            @Override
            public StudentsAdminHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.student_list_invitation_item, parent, false);
                Log.e("enter", "entermethodONCREAT");

                return new StudentsAdminHolder(view);

            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };
        firestoreAdminRecyclerAdapter.startListening();

        Log.e("enter", "entermethodADAPTER");

        firestoreAdminRecyclerAdapter.notifyDataSetChanged();
        chooseAdminList.setAdapter(firestoreAdminRecyclerAdapter);
        firestoreAdminRecyclerAdapter.notifyDataSetChanged();

    }

    public class StudentsAdminHolder extends RecyclerView.ViewHolder {
        TextView studentNameInvitation;
        CircleImageView studentPicInvitation;
        ImageView student_select, student_select_rectangle;


        public StudentsAdminHolder(View itemView) {
            super(itemView);
            Log.e("enter", "entermethod" +
                    "HOLDER");
            student_select = itemView.findViewById(R.id.student_select);
            studentPicInvitation = itemView.findViewById(R.id.studentPicInvitation);
            studentNameInvitation = itemView.findViewById(R.id.studentNameInvitation);
            student_select_rectangle = itemView.findViewById(R.id.student_select_rectangle);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        firestoreRecyclerChatAdapter.startListening();

    }

    private void fetchingCurrentLive() {

        db.collection("Communities").document(courseId).collection("Chats")
                .document(chat_id).collection("invitedStudent").addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        count = 0;
                        List<DocumentSnapshot> studentList = documentSnapshots.getDocuments();
                        Log.e("List", studentList.toString());
                        Log.e("count1", String.valueOf(count));
                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {

                                if (documentChange.getDocument().getData().get("status").toString().equals("live")) {

                                    count = count + 1;
                                    Log.e("count2", String.valueOf(count));

                                }

                            }

                        } else {

                            count = count;

                        }
                        String countString = String.valueOf(count);
                        Log.e("countString", String.valueOf(count));
                        students_no.setText(countString);
                        students_number.setText(countString);

                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (questionFlag) {
            player.play();
        }
        player.play();
        popupQuestion();

//        count = 0;

    }

    @Override
    public void onBackPressed() {
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
        Log.e("admin", isAdmin);

        if (isAdmin.equals("true")) {
            if (fromActivity.equals("groupPlayer")) {
                db = FirebaseFirestore.getInstance();
                db.collection("Communities").document(courseId).collection("Chats")
                        .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("backButton", "clicked");

                super.onBackPressed();

            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = GroupStudyVideoPlayerActivity.this.getLayoutInflater();
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


                message.setText("Before exit you have to choose another Admin for the Group Study");

                cancelBtn.setText("Stop Study");
                yesBtn.setText("Choose admin");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        editor.putInt("playbackTime", (int) player.getCurrentPosition());
                        db.collection("Communities").document(courseId).collection("Chats")
                                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "unlive");


                        alertDialog.dismiss();
                        mRtcEngine.leaveChannel();
                        mRtcEngine.destroy();
                        Intent communityIntent = new Intent(GroupStudyVideoPlayerActivity.this, CommunityChatPageActivity.class);


                        communityIntent.putExtra("community_id", courseId);
                        communityIntent.putExtra("community_name", communityNameG);
                        communityIntent.putExtra("community_logo", communityLogoG);
                        communityIntent.putExtra("activity", "groupPlayer");
                        startActivity(communityIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                    GroupStudyVideoPlayerActivity.super.onBackPressed();
                    }
                });
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        choose_admin_card.setVisibility(View.VISIBLE);
                        fetchLiveStudentListForAdminChoose();
                        alertDialog.dismiss();


                    }
                });


                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);

            }

        } else {

            if (fromActivity.equals("groupPlayer")) {
                super.onBackPressed();
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = GroupStudyVideoPlayerActivity.this.getLayoutInflater();
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


                message.setText("Do you rally want to exit the group");

                cancelBtn.setText("Cancel");
                yesBtn.setText("Exit Group");

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        editor.putInt("playbackTime", (int) player.getCurrentPosition());

                        mRtcEngine.leaveChannel();
                        db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                                Log.e("List", studentList.toString());


                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                        String sids = documentChange.getDocument().getData().get("invStudId").toString();


                                        if (studentUId.equals(sids)) {


                                            String documentId = documentChange.getDocument().getId();
                                            db.collection("Communities").document(courseId).collection("Chats")
                                                    .document(chat_id).collection("invitedStudent").document(documentId).update("status", "unlive");

                                            break;

                                        } else {

                                            Log.e("Not live status", "Not live status");

                                        }
                                    }


                                } else {


                                }


                            }
                        });


//        Log.e("seekToPlaybackTime",""+player.getDuration());
                        editor.apply();
                        courseEditor = getSharedPreferences("courseId_" + courseId, MODE_PRIVATE).edit();
                        courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
                        courseEditor.apply();
                        player.pause();
                        player.release();
                        popupQuestionChecker.removeCallbacksAndMessages(null);
                        alertDialog.dismiss();


                        Intent communityIntent = new Intent(GroupStudyVideoPlayerActivity.this, CommunityChatPageActivity.class);
                        communityIntent.putExtra("community_id", courseId);
                        communityIntent.putExtra("community_name", communityNameG);
                        communityIntent.putExtra("community_logo", communityLogoG);
                        communityIntent.putExtra("activity", "groupPlayer");
                        startActivity(communityIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                    finish();
//                    GroupStudyVideoPlayerActivity.super.onBackPressed();


                    }
                });


                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);

            }

        }
    }

    private void fetchLiveStudentListForAdminChooses() {

        Log.e("Enter adminchoose", "enter");

        db.collection("Communities").document(courseId).collection("Chats").document(chat_id).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();

                Log.e("List", studentList.toString());

                studentArrayListLive.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        ItemStudent itemStudent = new ItemStudent();
                        String sid = documentChange.getDocument().getData().get("invStudId").toString();
                        String name = documentChange.getDocument().getData().get("invStudUsername").toString();
                        itemStudent.setStudent_id(sid);
                        itemStudent.setStudent_username(name);
                        itemStudent.setSelected(false);
                        itemStudent.setActivity("groupStudyPlayer");

                        Log.e("studentIdLoop11", name);
                        studentArrayListLive.add(itemStudent);
                    }
                    List<ItemStudent> types = queryDocumentSnapshots.toObjects(ItemStudent.class);
                    Log.e("onSuccess: ", String.valueOf(studentArrayListLive));
                    itemStudentAdapter = new ItemStudentAdapter(studentArrayListLive);
                    chooseAdminList.setAdapter(itemStudentAdapter);


                } else {

                }
            }

        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        firestoreRecyclerChatAdapter.stopListening();
//        db.collection("Communities").document(courseId).collection("Chats")
//                .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "unlive");

        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        editor.putInt("playbackTime", (int) player.getCurrentPosition());
        editor.apply();
        courseEditor = getSharedPreferences("courseId_" + courseId, MODE_PRIVATE).edit();
        courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
        courseEditor.apply();
//        mRtcEngine.leaveChannel();
//        mRtcEngine.destroy();
        player.pause();
        Log.e("onstop", "onstop");

        popupQuestionChecker.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firestoreRecyclerChatAdapter.stopListening();

        if (isAdmin.equals("true")) {
            if (fromActivity.equals("groupPlayer")) {
                db.collection("Communities").document(courseId).collection("Chats")
                        .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "live");

            } else {
                db.collection("Communities").document(courseId).collection("Chats")
                        .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).update("liveUnliveStatus", "unlive");
            }
        }


        new UpdatePlayingStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        editor.putInt("playbackTime", (int) player.getCurrentPosition());
        editor.apply();
        courseEditor = getSharedPreferences("courseId_" + courseId, MODE_PRIVATE).edit();
        courseEditor.putInt("videoPosition", player.getCurrentWindowIndex());
        courseEditor.apply();
        player.pause();
        player.release();
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
        Log.e("Ondestroy", "Ondestroy");
        popupQuestionChecker.removeCallbacksAndMessages(null);
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GroupStudyVideoPlayerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    class GetSuggestedMileDetailsDash extends AsyncTask<String, Void, String> {
        String isAdminLive = "";
        private ProgressDialog dialog = new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(getApplicationContext());
            int versionCode = com.vision_digital.BuildConfig.VERSION_CODE;
            String param = "uid=" + userId + "&app_version=" + versionCode + "&student_id=" + sid + "&course_id=" + community_id;

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
                            String subscriptionStatus = dataObj.getString("subscription_status");
                            Log.e("subscriptionStatus", subscriptionStatus);


                            if (subscriptionStatus.equals("subscribed")) {

                                Log.e("Subscription", "Subscribed");

                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");

                                mileStonesArrayLists.clear();

                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));

                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
//                                        dialogForIntent.show();
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);

                                        if (mileStoneObj.getString("id").equals(suggMileId)) {

                                            Log.e("videoPos", String.valueOf(videoPos));
                                            mileStone.setId(mileStoneObj.getString("id"));
//                                        mileStone.setVideoPosition(positionNumber++);
                                            int videoPosTemp = positionNumber++;
                                            mileStone.setVideoPosition(videoPosTemp);
                                            Log.e("Topic name", "" + mileStoneObj.getString("title"));
                                            mileStone.setTitle(mileStoneObj.getString("title"));
                                            mileStone.setDuration(mileStoneObj.getString("duration"));
                                            mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                            mileStone.setVideoUrl(mileStoneObj.getString("video_link"));
                                            mileStone.setActivityType("courseDetails");
                                            mileStone.setSelected(false);
                                            videoPos = videoPosTemp;
                                            mileStonesArrayLists.add(mileStone);
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


                                }
                                Log.e("chat videoStatus", videoStatusId);

                                Intent mileStonePlayer = new Intent(GroupStudyVideoPlayerActivity.this, GroupStudyVideoPlayerActivity.class);
                                mileStonePlayer.putExtra("videoPosition", videoPos);
                                mileStonePlayer.putExtra("mileStonesList", mileStonesArrayLists);
                                mileStonePlayer.putExtra("id", community_id);
                                mileStonePlayer.putExtra("chapter_id", chapter_id);
                                Log.e("milearrayCommunitylive", String.valueOf(mileStonesArrayLists));
                                mileStonePlayer.putExtra("chatId", chatIdForVideo);
                                mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                                mileStonePlayer.putExtra("community_name", communityNameG);
                                mileStonePlayer.putExtra("community_logo", communityLogoG);
                                mileStonePlayer.putExtra("isAdmin", "false");
                                mileStonePlayer.putExtra("userImage", profileImageUrlString);
                                mileStonePlayer.putExtra("fromActivity", "chatActivity");
                                startActivity(mileStonePlayer);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                                db.collection("Communities").document(courseId).collection("Chats")
                                        .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).
                                        collection("SuggestedMilestone").document(videoStatusId).update("itemViewPos", "");

                                db.collection("Communities").document(courseId).collection("Chats")
                                        .document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId).
                                        collection("SuggestedMilestone").document(videoStatusId).update("mileId", "");
//


                            }


                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                                    ((Activity) getApplicationContext()).finish();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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