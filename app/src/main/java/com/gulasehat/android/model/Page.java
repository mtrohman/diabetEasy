package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class Page {

    @SerializedName("ID")
    private int id;
    @SerializedName("post_title")
    private String pageTitle;
    @SerializedName("post_date")
    private Long pageDate;
    @SerializedName("post_content")
    private String pageContent;
    @SerializedName("guid")
    private String pageURL;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("total_favorite")
    private int totalFavorite = 0;
    @SerializedName("featured_image")
    private String featuredImage;
    @SerializedName("comments")
    private Comments comments;
    @SerializedName("author")
    private User user;

    public int getId() {
        return id;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public Long getPageDate() {
        return pageDate;
    }

    public String getPageContent() {
        return pageContent;
    }

    public String getPageURL() {
        return pageURL;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getTotalFavorite() {
        return totalFavorite;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public Comments getComments() {
        return comments;
    }

    public User getUser() {
        return user;
    }

    public String getPageDateFormatted(){

        // Custom date format
        PrettyTime p = new PrettyTime(Locale.getDefault());
        return p.format(new Date(getPageDate()));
    }
}
