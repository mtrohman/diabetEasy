package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.Date;

public class Notification {

    @SerializedName("id")
    private int id;
    @SerializedName("notification_title")
    private String title;
    @SerializedName("notification_link")
    private String link;
    @SerializedName("notification_content")
    private String content;
    @SerializedName("notification_time")
    private long time;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public String getTimeFormatted(){

        Date date = new Date(getTime());

        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
        return dateFormatter.format(date);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
