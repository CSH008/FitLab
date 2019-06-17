package com.jq.app.data.content_helpers;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.jq.app.App;
import com.jq.app.data.model.SetModel;
import com.jq.app.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SetContentHelper extends BaseContentHelper {
    private static final String TAG = SetContentHelper.class.getSimpleName();

    public static final int REQUEST_CREATE_WORKOUT = 6;
    String video_id;

    public SetContentHelper(OnDataLoadListener listener) {
        super(listener);
    }

    public void getData(String video_id, String date) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("video_id", video_id);
        param.put("date", date);
        this.video_id = video_id;

        getRequestJSONObject(REQUEST_LIST, Config.API_GET_VIDEO_SETS, param, 0);
    }

    public void saveWorkout(int time, int pain_level, String date) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("time", "" + time);
        param.put("pain", "" + pain_level);
        param.put("video_id", video_id);
        param.put("date", date);


        getRequestJSONObject(REQUEST_CREATE_WORKOUT, Config.API_SAVE_WORKOUT, param, 0);
    }

    public void postSet(SetModel item, String date) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("video_id", video_id);
        param.put("order_number", ""+item.order_number);
        param.put("sets", ""+item.sets);
        param.put("reps", ""+item.reps);
        param.put("time", ""+item.time);
        param.put("pain_level", ""+item.pain_level);
        param.put("date", date);

        getRequestJSONObject(REQUEST_CREATE, Config.API_SAVE_VIDEO_SETS, param, 0);
    }

    public void deleteAllVideoSets(String video_id, String date) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("video_id", video_id);
        param.put("date", date);

        getRequestJSONObject(REQUEST_DELETE_ALL, Config.API_GET_DELETE_VIDEO_SETS, param, 0);
    }

    @Override
    public void dealResponseObject(int action, JSONObject object) throws JSONException, IOException {
        switch (action) {
            case REQUEST_LIST:
                clearItems();
                JSONArray jsonArray = object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    SetModel videoItem = LoganSquare.parse(item.toString(), SetModel.class);
                    videoItem.video_id = video_id;
                    addItem(videoItem);
                }
                break;
        }
    }

}
