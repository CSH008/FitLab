package com.jq.app.ui.share.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.local_helpers.LocalImageHelper;
import com.jq.app.ui.share.adapter.MyShareImageAdapter;
import com.jq.app.ui.share.model.Image;
import com.jq.app.ui.widgets.ImageOverlayView;
import com.jq.app.util.Config;
import com.jq.app.util.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.imageviewer.adapter.ViewpagerAdapter;
import cn.imageviewer.helper.ImageLoader;
import cn.imageviewer.view.ImageViewer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyShareImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyShareImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShareImagesFragment extends BaseFragment implements MyShareImageAdapter.OnListFragmentInteractionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Image> data= new ArrayList<>();

    FragmentManager manager;

    RecyclerView recyclerView;
    MyShareImageAdapter dataAdapter;
    LocalImageHelper imagesHelper;

    private ImageOverlayView overlayView;

    private OnFragmentInteractionListener mListener;
    int current_category = R.string.title_exercise;

    public MyShareImagesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyShareImagesFragment newInstance(String param1, String param2) {
        MyShareImagesFragment fragment = new MyShareImagesFragment();
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
        manager = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dumble, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        imagesHelper = LocalImageHelper.getInstance();

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        dataAdapter = new MyShareImageAdapter(data, this);
        recyclerView.setAdapter(dataAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiImages();
            }
        });

        callApiImages();

        return view;
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

    ImageViewer mViewer;
    @Override
    public void onListFragmentInteraction(int position) {
        ArrayList<String> paths = new ArrayList<>();
        for(int i=0; i<data.size(); i++) {
            paths.add(data.get(i).getUrl());
        }

        mViewer = new ImageViewer.Builder(
                new ImageLoader() {
                    @Override
                    public void showImage(int position, String path, ImageView imageView) {
                        Glide.with(getActivity().getApplicationContext())
                                .load(path)
                                .into(imageView);
                    }
                },
                new CustomViewpagerAdapter(getActivity()))
                .setIndex(position)
                .setPaths(paths)
                .setTransformerType(ImageViewer.TYPE_ZOOMOUT_TRANSFORMER)
                .build();
        mViewer.show(manager, "ImageViewer");
    }

    @Override
    public void onDeleteInteraction(final int position) {

        try{
            Image item = data.get(position);
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, item.getTitle());
            share.putExtra(Intent.EXTRA_TEXT, item.getUrl());
            startActivity(Intent.createChooser(share, "Share Image Link!"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void finishedAction(int method, String message) {
      /*  recyclerView.setRefreshing(false);
        hideProgressDialog();
        if(message!=null && !message.isEmpty()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }*/
    }

    public class CustomViewpagerAdapter extends ViewpagerAdapter {

        public CustomViewpagerAdapter(Context context) {
            super(context);
        }

        @Override
        protected View initView(ViewGroup container, int position) {
            return LayoutInflater.from(mContext).inflate(R.layout.view_image, container, false);
        }

        @Override
        protected void loadImage(final int i, String s, View view, ImageViewer imageViewer) {
            final ImageView imageView = view.findViewById(R.id.image);

            Image item = data.get(i);
            if(item.getUrl()!=null &&!item.getUrl().isEmpty() && (new File(item.getUrl())).exists()) {
                imageLoader.showImage(i, item.getUrl(), imageView);

            } else {
                imageLoader.showImage(i, s, imageView);
            }

            ImageButton btnShare = view.findViewById(R.id.btnShare);
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ImageButton btnBack = view.findViewById(R.id.btnBack);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewer.dismiss();
                }
            });
        }

        @Override
        protected void recycleImage(View view) {
        }

    }

    public void callApiImages() {

        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.post(Config.USER_ALL_VIDEO_LIST)
                .addBodyParameter("email",  App.my_email )
                .addBodyParameter("type", "image")
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
                                    Image video = new Image(response.getJSONArray("data").getJSONObject(i).getString("id"),
                                            response.getJSONArray("data").getJSONObject(i).getString("url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("body_part_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("work_out_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("title"),
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
