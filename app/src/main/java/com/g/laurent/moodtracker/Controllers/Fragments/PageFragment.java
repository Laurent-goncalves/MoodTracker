package com.g.laurent.moodtracker.Controllers.Fragments;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.g.laurent.moodtracker.Models.Feelings;
import com.g.laurent.moodtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends Fragment implements Feelings {

    // 1 - Create keys for our Bundle
    @BindView(R.id.fragment_green_circle) ImageView green_circle;
    @BindView(R.id.fragment_comment) TextView commentView;
    @BindView(R.id.fragment_page_feeling) ImageView image_feeling;
    @BindView(R.id.fragment_page_back) FrameLayout frameLayout;
    @BindView(R.id.fragment_test) ImageView test_imageview;

    private static final String KEY_POSITION="position";
    private static final String KEY_COLOR="color";
    private static final String KEY_IS_SELECTED="selection";
    private static final String KEY_COMMENT="selection";
    protected callbackMainActivity mCallbackMainActivity;
    protected callbackMainActivity_test mCallbackMainActivity_test;

    public PageFragment() { }

    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.
    public static PageFragment newInstance(int position, int color, boolean selected, String comment) {

        // 2.1 Create new fragment
        PageFragment frag = new PageFragment();

        // 2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        args.putBoolean(KEY_IS_SELECTED,selected);
        args.putString(KEY_COMMENT,comment);
        frag.setArguments(args);
        return(frag);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);

        // Get data from Bundle (created in method newInstance)
        final int position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);
        String comment = getArguments().getString(KEY_COMMENT, null);
        Boolean selected = getArguments().getBoolean(KEY_IS_SELECTED,false);

        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.ukulele);

        image_feeling.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                green_circle.setImageResource(R.drawable.cercle_vert);
                mp.start();
                mCallbackMainActivity.save_temp_last_feeling(position);

            }
        });

        test_imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mCallbackMainActivity_test.display_test();


            }
        });


        // Draw imageView

        image_feeling.setImageResource(mFeelings[position]);
        frameLayout.setBackgroundColor(color);
        commentView.setText(comment);

        if(selected)
            green_circle.setImageResource(R.drawable.cercle_vert);

        Log.e(getClass().getSimpleName(), "onCreateView called for fragment number "+position);
        return view;

    }


    // ------ CALLBACK FOR MAIN ACTIVITY (used to send the feeling number selected by the user) ----------------
    private void createCallbackToMainActivity(){
        try {
            // Parent activity will automatically subscribe to callback
            mCallbackMainActivity = (callbackMainActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement callbackMainActivity");
        }
    }

    public interface callbackMainActivity {
        void save_temp_last_feeling(int position);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToMainActivity();
        this.createCallbackToMainActivity_test();
    }



    private void createCallbackToMainActivity_test(){
        try {
            // Parent activity will automatically subscribe to callback
            mCallbackMainActivity_test = (callbackMainActivity_test) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement callbackMainActivity_test");
        }
    }

    public interface callbackMainActivity_test {
        void display_test();
    }

}
