package com.gulasehat.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bhargavms.dotloader.DotLoader;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.model.LocationData;
import com.gulasehat.android.model.RegisterAppData;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.Permission;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class SplashScreenActivity extends BaseActivity {

    @BindView(R.id.filter)
    protected ImageView filter;
    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.loader)
    protected DotLoader loader;

    private final int MULTIPLE_PERMISSIONS = 100;
    private Handler handler;
    private Runnable runnable;
    private RegisterAppData appData;

    int delayMillis = 2000;
    long startTime;

    private boolean registerCompleted = false;
    private boolean changePasswordRedirect = false;
    private Intent changePasswordIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initLanguage();
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        handleIntent(intent);

        filter.setImageResource(Resource.getDrawableResID("splash_filter_" + Resource.getAppTheme().toLowerCase(Locale.ENGLISH)));
        //filter.setImageResource(Resource.getDrawableResID("splash_filter_lime"));

        String logoPath = BuildConfig.BASEURL + "wp-content/plugins/flink/assets/img/app/splash-screen-logo.png?v=" + System.currentTimeMillis();


        GlideApp.with(this).load(logoPath).into(logo);

        Animation alpha = AnimationUtils.loadAnimation(getApplication(), R.anim.alpha);

        logo.startAnimation(alpha);


        startTime = System.currentTimeMillis();


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                redirectMain();
            }
        };

        handler.postDelayed(runnable,delayMillis);

        registerAppData();


    }

    private void registerAppData() {

        appData  = prepareAppData();
        sendAppData();

    }

    private void sendAppData(){




        AuthService.initialize(appData, new AuthService.OnRegisterAppCompleteListener() {
            @Override
            public void onSuccess(AppSettings settings) {

                Preferences.setString(Preferences.REGISTER_ID, settings.getRegisterID());
                Preferences.setString(Preferences.THEME_DEFAULT, settings.getAppTheme());
                Preferences.setBoolean(Preferences.MEMBERSHIP, settings.getAppMembershipStatus());

                com.gulasehat.android.util.Settings.setAppSettings(settings);

                String[] languages = getResources().getStringArray(R.array.language_codes);
                for (int i= 0; i < languages.length; i++) {

                    if(settings.getAppDefaultLang().equals(languages[i])){
                        Preferences.setString(Preferences.LANGUAGE_DEFAULT, languages[i]);
                    }
                }




                Log.d("burakx1", "register tamamlandı");



                FlinkApplication.initializeInterstitialAds();

                FlinkApplication.initializeNativeAds();

                initLanguage();

                if(Settings.getAppSettings().getLocationPermissionStatus()){
                    Log.d("burakloc", "Location status aktif");
                    initLocationRequest();
                }else{
                    Log.d("burakloc", "Location status deaktif, devam et");
                    registerCompleted = true;
                    redirectMain();
                }

            }

            @Override
            public void onFailed(ApiResponse response) {
                Alert.make(SplashScreenActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING, new Alert.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                });
            }
        });

    }

    private boolean canRedirectMain(){

        if(!registerCompleted){
            return false;
        }

        if(changePasswordRedirect && changePasswordIntent == null){
            return false;
        }

        long elapsedTime = System.currentTimeMillis()-startTime;

        return elapsedTime >= delayMillis;
    }

    private void redirectMain(){

        if(canRedirectMain()){

            if(changePasswordRedirect){
                Log.d("buraks", "redirect etmesi lazım");

                startActivity(changePasswordIntent);
                return;
            }

            Log.d("burakx1", "canRedirectMain true");

            AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0f);
            animation1.setDuration(200);
            animation1.setFillAfter(true);
            loader.startAnimation(animation1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if(Preferences.getBoolean(Preferences.MEMBERSHIP, false)){
                        if(Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(SplashScreenActivity.this, StartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }else{
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    finish();

                }
            }, 200);


        }else {
            Log.d("burakx1", "canRedirectMain false");
        }


    }

    private void initLocationRequest(){

        if (!Permission.hasPermission(this, Permission.ACCESS_FINE_LOCATION)) {
            Permission.requestPermissions(this, new String[]{Permission.ACCESS_FINE_LOCATION}, MULTIPLE_PERMISSIONS);
            Log.d("burakloc", "Location permission yok");

            return;
        }

        prepareLocationData();

    }

    @SuppressLint("MissingPermission")
    private void prepareLocationData() {


        Log.d("burakloc", "Location topla");

        final LocationData locationData = new LocationData();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = null;
        if (locationManager != null) {
             location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (location != null) {

            locationData.setLocationLat(location.getLatitude());
            locationData.setLocationLng(location.getLongitude());

            if(Preferences.getString(Preferences.LAST_LATITUDE, "").equals(String.valueOf(location.getLatitude())) && Preferences.getString(Preferences.LAST_LONGITUDE, "").equals(String.valueOf(location.getLongitude()))){
                Log.d("burakloc", "Lokasyon aynı olduğundan istek gönderilmiyor");
                registerCompleted = true;
                redirectMain();
                return;
            }

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addresses.size() > 0) {
                    locationData.setLocationCountry(addresses.get(0).getCountryName());
                    locationData.setLocationCity(addresses.get(0).getAdminArea());
                    locationData.setLocationDistrict(addresses.get(0).getSubAdminArea());

                    AuthService.updateLocationData(locationData, new AuthService.OnCompletedListener() {
                        @Override
                        public void onCompleted() {
                            Preferences.setString(Preferences.LAST_LATITUDE, String.valueOf(locationData.getLocationLat()));
                            Preferences.setString(Preferences.LAST_LONGITUDE, String.valueOf(locationData.getLocationLng()));
                            registerCompleted = true;
                            redirectMain();
                        }
                    });
                }else{
                    Log.d("burakloc", "adres yok");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("burakloc", Objects.requireNonNull(e.getMessage()));

                registerCompleted = true;
                redirectMain();

            }
        } else {
            Log.d("burakloc", "location null");
            registerCompleted = true;
            redirectMain();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                if(Permission.hasPermission(this, Permission.ACCESS_FINE_LOCATION)){
                    initLocationRequest();
                    return;
                }
            }
            registerCompleted = true;
            redirectMain();
        }
    }


    private RegisterAppData prepareAppData() {

        RegisterAppData data = new RegisterAppData();

        // set app version
        data.setAppVersionName(BuildConfig.VERSION_NAME);
        data.setAppVersionCode(BuildConfig.VERSION_CODE);

        // set os version
        data.setOsVersion(Build.VERSION.RELEASE);

        // set manufacturer
        data.setManufacturer(Build.MANUFACTURER);

        // set model
        data.setModel(Build.MODEL);

        // set language
        data.setAppLanguage(Resources.getSystem().getConfiguration().locale.getLanguage());
        data.setAppUserLanguage(Preferences.getString(Preferences.SETTINGS_APP_LANG, ""));

        data.setCountryCode(Preferences.getString(Preferences.COUNTRY_CODE, ""));

        return data;

    }

    protected void handleIntent(Intent intent) {

        try {

            String action = intent.getAction();
            String data = intent.getDataString();

            if (Intent.ACTION_VIEW.equals(action) && data != null) {

                changePasswordRedirect = true;

                Pattern setPasswordPattern = Pattern.compile("wp-login.php\\?action=rp&key=([0-9a-zA-Z]+)&login=([0-9a-zA-Z]+)");
                Matcher setPasswordMatches = setPasswordPattern.matcher(data);

                if (setPasswordMatches.find()) {
                    changePasswordIntent = new Intent(this, PasswordChangeActivity.class);
                    changePasswordIntent.putExtra("key", setPasswordMatches.group(1));
                    changePasswordIntent.putExtra("login", setPasswordMatches.group(2));
                }else{
                    changePasswordRedirect = false;
                }

                redirectMain();

            }
        } catch (Exception e) {
        }
    }

    public void initLanguage(){


        String userDefinedLang = Preferences.getString(Preferences.SETTINGS_APP_LANG, "");
        String appDefinedLang  = Preferences.getString(Preferences.LANGUAGE_DEFAULT, "");

        Log.d("burakv", "User Defined Lang: " + userDefinedLang);
        Log.d("burakv", "App Defined Lang: " + appDefinedLang);


        // Kullanıcı tanımlaması varsa
        if(!userDefinedLang.equals("")){
            setLanguage(userDefinedLang);
        }

        // Override varsa
        else if(com.gulasehat.android.util.Settings.getAppSettings() != null && com.gulasehat.android.util.Settings.getAppSettings().isOverrideLanguage()){
            if(!appDefinedLang.equals("")){
                setLanguage(appDefinedLang);
            }
        }

        // Yoksa telefon dili
        Log.d("burakv", "Current App Lang: " + getResources().getConfiguration().locale.getLanguage().substring(0,2));
        Preferences.setString(Preferences.CURRENT_APP_LANG_CODE, getResources().getConfiguration().locale.getLanguage().substring(0,2));



    }





    @Override
    protected int getContentView() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
