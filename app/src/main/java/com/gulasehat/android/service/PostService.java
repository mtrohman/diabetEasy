package com.gulasehat.android.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.PostArgs;
import com.gulasehat.android.model.PostData;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostService {

    public static void posts(final @NonNull OnPostFetchedListener onPostFetchedListener, @Nullable PostArgs args){


        RestClient.CallApiInterface service = RestClient.getClient();
        Call<PostData> call = service.posts(args);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                Log.d("buraks", "code : " + response.code());
                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onPostFetchedListener.onSuccess(response.body());
                    }
                }else{
                    onPostFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Log.d("buraks", t.getMessage());
                onPostFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }

        });
    }

    public static void post(int id, final OnPostListener onPostListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<Post> call = service.post(id);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onPostListener.onSuccess(response.body());
                    }
                }else{
                    Log.d("burki123", response.message());

                    onPostListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("burki123", t.getMessage());
                onPostListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void addFavorite(int postID){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<Void> call = service.addFavorite(postID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                    }
                }else{
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public static void favorite(int postId, boolean fav, final OnFavoriteChangeListener onFavoriteChangeListener){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.favorite(postId, fav);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Log.d("burkist123", "kod: " + response.code());

                if (response.isSuccessful()) {
                    onFavoriteChangeListener.onSuccess(response.body());
                }else{
                    onFavoriteChangeListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                onFavoriteChangeListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void removeFavorite(int postID){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<Void> call = service.removeFavorite(postID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Log.d("buraks", "Kaldırma başarılı");
                }else{
                    Log.d("buraks", "kaldırma başarısız");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("buraks", "kaldırma failure");
            }
        });
    }

    public static void deleteAllFavorites(final OnAllFavoriteDeletedListener onAllFavoriteDeletedListener){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.deleteAllFavorites();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onAllFavoriteDeletedListener.onSuccess(response.body());
                }else{
                    onAllFavoriteDeletedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onAllFavoriteDeletedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void checkPassword(int postID, String password, final OnPostListener onPostListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<Post> call = service.checkPassword(postID, password);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onPostListener.onSuccess(response.body());
                    }
                }else{
                    onPostListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                onPostListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public interface OnAllFavoriteDeletedListener{
        void onSuccess(ApiResponse response);
        void onFailed(ApiResponse response);
    }

    public interface OnPostFetchedListener{
        void onSuccess(PostData postData);
        void onFailed(ApiResponse response);
    }

    public interface OnPostListener{
        void onSuccess(Post post);
        void onFailed(ApiResponse response);
    }

    public interface OnFavoriteChangeListener{
        void onSuccess(ApiResponse response);
        void onFailed(ApiResponse response);
    }

}
