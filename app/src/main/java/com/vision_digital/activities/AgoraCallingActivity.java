package com.vision_digital.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.vision_digital.R;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
// Java

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.IRtcEngineEventHandler;
// Java




public class AgoraCallingActivity extends AppCompatActivity {

    // Java
    TextView txt;
    private int mRole;
    private ImageView btn_call, btn_end;
    private boolean mMuted = false;

    // Fill the App ID of your project generated on Agora Console.
    private String appId = "";
    // Fill the channel name.
    private String channelName = "Test";
    // Fill the temp token generated on Agora Console.
    private String token = "tokencnb";
    private RtcEngine mRtcEngine;

    private String uid;
    private FirebaseUser user;
    private int sid;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt.setText("Joined");
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
                    Toast.makeText(AgoraCallingActivity.this, "Session Ended", Toast.LENGTH_SHORT).show();
                    finish();
                    // onRemoteUserLeft();

                }
            });
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agora_calling);
        btn_end = findViewById(R.id.btn_end);
        btn_call  = findViewById(R.id.btn_call);

        txt = findViewById(R.id.txt);

        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        sid = studDetails.getInt("sid", 0);

        mRole = Constants.CLIENT_ROLE_BROADCASTER;
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)) {
                    initEngineAndJoinChannel();
                }
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRtcEngine.leaveChannel();
                txt.setText("Channel Leaved");
                Log.e("channel leaved","channel leaved");
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
    protected void onDestroy() {
        super.onDestroy();
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setChannelProfile();
        setClientRole();
        joinChannel();

    }

    private void joinChannel() {

        // For SDKs earlier than v3.0.0, call this method to enable interoperability between the Native SDK and the Web SDK if the Web SDK is in the channel. As of v3.0.0, the Native SDK enables the interoperability with the Web SDK by default.
        mRtcEngine.enableWebSdkInteroperability(true);

        // Join a channel with a token.


        mRtcEngine.joinChannel(getString(R.string.agora_access_token), channelName, "Extra Optional Data", 0);
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
        mRtcEngine.setChannelProfile(Constants.CLIENT_ROLE_BROADCASTER);
    }

    private void setClientRole() {
        mRtcEngine.setClientRole(mRole);
    }


}

