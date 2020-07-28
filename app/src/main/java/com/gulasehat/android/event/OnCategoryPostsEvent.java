package com.gulasehat.android.event;

import com.gulasehat.android.model.Category;

public class OnCategoryPostsEvent {

    private Category category;

    public OnCategoryPostsEvent(Category category) {
        this.category = category;
    }

    public OnCategoryPostsEvent() {
    }

    public Category getCategory() {
        return category;
    }
}
