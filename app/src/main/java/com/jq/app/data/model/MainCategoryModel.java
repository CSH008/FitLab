package com.jq.app.data.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 10/15/2016.
 */

@JsonObject
public class MainCategoryModel extends BaseModel {
    @JsonField
    public String title;

    @JsonField
    public String image_url;

    public int image_resource;

    public MainCategoryModel() {}

    public MainCategoryModel(String title, int image_resource) {
        this.title = title;
        this.image_resource = image_resource;
    }

}
