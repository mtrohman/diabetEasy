package com.gulasehat.android.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.SocialAccount;
import com.gulasehat.android.widget.TextAwesome;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SocialRecyclerAdapter extends RecyclerView.Adapter<SocialRecyclerAdapter.SocialViewHolder> {

    private List<SocialAccount> socialAccounts = new ArrayList<>();

    public static final int TYPE_VIEW_LIST = 10;
    public static final int TYPE_VIEW_GRID = 20;

    private int viewType = TYPE_VIEW_LIST;


    private OnSocialAccountClickListener onSocialAccountClickListener;


    @NonNull
    @Override
    public SocialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;

        switch (viewType) {
            case TYPE_VIEW_LIST:
                layout = R.layout.item_social_account_list;
                break;
            case TYPE_VIEW_GRID:
            default:
                layout = R.layout.item_social_account_grid;
        }

        return new SocialViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SocialViewHolder holder, final int position) {

        final SocialAccount socialAccount = socialAccounts.get(position);

        Drawable drawable = holder.iconContainer.getBackground().mutate();
        drawable.setColorFilter(Color.parseColor(socialAccount.getBgColor()), PorterDuff.Mode.SRC_ATOP);


        holder.name.setText(socialAccount.getName());
        holder.iconFontAwesome.setStyle(TextAwesome.FA_BRANDS);
        holder.iconFontAwesome.setText(Html.fromHtml(socialAccount.getIcon()));
        holder.iconFontAwesome.setTextColor(Color.parseColor(socialAccount.getFontColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSocialAccountClickListener != null) {
                    onSocialAccountClickListener.onClicked(socialAccount);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public int getItemCount() {
        return socialAccounts.size();
    }

    public void setViewType(int viewType) {
        if (this.viewType != viewType) {
            this.viewType = viewType;
        }
    }

    public void setSocialAccounts(ArrayList<SocialAccount> socialAccounts) {
        this.socialAccounts = socialAccounts;
        notifyDataSetChanged();
    }


    static class SocialViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iconContainer)
        LinearLayout iconContainer;
        @BindView(R.id.iconFontAwesome)
        TextAwesome iconFontAwesome;
        @BindView(R.id.name)
        TextView name;

        SocialViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnSocialAccountClickListener(OnSocialAccountClickListener onSocialAccountClickListener) {
        this.onSocialAccountClickListener = onSocialAccountClickListener;
    }

    public interface OnSocialAccountClickListener {
        void onClicked(SocialAccount socialAccount);
    }

}
