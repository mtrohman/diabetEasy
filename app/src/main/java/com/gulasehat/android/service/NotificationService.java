package com.gulasehat.android.service;

import android.util.Log;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Notification;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService {

    public static void getNotifications(final OnNotificationFetchedListener onNotificationFetchedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ArrayList<Notification>> call = service.getNotifications();
        call.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onNotificationFetchedListener.onSuccess(response.body());
                    }
                }else{
                    onNotificationFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                Log.d("burak", "failure : " + t.getMessage());
                onNotificationFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public interface OnNotificationFetchedListener{
        void onSuccess(ArrayList<Notification> notifications);
        void onFailed(ApiResponse response);
    }

}
