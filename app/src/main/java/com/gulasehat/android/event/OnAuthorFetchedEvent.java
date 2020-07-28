package com.gulasehat.android.event;

import com.gulasehat.android.model.User;

public class OnAuthorFetchedEvent {

    private User user;

    public OnAuthorFetchedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
