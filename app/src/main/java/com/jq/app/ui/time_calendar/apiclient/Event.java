package com.jq.app.ui.time_calendar.apiclient;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An event model that was built for automatic serialization from json to object.
 * Created by Raquib-ul-Alam Kanak on 1/3/16.
 * Website: http://alamkanak.github.io
 */
public class Event {
    private static final String TAG = Event.class.getSimpleName();

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dayOfMonth")
    @Expose
    private String dayOfMonth;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("description")
    @Expose
    private String description;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressLint("SimpleDateFormat")
    public WeekViewEvent toWeekViewEvent() {

        // Parse time.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date start = new Date();
        Date end = new Date();
        try {
            start = sdf.parse(getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            end = sdf.parse(getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Initialize start and end time.
        Calendar startTime = Calendar.getInstance();


//        Calendar startTime = (Calendar) now.clone();
        startTime.setTimeInMillis(start.getTime());
        startTime.set(Calendar.YEAR, Integer.parseInt(getYear()));
        startTime.set(Calendar.MONTH, Integer.parseInt(getMonth()) - 1);
        startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getDayOfMonth()));


        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(end.getTime());
        endTime.set(Calendar.YEAR, Integer.parseInt(getYear()));
        endTime.set(Calendar.MONTH, Integer.parseInt(getMonth()) - 1);
        endTime.set(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH));

        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName(getName()+"\n\n"+getDescription()+"\nStart Time : "+getStartTime()+"\nEnd Time : "+getEndTime());
        weekViewEvent.setStartTime(startTime);
        weekViewEvent.setEndTime(endTime);

        if (getColor().length() > 7 || getColor().length() < 7) {
            Log.e(TAG,"getColor() : #FF0000");
            weekViewEvent.setColor(Color.parseColor("#FF0000"));

        } else {
            Log.e(TAG,"getColor() : "+getColor());
            weekViewEvent.setColor(Color.parseColor(getColor()));
        }


//        Log.e(TAG,"dates----> : " + toWeekViewEvent().getStartTime().getTime()+"-----"+toWeekViewEvent().getEndTime().getTime());


        return weekViewEvent;
    }
}
