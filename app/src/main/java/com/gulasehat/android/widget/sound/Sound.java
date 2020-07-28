package com.gulasehat.android.widget.sound;

public class Sound{

    private int id;
    private String title;
    private String source;
    private String image;
    private int length;

    public Sound(int id, String title, String source, String image, int length) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.image = image;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getImage() {
        return image;
    }

    public int getLength() {
        return length;
    }
}