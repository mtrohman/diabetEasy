package com.gulasehat.android.event;

import com.gulasehat.android.model.Year;

public class OnArchiveChangeEvent {

    private Year year;

    public OnArchiveChangeEvent(Year year) {
        this.year = year;
    }

    public Year getYear() {
        return year;
    }
}
