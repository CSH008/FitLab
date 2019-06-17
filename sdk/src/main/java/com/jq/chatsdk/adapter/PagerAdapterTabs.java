/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.jq.chatsdk.adapter;


import android.app.FragmentManager;

import com.astuetz.pagersslidingtabstrip.PagerSlidingTabStrip;
import com.jq.chatsdk.R;
import com.jq.chatsdk.fragments.ChatSDKBaseFragment;
import com.jq.chatsdk.fragments.ChatSDKContactsFragment;
import com.jq.chatsdk.fragments.ChatSDKConversationsFragment;
import com.jq.chatsdk.fragments.ChatSDKProfileFragment;
import com.jq.chatsdk.fragments.ChatSDKThreadsFragment;

/**
 * Created by itzik on 6/16/2014.
 */
public class PagerAdapterTabs extends AbstractChatSDKTabsAdapter implements PagerSlidingTabStrip.IconTabProvider {

    public PagerAdapterTabs(FragmentManager fm) {
        super(fm);

        fragments = new ChatSDKBaseFragment[] {ChatSDKConversationsFragment.newInstance(),
                ChatSDKThreadsFragment.newInstance(),
                ChatSDKContactsFragment.newInstance("ConvFragmentPage"),
                ChatSDKProfileFragment.newInstance()};

        icnns = new int[] {R.drawable.ic_action_private, R.drawable.ic_action_public, R.drawable.ic_action_contacts, R.drawable.ic_action_user };
    }

}
