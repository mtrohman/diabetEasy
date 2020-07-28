package com.gulasehat.android.event;

public class OnCategoryChangedEvent {

    private int count;

    public OnCategoryChangedEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
