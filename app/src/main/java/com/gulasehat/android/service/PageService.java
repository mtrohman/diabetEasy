package com.gulasehat.android.service;


import androidx.annotation.NonNull;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.PageData;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageService {

    public static void getPages(@NonNull OnPageFetchedListener onPageFetchedListener, String filter, int page){
        getPagesRequest(onPageFetchedListener, filter, page);
    }

    public static void getPages(@NonNull OnPageFetchedListener onPageFetchedListener){
        getPagesRequest(onPageFetchedListener,"date_desc", 0);
    }

    private static void getPagesRequest(final @NonNull OnPageFetchedListener onPageFetchedListener, String filter, int page){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<PageData> call = service.getPages(filter, page);
        call.enqueue(new Callback<PageData>() {
            @Override
            public void onResponse(Call<PageData> call, Response<PageData> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onPageFetchedListener.onSuccess(response.body());
                    }
                }else{
                    onPageFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<PageData> call, Throwable t) {
                onPageFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public interface OnPageFetchedListener{
        void onSuccess(PageData pageData);
        void onFailed(ApiResponse response);
    }


}
