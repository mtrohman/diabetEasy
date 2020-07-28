package com.gulasehat.android.listener;

import com.gulasehat.android.model.AuthUser;

public interface OnLoginListener {

    void onLoginSuccess(AuthUser authUser);
    void onLoginFailed();

}
