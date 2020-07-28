package com.gulasehat.android;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Report;
import com.gulasehat.android.util.Preferences;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class CrashActivity extends BaseActivity {

    @BindView(R.id.restart_button)
    protected Button restartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());


        if (config == null) {
            finish();
            return;
        }

        sendReport();

        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(CrashActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(CrashActivity.this, config);
                }
            });
        }
    }

    private void sendReport(){

        Report report = new Report();
        report.setAppId(BuildConfig.APPLICATION_ID);
        report.setAppName(getString(R.string.app_name));
        report.setApiLevel(Build.VERSION.SDK_INT);
        report.setApiUrl(BuildConfig.BASEURL);
        report.setAppLanguage(Resources.getSystem().getConfiguration().locale.getLanguage());
        report.setAppUserLanguage(Preferences.getString(Preferences.SETTINGS_APP_LANG, ""));
        report.setAppVersionCode(BuildConfig.VERSION_CODE);
        report.setAppVersionName(BuildConfig.VERSION_NAME);
        report.setManufacturer(Build.MANUFACTURER);
        report.setModel(Build.MODEL);
        report.setErrorLog(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent()));



        Retrofit client = retrofit();
        CallApiInterface service = client.create(CallApiInterface.class);
        Call<ApiResponse> call = service.sendReport(report);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
            }
        });

    }

    private static Retrofit retrofit(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(10, TimeUnit.MINUTES);
        httpClient.writeTimeout(10, TimeUnit.MINUTES);
        httpClient.connectTimeout(10, TimeUnit.MINUTES);

        return new Retrofit.Builder()
                .baseUrl("https://build.fyncode.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(httpClient.build())
                .build();

    }


    public interface CallApiInterface {
        @POST("report")
        Call<ApiResponse> sendReport(@Body Report report);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_crash;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

}
