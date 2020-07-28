package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.gulasehat.android.model.Author;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthorRecyclerAdapter extends RecyclerView.Adapter<AuthorRecyclerAdapter.AuthorViewHolder> {

    private Context context;
    private List<Author> authors = new ArrayList<>();

    public static final int TYPE_VIEW_LIST = 10;
    public static final int TYPE_VIEW_GRID = 20;

    private int viewType = TYPE_VIEW_LIST;

    private boolean postCountStatus = true;

    private OnAuthorClickListener onAuthorClickListener;

    public AuthorRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;

        switch (viewType){
            case TYPE_VIEW_GRID: layout = R.layout.item_author_grid; break;
            case TYPE_VIEW_LIST:
                default: layout = R.layout.item_author_list;
        }
        return new AuthorViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AuthorViewHolder holder, final int position) {

        final Author author = authors.get(position);

        holder.name.setText(author.getAuthorFullName());
        holder.user.setText(author.getAuthorName());

        if(postCountStatus){
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText(context.getString(R.string.post_count, author.getAuthorPostCount() + ""));
        }
        else{
            holder.count.setVisibility(View.GONE);
        }


        if(! author.getAuthorPicture().equals("")){
//            Picasso.get().load(author.getAuthorPicture()).transform(new CircleTransform()).into(holder.avatar);
            GlideApp.with(context).load(author.getAuthorPicture()).apply(RequestOptions.circleCropTransform()).into(holder.avatar);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAuthorClickListener != null){
                    onAuthorClickListener.onClicked(author);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setPostCountStatus(boolean postCountStatus) {
        this.postCountStatus = postCountStatus;
    }


    @Override
    public int getItemCount() {
        return authors.size();
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
        notifyDataSetChanged();
    }

    public void addAuthors(ArrayList<Author> authors) {
        this.authors.addAll(authors);
        notifyItemRangeInserted(getItemCount(), this.authors.size());
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.user) TextView user;
        @BindView(R.id.count) TextView count;

        AuthorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnAuthorClickListener(OnAuthorClickListener onAuthorClickListener) {
        this.onAuthorClickListener = onAuthorClickListener;
    }

    public interface OnAuthorClickListener{
        void onClicked(Author author);
    }

}
