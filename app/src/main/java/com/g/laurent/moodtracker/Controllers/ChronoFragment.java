package com.g.laurent.moodtracker.Controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.g.laurent.moodtracker.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class ChronoFragment extends Fragment {

    @BindViews({ R.id.fragment_chrono_feeling1, R.id.fragment_chrono_feeling2, R.id.fragment_chrono_feeling3,
            R.id.fragment_chrono_feeling4,R.id.fragment_chrono_feeling5,R.id.fragment_chrono_feeling6,R.id.fragment_chrono_feeling7})

    List<LinearLayout> mLinearLayouts;

    @BindViews({ R.id.fragment_chrono_comment1, R.id.fragment_chrono_comment2, R.id.fragment_chrono_comment3,
            R.id.fragment_chrono_comment4,R.id.fragment_chrono_comment5,R.id.fragment_chrono_comment6,R.id.fragment_chrono_comment7})

    List<ImageView> mImageViews;


    private SharedPreferences sharedPreferences;
    private int position;
    private String comment;
    private Date mDateTime;
    private int[] colors;

    public ChronoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chrono, container, false);
        ButterKnife.bind(this, view);

        // Recover sharedPreferences and colors table for the different feelings
        sharedPreferences=getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        colors = getResources().getIntArray(R.array.colorPagesViewPager);

        // For each day in sharedpreferences, adjust the linearlayout and the icon comment

        for(int i = 6; i>=0; i--) {

            position = sharedPreferences.getInt("FEELING_" + i, 0);
            comment = sharedPreferences.getString("COMMENT_" + i, null);
            mDateTime = new Date(sharedPreferences.getLong("DATE_TIME_" + i, 0));

System.out.println("eeee " + i);

            // FOR SAVE : sharedPreferences.edit().putLong("time", date.getTime()).apply();

            if (!mDateTime.equals(new Date(0))){ // if feeling saved in sharepreferences for the day "i"
                // the linearlayout is adjusted to the width of the view
                mLinearLayouts.get(i).getLayoutParams().width = position * container.getWidth() / 7;

                // the linearlayout must be colored with the color related to the feeling
                mLinearLayouts.get(i).setBackgroundColor(colors[position]);

                // the icon for the comment appears only if there is a comment different than null
                if (comment != null && !comment.equals("")) {
                    mImageViews.get(i).setImageResource(R.drawable.ic_comment_black_48px);
                }

            } else { // else, the linearlayout is invisible
                mLinearLayouts.get(i).setVisibility(View.INVISIBLE);
            }
        }

        // Inflate the layout for this fragment
        return view;

    }




}
