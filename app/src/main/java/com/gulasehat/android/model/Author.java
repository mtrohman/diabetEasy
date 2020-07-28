package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Author {

    @SerializedName("ID")
    private int authorID;
    @SerializedName("user_nicename")
    private String authorName;
    @SerializedName("display_name")
    private String authorFullName;
    @SerializedName("user_email")
    private String authorEmail;
    @SerializedName("user_url")
    private String authorWebsite;
    @SerializedName("user_biographical_info")
    private String authorBiographicalInfo;
    @SerializedName("user_gender")
    private String authorGender;
    @SerializedName("user_job")
    private String authorJob;
    @SerializedName("user_thumb")
    private String authorThumb;
    @SerializedName("user_picture")
    private String authorPicture;
    @SerializedName("user_post_categories")
    private ArrayList<Category> authorPostCategories;
    @SerializedName("user_post_count")
    private int authorPostCount;
    @SerializedName("user_posts")
    private PostData authorPosts;
    @SerializedName("user_cookie_hash")
    private String userCookieHash;
    @SerializedName("is_author")
    private boolean author;

    public int getAuthorID() {
        return authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getAuthorWebsite() {
        return authorWebsite;
    }

    public String getAuthorBiographicalInfo() {
        return authorBiographicalInfo;
    }

    public String getAuthorGender() {
        return authorGender;
    }

    public String getAuthorJob() {
        return authorJob;
    }

    public String getAuthorThumb() {
        return authorThumb;
    }

    public String getAuthorPicture() {
        return authorPicture;
    }

    public ArrayList<Category> getAuthorPostCategories() {
        return authorPostCategories;
    }

    public int getAuthorPostCount() {
        return authorPostCount;
    }

    public PostData getAuthorPosts() {
        return authorPosts;
    }

    public String getUserCookieHash() {
        return userCookieHash;
    }

    public boolean isAuthor() {
        return author;
    }
}
