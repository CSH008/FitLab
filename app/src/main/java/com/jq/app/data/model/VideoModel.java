package com.jq.app.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 10/15/2016.
 */

@JsonObject
public class VideoModel extends BaseModel {

    @JsonField
    public String workout_name;

    @JsonField
    public String work_out_code;

    @JsonField
    public String body_part_code;

    @JsonField
    public String title;

    @JsonField
    public String thumbnail_url;

    @JsonField
    public String description;

    @JsonField
    public String url;

    @JsonField
    public boolean is_like;

    @JsonField
    public String comment;

    public VideoModel() {}

    public VideoModel(Favorite item) {
        workout_name = item.workout_name;
        work_out_code = item.work_out_code;
        body_part_code = item.body_part_code;
        title = item.title;
        thumbnail_url = item.thumbnail_url;
        description = item.description;
        url = item.url;
        is_like = true;
        comment = item.comment;
        id = item.video_id;
    }

}
