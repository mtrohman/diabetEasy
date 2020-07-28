package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {

    @SerializedName("user_id")
    private int userID;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("user_fullname")
    private String userFullName;
    @SerializedName("user_email")
    private String userEmail;
    @SerializedName("user_website")
    private String userWebsite;
    @SerializedName("user_biographical_info")
    private String userBiographicalInfo;
    @SerializedName("user_gender")
    private String userGender;
    @SerializedName("user_job")
    private String userJob;
    @SerializedName("user_thumb")
    private String userThumb;
    @SerializedName("user_picture")
    private String userPicture;
    @SerializedName("user_has_picture")
    private boolean hasPicture = false;
    @SerializedName("user_post_categories")
    private ArrayList<Category> userPostCategories;
    @SerializedName("user_post_count")
    private int userPostCount;
    @SerializedName("is_author")
    private boolean author;


    @SerializedName("user_profile_email_status")
    private boolean hasEmail;
    @SerializedName("user_profile_website_status")
    private boolean hasWebsite;
    @SerializedName("user_profile_biographical_info_status")
    private boolean hasBio;
    @SerializedName("user_profile_gender_status")
    private boolean hasGender;
    @SerializedName("user_profile_job_status")
    private boolean hasJob;

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserWebsite() {
        return userWebsite;
    }

    public String getUserBiographicalInfo() {
        return userBiographicalInfo;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserJob() {
        return userJob;
    }

    public String getUserThumb() {
        return userThumb;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public ArrayList<Category> getUserPostCategories() {
        return userPostCategories;
    }

    public int getUserPostCount() {
        return userPostCount;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserWebsite(String userWebsite) {
        this.userWebsite = userWebsite;
    }

    public void setUserBiographicalInfo(String userBiographicalInfo) {
        this.userBiographicalInfo = userBiographicalInfo;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public void setUserThumb(String userThumb) {
        this.userThumb = userThumb;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public void setUserPostCategories(ArrayList<Category> userPostCategories) {
        this.userPostCategories = userPostCategories;
    }

    public void setUserPostCount(int userPostCount) {
        this.userPostCount = userPostCount;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    public boolean hasEmail() {
        return hasEmail;
    }

    public boolean hasWebsite() {
        return hasWebsite;
    }

    public boolean hasBio() {
        return hasBio;
    }

    public boolean hasGender() {
        return hasGender;
    }

    public boolean hasJob() {
        return hasJob;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public boolean hasPicture() {
        return hasPicture;
    }
}
