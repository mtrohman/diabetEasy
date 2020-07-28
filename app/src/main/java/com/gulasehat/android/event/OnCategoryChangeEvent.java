package com.gulasehat.android.event;

import com.gulasehat.android.model.Category;

public class OnCategoryChangeEvent {

    private Category category;

    public OnCategoryChangeEvent(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
