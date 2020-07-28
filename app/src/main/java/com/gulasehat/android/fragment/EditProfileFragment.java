package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.LoginActivity;
import com.gulasehat.android.MainActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.gulasehat.android.widget.EditableProfileItem;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditProfileFragment extends BaseFragment {

    @BindView(R.id.authorName)
    protected EditableProfileItem authorName;
    @BindView(R.id.authorEmail)
    protected EditableProfileItem authorEmail;
    @BindView(R.id.websiteContainer)
    protected LinearLayout websiteContainer;
    @BindView(R.id.authorWebsite)
    protected EditableProfileItem authorWebsite;
    @BindView(R.id.authorPassword)
    protected EditableProfileItem authorPassword;
    @BindView(R.id.logout)
    protected EditableProfileItem logout;

    private AuthUser user;
    private boolean isShown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = AuthUtil.getUser();

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ButterKnife.bind(this, view);

        prepareUI();

        return view;
    }


    private void prepareUI() {
        if (!user.getUserFullName().equals("")) {
            authorName.setText(user.getUserFullName());
        }
        if (!user.getUserEmail().equals("")) {
            authorEmail.setText(user.getUserEmail());
        }
        if (!user.getUserWebsite().equals("")) {
            authorWebsite.setText(user.getUserWebsite());
        }

        if(Settings.getAppSettings().getAppUserProfile().hasWebsite()){
            websiteContainer.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.authorName)
    public void onAuthorNameClick(View view){

        if(isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_person_black)
                .title(R.string.edit_name)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.name_hint), user.getUserFullName(),false, inputCallback)
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(authorNameCallback)
                .onNeutral(negativeCallback)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();
    }

    @OnClick(R.id.authorEmail)
    public void onAuthorEmailClick(View view){

        if(isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_mail_bordered_black)
                .title(R.string.edit_email)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input(getString(R.string.email_hint), user.getUserEmail(), false, inputCallback)
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(authorEmailCallback)
                .onNeutral(negativeCallback)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();

    }

    @OnClick(R.id.authorWebsite)
    public void onAuthorWebsiteClick(View view){

        if(isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_website_black)
                .title(R.string.edit_website)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.website_hint), user.getUserWebsite(), inputCallback)
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(authorWebsiteCallback)
                .onNeutral(negativeCallback)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();

    }

    @OnClick(R.id.authorPassword)
    public void onAuthorPasswordClick(){
        if(isShown){
            return;
        }
        isShown = true;
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_lock_black)
                .title(R.string.change_password)
                .customView(R.layout.dialog_change_password, false)
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(authorPasswordCallback)
                .onNeutral(negativeCallback)
                .tag("password")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();

    }

    @OnClick(R.id.logout)
    public void onLogoutClick(View view){
        AuthUtil.logout();
        if(Settings.getAppSettings().getAppMembershipStatus()){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private MaterialDialog.SingleButtonCallback authorNameCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            if(dialog.getInputEditText() == null){
                return;
            }

            final String name = dialog.getInputEditText().getText().toString().trim();

            AuthService.update("name", name, new AuthService.OnProfileUpdateListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    user.setUserFullName(name);
                    authorName.setText(name);
                    AuthUtil.setUser(user);
                    dialog.dismiss();

                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_SUCCESS);

                }

                @Override
                public void onFailed(ApiResponse response) {
                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
                }
            });
        }
    };

    private MaterialDialog.SingleButtonCallback authorEmailCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            if(dialog.getInputEditText() == null){
                return;
            }

            final String email = dialog.getInputEditText().getText().toString().trim();

            if(! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Alert.make(getContext(), R.string.invalid_email, Alert.ALERT_TYPE_WARNING);
                return;
            }

            AuthService.update("email", email, new AuthService.OnProfileUpdateListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    user.setUserEmail(email);
                    authorEmail.setText(email);
                    AuthUtil.setUser(user);
                    dialog.dismiss();

                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_SUCCESS);

                }

                @Override
                public void onFailed(ApiResponse response) {
                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
                }
            });
        }
    };

    private MaterialDialog.SingleButtonCallback authorWebsiteCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            if(dialog.getInputEditText() == null){
                return;
            }

            final String website = dialog.getInputEditText().getText().toString().trim();

            if(! website.isEmpty() && ! Patterns.WEB_URL.matcher(website).matches()){
                Alert.make(getContext(), R.string.invalid_url, Alert.ALERT_TYPE_WARNING);
                return;
            }

            AuthService.update("website", website, new AuthService.OnProfileUpdateListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    user.setUserWebsite(website);
                    if(website.isEmpty()){
                        authorWebsite.setText(getString(R.string.unspecified));
                    }else{
                        authorWebsite.setText(website);
                    }
                    AuthUtil.setUser(user);
                    dialog.dismiss();

                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_SUCCESS);

                }

                @Override
                public void onFailed(ApiResponse response) {
                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
                }
            });
        }
    };

    private MaterialDialog.SingleButtonCallback authorPasswordCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            View view = dialog.getView();
            EditText oldPasswordInput = view.findViewById(R.id.old_password);
            EditText newPasswordInput = view.findViewById(R.id.new_password);
            EditText newPasswordAgainInput = view.findViewById(R.id.new_password_again);

            String oldPassword = oldPasswordInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();
            String newPasswordAgain = newPasswordAgainInput.getText().toString();

            if(oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordAgain.isEmpty()){
                Alert.make(getContext(), R.string.all_required, Alert.ALERT_TYPE_WARNING);
                return;
            }

            if(oldPassword.length() < 6 || newPassword.length() < 6 || newPasswordAgain.length() < 6){
                Alert.make(getContext(), R.string.password_min_6_char, Alert.ALERT_TYPE_WARNING);
                return;
            }

            if(! newPassword.equals(newPasswordAgain)){
                Alert.make(getContext(), R.string.not_match_passwords, Alert.ALERT_TYPE_WARNING);
                return;
            }

            AuthService.changePassword(oldPassword, newPassword, new AuthService.OnPasswordChangeListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    AuthUtil.setToken(response.getData().get("cookie_hash"));
                    dialog.dismiss();

                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_SUCCESS);
                }

                @Override
                public void onFailed(ApiResponse response) {
                    Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
                }
            });
        }
    };



    private MaterialDialog.SingleButtonCallback negativeCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
        }
    };

    private MaterialDialog.InputCallback inputCallback = new MaterialDialog.InputCallback() {
        @Override
        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

        }
    };



}