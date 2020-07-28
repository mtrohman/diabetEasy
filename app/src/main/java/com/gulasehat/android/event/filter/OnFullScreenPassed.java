package com.gulasehat.android.event.filter;

import android.view.View;

public class OnFullScreenPassed {
    private View view;

    public OnFullScreenPassed(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
