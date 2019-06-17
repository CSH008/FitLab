package com.jq.app.data.content_helpers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.jq.app.App;
import com.jq.app.util.Common;
import com.jq.app.util.Config;

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
public class ProfileHelper extends BaseContentHelper {

    public static String age = "";
    public static String weight = "";
    public static String height = "";
    public static String username = "";
    public int plan_exercise, plan_mobility, plan_stretching, plan_weight, month_exercise = 10, month_mobility = 10,
            month_stretching = 10, user_weight, pain_level;

    private static Context mContext;

    public static final int REQUEST_GET_ACTIVITY = 6;
    public static final int REQUEST_SAVE_TODAY_WEIGHT = 7;

    public ProfileHelper(OnDataLoadListener listener) {
        super(listener);

        if(listener instanceof Activity && mContext==null) {
            mContext = (Context) listener;
        } else if(listener instanceof Fragment && mContext==null) {
            mContext = ((Fragment)listener).getActivity();
        }

        if(mContext!=null) {
            age = Common.getStringWithValueKey(mContext, "user_age");
            weight = Common.getStringWithValueKey(mContext, "user_weight");
            height = Common.getStringWithValueKey(mContext, "user_height");
            username = Common.getStringWithValueKey(mContext, "username");

            plan_exercise = Common.getIntWithValueKey(mContext, "user_plan_exercise");
            plan_mobility = Common.getIntWithValueKey(mContext, "user_plan_mobility");
            plan_stretching = Common.getIntWithValueKey(mContext, "user_plan_stretching");
            plan_weight = Common.getIntWithValueKey(mContext, "user_plan_weight");
            month_exercise = Common.getIntWithValueKey(mContext, "user_month_exercise");
            month_mobility = Common.getIntWithValueKey(mContext, "user_month_mobility");
            month_stretching = Common.getIntWithValueKey(mContext, "user_month_stretching");
            user_weight = Common.getIntWithValueKey(mContext, "user_user_weight");
            pain_level = Common.getIntWithValueKey(mContext, "user_pain_level");
        }
    }

    public void getProfile() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);

        getRequestJSONObject(REQUEST_LIST, Config.API_GET_PROFILE, param, 0);
    }

    public void saveTodayWeight(String today, String weight) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("date", today);
        param.put("user_weight", weight);

        String url = Config.API_SAVE_TODAY_WEIGHT + "?email=" + App.my_email + "&date=" + today + "&weight=" + weight;
        getRequestJSONObject(REQUEST_SAVE_TODAY_WEIGHT, Config.API_SAVE_TODAY_WEIGHT, param, 0);
    }

    public void saveProfile(String age, String weight, String height) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("age", age);
        param.put("weight", weight);
        param.put("height", height);

        this.age = age;
        this.weight = weight;
        this.height = height;

        String url = Config.API_GET_VIDEOS + "?email=" + App.my_email + "&age=" + age + "&weight=" + weight + "&height=" + height;
        getRequestJSONObject(REQUEST_EDIT, Config.API_SAVE_PROFILE, param, 0);
    }

    public void getActivity() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);

        getRequestJSONObject(REQUEST_GET_ACTIVITY, Config.API_GET_ACTIVITY, param, 0);
    }

    @Override
    public void dealResponseObject(int action, JSONObject object) throws JSONException, IOException {
        switch (action) {
            case REQUEST_LIST:
                JSONObject jsonObject = object.getJSONObject("data");
                age = jsonObject.getString("age");
                weight = jsonObject.getString("weight");
                height = jsonObject.getString("height");
                username = jsonObject.getString("username");

                if(mContext!=null) {
                    Common.saveStringWithKeyValue(mContext, "user_age", age);
                    Common.saveStringWithKeyValue(mContext, "user_weight", weight);
                    Common.saveStringWithKeyValue(mContext, "user_height", height);
                    Common.saveStringWithKeyValue(mContext, "username", username);
                }
                break;

            case REQUEST_GET_ACTIVITY:
                jsonObject = object.getJSONObject("data");
                plan_exercise = jsonObject.getInt("plan_exercise");
                plan_mobility = jsonObject.getInt("plan_mobility");
                plan_stretching = jsonObject.getInt("plan_stretching");
                plan_weight = jsonObject.getInt("plan_weight");
                month_exercise = jsonObject.getInt("month_exercise");
                month_mobility = jsonObject.getInt("month_mobility");
                month_stretching = jsonObject.getInt("month_stretching");
                user_weight = jsonObject.getInt("user_weight");
                pain_level = jsonObject.getInt("pain_level");

                if(mContext!=null) {
                    Common.saveIntWithKeyValue(mContext, "user_plan_exercise", plan_exercise);
                    Common.saveIntWithKeyValue(mContext, "user_plan_mobility", plan_mobility);
                    Common.saveIntWithKeyValue(mContext, "user_plan_stretching", plan_stretching);
                    Common.saveIntWithKeyValue(mContext, "user_plan_weight", plan_weight);
                    Common.saveIntWithKeyValue(mContext, "user_month_exercise", month_exercise);
                    Common.saveIntWithKeyValue(mContext, "user_month_mobility", month_mobility);
                    Common.saveIntWithKeyValue(mContext, "user_month_stretching", month_stretching);
                    //Common.saveIntWithKeyValue(mContext, "user_weight", user_weight);
                    Common.saveIntWithKeyValue(mContext, "user_pain_level", pain_level);
                }
                break;
        }
    }



}
