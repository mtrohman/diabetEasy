package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AdInterstitial {

    @SerializedName("network")
    private String network;
    @SerializedName("status")
    private boolean status = false;
    @SerializedName("app_id")
    private String appId;
    @SerializedName("ad_unit_id")
    private String adUnitId;
    @SerializedName("ad_placement_id")
    private String adPlacementId;
    @SerializedName("opened_post_page_detail")
    private boolean openedPostPageDetail = false;
    @SerializedName("closed_post_page_detail")
    private boolean closedPostPageDetail = false;
    @SerializedName("opened_category_lists")
    private boolean openedCategoryList = false;
    @SerializedName("closed_category_lists")
    private boolean closedCategoryList = false;
    @SerializedName("opened_post_lists")
    private boolean openedPostList = false;
    @SerializedName("closed_post_lists")
    private boolean closedPostList = false;
    @SerializedName("opened_page_list")
    private boolean openedPageList = false;
    @SerializedName("closed_page_list")
    private boolean closedPageList = false;
    @SerializedName("opened_author_list")
    private boolean openedAuthorList = false;
    @SerializedName("closed_author_list")
    private boolean closedAuthorList = false;
    @SerializedName("opened_author_user_profile")
    private boolean openedAuthorUserProfile = false;
    @SerializedName("closed_author_user_profile")
    private boolean closedAuthorUserProfile = false;
    @SerializedName("opened_archive_pages")
    private boolean openedArchivePages = false;
    @SerializedName("closed_archive_pages")
    private boolean closedArchivePages = false;
    @SerializedName("opened_tag_cloud")
    private boolean openedTagCloud = false;
    @SerializedName("closed_tag_cloud")
    private boolean closedTagCloud = false;

    public String getNetwork() {
        return network;
    }

    public boolean isStatus() {
        return status;
    }

    public String getAppId() {
        return appId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public String getAdPlacementId() {
        return adPlacementId;
    }

    public boolean isOpenedPostPageDetail() {
        return openedPostPageDetail;
    }

    public boolean isClosedPostPageDetail() {
        return closedPostPageDetail;
    }

    public boolean isOpenedCategoryList() {
        return openedCategoryList;
    }

    public boolean isClosedCategoryList() {
        return closedCategoryList;
    }

    public boolean isOpenedPostList() {
        return openedPostList;
    }

    public boolean isClosedPostList() {
        return closedPostList;
    }

    public boolean isOpenedPageList() {
        return openedPageList;
    }

    public boolean isClosedPageList() {
        return closedPageList;
    }

    public boolean isOpenedAuthorList() {
        return openedAuthorList;
    }

    public boolean isClosedAuthorList() {
        return closedAuthorList;
    }

    public boolean isOpenedAuthorUserProfile() {
        return openedAuthorUserProfile;
    }

    public boolean isClosedAuthorUserProfile() {
        return closedAuthorUserProfile;
    }

    public boolean isOpenedArchivePages() {
        return openedArchivePages;
    }

    public boolean isClosedArchivePages() {
        return closedArchivePages;
    }

    public boolean isOpenedTagCloud() {
        return openedTagCloud;
    }

    public boolean isClosedTagCloud() {
        return closedTagCloud;
    }
}
