package com.gulasehat.android.event;

import com.gulasehat.android.model.PostData;

public class OnFetchedPageData {

    private PostData data;

    public OnFetchedPageData(PostData data) {
        this.data = data;
    }

    public PostData getData() {
        return data;
    }
}
