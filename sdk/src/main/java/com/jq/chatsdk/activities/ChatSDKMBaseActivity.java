package com.jq.chatsdk.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jq.chatsdk.R;

/**
 * Created by Administrator on 1/3/2018.
 */

public class ChatSDKMBaseActivity extends AppCompatActivity {

    protected void initActionBar() {
        Toolbar toolbar = findViewById(R.id.chat_sdk_toolbar);
        if(toolbar==null) return;
        setSupportActionBar(toolbar);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
        View btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(onClickListener);
    }

    public void setTitle(String title) {
        TextView txtName = findViewById(R.id.chat_sdk_name);
        if(txtName!=null) txtName.setText(title);
    }

}
