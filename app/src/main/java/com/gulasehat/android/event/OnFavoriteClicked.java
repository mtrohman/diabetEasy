package com.gulasehat.android.event;

public class OnFavoriteClicked {

    private int postId;
    private boolean fav;

    public OnFavoriteClicked(int postId, boolean fav) {
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
