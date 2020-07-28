package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class PostArgs {

    @SerializedName("post_type")
    private String postType = "post";

    @SerializedName("cat")
    private Integer category;

    @SerializedName("paged")
    private Integer page = 1;

    @SerializedName("author")
    private Integer author;

    @SerializedName("year")
    private Integer year;

    @SerializedName("monthnum")
    private Integer month;

    @SerializedName("tag")
    private String tag;

    @SerializedName("sort_type")
    private String sortType;

    @SerializedName("search")
    private String search;

    @SerializedName("favorites")
    private boolean favorites = false;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }
}
