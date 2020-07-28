package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class CategoryArgs {

    @SerializedName("category_id")
    private int categoryId;
    @SerializedName("sort_type")
    private String sortType;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
