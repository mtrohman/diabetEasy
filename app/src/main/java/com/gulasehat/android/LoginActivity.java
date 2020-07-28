package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity{

    @BindView(R.id.showPassword)
    protected ImageButton showPassword;
    @BindView(R.id.userInputLayout)
    protected TextInputLayout userInputLayout;
    @BindView(R.id.user)
    protected EditText user;
    @BindView(R.id.passwordInputLayout)
    protected TextInputLayout passwordInputLayout;
    @BindView(R.id.password)
    protected EditText password;
    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.footer)
    protected LinearLayout footer;
    @BindView(R.id.register)
    protected Button register;

    private boolean passVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Login");


        AppSettings settings = Settings.getAppSettings();
        if(settings != null){
            GlideApp.with(this).load(settings.getAppStartPage().getLogo()).into(logo);
        }

        register.setText(Html.fromHtml(getString(R.string.register_text)));


        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            logo.setVisibility(View.GONE);
                            footer.setVisibility(View.GONE);
                        }else{
                            logo.setVisibility(View.VISIBLE);
                            footer.setVisibility(View.VISIBLE);
                        }
                    }
                });

        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.input_bottom_border_activate);
                }else{
                    v.setBackgroundResource(R.drawable.input_bottom_border);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        return R.layout.activity_login;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }


    @OnClick(R.id.showPassword)
    public void onChangePasswordVisibility(View view){

        if(passVisible){
            showPassword.setImageResource(R.drawable.icon_show_white);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passVisible = false;
        }else{
            showPassword.setImageResource(R.drawable.icon_hide_white);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passVisible = true;
        }

        password.setSelection(password.getText().length());
    }


    @OnClick(R.id.reset)
    public void onPasswordResetClick(View view){
        startActivity(new Intent(this, PasswordResetActivity.class));
    }

    @OnClick(R.id.register)
    public void onRegisterClick(){
        startActivity(new Intent(this, RegisterActivity.class));
    }


    @OnClick(R.id.login)
    public void onLoginClick(){

        userInputLayout.setErrorEnabled(false);
        passwordInputLayout.setErrorEnabled(false);

        String mUser = user.getText().toString().trim();
        String mPass = password.getText().toString().trim();


        if(mUser.isEmpty()){
            userInputLayout.setErrorEnabled(true);
            userInputLayout.setError(getString(R.string.username_required));

            return;
        }

        if(mPass.isEmpty()){
            passwordInputLayout.setErrorEnabled(true);
            passwordInputLayout.setError(getString(R.string.password_required));

            return;
        }

        if(password.length() < 6){
            passwordInputLayout.setErrorEnabled(true);
            passwordInputLayout.setError(getString(R.string.pass_min_char));

            return;
        }

        showProgress();
        AuthService.login(mUser, mPass, new AuthService.OnLoginListener() {
            @Override
            public void onLoginSuccess(AuthUser authUser) {

                hideProgress();
                Gson gson = new Gson();

                Preferences.setString(Preferences.TOKEN, authUser.getUserCookieHash());
                Preferences.setBoolean(Preferences.IS_LOGGED_IN, true);
                Preferences.setString(Preferences.LOGGED_IN_USER, gson.toJson(authUser));

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onLoginFailed(ApiResponse response) {
                hideProgress();
                Alert.make(LoginActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });
    }

}
