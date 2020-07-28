package com.gulasehat.android.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gulasehat.android.model.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ErrorConverter {

    public static ApiResponse convert(@Nullable ResponseBody responseBody) {

        Gson gson = new Gson();

        try {
            if (responseBody != null) {
                //Log.d("buraktest", "test2: " + responseBody.string());
                ApiResponse resp =  gson.fromJson(responseBody.charStream(), ApiResponse.class);

                if(resp != null){
                    Log.d("buraktest", "null değil");
                    if(resp.getMessage().equals("unauthorized_request")){
                        resp.setMessage("login_required");
                    }
                    return resp;
                }

            }
        } catch (JsonSyntaxException e) {
            return new ApiResponse("server_error");
        }
        return new ApiResponse();
    }


    public static ApiResponse handle(final Context context, Throwable t) {

        final ApiResponse response = new ApiResponse();

        /// bu hata oluştu mesajının set edilmesi (default)
        response.setMessage("error");

        // Bu IO exception if
        if (t instanceof IOException) {

            ConnectionDetector connectionDetector = new ConnectionDetector(context);
            // interneti kontrol ediyoruz.
            if (!connectionDetector.isConnectingToInternet()) {
                response.setMessage("connection_error");
            } else {
                // internet ile ilgili değil başka bişey.
                response.setMessage("error");
            }

        }

        return response;

    }

}
