package com.jq.app.ui.time_calendar;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.JsonObject;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.ui.time_calendar.apiclient.Event;
import com.jq.app.ui.time_calendar.apiclient.Example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * An example of how events can be fetched from network and be displayed on the week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class TimeCalendarActivity extends BaseActivity {
    private static final String TAG = TimeCalendarActivity.class.getSimpleName();

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    boolean calledNetwork = false;
    private int count = 0;
    private String previous_month = "";
    private String previous_year = "";

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Download events from network if it hasn't been done already. To understand how events are
        // downloaded using retrofit, visit http://square.github.io/retrofit
        if (!calledNetwork) {

            Map<String, String> map = new HashMap();
            map.put("year", newYear + "");
            map.put("email", App.my_email);

            String mm;
            if (newMonth < 10) {
                mm = "0" + newMonth;
            } else {

                mm = "" + newMonth + "";

            }
            map.put("month", mm);

            if (!previous_year.equalsIgnoreCase(newYear + "") && !previous_month.equalsIgnoreCase(mm)) {


                addCall(((ApiStores) ApiClient.retrofit(getApplicationContext()).create(ApiStores.class)).event(map), apiCallback);
                calledNetwork = true;
                previous_month = mm;
                previous_year = newYear + "";

                Log.e(TAG, "Call " + count++);


            } else {

                Log.e(TAG, "Same request : " + count++);
            }
        }

        // Return only the events that matches newYear and newMonth.
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }


        return matchedEvents;


    }

    /**
     * Checks if an event falls into a specific year and month.
     *
     * @param event The event to check for.
     * @param year  The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }


    ApiCallback apiCallback = new ApiCallback<Example>() {

        public void onSuccess(Example example) {
            try {

                if(example.getStatus().equals("1")){

                    TimeCalendarActivity.this.events.clear();

                    for (Event event : example.getData()) {

//                        Log.e(TAG,"dates----> : " + event.toWeekViewEvent().getStartTime().getTime()+"-----"+event.toWeekViewEvent().getEndTime().getTime());

                        TimeCalendarActivity.this.events.add(event.toWeekViewEvent());
                    }

                    Log.e(TAG, "events.size() : " + events.size());

                    getWeekView().notifyDatasetChanged();

                }else if(example.getStatus().equals("0")){

                    Toast.makeText(TimeCalendarActivity.this, example.getMessage(), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(TimeCalendarActivity.this, "Invalid response", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void onFailure(String msg) {
            Log.e(TAG, "Error Message : " + msg);
        }

        public void onFinish() {
            calledNetwork = false;
        }

        public void onTokenExpire() {
        }
    };


//    private Calendar getCalendar(String day, String month, String year, String hour, String minute) {
//        Calendar date = Calendar.getInstance();
//
//        //Set year
//        date.set(Calendar.YEAR, Integer.parseInt(year));
//
//        // We will have to increment the month field by 1
//        date.set(Calendar.MONTH, (Integer.parseInt(month)));
//
//        // As the month indexing starts with 0
//        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
//
//        // Set hour
//        date.set(Calendar.HOUR, Integer.parseInt(hour));
//
//        //Set minutes
//        date.set(Calendar.MINUTE, Integer.parseInt(minute));
//
//        return date;
//    }
}
