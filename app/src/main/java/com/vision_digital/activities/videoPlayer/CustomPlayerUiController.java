package com.vision_digital.activities.videoPlayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils.FadeViewHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBarListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vision_digital.R;


class CustomPlayerUiController extends AbstractYouTubePlayerListener {
    private final YouTubePlayerTracker playerTracker;
    private final Context context;
    private final YouTubePlayer youTubePlayer;
    private final YouTubePlayerView youTubePlayerView;
    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    private View progressbar;
    private TextView videoCurrentTimeTextView;
    private TextView videoDurationTextView;
    private boolean fullscreen = false;
    private Activity activity;
    private static final int SKIP_OFFSET = 10;

    ImageView skipPreviousButton ,skipNextButton;

    Handler handler;
    Runnable hideUiRunnable;

    CustomPlayerUiController(Context context, View customPlayerUi, YouTubePlayer youTubePlayer, YouTubePlayerView youTubePlayerView, Activity activity) {
        this.context = context;
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayerView = youTubePlayerView;
        this.activity = activity;

        playerTracker = new YouTubePlayerTracker();
        youTubePlayer.addListener(playerTracker);

        initViews(customPlayerUi);
    }

    private void initViews(View playerUi) {
        panel = playerUi.findViewById(R.id.panel);
        progressbar = playerUi.findViewById(R.id.progressbar);
        videoCurrentTimeTextView = playerUi.findViewById(R.id.video_current_time);
        videoDurationTextView = playerUi.findViewById(R.id.video_duration);
        skipPreviousButton=playerUi.findViewById(R.id.skip_previous_button);
        skipNextButton=playerUi.findViewById(R.id.skip_next_button);
        YouTubePlayerSeekBar seekBar = playerUi.findViewById(R.id.playerSeekbar);
        ImageView playPauseButton = playerUi.findViewById(R.id.play_pause_button);
        ImageView enterExitFullscreenButton = playerUi.findViewById(R.id.enter_exit_fullscreen_button);
        LinearLayout relativeLayout = playerUi.findViewById(R.id.root);

        youTubePlayer.addListener(seekBar);

        seekBar.setYoutubePlayerSeekBarListener(new YouTubePlayerSeekBarListener() {
            @Override
            public void seekTo(float v) {
                youTubePlayer.seekTo(v);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTracker.getState() == PlayerConstants.PlayerState.PLAYING) {
                    playPauseButton.setImageResource(R.drawable.ic_play);
                    youTubePlayer.pause();
                } else {
                    playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    youTubePlayer.play();
                }
            }
        });


        enterExitFullscreenButton.setOnClickListener((view) -> {
            if (fullscreen)
            {
                youTubePlayerView.wrapContent();
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                enterExitFullscreenButton.setImageResource(R.drawable.ic_fullscreen_open_24);
            }

            else {
                // Enter fullscreen mode and lock orientation to landscape
                youTubePlayerView.matchParent();
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                enterExitFullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit_24);
            }

            fullscreen = !fullscreen;
        });

        skipNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipForward();
            }
        });

        skipPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipBackward();
            }
        });

        FadeViewHelper fadeViewHelper = new FadeViewHelper(relativeLayout);
        fadeViewHelper.setAnimationDuration(FadeViewHelper.DEFAULT_ANIMATION_DURATION);
        fadeViewHelper.setFadeOutDelay(FadeViewHelper.DEFAULT_FADE_OUT_DELAY);
        youTubePlayer.addListener(fadeViewHelper);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fadeViewHelper.toggleVisibility();
            }
        });

        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fadeViewHelper.toggleVisibility();
            }


        });


    }



    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        progressbar.setVisibility(View.GONE);

    }

    @Override
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED)
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        else if (state == PlayerConstants.PlayerState.BUFFERING)
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
        videoCurrentTimeTextView.setText(second + "");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
        videoDurationTextView.setText(duration + "");
    }

    private void skipBackward() {
        if (youTubePlayer != null) {
            float currentTime = playerTracker.getCurrentSecond();
            float newTime = Math.max(0, currentTime - SKIP_OFFSET); // Ensure the time doesn't go below 0.
            youTubePlayer.seekTo(newTime);
        }
    }

    private void skipForward() {
        if (youTubePlayer != null) {
            float currentTime = playerTracker.getCurrentSecond();
            float videoDuration = playerTracker.getVideoDuration();
            float newTime = Math.min(videoDuration, currentTime + SKIP_OFFSET); // Ensure the time doesn't exceed the video duration.
            youTubePlayer.seekTo(newTime);
        }
    }


}