package com.jq.app.ui.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.jq.app.R;
import com.jq.app.ui.exercise.pojo.ExerciseResponse.Data.Video;
import com.jq.app.ui.views.CustomVideoView;
import com.jq.app.util.Utility;
import com.squareup.picasso.Picasso;

import at.grabner.circleprogress.CircleProgressView;
import cn.jzvd.JZVideoPlayer;

public class PlayerActivity extends AppCompatActivity {
    String TAG = PlayerActivity.class.getName();
    private CircleProgressView circleView;
    private Handler customHandler = new Handler();
    TextView hint_txt;
    int lap = 0;
    private long lastTime = 0;
    long lessTime = 0;
    private CustomVideoView mCustomVideoView;
    private Video mVideo;
    private int maxTime;
    ImageView pause;
    private long pauseTime = 0;
    ImageView play;
    TextView plus_txt;
    int round = 0;
    TextView round_cn_txt;
    private RelativeLayout round_ly;
    TextView round_txt;
    long secsOld = 0;
    int selectedRound = 0;
    private long startTime = 0;
    boolean started = false;
    long timeInMilliseconds = 0;
    long timeSwapBuff = 0;
    TextView timer;
    String timerName = "";
    long timerProgressEMOM = 0;
    private Runnable updateTimerThread = new C15313();
    long updatedTime = 0;

    /* renamed from: com.jq.app.ui.player.PlayerActivity$1 */
    class C15291 implements OnItemSelectedListener {
        C15291() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Utility.setIntegerSharedPreference(PlayerActivity.this, "before_v", position);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.jq.app.ui.player.PlayerActivity$2 */
    class C15302 implements OnItemSelectedListener {
        C15302() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Utility.setIntegerSharedPreference(PlayerActivity.this, "after_v", position);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.jq.app.ui.player.PlayerActivity$3 */
    class C15313 implements Runnable {
        C15313() {
        }

        public void run() {
            PlayerActivity playerActivity = PlayerActivity.this;
            playerActivity.timeInMilliseconds++;
            PlayerActivity.this.circleView.setValue((float) (PlayerActivity.this.timeInMilliseconds - PlayerActivity.this.lessTime));
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        this.circleView = (CircleProgressView) findViewById(R.id.circleView);
        this.circleView.setRimWidth(4);
        this.circleView.setBarWidth(16);
        this.circleView.setBarColor(new int[]{getResources().getColor(R.color.purple)});
        this.circleView.setRimColor(getResources().getColor(R.color.gray_rim));
        this.timerName = getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.titleView)).setText(this.timerName);
        this.hint_txt = (TextView) findViewById(R.id.hint_txt);
        this.timer = (TextView) findViewById(R.id.timer);
        this.pause = (ImageView) findViewById(R.id.pause);
        this.play = (ImageView) findViewById(R.id.play);
        this.plus_txt = (TextView) findViewById(R.id.plus_txt);
        this.round_cn_txt = (TextView) findViewById(R.id.round_cn_txt);
        this.round_txt = (TextView) findViewById(R.id.round_txt);
        this.maxTime = getIntent().getIntExtra("time", 0);
        this.selectedRound = getIntent().getIntExtra("round", 0);
        this.circleView.setBarColor(new int[]{getResources().getColor(R.color.timer)});
        this.circleView.setMaxValue(100.0f);
        this.timer.setTextColor(getResources().getColor(R.color.timer));
        this.play.setImageResource(R.drawable.ic_play_timer);
        this.pause.setImageResource(R.drawable.ic_pause_timer);
        ((Spinner) findViewById(R.id.after_spinner)).setSelection(Utility.getIntegerSharedPreferences(this, "after_v"));
        ((Spinner) findViewById(R.id.before_spinner)).setSelection(Utility.getIntegerSharedPreferences(this, "before_v"));
        ((Spinner) findViewById(R.id.before_spinner)).setOnItemSelectedListener(new C15291());
        ((Spinner) findViewById(R.id.after_spinner)).setOnItemSelectedListener(new C15302());
        this.mCustomVideoView = (CustomVideoView) findViewById(R.id.videoplayer);
        this.mVideo = (Video) getIntent().getExtras().getSerializable(DataSchemeDataSource.SCHEME_DATA);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (v.getId() == R.id.play) {
            this.started = true;
            v.setVisibility(View.GONE);
            this.timer.setVisibility(View.VISIBLE);
            this.hint_txt.setText("tap to pause");
            startTime();
        } else if (v.getId() == R.id.pause) {
            this.started = true;
            v.setVisibility(View.GONE);
            this.timer.setVisibility(View.VISIBLE);
            resume();
            this.hint_txt.setText("tap to pause");
        } else if (v.getId() == R.id.timer) {
            this.started = false;
            v.setVisibility(View.GONE);
            this.pause.setVisibility(View.VISIBLE);
            stopTime();
            this.hint_txt.setText("tap to resume");
        } else if (v.getId() == R.id.round_ly) {
            if (this.started) {
                long last_round = this.timeInMilliseconds - this.lastTime;
                this.lastTime += last_round;
                int mins = ((int) last_round) / 60;
                this.round++;
                this.selectedRound++;
                TextView textView = this.plus_txt;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.round);
                stringBuilder.append("");
                textView.setText(stringBuilder.toString());
            }
        } else if (v.getId() == R.id.t1 || v.getId() == R.id.t2 || v.getId() == R.id.t3 || v.getId() == R.id.t4) {
            onBackPressed();
        }
    }

    private void startTime() {
        this.circleView.setValue(0.0f);
        this.circleView.setMaxValue((float) ((this.maxTime * 60) / this.selectedRound));
        this.updatedTime = 0;
        int secs = (int) this.updatedTime;
        int mins = secs / 60;
        secs %= 60;
        TextView textView = this.timer;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(mins)}));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", new Object[]{Integer.valueOf(secs)}));
        textView.setText(stringBuilder.toString());
        this.lap++;
        this.customHandler.postDelayed(this.updateTimerThread, 1000);
        setAndStartVideo();
    }

    private void completed() {
        this.customHandler.removeCallbacks(this.updateTimerThread);
    }

    private void stopTime() {
        this.pauseTime = SystemClock.uptimeMillis();
        this.customHandler.removeCallbacks(this.updateTimerThread);
    }

    private void resume() {
        this.customHandler.postDelayed(this.updateTimerThread, 1000);
    }

    public void setAndStartVideo() {
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" mVideo.url : ");
        stringBuilder.append(this.mVideo.getUrl());
        Log.e(str, stringBuilder.toString());
        str = this.TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append(" mVideo.thumbnail_url : ");
        stringBuilder.append(this.mVideo.getThumbnail_url());
        Log.e(str, stringBuilder.toString());
        this.mCustomVideoView.setUp(this.mVideo.getUrl(), 0, new Object[]{" "});
        Picasso.with(this).load(this.mVideo.getThumbnail_url()).into(this.mCustomVideoView.thumbImageView);
        setTitle(" ");
    }

    protected void onPause() {
        super.onPause();
        Log.d(this.TAG, "onPause ");
        JZVideoPlayer.releaseAllVideos();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
