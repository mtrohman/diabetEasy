package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("term_id")
    private int termID;
    @SerializedName("parent")
    private int parent;
    @SerializedName("name")
    private String name;
    @SerializedName("sub_category_status")
    private boolean subCategory;
    @SerializedName("post_count")
    private int postCount;
    @SerializedName("icon")
    private CategoryIcon icon;
    @SerializedName("is_all_contents")
    private boolean isAllContents = false;

    public Category(int termID, int parent, String name, int postCount, CategoryIcon icon, boolean isAllContents) {
        this.termID = termID;
        this.parent = parent;
        this.name = name;
        this.postCount = postCount;
        this.icon = icon;
        this.isAllContents = isAllContents;
    }

    public int getTermID() {
        return termID;
    }

    public int getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public boolean hasSubCategory() {
        return subCategory;
    }

    public int getPostCount() {
        return postCount;
    }

    public String getPostCountString(){
        return String.valueOf(getPostCount());
    }

    public CategoryIcon getIcon() {
        return icon;
    }


    public boolean isAllContents() {
        return isAllContents;
    }

    public void setAllContents(boolean allContents) {
        isAllContents = allContents;
    }
}