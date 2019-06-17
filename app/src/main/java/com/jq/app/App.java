package com.jq.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.crashlytics.android.Crashlytics;
import com.jq.app.ui.MainActivity;
import com.jq.app.ui.auth.LoginActivity;
import com.jq.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.jq.chatsdk.activities.ChatSDKChatActivity;
import com.jq.chatsdk.network.BDefines;
import com.jq.chatsdk.network.BNetworkManager;
import com.jq.fitlab.chatsdk.BChatcatNetworkAdapter;
import com.jq.fitlab.chatsdk.BuildConfig;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * Created by itzik on 6/8/2014.
 */
public class App extends MultiDexApplication {

    public static boolean isLoginedOnFirebase = false;
    public static String my_email = null;

    @Override
    public void onCreate() {

        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Realm.init(this);
//        AndroidNetworking.initialize(getApplicationContext());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //Set the Debug Log mode
        builder.addInterceptor(loggingInterceptor);

        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

        Context context = getApplicationContext();

        // Setting the version name of the sdk, This data will be added
        // to the user metadata and will help in future when doing code updating.
        BDefines.BAppVersion = BuildConfig.VERSION_NAME;

        MultiDex.install(this);

        // Logging tool.
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.HollowTree());
        }

        // Android chat SDK init!
        ChatSDKUiHelper.init(ChatSDKChatActivity.class, MainActivity.class, LoginActivity.class);
        BNetworkManager.init(getApplicationContext());
        // Adapter init.
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);
    }

    @Override
    protected void attachBaseContext (Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
