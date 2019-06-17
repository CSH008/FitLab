package com.jq.app.data.content_helpers;

import com.bluelinelabs.logansquare.LoganSquare;
import com.jq.app.App;
import com.jq.app.data.model.VideoModel;
import com.jq.app.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class VideoContentHelper extends BaseContentHelper {

    public static final int REQUEST_SEARCH_LIST = 6;
    private static ArrayList<VideoModel> tempLoadedVideos = new ArrayList<>();

    public VideoContentHelper(OnDataLoadListener listener) {
        super(listener);
    }

    public void getVideoData(String workout_code, String bodypart_code) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("workout_code", workout_code);
        param.put("bodypart_code", bodypart_code);

        //String url = Config.API_GET_VIDEOS + "?workout_code=" + workout_code + "&bodypart_code=" + bodypart_code + "&email=" + email;
        getRequestJSONObject(REQUEST_LIST, Config.API_GET_VIDEOS, param, 0);
    }

    public void getSearchVideos(String keyword, String workout_code) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("workout_code", workout_code);
        param.put("search_keyword", keyword);

        //String url = Config.API_GET_VIDEOS + "?workout_code=" + workout_code + "&bodypart_code=" + bodypart_code + "&email=" + email;
        getRequestJSONObject(REQUEST_SEARCH_LIST, Config.API_GET_SEARCH_VIDEOS, param, 0);
    }

    public void getChooserVideoData(String category, String bodypart) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("category", category);
        param.put("sub_category", bodypart);

        //String url = Config.API_GET_VIDEOS + "?workout_code=" + workout_code + "&bodypart_code=" + bodypart_code + "&email=" + email;
        getRequestJSONObject(REQUEST_LIST, Config.API_GET_GOAL_CHOOSER_VIDEOS, param, 0);
    }

    @Override
    public void dealResponseObject(int action, JSONObject object) throws JSONException, IOException {
        switch (action) {
            case REQUEST_LIST:
                clearItems();
                JSONArray jsonArray = object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    VideoModel videoItem = LoganSquare.parse(item.toString(), VideoModel.class);
                    addItem(videoItem);
                    addToTemp(videoItem);
                }
                break;

            case REQUEST_SEARCH_LIST:
                clearItems();
                jsonArray = object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    VideoModel videoItem = LoganSquare.parse(item.toString(), VideoModel.class);
                    addItem(videoItem);
                    addToTemp(videoItem);
                }
                break;
        }
    }

    private void addToTemp(VideoModel model) {
        if(getVideoModelFromTemp(model.id)==null) {
            tempLoadedVideos.add(model);
        }
    }

    public static VideoModel getVideoModelFromTemp(String id) {
        for(int i=0; i<tempLoadedVideos.size(); i++) {
            VideoModel item = tempLoadedVideos.get(i);
            if(item.id.equals(id)) {
                return item;
            }
        }

        return null;
    }

}
