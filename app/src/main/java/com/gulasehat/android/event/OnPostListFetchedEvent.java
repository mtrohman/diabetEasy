package com.gulasehat.android.event;

import com.gulasehat.android.model.PostData;

public class OnPostListFetchedEvent {

    private PostData postData;

    public OnPostListFetchedEvent(PostData postData) {
        this.postData = postData;
    }

    public PostData getPostData() {
        return postData;
    }
}