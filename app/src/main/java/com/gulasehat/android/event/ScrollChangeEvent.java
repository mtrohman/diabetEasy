package com.gulasehat.android.event;

public class ScrollChangeEvent {

    private int position;
    private boolean smooth = false;

    public ScrollChangeEvent(int position) {
        this.position = position;
    }

    public ScrollChangeEvent(int position, boolean smooth) {
        this.position = position;
        this.smooth = smooth;
    }

    public int getPosition() {
        return position;
    }

    public boolean isSmooth() {
        return smooth;
    }
}
