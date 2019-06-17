package com.jq.app.ui.sport;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 10/15/2016.
 */

@JsonObject
public class SportCategoryModel extends SportBaseModel {
    @JsonField
    public String title;

    @JsonField
    public String image_url;

    public int image_resource;

    public SportCategoryModel() {}

    public SportCategoryModel(String title, int image_resource) {
        this.title = title;
        this.image_resource = image_resource;
    }

}
