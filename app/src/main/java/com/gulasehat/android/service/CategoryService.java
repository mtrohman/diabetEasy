package com.gulasehat.android.service;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.CategoryArgs;
import com.gulasehat.android.model.CategoryData;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryService {

    public static void getCategories(CategoryArgs args, final OnCategoryFetchedListener onCategoryFetchedListener){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<CategoryData> call = service.getCategories(args);
        call.enqueue(new Callback<CategoryData>() {
            @Override
            public void onResponse(Call<CategoryData> call, Response<CategoryData> response) {

                if (response.isSuccessful()) {
                    onCategoryFetchedListener.onSuccess(response.body());
                }else{
                    onCategoryFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<CategoryData> call, Throwable t) {
                onCategoryFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public interface OnCategoryFetchedListener{
        void onSuccess(CategoryData categoryData);
        void onFailed(ApiResponse response);
    }

}
