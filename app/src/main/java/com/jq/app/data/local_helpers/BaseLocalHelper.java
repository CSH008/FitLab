package com.jq.app.data.local_helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.data.model.BaseModel;
import com.jq.app.data.model.Favorite;
import com.jq.app.data.model.LocalImage;
import com.jq.app.data.model.LocalVideo;
import com.jq.app.data.model.VideoModel;
import com.jq.app.ui.auth.LoginActivity;
import com.jq.app.ui.auth.SplashActivity;
import com.jq.app.util.Common;
import com.jq.app.util.Config;
import com.jq.app.util.Constants;
import com.jq.app.util.base.BaseActivity;
import com.jq.app.util.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Administrator on 5/31/2017.
 */

public class BaseLocalHelper {

    private static String TAG = "BaseLocalHelper";

    public ArrayList<RealmObject> newlyUpdatedOrAddedItems = new ArrayList<>();
    private static BaseLocalHelper instance;
    RealmResults results;
    Realm realm;

    public static synchronized BaseLocalHelper getInstance() {
        if (instance == null) {
            instance = new BaseLocalHelper();
        }
        return instance;
    }

    protected BaseLocalHelper() {
        TAG = BaseLocalHelper.this.getClass().getSimpleName();
        realm = Realm.getDefaultInstance();
        syncList();
    }

    protected void getItemsFromLocal() {
    }

    public ArrayList<RealmObject> getItems() {
        getItemsFromLocal();

        ArrayList<RealmObject> mItems = new ArrayList<>();
        if (results != null) mItems.addAll(realm.copyFromRealm(results));
        return mItems;
    }

    public boolean isItemUpdated(RealmObject item) {
        return false;
    }

    public void syncList() {
        getItemsFromLocal();
        if (getApiUrl() == null) return;

        AndroidNetworking.get(getApiUrl())
                .addQueryParameter(getParams())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url = getApiUrl();
                            Map<String, String> params = getParams();
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                newlyUpdatedOrAddedItems.clear();
                                updateLocalData(jsonArray);
                            } else {
                                String message = response.getString("message");
                                notifyActionCompleted(ACTION_LOAD, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            notifyActionCompleted(ACTION_LOAD, R.string.msg_network_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        notifyActionCompleted(ACTION_LOAD, R.string.msg_network_error);
                    }
                });
    }

    protected String getApiUrl() {
        return null;
    }

    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", App.my_email);
        return params;
    }

    private void updateLocalData(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            insertItem(json, false);
        }

        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // Do task here
                if (msg.what == 1000) {
                    deleteUnchangedItems();
                    notifyActionCompleted(ACTION_LOAD, 0);
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(1000, 1000);
    }

    protected void insertItem(final JSONObject json, final boolean notify) {
    }

    public RealmObject getItem(String item_id) {
        for (int i = 0; i < results.size(); i++) {
            RealmObject item = (RealmObject) results.get(i);
            if (item != null && getId(item) != null && getId(item).equals(item_id)) {
                return item;
            }
        }

        return null;
    }

    public String getId(RealmObject object) {
        return "";
    }

    protected void deleteUnchangedItems() {
        ArrayList<RealmObject> tempArray = getItems();
        for (int i = 0; i < tempArray.size(); i++) {
            RealmObject item = tempArray.get(i);
            if (!isItemUpdated(item)) {
                deleteItem(item, false);
            }
        }
    }

    public void createItem(final BaseModel item) {
    }

    public void deleteItem(RealmObject item, final boolean notify) {
        final RealmObject realmObject = getItem(getId(item));
        if (realmObject != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realmObject.deleteFromRealm();
                    if (notify) notifyActionCompleted(ACTION_DELETE, 0);
                }
            });
        }
    }

    public void deleteItemFromServer(final RealmObject item, final Boolean notify) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", App.my_email);
        if (item instanceof LocalVideo) {
            param.put("media_id", ((LocalVideo) item).id);
        } else if (item instanceof LocalImage) {
            param.put("media_id", ((LocalVideo) item).id);
        } else {
            if (notify)
                notifyActionCompleted(ACTION_DELETE_FROM_SERVER, R.string.msg_network_error);
            return;
        }

        AndroidNetworking.get(Config.API_GET_DELETE_MY_MEDIA)
                .addQueryParameter(param)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url = getApiUrl();
                            Map<String, String> params = getParams();
                            int status = response.getInt("status");
                            if (status == 1) {
                                deleteItem(item, false);
                                if (notify) notifyActionCompleted(ACTION_DELETE_FROM_SERVER, 0);

                            } else {
                                String message = response.getString("message");
                                if (notify)
                                    notifyActionCompleted(ACTION_DELETE_FROM_SERVER, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (notify)
                                notifyActionCompleted(ACTION_DELETE_FROM_SERVER, R.string.msg_network_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (notify)
                            notifyActionCompleted(ACTION_DELETE_FROM_SERVER, R.string.msg_network_error);
                    }
                });
    }


    protected Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    protected String getFileName(String url) {
        String[] nodes = url.split("/");
        if (nodes.length > 0) {
            return nodes[nodes.length - 1];
        }

        return null;
    }

    public static final int ACTION_LOAD = 1;
    public static final int ACTION_CREATE = 2;
    public static final int ACTION_UPDATE = 3;
    public static final int ACTION_DELETE = 4;
    public static final int ACTION_DOWNLOADED = 5;
    public static final int ACTION_DELETE_FROM_SERVER = 6;

    public interface LoadCompletionListener {
        void finishedAction(int method, int message);

        void finishedAction(int method, String message);
    }

    protected ArrayList<LoadCompletionListener> mListeners = new ArrayList<>();

    public void addListener(LoadCompletionListener listener) {
        if (mListeners.indexOf(listener) == -1) {
            mListeners.add(listener);
        }
    }

    public void deleteListener(LoadCompletionListener listener) {
        int index = mListeners.indexOf(listener);
        if (index > -1) {
            mListeners.remove(index);
        }
    }

    protected void notifyActionCompleted(int method, int msg) {
        for (int i = 0; i < mListeners.size(); i++) {
            LoadCompletionListener itemListener = mListeners.get(i);
            itemListener.finishedAction(method, msg);
        }
    }

    protected void notifyActionCompleted(int method, String msg) {
        for (int i = 0; i < mListeners.size(); i++) {
            LoadCompletionListener itemListener = mListeners.get(i);
            itemListener.finishedAction(method, msg);
        }
    }

    protected void notifyActionProgress(float progress) {
        for (int i = 0; i < mListeners.size(); i++) {
            LoadCompletionListener itemListener = mListeners.get(i);
            if (itemListener instanceof BaseActivity) {
                ((BaseActivity) itemListener).setProgressDialogProgress((int) progress);
            }

            if (itemListener instanceof BaseFragment) {
                ((BaseFragment) itemListener).setProgressDialogProgress((int) progress);
            }
        }
    }

    public static String getWorkoutCode(int category) {
        switch (category) {
            case R.string.title_exercise:
                return "3";

            case R.string.title_mobility:
                return "1";

            case R.string.title_stretching:
                return "2";
        }

        return "3";
    }

    public static String getWorkoutCode(String category) {
        switch (category) {
            case "Exercise":
                return "3";

            case "Mobility":
                return "1";

            case "Stretching":
                return "2";
        }

        return "1";
    }

    public static String getCalegoryType(String category) {
        switch (category) {
            case "exercise":
                return "3";
            case "mobility":
                return "1";
            case "stretching":
                return "2";
            case "sports":
                return "4";
            case "cardio":
                return "5";
        }
        return "1";
    }

    public static String getCalegoryColor(String category) {
        switch (category) {
            case "3":
                return "#0238CC";
            case "1":
                return "#8AC149";
            case "2":
                return "#AC00D7";
            case "4":
                return "#C90A0A";
            case "5":
                return "#Ff8229";
        }
        return "1";
    }
}
