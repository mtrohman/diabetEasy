package com.gulasehat.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.Month;
import com.gulasehat.android.model.Year;
import com.gulasehat.android.util.DateUtil;
import com.gulasehat.android.util.Resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.ArchiveViewHolder> {

    private Context context;
    private ArrayList<Year> years = new ArrayList<>();
    private ArrayList<Month> months = new ArrayList<>();

    public static final int TYPE_VIEW_LIST = 10;
    public static final int TYPE_VIEW_GRID = 20;

    private static final int MODE_YEAR_LIST = 100;
    private static final int MODE_MONTH_LIST = 101;

    private int viewType = TYPE_VIEW_LIST;

    private int activeMode = MODE_YEAR_LIST;

    private OnArchiveClickListener onArchiveClickListener;

    public ArchiveRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;

        switch (viewType){
            case TYPE_VIEW_GRID: layout = R.layout.item_archive_grid; break;
            case TYPE_VIEW_LIST:
                default: layout = R.layout.item_archive_list;
        }

        return new ArchiveViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ArchiveViewHolder holder, final int position) {

        if(activeMode == MODE_YEAR_LIST){
            bindYear(holder, position);
        }

        if(activeMode == MODE_MONTH_LIST){
            bindMonth(holder, position);
        }

    }

    private void bindYear(ArchiveViewHolder holder, int position){

        final Year year = years.get(position);

        if(Calendar.getInstance().get(Calendar.YEAR) == year.getYear()){
            Drawable icon = ContextCompat.getDrawable(context, R.drawable.icon_date_small_gray).mutate();
            Resource.colorizeByTheme(icon);
            holder.icon.setImageDrawable(icon);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.count.setBackgroundColor(ContextCompat.getColor(context, Resource.getColorPrimary()));
        }else{
            holder.icon.setImageResource(R.drawable.icon_date_small_gray);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            holder.count.setBackgroundColor(ContextCompat.getColor(context, R.color.badgeBgGray));
        }

        holder.title.setText(String.valueOf(year.getYear()));
        holder.count.setText(String.valueOf(year.getPostCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onArchiveClickListener != null){
                    setViewType(year.getMonths().getLayout());
                    setMonths(year.getMonths().getMonths());
                    onArchiveClickListener.onYearClick(year);
                }
            }
        });

    }

    private void bindMonth(ArchiveViewHolder holder, int position){

        final Month month = months.get(position);

//        Log.d("burkitest", Calendar.getInstance().get(Calendar.YEAR) +" - "+ month.getYear());
//        Log.d("burkitest", Calendar.getInstance().get(Calendar.MONTH) +" - " + month.getMonth());

        if(Calendar.getInstance().get(Calendar.YEAR) == month.getYear() &&
                Calendar.getInstance().get(Calendar.MONTH) == (month.getMonth() -1)){
            Drawable icon = ContextCompat.getDrawable(context, R.drawable.icon_date_small_gray).mutate();
            Resource.colorizeByTheme(icon);
            holder.icon.setImageDrawable(icon);
            holder.count.setBackgroundColor(ContextCompat.getColor(context, Resource.getColorPrimary()));
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.black));
        }else{
            holder.icon.setImageResource(R.drawable.icon_date_small_gray);
            holder.count.setBackgroundColor(ContextCompat.getColor(context, R.color.badgeBgGray));
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.textColor));
        }

        holder.title.setText(DateUtil.getMonthName(month.getMonth()));
        holder.count.setText(String.valueOf(month.getPostCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onArchiveClickListener != null){
                    onArchiveClickListener.onMonthClick(month);
                }
            }
        });

    }


    public void setYears(ArrayList<Year> years) {
        this.years = years;
        activeMode = MODE_YEAR_LIST;
        notifyDataSetChanged();
    }

    public void setMonths(ArrayList<Month> months) {
        this.months = months;
        activeMode = MODE_MONTH_LIST;
        notifyDataSetChanged();
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public int getItemCount() {

        if(activeMode == MODE_YEAR_LIST){
            return years.size();
        }

        if(activeMode == MODE_MONTH_LIST){
            return months.size();
        }

        return 0;
    }

    public void reverseContents(){

        if(activeMode == MODE_YEAR_LIST){
            Collections.reverse(years);
        }
        if(activeMode == MODE_MONTH_LIST){
            Collections.reverse(months);
        }
        notifyDataSetChanged();
    }

    static class ArchiveViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.count)
        TextView count;

        ArchiveViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnArchiveClickListener(OnArchiveClickListener onArchiveClickListener) {
        this.onArchiveClickListener = onArchiveClickListener;
    }

    public interface OnArchiveClickListener{
        void onYearClick(Year year);
        void onMonthClick(Month month);
    }


}
