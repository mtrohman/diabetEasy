package com.gulasehat.android.event;

import com.gulasehat.android.model.SearchResult;

public class OnSearchResultEvent {

    private SearchResult result;

    public OnSearchResultEvent(SearchResult result) {
        this.result = result;
    }

    public SearchResult getResult() {
        return result;
    }
}
