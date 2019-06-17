package com.jq.app.ui.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.jq.app.R;
import com.jq.app.ui.timer.tabata.TabataActivity;

public class SelectTimerActivity extends AppCompatActivity {

    /* renamed from: com.jq.app.ui.timer.SelectTimerActivity$1 */
    class C15881 implements OnClickListener {
        C15881() {
        }

        public void onClick(View v) {
            SelectTimerActivity.this.startActivity(new Intent(SelectTimerActivity.this, SelectTime.class));
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timer);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (v.getId() == R.id.amrap) {
            Intent intent = new Intent(this, SelectTime.class);
            intent.putExtra("name", "AMRAP");
            startActivity(intent);
        } else if (v.getId() == R.id.emom) {
            Intent intent = new Intent(this, SelectTime.class);
            intent.putExtra("name", "EMOM");
            startActivity(intent);
        } else if (v.getId() == R.id.stop_watch) {
            Intent intent = new Intent(this, SelectTime.class);
            intent.putExtra("name", "STOPWATCH");
            startActivity(intent);
        } else if (v.getId() == R.id.tabata) {
            Intent intent = new Intent(this, TabataActivity.class);
            intent.putExtra("name", "TABATA");
            startActivity(intent);
        } else if (v.getId() == R.id.timer) {
            Intent intent = new Intent(this, SelectTime.class);
            intent.putExtra("name", "TIMER");
            startActivity(intent);
        }
    }
}
