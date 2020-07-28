package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostData {

    @SerializedName("data")
    private ArrayList<Post> posts;
    @SerializedName("total_data")
    private int postCount = 0;
    @SerializedName("total_data_count_status")
    private boolean postCountStatus;
    @SerializedName("search_status")
    private boolean searchStatus;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("layout")
    private int layout;
    @SerializedName("limit")
    private int limit;
    @SerializedName("paged")
    private int page;

    public PostData(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public int getPostCount() {
        return postCount;
    }

    public boolean getPostCountStatus() {
        return postCountStatus;
    }

    public boolean isSearchStatus() {
        return searchStatus;
    }

    public String getSortType() {
        return sortType;
    }

    public int getLayout() {
        return layout;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }
}
