package com.jq.app.ui.PlannerNew;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.ui.PlannerNew.Adapter.CustomAdapter;
import com.jq.app.ui.PlannerNew.Adapter.EditItemTouchHelperCallback;
import com.jq.app.ui.PlannerNew.Adapter.ItemAdapter;
import com.jq.app.ui.PlannerNew.interfaces.OnStartDragListener;
import com.jq.app.ui.PlannerNew.model.Planner;
import com.jq.app.ui.PlannerNew.model.Video;
import com.jq.app.ui.save_video.SaveVideoActivity;
import com.jq.app.util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlannerFragmentNew extends Fragment implements OnStartDragListener, View.OnDragListener, View.OnClickListener {
    private static final String TAG = PlannerFragmentNew.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View view;
    ArrayList<Planner> dataOneList = new ArrayList<>();
    CustomAdapter dataAdapterOne;

    private Planner videoToMove;
    private int newContactPosition = -1;


    ArrayList<Planner> dataTwoList;

    ItemAdapter dataAdapterTwo;
    ItemTouchHelper mItemTouchHelper;

    private ProgressDialog progressDialog;

    private RecyclerView recyclerViewOne, recyclerTwo;
//    private RadioGroup radioGroup;
    private LinearLayout ll_drag_n_drop;

    private ImageView iv_dumbbell;
    private ImageView iv_body_exercises;
    private ImageView iv_body_stretch;
    private ImageView btn_save;

    private View view_dumbbell;
    private View view_exercise;
    private View view_stretch;

    private TextView tv_available_videos;
    private static final int REQUEST_CODE = 404;
    public PlannerFragmentNew() {
        // Required empty public constructor
    }

   /* public static PlannerFragmentNew newInstance(String param1, String param2) {
        PlannerFragmentNew fragment = new PlannerFragmentNew();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

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
            try{
                view = inflater.inflate(R.layout.fragment_planner_fragment_new, container, false);
                setupProgressBar();
                dataTwoList = new ArrayList<>();
                initView(view);
                setupOneAdatper();
                setupTwoAdapter();
                recyclerTwo.setOnDragListener(this);
            }catch (Exception e)
            {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        return view;
    }

    public void setupProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
    }

    public void setupOneAdatper() {
        recyclerViewOne.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        dataAdapterOne = new CustomAdapter(getActivity(), dataOneList, recyclerViewOne);
        recyclerViewOne.setAdapter(dataAdapterOne);
    }

    public void setupTwoAdapter() {
        dataTwoList.add(new Planner("", "", "", "", "", "", "","","-1",""));
        recyclerTwo.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerTwo.setLayoutManager(mLayoutManager);

        dataAdapterTwo = new ItemAdapter(getActivity(), dataTwoList, this);
        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(dataAdapterTwo);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerTwo);
        recyclerTwo.setAdapter(dataAdapterTwo);

    }

    public void initView(View view) {
        recyclerViewOne = view.findViewById(R.id.recyclerview_main);
        recyclerTwo = view.findViewById(R.id.recyclerview_child);
        ll_drag_n_drop = view.findViewById(R.id.ll_drag_n_drop);

        iv_dumbbell = view.findViewById(R.id.iv_dumbbell);
        iv_body_exercises = view.findViewById(R.id.iv_body_exercises);
        iv_body_stretch = view.findViewById(R.id.iv_body_stretch);
        btn_save = view.findViewById(R.id.btn_save);

        view_dumbbell = view.findViewById(R.id.view_dumbbell);
        view_dumbbell.setVisibility(View.VISIBLE);

        view_exercise = view.findViewById(R.id.view_exercise);
        view_exercise.setVisibility(View.GONE);

        view_stretch = view.findViewById(R.id.view_stretch);
        view_stretch.setVisibility(View.GONE);

        tv_available_videos = view.findViewById(R.id.tv_available_videos);

//        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
//        switch (radioGroup.getCheckedRadioButtonId()) {
//            case R.id.dumble:
//                callApiVideo("excerise");
//                break;
//            case R.id.streching:
//                callApiVideo("strtching");
//                break;
//            case R.id.roller:
//                callApiVideo("mobility");
//                break;
//            default:
//                break;
//        }

//        // Checked change Listener for RadioGroup 1
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.dumble:
//                        callApiVideo("excerise");
//                        break;
//                    case R.id.streching:
//                        callApiVideo("strtching");
//                        break;
//                    case R.id.roller:
//                        callApiVideo("mobility");
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        /**
         * Dumbbell button click
         */
        iv_dumbbell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_dumbbell.setVisibility(View.VISIBLE);
                view_exercise.setVisibility(View.GONE);
                view_stretch.setVisibility(View.GONE);

                callApiVideo("excerise");


            }
        });

        /**
         * Body exercise button click
         */
        iv_body_exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_exercise.setVisibility(View.VISIBLE);
                view_dumbbell.setVisibility(View.GONE);
                view_stretch.setVisibility(View.GONE);

                callApiVideo("mobility");
            }
        });

        /**
         * Stretch button click
         */
        iv_body_stretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_stretch.setVisibility(View.VISIBLE);
                view_dumbbell.setVisibility(View.GONE);
                view_exercise.setVisibility(View.GONE);

                callApiVideo("strtching");

            }
        });

        /**
         * save video list
         */
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataTwoList.size() > 1) {
                    /**
                     * start save activity
                     */
                    Intent mIntent = new Intent(getActivity(), SaveVideoActivity.class);
                    mIntent.putParcelableArrayListExtra("video_list", dataTwoList);
                    startActivityForResult(mIntent , REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), "Workout list is empty!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        /**
         * default loading
         */
        callApiVideo("excerise");


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
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void callApiVideo(String category) {
        progressDialog.show();
        /**
         * a) mobility      1
         * b) strtching     2
         * c) excerise      3
         */
        String workout_code = "0";
        if(category.equalsIgnoreCase("mobility")){
            workout_code = "1";

        }else  if(category.equalsIgnoreCase("strtching")){
            workout_code = "2";

        }else  if(category.equalsIgnoreCase("excerise")){
            workout_code = "3";

        }
        Log.d("Category--->", category);
        AndroidNetworking.post(Config.PLANNER_VIDEO)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("workout_code", workout_code)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
                                dataOneList.clear();
                                dataAdapterOne.notifyDataSetChanged();
                                JSONArray mJsonArray = response.getJSONArray("data");
                                tv_available_videos.setText(mJsonArray.length()+" exercises matches in this");
                                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                    Planner mPlanner = new Planner(
                                            response.getJSONArray("data").getJSONObject(i).getString("workout_name"),
                                            response.getJSONArray("data").getJSONObject(i).getString("work_out_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("body_part_code"),
                                            response.getJSONArray("data").getJSONObject(i).getString("comment"),
                                            response.getJSONArray("data").getJSONObject(i).getString("url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("thumbnail_url"),
                                            response.getJSONArray("data").getJSONObject(i).getString("title"),
                                            response.getJSONArray("data").getJSONObject(i).getString("description"),
                                            response.getJSONArray("data").getJSONObject(i).getString("id"),
                                            response.getJSONArray("data").getJSONObject(i).getString("is_like")
                                    );

                                    dataOneList.add(mPlanner);

                                }
                            } else {
                                dataOneList.clear();
                                dataAdapterOne.notifyDataSetChanged();
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }

                            dataAdapterOne.notifyDataSetChanged();
                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            dataOneList.clear();
                            dataAdapterOne.notifyDataSetChanged();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        dataOneList.clear();
                        dataAdapterOne.notifyDataSetChanged();
                        Toast.makeText(getActivity(), R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        View selectedView = (View) dragEvent.getLocalState();
        RecyclerView selectedRecyclerView = (RecyclerView) view;
        int currentPosition = -1;
        try {
            currentPosition = recyclerViewOne.getChildAdapterPosition(selectedView);
            //Ensure the position is valid
            if (currentPosition != -1) {
                videoToMove = dataOneList.get(currentPosition);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION:
                View onTopOf = selectedRecyclerView.findChildViewUnder(dragEvent.getX(), dragEvent.getY());
                newContactPosition = selectedRecyclerView.getChildAdapterPosition(onTopOf);
                Log.d("newContactPosition----->", "" + newContactPosition);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                //when item is droppe off to recyclerview
                dataTwoList.add(videoToMove);
                dataAdapterTwo.notifyDataSetChanged();

                /**
                 * hide drag and drop text
                 */
                ll_drag_n_drop.setVisibility(View.GONE);

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                selectedView.setVisibility(View.VISIBLE);
                if (newContactPosition != -1) {
                    selectedRecyclerView.scrollToPosition(newContactPosition);
                    newContactPosition = 1;
                } else {
                    selectedRecyclerView.scrollToPosition(0);
                }

            default:
                break;

        }

       // Log.d("data----->", dragEvent.getLocalState().toString());


        return true;
    }
}
