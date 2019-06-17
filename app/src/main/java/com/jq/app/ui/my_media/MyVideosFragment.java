package com.jq.app.ui.my_media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.local_helpers.BaseLocalHelper;
import com.jq.app.data.local_helpers.FavoriteHelper;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.data.model.LocalImage;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.ui.exercise.ExerciseActivity;
import com.jq.app.ui.my_media.adapter.MyVideoAdapter;
import com.jq.app.util.base.BaseFragment;
import com.jq.chatsdk.activities.VideoActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyVideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyVideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyVideosFragment extends BaseFragment implements MyVideoAdapter.OnListFragmentInteractionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EasyRecyclerView recyclerView;
    MyVideoAdapter dataAdapter;
    LocalVideoHelper videoHelper;

    private OnFragmentInteractionListener mListener;
    int current_category = R.string.category_exercise;

    public MyVideosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyVideosFragment newInstance(String param1, String param2) {
        MyVideosFragment fragment = new MyVideosFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        videoHelper = LocalVideoHelper.getInstance();
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (videoHelper != null) {
                    videoHelper.syncList();
                }
            }
        });

        mProgressView = view.findViewById(R.id.view_progress);

        return view;
    }

    public void reloadData() {
        if(dataAdapter==null) {
            dataAdapter = new MyVideoAdapter(videoHelper.getMedia(current_category), this);
            recyclerView.setAdapter(dataAdapter);

        } else {
            dataAdapter.setData(videoHelper.getMedia(current_category));
        }
    }

    public void filterMedia(int category) {
        current_category = category;
        reloadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().setTitle("My Videos");
        FavoriteHelper.getInstance().addListener(this);
        videoHelper.addListener(this);
        reloadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        FavoriteHelper.getInstance().deleteListener(this);
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
    public void onListFragmentInteraction(LocalVideo item) {
        if(item.localPath!=null && !item.localPath.isEmpty()) {
            Intent intent = new Intent(mActivity, VideoActivity.class);
            intent.putExtra(VideoActivity.FILE_PATH, item.localPath);
            mActivity.startActivity(intent);

        } else {
            tempItem = item;
            showUploadingProgressDialog("Downloading");
            videoHelper.downloadVideo(item, true);
        }
    }

    @Override
    public void onDeleteInteraction(final LocalVideo item) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(mActivity);
        mMaterialDialog.setTitle(R.string.msg_warning)
                .setMessage(R.string.msg_confirm_delete_past_reservations)
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoHelper.deleteItemFromServer(item, true);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void finishedAction(int method, String message) {
        reloadData();
        recyclerView.setRefreshing(false);
        hideProgressDialog();
        hideUploadingProgressDialog();

        if(message!=null && !message.isEmpty()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            return;
        }

        switch (method) {
            case LocalVideoHelper.ACTION_DOWNLOADED:
                if(tempItem!=null && tempItem.localPath!=null && !tempItem.localPath.isEmpty()) {
                    Intent intent = new Intent(mActivity, VideoActivity.class);
                    intent.putExtra(VideoActivity.FILE_PATH, tempItem.localPath);
                    mActivity.startActivity(intent);
                }
                break;
        }
    }

}
