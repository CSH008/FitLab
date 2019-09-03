package com.jq.app.ui.PlannerNew.model;

/**
 * Created by station13 on 09-02-2018.
 */

public class Video {

    String id,url,body_part_code,work_out_code,title,thumbnail_url,created_date_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody_part_code() {
        return body_part_code;
    }

    public void setBody_part_code(String body_part_code) {
        this.body_part_code = body_part_code;
    }

    public String getWork_out_code() {
        return work_out_code;
    }

    public void setWork_out_code(String work_out_code) {
        this.work_out_code = work_out_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getCreated_date_time() {
        return created_date_time;
    }

    public void setCreated_date_time(String created_date_time) {
        this.created_date_time = created_date_time;
    }

    public Video(String id, String url, String body_part_code, String work_out_code, String title, String thumbnail_url, String created_date_time) {
        this.id = id;
        this.url = url;
        this.body_part_code = body_part_code;
        this.work_out_code = work_out_code;
        this.title = title;
        this.thumbnail_url = thumbnail_url;
        this.created_date_time = created_date_time;
    }
}