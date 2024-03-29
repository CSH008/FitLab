/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.jq.chatsdk.network.events;

import com.jq.chatsdk.dao.BUser;
import com.jq.chatsdk.interfaces.AppEvents;

public abstract class UserEventListener extends Event implements AppEvents{
    public UserEventListener(String tag, String userEntityId){
        super(tag, userEntityId);
    }

    public UserEventListener(String tag){
        super(tag, "");
    }


    @Override
    public abstract boolean onUserDetailsChange(BUser user);
}
