package com.gulasehat.android.event;

public class OnNewCommentVisibleEvent {

    private boolean isVisible;

    public OnNewCommentVisibleEvent(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
