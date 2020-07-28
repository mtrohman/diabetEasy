package com.gulasehat.android.event;

import com.gulasehat.android.model.CategoryData;

public class OnFetchedCategoryData {

    private CategoryData categoryData;

    public OnFetchedCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }
}
