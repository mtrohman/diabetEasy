package com.gulasehat.android.util;

import android.text.TextUtils;
import android.util.Patterns;

public class Validation {

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
