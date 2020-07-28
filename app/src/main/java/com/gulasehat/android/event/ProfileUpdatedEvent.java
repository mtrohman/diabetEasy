package com.gulasehat.android.event;

import com.gulasehat.android.model.AuthUser;

public class ProfileUpdatedEvent {

    private AuthUser user;

    public ProfileUpdatedEvent(AuthUser user) {
        this.user = user;
    }

    public AuthUser getUser() {
        return user;
    }
}
