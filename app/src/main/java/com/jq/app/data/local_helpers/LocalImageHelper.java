package com.jq.app.data.local_helpers;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.LocalImage;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.util.Common;
import com.jq.app.util.Config;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Administrator on 5/31/2017.
 */

public class LocalImageHelper extends BaseLocalHelper {

    private static LocalImageHelper instance;
    public static synchronized LocalImageHelper getInstance() {
        if (instance == null) {
            instance = new LocalImageHelper();
        }

        return instance;
    }

    private LocalImageHelper() {
        super();
    }

    @Override
    protected void getItemsFromLocal() {
        results = realm.where(LocalImage.class).equalTo("m_user_id", App.my_email).findAll();
    }

    @Override
    protected String getApiUrl() {
        return Config.API_GET_MY_MEDIA;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", App.my_email);
        params.put("type", "image");
        return params;
    }

    public List<LocalImage> getMedia(int category) {
        ArrayList<LocalImage> result = new ArrayList<>();

        String catString = getWorkoutCode(category);
        for (RealmObject item:getItems()) {
            if(((LocalImage)item).work_out_code.equals(catString)) {
                result.add((LocalImage)item);
            }
        }

        return result;
    }

    @Override
    protected void insertItem(final JSONObject item, final boolean notify) {
        final String item_id = Common.getJSONStringWithKey(item, "id");
        final LocalImage existItem = (LocalImage)getItem(item_id);

        if (existItem == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LocalImage newItem = realm.createObject(LocalImage.class);
                    newItem.update(item);
                    newItem.m_user_id = App.my_email;

                    newlyUpdatedOrAddedItems.add(newItem);
                    if(notify) notifyActionCompleted(ACTION_CREATE, 0);
                }
            });
        } else {
            newlyUpdatedOrAddedItems.add(existItem);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    existItem.update(item);
                    existItem.m_user_id = App.my_email;

                    notifyActionCompleted(ACTION_CREATE, 0);
                }
            });
        }
    }

    @Override
    public boolean isItemUpdated(RealmObject item) {
        for (RealmObject object:newlyUpdatedOrAddedItems) {
            if(getId(object).equals(getId(item))) {
                return true;
            }
        }

        return false;
    }

    public void createItem(final String filePath, final JSONObject createdItem) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LocalImage newItem = realm.createObject(LocalImage.class);
                newItem.update(createdItem);
                newItem.localPath = filePath;
                newItem.m_user_id = App.my_email;

                notifyActionCompleted(ACTION_CREATE, 0);
            }
        });
    }

    @Override
    public String getId(RealmObject object) {
        return ((LocalImage)object).id;
    }

}
