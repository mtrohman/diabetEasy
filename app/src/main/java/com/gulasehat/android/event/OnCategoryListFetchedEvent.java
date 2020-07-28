package com.gulasehat.android.event;

import com.gulasehat.android.model.CategoryData;

public class OnCategoryListFetchedEvent {

    private CategoryData categoryData;

    public OnCategoryListFetchedEvent(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }
}