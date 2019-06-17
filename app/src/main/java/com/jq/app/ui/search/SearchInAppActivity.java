package com.jq.app.ui.search;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.ui.exercise.ExerciseListActivity;
import com.jq.app.ui.exercise.pojo.ExerciseResponse;
import com.jq.app.ui.my_workout.Workout;
import com.jq.app.ui.my_workout.WorkoutAdapter;
import com.jq.app.ui.timer.ThankYou;
import com.jq.app.util.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchInAppActivity extends BaseActivity {
    private static final String TAG = SearchInAppActivity.class.getSimpleName();
    private EditText et_search;
    private RecyclerView rv_search_result;
    private Activity mActivity;
    private Timer mTimer = new Timer();
    private final long DELAY = 600; // in ms

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_app);
        mActivity = this;
        init();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                populateWorkoutList(new ArrayList<Workout>());

            }
        });


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(final Editable search_key) {
                try {
                    if (mTimer != null) {
                        mTimer.cancel();
                    }

                    if (search_key.toString().length() > 2) {
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        search(search_key.toString());
                                    }
                                });
                            }
                        }, DELAY);
                    }else{
                        populateWorkoutList(new ArrayList<Workout>());
                    }

                } catch (Exception e) {
                    search(search_key.toString());
                    e.printStackTrace();

                }

            }
        });


    }

    private void init() {
        et_search = findViewById(R.id.et_search);
        rv_search_result = findViewById(R.id.rv_search_result);
        rv_search_result.setLayoutManager(new LinearLayoutManager(mActivity));

    }

    private void search(String search_key) {
        Log.e(TAG, "search_key : " + search_key);
        showProgressDialog();
        ApiCallback apiCallback = new C16232();

        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("search_title", search_key);
        addCall(((ApiStores) ApiClient.retrofit(this).create(ApiStores.class)).searchPlanner(map), apiCallback);

    }

    class C16232 extends ApiCallback<JsonObject> {

        public void onSuccess(JsonObject res) {
            try {
                JSONObject jsonObject = new JSONObject(res.toString());
                Log.e(TAG, "Response : " + jsonObject);

                if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                    ArrayList<Workout> workoutArrayList = new ArrayList<>();
                    JSONArray mJsonArray = jsonObject.getJSONArray("data");
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

                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {

                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(mActivity, "Someting went wrong", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                hideProgressDialog();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            hideProgressDialog();
        }

        public void onTokenExpire() {
        }
    }

    private void populateWorkoutList(ArrayList<Workout> workoutArrayList) {

            rv_search_result.setAdapter(new WorkoutAdapter(workoutArrayList, new WorkoutAdapter.ListSelect() {
                @Override
                public void onListSelected(Workout workout) {

                    getVideoInList(workout.getTitle(), workout.getPlanner_id());

                }

                @Override
                public void onLongPress(final Workout workout) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//                    builder.setMessage("Are you sure you want to delete?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.dismiss();
//                                    deleteWorkoutList(workout.getTitle());
//
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // User cancelled the dialog
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.create().show();

                }
            }));


    }

    private void getVideoInList(final String planner_title, final String planner_id) {
        showProgressDialog();
        final ApiCallback apiCallback = new ApiCallback<ExerciseResponse>() {
            public void onSuccess(ExerciseResponse jsonObject) {
                try {
                    if (!jsonObject.getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(mActivity, jsonObject.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getData().size() > 0) {

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("videoInListArrayList.size() : ");
                        stringBuilder.append(jsonObject.getData().size());
                        Log.e(TAG, stringBuilder.toString());
                        Intent i = new Intent(mActivity, ExerciseListActivity.class);
                        i.putExtra("video_in_list_array", jsonObject.getData());
                        i.putExtra("is_my_workout", true);
                        i.putExtra("planner_title", planner_title);
                        i.putExtra("planner_id", planner_id);
                        i.putExtra("email", App.my_email);
                        startActivity(i);
                    } else {
                        Toast.makeText(mActivity, "Opps No video in selected workout!", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog();
                } catch (Exception e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }

            public void onFailure(String msg) {
                Log.e("onFailure", msg);
            }

            public void onFinish() {
                hideProgressDialog();
            }

            public void onTokenExpire() {
            }
        };

        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("planner_title", planner_title);
        map.put("planner_id", planner_id);
        ApiClient.retrofit(mActivity).create(ApiStores.class).getPlanner_workout(map)
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
}
