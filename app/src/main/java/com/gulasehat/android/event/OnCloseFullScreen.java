package com.gulasehat.android.event;

import android.view.View;

public class OnCloseFullScreen {
    private View view;

    public OnCloseFullScreen(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
