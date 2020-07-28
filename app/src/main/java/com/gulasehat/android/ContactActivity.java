package com.gulasehat.android;

import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;

import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.widget.Alert;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactActivity extends BaseActivity {

    @BindView(R.id.name)
    protected EditText name;
    @BindView(R.id.email)
    protected EditText email;
    @BindView(R.id.message)
    protected EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Contact");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_contact);
        }

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            message.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        }else{
                            message.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @OnClick(R.id.send)
    public void onSendClick(View view){

        String name = this.name.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String message = this.message.getText().toString().trim();

        if(name.isEmpty() || email.isEmpty() || message.isEmpty()){
            Alert.make(this, R.string.all_required, Alert.ALERT_TYPE_WARNING);
            return;
        }

        if(! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Alert.make(this, R.string.invalid_email, Alert.ALERT_TYPE_WARNING);
            return;
        }

        CommonService.contact(name, email, message, new CommonService.OnContactSendListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                Alert.make(ContactActivity.this, response.getMessage(), Alert.ALERT_TYPE_SUCCESS, new Alert.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                });
            }

            @Override
            public void onFailed(ApiResponse response) {
                Alert.make(ContactActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_contact;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
