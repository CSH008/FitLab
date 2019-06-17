package com.jq.app.ui.sport;

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
//import com.jq.app.ui.search.pager.BodyBackFragment;
import com.jq.app.ui.sport.BodyFrontFragment;
//import com.jq.app.ui.search.adapter.ViewPagerAdapter;

public class SportSearchActivity extends BaseMainActivity {

    public String mWorkoutCode;

    AutoCompleteTextView search_video;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");

        mWorkoutCode = getIntent().getStringExtra(SportVideosActivity.KEY_SPORT_CODE_2);

        ViewPager pager = findViewById(R.id.baseball_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BodyFrontFragment(), "baseball");
        //adapter.addFragment(new BodyAngleFrontFragment(), "front_angle");
       // adapter.addFragment(new BodySideFragment(), "side");
       // adapter.addFragment(new BodyBackFragment(), "back");
        pager.setAdapter(adapter);
		System.out.println("Sport Search Activity");
      // TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
      // tabLayout.setupWithViewPager(pager, true);
/*
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
		*/
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

        Intent i = new Intent(this, SportVideosActivity.class);
        i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, mWorkoutCode);
        i.putExtra(SportVideosActivity.KEY_SPORT_CODE_3, search_video.getText().toString());
        startActivity(i);
    }

}
