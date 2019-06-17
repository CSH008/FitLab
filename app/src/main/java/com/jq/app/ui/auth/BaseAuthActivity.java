package com.jq.app.ui.auth;

import android.content.Intent;
import com.jq.app.App;
import com.jq.app.util.Common;
import com.jq.app.util.Config;
import com.jq.app.util.Constants;
import com.jq.app.util.base.BaseActivity;
import com.jq.chatsdk.activities.ChatSDKMainActivity;
import com.jq.chatsdk.dao.BUser;
import com.jq.chatsdk.dao.core.DaoCore;
import com.jq.chatsdk.network.AbstractNetworkAdapter;
import com.jq.chatsdk.network.BDefines;
import com.jq.chatsdk.network.BNetworkManager;
import com.jq.chatsdk.object.BError;
import org.apache.commons.lang3.StringUtils;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import java.util.Map;
/**
 * Created by Administrator on 12/1/2017.
 */

public class BaseAuthActivity extends BaseActivity {

    public void loginFirebase() {
        String email = App.my_email;
        Map<String, Object> data = AbstractNetworkAdapter.getMap(
                new String[]{BDefines.Prefs.LoginTypeKey, BDefines.Prefs.LoginEmailKey, BDefines.Prefs.LoginPasswordKey},
                BDefines.BAccountType.Password, email, Config.FIREBASE_USER_PASSWORD);

        BNetworkManager.sharedManager().getNetworkAdapter()
                .authenticateWithMap(data).done(new DoneCallback<Object>() {
            @Override
            public void onDone(Object o) {
                afterLogin();

            }
        }).fail(new FailCallback<BError>() {
            @Override
            public void onFail(BError bError) {
                showToast(bError.message);
                failedFirebaseAuthenticate();
            }
        });
    }

    public void registerFirebase() {
        final String email = App.my_email;
        Map<String, Object> data = AbstractNetworkAdapter.getMap(
                new String[]{BDefines.Prefs.LoginTypeKey, BDefines.Prefs.LoginEmailKey, BDefines.Prefs.LoginPasswordKey },
                BDefines.BAccountType.Register, email, Config.FIREBASE_USER_PASSWORD);

        BNetworkManager.sharedManager().getNetworkAdapter()
                .authenticateWithMap(data).done(new DoneCallback<Object>() {
            @Override
            public void onDone(Object o) {
                /*String username = Common.getStringWithValueKey(BaseAuthActivity.this, Constants.KEY_USERNAME);
                if(username != null && !TextUtils.isEmpty(username)) {
                    BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                    user.setMetadataString(BDefines.Keys.BName, email);
                    BNetworkManager.sharedManager().getNetworkAdapter().pushUser();
                }*/
                BUser user = BNetworkManager.sharedManager().getNetworkAdapter().currentUserModel();
                user.setMetadataString(BDefines.Keys.BName, email);
                BNetworkManager.sharedManager().getNetworkAdapter().pushUser();

                afterLogin();
                showProgressView(false);

            }
        }).fail(new FailCallback<BError>() {
            @Override
            public void onFail(BError bError) {
                if(bError.message.equals("The email address is already in use by another account")) {
                    loginFirebase();

                } else {
                    failedFirebaseAuthenticate();
                    showToast(bError.message);
                }
            }
        });
    }

    public AbstractNetworkAdapter getNetworkAdapter() {
        return BNetworkManager.sharedManager().getNetworkAdapter();
    }

    protected void afterLogin() {
        App.isLoginedOnFirebase = true;
        Common.saveStringWithKeyValue(mContext, Constants.KEY_REGISTERED_ON_FIREBASE, Constants.KEY_TRUE);
        // Indexing the user.
        BUser currentUser = getNetworkAdapter().currentUserModel();
        if(getNetworkAdapter().currentUserModel() != null) {
            getNetworkAdapter().pushUser();
        }

        Intent logout = new Intent(ChatSDKMainActivity.Action_clear_data);
        sendBroadcast(logout);

        String version = BDefines.BAppVersion,
                metaVersion = currentUser.metaStringForKey(BDefines.Keys.BVersion);

        if (StringUtils.isNotEmpty(version))
        {
            if (StringUtils.isEmpty(metaVersion) || !metaVersion.equals(version))
            {
                currentUser.setMetadataString(BDefines.Keys.BVersion, version);
            }

            DaoCore.updateEntity(currentUser);
        }

        succeedFirebaseAuthenticate();
    }

    public void succeedFirebaseAuthenticate() {}

    public void failedFirebaseAuthenticate() {}

}
