package com.gulasehat.android.model;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

public class CategoryIcon {

    @SerializedName("family")
    private String categoryIconSet;
    @SerializedName("bg_color")
    private String categoryBgColor;
    @SerializedName("color")
    private String categoryFontColor;
    @SerializedName("unicode")
    private String categoryIcon;
    @SerializedName("style_tag")
    private String styleTag;

    public CategoryIcon(String categoryIconSet, String categoryBgColor, String categoryFontColor, String categoryIcon) {
        this.categoryIconSet = categoryIconSet;
        this.categoryBgColor = categoryBgColor;
        this.categoryFontColor = categoryFontColor;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryIconSet() {
        return categoryIconSet;
    }

    public String getCategoryBgColor() {
        return categoryBgColor;
    }

    public String getCategoryFontColor() {
        return categoryFontColor;
    }

    public Spanned getCategoryIcon() {
        return Html.fromHtml(categoryIcon);
    }

    public void setCategoryIconSet(String categoryIconSet) {
        this.categoryIconSet = categoryIconSet;
    }

    public void setCategoryBgColor(String categoryBgColor) {
        this.categoryBgColor = categoryBgColor;
    }

    public void setCategoryFontColor(String categoryFontColor) {
        this.categoryFontColor = categoryFontColor;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getStyleTag() {
        return styleTag;
    }
}