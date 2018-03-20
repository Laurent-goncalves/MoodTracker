package com.g.laurent.moodtracker.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.g.laurent.moodtracker.Models.Feelings;
import com.g.laurent.moodtracker.R;

public class PageFragment extends Fragment implements Feelings {

    // 1 - Create keys for our Bundle
    private static final String KEY_POSITION="position";
    private static final String KEY_COLOR="color";

    public PageFragment() { }

    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.
    public static PageFragment newInstance(int position, int color) {

        // 2.1 Create new fragment
        PageFragment frag = new PageFragment();

        // 2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        frag.setArguments(args);
        return(frag);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 3 - Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_page, container, false);

        // 4 - Get widgets from layout and serialise it
        ImageView image_feeling = (ImageView) result.findViewById(R.id.fragment_page_feeling);

        FrameLayout frameLayout = (FrameLayout) result.findViewById(R.id.fragment_page_back);

        // 5 - Get data from Bundle (created in method newInstance)
        int position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);

        image_feeling.setImageResource(mFeelings[position]);

        // 6 - Update widgets with it
        frameLayout.setBackgroundColor(color);

        Log.e(getClass().getSimpleName(), "onCreateView called for fragment number "+position);
        return result;

    }

}
