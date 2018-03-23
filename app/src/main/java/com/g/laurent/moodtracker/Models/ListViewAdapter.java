package com.g.laurent.moodtracker.Models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.moodtracker.R;
import com.g.laurent.moodtracker.Views.ListViewHolder;


public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] texts_chrono;
    private String[] texts_comment;
    private int[] feelings;
    private int[] colors;
    private int screen_width;
    private int screen_height;

    public ListViewAdapter(Context context, String[] texts_chrono,String[] texts_comment ,int[] feelings, int[] colors, int screen_width, int screen_height){
        this.context=context;
        this.texts_chrono=texts_chrono;
        this.texts_comment=texts_comment;
        this.feelings=feelings;
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

        ListViewHolder viewHolder = (ListViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ListViewHolder();

            // RELATIVELAYOUT

            if(feelings[position]!=-1){

                viewHolder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_layout);
                viewHolder.mRelativeLayout.setBackgroundColor(colors[feelings[position]]);
                viewHolder.mRelativeLayout.getLayoutParams().width = (1 + feelings[position]) * screen_width / 5;
                viewHolder.mRelativeLayout.getLayoutParams().height = screen_height / 7;

                //TEXTVIEW
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.fragment_chrono_text);
                viewHolder.mTextView.setText(texts_chrono[position]);

                final String comment = texts_comment[position];

                //IMAGEVIEW
                if (comment != null) {
                    viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.fragment_chrono_comment);
                    viewHolder.mImageView.setImageResource(R.drawable.ic_comment_black_48px);
                    viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast = Toast.makeText(context, comment, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            } else {
                viewHolder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_layout);
                viewHolder.mRelativeLayout.setBackgroundColor(Color.BLACK);
                viewHolder.mRelativeLayout.getLayoutParams().width = (1 + feelings[position]) * screen_width / 5;
            }
        }
        return convertView;
    }

}
