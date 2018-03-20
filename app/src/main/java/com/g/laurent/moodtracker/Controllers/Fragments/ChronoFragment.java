package com.g.laurent.moodtracker.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.g.laurent.moodtracker.R;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ChronoFragment extends Fragment {

    @BindViews({ R.id.fragment_chrono_feeling1, R.id.fragment_chrono_feeling2, R.id.fragment_chrono_feeling3,
            R.id.fragment_chrono_feeling4,R.id.fragment_chrono_feeling5,R.id.fragment_chrono_feeling6,R.id.fragment_chrono_feeling7})

    List<RelativeLayout> mRelativeLayouts;

    @BindViews({ R.id.fragment_chrono_comment1, R.id.fragment_chrono_comment2, R.id.fragment_chrono_comment3,
            R.id.fragment_chrono_comment4,R.id.fragment_chrono_comment5,R.id.fragment_chrono_comment6,R.id.fragment_chrono_comment7})

    List<ImageView> mImageViews;


    private SharedPreferences sharedPreferences;
    private int position;
    private String comment;
    private Date mDateTime;
    private int[] colors;
    private HashMap<Integer,String> CommentTable;
    private ViewGroup screen;

    public ChronoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chrono, container, false);

        ButterKnife.bind(this, view);
        screen = container;

        CommentTable=new HashMap<Integer,String>();

        // Recover sharedPreferences and colors table for the different feelings
        sharedPreferences=getContext().getSharedPreferences("chrono", Context.MODE_PRIVATE);
        colors = getResources().getIntArray(R.array.colorPagesViewPager);

        // For each day in sharedpreferences, adjust the linearlayout and the icon comment
        create_layout_chronofragment();

        // Inflate the layout for this fragment
        return view;

    }

    private void create_layout_chronofragment(){

        for(int i = 6; i>=0; i--) {

            position = sharedPreferences.getInt("FEELING_" + i, 0);
            comment = sharedPreferences.getString("COMMENT_" + i, null);
            mDateTime = new Date(sharedPreferences.getLong("DATE_TIME_" + i, 0));

            if (!mDateTime.equals(new Date(0))){ // if feeling saved in sharepreferences for the day "i"
                // the linearlayout is adjusted to the width of the view
                mRelativeLayouts.get(i).getLayoutParams().width=(1+position) * screen.getWidth()/5;
                //mImageViews.get(i).setWidth((1+position) * container.getWidth()/5);

                // the linearlayout must be colored with the color related to the feeling
                mRelativeLayouts.get(i).setBackgroundColor(colors[position]);

                // the icon for the comment appears only if there is a comment different than null
                if (comment != null && !comment.equals("")) {

                    mImageViews.get(i).setImageResource(R.drawable.ic_comment_black_48px);

                    CommentTable.put(mImageViews.get(i).getId(),comment);

                    // add a clicklistener on comment icon to open a Toast message with the comment
                    mImageViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast toast = Toast.makeText(getContext(), CommentTable.get(v.getId()), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }

            } else { // else, the linearlayout is invisible
                mRelativeLayouts.get(i).setVisibility(View.INVISIBLE);
            }
        }

    }


}
