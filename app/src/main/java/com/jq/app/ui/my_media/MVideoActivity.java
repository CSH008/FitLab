package com.jq.app.ui.my_media;

import android.os.Bundle;

import com.jq.app.data.model.LocalVideo;
import com.jq.chatsdk.activities.VideoActivity;

/**
 * Created by Administrator on 12/19/2017.
 */

public class MVideoActivity extends VideoActivity {

    public static LocalVideo mLocalVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoFilePath = mLocalVideo.localPath;

    }

}
