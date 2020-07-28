package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class CommentArgs {

    @SerializedName("post_id")
    private int postId;
    @SerializedName("page")
    private int page = 1;
    @SerializedName("sort_type")
    private String sortType;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

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
