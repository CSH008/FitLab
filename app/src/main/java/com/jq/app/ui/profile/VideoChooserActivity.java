package com.jq.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.content_helpers.VideoContentHelper;
import com.jq.app.data.local_helpers.FavoriteHelper;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.search.adapter.VideoAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;

public class VideoChooserActivity extends BaseMainActivity implements VideoAdapter.OnListFragmentInteractionListener {

    private int mColumnCount = 1;
    EasyRecyclerView recyclerView;

    VideoAdapter dataAdapter;
    VideoContentHelper videoHelper;

    public static String KEY_BODY_PART = "key_body_part";
    public static String KEY_WORKOUT = "key_workout";

    public static String KEY_WORKOUT_EXERCISE_VIDEOS = "01";
    public static String KEY_WORKOUT_ROLLER_VIDEOS = "02";
    public static String KEY_WORKOUT_STRETCHING_VIDEOS = "03";

    public String mWorkout;
    public String mBodyPart;

    @Override
    public void onDeleteFavorite(VideoModel item) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" ");

        mWorkout = getIntent().getStringExtra(KEY_WORKOUT);
        mBodyPart = getIntent().getStringExtra(KEY_BODY_PART);

        mProgressView = findViewById(R.id.view_progress);

        recyclerView = findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }

        getVideoData();
        dataAdapter = new VideoAdapter(videoHelper.ITEMS, this);
        recyclerView.setAdapter(dataAdapter);
        //recyclerView.setPullRefreshEnabled(false);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (videoHelper != null) {
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
        videoHelper = new VideoContentHelper(this);
        videoHelper.getChooserVideoData(LocalVideoHelper.getWorkoutCode(mWorkout), mBodyPart);
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
        Intent returnIntent = new Intent();
        returnIntent.putExtra("video_id", item.id);
        setResult(RESULT_OK, returnIntent);
        finish();
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
            }
        }
    }

}
