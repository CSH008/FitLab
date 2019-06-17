package com.jq.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.ui.MainActivity;
import com.jq.app.util.Common;
import com.jq.app.util.Constants;


public class SplashActivity extends BaseAuthActivity {

    private static final int DISPLAY_DATA = 1;
    private static final int DELAY_DEVELOP = 3000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Do task here
            if (msg.what == DISPLAY_DATA) {
                if(Common.getStringWithValueKey(SplashActivity.this, Constants.KEY_REMEMBER_LOGIN).equals("true")) {
                    String email = Common.getStringWithValueKey(SplashActivity.this, Constants.KEY_USER_EMAIL);
                    App.my_email = email;
                    //startActivity(new Intent(SplashActivity.this, MainActivityPlanner.class));
                    loginFirebase();

                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish()    ;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(DISPLAY_DATA, DELAY_DEVELOP);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void succeedFirebaseAuthenticate() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
