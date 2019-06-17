package com.jq.app.ui.share.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.ui.share.adapter.MyShareVideoAdapter;
import com.jq.app.ui.share.model.Video;
import com.jq.app.util.Config;
import com.jq.app.util.base.BaseFragment;
import com.jq.chatsdk.activities.VideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyShareVideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyShareVideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShareVideosFragment extends BaseFragment implements MyShareVideoAdapter.OnListFragmentInteractionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    MyShareVideoAdapter dataAdapter;
    LocalVideoHelper videoHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Video> data= new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    int current_category = R.string.category_exercise;

    public MyShareVideosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyShareVideosFragment newInstance(String param1, String param2) {
        MyShareVideosFragment fragment = new MyShareVideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dumble, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        dataAdapter = new MyShareVideoAdapter(data, this);
        recyclerView.setAdapter(dataAdapter);
        callApiVideo();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiVideo();
            }
        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private LocalVideo tempItem;
    @Override
    public void onListFragmentInteraction(Video item) {

        if(!item.getUrl().isEmpty() && !item.getUrl().equalsIgnoreCase("")) {
            Intent intent = new Intent(mActivity, VideoActivity.class);
            intent.putExtra(VideoActivity.FILE_PATH, item.getUrl());
            mActivity.startActivity(intent);
        }


    }

    @Override
    public void onDeleteInteraction(final Video item) {
      try{
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, item.getTitle());
            share.putExtra(Intent.EXTRA_TEXT, item.getUrl());
            startActivity(Intent.createChooser(share, "Share Video Link!"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void callApiVideo() {
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.post(Config.USER_ALL_VIDEO_LIST)
                .addBodyParameter("email",  App.my_email )
                .addBodyParameter("type", "video")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
                                data.clear();
                                dataAdapter.notifyDataSetChanged();
                                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                    Video video = new Video(response.getJSONArray("data").getJSONObject(i).getString("id"),
                                            response.getJSONArray("data").getJSONObject(i).getString("url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("body_part_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("work_out_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("title"),
                                            response.getJSONArray("data").getJSONObject(i).getString("thumbnail_url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("created_date_time")
                                    );

                                    data.add(video);

                                }
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }

                            dataAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            swipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

}
