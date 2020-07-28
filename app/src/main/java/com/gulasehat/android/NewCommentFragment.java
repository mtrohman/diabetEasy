package com.gulasehat.android;


import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.gulasehat.android.event.OnCommentSendEvent;
import com.gulasehat.android.event.OnCommentSentEvent;
import com.gulasehat.android.event.OnNewCommentVisibleEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.User;
import com.gulasehat.android.service.CommentService;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.KeyboardUtils;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.widget.Alert;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewCommentFragment extends BaseFragment {

    @BindView(R.id.name)
    protected EditText name;
    @BindView(R.id.email)
    protected EditText email;
    @BindView(R.id.comment)
    protected EditText comment;

    private int postID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            postID = getArguments().getInt("id", 0);
        }else{
            postID = 0;
        }

        if(getActivity() != null){
            KeyboardVisibilityEvent.setEventListener(
                    getActivity(),
                    new KeyboardVisibilityEventListener() {
                        @Override
                        public void onVisibilityChanged(boolean isOpen) {
                            if(isOpen){
                                comment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));

                            }else{
                                comment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
                            }
                        }
                    });
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_comment, container, false);

        ButterKnife.bind(this, view);

        User user = AuthUtil.getUser();

        if(user != null){
            name.setText(user.getUserFullName());
            name.setEnabled(false);
            email.setText(user.getUserEmail());
            email.setEnabled(false);
            //comment.requestFocus();
        }

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentSendEvent(OnCommentSendEvent event) {

        String name = this.name.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String comment = this.comment.getText().toString().trim();

        if(name.isEmpty() || email.isEmpty() || comment.isEmpty()){
            Alert.make(getContext(), R.string.all_required, Alert.ALERT_TYPE_WARNING);
            return;
        }

        if(! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Alert.make(getContext(), R.string.invalid_email, Alert.ALERT_TYPE_WARNING);
            return;
        }

        CommentService.sendComment(postID, name, email, comment, new CommentService.OnSendCommentListener() {
            @Override
            public void success(ApiResponse response) {


                Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_SUCCESS, new Alert.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        if(! AuthUtil.isLoggedIn()){
                            NewCommentFragment.this.name.setText("");
                            NewCommentFragment.this.email.setText("");
                        }
                        NewCommentFragment.this.comment.setText("");

                        EventBus.getDefault().post(new OnCommentSentEvent());
                    }
                });


            }

            @Override
            public void failed(ApiResponse response) {
                Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewCommentVisible(OnNewCommentVisibleEvent event){

        if(event != null && getContext() != null){
            if(event.isVisible()){
                if(Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
                    comment.requestFocus();
                    KeyboardUtils.showKeyboard(getContext(), comment);

                }else{
                    name.requestFocus();
                    KeyboardUtils.showKeyboard(getContext(), name);

                }
            }else{
                if(name.isFocused()){
                    KeyboardUtils.hideKeyboard(getContext(), name);
                }else if(email.isFocused()){
                    KeyboardUtils.hideKeyboard(getContext(), email);
                }else{
                    KeyboardUtils.hideKeyboard(getContext(), comment);
                }
            }
        }
    }


}
