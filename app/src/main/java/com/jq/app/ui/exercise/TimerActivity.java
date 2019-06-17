package com.jq.app.ui.exercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.ui.base.BaseMainActivity;

import java.util.Timer;
import java.util.TimerTask;

import at.grabner.circleprogress.CircleProgressView;

public class TimerActivity extends BaseMainActivity {

    public static final String KEY_TIMER_DURATION = "key_timer_duration";

    CircleProgressView circleView;
    TextView timerText;
    ImageView btn_timer_start;
    ImageView btn_timer_reset;

    boolean isRunningTimer = false;
    int timerDuration = 0;
    int currentPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerDuration = getIntent().getIntExtra(KEY_TIMER_DURATION, 0);

        timerText = (TextView) findViewById(R.id.timerText);
        circleView = (CircleProgressView) findViewById(R.id.circleView);

        btn_timer_start = (ImageView) findViewById(R.id.btn_timer_start);
        btn_timer_reset = (ImageView) findViewById(R.id.btn_timer_reset);
        currentPosition = timerDuration;
        circleView.setMaxValue(timerDuration);
        startTimer();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_timer_start:
                processTimer();
                break;
            case R.id.btn_timer_reset:
                resetTimer();
                break;
        }
    }

    private String getTimeValue(int progress) {
        String result = "";
        int mins = progress / 60;
        int secs = progress - mins * 60;
        result = "" + mins + ":" + String.format("%02d", secs);
        return result;
    }

    private void processTimer() {
        if(isRunningTimer) {
            pauseTimer();

        } else {
            startTimer();
        }
    }

    private Timer mTimer;
    private void startTimer() {
        isRunningTimer = true;
        btn_timer_start.setImageResource(R.mipmap.pause);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentPosition --;
                runOnUiThread(new Thread() {
                    public void run() {
                        if(currentPosition<=0) {
                            resetTimer();
                        } else {
                            updateTimerUI();
                        }
                    }
                });

            }
        }, 0, 1000);
    }

    private void pauseTimer() {
        if (mTimer != null) {
            isRunningTimer = false;
            btn_timer_start.setImageResource(R.mipmap.start);
            mTimer.cancel();
        }
    }

    private void resetTimer() {
        if(isRunningTimer) {
            pauseTimer();
        }
        currentPosition = timerDuration;
        updateTimerUI();
    }

    private void updateTimerUI() {
        timerText.setText(getTimeValue(currentPosition));
        int circleProgress = timerDuration - currentPosition;
        circleView.setValue(circleProgress);
    }

}
