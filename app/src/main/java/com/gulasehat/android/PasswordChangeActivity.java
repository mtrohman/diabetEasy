package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PasswordChangeActivity extends BaseActivity {

    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.passwordInputLayout)
    protected TextInputLayout passwordInputLayout;
    @BindView(R.id.password)
    protected EditText password;

    @BindView(R.id.new_password_text)
    protected TextView new_password_text;

    @BindView(R.id.passwordAgainInputLayout)
    protected TextInputLayout passwordAgainInputLayout;
    @BindView(R.id.passwordAgain)
    protected EditText passwordAgain;

    @BindView(R.id.showPassword1)
    protected ImageButton showPassword1;
    @BindView(R.id.showPassword2)
    protected ImageButton showPassword2;

    private boolean pass1Visible = false;
    private boolean pass2Visible = false;

    private String key;
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();

        if(!intent.hasExtra("key") || !intent.hasExtra("login")){
            finish();
            return;
        }

        key = intent.getStringExtra("key");
        login = intent.getStringExtra("login");

        checkPasswordLink();

        Analytics.logScreen("Change Password");

        AppSettings settings = Settings.getAppSettings();
        if(settings != null){
            //Picasso.get().load(settings.getAppStartPage().getLogo()).into(logo);
            GlideApp.with(this).load(settings.getAppStartPage().getLogo()).into(logo);
        }

        new_password_text.setText(Html.fromHtml(getString(R.string.new_password_text)));

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

        passwordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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



    private void checkPasswordLink() {
        AuthService.checkPasswordLink(key, login, new AuthService.OnCheckPasswordLinkListener() {
            @Override
            public void onSuccess(ApiResponse response) {

            }

            @Override
            public void OnFailed(ApiResponse response) {
                finishAffinity();
                Toast.makeText(PasswordChangeActivity.this, R.string.invalid_hash, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_password_change;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @OnClick(R.id.showPassword1)
    public void onShowPassword1(View view){

        if(pass1Visible){
            showPassword1.setImageResource(R.drawable.icon_show_white);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pass1Visible = false;
        }else{
            showPassword1.setImageResource(R.drawable.icon_hide_white);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pass1Visible = true;
        }

        password.setSelection(password.getText().length());
    }

    @OnClick(R.id.showPassword2)
    public void onShowPassword2(View view){

        if(pass2Visible){
            showPassword2.setImageResource(R.drawable.icon_show_white);
            passwordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pass2Visible = false;
        }else{
            showPassword2.setImageResource(R.drawable.icon_hide_white);
            passwordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pass2Visible = true;
        }

        passwordAgain.setSelection(passwordAgain.getText().length());
    }

    @OnClick(R.id.reset)
    public void onChangePasswordButtonClick(View view){

        passwordInputLayout.setErrorEnabled(false);
        passwordAgainInputLayout.setErrorEnabled(false);

        String pass1 = password.getText().toString().trim();
        String pass2 = passwordAgain.getText().toString().trim();

        if(pass1.isEmpty()){
            passwordInputLayout.setErrorEnabled(true);
            passwordInputLayout.setError(getString(R.string.new_password_required));
            return;
        }

        if(pass1.length() < 6){
            passwordInputLayout.setErrorEnabled(true);
            passwordInputLayout.setError(getString(R.string.pass_min_char));
            return;
        }

        if(pass2.isEmpty()){
            passwordAgainInputLayout.setErrorEnabled(true);
            passwordAgainInputLayout.setError(getString(R.string.new_password_again));
            return;
        }

        if(pass2.length() < 6){
            passwordAgainInputLayout.setErrorEnabled(true);
            passwordAgainInputLayout.setError(getString(R.string.pass_min_char));
            return;
        }

        if(!pass1.equals(pass2)){
            passwordAgainInputLayout.setErrorEnabled(true);
            passwordAgainInputLayout.setError(getString(R.string.not_match_passwords));
            return;
        }

        showProgress();

        AuthService.setPassword(key, login, pass1, new AuthService.OnSetPasswordListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                hideProgress();
                startActivity(new Intent(PasswordChangeActivity.this, LoginActivity.class));
            }

            @Override
            public void OnFailed(ApiResponse response) {
                hideProgress();
                Alert.make(PasswordChangeActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });


    }
}
