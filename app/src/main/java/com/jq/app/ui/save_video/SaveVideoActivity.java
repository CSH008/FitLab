package com.jq.app.ui.save_video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.PlannerNew.model.Planner;
import com.jq.app.ui.createplanner.NoteListDialog;
import com.jq.app.util.Config;
import com.jq.chatsdk.activities.VideoActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveVideoActivity extends AppCompatActivity {
    private static final String TAG = SaveVideoActivity.class.getSimpleName();
    private Activity mActivity;
    private ImageView btn_save;
    private ImageView btn_back;
    private AutoCompleteTextView act_workout_list_name;
    private RecyclerView recyclerView;
    private ArrayList<Planner> videoArrayList;
    private ProgressDialog progressDialog;
    List<String> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_video);
        mActivity = this;
        init();
        setupProgressBar();

        /**
         * get video list from intent
         */
        videoArrayList = getIntent().getExtras().getParcelableArrayList("video_list");
        if (videoArrayList != null && videoArrayList.size() > 0) {
            /**
             * Remove -1 id from array list
             */
            for (int i = 0; i < videoArrayList.size(); i++) {
                Planner planner = videoArrayList.get(i);
                if (planner.getId().equalsIgnoreCase("-1")) {
                    videoArrayList.remove(i);
                    break;
                }
            }


            /**
             * set video in recycle view
             */
            recyclerView.setAdapter(new SaveVideoAdapter(videoArrayList, new SaveVideoAdapter.VideoClick() {
                @Override
                public void onVideoClick(Planner planner) {

                    /**
                     * play clicked video
                     */
                    if (!planner.getUrl().isEmpty() && !planner.getUrl().equalsIgnoreCase("")) {
                        Intent intent = new Intent(mActivity, VideoActivity.class);
                        intent.putExtra(VideoActivity.FILE_PATH, planner.getUrl());
                        startActivity(intent);
                    } else {
                        Toast.makeText(mActivity, "Video stream url not found", Toast.LENGTH_SHORT).show();
                    }

                }
            }));

        } else {
            Toast.makeText(mActivity, "Video list not found", Toast.LENGTH_SHORT).show();
        }

        /**
         * Back button click
         */
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * save button click
         */
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String video_id = "";
                String planner_title = act_workout_list_name.getText().toString().trim();

                if (planner_title.equalsIgnoreCase("")) {
                    Toast.makeText(mActivity, "Please enter planner title", Toast.LENGTH_SHORT).show();

                } else {
                    for (int i = 0; i < videoArrayList.size(); i++) {
                        Planner mVideo = videoArrayList.get(i);
                        video_id = video_id + mVideo.getId() + ",";
                    }
                    video_id = video_id.substring(0, video_id.length() - 1);
                    Log.e(TAG, "video_id : " + video_id);
                    Log.e(TAG, "planner_title : " + planner_title);

                    saveVideoList(video_id, planner_title);

                }
            }
        });
          notes=getIntent().getStringArrayListExtra("notes");
        findViewById(R.id.btn_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NoteListDialog(SaveVideoActivity.this, notes, new NoteListDialog.Callback() {
                    @Override
                    public void onNotesList(List<String> notes) {
                        SaveVideoActivity.this.notes = notes;
                    }
                }).show();
            }
        });
    }

    private void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        recyclerView = findViewById(R.id.rv_video);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        act_workout_list_name = findViewById(R.id.act_workout_list_name);
    }


    public void setupProgressBar() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
    }


    public void saveVideoList(String video_id, String planner_title) {
        progressDialog.show();
        String notesString = "";
        int noteCount = 0;
        if (notes.size() > 0) {
            notesString = TextUtils.join("@$#", notes);
            noteCount = notes.size();
        }

        AndroidNetworking.post(Config.SAVE_VIDEO_LIST)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("video_id", video_id)
                .addBodyParameter("planner_title", planner_title)
                .addBodyParameter("notes", notesString)
                .addBodyParameter("total_notes", String.valueOf(noteCount))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                                ActivityCompat.finishAffinity(SaveVideoActivity.this);
                                startActivity(new Intent(SaveVideoActivity.this,MainActivity.class));
                                /**
                                 * write code after save sucess where you want to go
                                 */
                            /*    Intent intent = getIntent();
                                intent.putExtra("key", "SaveVideoActivity");
                                setResult(404, intent);
                                finish();*/

                            } else {
                                Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                            }




                        } catch (Exception e) {
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
