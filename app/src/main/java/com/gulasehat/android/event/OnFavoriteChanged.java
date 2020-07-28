package com.gulasehat.android.event;

public class OnFavoriteChanged {

    private int postId;
    private boolean fav;

    public OnFavoriteChanged(int postId, boolean fav) {
        this.postId = postId;
        this.fav = fav;
    }

    public int getPostId() {
        return postId;
    }

    public boolean isFav() {
        return fav;
    }
}
