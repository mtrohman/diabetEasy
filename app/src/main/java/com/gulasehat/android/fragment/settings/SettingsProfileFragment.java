package com.gulasehat.android.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.EditProfileActivity;
import com.gulasehat.android.LoginActivity;
import com.gulasehat.android.MainActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.event.OnProfileSettingsFetchedEvent;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.CustomSwitch;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsProfileFragment extends BaseFragment {

    @BindView(R.id.email)
    protected CustomSwitch email;
    @BindView(R.id.websiteContainer)
    protected LinearLayout websiteContainer;
    @BindView(R.id.website)
    protected CustomSwitch website;
    @BindView(R.id.genderContainer)
    protected LinearLayout genderContainer;
    @BindView(R.id.gender)
    protected CustomSwitch gender;
    @BindView(R.id.jobContainer)
    protected LinearLayout jobContainer;
    @BindView(R.id.job)
    protected CustomSwitch job;
    @BindView(R.id.bioContainer)
    protected LinearLayout bioContainer;
    @BindView(R.id.bio)
    protected CustomSwitch bio;
    @BindView(R.id.loginContainer)
    protected LinearLayout loginContainer;
    @BindView(R.id.profileContainer)
    protected LinearLayout profileContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_profile, container, false);

        ButterKnife.bind(this, view);


        prepareUI();

        setListeners();


        return view;
    }

    private void prepareUI() {

        if(! AuthUtil.isLoggedIn()){

            profileContainer.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);

            return;
        }

        loginContainer.setVisibility(View.GONE);
        profileContainer.setVisibility(View.VISIBLE);

        if(Settings.getAppSettings().getAppUserProfile().hasWebsite()){
            websiteContainer.setVisibility(View.VISIBLE);
        }
        if(Settings.getAppSettings().getAppUserProfile().hasGender()){
            genderContainer.setVisibility(View.VISIBLE);
        }
        if(Settings.getAppSettings().getAppUserProfile().hasJob()){
            jobContainer.setVisibility(View.VISIBLE);
        }
        if(Settings.getAppSettings().getAppUserProfile().hasBio()){
            bioContainer.setVisibility(View.VISIBLE);
        }

    }

    private void setListeners() {
        email.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_PROFILE_EMAIL, val);
            }
        });
        website.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_PROFILE_WEBSITE, val);
            }
        });
        gender.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_PROFILE_GENDER, val);
            }
        });
        job.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_PROFILE_JOB, val);
            }
        });
        bio.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_PROFILE_BIO, val);
            }
        });
    }





    @OnClick(R.id.profile)
    public void onEditProfileClick(){
        startActivity(new Intent(getActivity(), EditProfileActivity.class));
    }

    @OnClick(R.id.logout)
    public void onLogoutClick(){
        AuthUtil.logout();
        if(Settings.getAppSettings().getAppMembershipStatus()){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnProfileSettingsFetched(OnProfileSettingsFetchedEvent event){

        boolean mEmail = Preferences.getBoolean(Preferences.SETTINGS_PROFILE_EMAIL, true);
        email.setValue(mEmail);

        boolean mWebsite = Preferences.getBoolean(Preferences.SETTINGS_PROFILE_WEBSITE, true);
        website.setValue(mWebsite);

        boolean mGender = Preferences.getBoolean(Preferences.SETTINGS_PROFILE_GENDER, true);
        gender.setValue(mGender);

        boolean mJob = Preferences.getBoolean(Preferences.SETTINGS_PROFILE_JOB, true);
        job.setValue(mJob);

        boolean mBio = Preferences.getBoolean(Preferences.SETTINGS_PROFILE_BIO, true);
        bio.setValue(mBio);

    }

}