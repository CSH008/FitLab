package com.jq.app.ui.my_media;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jq.app.R;
import com.jq.app.data.local_helpers.BaseLocalHelper;
import com.jq.app.data.local_helpers.FavoriteHelper;
import com.jq.app.data.local_helpers.LocalImageHelper;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.LocalImage;
import com.jq.app.ui.my_media.adapter.MyImageAdapter;
import com.jq.app.ui.widgets.ImageOverlayView;
import com.jq.app.util.base.BaseFragment;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.imageviewer.adapter.ViewpagerAdapter;
import cn.imageviewer.helper.ImageLoader;
import cn.imageviewer.view.ImageViewer;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyImagesFragment extends BaseFragment implements MyImageAdapter.OnListFragmentInteractionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentManager manager;

    EasyRecyclerView recyclerView;
    MyImageAdapter dataAdapter;
    LocalImageHelper imagesHelper;
    List<LocalImage> mImages;
    private ImageOverlayView overlayView;

    private OnFragmentInteractionListener mListener;
    int current_category = R.string.title_exercise;

    public MyImagesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyImagesFragment newInstance(String param1, String param2) {
        MyImagesFragment fragment = new MyImagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_images, container, false);

        imagesHelper = LocalImageHelper.getInstance();

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (imagesHelper != null) {
                    imagesHelper.syncList();
                }
            }
        });

        mProgressView = view.findViewById(R.id.view_progress);

        reloadData();

        return view;
    }

    public void reloadData() {
        mImages = imagesHelper.getMedia(current_category);
        if(dataAdapter==null) {
            dataAdapter = new MyImageAdapter(mImages, this);
            recyclerView.setAdapter(dataAdapter);

        } else {
            dataAdapter.setData(mImages);
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
        //getActivity().setTitle("My Images");
        FavoriteHelper.getInstance().addListener(this);
        imagesHelper.addListener(this);
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

    ImageViewer mViewer;
    @Override
    public void onListFragmentInteraction(int position) {
        ArrayList<String> paths = new ArrayList<>();
        for(int i=0; i<mImages.size(); i++) {
            paths.add(mImages.get(i).url);
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
        final MaterialDialog mMaterialDialog = new MaterialDialog(mActivity);
        mMaterialDialog.setTitle(R.string.msg_warning)
                .setMessage(R.string.msg_confirm_delete_past_reservations)
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalImage item = mImages.get(position);
                        imagesHelper.deleteItemFromServer(item, true);
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
        recyclerView.setRefreshing(false);
        hideProgressDialog();
        reloadData();

        if(message!=null && !message.isEmpty()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
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

            LocalImage item = mImages.get(i);
            if(item.localPath!=null &&!item.localPath.isEmpty() && (new File(item.localPath)).exists()) {
                imageLoader.showImage(i, item.localPath, imageView);

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

}
