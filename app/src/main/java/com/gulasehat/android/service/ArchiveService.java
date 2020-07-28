package com.gulasehat.android.service;

import androidx.annotation.NonNull;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.ArchiveData;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArchiveService {

    public static void getArchives(@NonNull OnArchiveFetchedListener onArchiveFetchedListener){
        archivesRequest(onArchiveFetchedListener, "asc");
    }

    public static void getArchives(@NonNull OnArchiveFetchedListener onArchiveFetchedListener, String sortType){
        archivesRequest(onArchiveFetchedListener, sortType);
    }

    private static void archivesRequest(final @NonNull OnArchiveFetchedListener onArchiveFetchedListener, String sortType){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ArchiveData> call = service.getArchives(sortType);
        call.enqueue(new Callback<ArchiveData>() {
            @Override
            public void onResponse(Call<ArchiveData> call, Response<ArchiveData> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onArchiveFetchedListener.onSuccess(response.body());
                    }
                }else{
                    onArchiveFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ArchiveData> call, Throwable t) {
                onArchiveFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public interface OnArchiveFetchedListener{
        void onSuccess(ArchiveData archiveData);
        void onFailed(ApiResponse response);
    }

}
