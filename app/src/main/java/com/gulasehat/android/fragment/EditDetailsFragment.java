package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.R;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.KeyboardUtils;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.gulasehat.android.widget.EditableProfileItem;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditDetailsFragment extends BaseFragment {

    @BindView(R.id.genderContainer)
    protected LinearLayout genderContainer;
    @BindView(R.id.authorGender)
    protected EditableProfileItem authorGender;
    @BindView(R.id.jobContainer)
    protected LinearLayout jobContainer;
    @BindView(R.id.authorJob)
    protected EditableProfileItem authorJob;
    @BindView(R.id.bioContainer)
    protected LinearLayout bioContainer;
    @BindView(R.id.authorBio)
    protected EditableProfileItem authorBio;

    private AuthUser user;
    private boolean isShown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = AuthUtil.getUser();

        View view = inflater.inflate(R.layout.fragment_edit_details, container, false);

        ButterKnife.bind(this, view);

        prepareUI();

        return view;
    }



    private void prepareUI() {

        if(Settings.getAppSettings().getAppUserProfile().hasGender()){

            genderContainer.setVisibility(View.VISIBLE);

            if(user.getUserGender().equals("female")){
                authorGender.setText(getString(R.string.female));
            }

            if(user.getUserGender().equals("male")){
                authorGender.setText(getString(R.string.male));
            }

            if (user.getUserGender().equals("")){
                authorGender.setText(getString(R.string.unspecified));
            }

        }

        if(Settings.getAppSettings().getAppUserProfile().hasJob()){

            jobContainer.setVisibility(View.VISIBLE);

            if(user.getUserJob().equals("")){
                authorJob.setText(getString(R.string.unspecified));
            }else{
                authorJob.setText(user.getUserJob());
            }

        }

        if(Settings.getAppSettings().getAppUserProfile().hasBio()){

            bioContainer.setVisibility(View.VISIBLE);

            if(user.getUserBiographicalInfo().equals("")){
                authorBio.setText(getString(R.string.unspecified));
            }else{
                authorBio.setText(user.getUserBiographicalInfo());
            }

        }

    }

    @OnClick(R.id.authorGender)
    public void onAuthorGenderClick(View view){

        if(isShown){
            return;
        }

        isShown = true;

        int selected;

        switch (user.getUserGender()){
            case "female":
                selected = 0;
                break;
            case "male":
                selected = 1;
                break;
                default:
                    selected = -1;
        }

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_gender_black)
                .title(R.string.edit_gender)
                .items(R.array.gender)
                .itemsCallbackSingleChoice(selected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        return true;
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(authorGenderCallback)
                .onNeutral(negativeCallback)
                .show();

    }

    @OnClick(R.id.authorJob)
    public void onAuthorJobClick(View view){

        if(isShown){
            return;
        }

        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_work_black)
                .title(R.string.edit_job)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.job_hint), user.getUserJob(), inputCallback)
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(authorJobCallback)
                .onNeutral(negativeCallback)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();

    }

    @OnClick(R.id.authorBio)
    public void onAuthorBioClick(View view){

        if(isShown){
            return;
        }

        isShown = true;

        final View v = getLayoutInflater().inflate(R.layout.dialog_bio, null);

        final EditText bio = v.findViewById(R.id.bio);
        bio.setText(user.getUserBiographicalInfo());
        bio.setSelection(user.getUserBiographicalInfo().length());

        final MaterialDialog dialog = new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_bio_black)
                .title(R.string.edit_bio)
                .customView(v, false)
                .autoDismiss(false)
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.save)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .onPositive(authorBioCallback)
                .onNeutral(negativeCallback)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        KeyboardUtils.showKeyboard(getContext(), bio);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .build();

        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.getCustomView() != null) {
                    dialog.getCustomView().requestFocus();
                }

            }
        }, 500);





    }

    private MaterialDialog.SingleButtonCallback authorGenderCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            final String gender;

            switch (dialog.getSelectedIndex()){
                case 0:
                    gender = "female";
                    break;
                case 1:
                    gender = "male";
                    break;
                    default:
                        gender = "";
            }


            AuthService.update("gender", gender, new AuthService.OnProfileUpdateListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    user.setUserGender(gender);

                    switch (dialog.getSelectedIndex()){
                        case 0:
                            authorGender.setText(getString(R.string.female));
                            break;
                        case 1:
                            authorGender.setText(getString(R.string.male));
                            break;
                        default:
                            authorGender.setText(getString(R.string.unspecified));
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

    private MaterialDialog.SingleButtonCallback authorJobCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            if(dialog.getInputEditText() == null){
                return;
            }

            final String job = dialog.getInputEditText().getText().toString().trim();

            AuthService.update("job", job, new AuthService.OnProfileUpdateListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    user.setUserJob(job);

                    if(job.isEmpty()){
                        authorJob.setText(getString(R.string.unspecified));
                    }else{
                        authorJob.setText(job);
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

    private MaterialDialog.SingleButtonCallback authorBioCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            View view = dialog.getView();
            EditText bioInput = view.findViewById(R.id.bio);

            final String bio = bioInput.getText().toString().trim();

            AuthService.update("biographical_info", bio, new AuthService.OnProfileUpdateListener() {
                @Override
                public void onSuccess(ApiResponse response) {

                    user.setUserBiographicalInfo(bio);

                    if(bio.isEmpty()){
                        authorBio.setText(getString(R.string.unspecified));
                    }else{
                        authorBio.setText(bio);
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

    private MaterialDialog.SingleButtonCallback negativeCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
        }
    };

    private MaterialDialog.InputCallback inputCallback = new MaterialDialog.InputCallback() {
        @Override
        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            dialog.getContentView().requestFocus();
        }
    };

}
