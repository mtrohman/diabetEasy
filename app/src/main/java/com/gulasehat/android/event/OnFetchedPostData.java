package com.gulasehat.android.event;

import com.gulasehat.android.model.PostData;

public class OnFetchedPostData {

    private PostData data;

    public OnFetchedPostData(PostData data) {
        this.data = data;
    }

    public PostData getData() {
        return data;
    }
}
