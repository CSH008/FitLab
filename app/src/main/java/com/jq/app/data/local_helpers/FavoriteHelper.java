package com.jq.app.data.local_helpers;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.model.Progress;
import com.bluelinelabs.logansquare.LoganSquare;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.VideoModel;
import com.jq.app.util.Config;
import com.jq.app.util.base.BaseActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Administrator on 5/31/2017.
 */

public class FavoriteHelper extends BaseLocalHelper {
    
    private static FavoriteHelper instance;
    public static synchronized FavoriteHelper getInstance() {
        if (instance == null) {
            instance = new FavoriteHelper();
        }
        return instance;
    }

    private FavoriteHelper() {
        super();
    }

    @Override
    protected void getItemsFromLocal() {
        results = realm.where(Favorite.class).equalTo("m_user_id", App.my_email).findAll();
    }

    protected String getApiUrl() {
        return Config.API_GET_FAVORITE_VIDEOS;
    }
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", App.my_email);
        return params;
    }

    @Override
    public String getId(RealmObject object) {
        return ((Favorite)object).video_id;
    }

    @Override
    protected void insertItem(final JSONObject item, final boolean notify) {
        try {
            VideoModel model = getBaseItem(item);
            insertMItem(model, notify);

        } catch (IOException e) {
            e.printStackTrace();
            if(notify) notifyActionCompleted(ACTION_LOAD, R.string.msg_network_error);
        }
    }

    private VideoModel getBaseItem(JSONObject json) throws IOException {
        return LoganSquare.parse(json.toString(), VideoModel.class);
    }

    private void insertMItem(final BaseModel model, final boolean notify) {
        final String item_id = model.id;
        final Favorite existFavorite = (Favorite) getItem(item_id);

        if (existFavorite == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a Favorite
                    Favorite newItem = realm.createObject(Favorite.class);
                    newItem.update((VideoModel) model);
                    newItem.m_user_id = App.my_email;

                    newlyUpdatedOrAddedItems.add(newItem);
                    //downloadVideo(newItem, false);
                    if (notify) notifyActionCompleted(ACTION_CREATE, 0);
                }
            });
        } else {
            newlyUpdatedOrAddedItems.add(existFavorite);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a Favorite
                    existFavorite.update((VideoModel) model);
                    existFavorite.m_user_id = App.my_email;

                    //downloadVideo(existFavorite, false);
                    if (notify) notifyActionCompleted(ACTION_CREATE, 0);
                }
            });
        }
    }

    @Override
    public boolean isItemUpdated(RealmObject item) {
        for (RealmObject object:newlyUpdatedOrAddedItems) {
            Favorite favorite = (Favorite) object;
            if(favorite.video_id.equals(((Favorite)item).video_id)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void createItem(BaseModel bItem) {
        final VideoModel item = (VideoModel) bItem;
        Favorite existFavorite = (Favorite) getItem(item.id);
        if(existFavorite != null) {
            item.is_like = !item.is_like;
            deleteItem(existFavorite, true);
        }

        AndroidNetworking.get(Config.API_GET_UPDATE_FAVORITE)
                .addQueryParameter("email", App.my_email)
                .addQueryParameter("id", item.id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        item.is_like = !item.is_like;
                        //notifyActionCompleted(ACTION_CREATE, 0);
                        insertMItem(item, true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        notifyActionCompleted(ACTION_CREATE, R.string.msg_failed);
                    }
                });
    }

    String currentDownloadUrl;
    public void downloadVideo(final Favorite favorite, final boolean notify) {
        if(mContext==null) {
            if(notify) notifyActionCompleted(ACTION_DOWNLOADED, R.string.msg_failed);
            return;
        }

        File mydir = mContext.getDir("favorite_videos", Context.MODE_PRIVATE); //Creating an internal dir;
        if (!mydir.exists())
        {
            mydir.mkdirs();
        }

        if(favorite.localPath!=null && !favorite.localPath.isEmpty() && (new File(favorite.localPath)).exists()) {
            if(notify) notifyActionCompleted(ACTION_DOWNLOADED, 0);
            return;
        }

        if(favorite.url==null || favorite.url.isEmpty()) {
            if(notify) notifyActionCompleted(ACTION_DOWNLOADED, R.string.msg_url_empty);
            return;
        }
        String fileName = getFileName(favorite.url);
        final String filePath = mydir.getPath() + "/" + fileName;
        File file = new File(filePath);
        if(file.exists()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    favorite.localPath = filePath;
                    if(notify) notifyActionCompleted(ACTION_DOWNLOADED, 0);
                }
            });

        } else {
            if(currentDownloadUrl!=null && favorite.url.equals(currentDownloadUrl)) return;
            AndroidNetworking.download(favorite.url, mydir.getPath(), fileName)
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
                            currentDownloadUrl = null;
                            realm.executeTransaction(new Realm.Transaction() {
                                                         @Override
                                                         public void execute(Realm realm) {
                                                             favorite.localPath = filePath;
                                                             if(notify) notifyActionCompleted(ACTION_DOWNLOADED, 0);
                                                         }
                                                     });
                        }

                        @Override
                        public void onError(ANError error) {
                            currentDownloadUrl = null;
                            Log.e("downloading_favorite", error.getMessage());
                            if(notify) notifyActionCompleted(ACTION_DOWNLOADED, R.string.msg_network_error);
                        }
                    });
        }

    }

}
