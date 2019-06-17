package com.jq.app.data.content_helpers;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.model.DayGoalModel;
import com.jq.app.data.model.SetModel;
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
public class GoalContentHelper extends BaseContentHelper {

    public static final int REQUEST_PLAN_SET_LIST = 6;
    public static final int REQUEST_CREATE_SET = 7;
    public static final int REQUEST_DELETE_ALL_SET = 8;

    public ArrayList<SetModel> planSetList = new ArrayList<>();

    public GoalContentHelper(OnDataLoadListener listener) {
        super(listener);
    }

    public void getPlanList(String from, String to) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("from_date", from);
        param.put("to_date", to);

        String url = Config.API_GET_GOAL_PLAN_LIST + "?email=" + App.my_email + "&from_date=" + from + "&to_date=" + to;

        getRequestJSONObject(REQUEST_LIST, Config.API_GET_GOAL_PLAN_LIST, param, 0);
    }

    public void postGoal(DayGoalModel item, final int index, String planWeight) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("date", item.date);
        param.put("exercise", "" + item.exercise);
        param.put("mobility", "" + item.mobility);
        param.put("stretching", "" + item.stretching);
        param.put("plan_weight", planWeight);

        String url = Config.API_CREATE_GOAL_PLAN + "?email=" + App.my_email + "&date=" + item.date + "&exercise=" + item.exercise + "&mobility=" +
                item.mobility + "&stretching=" + item.stretching + "&plan_weight=" + planWeight;

        getRequestJSONObject(REQUEST_CREATE, Config.API_CREATE_GOAL_PLAN, param, index);
    }

    public void getPlanSetList(String from, String to) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("from_date", from);
        param.put("to_date", to);

        getRequestJSONObject(REQUEST_PLAN_SET_LIST, Config.API_GET_GOAL_SETS, param, 0);
    }

    public void deleteAllGoalSetForDate(String from, String to) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("from_date", from);
        param.put("to_date", to);

        getRequestJSONObject(REQUEST_DELETE_ALL_SET, Config.API_DELETE_GOAL_SETS, param, 0);
    }

    public void postSet(SetModel item, int index) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        param.put("video_id", item.video_id);
        param.put("order_number", ""+item.order_number);
        param.put("sets", ""+item.sets);
        param.put("reps", ""+item.reps);
        param.put("time", ""+item.time);
        param.put("date", item.date);
        param.put("pain_level", ""+item.pain_level);

        String url = Config.API_CREATE_GOAL_SET + "?email=" + App.my_email + "&date=" + item.date + "&video_id=" + item.video_id + "&order_number=" +
                item.order_number + "&sets=" + item.sets + "&reps=" + item.reps  + "&time=" + item.time;

        getRequestJSONObject(REQUEST_CREATE_SET, Config.API_CREATE_GOAL_SET, param, index);
    }


    @Override
    public void dealResponseObject(int action, JSONObject object)  throws JSONException, IOException {
        switch (action) {
            case REQUEST_LIST:
                clearItems();
                JSONArray array = object.getJSONArray("data");
                for(int i=0; i<array.length(); i++) {
                    JSONObject jsonItem = array.getJSONObject(i);
                    DayGoalModel item = LoganSquare.parse(jsonItem.toString(), DayGoalModel.class);
                    addItem(item);
                }
                break;

            case REQUEST_PLAN_SET_LIST:
                planSetList.clear();
                JSONArray array1 = object.getJSONArray("data");
                for(int i=0; i<array1.length(); i++) {
                    JSONObject jsonItem = array1.getJSONObject(i);
                    SetModel item = LoganSquare.parse(jsonItem.toString(), SetModel.class);
                    planSetList.add(item);
                }
                break;
        }

    }

}
