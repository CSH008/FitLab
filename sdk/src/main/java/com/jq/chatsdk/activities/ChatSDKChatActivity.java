/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.jq.chatsdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.jq.chatsdk.R;
import com.jq.chatsdk.Utils.Debug;
import com.jq.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.jq.chatsdk.activities.abstracted.ChatSDKAbstractChatActivity;

/**
 * Created by itzik on 6/8/2014.
 */
public class ChatSDKChatActivity extends ChatSDKAbstractChatActivity implements AbsListView.OnScrollListener{

    private static final String TAG = ChatSDKChatActivity.class.getSimpleName();
    private static final boolean DEBUG = Debug.ChatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_sdk_activity_chat);
        initActionBar();

        initViews();

        chatSDKChatHelper.checkIfWantToShare(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home) {
            startActivity(new Intent(this, ChatSDKUiHelper.getInstance().mainActivity));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
