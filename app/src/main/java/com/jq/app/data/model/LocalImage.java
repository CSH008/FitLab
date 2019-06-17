package com.jq.app.data.model;

import com.jq.app.util.Common;

import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by Administrator on 10/15/2016.
 */

public class LocalImage extends RealmObject {

    public String id;
    public String work_out_code;
    public String body_part_code;
    public String title;
    public String url;
    public String created_date_time;

    public String m_user_id;
    public String localPath;

    public LocalImage() {}

    public void update(JSONObject item) {
        id = Common.getJSONStringWithKey(item, "id");
        work_out_code = Common.getJSONStringWithKey(item, "work_out_code");
        body_part_code = Common.getJSONStringWithKey(item, "body_part_code");
        title = Common.getJSONStringWithKey(item, "title");
        url = Common.getJSONStringWithKey(item, "url");
        created_date_time = Common.getJSONStringWithKey(item, "created_date_time");
    }

}
