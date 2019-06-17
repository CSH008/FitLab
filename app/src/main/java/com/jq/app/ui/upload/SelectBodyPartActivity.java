package com.jq.app.ui.upload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.model.BodyPoint;
import com.jq.app.ui.createplanner.PreparePlannerActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SelectBodyPartActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean needToclose = false;
    private Uri uri;
    private String type;

    public static Map<String, BodyPoint> selectedPoints = new HashMap<>();
    private boolean fromPlanner;
    private String category;
    private int tab;
    private boolean fromFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_part);
        selectedPoints.clear();
        Bundle extras = getIntent().getExtras();
        fromFilter = getIntent().getBooleanExtra("from_filter", false);
        fromPlanner = extras.getBoolean("from_planner");
        if (!fromPlanner) {
            uri = extras.getParcelable("uri");
            type = extras.getString("type");
        } else {
            category = extras.getString("category");
            tab = extras.getInt("tab");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");
        ViewPager pager = findViewById(R.id.body_parts_viewpager);
        SelectViewPagerAdapter adapter = new SelectViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SelectBodyFrontFragment(), "front");
        //adapter.addFragment(new BodyAngleFrontFragment(), "front_angle");
        adapter.addFragment(new SelectBodySideFragment(), "side");
        adapter.addFragment(new SelectBodyBackFragment(), "back");
        pager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);
        Button next = findViewById(R.id.button_next);
        if (fromFilter)
            next.setText("Done");
        findViewById(R.id.button_next).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needToclose) {
            needToclose = false;
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.button_next:
                if (selectedPoints.size() > 0) {
                    Set<String> strings = selectedPoints.keySet();
                    Object[] objects = strings.toArray();
                    String join = TextUtils.join(",", objects);
                    Log.v("Body Parts", join);
                    if (fromFilter) {
                        Intent intent = new Intent();
                        intent.putExtra("parts", join);
                        setResult(RESULT_OK,intent);
                        finish();
                        return;
                    }
                    if (!fromPlanner) {
                        Intent intent = new Intent(this, SaveUploadActivity.class);
                        intent.putExtra("image_video", type);
                        intent.putExtra("uri", uri);
                        intent.putExtra("body_part", join);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, PreparePlannerActivity.class);
                        intent.putExtra("body_part", true);
                        intent.putExtra("selected", join);
                        intent.putExtra("tab", tab);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(this, "Please select at least one body part", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
