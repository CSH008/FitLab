package com.jq.app.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 10/15/2016.
 */

@JsonObject
public class DayGoalModel extends BaseModel {

    @JsonField
    public String date;

    @JsonField
    public int exercise;

    @JsonField
    public int mobility;

    @JsonField
    public int stretching;

    @JsonField
    public int weight;

    public DayGoalModel() {}

    public DayGoalModel(String date) {
        this.date = date;
    }

}
