package com.evans.sampleimpl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.evans.sampleimpl.Constants.VIDEO_URL_FIVE;
import static com.evans.sampleimpl.Constants.VIDEO_URL_FOUR;
import static com.evans.sampleimpl.Constants.VIDEO_URL_ONE;
import static com.evans.sampleimpl.Constants.VIDEO_URL_THREE;
import static com.evans.sampleimpl.Constants.VIDEO_URL_TWO;
import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_READY;

public class TopicActivity extends AppCompatActivity implements Player.EventListener {

    private String sectionId, sectionTitle;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = false, fullScreen = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ImageView fullScreenIcon;
    private FrameLayout videoFrame;
    private ProgressBar loaderProgress;
    private Button btnRetry, btnNext;
    private List<Topic> mTopics = new ArrayList<>();
    private int currentPosition = 0;
    private TextView title, description;
    private ImageButton exoPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        if (intent != null) {
            sectionId = intent.getStringExtra("sectionId");
            sectionTitle = intent.getStringExtra("sectionTitle");
        }

        playerView = findViewById(R.id.videoView);
        fullScreenIcon = playerView.findViewById(R.id.exo_fullscreen_icon);
        exoPlay = playerView.findViewById(R.id.exo_play);
        videoFrame = findViewById(R.id.videoFrame);
        loaderProgress = findViewById(R.id.loaderProgress);
        btnRetry = findViewById(R.id.retryBtn);
        btnNext = findViewById(R.id.btnNext);
        title = findViewById(R.id.topicTitle);
        description = findViewById(R.id.topicDesc);

//        initializePlayer();

        initializeTopics();

        loadTopicAtCurrentPosition();

        fullScreenIcon.setOnClickListener(v -> toggleOrientation());

        btnRetry.setOnClickListener(v -> initializePlayer());

        btnNext.setOnClickListener(v -> moveNext());
    }

    private void loadTopicAtCurrentPosition() {
        Topic topic = mTopics.get(currentPosition);
        title.setText(topic.getTopicTitle());
        description.setText(topic.getTopicDesc());

        initializePlayer();
    }

    private void moveNext() {
        currentPosition = currentPosition + 1;
        if (currentPosition < mTopics.size()){
            Topic topic = mTopics.get(currentPosition);
            String title = topic.getTopicTitle();
            loadTopicAtCurrentPosition();
            Toast.makeText(this, "loading " + title, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "can't go past this position", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeTopics() {
        mTopics.add(new Topic("001", "Topic One", "Sample Topic One Description", VIDEO_URL_ONE));
        mTopics.add(new Topic("002", "Topic Two", "Sample Topic Two Description", VIDEO_URL_TWO));
        mTopics.add(new Topic("003", "Topic Three", "Sample Topic Three Description", VIDEO_URL_THREE));
        mTopics.add(new Topic("004", "Topic Four", "Sample Topic Four Description", VIDEO_URL_FOUR));
        mTopics.add(new Topic("005", "Topic Five", "Sample Topic Five Description", VIDEO_URL_FIVE));
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void toggleOrientation() {
        if (fullScreen) {
            fullScreenIcon.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_open)
            );
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoFrame.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 0;
            params.weight = 0.6f;
            videoFrame.setLayoutParams(params);
            fullScreen = false;
        } else {
            fullScreenIcon.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_close)
            );
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoFrame.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoFrame.setLayoutParams(params);
            fullScreen = true;
        }
    }

    private void initializePlayer() {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        }

        playerView.setPlayer(player);
        MediaSource mediaSource = buildMediaSource(Uri.parse(mTopics.get(currentPosition).getVideoUrl()));
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.addListener(this);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "sample-impl");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
        super.onStop();
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == STATE_BUFFERING) {
            loaderProgress.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.GONE);
        } else if (playbackState == STATE_READY) {
            loaderProgress.setVisibility(View.GONE);
            btnRetry.setVisibility(View.GONE);
        } else {
            loaderProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        btnRetry.setVisibility(View.VISIBLE);
    }
}
