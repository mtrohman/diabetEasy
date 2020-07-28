package com.gulasehat.android.util;

import com.gulasehat.android.event.ProfileUpdatedEvent;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.model.MyUser;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

public class AuthUtil {


    public static AuthUser getUser(){
        if(isLoggedIn()){
             Gson gson = new Gson();
             AuthUser authUser = gson.fromJson(Preferences.getString(Preferences.LOGGED_IN_USER, ""), AuthUser.class);

             if(authUser != null){
                 return authUser;
             }
        }

        return null;
    }

    public static void setUser(AuthUser authUser){
        Gson gson = new Gson();
        Preferences.setString(Preferences.LOGGED_IN_USER, gson.toJson(authUser));
        EventBus.getDefault().post(new ProfileUpdatedEvent(authUser));
    }

    public static void setUser(MyUser user){
        setUser(new AuthUser(user));
    }



    public static boolean isLoggedIn(){
        if(Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
            return true;
        }
        return false;
    }

    public static void setToken(String token) {
        Preferences.setString(Preferences.TOKEN, token);
    }

    public static void logout(){
        Preferences.remove(Preferences.IS_LOGGED_IN);
        Preferences.remove(Preferences.LOGGED_IN_USER);
        Preferences.remove(Preferences.TOKEN);
    }

    public static void flush(){
        Preferences.clearAll();
    }
}
