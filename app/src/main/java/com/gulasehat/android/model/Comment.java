package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("comment_ID")
    private int commentID;
    @SerializedName("comment_post_ID")
    private int commentPostID;
    @SerializedName("comment_author")
    private String commentAuthor;
    @SerializedName("comment_date")
    private long commentDate;
    @SerializedName("comment_content")
    private String commentContent;
    @SerializedName("comment_author_avatar")
    private String commentAuthorAvatar;
    @SerializedName("comment_like_count")
    private int commentLikeCount;
    @SerializedName("comment_unlike_count")
    private int commentUnlikeCount;
    @SerializedName("is_rated")
    private boolean isRated;
    @SerializedName("comment_approved")
    private boolean isApproved;
    @SerializedName("user_id")
    private int commentUserID;

    public int getCommentID() {
        return commentID;
    }

    public int getCommentPostID() {
        return commentPostID;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public long getCommentDate() {
        return commentDate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentAuthorAvatar() {
        return commentAuthorAvatar;
    }

    public int getCommentLikeCount() {
        return commentLikeCount;
    }

    public int getCommentUnlikeCount() {
        return commentUnlikeCount;
    }

    public boolean isRated() {
        return isRated;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public int getCommentUserID() {
        return commentUserID;
    }

    public void setCommentLikeCount(int commentLikeCount) {
        this.commentLikeCount = commentLikeCount;
    }

    public void setCommentUnlikeCount(int commentUnlikeCount) {
        this.commentUnlikeCount = commentUnlikeCount;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }
}
