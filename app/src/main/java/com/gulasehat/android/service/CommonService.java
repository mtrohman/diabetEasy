package com.gulasehat.android.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Question;
import com.gulasehat.android.model.SearchArgs;
import com.gulasehat.android.model.SearchResult;
import com.gulasehat.android.model.SocialAccountData;
import com.gulasehat.android.model.TagArgs;
import com.gulasehat.android.model.TagData;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonService {


    public static void getSocialAccounts(final @NonNull OnSocialAccountsFetchedListener onSocialAccountsFetchedListener){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<SocialAccountData> call = service.getSocialAccounts();
        call.enqueue(new Callback<SocialAccountData>() {
            @Override
            public void onResponse(Call<SocialAccountData> call, Response<SocialAccountData> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onSocialAccountsFetchedListener.onSuccess(response.body());
                    }
                }else{
                    onSocialAccountsFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<SocialAccountData> call, Throwable t) {
                onSocialAccountsFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void contact(String name, String email, String message, final OnContactSendListener onContactSendListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.contact(name, email, message);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onContactSendListener.onSuccess(response.body());
                }else{
                    onContactSendListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onContactSendListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void getTagClouds(TagArgs args, final OnTagCloudFetchedListener onTagCloudFetchedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<TagData> call = service.getTagClouds(args);
        call.enqueue(new Callback<TagData>() {
            @Override
            public void onResponse(Call<TagData> call, Response<TagData> response) {

                if (response.isSuccessful()) {
                    onTagCloudFetchedListener.onSuccess(response.body());
                }else{
                    Log.d("burkitest", "s1 : " +  ErrorConverter.convert(response.errorBody()).getCode());
                    onTagCloudFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TagData> call, Throwable t) {
                Log.d("burkitest", "s2 : " + t.getMessage());
                onTagCloudFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }


    public static void getQuestions(final OnQuestionsFetchedListener onQuestionsFetchedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ArrayList<Question>> call = service.getQuestions();
        call.enqueue(new Callback<ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {

                if (response.isSuccessful()) {
                    onQuestionsFetchedListener.onSuccess(response.body());
                }else{
                    onQuestionsFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                onQuestionsFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void search(SearchArgs args, final OnSearchResultListener onSearchResultListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<SearchResult> call = service.search(args);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                if (response.isSuccessful()) {
                    onSearchResultListener.onSuccess(response.body());
                }else{
                    onSearchResultListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onSearchResultListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void updateAppTheme(String theme){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.updateAppTheme(theme);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public static void sendPushToken(String token){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.sendPushToken(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    Log.d("buraks", "token başarılı");
                }else{
                    Log.d("buraks", "token hata oluştu. " + ErrorConverter.convert(response.errorBody()).getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("buraks", "token fail oluştu");
            }
        });
    }

    public interface OnContactSendListener{
        void onSuccess(ApiResponse apiResponse);
        void onFailed(ApiResponse apiResponse);
    }

    public interface OnSocialAccountsFetchedListener{
        void onSuccess(SocialAccountData socialAccountData);
        void onFailed(ApiResponse response);
    }

    public interface OnTagCloudFetchedListener{
        void onSuccess(TagData tagData);
        void onFailed(ApiResponse apiResponse);
    }

    public interface OnQuestionsFetchedListener{
        void onSuccess(ArrayList<Question> questions);
        void onFailed(ApiResponse response);
    }

    public interface OnSearchResultListener{
        void onSuccess(SearchResult result);
        void onFailed(ApiResponse response);
    }

}
