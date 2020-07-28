package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AuthorArgs {

    @SerializedName("paged")
    private int page = 1;
    @SerializedName("sort_type")
    private String sortType = "display_name|asc";

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
