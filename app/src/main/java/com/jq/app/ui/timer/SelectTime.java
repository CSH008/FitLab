package com.jq.app.ui.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.jq.app.R;

import java.util.ArrayList;
import java.util.List;

public class SelectTime extends AppCompatActivity {
    List<String> list = new ArrayList();
    Spinner spinner;
    WheelPicker wheelPicker;

    /* renamed from: com.jq.app.ui.timer.SelectTime$1 */
    class C15871 implements OnClickListener {
        C15871() {
        }

        public void onClick(View v) {
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        this.list.clear();
        for (int i = 1; i <= 60; i++) {
            List list = this.list;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i);
            stringBuilder.append(" minutes");
            list.add(stringBuilder.toString());
        }
        this.wheelPicker = (WheelPicker) findViewById(R.id.wheel_picker);
        this.wheelPicker.setData(this.list);
        this.wheelPicker.setIndicator(true);
        this.wheelPicker.setCurved(true);
        this.wheelPicker.setSelectedItemPosition(4);
        this.wheelPicker.setVisibleItemCount(10);
        this.wheelPicker.setIndicatorColor(getResources().getColor(R.color.gray_selecter));
        this.wheelPicker.setIndicatorSize(4);
        this.wheelPicker.setItemTextSize(60);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ((TextView) findViewById(R.id.header_name)).setText(getIntent().getStringExtra("name"));
        TextView textView = (TextView) findViewById(R.id.header_title);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Set Your ");
        stringBuilder2.append(getIntent().getStringExtra("name"));
        stringBuilder2.append(" Time");
        textView.setText(stringBuilder2.toString());
        if (!(getIntent().getStringExtra("name").equalsIgnoreCase("AMRAP") || getIntent().getStringExtra("name").equalsIgnoreCase("TIMER") || getIntent().getStringExtra("name").equalsIgnoreCase("STOPWATCH"))) {
            findViewById(R.id.round_visible).setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("name").equalsIgnoreCase("AMRAP")) {
            findViewById(R.id.ok).setBackgroundColor(getResources().getColor(R.color.amrap));
        } else if (getIntent().getStringExtra("name").equalsIgnoreCase("EMOM")) {
            findViewById(R.id.ok).setBackgroundColor(getResources().getColor(R.color.emom));
        } else if (getIntent().getStringExtra("name").equalsIgnoreCase("STOPWATCH")) {
            findViewById(R.id.ok).setBackgroundColor(getResources().getColor(R.color.stopwatch));
        } else if (getIntent().getStringExtra("name").equalsIgnoreCase("TABATA")) {
            findViewById(R.id.ok).setBackgroundColor(getResources().getColor(R.color.tabata));
        } else if (getIntent().getStringExtra("name").equalsIgnoreCase("TIMER")) {
            findViewById(R.id.ok).setBackgroundColor(getResources().getColor(R.color.timer));
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }
        if (v.getId() == R.id.ok) {

            Intent intent = new Intent(this, ClockTime.class);
            intent.putExtra("name", getIntent().getExtras().getString("name"));
            intent.putExtra("time", (this.wheelPicker.getCurrentItemPosition() + 1)+"");
            intent.putExtra("round", (this.spinner.getSelectedItemPosition() + 1)+"");
            intent.putExtra("video_url",getIntent().getExtras().getString("video_url") );
            intent.putExtra("video_id", getIntent().getExtras().getString("video_id"));
            intent.putExtra("sets", getIntent().getExtras().getString("sets"));
            intent.putExtra("reps", getIntent().getExtras().getString("reps"));
            intent.putExtra("thumbnail", getIntent().getExtras().getString("thumbnail"));

            startActivity(intent);
            finish();
        }
    }
}
