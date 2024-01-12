package com.vision_digital.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.RelativeLayout;
import android.widget.Toast;


import com.vision_digital.R;
import com.vision_digital.model.suggestionTxtMsgs.SuggestionTxtMsg;
import com.vision_digital.model.suggestionTxtMsgs.SuggestionTxtMsgsViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class LiveVideoPlayerActivity extends AppCompatActivity {

    View mPlayerContentView;

    //Keep sreen on--------------------------------
    protected PowerManager.WakeLock mWakeLock;

    //Query----------------------------------------------
    CardView keypadBtn, msgBar;
    boolean keypadOn = false, keypadBtnOn = true;
    EditText msgContent;
    RecyclerView querySuggestionList;
    FirestoreRecyclerAdapter queriesAdapter;

    private static String APPLICATION_ID = "";

    String queries;
    ImageButton postQuery;
    String classCode = "";
    String schoolId = "";

    ProgressDialog dialog;

    private int mRole;

    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora", "Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                    setVideoConfig();
                }
            });
        }

        @Override
        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of the host is received and decoded after the host successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora", "First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                    dialog.dismiss();
                    SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
                    classCode = studDetails.getString("qualification", "NO_CLASS");
                    schoolId = studDetails.getString("schoolId", "NO_SCHOOL");
                    Map<String, Object> joineddetails = new HashMap<>();
                    joineddetails.put("class", classCode + "th");
                    joineddetails.put("joinedAt", FieldValue.serverTimestamp());
                    joineddetails.put("id", studDetails.getInt("sid", 0));
                    joineddetails.put("mobile", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    joineddetails.put("name", studDetails.getString("profileName", "NO_NAME"));
                    joineddetails.put("email", studDetails.getString("email", "NO_EMAIL"));
                    joineddetails.put("status", "Watching");

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("liveClasses").document(APPLICATION_ID).collection("joined_students").document(String.valueOf(studDetails.getInt("sid", 0))).set(joineddetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
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
                    Log.i("agora", "User offline, uid: " + (uid & 0xFFFFFFFFL));
                    dialog.hide();
                    Toast.makeText(LiveVideoPlayerActivity.this, "Session Ended", Toast.LENGTH_SHORT).show();
                    finish();
                    // onRemoteUserLeft();

                }
            });
        }
    };

//    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
//        @Override
//        public void onStateChange(PlayerState playerState) {
////            if (mPlayerStatusTextView != null)
////                mPlayerStatusTextView.setText("Status: " + playerState);
//            Log.e("Status", String.valueOf(playerState));
//            switch (playerState.toString()) {
//                case "BUFFERING":
//                    dialog.hide();
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setMessage("Buffering");
//                    dialog.show();
//                    break;
//                case "LOADING":
//                    dialog.hide();
//                    dialog.setMessage("Loading");
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.show();
//                    break;
//                case "PLAYING":
//                    dialog.dismiss();
//                    SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
//                    classCode = studDetails.getString("qualification", "NO_CLASS");
//                    schoolId = studDetails.getString("schoolId","NO_SCHOOL");
//                    Map<String, Object> joineddetails = new HashMap<>();
//                    joineddetails.put("class", classCode + "th");
//                    joineddetails.put("joinedAt", FieldValue.serverTimestamp());
//                    joineddetails.put("id", studDetails.getInt("sid", 0));
//                    joineddetails.put("mobile", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
//                    joineddetails.put("name", studDetails.getString("profileName", "NO_NAME"));
//                    joineddetails.put("email", studDetails.getString("email", "NO_EMAIL"));
//                    joineddetails.put("status", "Watching");
//
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    db.collection("schools").document(schoolId).collection("live_classes").document(APPLICATION_ID).collection("joined_students").document(String.valueOf(studDetails.getInt("sid", 0))).set(joineddetails).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                        }
//                    });
//                    break;
//                case "COMPLETED":
//                    dialog.hide();
//                    Toast.makeText(LiveVideoPlayerActivity.this, "Session Ended", Toast.LENGTH_SHORT).show();
//                    finish();
//                    break;
//            }
//        }
//
//        @Override
//        public void onBroadcastLoaded(boolean live, int width, int height) {
//            Log.e("live", String.valueOf(live));
//            if(!live){
//                dialog.hide();
//                Toast.makeText(LiveVideoPlayerActivity.this, "Session has been Ended.", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    };

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live_video_player);

        querySuggestionList = findViewById(R.id.querySuggestionList);


        //Keep display on-----------------------------------------
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Screen waked up");
        this.mWakeLock.acquire();

        dialog = new ProgressDialog(LiveVideoPlayerActivity.this);

        APPLICATION_ID = getIntent().getStringExtra("ApplicationID");
        mRole = Constants.CLIENT_ROLE_AUDIENCE;

        Log.e("ApplicationID", APPLICATION_ID);
        initEngineAndJoinChannel();

        mPlayerContentView = findViewById(R.id.PlayerContentView);
        //Query posting-------------------------------------------
        mPlayerContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keypadBtnOn) {
                    keypadBtnOn = false;
                    keypadOn = false;
                    msgBar.setVisibility(View.GONE);
                    keypadBtn.setVisibility(View.GONE);
                    querySuggestionList.setVisibility(View.GONE);
                } else {
                    keypadBtnOn = true;
                    keypadBtn.setVisibility(View.VISIBLE);
                    querySuggestionList.scrollToPosition(0);
                    querySuggestionList.setVisibility(View.VISIBLE);
                }
            }
        });
        msgContent = findViewById(R.id.textMsgEt);
        msgBar = findViewById(R.id.msgBarBottom);
        keypadBtn = findViewById(R.id.keypadBtn);
        keypadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keypadOn) {
                    msgBar.setVisibility(View.GONE);
                    keypadOn = false;
                } else {
                    msgContent.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    msgBar.setVisibility(View.VISIBLE);
                    keypadOn = true;
                }
            }
        });

        postQuery = findViewById(R.id.sendBtn);
        postQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
                classCode = studDetails.getString("qualification", "NO_CLASS");

                queries = msgContent.getText().toString().trim();
                if (!queries.equals("")) {
                    Map<String, Object> msgdetails = new HashMap<>();
                    msgdetails.put("msgText", queries);
                    msgdetails.put("msgTimestamp", FieldValue.serverTimestamp());
                    msgdetails.put("stdId", studDetails.getInt("sid", 0));
                    msgdetails.put("stdImage", "");
                    msgdetails.put("stdName", studDetails.getString("profileName", "NO_NAME"));

                    msgContent.setText("");
                    msgBar.setVisibility(View.GONE);
                    keypadOn = false;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("liveClasses").document(APPLICATION_ID).collection("messages").add(msgdetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(LiveVideoPlayerActivity.this, "Please wait.. Your Query has been sent. Teacher will answer soon.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(LiveVideoPlayerActivity.this, "Please type your query.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Firestore work-----------------------------------------------

        Query query = FirebaseFirestore.getInstance().collection("suggestionsForQueries").orderBy("count", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SuggestionTxtMsg> options = new FirestoreRecyclerOptions.Builder<SuggestionTxtMsg>().setQuery(query, SuggestionTxtMsg.class).build();
        queriesAdapter = new FirestoreRecyclerAdapter<SuggestionTxtMsg, SuggestionTxtMsgsViewHolder>(options) {
            @NonNull
            @Override
            public SuggestionTxtMsgsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestions_for_query, parent, false);

                return new SuggestionTxtMsgsViewHolder(mView);
            }

            @Override
            protected void onBindViewHolder(@NonNull SuggestionTxtMsgsViewHolder holder, int position, @NonNull final SuggestionTxtMsg model) {

                holder.msgContet.setText(model.getMsgContent());
                holder.msgContet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
                        classCode = studDetails.getString("qualification", "NO_CLASS");

                        queries = model.getMsgContent();
                        if (!queries.equals("")) {
                            Map<String, Object> msgdetails = new HashMap<>();
                            msgdetails.put("msgText", queries);
                            msgdetails.put("msgTimestamp", FieldValue.serverTimestamp());
                            msgdetails.put("stdId", studDetails.getInt("sid", 0));
                            msgdetails.put("stdImage", "");
                            msgdetails.put("stdName", studDetails.getString("profileName", "NO_NAME"));

                            msgBar.setVisibility(View.GONE);
                            querySuggestionList.setVisibility(View.GONE);
                            keypadBtn.setVisibility(View.GONE);
                            keypadBtnOn=false;
                            keypadOn = false;
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("suggestionsForQueries").document(model.getKey()).update("count",model.getCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Successfully Increaed priority
                                }
                            });
                            db.collection("liveClasses").document(APPLICATION_ID).collection("messages").add(msgdetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(LiveVideoPlayerActivity.this, "Please wait.. Your Query has been sent. Teacher will answer soon.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(LiveVideoPlayerActivity.this, "Please type your query.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };


//        querySuggestionList.setHasFixedSize(true);
        querySuggestionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        querySuggestionList.setAdapter(queriesAdapter);


    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setChannelProfile();
        setClientRole();
        joinChannel();

    }

    // Initialize the RtcEngine object.
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);

        } catch (Exception e) {
            Log.e("TAG", Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setChannelProfile() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
    }

    private void setClientRole() {
        mRtcEngine.setClientRole(mRole);
    }

    private void setupRemoteVideo(int uid) {
        RelativeLayout mRemoteContainer;
        mRemoteContainer = findViewById(R.id.PlayerContentView);
        SurfaceView mRemoteView;

        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        // Set the remote video view.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    @Override
    protected void onStart() {
        super.onStart();
        queriesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        queriesAdapter.stopListening();


    }


    @Override
    protected void onPause() {

        mRtcEngine.leaveChannel();
        mWakeLock.release();
//        mOkHttpClient.dispatcher().cancelAll();
//        mVideoSurface = null;
//
//        if (mBroadcastPlayer != null)
//            mBroadcastPlayer.close();
//        mBroadcastPlayer = null;

        if (!classCode.equals("")) {
            Map<String, Object> joineddetails = new HashMap<>();
            joineddetails.put("status", "Leaved");

            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("liveClasses").document(APPLICATION_ID).collection("joined_students").document(String.valueOf(studDetails.getInt("sid", 0))).set(joineddetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
        super.onPause();
    }

    private void setVideoConfig() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_840x480, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30, VideoEncoderConfiguration.STANDARD_BITRATE, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE));
    }

    @Override
    protected void onResume() {

//        mVideoSurface = findViewById(R.id.VideoSurfaceView);
//        mPlayerStatusTextView.setText("Loading latest broadcast");
        mWakeLock.acquire();
        initEngineAndJoinChannel();
        super.onResume();
//        getLatestResourceUri();
    }

//    void getLatestResourceUri() {
//        Request request = new Request.Builder()
//                .url("https://api.bambuser.com/broadcasts/"+BROADCAST_ID)
//                .addHeader("Accept", "application/vnd.bambuser.v1+json")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .get()
//                .build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(final Call call, final IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("ERROR",""+e);
////                        if (mPlayerStatusTextView != null)
////                            mPlayerStatusTextView.setText("Http exception: " + e);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(final Call call, final Response response) throws IOException {
//                String body = response.body().string();
//                String resourceUri = null;
//                try {
//                    JSONObject json = new JSONObject(body);
////                    JSONArray results = json.getJSONArray("results");
////                    JSONObject latestBroadcast = results.optJSONObject(0);
//                    resourceUri = json.getString("resourceUri");
//                    Log.e("resourceURI",resourceUri);
//                } catch (Exception ignored) {
//                    Log.e("resourceURI",""+ignored);
//                }
//                final String uri = resourceUri;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initPlayer(uri);
//                    }
//                });
//            }
//        });
//    }

    @Override
    public void onBackPressed() {

        mRtcEngine.leaveChannel();
        RtcEngine.destroy();
        if (!classCode.equals("")) {
            Map<String, Object> joineddetails = new HashMap<>();
            joineddetails.put("status", "Leaved");

            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("liveClasses").document(APPLICATION_ID).collection("joined_students").document(String.valueOf(studDetails.getInt("sid", 0))).set(joineddetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mWakeLock.release();
        if (!classCode.equals("")) {
            Map<String, Object> joineddetails = new HashMap<>();
            joineddetails.put("status", "Leaved");

            SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("schools").document(schoolId).collection("live_classes").document(APPLICATION_ID).collection("joined_students").document(String.valueOf(studDetails.getInt("sid", 0))).set(joineddetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
        super.onDestroy();
    }

    private void joinChannel() {

        // For SDKs earlier than v3.0.0, call this method to enable interoperability between the Native SDK and the Web SDK if the Web SDK is in the channel. As of v3.0.0, the Native SDK enables the interoperability with the Web SDK by default.
        mRtcEngine.enableWebSdkInteroperability(true);

        // Join a channel with a token.
        String mRoomName;
        mRoomName = APPLICATION_ID;
        mRtcEngine.joinChannel(getString(R.string.agora_access_token), mRoomName, "Extra Optional Data", 0);
    }
//    void initPlayer(String resourceUri) {
//        if (resourceUri == null) {
////            if (mPlayerStatusTextView != null)
////                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
//            return;
//        }
//        if (mVideoSurface == null) {
//            // UI no longer active
//            return;
//        }
//
//        if (mBroadcastPlayer != null)
//            mBroadcastPlayer.close();
//        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
//        mBroadcastPlayer.setSurfaceView(mVideoSurface);
//        mBroadcastPlayer.load();
//    }
}
