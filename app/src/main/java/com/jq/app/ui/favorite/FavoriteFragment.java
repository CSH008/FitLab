package com.jq.app.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.data.local_helpers.FavoriteHelper;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.exercise.ExerciseActivity;
import com.jq.app.ui.favorite.adapter.FavoriteAdapter;
import com.jq.app.ui.search.VideosActivity;
import com.jq.app.ui.search.adapter.VideoAdapter;
import com.jq.app.util.base.BaseFragment;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends BaseFragment implements VideoAdapter.OnListFragmentInteractionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EasyRecyclerView recyclerView;
    VideoAdapter dataAdapter;
    FavoriteHelper favoriteHelper;

    private OnFragmentInteractionListener mListener;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        favoriteHelper = FavoriteHelper.getInstance();
        dataAdapter = new FavoriteAdapter(this);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (favoriteHelper != null) {
                    favoriteHelper.syncList();
                }
            }
        });
        mProgressView = view.findViewById(R.id.view_progress);

        reloadData();

        return view;
    }

    public void reloadData() {
        ArrayList<BaseModel> temp = new ArrayList<>();
        for (int i=0; i<favoriteHelper.getItems().size(); i++) {
            Favorite item = (Favorite) favoriteHelper.getItems().get(i);
            VideoModel videoItem = new VideoModel(item);
            temp.add(videoItem);
        }
        ((FavoriteAdapter)dataAdapter).setData(temp);
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
        getActivity().setTitle("Favorites");
        FavoriteHelper.getInstance().addListener(this);
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

    @Override
    public void updateLikeStatus(VideoModel item) {
        FavoriteHelper.getInstance().createItem(item);
    }

    @Override
    public void onListFragmentInteraction(VideoModel item) {
        Intent i = new Intent(getActivity(), ExerciseActivity.class);
        i.putExtra(ExerciseActivity.KEY_VIDEO_ITEM, item);
        startActivity(i);
    }

    @Override
    public void finishedAction(int method, String message) {
        reloadData();
        recyclerView.setRefreshing(false);
        hideProgressDialog();

        if(message!=null && !message.isEmpty()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeleteFavorite(final VideoModel item) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(mActivity);
        mMaterialDialog.setTitle(R.string.msg_warning)
                .setMessage(R.string.msg_confirm_delete_past_reservations)
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(favoriteHelper!=null) {
                            Favorite favorite = (Favorite) favoriteHelper.getItem(item.id);
                            if(favorite!=null) {
                                favoriteHelper.deleteItem(favorite, true);
                            }
                        }
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

}
