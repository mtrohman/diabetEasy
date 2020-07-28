package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AdBanner {

    @SerializedName("status")
    private boolean status;
    @SerializedName("network")
    private String network;
    @SerializedName("publisher_id")
    private String publisherId;
    @SerializedName("ad_unit_id")
    private String adUnitId;
    @SerializedName("ad_placement_id")
    private String adPlacementId;
    @SerializedName("home_page")
    private boolean homePage;
    @SerializedName("categories")
    private boolean categories;
    @SerializedName("post_list")
    private boolean postList;
    @SerializedName("post_page_detail")
    private boolean postPageDetail;
    @SerializedName("page_list")
    private boolean pageList;
    @SerializedName("author_list")
    private boolean authorList;
    @SerializedName("archive")
    private boolean archive;
    @SerializedName("tag_cloud")
    private boolean tagCloud;
    @SerializedName("social_accounts")
    private boolean socialAccounts;
    @SerializedName("search")
    private boolean search;
    @SerializedName("faq")
    private boolean faq;
    @SerializedName("notifications")
    private boolean notifications;


    public boolean isStatus() {
        return status;
    }

    public String getNetwork() {
        return network;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public String getAdPlacementId() {
        return adPlacementId;
    }

    public boolean isHomePage() {
        return homePage;
    }

    public boolean isCategories() {
        return categories;
    }

    public boolean isPostList() {
        return postList;
    }

    public boolean isPostPageDetail() {
        return postPageDetail;
    }

    public boolean isPageList() {
        return pageList;
    }

    public boolean isAuthorList() {
        return authorList;
    }

    public boolean isArchive() {
        return archive;
    }

    public boolean isTagCloud() {
        return tagCloud;
    }

    public boolean isSocialAccounts() {
        return socialAccounts;
    }

    public boolean isSearch() {
        return search;
    }

    public boolean isFaq() {
        return faq;
    }

    public boolean isNotifications() {
        return notifications;
    }
}
