package com.vision_digital.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.vision_digital.BuildConfig;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.activities.PdfRenderActivity;
import com.vision_digital.R;
import com.vision_digital.community.chatModels.ImageModel;
import com.vision_digital.community.chatModels.ItemChat;
import com.vision_digital.community.chatModels.AudioModel;
import com.vision_digital.community.invitation.SelectMilestoneInvitationActivity;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityChatPageActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    // for voice channel permission
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private static final int PERMISSION_REQUEST_CODE = 200;


    private Toolbar community_toolbar;

    RelativeLayout chat_parent_layout;
    public static String communityName;
    String communityId;
    public static String communityLogo;
    private CircleImageView logo;
    private TextView community_name;
    private ImageView back_btn, menu_chat;
    public static String invitationMilestoneId = "";
    LinearLayout messageEdt_layout;


    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private String currentPhotoPath;
    private String fileName = "";
    private Boolean isReply = false;
    private ImageView buttonPlus, chat_send_image, btndoubtSms, chat_send_image_close;
    TextView btnSend;
    private EditText msgEdt;
    private FirestoreRecyclerAdapter firestoreRecyclerChatAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String chatImage = "";
    private String chatAudio = "";
    private String timestamp;
    private Boolean isDoubt = false;
    private Boolean isAnswer = false;
    private Boolean isReplyOfAnswer = false;
    private Boolean isInvitation = false;
    private Boolean isChat = true;
    private Boolean isAudio = false;

    private String msgCategory = "";
    private Boolean isImage = false;
    public static String community_id;
    private String chat_id = "";
    private String chatIdForVideo = "";
    private String communityname;
    private String invitedTime;
    private String selectedTimeStringLanguage = "";
    private RecyclerView chat_recyclerview;
    final int MSG_TYPE_LEFT = 0;
    final int MSG_TYPE_RIGHT = 1;
    private LinearLayoutManager linearLayoutManager;
    private Uri selectedImageUri;
    private ImageModel imageModel;
    private AudioModel audioModel;
    private String replyOfUserString = "";
    private String replyOfMsgString = "";
    private String replyOfChatIDString = "";
    private int doubt_weight = 0;
    private int videoPosForSendInvite = 0;

    private LinearLayoutCompat edtTxtReplyLayout;
    private TextView edtTxtReplyUser, edtTxtReply;
    private ImageView chat_reply_cancel_image;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatImage");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private HashMap<String, Object> chatdetails = new HashMap<>();
    private HashMap<String, Object> invitedVideodetails = new HashMap<>();
    private HashMap<String, Object> suggMileVideodetails = new HashMap<>();
    private DocumentReference reference, documentReferenceForVideoStatus, docRefVideoStatusForSuggMile;
    private String milestoneId = "", milestone_name;
    private String from_activity = "";

    // for invitation join
    private String courseDetailsUrl = "";
    int chapter_id;
    private String courseDescription;
    int videoPos = 0;
    int videoPosTemp = 0;
    private String chap_id = "";
    private String lastMilestoneId = "";
    private String subscriptionStatus = "";
    private String courseDuration = "";
    private String courseOwnerName = "", courseOwnerQuali = "";
    private String invitationId = "";
    private List<Long> forMonths = new ArrayList<>();
    private Uri previewVideoURI;
    private ArrayList<Uri> courseMilestoneList = new ArrayList<>();
    private ArrayList<ItemMileStone> mileStonesArrayList = new ArrayList<>();
    private ArrayList<ItemChapter> chaptersList = new ArrayList<>();
    private ArrayList<String> invitedStudentId = new ArrayList<>();
    private ArrayList<String> invitedStudentName = new ArrayList<>();
    private String studentUId = "";
    private String userType = "";
    private String videoStatusId = "";
    private String firestoreUserId = "";
    private String invitationTimeInString = "";
    private String timeString = "";

    //  chatmsg exactly jo firestore pr save hoga
    private String chatMsg = "";
    private String admin = "";
    private String isAdmin = "";
    private Date date, dateNowJoin;

    private CollectionReference collectionReference;
    private HashMap<String, Object> invitedStudentDetails = new HashMap<>();

    //profile

    private String userNameString = "";
    private String profileImageUrlString = "";
    private String userNameFromSelectStudent = "";

    //filter

    private boolean showInvitation = false;
    private boolean showDoubt = false;
    private boolean showImages = false;
    private boolean showDoc = false;
    private boolean showAudio = false;
    private String isFilterOn = "no";


    //Attachment after clicking + 'plus' Button

    LinearLayout attachment_file_layout;
    ImageView chat_camera, chat_gallery, chat_doc, chat_mic, chat_invitation, chat_mic_off;
    private boolean isAttachmentLayoutOpen = false;
    boolean isAudioPlaying = false;


    private String userId = "";
    private int sid = 0;
    private FirebaseUser user;

    //reference for last message
    private DocumentReference lastMsgReference,lastMsgReferenceCommunityAddress;
    private HashMap<String, Object> lastChatDetails = new HashMap<>();

    //for audio
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chat_page);
        logo = findViewById(R.id.community_logo_chat);
        community_name = findViewById(R.id.community_name_chat);
        setSupportActionBar(community_toolbar);
        chat_parent_layout = findViewById(R.id.chat_parent_layout);
        back_btn = findViewById(R.id.back_btn);
        menu_chat = findViewById(R.id.menu_chat);
        attachment_file_layout = findViewById(R.id.attachment_file_layout);
        chat_camera = findViewById(R.id.chat_camera);
        chat_gallery = findViewById(R.id.chat_gallery);
        chat_doc = findViewById(R.id.chat_doc);
        chat_mic = findViewById(R.id.chat_mic);
        btnSend = findViewById(R.id.btnSendSms);
        chat_invitation = findViewById(R.id.chat_invitation);
        chat_mic_off = findViewById(R.id.chat_mic_off);
        edtTxtReplyLayout = findViewById(R.id.edtTxtReplyLayout);
        edtTxtReplyUser = findViewById(R.id.edtTxtReplyUser);
        edtTxtReply = findViewById(R.id.edtTxtReply);
        chat_reply_cancel_image = findViewById(R.id.chat_reply_cancel_image);
        btndoubtSms = findViewById(R.id.btndoubtSms);
        btndoubtSms.setVisibility(View.GONE);
        buttonPlus = findViewById(R.id.btnPlus);
        chat_send_image = findViewById(R.id.chat_send_image);
        chat_send_image.setVisibility(View.GONE);
        chat_send_image_close = findViewById(R.id.chat_send_image_close);
        chat_send_image_close.setVisibility(View.GONE);
        msgEdt = findViewById(R.id.edtTxtSms);
        ImageView cancelAudio = findViewById(R.id.cancelAudio);
        messageEdt_layout = findViewById(R.id.messageEdt_layout);
        courseDetailsUrl = getString(R.string.apiURL) + "getCourseDetails";


        studentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotToCommunityDetails = new Intent(CommunityChatPageActivity.this, CommunityDetailsActivity.class);
                gotToCommunityDetails.putExtra("community_id", communityId);
                gotToCommunityDetails.putExtra("community_name", communityName);
                gotToCommunityDetails.putExtra("community_logo", communityLogo);
                startActivity(gotToCommunityDetails);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        community_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotToCommunityDetails = new Intent(CommunityChatPageActivity.this, CommunityDetailsActivity.class);
                gotToCommunityDetails.putExtra("community_id", communityId);
                gotToCommunityDetails.putExtra("community_name", communityName);
                gotToCommunityDetails.putExtra("community_logo", communityLogo);
                startActivity(gotToCommunityDetails);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        cancelAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSend.setVisibility(View.GONE);
                btndoubtSms.setVisibility(View.GONE);
                msgEdt.setText("");
                cancelAudio.setVisibility(View.GONE);
                if (isAudio) {
                    isAudio = false;
                }

            }
        });


        chat_recyclerview = findViewById(R.id.chat_recyclerview);

        btnSend.setVisibility(View.GONE);
        btndoubtSms.setVisibility(View.GONE);

        attachment_file_layout.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            SharedPreferences userIsRegisteredSuccessful = getSharedPreferences("CNB", MODE_PRIVATE);
            boolean registered = userIsRegisteredSuccessful.getBoolean("registered", true);
            sid = userIsRegisteredSuccessful.getInt("sid", 0);
            Log.e("sid", String.valueOf(sid));
            if (registered) {
                userId = user.getUid();
                Log.e("uid", userId);
            }
        }

        communityId = getIntent().getStringExtra("community_id");

        communityName = getIntent().getStringExtra("community_name");
        communityLogo = getIntent().getStringExtra("community_logo");

        invitationMilestoneId = getIntent().getStringExtra("milestone_id");
//        Log.e("logo", communityLogo);

        menu_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CommunityChatPageActivity.this, v);
                popup.setOnMenuItemClickListener(CommunityChatPageActivity.this);
                popup.inflate(R.menu.chat_menu);
                popup.show();
            }
        });
        Glide.with(getApplicationContext())
                .load(communityLogo)
                .into(logo);
        community_name.setText(communityName);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        msgEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                attachment_file_layout.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                if (isInvitation || isReplyOfAnswer) {
                    btndoubtSms.setVisibility(View.GONE);
                } else {
                    btndoubtSms.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnSend.setVisibility(View.VISIBLE);
                if (isInvitation || isReplyOfAnswer) {
                    btndoubtSms.setVisibility(View.GONE);
                } else {
                    btndoubtSms.setVisibility(View.VISIBLE);
                }
            }
        });

        chat_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_file_layout.setVisibility(View.GONE);
            }
        });

        // Attachment

        chat_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    isImage = true;
                    isAttachmentLayoutOpen = false;
                    attachment_file_layout.setVisibility(View.GONE);
                    ImagePicker.Companion.with(CommunityChatPageActivity.this)
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
                if (checkPermission()) {
                    isImage = true;
                    isAttachmentLayoutOpen = false;
                    attachment_file_layout.setVisibility(View.GONE);
                    ImagePicker.Companion.with(CommunityChatPageActivity.this)
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

        btndoubtSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    isDoubt = true;
                    isChat = false;
                    isAnswer = false;
                    isInvitation = false;

                    if (isImage) {
                        msgCategory = "image";
                        sendImage();
                        edtTxtReplyLayout.setVisibility(View.GONE);
                        btnSend.setVisibility(View.GONE);
                        btndoubtSms.setVisibility(View.GONE);

                    } else if (isAudio) {
                        msgCategory = "audio";
                        sendAudio();
                    } else {
                        String msgUtils = msgEdt.getText().toString();
                        if (!msgUtils.equals("")) {
                            msgCategory = "txt";
                            sendMessage();
                            btnSend.setVisibility(View.GONE);
                            btndoubtSms.setVisibility(View.GONE);
                        } else {

                        }
                    }


            }
        });


        // After Clicking on this we will be
        // redirected to choose pdf
        chat_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAttachmentLayoutOpen = false;
                attachment_file_layout.setVisibility(View.GONE);
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 3);

            }
        });

        chat_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetStudentSubscriptionStatus().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//                attachment_file_layout.setVisibility(View.GONE);
//                Intent intent = new Intent(CommunityChatPageActivity.this, SelectMilestoneInvitationActivity.class);
//                intent.putExtra("community_id", community_id);
//                intent.putExtra("community_name", communityName);
//                intent.putExtra("community_logo", communityLogo);
//                intent.putExtra("userNameString", userNameString);
//                Log.e("community_logo", communityLogo);
//                startActivity(intent);

            }
        });


        chat_mic_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Stop recording and save file
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(CommunityChatPageActivity.this, "Tap on animation to stop recording!.. ", Toast.LENGTH_SHORT).show();


            }
        });


        chat_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isMicrophonePresent()) {
                    getMicrophonePermission();

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    audioMessageRecord();
                }
            }
        });


        getProfileData();

        from_activity = getIntent().getStringExtra("activity");
        Log.e("fromActivity", from_activity);
        if (from_activity.equals("community_list") || from_activity.equals("course_details")  || from_activity.equals("groupPlayer") || from_activity.equals("milestone")) {
            getProfileData();
            community_id = getIntent().getStringExtra("community_id");
            communityName = getIntent().getStringExtra("community_name");
            buttonPlus.setVisibility(View.VISIBLE);
            Log.e("communityid", community_id);


        }
        else if (from_activity.equals("courseDetails") || from_activity.equals("communityChatPage") || from_activity.equals("milestonePlayer")) {
            getProfileData();
            milestoneId = getIntent().getStringExtra("milestone_id");
//            Log.e("milestone_idCht", milestoneId);
//            btnSend.setVisibility(View.VISIBLE);
            community_id = getIntent().getStringExtra("community_id");
            communityName = getIntent().getStringExtra("community_name");
            milestone_name = getIntent().getStringExtra("milestone_name");
            invitationTimeInString = getIntent().getStringExtra("timeInSting");
            String videoposTemperary = getIntent().getStringExtra("videoPos");
            Log.e("videoposTemperary", String.valueOf(videoposTemperary));
            Log.e("community_idChat", community_id);

            userNameFromSelectStudent = getIntent().getStringExtra("userNameString");
            videoPosForSendInvite = Integer.parseInt(videoposTemperary);
            btndoubtSms.setVisibility(View.GONE);
            buttonPlus.setVisibility(View.GONE);
            Log.e("videoPosForSendInvite", String.valueOf(videoPosForSendInvite));
            invitedTime = getIntent().getStringExtra("invitedTime");
            selectedTimeStringLanguage = getIntent().getStringExtra("selectedTimeStringLanguage");

            invitedStudentId = (ArrayList<String>) getIntent().getSerializableExtra("invitedStudId");
            invitedStudentName = (ArrayList<String>) getIntent().getSerializableExtra("invitedStudName");
            Log.e("selectedItems", String.valueOf(invitedStudentId));
            Log.e("milestonename chat", milestone_name);
            Log.e("invitedTime chat", String.valueOf(invitedTime));



            isInvitation = true;
            isDoubt = false;
            isAnswer = false;
            isChat = false;
            isReply = false;
            msgEdt.setText(userNameFromSelectStudent + " send Invitation for Group Study on Topic: " + milestone_name + ". You can join this group " + selectedTimeStringLanguage + "\n" + " \nhttps://chalksnboard.com/downloadAppChalksnboard?data/community/" + communityId);
            msgEdt.setEnabled(false);
            edtTxtReply.setHeight(40);

        } else {

        }


        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        String chat_id = userId + timestamp;
        videoStatusId = userId + "video" + timestamp;

        Log.e("chat_id", chat_id);

        edtTxtReplyLayout.setVisibility(View.GONE);
        chat_reply_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTxtReplyLayout.setVisibility(View.GONE);
            }
        });


        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAttachmentLayoutOpen) {
                    attachment_file_layout.setVisibility(View.GONE);
                    isAttachmentLayoutOpen = false;
                    chat_mic_off.setVisibility(View.GONE);
                    chat_send_image.setVisibility(View.GONE);
                    chat_send_image_close.setVisibility(View.GONE);
                } else {
                    attachment_file_layout.setVisibility(View.VISIBLE);
                    chat_mic_off.setVisibility(View.GONE);
                    isAttachmentLayoutOpen = true;
                    chat_send_image.setVisibility(View.GONE);
                    chat_send_image_close.setVisibility(View.GONE);
                }

            }
        });

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        chat_recyclerview.setLayoutManager(linearLayoutManager);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAudio.setVisibility(View.GONE);
                if (isImage) {
                    msgCategory = "image";
                    sendImage();
                    edtTxtReplyLayout.setVisibility(View.GONE);
                    btnSend.setVisibility(View.GONE);
                } else if (isAudio) {
                    msgCategory = "audio";
                    sendAudio();
                } else {
                    String msgUtils = msgEdt.getText().toString();
                    if (!msgUtils.equals("")) {
                        msgCategory = "txt";
                        sendMessage();
                        btnSend.setVisibility(View.GONE);

                    } else {

                    }
                }
            }
        });
        chat_send_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlus.setVisibility(View.VISIBLE);
                msgEdt.setText("");
                msgEdt.setEnabled(true);
                selectedImageUri.equals(null);
                isImage = false;
                isChat = true;
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

        msgEdt.setEnabled(true);
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
                            Log.e("chatAudio", audioModel.getChatAudioUri());
                            Log.e("chatAudio", audioModel.getChatAudioUri());
                            Log.e("chatAudio", audioModel.getChatAudioUri());

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
                    Toast.makeText(CommunityChatPageActivity.this, "Uploading Failed..", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(this, "Tap on animation to stop recording!...", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommunityChatPageActivity.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = CommunityChatPageActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.audio_recording_layout_popup, null);
        dialogBuilder.setView(dialogView);

        //Alert Dialog Layout work
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LottieAnimationView micAnimationView = dialogView.findViewById(R.id.micAnimationView);
        ImageView cancelAudio = findViewById(R.id.cancelAudio);


        micAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Stop recording and save file
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(CommunityChatPageActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
                msgCategory = "audio";
                sendAudio();
                msgEdt.setEnabled(false);
                msgEdt.setText("audio_cnb_message" + timestamp + ".mp3");
                btndoubtSms.setVisibility(View.VISIBLE);
//                btnSend.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    private void sendImage() {
        Log.e("method call sendImage", "method call");

        if (selectedImageUri != null) {
            Log.e("enter if ", "enter if");

            ProgressDialog progressDialog = new ProgressDialog(this);
            if (msgCategory.equals("pdf")){
                progressDialog.setTitle("Sending PDF...");

            }else{
                progressDialog.setTitle("Sending Image...");
            }

            progressDialog.show();
//            StorageReference fileRef = storageReference.child(fileName + "." + getFileExtension(selectedImageUri));

            StorageReference fileRef = storageReference.child("ChatImage").child(fileName);
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
                    Toast.makeText(CommunityChatPageActivity.this, "Uploading Failed..", Toast.LENGTH_SHORT).show();
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

    private void sendMessage() {

        msgEdt.setEnabled(true);
        btnSend.setVisibility(View.GONE);
        chap_id = "";

        if (msgCategory.equals("image") || msgCategory.equals("pdf")) {

            chatMsg = fileName;
        } else if (msgCategory.equals("audio")) {
            chatMsg = "";
        } else {
            chatMsg = msgEdt.getText().toString().trim();
        }


        Log.e("chatmsg", chatMsg);
        Log.e("userId", userId);
        Log.e("chat", "chat");

        chatdetails.put("chatAudio", chatAudio);
        Log.e("chatAudioType", chatAudio);
        chatdetails.put("message", chatMsg);
        chatdetails.put("userId", userId);
        chatdetails.put("userName", userNameString);
        chatdetails.put("userImage", profileImageUrlString);
        Log.e("msgType", String.valueOf(isDoubt));
        Log.e("msgType", String.valueOf(isAnswer));
        Log.e("msgType", String.valueOf(isInvitation));

        if (isDoubt) {
            doubt_weight = 0;
            chatdetails.put("msgType", "doubt");
            chatdetails.put("doubtWeight", doubt_weight);
        } else if (isAnswer) {
            doubt_weight = 0;
            chatdetails.put("msgType", "answer");
            chatdetails.put("doubtWeight", 0);
        } else if (isInvitation) {
            doubt_weight = 0;
            chatdetails.put("msgType", "invitation");
            chatdetails.put("doubtWeight", 0);
        } else if (isChat) {
            doubt_weight = 0;
            chatdetails.put("msgType", "chat");
            chatdetails.put("doubtWeight", 0);
        }

        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        chat_id = userId + timestamp;
        Log.e("chat_id", chat_id);
        FieldValue msgTime = FieldValue.serverTimestamp();

        chatdetails.put("chatId", chat_id);
        chatdetails.put("isReply", isReply);
        chatdetails.put("replyOfMsg", replyOfMsgString);
        chatdetails.put("replyOfUser", replyOfUserString);
        chatdetails.put("repliedOfChatId", replyOfChatIDString);
        chatdetails.put("msgTime", msgTime);
        chatdetails.put("chatImage", chatImage);
        chatdetails.put("msgCategory", msgCategory);
        chatdetails.put("invitationTime", invitedTime);
        chatdetails.put("invitationTimeInString",invitationTimeInString);
        chatdetails.put("milestoneID", milestoneId);
        chatdetails.put("videoPosition", videoPosForSendInvite);
        chatdetails.put("videoStatusId", videoStatusId);


        reference = db.collection("Communities").document(community_id).collection("Chats").document(chat_id);


        reference.set(chatdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (isInvitation) {
                    collectionReference = db.collection("Communities").document(community_id).collection("Chats").document(chat_id).collection("invitedStudent");
                    documentReferenceForVideoStatus = db.collection("Communities").document(community_id).collection("Chats").document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId);
                    docRefVideoStatusForSuggMile = db.collection("Communities").document(community_id).collection("Chats").document(chat_id).collection("invitedVideoPlayingStatus").
                            document(videoStatusId).collection("SuggestedMilestone").document(videoStatusId);


                    btnSend.setVisibility(View.GONE);
                    btndoubtSms.setVisibility(View.VISIBLE);
                    saveInvitedStudentDetails();
                    saveInvitedVideoStatus();
                    msgEdt.setText("");
//                    msgEdt.setFocusable(false);
                    chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                    firestoreRecyclerChatAdapter.notifyDataSetChanged();
                    Log.e("Chat saved", "Chat saved");
                    msgCategory = "";
                    isDoubt = false;
                    isAnswer = false;
                    isInvitation = false;
                    isChat = true;
                    isReply = false;
                    edtTxtReplyLayout.setVisibility(View.GONE);
                } else {
                    msgEdt.setText("");
//                    msgEdt.setFocusable(false);
                    chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                    firestoreRecyclerChatAdapter.notifyDataSetChanged();
                    Log.e("Chat saved", "Chat saved");
                    msgCategory = "";
                    isDoubt = false;
                    isAnswer = false;
                    isInvitation = false;
                    isChat = true;
                    isReply = false;
                    edtTxtReplyLayout.setVisibility(View.GONE);
                }


                FieldValue msgTime = FieldValue.serverTimestamp();

                lastMsgReference = db.collection("Communities").document(community_id).collection("LastChat").document("last_chat");
                lastMsgReferenceCommunityAddress = db.collection("Communities").document(community_id);

                lastChatDetails.put("lastChat", chatMsg);
                lastChatDetails.put("community_id", communityId);
                lastChatDetails.put("community_name", communityName);
                lastChatDetails.put("community_image_url", communityLogo);
                lastChatDetails.put("lastChatTime", msgTime);

                lastMsgReference.set(lastChatDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("lastChat", "chatMsg");
                        Log.e("lastChatTime", String.valueOf(msgTime));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                lastMsgReferenceCommunityAddress.set(lastChatDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("lastChat", "chatMsg");
                        Log.e("lastChatTime", String.valueOf(msgTime));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                // For saving last chat

                db.collection("Communities").document(community_id).collection("LastChat").document("last_chat").update("lastChat", chatMsg);
                db.collection("Communities").document(community_id).collection("LastChat").document("last_chat").update("lastChatTime", msgTime);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Chat saved failed", e.toString());

                isDoubt = false;
                isAnswer = false;
                isInvitation = false;
                isChat = true;
                isReply = false;
                edtTxtReplyLayout.setVisibility(View.GONE);
            }
        });
        buttonPlus.setVisibility(View.VISIBLE);


    }

    private void saveInvitedVideoStatus() {
        invitedVideodetails.put("videoPlayingStatus", 00);
        invitedVideodetails.put("videoStatus", "pause");
        invitedVideodetails.put("liveUnliveStatus", "unlive");
        invitedVideodetails.put("previousBtn", "notClicked");
        invitedVideodetails.put("nextBtn", "notClicked");
        invitedVideodetails.put("seekFrwd", "notClicked");
        invitedVideodetails.put("seekBwrd", "notClicked");
        invitedVideodetails.put("backButton", "notClicked");
        invitedVideodetails.put("videoPosition", videoPosForSendInvite);
        invitedVideodetails.put("suggestedMile", "notClicked");
        invitedVideodetails.put("suggestedMileId", "");
        invitedVideodetails.put("videoStatusId", videoStatusId);
        documentReferenceForVideoStatus.set(invitedVideodetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        //for suggestedMilestone

        suggMileVideodetails.put("mileId", "");
        suggMileVideodetails.put("itemViewPos", "");

        docRefVideoStatusForSuggMile.set(suggMileVideodetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void saveInvitedStudentDetails() {

        String studentName = "";
        String studentId = "";
        for (int i = 0; i < invitedStudentId.size(); i++) {
            studentId = invitedStudentId.get(i);
            studentName = invitedStudentName.get(i);

            if (studentId.equals(studentUId)) {
                userType = "admin";
            } else {
                userType = "member";
            }

            invitedStudentDetails.put("invStudUsername", studentName);
            invitedStudentDetails.put("invStudId", studentId);
            invitedStudentDetails.put("userType", userType);
            invitedStudentDetails.put("status", "unlive");
            invitedStudentDetails.put("micStatus", "on");
            invitedStudentDetails.put("isSelected", "false");


            collectionReference.add(invitedStudentDetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override

                public void onSuccess(DocumentReference documentReference) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("Chat saved failed", e.toString());

                }
            });
        }

    }

    private void getChatList() {
        Query query = db.collection("Communities").document(community_id).collection("Chats").orderBy("msgTime", Query.Direction.ASCENDING);

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


                // checking message type like - doubt, normal chat, invitation, answer etc.
                if (model.getMsgType().equals("doubt")) {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_doubt_icon_)
                            .into(holder.chat_type);
                    holder.doubt_no.setVisibility(View.VISIBLE);
                    holder.chat_type.setVisibility(View.VISIBLE);
                    holder.doubt_layout.setVisibility(View.VISIBLE);

                    Log.e("doubt_weeight", String.valueOf(model.getDoubtWeight()));
                    String doubtWeight = String.valueOf(model.getDoubtWeight());
                    holder.doubt_no.setText(doubtWeight);


                } else {
                    holder.doubt_no.setVisibility(View.GONE);
                    holder.chat_type.setVisibility(View.GONE);
                    holder.doubt_layout.setVisibility(View.GONE);

                }
                if (model.getMsgType().equals("chat")) {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_baseline_sms_24)
                            .into(holder.chat_type);
                    holder.doubt_no.setVisibility(View.GONE);
                    holder.chat_type.setVisibility(View.GONE);
                    holder.doubt_layout.setVisibility(View.GONE);


                }else {

                }

                if (model.getMsgType().equals("answer")) {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_baseline_check_circle_outline_24)
                            .into(holder.chat_type);
                    holder.doubt_no.setVisibility(View.GONE);
                    holder.chat_type.setVisibility(View.GONE);
                    holder.doubt_layout.setVisibility(View.GONE);


                }else {

                }
                if (model.getMsgType().equals("invitation")) {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_keyboard_arrow_right_black_24dp)
                            .into(holder.chat_type);
                    holder.doubt_no.setVisibility(View.GONE);
                    holder.chat_type.setVisibility(View.GONE);
                    holder.doubt_layout.setVisibility(View.GONE);



                }else {


                }


                // reply messages
                if (!model.getReplyOfUser().equals("")) {
                    holder.replied_msg_card.setVisibility(View.VISIBLE);
                    holder.replied_by_user.setText(model.getReplyOfUser());
                    holder.replied_of_msg.setText(model.getReplyOfMsg());

                } else {
                    holder.replied_msg_card.setVisibility(View.GONE);
                }
                if (model.getMessage().equals("")) {
                    holder.chat.setVisibility(View.GONE);
                } else {
                    holder.chat.setVisibility(View.VISIBLE);
                }



                //for filter chat, image etc
                if (showInvitation) {
                    Log.e("showInvitation", String.valueOf(showInvitation));
                    if (model.getMsgType().equals("invitation")) {
                        holder.chat_layout.setVisibility(View.VISIBLE);
                        holder.chat.setVisibility(View.VISIBLE);
                        holder.incoming_chat_user.setVisibility(View.VISIBLE);
                        holder.chat_time.setVisibility(View.VISIBLE);
                        holder.doubt_layout.setVisibility(View.GONE);
                        holder.communityChatImage.setVisibility(View.VISIBLE);
                    } else {
                        holder.parent.setVisibility(View.GONE);

                    }

                } else if (showDoubt) {
                    Log.e("showDoubt", String.valueOf(showDoubt));

                    if (model.getMsgType().equals("doubt") || model.getMsgType().equals("answer")) {
                        holder.chat_layout.setVisibility(View.VISIBLE);
                        holder.chat.setVisibility(View.VISIBLE);
                        holder.incoming_chat_user.setVisibility(View.VISIBLE);
                        holder.chat_time.setVisibility(View.VISIBLE);
                        holder.doubt_no.setVisibility(View.VISIBLE);
                        holder.chat_type.setVisibility(View.VISIBLE);
                        holder.doubt_layout.setVisibility(View.VISIBLE);
                        holder.communityChatImage.setVisibility(View.VISIBLE);

                    } else {
                        holder.parent.setVisibility(View.GONE);
                        Log.e("No Doubts", "No Doubts");
                    }

                } else if (showImages) {
                    Log.e("showImages", String.valueOf(showImages));

                    if (model.getMsgCategory().equals("image")) {
                        holder.chat_layout.setVisibility(View.VISIBLE);
                        holder.chat.setVisibility(View.VISIBLE);
                        holder.incoming_chat_user.setVisibility(View.GONE);
                        holder.chat_time.setVisibility(View.VISIBLE);
                        holder.communityChatImage.setVisibility(View.VISIBLE);
                        holder.doubt_layout.setVisibility(View.GONE);

                    } else {
                        holder.parent.setVisibility(View.GONE);
                        Log.e("No Images", "No Images");
                    }

                } else if (showDoc) {
                    Log.e("showDoc", String.valueOf(showDoc));

                    if (model.getMsgCategory().equals("pdf")) {
                        holder.chat_layout.setVisibility(View.VISIBLE);
                        holder.chat.setVisibility(View.VISIBLE);
                        holder.incoming_chat_user.setVisibility(View.VISIBLE);
                        holder.chat_time.setVisibility(View.VISIBLE);
                        holder.doubt_layout.setVisibility(View.GONE);
                        holder.communityChatImage.setVisibility(View.VISIBLE);
                    } else {
                        holder.parent.setVisibility(View.GONE);
                        Log.e("No Images", "No Images");
                    }

                } else if (showAudio) {
                    Log.e("showAudio", String.valueOf(showAudio));
                    if (model.getMsgCategory().equals("audio")) {
                        holder.chat_layout.setVisibility(View.VISIBLE);
                        holder.chat.setVisibility(View.GONE);
                        holder.incoming_chat_user.setVisibility(View.VISIBLE);
                        holder.chat_time.setVisibility(View.VISIBLE);
                        holder.communityChatImage.setVisibility(View.VISIBLE);
                    } else {

                        holder.parent.setVisibility(View.GONE);
                        Log.e("No Images", "No Images");
                    }

                }



                // getting text chat message
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
                    holder.incoming_chat_user.setVisibility(View.GONE);
                    holder.incoming_chat_user.setText(model.getUserName());

                }
                if (!model.getUserImage().equals("")) {

                    Glide.with(getApplicationContext())
                            .load(model.getUserImage())
                            .into(holder.communityChatImage);
                } else {
                    Log.e("no image", "No image");
                }






                // attachment related chat like image, chat, pdf etc
                if (model.getMsgCategory().equals("image") || model.getMsgCategory().equals("audio") || model.getMsgCategory().equals("pdf")){

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

                    }else{
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
                    }else{
                        holder.pdf_icon.setVisibility(View.GONE);

                    }

                }else{
                    holder.pdf_icon.setVisibility(View.GONE);
                    holder.audio_linear.setVisibility(View.GONE);
                    holder.chat_image_card.setVisibility(View.GONE);
                    holder.chat_image.setVisibility(View.GONE);
                }



//                holder.audio_chat_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Uri uri = Uri.parse(model.getChatAudio());
//                        if (isAudioPlaying) {
//                            try {
//                                mediaPlayer.stop();
//                                isAudioPlaying = false;
//                                holder.audio_chat_animation.setVisibility(View.GONE);
//                                holder.audio_chat_pause_btn.setVisibility(View.GONE);
//                                holder.audio_chat_btn.setVisibility(View.VISIBLE);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            try {
//                                mediaPlayer = new MediaPlayer();
//                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                                mediaPlayer.setDataSource(CommunityChatPageActivity.this, uri);
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                                holder.audio_chat_animation.setVisibility(View.VISIBLE);
//                                isAudioPlaying = true;
//                                holder.audio_chat_pause_btn.setVisibility(View.VISIBLE);
//                                holder.audio_chat_btn.setVisibility(View.INVISIBLE);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    }
//                });
                holder.audio_chat_pause_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            mediaPlayer.stop();
                            isAudioPlaying = false;
//                            holder.audio_chat_animation.setVisibility(View.INVISIBLE);
                            holder.audio_chat_animation.pauseAnimation();
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
                            mediaPlayer.setDataSource(CommunityChatPageActivity.this, uri);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
//                            holder.audio_chat_animation.setVisibility(View.VISIBLE);
                            holder.audio_chat_animation.playAnimation();
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


                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommunityChatPageActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.chat_long_press_popup, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        AlertDialog alertDialog = dialogBuilder.create();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(alertDialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        alertDialog.getWindow().setAttributes(lp);

                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


                        TextView cancelBtn = dialogView.findViewById(R.id.no_button);
                        TextView deleteButton = dialogView.findViewById(R.id.delete_button);
                        View horizontalView = dialogView.findViewById(R.id.divider11);
                        TextView yesBtn = dialogView.findViewById(R.id.yes_button);
                        if (model.getUserId().equals(userId)) {
                            deleteButton.setVisibility(View.VISIBLE);
                            horizontalView.setVisibility(View.VISIBLE);
                        } else {
                            deleteButton.setVisibility(View.GONE);
                            horizontalView.setVisibility(View.GONE);

                        }


                        if (model.getMsgType().equals("doubt")) {
                            yesBtn.setText("Answer");
                            cancelBtn.setText("Raise Doubt");


                        } else if (model.getMsgType().equals("invitation")) {

                            yesBtn.setText("Join");
                            cancelBtn.setText("Share");


                        } else {
                            yesBtn.setText("Reply");
                            cancelBtn.setText("Cancel");
                        }

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                if (model.getMsgType().equals("doubt")) {
                                    doubt_weight = model.getDoubtWeight();

                                    int dw = doubt_weight + 1;

                                    db.collection("Communities").document(communityId).collection("Chats")
                                            .document(model.getChatId()).update("doubtWeight", dw);

                                    alertDialog.dismiss();



                                } else if (model.getMsgType().equals("invitation")) {
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    String shareBody = model.getMessage();

                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zeal");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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
                                    edtTxtReplyUser.setText(model.getUserName());
                                    edtTxtReplyUser.setMaxLines(1);
                                    edtTxtReply.setMaxLines(1);


                                    isAnswer = true;
                                    isChat = false;
                                    isReply = true;
                                    isInvitation = false;
                                    isDoubt = false;
                                } else if (model.getMsgType().equals("invitation")) {
                                    String invitationTime = model.getInvitationTime();

                                    Log.e("invitationTime", invitationTime);
                                    isReply = false;
                                    invitationId = model.getChatId();
                                    chatIdForVideo = model.getChatId();
                                    lastMilestoneId = model.getMilestoneID();
                                    videoStatusId = model.getVideoStatusId();
                                    String groupTime = model.getInvitationTime();

                                    timeString = model.getInvitationTimeInString();



                                    // time validation for the Group study

                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy").parse(invitationTime);
                                        Log.e("fetched time join", String.valueOf(date));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // for current time
//
                                    DateFormat formatterDateNow = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
                                    Date dateNow = new Date();
                                    String localTime = (formatterDateNow.format(dateNow));
                                    Date joinDateTime = null;
                                    try {
                                        joinDateTime = (Date)formatterDateNow.parse(localTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("current time", localTime);



                                    Log.e("current time join", localTime);
                                    Log.e("joinDateTime", String.valueOf(joinDateTime));


////
//                                    if (joinDateTime.equals(date) ) {
//                                        fetchStudentJoinedList(invitationId);
//                                    }else{
//                                        Toast.makeText(CommunityChatPageActivity.this, "Group study will start on time!", Toast.LENGTH_SHORT).show();
//                                    }

                                    fetchStudentJoinedList(invitationId);


                                } else if (model.getMsgType().equals("answer")) {
                                    edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                    edtTxtReplyUser.setText(model.getUserName());
                                    edtTxtReplyUser.setMaxLines(1);
                                    edtTxtReply.setMaxLines(1);
                                    isAnswer = false;
                                    isChat = true;
                                    isInvitation = false;
                                    isDoubt = false;
                                    isReply = true;
                                    isReplyOfAnswer = true;

                                } else {
                                    edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                    edtTxtReplyUser.setText(model.getUserName());
                                    edtTxtReplyUser.setMaxLines(1);
                                    edtTxtReply.setMaxLines(1);
                                    isAnswer = false;
                                    isChat = true;
                                    isInvitation = false;
                                    isDoubt = false;
                                    isReply = true;
                                }


                                if (isFilterOn.equals("yes")){
                                    edtTxtReplyLayout.setVisibility(View.GONE);
                                    messageEdt_layout.setVisibility(View.GONE);
                                }else{
                                    edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                    messageEdt_layout.setVisibility(View.VISIBLE);
                                    edtTxtReply.setText(model.getMessage());
                                    replyOfChatIDString = model.getChatId();
                                    replyOfMsgString = edtTxtReply.getText().toString();
                                    replyOfUserString = edtTxtReplyUser.getText().toString();
                                    Log.e("replyOfUserString", replyOfUserString);
                                    Log.e("replyOfMsgString", replyOfMsgString);
                                }



                                alertDialog.dismiss();
                                msgEdt.requestFocus();
                                msgEdt.setFocusable(true);
//                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.showSoftInput(msgEdt, InputMethodManager.SHOW_IMPLICIT);

                            }
                        });

                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                db.collection("Communities").document(communityId).collection("Chats")
                                        .document(model.getChatId()).delete();
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
                                Intent mileStonePlayer = new Intent(getApplicationContext(), PdfRenderActivity.class);
                                mileStonePlayer.putExtra("pdfLink", model.getChatImage());
                                startActivity(mileStonePlayer);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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


                if (model.getUserId().equals(studentUId)) {
                    holder.communityChatImage.setVisibility(View.GONE);

                } else {
                    holder.communityChatImage.setVisibility(View.VISIBLE);

                }


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
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };


//      jab bhi new chat add hoga ye code last wle chat pr leke chayega all members ko
        firestoreRecyclerChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                chat_recyclerview.smoothScrollToPosition(firestoreRecyclerChatAdapter.getItemCount());
            }
        });
        firestoreRecyclerChatAdapter.notifyDataSetChanged();
        chat_recyclerview.setAdapter(firestoreRecyclerChatAdapter);
        firestoreRecyclerChatAdapter.notifyDataSetChanged();


        // niche ke do lines sirf jo new message send krega usko last message tk lek jayenge
        chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
        firestoreRecyclerChatAdapter.notifyItemRangeChanged(0, firestoreRecyclerChatAdapter.getItemCount()-1);


    }


    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView incoming_chat_user, chat, chat_time, replied_by_user, replied_of_msg, doubt_no;
        ImageView communityChatImage, chat_type, chat_image, pdf_icon, audio_chat_btn, audio_chat_pause_btn;
        LinearLayoutCompat replied_msg_card;
        LinearLayout chat_layout, parent;
        CardView chat_image_card;
        ConstraintLayout audio_linear, doubt_layout;
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
            chat_layout = itemView.findViewById(R.id.chat_layout);
            parent = itemView.findViewById(R.id.parent);
            doubt_layout = itemView.findViewById(R.id.doubt_layout);
            chat_image_card = itemView.findViewById(R.id.chat_image_card);
            audio_chat_btn = itemView.findViewById(R.id.audio_chat_btn);
            audio_linear = itemView.findViewById(R.id.audio_linear);
            audio_chat_animation = itemView.findViewById(R.id.audio_chat_animation);
            doubt_no = itemView.findViewById(R.id.doubt_weight);
            audio_chat_pause_btn = itemView.findViewById(R.id.audio_chat_pause_btn);


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "enter");

        btnSend.setVisibility(View.VISIBLE);
        btndoubtSms.setVisibility(View.VISIBLE);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 3) {

                selectedImageUri = data.getData();
                msgCategory = "pdf";
                isImage = true;
                fileName = selectedImageUri.getLastPathSegment();
                Log.e("selectedImageUri", selectedImageUri.toString());
                Log.e("fileName", fileName);

                sendImage();
            } else {
                //            ---------Image URi-----------

                isImage = true;
                msgCategory = "image";
                selectedImageUri = data.getData();
                chat_send_image.setVisibility(View.VISIBLE);
                chat_send_image_close.setVisibility(View.VISIBLE);
                Log.e("selectedImageUri", selectedImageUri.toString());
                fileName = selectedImageUri.getLastPathSegment();
                chat_send_image.setImageURI(selectedImageUri);
                chat_send_image.setVisibility(View.VISIBLE);

            }
            buttonPlus.setVisibility(View.GONE);
            msgEdt.setText("");
            msgEdt.setEnabled(false);

        }
        btnSend.setVisibility(View.VISIBLE);
        btndoubtSms.setVisibility(View.VISIBLE);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.e("storageDir", "enter");


        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


//     ---------- camera capture in uri-----------

    private void dispatchTakePictureIntent() {
        Log.e("dispach", "enter");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(CommunityChatPageActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.e("createImageFile", "enter");

            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.chalksnboard.android.fileprovider",
                        photoFile);
                Log.e("photoFile", "enter");

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    class GetCourseDetailsDash extends AsyncTask<String, Void, String> {
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
            int versionCode = BuildConfig.VERSION_CODE;
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
                            subscriptionStatus = dataObj.getString("subscription_status");
                            Log.e("subscriptionStatus", subscriptionStatus);


                            String courseName = dataObj.getString("title");
                            String courseDesc = dataObj.getString("description");
                            courseDuration = dataObj.getString("duration");
                            courseOwnerName = dataObj.getJSONObject("owner_details").getString("owned_name");
                            courseOwnerQuali = dataObj.getJSONObject("owner_details").getString("owner_qualification");

                            if (subscriptionStatus.equals("subscribed")) {

                                Log.e("Subscription", "Subscribed");

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

                                        if (!mileStoneObj.getString("video_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(mileStoneObj.getString("video_link")));
                                        }
                                        if (!mileStoneObj.getString("duration").equals("1")) {
                                            mileStonesArrayList.add(mileStone);

                                        }
//                                        Log.e("Milestone Id", lastMilestoneId);
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

                                db = FirebaseFirestore.getInstance();
                                db.collection("Communities").document(communityId).collection("Chats")
                                        .document(chatIdForVideo).collection("invitedVideoPlayingStatus").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                                if (e != null) {

                                                }

                                                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                                                    isAdminLive = documentChange.getDocument().getData().get("liveUnliveStatus").toString();

                                                    Log.e("isAdminLive", isAdminLive);

                                                }
                                            }
                                        });
                                Log.e("chat videoStatus", videoStatusId);
                                if (studentUId.equals(admin) || isAdminLive.equals("live")) {
                                    Intent mileStonePlayer = new Intent(getApplicationContext(), GroupStudyVideoPlayerActivity.class);
                                    mileStonePlayer.putExtra("videoPosition", videoPos);
                                    mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                    mileStonePlayer.putExtra("id", community_id);
                                    mileStonePlayer.putExtra("chapter_id", chapter_id);
                                    Log.e("milearrayCommunitylive", String.valueOf(mileStonesArrayList));
                                    mileStonePlayer.putExtra("chatId", chatIdForVideo);
                                    mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                                    mileStonePlayer.putExtra("community_name", communityName);
                                    mileStonePlayer.putExtra("community_logo", communityLogo);
                                    mileStonePlayer.putExtra("isAdmin", "true");
                                    mileStonePlayer.putExtra("userImage", profileImageUrlString);
                                    mileStonePlayer.putExtra("fromActivity", "chatActivity");

                                    startActivity(mileStonePlayer);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                } else if (isAdminLive.equals("live") || studentUId.equals(admin)) {
                                    Intent mileStonePlayer = new Intent(getApplicationContext(), GroupStudyVideoPlayerActivity.class);
                                    mileStonePlayer.putExtra("videoPosition", videoPos);
                                    mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                    Log.e("milearrayCommunitylive", String.valueOf(mileStonesArrayList));
                                    mileStonePlayer.putExtra("id", community_id);
                                    mileStonePlayer.putExtra("chapter_id", chapter_id);
                                    mileStonePlayer.putExtra("chatId", chatIdForVideo);
                                    mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                                    mileStonePlayer.putExtra("community_name", communityName);
                                    mileStonePlayer.putExtra("community_logo", communityLogo);
                                    mileStonePlayer.putExtra("isAdmin", "false");
                                    mileStonePlayer.putExtra("userImage", profileImageUrlString);
                                    mileStonePlayer.putExtra("fromActivity", "chatActivity");
                                    startActivity(mileStonePlayer);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                } else {
                                    Intent mileStonePlayer = new Intent(getApplicationContext(), UserHoldingScreenActivity.class);
                                    mileStonePlayer.putExtra("videoPosition", videoPos);
                                    mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                    mileStonePlayer.putExtra("id", community_id);
                                    mileStonePlayer.putExtra("chapter_id", chapter_id);
                                    mileStonePlayer.putExtra("chatId", chatIdForVideo);
                                    mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                                    mileStonePlayer.putExtra("community_name", communityName);
                                    mileStonePlayer.putExtra("community_logo", communityLogo);
                                    mileStonePlayer.putExtra("isAdmin", "false");
                                    mileStonePlayer.putExtra("userImage", profileImageUrlString);
                                    mileStonePlayer.putExtra("timeString",timeString);
                                    Log.e("milearrayCommunitylive", String.valueOf(mileStonesArrayList));
                                    startActivity(mileStonePlayer);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                }


                            } else if (subscriptionStatus.equals("unsubscribed")) {
                                Log.e("Subscription", "unSubscribed");

                                Toast.makeText(getApplicationContext(), "Subscribe this Course Again!..", Toast.LENGTH_LONG).show();

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
                                        Log.e("userNameString", userNameString);
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


    private void fetchStudentJoinedList(String invitationId) {

        Log.e("community Id", community_id);
        db.collection("Communities").document(community_id).collection("Chats").document(invitationId).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());


                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
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
                        if (studentUId.equals(invitedStuId)) {
                            Log.e("studentIdloop", invitedStuId);
                            firestoreUserId = invitedStuId;

                            break;

                        } else {
                            firestoreUserId = "";
                        }
                    }
                    if (studentUId.equals(firestoreUserId)) {

                        new GetCourseDetailsDash().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    } else {
                        Toast.makeText(getApplicationContext(), "This Invitation is not for you!...", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "This Invitation is not for you!...", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.allChat:
                showInvitation = false;
                showDoubt = false;
                showAudio = false;
                showImages = false;
                showDoc = false;
                isFilterOn = "no";
                edtTxtReplyLayout.setVisibility(View.GONE);
                messageEdt_layout.setVisibility(View.VISIBLE);
                getChatList();
                firestoreRecyclerChatAdapter.startListening();
                return true;


            case R.id.invitation:
                showInvitation = true;
                showDoubt = false;
                showAudio = false;
                showImages = false;
                showDoc = false;
                isFilterOn = "yes";
                edtTxtReplyLayout.setVisibility(View.GONE);
                messageEdt_layout.setVisibility(View.GONE);
                getChatList();
                firestoreRecyclerChatAdapter.startListening();
                return true;


            case R.id.doubt:
                showInvitation = false;
                showDoubt = true;
                showAudio = false;
                showImages = false;
                showDoc = false;
                isFilterOn = "yes";
                edtTxtReplyLayout.setVisibility(View.GONE);
                messageEdt_layout.setVisibility(View.GONE);
                getChatList();
                firestoreRecyclerChatAdapter.startListening();
                return true;


            case R.id.images:
                showInvitation = false;
                showDoubt = false;
                showAudio = false;
                showImages = true;
                showDoc = false;
                isFilterOn = "yes";
                edtTxtReplyLayout.setVisibility(View.GONE);
                messageEdt_layout.setVisibility(View.GONE);
                getChatList();
                firestoreRecyclerChatAdapter.startListening();
                return true;


            case R.id.doc:
                showInvitation = false;
                showDoubt = false;
                showAudio = false;
                showImages = false;
                showDoc = true;
                isFilterOn = "yes";
                edtTxtReplyLayout.setVisibility(View.GONE);
                messageEdt_layout.setVisibility(View.GONE);
                getChatList();
                firestoreRecyclerChatAdapter.startListening();
                return true;


            case R.id.audio:
                showInvitation = false;
                showDoubt = false;
                showAudio = true;
                showImages = false;
                showDoc = false;
                isFilterOn = "yes";
                edtTxtReplyLayout.setVisibility(View.GONE);
                messageEdt_layout.setVisibility(View.GONE);
                getChatList();
                firestoreRecyclerChatAdapter.startListening();
                return true;


            case R.id.group_details:
                Intent gotToCommunityDetails = new Intent(CommunityChatPageActivity.this, CommunityDetailsActivity.class);
                gotToCommunityDetails.putExtra("community_id", communityId);
                gotToCommunityDetails.putExtra("community_name", communityName);
                gotToCommunityDetails.putExtra("community_logo", communityLogo);
                gotToCommunityDetails.putExtra("userNameString", userNameString);
                startActivity(gotToCommunityDetails);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        if (isAttachmentLayoutOpen) {
            attachment_file_layout.setVisibility(View.GONE);
            isAttachmentLayoutOpen = false;
        }else if (isFilterOn.equals("yes")){
            showInvitation = false;
            showDoubt = false;
            showAudio = false;
            showImages = false;
            showDoc = false;
            isFilterOn = "no";
            edtTxtReplyLayout.setVisibility(View.GONE);
            messageEdt_layout.setVisibility(View.VISIBLE);
            getChatList();
            firestoreRecyclerChatAdapter.startListening();
        }

        else if (from_activity.equals("communityChatPage") || from_activity.equals("groupPlayer")) {
            Intent community = new Intent(CommunityChatPageActivity.this, CommunitiesListActivity.class);
            community.putExtra("id", "id");
            community.putExtra("fromActivity", "dashboard");
            startActivity(community);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (from_activity.equals("course_details") || from_activity.equals("courseDetails")) {
            Intent community = new Intent(CommunityChatPageActivity.this, CourseDetailsActivity.class);
            community.putExtra("id", communityId);
            community.putExtra("image", communityLogo);
            community.putExtra("fromActivity","communityChatPage");
            startActivity(community);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            super.onBackPressed();
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
        new AlertDialog.Builder(CommunityChatPageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    class GetStudentSubscriptionStatus extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(CommunityChatPageActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(CommunityChatPageActivity.this);
            int versionCode;


            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(CommunityChatPageActivity.this.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versionCode = pInfo.versionCode;
            Log.e("versionCode", String.valueOf(versionCode));


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

                            //Setting course Details for subscribed course--------------------------
                            subscriptionStatus = dataObj.getString("subscription_status");

                            if (subscriptionStatus.equals("subscribed")) {
                                attachment_file_layout.setVisibility(View.GONE);
                                Intent intent = new Intent(CommunityChatPageActivity.this, SelectMilestoneInvitationActivity.class);
                                intent.putExtra("community_id", community_id);
                                intent.putExtra("community_name", communityName);
                                intent.putExtra("community_logo", communityLogo);
                                intent.putExtra("userNameString", userNameString);
                                intent.putExtra("fromActivity","communityChatPage");
                                Log.e("community_logo", communityLogo);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                            } else if (subscriptionStatus.equals("unsubscribed")) {
                                Toast.makeText(CommunityChatPageActivity.this, "Please subscribe " + communityName + " for sending Invitation!..", Toast.LENGTH_LONG).show();

                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommunityChatPageActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = (LayoutInflater) CommunityChatPageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                                    ((Activity) CommunityChatPageActivity.this).finish();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(CommunityChatPageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(CommunityChatPageActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
        firestoreRecyclerChatAdapter.startListening();
        firestoreRecyclerChatAdapter.notifyDataSetChanged();
//        getChatList();


    }
}