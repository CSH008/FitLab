package com.jq.app.ui.calendar;

/**
 * Created by Hasnain on 19-Feb-18.
 */

public class CountModel {
    private int count;
    private String date;

    public CountModel(int count, String date) {
        this.count = count;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public String getDate() {
        return date;
    }
}

