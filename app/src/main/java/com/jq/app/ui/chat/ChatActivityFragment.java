package com.jq.app.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jq.chatsdk.fragments.ChatSDKConversationsFragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatActivityFragment extends ChatSDKConversationsFragment {

    public static ChatActivityFragment newInstance() {
        return new ChatActivityFragment();
    }

    public ChatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
