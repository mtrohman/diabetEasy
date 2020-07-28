package com.gulasehat.android.util;

import android.content.Context;
import com.gulasehat.android.R;
import com.gulasehat.android.event.OnFavoriteChanged;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.service.PostService;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.EventBus;

public class FavoriteManager {

    public static void handle(final Context context, final int postId, final boolean fav){

        if(!Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
            Alert.make(context, R.string.login_required, Alert.ALERT_TYPE_WARNING);
            return;
        }

        PostService.favorite(postId, fav, new PostService.OnFavoriteChangeListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                EventBus.getDefault().postSticky(new OnFavoriteChanged(postId, fav));
            }

            @Override
            public void onFailed(ApiResponse response) {
                EventBus.getDefault().postSticky(new OnFavoriteChanged(postId, !fav));
                Alert.make(context, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });
    }
}
