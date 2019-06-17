package com.jq.app.ui.sqlite;

import android.content.ContentValues;

import com.jq.app.ui.calendar.CountModel;
import com.jq.app.ui.calendar.EventModel;

import java.util.ArrayList;

/**
 * Created by Hasnain on 17-Feb-18.
 */

public interface DatabaseOperation {
    public long insert(String title, String subject, String description, String date,String time);
    public int update(String event_id);
    public boolean delete(String event_id);
    public ArrayList<EventModel> getEventByDate(String date);
    public ArrayList<CountModel> getCount();
}
