package com.jq.app.ui.sport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.auth.LoginActivity;
import com.jq.app.ui.sport.SportSearchActivity;
import com.jq.app.util.base.BaseFragment;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportFragment extends BaseFragment implements SportCategoryAdapter.OnListFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EasyRecyclerView recyclerView;
    SportCategoryAdapter dataAdapter;
    private TextView tv_title;
    private Typeface Caviar_Dreams_Regular;

    private OnFragmentInteractionListener mListener;

    public SportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @param param2 Parameter 2.
     * @return A new instance of fragment SportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportFragment newInstance(String param1, String param2) {
        SportFragment fragment = new SportFragment();
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
		System.out.println("Sport Fragment");
        View view = inflater.inflate(R.layout.fragment_sport, container, false);
        Caviar_Dreams_Regular = Typeface.createFromAsset((getActivity().getApplicationContext().getAssets()), String.format(Locale.US, "hasnain_fonts/%s", "CaviarDreams.ttf"));

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        dataAdapter = new SportCategoryAdapter(Caviar_Dreams_Regular,getMainCategories(), this);
        recyclerView.setAdapter(dataAdapter);

        mProgressView = view.findViewById(R.id.view_progress);

        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setTypeface(Caviar_Dreams_Regular);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(" ");
    }

    public List<SportCategoryModel> getMainCategories() {
        ArrayList<SportCategoryModel> categories = new ArrayList<>();
        categories.add(new SportCategoryModel(getString(R.string.sport_baseball), R.mipmap.sport_baseball));
        categories.add(new SportCategoryModel(getString(R.string.sport_basketball), R.mipmap.sport_basketball));
        categories.add(new SportCategoryModel(getString(R.string.sport_football), R.mipmap.sport_football));
        categories.add(new SportCategoryModel(getString(R.string.sport_soccer), R.mipmap.sport_soccer));

		categories.add(new SportCategoryModel(getString(R.string.sport_gymnastics), R.mipmap.sport_gymnastics));
        categories.add(new SportCategoryModel(getString(R.string.sport_vollyball), R.mipmap.sport_volleyball));
		//categories.add(new SportCategoryModel(getString(R.string.category_cardio), R.mipmap.cardio_video));
		//categories.add(new SportCategoryModel(getString(R.string.category_timer), R.mipmap.timer));
        //categories.add(new SportCategoryModel(getString(R.string.category_logout), R.mipmap.logout));
        return categories;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onListFragmentInteraction(SportCategoryModel item) {
        Intent i = new Intent(getActivity(), SportSearchActivity.class);
        switch (item.image_resource) {
            case R.mipmap.sport_baseball:
                i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, SportVideosActivity.CODE_SPORT_1);
                break;

            case R.mipmap.sport_basketball:
                i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, SportVideosActivity.CODE_SPORT_2);
                break;

            case R.mipmap.sport_football:
                i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, SportVideosActivity.CODE_SPORT_3);
                break;

				case R.mipmap.sport_soccer:
                i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, SportVideosActivity.CODE_SPORT_4);
                break;

				case R.mipmap.sport_gymnastics:
                i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, SportVideosActivity.CODE_SPORT_5);
                break;

				case R.mipmap.sport_volleyball:
                i.putExtra(SportVideosActivity.KEY_SPORT_CODE_2, SportVideosActivity.CODE_SPORT_6);
                break;

				
            case R.mipmap.my_tracker:
                ((MainActivity)getActivity()).showProfilePage();
                return;

            case R.mipmap.logout:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return;
        }
        startActivity(i);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
