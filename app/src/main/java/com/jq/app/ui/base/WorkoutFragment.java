package com.jq.app.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.facebook.android.Util;
import com.facebook.internal.Utility;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.local_helpers.LocalVideoHelper;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.exercise.ExerciseActivity;
import com.jq.app.ui.my_workout.Workout;
import com.jq.app.ui.upload.SelectBodyPartActivity;
import com.jq.app.ui.views.CustomVideoView;
import com.jq.app.util.Config;
import com.jq.app.util.FileUtils;
import com.jq.app.util.UploadFileToServer;
import com.jq.app.util.base.BaseActivity;
import com.jq.app.util.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Hasnain on 16-Feb-18.
 */
public class WorkoutFragment extends BaseFragment implements UploadFileToServer.CompletionListener {

    private static final String TAG = WorkoutFragment.class.getSimpleName();
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    JZVideoPlayerStandard videoView;
    private static final String VIDEO_DIRECTORY = "/demonuts";
    private ProgressDialog progressDialog;
    ImageView iv_play, iv_toShow;
    TextView tv_intermediate, tv_advanced, tv_pro, tv_base;
    RecyclerView category_recyclerview;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    Context mContext;
    String category = "";
    String userStatusLevel = "1";
    int whichposition = 0;
    String bodyPart;

    private String image_video;
    private Uri uri;


    public WorkoutFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WorkoutFragment newInstance(String param1, String param2) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void updateArgs(Bundle bundle) {
        uri = bundle.getParcelable("uri");
        image_video = bundle.getString("image_video");
        bodyPart = bundle.getString("body_part");
        setUpView(uri, image_video);
    }

    @Override
    public void onCompleted(String message) {
        showToast(message);
        hideUploadingProgressDialog();
        ActivityCompat.finishAffinity(getActivity());
        startActivity(new Intent(getActivity(), MainActivity.class));
     /*   getActivity().finish();
        SelectBodyPartActivity.needToclose = true;*/
    }

    @Override
    public void onFailed(String msg) {
        showToast(msg);
        hideUploadingProgressDialog();
    }


    public interface OnSubCategoryListerner {
        // This can be any number of events to be sent to the activity
        public void subCategoryClicked(String Category, int position, String userStatusLevel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        videoView = view.findViewById(R.id.vv);
        iv_play = view.findViewById(R.id.iv_play);
        iv_toShow = view.findViewById(R.id.iv);
        tv_intermediate = view.findViewById(R.id.tv_intermediate);
        tv_advanced = view.findViewById(R.id.tv_advanced);
        tv_base = view.findViewById(R.id.tv_base);
        category_recyclerview = view.findViewById(R.id.category_recyclerview);
        category_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category("SPORTS", false));
        categoryArrayList.add(new Category("MOBILITY", false));
        categoryArrayList.add(new Category("EXERCISE", false));
        categoryArrayList.add(new Category("STRETCHING", false));
        categoryArrayList.add(new Category("CARDIO", false));

        categoryAdapter = new CategoryAdapter(mContext, categoryArrayList, new onCategorySelectionListner());
        category_recyclerview.setAdapter(categoryAdapter);
        tv_pro = view.findViewById(R.id.tv_proo);


        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category == null || category.isEmpty()) {
                    showToast("Please select category.");
                    return;
                }
                if (userStatusLevel == null || userStatusLevel.isEmpty()) {
                    showToast("Please select level.");
                    return;
                }
                uploadImageOrVideo();
                //listener.subCategoryClicked(category, whichposition, userStatusLevel);
            }
        });


        tv_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_pro.setTextColor(getResources().getColor(R.color.red));

                tv_intermediate.setTextColor(getResources().getColor(R.color.black));

                tv_advanced.setTextColor(getResources().getColor(R.color.black));

                tv_base.setTextColor(getResources().getColor(R.color.black));
                userStatusLevel = "4";
            }
        });

        tv_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_pro.setTextColor(getResources().getColor(R.color.black));

                tv_intermediate.setTextColor(getResources().getColor(R.color.black));

                tv_advanced.setTextColor(getResources().getColor(R.color.black));

                tv_base.setTextColor(getResources().getColor(R.color.red));
                userStatusLevel = "1";
            }
        });

        tv_intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_intermediate.setTextColor(getResources().getColor(R.color.red));

                tv_advanced.setTextColor(getResources().getColor(R.color.black));

                tv_pro.setTextColor(getResources().getColor(R.color.black));

                tv_base.setTextColor(getResources().getColor(R.color.black));
                userStatusLevel = "2";
            }
        });

        tv_advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_intermediate.setTextColor(getResources().getColor(R.color.black));

                tv_advanced.setTextColor(getResources().getColor(R.color.red));

                tv_pro.setTextColor(getResources().getColor(R.color.black));

                tv_base.setTextColor(getResources().getColor(R.color.black));
                userStatusLevel = "3";
            }
        });
        uri = getArguments().getParcelable("uri");
        image_video = getArguments().getString("image_video");
        bodyPart = getArguments().getString("body_part");
        setUpView(uri, image_video);

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_play.setVisibility(View.GONE);
                videoView.startButton.performClick();
                iv_toShow.setVisibility(View.GONE);
                /*   if (!videoView.isPlaying()) {
                    videoView.start();
                }
                iv_play.setVisibility(View.GONE);
//                iv_pause.setVisibility(View.VISIBLE);*/
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                videoView.pause();
              /*  if (!videoView.isPlaying()) {
                    videoView.start();
                    iv_play.setVisibility(View.GONE);
                } else {
                    videoView.pause();
                    iv_play.setVisibility(View.VISIBLE);
                }*/
            }
        });
       /* videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                iv_play.setVisibility(View.VISIBLE);
            }
        });*/

        setupProgressBar();
        //loadData();
        return view;
    }

    private void setUpView(Uri uri, String type) {

        if (type.equals("video")) {
            // videoView.setVisibility(View.GONE);
            iv_play.setVisibility(View.VISIBLE);
            iv_toShow.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            Glide.with(iv_toShow.getContext()).load(FileUtils.getPath(getActivity(), uri)).into(iv_toShow);
            videoView.setUp(FileUtils.getPath(getActivity(), uri), JZVideoPlayer.SCREEN_WINDOW_NORMAL);
            /*   Bitmap thumb = ThumbnailUtils.createVideoThumbnail("" + uri,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
            videoView.setBackgroundDrawable(bitmapDrawable);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.pause();*/
        } else {
            iv_toShow.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            iv_play.setVisibility(View.GONE);
            try {
                ((BitmapDrawable) iv_toShow.getDrawable()).getBitmap().recycle();
//                iv_toShow.setImageBitmap(null);
                URL url = new URL("" + uri);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                iv_toShow.setImageBitmap(bmp);
            } catch (Exception e) {

            }
        }
    }

    public void showDotsCategoryDialog() {
        new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity())
                .title(R.string.dots_category)
                .items(R.array.dot_sub_categories)
                .itemsCallback(new com.afollestad.materialdialogs.MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        chosenBodyPartCategory(which, text);
                    }
                })
                .show();
    }

    public void chosenBodyPartCategory(int which, CharSequence text) {

    }

    public class onCategorySelectionListner {
        public void onSelect(int pos) {
            for (int i = 0; i < categoryArrayList.size(); i++) {
                if (i == pos) {
                    category = categoryArrayList.get(i).getCat_name();
                    whichposition = pos;
                    categoryArrayList.get(i).setSelected_status(true);
                } else {
                    categoryArrayList.get(i).setSelected_status(false);
                }
                categoryAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void saveVideoToInternalStorage(String filePath) {
        File newfile;
        try {
            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            if (currentFile.exists()) {

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                                            "", mJsonArray.getJSONObject(i).getString("planner_id")

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
//            rv_workout_list.setAdapter(new WorkoutAdapter(workoutArrayList, new WorkoutAdapter.ListSelect() {
//                @Override
//                public void onListSelected(Workout workout) {
////                    Log.e(TAG,workout.getTitle());
////                    Toast.makeText(getActivity(), workout.getTitle(), Toast.LENGTH_SHORT).show();
//                    /**
//                     * get video and go to play
//                     */
//                    getVideoInList(workout.getTitle());
//
//                }
//
//                @Override
//                public void onLongPress(final Workout workout) {
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
//
//                }
//            }));

        } else {
            Toast.makeText(getActivity(), "Opps No workout find!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getVideoInList(String planner_title) {
        progressDialog.show();
        AndroidNetworking.post(Config.PLANNER_WORKOUT)
                .addBodyParameter("email", App.my_email)
                .addBodyParameter("planner_title", planner_title)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int status = response.getInt("status");
                            if (status == 1) {
                                //Log.e(TAG, "response : " + response);
//                                ArrayList<VideoInList> videoInListArrayList = new ArrayList<>();
                                ArrayList<VideoModel> videoInListArrayList = new ArrayList<>();
                                JSONArray mJsonArray = response.getJSONArray("data");
                                for (int i = 0; i < mJsonArray.length(); i++) {

                                    Favorite favorite = new Favorite();
                                    favorite.video_id = mJsonArray.getJSONObject(i).getString("id");
                                    favorite.workout_name = mJsonArray.getJSONObject(i).getString("workout_name");
                                    favorite.work_out_code = mJsonArray.getJSONObject(i).getString("work_out_code");
                                    favorite.body_part_code = mJsonArray.getJSONObject(i).getString("body_part_code");
                                    favorite.title = mJsonArray.getJSONObject(i).getString("title");
                                    favorite.thumbnail_url = mJsonArray.getJSONObject(i).getString("thumbnail_url");
                                    favorite.description = mJsonArray.getJSONObject(i).getString("description");
                                    favorite.url = mJsonArray.getJSONObject(i).getString("url");
                                    favorite.comment = mJsonArray.getJSONObject(i).getString("description");
                                    favorite.m_user_id = App.my_email;
                                    favorite.localPath = "";


//                                    videoInListArrayList.add(new VideoInList(
//                                            mJsonArray.getJSONObject(i).getString("id") ,
//                                            mJsonArray.getJSONObject(i).getString("url") ,
//                                            mJsonArray.getJSONObject(i).getString("body_part_code") ,
//                                            mJsonArray.getJSONObject(i).getString("work_out_code") ,
//                                            mJsonArray.getJSONObject(i).getString("thumbnail_url")
//                                    ));

                                    videoInListArrayList.add(new VideoModel(favorite));
                                }

                                /**
                                 * check video availability
                                 */
                                if (videoInListArrayList.size() > 0) {
                                    Log.e(TAG, "videoInListArrayList.size() : " + videoInListArrayList.size());

                                    /**
                                     * Open ExerciseActivity
                                     */
                                    Intent i = new Intent(getActivity(), ExerciseActivity.class);
                                    i.putExtra("video_in_list_array", videoInListArrayList);
                                    i.putExtra("is_my_workout", true);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(getActivity(), "Opps No video in selected workout!", Toast.LENGTH_SHORT).show();
                                }


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


    private void uploadImageOrVideo() {
        showUploadingProgressDialog("Uploading...");
        String params = "?email=" + App.my_email + "&catagory=" + LocalVideoHelper.getCalegoryType(category.toLowerCase()) + "&bodypart=" + bodyPart + "&user_status=" + userStatusLevel;
        UploadFileToServer task = new UploadFileToServer((BaseActivity) getActivity(), image_video, FileUtils.getPath(getActivity(), uri), params, this);
        task.execute();
    }
}

