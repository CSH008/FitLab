package com.jq.app.ui.my_media;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.my_media.adapter.MyMediaPagerAdapter;
import com.jq.app.util.base.BaseFragment;

import java.io.File;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMediaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMediaFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MyVideosFragment myVideosFragment = new MyVideosFragment();
    MyImagesFragment myImagesFragment = new MyImagesFragment();

    private OnFragmentInteractionListener mListener;


    public MyMediaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyMediaFragment newInstance(String param1, String param2) {
        MyMediaFragment fragment = new MyMediaFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_media, container, false);

        ViewPager pager = view.findViewById(R.id.body_parts_viewpager);
        MyMediaPagerAdapter adapter = new MyMediaPagerAdapter(getChildFragmentManager());
        myVideosFragment = new MyVideosFragment();
        myImagesFragment = new MyImagesFragment();
        adapter.addFragment(myVideosFragment, "Videos");
        adapter.addFragment(myImagesFragment, "Images");
        pager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);

        /**
         * set text
         */
        ((TextView) view.findViewById(R.id.tv_share)).setText("Press hold to delete");
        return view;
    }

    public void filterMedia(int category) {
        myImagesFragment.filterMedia(category);
        myVideosFragment.filterMedia(category);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    MenuItem categoryItem;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // TODO Add your menu entries here
        inflater.inflate(R.menu.my_media, menu);
        categoryItem = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_exercise:
                if(categoryItem!=null) {
                    categoryItem.setTitle(R.string.title_exercise);
                    categoryItem.setIcon(R.mipmap.exercise_videos);
                }
                filterMedia(R.string.title_exercise);
                break;

            case R.id.action_mobility:
                if(categoryItem!=null) {
                    categoryItem.setTitle(R.string.title_mobility);
                    categoryItem.setIcon(R.mipmap.roller_videos);
                }
                filterMedia(R.string.title_mobility);
                break;

            case R.id.action_stretching:
                if(categoryItem!=null) {
                    categoryItem.setTitle(R.string.title_stretching);
                    categoryItem.setIcon(R.mipmap.stretching_videos);
                }
                filterMedia(R.string.title_stretching);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
