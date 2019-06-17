package com.jq.app.ui.views;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.ui.exercise.ExerciseActivity;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Hasnain on 12-Feb-18.
 */

public class CustomVideoView extends JZVideoPlayerStandard {
    private Context mContext;

    public CustomVideoView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    @Override
    public void dissmissControlView() {
        super.dissmissControlView();

        ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bottomProgressBar.setVisibility(GONE);
                backButton.setVisibility(GONE);
            }
        });

    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, thumbImg, bottomPro, retryLayout);

        bottomProgressBar.setVisibility(GONE);
        backButton.setVisibility(GONE);

    }

    @Override
    public void init(final Context context) {
        super.init(context);

        findViewById(cn.jzvd.R.id.surface_container).setOnTouchListener(null);


        findViewById(cn.jzvd.R.id.layout_bottom).setVisibility(GONE);


        findViewById(cn.jzvd.R.id.start_layout).setVisibility(GONE);
        findViewById(cn.jzvd.R.id.replay_text).setVisibility(GONE);
        findViewById(cn.jzvd.R.id.retry_layout).setVisibility(GONE);

        /**
         * Payer control
         */
        findViewById(cn.jzvd.R.id.clarity).setVisibility(GONE);
        findViewById(cn.jzvd.R.id.bottom_seek_progress).setVisibility(GONE);
        findViewById(cn.jzvd.R.id.current).setVisibility(GONE);
        findViewById(cn.jzvd.R.id.total).setVisibility(GONE);
        findViewById(cn.jzvd.R.id.fullscreen).setVisibility(GONE);


        findViewById(cn.jzvd.R.id.thumb).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        findViewById(cn.jzvd.R.id.tv_skip_next).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(JZMediaManager.isPlaying()){
                    onEvent(6);
                }else{
                    Toast.makeText(context, "Player is not ready", Toast.LENGTH_SHORT).show();
                }



            }
        });


        findViewById(cn.jzvd.R.id.surface_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                startDismissControlViewTimer();

// if (mChangePosition) {
// int duration = getDuration();
// int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
// bottomProgressBar.setProgress(progress);
// }
// if (!mChangePosition && !mChangeVolume) {
// onEvent(JZUserActionStandard.ON_CLICK_BLANK);
// onClickUiToggle();
// }
            }
        });
    }


    @Override
    public void onEvent(int type) {
        super.onEvent(type);
        /**
         *  int ON_CLICK_START_ICON = 0;
         int ON_CLICK_START_ERROR = 1;
         int ON_CLICK_START_AUTO_COMPLETE = 2;

         int ON_CLICK_PAUSE = 3;
         int ON_CLICK_RESUME = 4;
         int ON_SEEK_POSITION = 5;
         int ON_AUTO_COMPLETE = 6;

         int ON_ENTER_FULLSCREEN = 7;
         int ON_QUIT_FULLSCREEN = 8;
         int ON_ENTER_TINYSCREEN = 9;
         int ON_QUIT_TINYSCREEN = 10;


         int ON_TOUCH_SCREEN_SEEK_VOLUME = 11;
         int ON_TOUCH_SCREEN_SEEK_POSITION = 12;
         */

        switch (type) {
            case 0:
                Log.e(TAG, "onEvent : ON_CLICK_START_ICON");
                break;

            case 1:
                Log.e(TAG, "onEvent : ON_CLICK_START_ERROR");
                break;

            case 2:
                Log.e(TAG, "onEvent : ON_CLICK_START_AUTO_COMPLETE");
                break;

            case 3:
                Log.e(TAG, "onEvent : ON_CLICK_PAUSE");
                break;

            case 4:
                Log.e(TAG, "onEvent : ON_CLICK_RESUME");
                break;

            case 5:
                Log.e(TAG, "onEvent : ON_SEEK_POSITION");
                break;

            case 6:
                Log.e(TAG, "onEvent : ON_AUTO_COMPLETE");
                MyPlayerTask playerTask = new MyPlayerTask();
                playerTask.execute();
                break;

            case 7:
                Log.e(TAG, "onEvent : ON_ENTER_FULLSCREEN");
                break;

            case 8:
                Log.e(TAG, "onEvent : ON_QUIT_FULLSCREEN");
                break;

            case 9:
                Log.e(TAG, "onEvent : ON_ENTER_TINYSCREEN");
                break;

            case 10:
                Log.e(TAG, "onEvent : ON_QUIT_TINYSCREEN");
                break;

            case 11:
                Log.e(TAG, "onEvent : ON_TOUCH_SCREEN_SEEK_VOLUME");
                break;

            case 12:
                Log.e(TAG, "onEvent : ON_TOUCH_SCREEN_SEEK_POSITION");
                break;
        }
    }

    class MyPlayerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            startVideo();
            if (mContext instanceof ExerciseActivity){
                ((ExerciseActivity) mContext).next();
            }
        }
    }

    public void hideSkip(){
     findViewById(R.id.tv_skip_next).setVisibility(GONE);
    }

}
