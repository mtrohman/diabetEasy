package com.gulasehat.android.event;

import com.gulasehat.android.model.AuthorData;

public class OnFetchedAuthorData {

    private AuthorData data;

    public OnFetchedAuthorData(AuthorData data) {
        this.data = data;
    }

    public AuthorData getData() {
        return data;
    }
}
