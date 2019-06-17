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

public class LocalVideoHelper extends BaseLocalHelper {

    private static LocalVideoHelper instance;

    public static synchronized LocalVideoHelper getInstance() {
        if (instance == null) {
            instance = new LocalVideoHelper();
        }
        return instance;
    }

    private LocalVideoHelper() {
        super();
    }

    @Override
    protected void getItemsFromLocal() {
        results = realm.where(LocalVideo.class).equalTo("m_user_id", App.my_email).findAll();
    }

    @Override
    protected String getApiUrl() {
        return Config.API_GET_MY_MEDIA;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", App.my_email);
        params.put("type", "video");

        return params;
    }

    public List<LocalVideo> getMedia(int category) {
        ArrayList<LocalVideo> result = new ArrayList<>();

        String catString = getWorkoutCode(category);
        for (RealmObject item:getItems()) {
          if(((LocalVideo)item).work_out_code.equals(catString)) {
                result.add((LocalVideo)item);
            }
        }

        return result;
    }

    private LocalVideo getLocalItem(LocalVideo localVideo) {
        for (RealmObject item:getItems()) {
            if(((LocalVideo)item).id.equals(localVideo.id)) {
                return (LocalVideo)item;
            }
        }
        return null;
    }

    @Override
    protected void insertItem(final JSONObject item, final boolean notify) {
        final String item_id = Common.getJSONStringWithKey(item, "id");
        final LocalVideo existItem = (LocalVideo)getItem(item_id);

        if (existItem == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a Favorite
                    LocalVideo newItem = realm.createObject(LocalVideo.class);
                    newItem.update(item);
                    newItem.m_user_id = App.my_email;

                    newlyUpdatedOrAddedItems.add(newItem);
                    //downloadVideo(newItem, false);
                    if(notify) notifyActionCompleted(ACTION_CREATE, 0);
                }
            });
        } else {
            newlyUpdatedOrAddedItems.add(existItem);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //Add a Favorite
                    existItem.update(item);
                    existItem.m_user_id = App.my_email;

                    //downloadVideo(existItem, false);
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

    @Override
    public String getId(RealmObject object) {
        return ((LocalVideo)object).id;
    }

    public void createItem(final String filePath, final JSONObject createdItem) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LocalVideo newItem = realm.createObject(LocalVideo.class);
                newItem.update(createdItem);
                newItem.localPath = filePath;
                newItem.m_user_id = App.my_email;

                notifyActionCompleted(ACTION_CREATE, 0);
            }
        });
    }

    public void downloadVideo(final LocalVideo item, final boolean notify) {
        String fileName = getFileName(item.url);
        File mydir = mContext.getDir("favorite_videos", Context.MODE_PRIVATE); //Creating an internal dir;
        if (!mydir.exists())
        {
            mydir.mkdirs();
        }

        if(item.localPath!=null && !item.localPath.isEmpty() && (new File(item.localPath)).exists()) {
            if(notify) notifyActionCompleted(ACTION_DOWNLOADED, 0);
            return;
        }

        final String filePath = mydir.getPath() + "/" + fileName;
        File file = new File(filePath);
        if(file.exists()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LocalVideo localItem = getLocalItem(item);
                    if(localItem!=null) {
                        item.localPath = filePath;
                    }
                    if(notify) notifyActionCompleted(ACTION_DOWNLOADED, 0);
                }
            });

        } else {
            AndroidNetworking.download(item.url, mydir.getPath(), fileName)
                    .setTag("downloadTest")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setDownloadProgressListener(new DownloadProgressListener() {
                        @Override
                        public void onProgress(long bytesDownloaded, long totalBytes) {
                            // do anything with progress
                            float percentDownloaded = bytesDownloaded * 100 / totalBytes;
                            if(notify) notifyActionProgress(percentDownloaded);
                        }
                    })
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    LocalVideo localItem = getLocalItem(item);
                                    if(localItem!=null) {
                                        item.localPath = filePath;
                                    }
                                    if(notify) notifyActionCompleted(ACTION_DOWNLOADED, 0);
                                }
                            });
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.e("downloading_favorite", error.getMessage());
                            if(notify) notifyActionCompleted(ACTION_DOWNLOADED, R.string.msg_network_error);
                        }
                    });
        }

    }

}
