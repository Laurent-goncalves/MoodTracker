package com.g.laurent.moodtracker.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.moodtracker.R;
import com.g.laurent.moodtracker.Views.ListViewHolder;
import butterknife.BindView;


public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private TextView comment_view;
    private FrameLayout mFrameLayout;
    private String[] texts_chrono;
    private ListViewHolder viewHolder;
    private FeelingsChronology mFeelingsChronology;
    private int[] colors;
    private int screen_width;
    private int screen_height;
    private View view;
    @BindView(R.id.toast_custom_layout) TextView convert_view;

    public ListViewAdapter(Context context, String[] texts_chrono, FeelingsChronology mFeelingsChronology, int[] colors, int screen_width, int screen_height){

        this.context=context;
        this.texts_chrono=texts_chrono;
        this.mFeelingsChronology=mFeelingsChronology;
        this.colors = colors;
        this.screen_width=screen_width;
        this.screen_height=screen_height;
    }

    @Override
    public int getCount() {
        return texts_chrono.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.chrono_item, parent, false);
        }

        // Initialize views
        view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        mFrameLayout= view.findViewById(R.id.toast_custom_layout);
        comment_view=view.findViewById(R.id.toast_custom_textview);

        viewHolder = (ListViewHolder) convertView.getTag();
        if (this.viewHolder == null)
            this.viewHolder = new ListViewHolder();

        // RELATIVELAYOUT (bar for each feeling)
        this.viewHolder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_layout);

        // Define the height of each relativelayout according to the number of days of chronology
        define_height_of_each_relativelayout();

        // Recover the feeling for the considered position
        Feeling feeling = mFeelingsChronology.getFeeling(position);

        // Define the layout of the views for the feeling
        if(feeling.getFeeling()!=-1 && texts_chrono.length>0)
            define_layout_views(feeling, position, convertView);
        else
            this.viewHolder.mRelativeLayout.getLayoutParams().width = 0;

        return convertView;
    }

    private void define_layout_views(Feeling feeling, int position, View convertView){

        viewHolder.mRelativeLayout.setBackgroundColor(colors[feeling.getFeeling()]);
        viewHolder.mRelativeLayout.getLayoutParams().width = (1 + feeling.getFeeling()) * screen_width / colors.length;

        // TEXTVIEW for the chrono tests (e.g. "il y a une semaine", "hier",...)
        viewHolder.mTextView = (TextView) convertView.findViewById(R.id.fragment_chrono_text);
        viewHolder.mTextView.setText(texts_chrono[position]);

        // IMAGEVIEW for the symbol comment
        define_imageview_for_symbol_comment(feeling,convertView);
    }

    private void define_height_of_each_relativelayout(){
        viewHolder.mRelativeLayout.getLayoutParams().height = screen_height / 7; // divide the screen
    }

    private void define_imageview_for_symbol_comment(Feeling feeling, View convertView){
        final String comment = feeling.getComment();
        if (comment != null) {
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.fragment_chrono_comment);
            viewHolder.mImageView.setImageResource(R.drawable.ic_comment_black_48px);
            viewHolder.mImageView.setOnClickListener(v -> {
                comment_view.setText(comment);
                Toast toast = new Toast(v.getContext());
                toast.setView(mFrameLayout);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            });
        }
    }

}

