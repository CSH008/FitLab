package com.jq.fitlab.chatsdk.backendless;

import android.content.Context;

import com.jq.chatsdk.Utils.Debug;
import com.jq.chatsdk.interfaces.BPushHandler;
import com.jq.chatsdk.interfaces.BUploadHandler;
import com.jq.chatsdk.network.BNetworkManager;

import org.jdeferred.Promise;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by Erk on 27.07.2016.
 */
public class BBackendlessHandler implements BPushHandler, BUploadHandler {

    private static final String TAG = BBackendlessHandler.class.getSimpleName();
    private static final boolean DEBUG = Debug.BBackendlessPushHandler;

    private boolean isSubscribed;
    private Context context;

    public void setContext(Context ctx) {
        context = ctx;
    }

    public void initWithAppKey(String appKey, String secretKey, String versionKey)
    {

    }

    @Override
    public boolean subscribeToPushChannel(final String channel) {
        if (!BNetworkManager.sharedManager().getNetworkAdapter().backendlessEnabled())
            return false;

        return true;
    }

    @Override
    public boolean unsubscribeToPushChannel(String channel) {
        if (!BNetworkManager.sharedManager().getNetworkAdapter().backendlessEnabled())
            return false;


        return true;
    }

    @Override
    public void pushToChannels(Collection<String> channels, JSONObject data) {

    }

    @Override
    public Promise uploadFile(byte[] data, String name, String mimeType) {
        return null;
    }
}
