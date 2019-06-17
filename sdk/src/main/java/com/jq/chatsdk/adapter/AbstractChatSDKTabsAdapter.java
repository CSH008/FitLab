/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.jq.chatsdk.adapter;


import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.astuetz.pagersslidingtabstrip.PagerSlidingTabStrip;
import com.jq.chatsdk.R;
import com.jq.chatsdk.fragments.ChatSDKBaseFragment;
import com.jq.chatsdk.fragments.ChatSDKContactsFragment;
import com.jq.chatsdk.fragments.ChatSDKConversationsFragment;
import com.jq.chatsdk.fragments.ChatSDKThreadsFragment;
import com.jq.chatsdk.fragments.ChatcatProfileFragment;

/**
 * Created by itzik on 6/16/2014.
 */
public class AbstractChatSDKTabsAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    protected final String[] titles = {"Conversations", "Chat Rooms", "Contacts", "Profile"};

    protected ChatSDKBaseFragment[] fragments = new ChatSDKBaseFragment[] {ChatSDKConversationsFragment.newInstance(), ChatSDKThreadsFragment.newInstance(), ChatSDKContactsFragment.newInstance("ConvFragmentPage"), ChatcatProfileFragment.newInstance()};

    protected int[] icnns = new int[] {R.drawable.ic_action_private, R.drawable.ic_action_public, R.drawable.ic_action_contacts, R.drawable.ic_action_user };

    public static final int Profile = 3, ChatRooms = 1, Contacts = 2, Conversations = 0;

    public AbstractChatSDKTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public ChatSDKBaseFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getPageIconResId(int position) {
        return icnns[position];
    }
}
