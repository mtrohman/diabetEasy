package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.gulasehat.android.model.Comment;
import com.gulasehat.android.util.Constant;
import com.gulasehat.android.util.DateUtil;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final int LIMIT = 10;
    private final int RATE_TYPE_LIKE = 1;
    private final int RATE_TYPE_UNLIKE = 0;

    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();

    private OnCommentAuthorClickListener onCommentAuthorClickListener;
    private OnCommentRateClickListener onCommentRateClickListener;

    private List<WebView> webViews = new ArrayList<>();



    public CommentRecyclerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {

        final Comment comment = comments.get(position);

        holder.name.setText(comment.getCommentAuthor());
        holder.upCount.setText(String.valueOf(comment.getCommentLikeCount()));
        holder.downCount.setText(String.valueOf(comment.getCommentUnlikeCount()));

        if (!comment.getCommentAuthorAvatar().equals("")) {
            GlideApp.with(context).load(comment.getCommentAuthorAvatar()).apply(RequestOptions.circleCropTransform()).into(holder.photo);
        }

        if (!Settings.getAppSettings().getAppComment().isRateStatus()) {
            holder.footer.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }


        WebSettings webSettings = holder.comment.getSettings();
        webSettings.setStandardFontFamily("Roboto-Regular");

        String body = "<html><head><style>body{ padding:0; margin:0; color: #232140; font-size: 16px; line-height:20px; } a{ color: #" + context.getString(Resource.getColorPrimary()).substring(3, 9) + ";} ul{ padding: 0; margin: 0;} ul li{ line-height: 10px; margin-left:20px; list-style:disc; } body > div > :first-child { margin-top: 0px!important; } body > div > :last-child{ margin-bottom: 0px!important }</style></head><body><div>" + comment.getCommentContent() + "</div></body></html>";


        holder.comment.loadDataWithBaseURL("same://ur/l/tat/does/not/work", body, "text/html", "utf-8", null);


        holder.time.setText(DateUtil.getFormattedDate(context, comment.getCommentDate()));

        if (comment.isApproved()) {
            holder.pending.setVisibility(View.GONE);
            holder.upButton.setClickable(true);
            holder.upButton.setFocusable(true);
            holder.downButton.setClickable(true);
            holder.downButton.setFocusable(true);
            holder.commentContainer.setAlpha(1f);
        } else {
            holder.pending.setVisibility(View.VISIBLE);
            holder.upButton.setClickable(false);
            holder.upButton.setFocusable(false);
            holder.downButton.setClickable(false);
            holder.downButton.setFocusable(false);
            holder.commentContainer.setAlpha(0.5f);
        }


        if (comment.getCommentUserID() > 0) {
            holder.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentAuthorClickListener != null) {
                        onCommentAuthorClickListener.onCommentAuthorClick(comment);
                    }
                }
            });
        } else {
            holder.header.setOnClickListener(null);
        }

        holder.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!comment.isApproved()) {
                    return;
                }

                if(!Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
                    Alert.make(context, R.string.login_required, Alert.ALERT_TYPE_WARNING);
                    return;
                }

                if (comment.isRated()) {
                    Alert.make(context, R.string.already_rated, Alert.ALERT_TYPE_WARNING);
                    return;
                }


                comments.get(position).setCommentLikeCount(comments.get(position).getCommentLikeCount() + 1);
                holder.upCount.setText(String.valueOf(comment.getCommentLikeCount()));
                v.setClickable(false);
                v.setFocusable(false);
                comment.setRated(true);

                if (onCommentRateClickListener != null) {
                    onCommentRateClickListener.onRateClick(comment, Constant.RATE_TYPE_LIKE);
                }
            }
        });

        holder.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!comment.isApproved()) {
                    return;
                }

                if(!Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
                    Alert.make(context, R.string.login_required, Alert.ALERT_TYPE_WARNING);
                    return;
                }

                if (comment.isRated()) {
                    Alert.make(context, R.string.already_rated, Alert.ALERT_TYPE_WARNING);
                    return;
                }

                comments.get(position).setCommentUnlikeCount(comments.get(position).getCommentUnlikeCount() + 1);
                holder.downCount.setText(String.valueOf(comment.getCommentUnlikeCount()));
                v.setClickable(false);
                v.setFocusable(false);
                comment.setRated(true);

                if (onCommentRateClickListener != null) {
                    onCommentRateClickListener.onRateClick(comment, Constant.RATE_TYPE_UNLIKE);
                }
            }
        });


    }


    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void addComments(ArrayList<Comment> comments){

        this.comments.addAll(comments);
        notifyItemRangeInserted(getItemCount(), comments.size());


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.comment)
        WebView comment;
        @BindView(R.id.upButton)
        LinearLayout upButton;
        @BindView(R.id.downButton)
        LinearLayout downButton;
        @BindView(R.id.upCount)
        TextView upCount;
        @BindView(R.id.downCount)
        TextView downCount;
        @BindView(R.id.pending)
        TextView pending;
        @BindView(R.id.commentContainer)
        CardView commentContainer;
        @BindView(R.id.header)
        RelativeLayout header;
        @BindView(R.id.footer)
        LinearLayout footer;
        @BindView(R.id.divider)
        View divider;

        CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnCommentAuthorClickListener(OnCommentAuthorClickListener onCommentAuthorClickListener) {
        this.onCommentAuthorClickListener = onCommentAuthorClickListener;
    }

    public void setOnCommentRateClickListener(OnCommentRateClickListener onCommentRateClickListener) {
        this.onCommentRateClickListener = onCommentRateClickListener;
    }

    public interface OnCommentAuthorClickListener {
        void onCommentAuthorClick(Comment comment);
    }

    public interface OnCommentRateClickListener {
        void onRateClick(Comment comment, int rateType);
    }

}
