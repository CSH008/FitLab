package com.riontech.calendar.fragment;

/**
 * Created by Hasnain on 16-Feb-18.
 */

public interface addEventListener {
    void onTapToAddClick(String date);
    void onTapAddOneMoreClick(String date);
    void onTapDelete(String date,String event_id);
    void onTapToEdit(String event_type, String event_title,String event_about,String date,String event_time);

}
