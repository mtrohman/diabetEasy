package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class SearchArgs {

    @SerializedName("s")
    private String query;
    @SerializedName("type")
    private String type;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("paged")
    private int page = 1;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
