package com.jq.app.ui.chat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.chatsdk.activities.ChatSDKMainActivity;

public class ChatActivity extends ChatSDKMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initActionBar();
        setTitle("Chats");

        App.isLoginedOnFirebase = true;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatSDKUiHelper != null)
                    chatSDKUiHelper.startPickFriendsActivity();
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
