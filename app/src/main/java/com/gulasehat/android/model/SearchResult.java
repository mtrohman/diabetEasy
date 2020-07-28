package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("post")
    private PostData postData;
    @SerializedName("page")
    private PostData pageData;
    @SerializedName("author")
    private AuthorData authorData;
    private String query;
    private SearchArgs args;

    public PostData getPostData() {
        return postData;
    }

    public PostData getPageData() {
        return pageData;
    }

    public AuthorData getAuthorData() {
        return authorData;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SearchArgs getArgs() {
        return args;
    }

    public void setArgs(SearchArgs args) {
        this.args = args;
    }
}
