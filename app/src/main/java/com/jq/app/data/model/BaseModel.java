package com.jq.app.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Administrator on 10/15/2016.
 */

@JsonObject
public class BaseModel implements Serializable {
    @JsonField //or can use @JsonField(name = "id")
    public String id;

}
