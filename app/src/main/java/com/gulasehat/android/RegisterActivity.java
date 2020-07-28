package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.userInputLayout)
    protected TextInputLayout userInputLayout;
    @BindView(R.id.user)
    protected EditText mUser;

    @BindView(R.id.nameInputLayout)
    protected TextInputLayout nameInputLayout;
    @BindView(R.id.name)
    protected EditText mName;

    @BindView(R.id.emailInputLayout)
    protected TextInputLayout emailInputLayout;
    @BindView(R.id.email)
    protected EditText mEmail;
    @BindView(R.id.footer)
    protected LinearLayout footer;
    @BindView(R.id.form)
    protected RelativeLayout form;
    @BindView(R.id.success)
    protected RelativeLayout success;

    private String hash;

    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.login)
    protected Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Register");

        AppSettings settings = Settings.getAppSettings();
        if(settings != null){
            //Picasso.get().load(settings.getAppStartPage().getLogo()).into(logo);
            GlideApp.with(this).load(settings.getAppStartPage().getLogo()).into(logo);
        }

        login.setText(Html.fromHtml(getString(R.string.login_text)));

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

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.input_bottom_border_activate);
                }else{
                    v.setBackgroundResource(R.drawable.input_bottom_border);
                }
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        return R.layout.activity_register;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @OnClick(R.id.register)
    public void onRegisterButtonClick(View view){

        nameInputLayout.setErrorEnabled(false);
        userInputLayout.setErrorEnabled(false);
        emailInputLayout.setErrorEnabled(false);

        String name = mName.getText().toString().trim();
        String username = mUser.getText().toString().trim();
        String email = mEmail.getText().toString().trim();

        if(name.isEmpty()){
            nameInputLayout.setErrorEnabled(true);
            nameInputLayout.setError(getString(R.string.name_required));
            return;
        }

        if(username.isEmpty()){
            userInputLayout.setErrorEnabled(true);
            userInputLayout.setError(getString(R.string.username_required));
            return;
        }

        if(email.isEmpty()){
            emailInputLayout.setErrorEnabled(true);
            emailInputLayout.setError(getString(R.string.email_required));
            return;
        }

        if(! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInputLayout.setErrorEnabled(true);
            emailInputLayout.setError(getString(R.string.invalid_email));
            return;
        }

        showProgress();

        AuthService.register(username, name, email, new AuthService.OnRegisterListener() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                hideProgress();

                hash = apiResponse.getData().get("user_hash");

                form.setVisibility(View.GONE);
                success.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(ApiResponse response) {
                hideProgress();
                Alert.make(RegisterActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });


    }

    @OnClick(R.id.login)
    public void onLoginButtonClick(View view){
        startActivity(new Intent(this, LoginActivity.class));


    }


}
