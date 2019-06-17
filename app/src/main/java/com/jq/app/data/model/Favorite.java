package com.jq.app.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import io.realm.RealmObject;

/**
 * Created by Administrator on 10/15/2016.
 */

public class Favorite extends RealmObject {

    public String video_id;
    public String workout_name;
    public String work_out_code;
    public String body_part_code;
    public String title;
    public String thumbnail_url;
    public String description;
    public String url;
    public String comment;
    public String m_user_id;
    public String localPath;

    public Favorite() {}

    public void update(VideoModel item) {
        video_id = item.id;
        workout_name = item.workout_name;
        work_out_code = item.work_out_code;
        body_part_code = item.body_part_code;
        title = item.title;
        thumbnail_url = item.thumbnail_url;
        description = item.description;
        url = item.url;
        comment = item.comment;
    }

}
