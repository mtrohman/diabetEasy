package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post {

    public static final String PAGE = "page";
    public static final String POST = "post";

    @SerializedName("ID")
    private int id;
    @SerializedName("post_type")
    private String postType;
    @SerializedName("post_title")
    private String postTitle;
    @SerializedName("post_date")
    private Long postDate;
    @SerializedName("post_source_url")
    private String postSourceUrl;
    @SerializedName("guid")
    private String postURL;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("comment_status")
    private boolean commentAvailable;
    @SerializedName("is_favorite")
    private boolean favorite;
    @SerializedName("is_protected")
    private boolean isProtected;
    @SerializedName("favorite_count")
    private int favoriteCount = 0;
    @SerializedName("featured_image")
    private String featuredImage;
    @SerializedName("comments")
    private Comments comments;
    @SerializedName("tags")
    private ArrayList<Tag> tags;
    @SerializedName("author")
    private Author author;
    @SerializedName("categories")
    private ArrayList<Category> categories;
    @SerializedName("galleries")
    private ArrayList<ArrayList<Photo>> galleries;
    @SerializedName("sounds")
    private ArrayList<ArrayList<Sound>> sounds;
    @SerializedName("videos")
    private ArrayList<ArrayList<Video>> videos;
    @SerializedName("other_posts")
    private PostData otherPosts;


    public int getId() {
        return id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public Long getPostDate() {
        return postDate;
    }

    public String getPostSourceUrl() {
        return postSourceUrl;
    }

    public String getPostURL() {
        return postURL;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isFavorite() {
        return favorite;
    }


    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public Comments getComments() {
        return comments;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public Author getAuthor() {
        return author;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }


    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public ArrayList<ArrayList<Photo>> getGalleries() {
        return galleries;
    }

    public ArrayList<ArrayList<Sound>> getSounds() {
        return sounds;
    }

    public ArrayList<ArrayList<Video>> getVideos() {
        return videos;
    }

    public boolean isCommentAvailable() {
        return commentAvailable;
    }


    public String getPostType() {
        return postType;
    }

    public PostData getOtherPosts() {
        return otherPosts;
    }
}
