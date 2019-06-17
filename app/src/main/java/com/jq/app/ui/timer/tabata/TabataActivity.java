package com.jq.app.ui.timer.tabata;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bumptech.glide.Glide;
import com.jq.app.R;
import com.jq.app.ui.timer.ClockTime;

import java.util.ArrayList;
import java.util.List;

public class TabataActivity extends AppCompatActivity {
    private static final String TAG = TabataActivity.class.getSimpleName();
    private Activity mActivity;
    private TextView tv_rounds;
    private TextView tv_work;
   private TextView tv_rest;
    private String SELECTED_ROUND = "05";
    private String SELECTED_WORK = "01";
    private String SELECTED_REST = "01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata);
        mActivity = this;
        init();

        findViewById(R.id.btn_start_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mActivity, StartTabataTimerActivity.class);
//                intent.putExtra("SELECTED_ROUND",SELECTED_ROUND);
//                intent.putExtra("SELECTED_WORK",SELECTED_WORK);
//                intent.putExtra("SELECTED_REST",SELECTED_REST);
//                startActivity(intent);


                Intent intent = new Intent(mActivity, ClockTime.class);
                intent.putExtra("name", getIntent().getExtras().getString("name"));
                intent.putExtra("time", SELECTED_WORK);
                intent.putExtra("round", SELECTED_ROUND);
				intent.putExtra("SELECTED_REST",SELECTED_REST);
                intent.putExtra("video_url",getIntent().getExtras().getString("video_url") );
                intent.putExtra("video_id", getIntent().getExtras().getString("video_id"));
                intent.putExtra("sets", getIntent().getExtras().getString("sets"));
                intent.putExtra("reps", getIntent().getExtras().getString("reps"));
                intent.putExtra("thumbnail", getIntent().getExtras().getString("thumbnail"));
                startActivity(intent);

            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBackPressed();


            }
        });

        tv_rounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList();
                for (int i = 1; i <= 100; i++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(i);
                    list.add(stringBuilder.toString());
                }
                selectTime(list, "ROUND");
            }
        });


        tv_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList();
                for (int i = 1; i <= 45; i++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(i);
                    stringBuilder.append(" minutes");
                    list.add(stringBuilder.toString());
                }
                selectTime(list, "WORK");
            }
        });
// commented
        tv_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList();
                for (int i = 1; i <= 45; i++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(i);
                    stringBuilder.append(" minutes");
                    list.add(stringBuilder.toString());
                }
                selectTime(list, "REST");
            }
        }); //till here
    }

    private void init() {
        tv_rounds = findViewById(R.id.tv_rounds);
        tv_work = findViewById(R.id.tv_work);
       tv_rest = findViewById(R.id.tv_rest); // commented


    }


    private void selectTime(List<String> list, final String timerType) {
        try {
            final Dialog dialog = new Dialog(mActivity);

            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.popup_select_time);
            dialog.getWindow().setLayout(-1, -2);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);


            final WheelPicker mWheelPicker = dialog.findViewById(R.id.tabata_wheel_picker);
            mWheelPicker.setData(list);
            mWheelPicker.setIndicator(true);
            mWheelPicker.setCurved(true);

            if (timerType.equalsIgnoreCase("ROUND")) {

                mWheelPicker.setSelectedItemPosition((Integer.parseInt(SELECTED_ROUND) - 1));

            } else if (timerType.equalsIgnoreCase("WORK")) {

                mWheelPicker.setSelectedItemPosition((Integer.parseInt(SELECTED_WORK) - 1));
// commented
            } else if (timerType.equalsIgnoreCase("REST")) {
				 Log.e(TAG, "REST : " );
                mWheelPicker.setSelectedItemPosition((Integer.parseInt(SELECTED_REST) - 1));
// till here
            }

            mWheelPicker.setVisibleItemCount(10);
            mWheelPicker.setIndicatorColor(getResources().getColor(R.color.gray_selecter));
            mWheelPicker.setIndicatorSize(4);
            mWheelPicker.setItemTextSize(60);


            dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    int currentItemPosition = (mWheelPicker.getCurrentItemPosition() + 1);

                    if (timerType.equalsIgnoreCase("ROUND")) {
                        if (currentItemPosition < 10) {
                            SELECTED_ROUND = "0" + currentItemPosition;
                        } else {
                            SELECTED_ROUND = "" + currentItemPosition + "";
                        }

                        tv_rounds.setText(SELECTED_ROUND);
                        Log.e(TAG, "SELECTED_ROUND : " + SELECTED_ROUND);

                    } else if (timerType.equalsIgnoreCase("WORK")) {
                        if (currentItemPosition < 10) {
                            SELECTED_WORK = "0" + currentItemPosition;
                        } else {
                            SELECTED_WORK = "" + currentItemPosition + "";
                        }

                        tv_work.setText(SELECTED_WORK + ":00");
                        Log.e(TAG, "SELECTED_WORK : " + SELECTED_WORK);


//                    } else if (timerType.equalsIgnoreCase("REST")) {
//                        if (currentItemPosition < 10) {
//                            SELECTED_REST = "0" + currentItemPosition;
//                        } else {
//                            SELECTED_REST = "" + currentItemPosition + "";
//                        }
//
//                        tv_rest.setText(SELECTED_REST + ":00");
//                        Log.e(TAG, "SELECTED_REST : " + SELECTED_REST);

                    }


                }
            });


            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
