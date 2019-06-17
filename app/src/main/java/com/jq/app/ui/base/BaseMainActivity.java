package com.jq.app.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.ui.chat.ChatActivity;
import com.jq.app.ui.exercise.ExerciseListActivity;
import com.jq.app.ui.exercise.pojo.ExerciseResponse;
import com.jq.app.ui.search.SearchInAppActivity;
import com.jq.app.ui.timer.SelectTimerActivity;
import com.jq.app.ui.upload.SelectBodyPartActivity;
import com.jq.app.util.UploadFileToServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public abstract class BaseMainActivity extends BaseMediaActivity implements UploadFileToServer.CompletionListener {

    public MImageChosenListener imageChosenListener;


    public interface MImageChosenListener {
        void chooseImage(Uri imageUri);
    }

    public void customTab(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
    }

    private View getTabView(int i) {
        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        switch (i) {
            case 0:
                imageView.setImageResource(R.mipmap.exercise_videos);
                break;
            case 1:
                imageView.setImageResource(R.mipmap.roller_videos);
                break;
            case 2:
                imageView.setImageResource(R.mipmap.stretching_videos);
                break;
            case 3:
                imageView.setImageResource(R.mipmap.sport_video);
                break;
            case 4:
                imageView.setImageResource(R.mipmap.cardio_video);
                break;
        }
        return imageView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_camera:
                if (requestCameraPermission()) {
                    showSelectImageDialog(true);
                }
                break;

            case R.id.action_chat:
                startChat();
                break;

                case R.id.action_clock:
                    startActivity(new Intent(this, SelectTimerActivity.class));
                break;
            case R.id.action_edit:


//                ArrayList<ExerciseResponse.Data> dataList=new ArrayList<>();
//
                ExerciseResponse exerciseResponse = new ExerciseResponse();
//
//                ExerciseResponse.Data data1=exerciseResponse.new Data();
//                data1.setWork_out_code(1);
//
//                ExerciseResponse.Data data2=exerciseResponse.new Data();
//                data2.setWork_out_code(2);
//
//                ExerciseResponse.Data data3=exerciseResponse.new Data();
//                data3.setWork_out_code(3);
//
//                ExerciseResponse.Data data4=exerciseResponse.new Data();
//                data4.setWork_out_code(4);
//
//                ExerciseResponse.Data data5=exerciseResponse.new Data();
//                data5.setWork_out_code(5);
//
//                dataList.add(data1);
//                dataList.add(data2);
//                dataList.add(data3);
//                dataList.add(data4);
//                dataList.add(data5);

//                Intent i = new Intent(BaseMainActivity.this, ExerciseListActivity.class);
//                i.putExtra("video_in_list_array", exerciseResponse.getData());
//                i.putExtra("is_my_workout", true);
//                i.putExtra("planner_title", "Workout");
//                i.putExtra("planner_id", "");
//                i.putExtra("email", App.my_email);
//                startActivity(i);

//                Intent i = new Intent(BaseMainActivity.this, SearchInAppActivity.class);
//                i.putExtra("email", App.my_email);
//                startActivity(i);


                showProgressDialog();
                final ApiCallback apiCallback = new ApiCallback<ExerciseResponse>() {
                    public void onSuccess(ExerciseResponse jsonObject) {
                        try {
                            Log.e(TAG, "Response : " + jsonObject);

                            if (!jsonObject.getStatus().equalsIgnoreCase("1")) {
                                Toast.makeText(BaseMainActivity.this, jsonObject.getMessage(), Toast.LENGTH_SHORT).show();

                            } else if (jsonObject.getStatus().equalsIgnoreCase("1")) {

                                Intent i = new Intent(BaseMainActivity.this, ExerciseListActivity.class);
                                i.putExtra("video_in_list_array", jsonObject.getData())
                                ;
                                i.putExtra("is_my_workout", true);
                                i.putExtra("planner_title", "planner_title");
                                i.putExtra("planner_id", jsonObject.getPlanner_id());
                                i.putExtra("email", App.my_email);
                                startActivity(i);
                            } else {
                                Toast.makeText(BaseMainActivity.this, "Opps No video in selected workout!", Toast.LENGTH_SHORT).show();
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

                ApiClient.retrofit(BaseMainActivity.this).create(ApiStores.class).prePlannerWorkout(map)
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

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void permissionGranted() {
        showSelectImageDialog(true);
    }

    private String type;
    private Uri imageUri;

    @Override
    public void updateImageViewWithUri(Uri uri) {
        imageUri = uri;
        type = "image";
        if (imageUri != null) {
            showBodyPartList();
        } else {
            showToast("Something went wrong.");
        }
       /* if (imageChosenListener != null) {
            imageChosenListener.chooseImage(uri);
        } else {
           if(this instanceof MainActivity){
               MainActivity activity= (MainActivity) this;
               activity.showSelecterFragment(imageUri,type);
           }

//            MyWorkoutFragment myWorkoutFragment = new MyWorkoutFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, myWorkoutFragment).commit();
            //showMediaCategoryDialog();
        }*/
    }

    private void showBodyPartList() {
        Intent intent = new Intent(this, SelectBodyPartActivity.class);
        intent.putExtra("uri", imageUri);
        intent.putExtra("type", type);
        startActivity(intent);
     /*   new com.afollestad.materialdialogs.MaterialDialog.Builder(this)
                .title(R.string.dots_category)
                .items(R.array.dot_sub_categories)
                .itemsCallback(new com.afollestad.materialdialogs.MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(com.afollestad.materialdialogs.MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        dialog.dismiss();
                        if (BaseMainActivity.this instanceof MainActivity) {
                            ((MainActivity) BaseMainActivity.this).showSelecterFragment(imageUri, type, text.toString());
                        }
                    }
                })
                .show();*/
    }

    @Override
    public void setVideo(final String filePath, String thumbnailPath) {
        imageUri = Uri.parse(filePath);
        type = "video";
        if (imageUri != null) {
            showBodyPartList();
        } else {
            showToast("Something went wrong.");
        }
      /*  if (this instanceof MainActivity) {
            MainActivity activity = (MainActivity) this;
            activity.showSelecterFragment(imageUri, type, userLevelStatus);
        }*/
//        showMediaCategoryDialog();
    }

    private String category;

    @Override
    public void chosenMediaCategory(int which, CharSequence text) {
        category = String.valueOf(text);
        //showDotsCategoryDialog(category,which);
    }

    private String sub_category;

    @Override
    public void chosenBodyPartCategory(int which, CharSequence text, String userStatusLevel) {
        //showProgressView(true);
      /*  showUploadingProgressDialog("Uploading...");
        sub_category = String.valueOf(text);
        String params = "?email=" + App.my_email + "&catagory=" + LocalVideoHelper.getWorkoutCode(category) + "&bodypart=" + sub_category + "&user_status=" + userStatusLevel;
        UploadFileToServer task = new UploadFileToServer(type, FileUtils.getPath(BaseMainActivity.this, imageUri), params, this);
        task.execute();
*/
        /*AndroidNetworking.upload("http://173.8.145.131:8080/1fitlab/jsp/video-upload.jsp") //Config.API_POST_IMAGE_UPLOAD
                .addMultipartParameter("email", email)
                .addMultipartParameter("subcategory", category)
                .addMultipartFile("image", file)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        showProgressView(false);
                        try {
                            String status = response.getString("status");
                            if(status.equals("success")) {
                                showToast("Succeed!");
                            } else {
                                showToast("Failed");
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                            showToast("Failed");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        showProgressView(false);
                        showToast("Failed");
                    }
                });*/
    }

    public void startChat() {
        if (App.isLoginedOnFirebase) {
            startActivity(new Intent(this, ChatActivity.class));

        } else {
            showProgressDialog();
            loginFirebase();
        }
    }

    @Override
    public void succeedFirebaseAuthenticate() {
        startActivity(new Intent(this, ChatActivity.class));
        hideProgressDialog();
    }

    @Override
    public void failedFirebaseAuthenticate() {
        showToast("Login Failed.");
        hideProgressDialog();
    }

    @Override
    public void onCompleted(String message) {
        //showProgressView(false);
        hideUploadingProgressDialog();
        showToast(message);
        onBackPressed();
    }

    @Override
    public void onFailed(String msg) {
        hideUploadingProgressDialog();
        showToast(msg);
    }


    public void addCall(Observable observable, final ApiCallback apiCallback) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer() {
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
