package com.gulasehat.android.service;

import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.model.LocationData;
import com.gulasehat.android.model.MyUser;
import com.gulasehat.android.model.ProfileSettings;
import com.gulasehat.android.model.RegisterAppData;
import com.gulasehat.android.rest.RestClient;
import com.gulasehat.android.util.ErrorConverter;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthService {

    public static void login(String user, String pass, final OnLoginListener onLoginListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<AuthUser> call = service.login(user, pass);
        call.enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onLoginListener.onLoginSuccess(response.body());
                    }
                }else{
                    onLoginListener.onLoginFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<AuthUser> call, Throwable t) {
                onLoginListener.onLoginFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }



    public static void update(String key, String value, final OnProfileUpdateListener onProfileUpdateListener){

        Log.d("burak", key + " : " + value);

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.updateProfile(key, value);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onProfileUpdateListener.onSuccess(response.body());

                    }
                }else{
                    onProfileUpdateListener.onFailed(ErrorConverter.convert(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onProfileUpdateListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void changePassword(String oldPassword, String newPassword, final OnPasswordChangeListener onPasswordChangeListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.changePassword(oldPassword, newPassword);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    if(response.body() != null){
                        onPasswordChangeListener.onSuccess(response.body());
                    }
                }else{
                    onPasswordChangeListener.onFailed(ErrorConverter.convert(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onPasswordChangeListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void uploadAvatar(Uri uri, final OnAvatarUploadListener onAvatarUploadListener){

        File file = null;
        try {
            file = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            return;
        }

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                .toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase());

        Log.d("burakist1", "type: " +mimeType);

        RequestBody reqFile = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);


        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.uploadAvatar(body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onAvatarUploadListener.onSuccess(response.body());
                } else {
                    onAvatarUploadListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("buraks1", t.getMessage());
                onAvatarUploadListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void removeAvatar(final OnAvatarRemovedListener onAvatarRemovedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.removeAvatar();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onAvatarRemovedListener.onSuccess(response.body());
                } else {
                    Log.d("buraks", "hata kod : " + response.code() + " - " + response.message());
                    onAvatarRemovedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onAvatarRemovedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void updateGravatarStatus(boolean status, final OnAvatarRemovedListener onAvatarRemovedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.updateGravatarStatus(status);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onAvatarRemovedListener.onSuccess(response.body());
                } else {
                    Log.d("buraks", "hata kod : " + response.code() + " - " + response.message());
                    onAvatarRemovedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onAvatarRemovedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }

    public static void register(String user, String name, String email, final OnRegisterListener onRegisterListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.register(user, name, email);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    onRegisterListener.onSuccess(response.body());
                }else{
                    onRegisterListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onRegisterListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });

    }


    public static void resetPassword(String username, final OnResetPasswordListener onResetPasswordListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.resetPassword(username);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    onResetPasswordListener.onSuccess(response.body());
                }else{
                    onResetPasswordListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onResetPasswordListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });


    }

    public static void updateProfileSettings(ProfileSettings settings, final OnProfileSettingsUpdateListener onProfileSettingsUpdateListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.updateProfileSettings(settings);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    onProfileSettingsUpdateListener.onSuccess(response.body());
                    Log.d("burakss", "başarılı");
                }else{
                    Log.d("burakss", "başarısız");
                    onProfileSettingsUpdateListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onProfileSettingsUpdateListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void getProfileSettings(final OnProfileSettingsFetchedListener onProfileSettingsFetchedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ProfileSettings> call = service.profileSettings();
        call.enqueue(new Callback<ProfileSettings>() {
            @Override
            public void onResponse(Call<ProfileSettings> call, Response<ProfileSettings> response) {
                if (response.isSuccessful()) {
                    Log.d("burakss", "başarılı");
                    onProfileSettingsFetchedListener.onSuccess(response.body());
                }else{
                    onProfileSettingsFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ProfileSettings> call, Throwable t) {
                onProfileSettingsFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void initialize(RegisterAppData data, final OnRegisterAppCompleteListener onRegisterAppCompleteListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<AppSettings> call = service.initializeApp(data);
        call.enqueue(new Callback<AppSettings>() {
            @Override
            public void onResponse(Call<AppSettings> call, Response<AppSettings> response) {
                if (response.isSuccessful()) {
                    Log.d("burakss", "başarılı");
                    onRegisterAppCompleteListener.onSuccess(response.body());
                }else{
                    Log.d("burkist", "hata:" + response.code());
                    onRegisterAppCompleteListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<AppSettings> call, Throwable t) {

                Log.d("burkist", "hata: " + t);
                Log.d("burkist", "hata: " + t.toString());
                onRegisterAppCompleteListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });


    }

    public static void checkPasswordLink(String key, String login, final OnCheckPasswordLinkListener onCheckPasswordLinkListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.checkPasswordLink(key, login);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onCheckPasswordLinkListener.onSuccess(response.body());
                }else{
                    onCheckPasswordLinkListener.OnFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onCheckPasswordLinkListener.OnFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });


    }

    public static void setPassword(String key, String login, String password, final OnSetPasswordListener onSetPasswordListener){
        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.setPassword(key, login, password);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    onSetPasswordListener.onSuccess(response.body());
                }else{
                    onSetPasswordListener.OnFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onSetPasswordListener.OnFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });
    }

    public static void getUserInfo(final OnMyUserInfoFetchedListener onMyUserInfoFetchedListener){

        RestClient.CallApiInterface service = RestClient.getClient();
        Call<MyUser> call = service.getUserInfo();
        call.enqueue(new Callback<MyUser>() {
            @Override
            public void onResponse(Call<MyUser> call, Response<MyUser> response) {

                if (response.isSuccessful()) {
                    onMyUserInfoFetchedListener.onSuccess(response.body());
                }else{
                    onMyUserInfoFetchedListener.onFailed(ErrorConverter.convert(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<MyUser> call, Throwable t) {
                onMyUserInfoFetchedListener.onFailed(ErrorConverter.handle(FlinkApplication.getContext(), t));
            }
        });


    }

    public static void updateLocationData(LocationData locationData, final OnCompletedListener onCompletedListener){


        RestClient.CallApiInterface service = RestClient.getClient();
        Call<ApiResponse> call = service.updateLocationData(locationData);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    Log.d("burakloc", "complete: başarılı");
                }else{
                    Log.d("burakloc", "complete: " + response.code());
                }

                onCompletedListener.onCompleted();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if(t != null && t.getMessage() != null){
                    Log.d("burakloc", t.getMessage());
                }else{
                    Log.d("burakloc", "nooo");
                }


                onCompletedListener.onCompleted();
            }
        });


    }

    public interface OnProfileSettingsUpdateListener{
        void onSuccess(ApiResponse apiResponse);
        void onFailed(ApiResponse apiResponse);
    }

    public interface OnCompletedListener{
        void onCompleted();
    }

    public interface OnAvatarRemovedListener{
        void onSuccess(ApiResponse response);
        void onFailed(ApiResponse response);
    }

    public interface OnRegisterListener{
        void onSuccess(ApiResponse apiResponse);
        void onFailed(ApiResponse apiResponse);
    }

    public interface OnLoginListener{
        void onLoginSuccess(AuthUser authUser);
        void onLoginFailed(ApiResponse response);
    }

    public interface OnResetPasswordListener{
        void onSuccess(ApiResponse apiResponse);
        void onFailed(ApiResponse apiResponse);
    }

    public interface OnProfileUpdateListener{
        void onSuccess(ApiResponse response);
        void onFailed(ApiResponse response);

    }

    public interface OnPasswordChangeListener{
        void onSuccess(ApiResponse response);
        void onFailed(ApiResponse response);
    }

    public interface OnAvatarUploadListener{
        void onSuccess(ApiResponse response);
        void onFailed(ApiResponse response);
    }

    public interface OnProfileSettingsFetchedListener{
        void onSuccess(ProfileSettings settings);
        void onFailed(ApiResponse response);
    }

    public interface OnRegisterAppCompleteListener{
        void onSuccess(AppSettings settings);
        void onFailed(ApiResponse response);
    }

    public interface OnCheckPasswordLinkListener{
        void onSuccess(ApiResponse response);
        void OnFailed(ApiResponse response);
    }

    public interface OnSetPasswordListener{
        void onSuccess(ApiResponse response);
        void OnFailed(ApiResponse response);
    }

    public interface OnMyUserInfoFetchedListener{
        void onSuccess(MyUser user);
        void onFailed(ApiResponse apiResponse);
    }


}
