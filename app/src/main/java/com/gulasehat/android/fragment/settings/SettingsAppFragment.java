package com.gulasehat.android.fragment.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.BuildConfig;
import com.gulasehat.android.LanguageActivity;
import com.gulasehat.android.LoginActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.ThemesActivity;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.Constant;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.CustomPreference;
import com.gulasehat.android.widget.CustomSwitch;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsAppFragment extends BaseFragment {

    @BindView(R.id.settings_notify_status)
    protected CustomSwitch settingsNotifyStatus;
    @BindView(R.id.settings_notify_sound)
    protected CustomSwitch settingsNotifySound;
    @BindView(R.id.settings_notify_vibrate)
    protected CustomSwitch settingsNotifyVibrate;
    @BindView(R.id.theme)
    protected CustomPreference settingsTheme;
    @BindView(R.id.language)
    protected CustomPreference settingsLanguage;
    @BindView(R.id.version)
    protected CustomPreference version;

    private boolean isShown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_app, container, false);

        ButterKnife.bind(this, view);

        initialize();

        return view;
    }

    private void initialize() {

        settingsNotifyStatus.setValue(Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_STATUS, Constant.SETTINGS_NOTIFY_STATUS_DEFAULT));
        settingsNotifySound.setValue(Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_SOUND, Constant.SETTINGS_NOTIFY_SOUND_DEFAULT));
        settingsNotifyVibrate.setValue(Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_VIBRATE, Constant.SETTINGS_NOTIFY_VIBRATE_DEFAULT));

        settingsTheme.setValue(getString(Resource.getStringResID(Resource.getAppTheme())));

        settingsNotifyStatus.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_NOTIFY_STATUS, val);
            }
        });
        settingsNotifySound.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_NOTIFY_SOUND, val);
            }
        });
        settingsNotifyVibrate.setOnCheckedChangeListener(new CustomSwitch.OnCheckedChangeListener() {
            @Override
            public void onChanged(boolean val) {
                Preferences.setBoolean(Preferences.SETTINGS_NOTIFY_VIBRATE, val);
            }
        });

        settingsLanguage.setValue(Preferences.getString(Preferences.CURRENT_APP_LANG_NAME, ""));



        String[] languages = getResources().getStringArray(R.array.language_codes);
        if(languages.length < 2){
            settingsLanguage.setVisibility(View.GONE);
        }else{
            String[] languageCodes = getResources().getStringArray(R.array.language_codes);
            String[] languageNames = getResources().getStringArray(R.array.language_names);

            for (int i=0; i<languageCodes.length; i++){

                if(Preferences.getString(Preferences.CURRENT_APP_LANG_CODE, "").equals(languageCodes[i])){
                    settingsLanguage.setValue(languageNames[i]);
                }
            }
        }

        version.setValue(BuildConfig.VERSION_NAME);

    }


    @OnClick(R.id.language)
    public void onLanguages(){
        startActivity(new Intent(getActivity(), LanguageActivity.class));
    }

    @OnClick(R.id.theme)
    public void onThemes(){
        startActivity(new Intent(getActivity(), ThemesActivity.class));
    }

    @OnClick(R.id.flush)
    public void onFlushClick(){

        if(isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title(R.string.flush_app_title)
                .content(R.string.flush_app_question)
                .positiveText(R.string.yes)
                .positiveColorAttr(R.attr.colorPrimary)
                .negativeText(R.string.no)
                .negativeColorAttr(R.attr.colorPrimary)
                .icon(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.icon_flush_black)))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AuthUtil.flush();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();


    }

}