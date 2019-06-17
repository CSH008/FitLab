package com.jq.app.ui.calendar;

/**
 * Created by Hasnain on 17-Feb-18.
 */

public class EventModel {
    private String id;
    private String title;
    private String subject;
    private String description;
    private String date;
    private String time;

    public EventModel(String id, String title, String subject, String description, String date,String time) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
