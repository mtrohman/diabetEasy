package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AuthorData {

    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("layout")
    private int layout;
    @SerializedName("total_data")
    private int totalData;
    @SerializedName("total_data_count_status")
    private boolean totalDataCountStatus;
    @SerializedName("post_count_status")
    private boolean postCountStatus;
    @SerializedName("data")
    private ArrayList<Author> authors;
    @SerializedName("limit")
    private int limit;
    @SerializedName("paged")
    private int page;

    public String getSortType() {
        return sortType;
    }

    public int getLayout() {
        return layout;
    }

    public int getTotalData() {
        return totalData;
    }

    public boolean isTotalDataCountStatus() {
        return totalDataCountStatus;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public boolean getPostCountStatus() {
        return postCountStatus;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }
}
