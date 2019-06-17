package com.jq.app.ui.upload;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.base.WorkoutFragment;

public class SaveUploadActivity extends BaseMainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_upload);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");
        Bundle extras = getIntent().getExtras();
        WorkoutFragment myWorkoutFragment2 = new WorkoutFragment();
        myWorkoutFragment2.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout, myWorkoutFragment2).commit();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_home).setVisible(true);
        menu.findItem(R.id.action_clock).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            ActivityCompat.finishAffinity(this);
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
