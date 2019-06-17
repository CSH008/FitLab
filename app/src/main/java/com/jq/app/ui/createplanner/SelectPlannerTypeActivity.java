package com.jq.app.ui.createplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.upload.SelectBodyPartActivity;

public class SelectPlannerTypeActivity extends BaseMainActivity {

    private String currentCategory;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_planner_type);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(listener);
        customTab(tabLayout);
        tabLayout.getTabAt(getIntent().getIntExtra("tab",0)).select();
    }

    TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            setCurrentCategory(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void setCurrentCategory(int position) {
        switch (position) {
            case 0:
                currentCategory = "3";
                break;
            case 1:
                currentCategory = "1";
                break;
            case 2:
                currentCategory = "2";
                break;
            case 3:
                currentCategory = "4";
                break;
            case 4:
                currentCategory = "5";
                break;
        }

        Log.v("CURRENT_CATEGORY", currentCategory);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.button_body:
                Intent intent = new Intent(this, SelectBodyPartActivity.class);
                intent.putExtra("from_planner", true);
                intent.putExtra("category", currentCategory);
                intent.putExtra("tab", tabLayout.getSelectedTabPosition());
                startActivity(intent);
                break;
            case R.id.button_equipment:
                Intent intent2 = new Intent(this, SelectEquipmentActivity.class);
                intent2.putExtra("tab", tabLayout.getSelectedTabPosition());
                startActivity(intent2);
                break;
        }

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
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
