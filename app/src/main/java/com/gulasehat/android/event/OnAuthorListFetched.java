package com.gulasehat.android.event;

import com.gulasehat.android.model.AuthorData;

public class OnAuthorListFetched {

    private AuthorData authorData;

    public OnAuthorListFetched(AuthorData authorData) {
        this.authorData = authorData;
    }

    public AuthorData getAuthorData() {
        return authorData;
    }
}