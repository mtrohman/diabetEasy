package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.Notification;
import com.gulasehat.android.util.Resource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notifications = new ArrayList<>();


    private OnNotificationClickListener onNotificationClickListener;

    public NotificationRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, final int position) {

        final Notification notification = notifications.get(position);

        Resource.colorizeByTheme(holder.iconContainer.getBackground());
        holder.title.setText(notification.getTitle());
        holder.content.setText(notification.getContent());
        holder.date.setText(notification.getTimeFormatted());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNotificationClickListener != null){
                    onNotificationClickListener.onClicked(notification);
                }
            }
        });

    }




    // Layouta burada karar vereceğiz
    @Override
    public int getItemViewType(int position) {


        return R.layout.item_notification;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iconContainer)
        LinearLayout iconContainer;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.date)
        TextView date;

        NotificationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
    }

    public interface OnNotificationClickListener{
        void onClicked(Notification notification);
    }

}
