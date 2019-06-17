package com.jq.app.ui.exercise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.InputCallback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.base.BaseMainActivity;
import com.jq.app.ui.chat.ChatActivity;
import com.jq.app.ui.createplanner.FilterDialog;
import com.jq.app.ui.createplanner.PreparePlannerActivity;
import com.jq.app.ui.exercise.adapter.ExerciseAdapter;
import com.jq.app.ui.exercise.adapter.ItemMoveCallback;
import com.jq.app.ui.exercise.pojo.DataItem;
import com.jq.app.ui.exercise.pojo.ExerciseResponse;
import com.jq.app.ui.exercise.pojo.ExerciseResponse.Data;
import com.jq.app.ui.exercise.pojo.ExerciseResponse.Data.Video;
import com.jq.app.ui.exercise.pojo.HeaderValuesItem;
import com.jq.app.ui.exercise.pojo.SaveWorkout;
import com.jq.app.ui.exercise.pojo.VideoItem;
import com.jq.app.ui.search.SearchInAppActivity;
import com.jq.app.ui.timer.SelectTimerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseListActivity extends BaseMainActivity {
    private Context context;
    private ExerciseAdapter exerciseAdapter;
    private ArrayList<Data> mValues = new ArrayList();
    private ProgressDialog progressDialog;

    /* renamed from: com.jq.app.ui.exercise.ExerciseListActivity$1 */
    class C14801 implements OnClickListener {
        C14801() {
        }

        public void onClick(View v) {
            ExerciseListActivity.this.saveVideoInList();
        }
    }

    /* renamed from: com.jq.app.ui.exercise.ExerciseListActivity$2 */
    class C14812 implements OnClickListener {
        C14812() {
        }

        public void onClick(View v) {
            ExerciseListActivity.this.addNote();
        }
    }

    /* renamed from: com.jq.app.ui.exercise.ExerciseListActivity$4 */
    class C14824 implements InputCallback {
        C14824() {
        }

        public void onInput(MaterialDialog dialog, CharSequence input) {
            if (input == null || input.toString().isEmpty()) {
                Toast.makeText(ExerciseListActivity.this.getApplicationContext(), "Please enter note", Toast.LENGTH_SHORT).show();
            } else {
                ExerciseListActivity.this.addNotesAPI(ExerciseListActivity.this.getIntent().getStringExtra("planner_id"), input.toString());
            }
        }
    }

    class SortByCategoryNo implements Comparator<Data> {
        SortByCategoryNo() {
        }

        public int compare(Data a, Data b) {
            return a.getCat_seq() - b.getCat_seq();
        }
    }

    class SortByWorkoutNo implements Comparator<Video> {
        SortByWorkoutNo() {
        }

        public int compare(Video a, Video b) {
            return a.getWork_out_seq() - b.getWork_out_seq();
        }
    }

    /* renamed from: com.jq.app.ui.exercise.ExerciseListActivity$3 */
    class C16183 extends ApiCallback<JsonObject> {
        C16183() {
        }

        public void onSuccess(JsonObject jsonObject) {
            try {
                Log.e("saveVideoInList", jsonObject.toString());
                ExerciseListActivity.this.progressDialog.dismiss();
            } catch (Exception e) {
                ExerciseListActivity.this.progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            ExerciseListActivity.this.progressDialog.dismiss();
        }

        public void onTokenExpire() {
        }
    }

    /* renamed from: com.jq.app.ui.exercise.ExerciseListActivity$5 */
    class C16195 extends ApiCallback<JsonObject> {
        C16195() {
        }

        public void onSuccess(JsonObject jsonObject) {
            try {
                ExerciseListActivity.this.progressDialog.dismiss();
            } catch (Exception e) {
                ExerciseListActivity.this.progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            ExerciseListActivity.this.progressDialog.dismiss();
        }

        public void onTokenExpire() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        this.context = this;
        init();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = df.format(c);
        ((TextView)findViewById(R.id.today_date)).setText(todayDate);


        findViewById(R.id.search).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ExerciseListActivity.this, SearchInAppActivity.class);
                i.putExtra("email", App.my_email);
                startActivity(i);

            }
        });
    }

    FilterDialog dialog;

    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (v.getId() == R.id.action_clock || v.getId() == R.id.timer_list_icon) {
            startActivity(new Intent(this, SelectTimerActivity.class));
        } else if (v.getId() == R.id.action_home) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.action_chat) {

            if (App.isLoginedOnFirebase) {
                startActivity(new Intent(this, ChatActivity.class));

            } else {
                showProgressDialog();
                loginFirebase();
            }
        } else if (v.getId() == R.id.filter) {

            dialog = new FilterDialog(this, new FilterDialog.Callback() {
                @Override
                public void onApply(String category, String parts, String equipments) {

                    Intent intent = new Intent(ExerciseListActivity.this, PreparePlannerActivity.class);
                    intent.putExtra("from_slider", true);
                    intent.putExtra("parts", parts);
                    intent.putExtra("equipments", equipments);
                    intent.putExtra("category", category);
                    startActivity(intent);

                }
            });
            dialog.show();
        }
    }

    private void init() {
        this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setMessage("Please wait....");
        this.progressDialog.setCancelable(false);
        this.exerciseAdapter = new ExerciseAdapter(this.mValues);
        ArrayList<Data> data = (ArrayList) getIntent().getSerializableExtra("video_in_list_array");


        if (data.size() == 0) {

            ArrayList<ExerciseResponse.Data> dataList = new ArrayList<>();

            ExerciseResponse exerciseResponse = new ExerciseResponse();

            ExerciseResponse.Data data1 = exerciseResponse.new Data();
            data1.setWork_out_code(1);

            ExerciseResponse.Data data2 = exerciseResponse.new Data();
            data2.setWork_out_code(2);

            ExerciseResponse.Data data3 = exerciseResponse.new Data();
            data3.setWork_out_code(3);

            ExerciseResponse.Data data4 = exerciseResponse.new Data();
            data4.setWork_out_code(4);

            ExerciseResponse.Data data5 = exerciseResponse.new Data();
            data5.setWork_out_code(5);

            dataList.add(data1);
            dataList.add(data2);
            dataList.add(data3);
            dataList.add(data4);
            dataList.add(data5);

            data.addAll(dataList);
        }


        Collections.sort(data, new SortByCategoryNo());
        sortVideos(data);

        this.mValues.clear();
        this.mValues.addAll(data);
        RecyclerView video_list = (RecyclerView) findViewById(R.id.video_list);
        video_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        new ItemTouchHelper(new ItemMoveCallback(this.exerciseAdapter)).attachToRecyclerView(video_list);
        video_list.setAdapter(this.exerciseAdapter);
        findViewById(R.id.save_workout).setOnClickListener(new C14801());
        findViewById(R.id.save_note).setOnClickListener(new C14812());

        if (getIntent().getExtras().getString("planner_id") == null || getIntent().getExtras().getString("planner_id").equalsIgnoreCase("")) {
            //findViewById(R.id.search_view).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_button).setVisibility(View.GONE);
        } else {
//            findViewById(R.id.search_view).setVisibility(View.GONE);
            findViewById(R.id.ll_button).setVisibility(View.VISIBLE);
        }
    }

    private void saveVideoInList() {
        this.progressDialog.show();
        ApiCallback apiCallback = new C16183();
        SaveWorkout saveWorkout = new SaveWorkout();
        saveWorkout.setEmail(getIntent().getStringExtra("email"));
        saveWorkout.setPlanid(getIntent().getStringExtra("planner_id"));
        List<DataItem> dataS = new ArrayList();
        for (int i = 0; i < this.mValues.size(); i++) {
            DataItem dataItem = new DataItem();
            dataItem.setCatOrder(i);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(((Data) this.mValues.get(i)).getWork_out_code());
            stringBuilder.append("");
            dataItem.setWorkOutCode(stringBuilder.toString());
            List<VideoItem> dataVideos = new ArrayList();
            for (int j = 0; j < ((Data) this.mValues.get(i)).getVideo().size(); j++) {
                VideoItem videoItem = new VideoItem();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(((Video) ((Data) this.mValues.get(i)).getVideo().get(j)).getVideo_id());
                stringBuilder2.append("");
                videoItem.setId(stringBuilder2.toString());
                videoItem.setOrderNumber(j);
                HeaderValuesItem headerValuesItem = new HeaderValuesItem();
                headerValuesItem.setH1(((Video) ((Data) this.mValues.get(i)).getVideo().get(j)).getHeader_values()[0]);
                headerValuesItem.setH2(((Video) ((Data) this.mValues.get(i)).getVideo().get(j)).getHeader_values()[1]);
                headerValuesItem.setH3(((Video) ((Data) this.mValues.get(i)).getVideo().get(j)).getHeader_values()[2]);
                headerValuesItem.setH4(((Video) ((Data) this.mValues.get(i)).getVideo().get(j)).getHeader_values()[3]);
                headerValuesItem.setH5(((Video) ((Data) this.mValues.get(i)).getVideo().get(j)).getHeader_values()[4]);
                videoItem.setHeaderValues(headerValuesItem);
                dataVideos.add(videoItem);
            }
            dataItem.setVideo(dataVideos);
            dataS.add(dataItem);
        }
        saveWorkout.setData(dataS);
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(" -- ");
        stringBuilder3.append(new Gson().toJson(saveWorkout));

        Log.e("dada save ", stringBuilder3.toString());

        addCall(((ApiStores) ApiClient.retrofit(this.context).create(ApiStores.class)).whiteboard_save(saveWorkout), apiCallback);
    }

    private void sortVideos(ArrayList<Data> data) {
        for (int i = 0; i < data.size(); i++) {
            Collections.sort(((Data) data.get(i)).getVideo(), new SortByWorkoutNo());
        }
    }

    private void addNote() {
        new Builder(this.context).title("Enter Note")
                .inputType(1)
                .input("Write here...", "",
                        new C14824())
                .cancelable(true)
                .show();
    }

    private void addNotesAPI(String planner_id, String note) {
        this.progressDialog.show();
        ApiCallback apiCallback = new C16195();
        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("planner_id", planner_id);
        map.put("notes", note);
        addCall(((ApiStores) ApiClient.retrofit(getApplicationContext()).create(ApiStores.class)).addNotes(map), apiCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            if (dialog != null) {
                dialog.setSelecedParts(data.getStringExtra("parts"));
            }
        }
    }
}
