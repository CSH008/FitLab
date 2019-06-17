package com.jq.app.ui.my_workout;

public class Workout {
    private String duration;
    private String noOfVideo;
    private String planner_id;
    private String title;
    private String type;

    public Workout(String title, String duration, String noOfVideo, String type, String planner_id) {
        this.title = title;
        this.duration = duration;
        this.noOfVideo = noOfVideo;
        this.type = type;
        this.planner_id = planner_id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getNoOfVideo() {
        return this.noOfVideo;
    }

    public String getType() {
        return this.type;
    }

    public String getPlanner_id() {
        return this.planner_id;
    }
}