package com.gulasehat.android.event;

import android.view.View;

public class OnOpenFullScreen {

    private View view;

    public OnOpenFullScreen(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
