package com.gulasehat.android;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.google.android.material.textfield.TextInputLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordResetActivity extends BaseActivity {

    @BindView(R.id.userInputLayout)
    protected TextInputLayout userInputLayout;
    @BindView(R.id.user)
    protected EditText mUser;

    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.form)
    protected RelativeLayout form;
    @BindView(R.id.success)
    protected RelativeLayout success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Reset Password");

        AppSettings settings = Settings.getAppSettings();
        if(settings != null){
            GlideApp.with(this).load(settings.getAppStartPage().getLogo()).into(logo);
        }

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            logo.setVisibility(View.GONE);
                        }else{
                            logo.setVisibility(View.VISIBLE);
                        }
                    }
                });

        mUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.input_bottom_border_activate);
                }else{
                    v.setBackgroundResource(R.drawable.input_bottom_border);
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_password_reset;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @OnClick(R.id.reset)
    public void onResetPasswordButtonClick(View view){

        userInputLayout.setErrorEnabled(false);

        String username = mUser.getText().toString().trim();

        if(username.isEmpty()){
            userInputLayout.setErrorEnabled(true);
            userInputLayout.setError(getString(R.string.username_or_email_required));
            return;
        }

        showProgress();

        AuthService.resetPassword(username, new AuthService.OnResetPasswordListener() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                hideProgress();
                form.setVisibility(View.GONE);
                success.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(ApiResponse response) {
                hideProgress();
                Alert.make(PasswordResetActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }


}
