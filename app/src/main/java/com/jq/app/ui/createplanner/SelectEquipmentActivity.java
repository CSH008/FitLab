package com.jq.app.ui.createplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.base.BaseMainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectEquipmentActivity extends BaseMainActivity {

    private TabLayout tabLayout;
    private String currentCategory;

    private Button buttonBalls, buttonBands, buttonRoll, buttonTool;
    List<String> selectEquipments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_equipment);
        buttonBalls = findViewById(R.id.button_balls);
        buttonBands = findViewById(R.id.button_bands);
        buttonRoll = findViewById(R.id.button_roller);
        buttonTool = findViewById(R.id.button_tool);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(listener);
        customTab(tabLayout);
        tabLayout.getTabAt(getIntent().getIntExtra("tab", 0)).select();
        selectEquipments = new ArrayList<>();
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.button_balls:
                buttonBalls.setSelected(!view.isSelected());
                break;
            case R.id.button_bands:
                buttonBands.setSelected(!view.isSelected());
                break;
            case R.id.button_roller:
                buttonRoll.setSelected(!view.isSelected());
                break;
            case R.id.button_tool:
                buttonTool.setSelected(!view.isSelected());
                break;
            case R.id.button_next:
                navigateToNext();
                break;
        }

    }

    private void navigateToNext() {
        if (buttonBalls.isSelected() || buttonBands.isSelected() || buttonRoll.isSelected() || buttonTool.isSelected()) {
            selectEquipments.clear();
            if (buttonBalls.isSelected()) {
                selectEquipments.add("balls");
            }
            if (buttonBands.isSelected()) {
                selectEquipments.add("bands");
            }
            if (buttonRoll.isSelected()) {
                selectEquipments.add("roller");
            }
            if (buttonTool.isSelected()) {
                selectEquipments.add("tool");
            }
            Object[] objects = selectEquipments.toArray();
            String join = TextUtils.join(",", objects);
            Log.v("Equipment Parts", join);
            Intent intent=new Intent(this,PreparePlannerActivity.class);
            intent.putExtra("body_part",false);
            intent.putExtra("selected",join);
            intent.putExtra("tab",tabLayout.getSelectedTabPosition());
            startActivity(intent);
        } else {
            showToast("Please select at least one equipment");
        }
    }
}
