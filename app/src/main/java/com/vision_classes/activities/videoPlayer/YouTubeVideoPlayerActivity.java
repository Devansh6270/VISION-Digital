package com.vision_classes.activities.videoPlayer;

import static com.vision_classes.activities.DashboardActivity.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.vision_classes.R;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.model.chats.Messages;
import com.vision_classes.model.chats.MessagesAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class YouTubeVideoPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    String videoId;
    String videoTitle;
    String videoDescription;

    TextView  videoTitleText ,  toolBarText ;
    WebView videoDescriptionText;
    ImageView backButton;

    CardView toolBarPlayer;

    FloatingActionButton chatBtn;





    private RecyclerView chat_recyclerview;
    private EditText messageEditText;
    private ImageView sendButton;
    LinearLayout chattingLayout;

    ImageView backBtn;

    MessagesAdapter adapter;
    ArrayList<Messages> messages;

    String senderRoom = "";
    String receiverRoom = "";
    Boolean isClicked=false;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_video_player);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //        To Stop user from video recording or taking screen shot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        videoId="";
        videoId=getIntent().getStringExtra("videoId");
        videoDescription=getIntent().getStringExtra("videoDescription");
        videoTitle=getIntent().getStringExtra("videoTitle");

        Log.e("Video Id",videoId);

        videoTitleText = findViewById(R.id.videoTitle);
        videoDescriptionText = findViewById(R.id.videoDescription);
        toolBarText=findViewById(R.id.videoTitleToolbar);
        backButton=findViewById(R.id.backBtn);
        toolBarPlayer=findViewById(R.id.toolbarPlayer);
        chatBtn=findViewById(R.id.chatFloatBtn);
        chattingLayout=findViewById(R.id.chattingLayout);

        videoTitleText.setText(videoTitle);
//        videoDescriptionText.setText(videoDescription);
        videoDescriptionText.loadDataWithBaseURL(null, videoDescription, "text/html", "UTF-8", null);

        toolBarText.setText(videoTitle);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        chattingLayout.setVisibility(View.GONE);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(YouTubeVideoPlayerActivity.this, ChatActivity.class);
//                i.putExtra("videoId",videoId);
//                startActivity(i);
                if(isClicked){
                    chattingLayout.setVisibility(View.GONE);
                    videoDescriptionText.setVisibility(View.VISIBLE);
                    isClicked=false;

                }else {
                    chattingLayout.setVisibility(View.VISIBLE);
                    videoDescriptionText.setVisibility(View.INVISIBLE);
                    isClicked=true;
                }

            }
        });


        View customPlayerUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_ui_player);

//        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
//            @Override
//            public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
//
//                WebView player = (WebView) youTubePlayer;
//
//                player.clearCache(true); //we will need to reload www-player.css later
//                player.setWebViewClient(new WebViewClient() {
//
//                    private int loadCount;
//                    private boolean injected;
//
//                    @Override
//                    public WebResourceResponse shouldInterceptRequest (final WebView view, WebResourceRequest wrr) {
//
//                        String url = wrr.getUrl().toString();
//                        if(url.contains("youtube.com") && url.contains("www-player.css") ) { // find iframe player css file
//                            WebResourceResponse resp = doInject(url);
//                            if (resp != null) {
//                                injected = true;
//                                return resp;
//                            }
//                        }
//                        return super.shouldInterceptRequest(view, wrr);
//                    }
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        if(!injected && loadCount++ <3) // prevent loop
//                            view.reload(); // we need reload because we set WebViewClient
//                        // after WebView started loading the iframe
//                        // only once
//                    }
//
//                    private WebResourceResponse doInject(String url){
//                        try {
//                            Request request = new Request.Builder()
//                                    .url(url)
//                                    .build();
//
//                            Response response =
//                                    new OkHttpClient()
//                                            .newCall(request)
//                                            .execute();
//
//                            // inject code that hides the buttons
//                            assert response.body() != null;
//                            String fin = response
//                                    .body()
//                                    .string()
//                                    +" .ytp-chrome-top-buttons {display: none !important;}";
//
//                            return new WebResourceResponse(
//                                    "text/css",
//                                    "UTF-8",
//                                    new ByteArrayInputStream(fin.getBytes())
//                            );
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                        return null;
//                    }
//                });
//            }
//        });



        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController(YouTubeVideoPlayerActivity.this,
                        customPlayerUi, youTubePlayer, youTubePlayerView,YouTubeVideoPlayerActivity.this);
                youTubePlayer.addListener(customPlayerUiController);
                //   String videoId = "JbxA1XrLel4";
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        //  VideoIdsProvider.getNextVideoId(), 0f
                        videoId, 0f
                );
            }
        };

        // disable web ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().autoplay(0).ivLoadPolicy(0)
                .fullscreen(0).ccLoadPolicy(0).controls(0).modestBranding(1).rel(0).build();

        youTubePlayerView.initialize(listener, options);


        // Chatting
//        SharedPreferences sp = getSharedPreferences("CNB",MODE_PRIVATE);
//      String userName=  sp.getString("profileName","UnKnown User");
//        Log.e("UserName",userName);
        senderRoom =  videoId;
        receiverRoom = videoId;

        chat_recyclerview = findViewById(R.id.chat_recyclerview);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendBtn);


        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        messages = new ArrayList<>();
        adapter= new MessagesAdapter(this,messages,senderRoom,receiverRoom);
        chat_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        chat_recyclerview.setAdapter(adapter);

        database=FirebaseDatabase.getInstance();





        database=FirebaseDatabase.getInstance();

        if(!videoId.equals("master.mpd")){
            database.getReference().child("chats")
                    //.child(senderRoom)
                    .child(videoId)
                    .child("messages")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            messages.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                Messages message =snapshot1.getValue(Messages.class);

                                message.setMessageId(snapshot1.getKey());

                                messages.add(message);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }else{
            chatBtn.setVisibility(View.GONE);
        }



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                //  Date date = new Date();
                String dateTime="";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");


                    LocalDateTime now = LocalDateTime.now();
                    dateTime=dtf.format(now);

                }

                Messages message = new Messages(messageText,String.valueOf(sid),dateTime,"Nihal");
                messageEditText.setText("");


                String randamKey = database.getReference().push().getKey();

                HashMap<String ,Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime",dateTime);

                database.getReference().child("chats").child(videoId).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(videoId).updateChildren(lastMsgObj);


                database.getReference().child("chats")
                        .child(videoId)
                        .child("messages")
                        .child(randamKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                database.getReference().child("chats")
                                        .child(videoId)
                                        .child("messages")
                                        .child(randamKey)
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });


                            }
                        });
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoId="";
    }

    //    private void injectCustomCSS() {
//        WebView player = (WebView) youTubePlayerView;
//
//        player.clearCache(true);
//        player.setWebViewClient(new WebViewClient() {
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                String url = request.getUrl().toString();
//                if (url.contains("youtube.com") && url.contains("www-player.css")) {
//                    WebResourceResponse response = doInject(url);
//                    if (response != null) {
//                        return response;
//                    }
//                }
//                return super.shouldInterceptRequest(view, request);
//            }
//        });
//    }
//
//    private WebResourceResponse doInject(String url) {
//        try {
//            // Fetch the original www-player.css file from YouTube
//            OkHttpClient client = new OkHttpClient();
//            com.squareup.okhttp.Request request = new Request.Builder().url(url).build();
//            Response response = client.newCall(request).execute();
//
//            if (response.isSuccessful()) {
//                String originalCSS = response.body().string();
//
//                // Inject custom CSS to hide title (assuming the class names are correct)
//                String customCSS = originalCSS + " .ytp-title { display: none !important; }";
//
//                return new WebResourceResponse("text/css", "UTF-8", new ByteArrayInputStream(customCSS.getBytes()));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//}

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.matchParent();
            toolBarPlayer.setVisibility(View.GONE);
            chatBtn.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.wrapContent();
            toolBarPlayer.setVisibility(View.VISIBLE);
            chatBtn.setVisibility(View.VISIBLE);
        }
    }


}