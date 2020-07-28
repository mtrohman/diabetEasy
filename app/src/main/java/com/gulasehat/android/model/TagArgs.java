package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class TagArgs {

    @SerializedName("page")
    private int page;
    @SerializedName("sort_type")
    private String sortType;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
