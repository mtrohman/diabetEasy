package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.Question;
import com.gulasehat.android.util.Resource;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqRecyclerAdapter extends RecyclerView.Adapter<FaqRecyclerAdapter.FaqViewHolder> {

    private Context context;
    private List<Question> questions = new ArrayList<>();
    private static final int UNSELECTED = -1;

    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;


    public FaqRecyclerAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new FaqViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqViewHolder holder, final int position) {

        holder.bind();

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    public class FaqViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener{
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.question)
        TextView question;
        @BindView(R.id.answer)
        WebView answer;
        @BindView(R.id.expandable)
        ExpandableLayout expandable;

        FaqViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            //expandable.setInterpolator(new OvershootInterpolator());
            expandable.setOnExpansionUpdateListener(this);

            itemView.setOnClickListener(this);

        }

        public void bind() {
            int position = getAdapterPosition();
            boolean isSelected = position == selectedItem;



            final Question q = questions.get(position);

            question.setText(q.getQuestion());

            WebSettings webSettings = answer.getSettings();
            webSettings.setStandardFontFamily("Roboto-Regular");

            String body = "<html><head><style>body{ padding:0; margin:0; color: rgba(35, 33, 64, 0.7); font-size: 14px; } a{ color: #" + context.getString(Resource.getColorPrimary()).substring(3, 9) + ";} ul{ padding: 0; margin: 0;} ul li{ line-height: 20px; margin-left:20px; list-style:disc; } body > :first-child { margin-top: 0px!important; } body > :last-child{ margin-bottom: 0px!important }</style></head><body>" + q.getAnswer() + "</body></html>";

            answer.loadData(body, "text/html", "UTF-8");



            question.setSelected(isSelected);
            expandable.setExpanded(isSelected, false);

        }

        @Override
        public void onClick(View v) {
            FaqViewHolder holder = (FaqViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.question.setSelected(false);
                holder.expandable.collapse();

                holder.question.setTextColor(ContextCompat.getColor(context, R.color.text));
                holder.icon.setImageResource(R.drawable.icon_add);
                holder.icon.setBackgroundResource(R.drawable.oval_gray);

            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                question.setSelected(true);
                expandable.expand();
                uiExpand();
                selectedItem = position;
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state == ExpandableLayout.State.EXPANDING) {
                //recyclerView.smoothScrollToPosition(getAdapterPosition());
            }

            if(state == ExpandableLayout.State.EXPANDED){
                //uiExpand();

            }

            if(state == ExpandableLayout.State.COLLAPSED){
                //uiCollapse();
            }
        }

        private void uiExpand(){
            // Açılıyor
            question.setTextColor(ContextCompat.getColor(context, Resource.getColorPrimary()));
            icon.setImageResource(R.drawable.icon_remove);
            icon.setBackgroundResource(R.drawable.colored_oval);
        }

        private void uiCollapse(){
            // Kapanıyor
            question.setTextColor(ContextCompat.getColor(context, R.color.text));
            icon.setImageResource(R.drawable.icon_add);
            icon.setBackgroundResource(R.drawable.oval_gray);
        }


    }


}
