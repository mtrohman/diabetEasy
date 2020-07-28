package com.gulasehat.android.service;

import androidx.annotation.NonNull;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AuthorArgs;
import com.gulasehat.android.model.AuthorData;
import com.gulasehat.android.model.User;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorService {

    public static void getAuthors(@NonNull OnAuthorFetchedListener onAuthorFetchedListener){
        authorsRequest(onAuthorFetchedListener,new AuthorArgs());
    }

    public static void getAuthors(@NonNull OnAuthorFetchedListener onAuthorFetchedListener, AuthorArgs args){
        authorsRequest(onAuthorFetchedListener, args);
    }

    private static void authorsRequest(final @NonNull OnAuthorFetchedListener onAuthorFetchedListener, AuthorArgs args){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<AuthorData> call = service.getAuthors(args);
        call.enqueue(new Callback<AuthorData>() {
            @Override
            public void onResponse(Call<AuthorData> call, Response<AuthorData> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onAuthorFetchedListener.onSuccess(response.body());
                    }
                }else{
                    onAuthorFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<AuthorData> call, Throwable t) {
                onAuthorFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }


    public static void getAuthor(int authorID, final OnAuthorListener onAuthorListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<User> call = service.getUser(authorID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onAuthorListener.onSuccess(response.body());
                    }
                }else{
                    onAuthorListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onAuthorListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public interface OnAuthorFetchedListener{
        void onSuccess(AuthorData authorData);
        void onFailed(ApiResponse response);
    }

    public interface OnAuthorListener{
        void onSuccess(User user);
        void onFailed(ApiResponse response);
    }

}
