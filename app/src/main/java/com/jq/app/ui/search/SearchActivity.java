package com.jq.app.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.search.pager.BodyBackFragment;
import com.jq.app.ui.search.pager.BodyFrontFragment;
import com.jq.app.ui.search.pager.BodySideFragment;
import com.jq.app.ui.search.adapter.ViewPagerAdapter;

public class SearchActivity extends BaseMainActivity {

    public String mWorkoutCode;

    AutoCompleteTextView search_video;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");

        mWorkoutCode = getIntent().getStringExtra(VideosActivity.KEY_WORKOUT_CODE);

        ViewPager pager = findViewById(R.id.body_parts_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BodyFrontFragment(), "front");
        //adapter.addFragment(new BodyAngleFrontFragment(), "front_angle");
        adapter.addFragment(new BodySideFragment(), "side");
        adapter.addFragment(new BodyBackFragment(), "back");
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);

        search_video = findViewById(R.id.search_video);
        search_video.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;

                } else if(event.getAction()==KeyEvent.ACTION_DOWN) {
                    performSearch();
                    return true;

                } else if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    public void performSearch() {
        if(search_video.getText().toString().isEmpty()) return;

        Intent i = new Intent(this, VideosActivity.class);
        i.putExtra(VideosActivity.KEY_WORKOUT_CODE, mWorkoutCode);
        i.putExtra(VideosActivity.KEY_SEARCH_KEYWORD, search_video.getText().toString());
        startActivity(i);
    }

}
