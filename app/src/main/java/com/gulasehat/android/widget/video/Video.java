package com.gulasehat.android.widget.video;

public class Video {

    private String title;
    private String source;
    private String img;
    private int length;

    public Video(String title, String source, String img, int length) {
        this.title = title;
        this.source = source;
        this.img = img;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getImg() {
        return img;
    }

    public int getLength() {
        return length;
    }
}
