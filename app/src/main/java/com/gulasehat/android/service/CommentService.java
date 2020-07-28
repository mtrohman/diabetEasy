package com.gulasehat.android.service;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.CommentArgs;
import com.gulasehat.android.model.Comments;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentService {

    public static void sendComment(int postID, String name, String email, String comment, final OnSendCommentListener onSendCommentListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.sendComment(postID, name, email, comment);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onSendCommentListener.success(response.body());
                }else{
                    onSendCommentListener.failed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onSendCommentListener.failed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void getComments(CommentArgs args, final OnCommentsFetchedListener onCommentsFetchedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<Comments> call = service.getComments(args);
        call.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {

                if (response.isSuccessful()) {
                    onCommentsFetchedListener.onSuccess(response.body());
                }else{
                    onCommentsFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                onCommentsFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void rateComment(int commentID, int rateType, final OnRateCompletedListener onRateCompletedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.rateComment(commentID, rateType);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onRateCompletedListener.onSuccess(response.body());
                }else{
                    onRateCompletedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onRateCompletedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public interface OnCommentsFetchedListener{
        void onSuccess(Comments comments);
        void onFailed(ApiResponse apiResponse);
    }

    public interface OnSendCommentListener{
        void success(ApiResponse response);
        void failed(ApiResponse response);
    }

    public interface OnRateCompletedListener{
        void onSuccess(ApiResponse apiResponse);
        void onFailed(ApiResponse apiResponse);
    }


}
