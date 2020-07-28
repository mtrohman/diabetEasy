package com.gulasehat.android.rest;


import com.gulasehat.android.BuildConfig;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.model.ArchiveData;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.model.AuthorArgs;
import com.gulasehat.android.model.AuthorData;
import com.gulasehat.android.model.CategoryArgs;
import com.gulasehat.android.model.CategoryData;
import com.gulasehat.android.model.CommentArgs;
import com.gulasehat.android.model.Comments;
import com.gulasehat.android.model.LocationData;
import com.gulasehat.android.model.MyUser;
import com.gulasehat.android.model.Notification;
import com.gulasehat.android.model.PageData;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.PostArgs;
import com.gulasehat.android.model.PostData;
import com.gulasehat.android.model.ProfileSettings;
import com.gulasehat.android.model.Question;
import com.gulasehat.android.model.RegisterAppData;
import com.gulasehat.android.model.SearchArgs;
import com.gulasehat.android.model.SearchResult;
import com.gulasehat.android.model.SocialAccountData;
import com.gulasehat.android.model.TagArgs;
import com.gulasehat.android.model.TagData;
import com.gulasehat.android.model.User;
import com.gulasehat.android.util.Preferences;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class RestClient {

    private static CallApiInterface callApiInterface ;

    public static Retrofit retrofit(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(10, TimeUnit.MINUTES);
        httpClient.writeTimeout(10, TimeUnit.MINUTES);
        httpClient.connectTimeout(10, TimeUnit.MINUTES);

        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();

                String registerID = Preferences.getString(Preferences.REGISTER_ID, "");
                String deviceID   = Preferences.getString(Preferences.DEVICE_ID, "");
                String cookieHash = Preferences.getString(Preferences.TOKEN, "");

                //Log.d("regid", registerID);

                Request request = original.newBuilder()
                        .header("User-Agent", BuildConfig.APPLICATION_ID)
                        .header("REGISTERID", registerID)
                        .header("DEVICEID", deviceID)
                        .header("VERSION_NAME", BuildConfig.VERSION_NAME)
                        .header("VERSION_CODE", String.valueOf(BuildConfig.VERSION_CODE))
                        .header("COOKIEHASH", cookieHash)
                        .method(original.method(), original.body())
                        .build();


                return chain.proceed(request);
            }
        });

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL + BuildConfig.APIPATH + "/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(httpClient.build())

                .build();
    }

    public static CallApiInterface getClient() {

        if (callApiInterface == null) {

            Retrofit client = RestClient.retrofit();

            callApiInterface = client.create(CallApiInterface.class);


        }
        return callApiInterface ;
    }

    public interface CallApiInterface {

        @FormUrlEncoded
        @POST("posts")
        Call<PostData> getPosts(@Field("sort_type") String sortType, @Field("page") int page);

        @POST("posts")
        Call<PostData> posts(@Body PostArgs args);

        @FormUrlEncoded
        @POST("pages")
        Call<PageData> getPages(@Field("sort_type") String sortType, @Field("page") int page);

        @POST("categories")
        Call<CategoryData> getCategories(@Body CategoryArgs args);

        @FormUrlEncoded
        @POST("archives")
        Call<ArchiveData> getArchives(@Field("sort_type") String sortType);


        @FormUrlEncoded
        @POST("favorite/add")
        Call<Void> addFavorite(@Field("post_id") int postID);

        @FormUrlEncoded
        @POST("favorite/delete")
        Call<Void> removeFavorite(@Field("post_id") int postID);

        @POST("favorite/delete-all")
        Call<ApiResponse> deleteAllFavorites();

        @FormUrlEncoded
        @POST("user/login")
        Call<AuthUser> login(@Field("username_email") String user, @Field("password") String pass);

        @POST("authors")
        Call<AuthorData> getAuthors(@Body AuthorArgs args);

        @POST("social-accounts")
        Call<SocialAccountData> getSocialAccounts();

        @POST("notifications")
        Call<ArrayList<Notification>> getNotifications();

        @FormUrlEncoded
        @POST("user/update-profile")
        Call<ApiResponse> updateProfile(@Field("key") String key, @Field("value") String value);

        @FormUrlEncoded
        @POST("user/change-password")
        Call<ApiResponse> changePassword(@Field("old_password") String oldPassword, @Field("new_password") String newPassword);

        @POST("user/remove-avatar")
        Call<ApiResponse> removeAvatar();

        @FormUrlEncoded
        @POST("user/register")
        Call<ApiResponse> register(@Field("username") String username, @Field("name") String name, @Field("email") String email);

        @FormUrlEncoded
        @POST("user/reset-password")
        Call<ApiResponse> resetPassword(@Field("user_login") String username);

        @FormUrlEncoded
        @POST("user/check-password-link")
        Call<ApiResponse> checkPasswordLink(@Field("hash") String hash, @Field("username") String user);

        @FormUrlEncoded
        @POST("user/set-password")
        Call<ApiResponse> setPassword(@Field("hash") String hash, @Field("username") String user, @Field("password") String password);


        @FormUrlEncoded
        @POST("contact")
        Call<ApiResponse> contact(@Field("name") String name, @Field("email") String email, @Field("message") String message);

        @FormUrlEncoded
        @POST("user/profile")
        Call<User> getUser(@Field("user_id") int userID);

        @FormUrlEncoded
        @POST("comment/add")
        Call<ApiResponse> sendComment(@Field("comment_post_id") int postID, @Field("comment_author") String name, @Field("comment_author_email") String email, @Field("comment_content") String comment);

        @POST("comments")
        Call<Comments> getComments(@Body CommentArgs args);

        @FormUrlEncoded
        @POST("comment/rate")
        Call<ApiResponse> rateComment(@Field("comment_id") int commentID, @Field("rate_type") int rateType);

        @Multipart
        @POST("user/upload-avatar")
        Call<ApiResponse> uploadAvatar(@Part MultipartBody.Part file);

        @FormUrlEncoded
        @POST("post")
        Call<Post> post(@Field("post_id") int id);

        @POST("tags")
        Call<TagData> getTagClouds(@Body TagArgs args);

        @POST("user/update-profile-settings")
        Call<ApiResponse> updateProfileSettings(@Body ProfileSettings settings);

        @POST("user/get-profile-settings")
        Call<ProfileSettings> profileSettings();

        @GET("faq")
        Call<ArrayList<Question>> getQuestions();

        @FormUrlEncoded
        @POST("post/check-password")
        Call<Post> checkPassword(@Field("post_id") int postID, @Field("password") String password);

        @POST("search")
        Call<SearchResult> search(@Body SearchArgs args);

        @POST("initialize")
        Call<AppSettings> initializeApp(@Body RegisterAppData registerRequest);

        @FormUrlEncoded
        @POST("user/update-app-theme")
        Call<ApiResponse> updateAppTheme(@Field("theme") String theme);

        @FormUrlEncoded
        @POST("add-push-token")
        Call<ApiResponse> sendPushToken(@Field("push_token") String token);

        @FormUrlEncoded
        @POST("favorite")
        Call<ApiResponse> favorite(@Field("post_id") int postId, @Field("fav") boolean fav);

        @FormUrlEncoded
        @POST("user/send-password-identification-link")
        Call<ApiResponse> sendMailAgain(@Field("user_hash") String hash);

        @FormUrlEncoded
        @POST("user/update-gravatar-status")
        Call<ApiResponse> updateGravatarStatus(@Field("gravatar_status") boolean status);

        @POST("user/update-location-data")
        Call<ApiResponse> updateLocationData(@Body LocationData locationData);

        @POST("user/info")
        Call<MyUser> getUserInfo();



    }



}