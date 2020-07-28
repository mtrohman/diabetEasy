package com.gulasehat.android;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.gulasehat.android.event.ScrollChangeEvent;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.EventBus;

public class WebViewJavaScriptInterface {
    Context mContext;

    WebViewJavaScriptInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showSuccessAlert(String message){
        Alert.make(mContext, message, Alert.ALERT_TYPE_SUCCESS);
    }

    @JavascriptInterface
    public void showErrorAlert(String message){
        Alert.make(mContext, message, Alert.ALERT_TYPE_WARNING);
    }

    @JavascriptInterface
    public void scrollToPosition(int position){
        EventBus.getDefault().post(new ScrollChangeEvent(position, false));
    }

    @JavascriptInterface
    public void smoothScrollToPosition(int position){
        EventBus.getDefault().post(new ScrollChangeEvent(position, true));
    }
}
