package com.gulasehat.android.event;

public class OnPostCategoryChangedEvent {

    int category;

    public OnPostCategoryChangedEvent(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }
}
