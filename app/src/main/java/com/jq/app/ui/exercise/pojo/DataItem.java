package com.jq.app.ui.exercise.pojo;

import java.util.List;

public class DataItem {
    private int cat_order;
    private List<VideoItem> video;
    private String work_out_code;

    public void setWorkOutCode(String workOutCode) {
        this.work_out_code = workOutCode;
    }

    public String getWorkOutCode() {
        return this.work_out_code;
    }

    public void setVideo(List<VideoItem> video) {
        this.video = video;
    }

    public List<VideoItem> getVideo() {
        return this.video;
    }

    public void setCatOrder(int catOrder) {
        this.cat_order = catOrder;
    }

    public int getCatOrder() {
        return this.cat_order;
    }
}
