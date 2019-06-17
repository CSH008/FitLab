package com.jq.app.ui.time_calendar;

import com.jq.app.ui.calendar.EventModel;
import com.jq.app.ui.time_calendar.apiclient.Event;

import java.util.ArrayList;
import java.util.List;

public class EventResponse {

    String status;
    String message;
    String user_id;

    List<Event> data=new ArrayList<>();


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }
}
