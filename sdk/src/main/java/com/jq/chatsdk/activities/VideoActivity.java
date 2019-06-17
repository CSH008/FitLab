package com.jq.chatsdk.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.jq.chatsdk.R;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoActivity extends ChatSDKMBaseActivity {

    private static final String TAG = "VideoActivity";

    public static final String FILE_PATH = "file_path";
    protected String videoFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_sdk_activity_video);

        initActionBar();
        setTitle("Attachment Video");

        videoFilePath = getIntent().getStringExtra(FILE_PATH);
        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(videoFilePath
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, " ");
        //Picasso.with(this).load(mVideo.thumbnail_url).into(jzVideoPlayerStandard.thumbImageView);
        //mMediaController.hideTitleLayout();
    }

    public void onClick(View view) {
        int id = view.getId();
        if(R.id.btn_back == id) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }

        super.onBackPressed();
    }

}
