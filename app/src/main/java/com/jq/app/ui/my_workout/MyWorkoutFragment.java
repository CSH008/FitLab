package com.jq.app.ui.my_workout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AppEventsConstants;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.VideoModel;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.ui.exercise.ExerciseActivity;
import com.jq.app.ui.exercise.ExerciseListActivity;
import com.jq.app.ui.exercise.pojo.ExerciseResponse;
import com.jq.app.util.Config;
import com.jq.app.util.base.BaseFragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hasnain on 16-Feb-18.
 */

public class MyWorkoutFragment extends BaseFragment {
    private static final String TAG = MyWorkoutFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rv_workout_list;
    private ProgressDialog progressDialog;

    public MyWorkoutFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyWorkoutFragment newInstance(String param1, String param2) {
        MyWorkoutFragment fragment = new MyWorkoutFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.fragment_my_workout, container, false);
        rv_workout_list = view.findViewById(R.id.rv_workout_list);
        rv_workout_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupProgressBar();
        loadData();


        return view;
    }

    public void setupProgressBar() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Workout");
    }

    private void loadData() {
        progressDialog.show();

        AndroidNetworking.post(Config.SAVED_PLANNER__LIST)
                .addBodyParameter("email", App.my_email)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
                                ArrayList<Workout> workoutArrayList = new ArrayList<>();
                                //Log.e(TAG, "response : " + response);
                                JSONArray mJsonArray = response.getJSONArray("data");
                                for (int i = 0; i < mJsonArray.length(); i++) {

                                    workoutArrayList.add(new Workout(
                                            mJsonArray.getJSONObject(i).getString("planner_title"),
                                            "",
                                            mJsonArray.getJSONObject(i).getString("video_listed"),
                                            "",
                                            mJsonArray.getJSONObject(i).getString("planner_id")
                                    ));
                                }

                                populateWorkoutList(workoutArrayList);

                            } else {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                            }


                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void populateWorkoutList(ArrayList<Workout> workoutArrayList) {
        if (workoutArrayList.size() > 0) {
            rv_workout_list.setAdapter(new WorkoutAdapter(workoutArrayList, new WorkoutAdapter.ListSelect() {
                @Override
                public void onListSelected(Workout workout) {
//                    Log.e(TAG,workout.getTitle());
//                    Toast.makeText(getActivity(), workout.getTitle(), Toast.LENGTH_SHORT).show();
                    /**
                     * get video and go to play
                     */
                    getVideoInList(workout.getTitle(), workout.getPlanner_id());

                }

                @Override
                public void onLongPress(final Workout workout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    deleteWorkoutList(workout.getTitle());

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();

                }
            }));

        } else {
            Toast.makeText(getActivity(), "Opps No workout find!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getVideoInList(final String planner_title, final String planner_id) {
        progressDialog.show();
        final ApiCallback apiCallback = new ApiCallback<ExerciseResponse>() {
            public void onSuccess(ExerciseResponse jsonObject) {
                try {
                    if (!jsonObject.getStatus().equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
                        Toast.makeText(MyWorkoutFragment.this.mActivity, jsonObject.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getData().size() > 0) {
                        String access$400 = MyWorkoutFragment.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("videoInListArrayList.size() : ");
                        stringBuilder.append(jsonObject.getData().size());
                        Log.e(access$400, stringBuilder.toString());
                        Intent i = new Intent(MyWorkoutFragment.this.getActivity(), ExerciseListActivity.class);
                        i.putExtra("video_in_list_array", jsonObject.getData());
                        i.putExtra("is_my_workout", true);
                        i.putExtra("planner_title", planner_title);
                        i.putExtra("planner_id", planner_id);
                        i.putExtra("email", App.my_email);
                        MyWorkoutFragment.this.startActivity(i);
                    } else {
                        Toast.makeText(MyWorkoutFragment.this.getActivity(), "Opps No video in selected workout!", Toast.LENGTH_SHORT).show();
                    }
                    MyWorkoutFragment.this.progressDialog.dismiss();
                } catch (Exception e) {
                    MyWorkoutFragment.this.progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            public void onFailure(String msg) {
                Log.e("onFailure", msg);
            }

            public void onFinish() {
                MyWorkoutFragment.this.progressDialog.dismiss();
            }

            public void onTokenExpire() {
            }
        };

        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("planner_title", planner_title);
        map.put("planner_id", planner_id);
        ApiClient.retrofit(getContext()).create(ApiStores.class).getPlanner_workout(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer() {
                    public void onSubscribe(Disposable d) {
                    }

                    public void onNext(Object o) {
                        apiCallback.onNext(o);
                    }

                    public void onError(Throwable e) {
                        apiCallback.onError(e);
                    }

                    public void onComplete() {
                        apiCallback.onComplete();
                    }
                });
    }
    private void deleteWorkoutList(String planner_title) {
        progressDialog.show();
        AndroidNetworking.post(Config.DELETE_WORKOUT)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("planner_title", planner_title)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                                loadData();

                            } else {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                            }


                            progressDialog.dismiss();

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}

