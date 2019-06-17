package com.jq.app.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 10/15/2016.
 */

@JsonObject
public class SetModel extends BaseModel {

    @JsonField
    public String video_id;

    @JsonField
    public int order_number;

    @JsonField
    public int sets;

    @JsonField
    public int reps;

    @JsonField
    public int time;

    @JsonField
    public int pain_level;

    @JsonField
    public String date;

    public SetModel() {}

    public SetModel(String video_id, int pain_level) {
        this.video_id = video_id;
        this.pain_level = pain_level;
        this.time = 60;
    }

    public SetModel(String video_id, int pain_level, int time) {
        this.video_id = video_id;
        this.pain_level = pain_level;
        this.time = time;
    }

    public SetModel(int order_number, String date) {
        this.order_number = order_number;
        this.date = date;
        this.video_id = "0";
        this.time = 60;
    }

}
