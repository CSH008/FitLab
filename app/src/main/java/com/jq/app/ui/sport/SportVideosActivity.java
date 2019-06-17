package com.jq.app.ui.sport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.content_helpers.BaseContentHelper;
import com.jq.app.data.content_helpers.VideoContentHelper;
import com.jq.app.data.local_helpers.FavoriteHelper;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.exercise.ExerciseActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;

public class SportVideosActivity extends BaseMainActivity implements SportVideoAdapter.OnListFragmentInteractionListener,
        BaseContentHelper.OnDataLoadListener, FavoriteHelper.LoadCompletionListener {

    private int mColumnCount = 1;
    EasyRecyclerView recyclerView;

    SportVideoAdapter dataAdapter;
    VideoContentHelper videoContent;

    public static String KEY_SPORT_CODE_1 = "key_body_part_code";
    public static String KEY_SPORT_CODE_2 = "key_workout_code";
    public static String KEY_SPORT_CODE_3 = "key_search_keyword";

    public static String CODE_SPORT_1 = "4";
    public static String CODE_SPORT_2 = "5";
    public static String CODE_SPORT_3 = "6";
	public static String CODE_SPORT_4 = "7";
	public static String CODE_SPORT_5 = "8";
	public static String CODE_SPORT_6 = "9";
//System.out.println("Sport Activity");
	//public static String CODE_SPORT__VIDEOS = "4";
   // public static String CODE_WORKOUT_STRETCHING_VIDEOS = "5";

    @Override
    public void onDeleteFavorite(VideoModel item) {

    }

    public String mWorkoutCode;
    public String mBodyPartCode;
    public String mSearchKeyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");

        mWorkoutCode = getIntent().getStringExtra(KEY_SPORT_CODE_2);
        mBodyPartCode = getIntent().getStringExtra(KEY_SPORT_CODE_1);
        mSearchKeyword = getIntent().getStringExtra(KEY_SPORT_CODE_3);

        mProgressView = findViewById(R.id.view_progress);

        recyclerView = findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }

        getVideoData();
        dataAdapter = new SportVideoAdapter(videoContent.ITEMS, this);
        recyclerView.setAdapter(dataAdapter);
        //recyclerView.setPullRefreshEnabled(false);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (videoContent != null) {
                    getVideoData();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        FavoriteHelper.getInstance().addListener(this);
        tempItem = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        FavoriteHelper.getInstance().deleteListener(this);
    }

    public void getVideoData() {
        showProgressView(true);
        videoContent = new VideoContentHelper(this);
        if(mBodyPartCode!=null) {
            videoContent.getVideoData(mWorkoutCode, mBodyPartCode);

        } else if(mSearchKeyword!=null) {
            videoContent.getSearchVideos(mSearchKeyword, mWorkoutCode);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void updateLikeStatus(VideoModel item) {
        FavoriteHelper.getInstance().createItem(item);
    }


    private VideoModel tempItem;

    @Override
    public void onListFragmentInteraction(VideoModel item) {
        Intent i = new Intent(this, ExerciseActivity.class);
        i.putExtra(ExerciseActivity.KEY_VIDEO_ITEM, item);
        i.putExtra(KEY_SPORT_CODE_2, mWorkoutCode);
        startActivity(i);
    }

    @Override
    public void finishedAction(int method, int message) {
        if(message==0) {
            finishedAction(method, "");
        } else {
            finishedAction(method, getString(message));
        }
    }

    @Override
    public void finishedAction(int method, String message) {
        dataAdapter.notifyDataSetChanged();
        hideProgressDialog();

        if(message!=null && !message.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinishedAction(int action, int index, int errMsg) {
        if(errMsg>0) {
            onFinishedAction(action, index, getString(errMsg));
        } else {
            onFinishedAction(action, index, "");
        }
    }

    @Override
    public void onFinishedAction(int action, int index, String errMsg) {
        recyclerView.setRefreshing(false);
        showProgressView(false);

        if(errMsg!=null && !errMsg.isEmpty()) {
            showToast(errMsg);

        } else {
            switch (action) {
                case VideoContentHelper.REQUEST_LIST:
                    dataAdapter.notifyDataSetChanged();
                    break;

                case VideoContentHelper.REQUEST_SEARCH_LIST:
                    dataAdapter.notifyDataSetChanged();
            }
        }
    }

}
