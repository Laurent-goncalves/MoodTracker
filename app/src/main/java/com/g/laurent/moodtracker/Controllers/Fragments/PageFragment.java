package com.g.laurent.moodtracker.Controllers.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    @BindView(R.id.fragment_page_back) LinearLayout linearLayout;

    private static final String KEY_POSITION="position";
    private static final String KEY_COLOR="color";
    private static final String KEY_LAST_FEELING="feeling";
    private static final String KEY_LAST_COMMENT="comment";
    protected callbackMainActivity mCallbackMainActivity;
    protected callbackMainActivity_test mCallbackMainActivity_test;
    private int last_feeling;
    private String last_comment;
    private SharedPreferences sharedPreferences;

    public PageFragment() { }

    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.
    public static PageFragment newInstance(int position, int color, int last_feeling, String last_comment, Bundle args) {

        // 2.1 Create new fragment
        PageFragment frag = new PageFragment();

        // 2.2 Create bundle and add it some data
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        args.putInt(KEY_LAST_FEELING,last_feeling);
        args.putString(KEY_LAST_COMMENT,last_comment);
        frag.setArguments(args);

        return(frag);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = getContext().getSharedPreferences("chrono", Context.MODE_PRIVATE);

        // Get data from Bundle (created in method newInstance)
        final int position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);

        if(sharedPreferences!=null){
            last_feeling = sharedPreferences.getInt("FEELING_-1", -1);
            last_comment = sharedPreferences.getString("COMMENT_-1",null);
        } else {
            last_feeling=-1;
            last_comment=null;
        }




        image_feeling.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                green_circle.setImageResource(R.drawable.cercle_vert);
                final MediaPlayer mp = MediaPlayer.create(getContext(), mSounds[position]);
                mp.start();
                mCallbackMainActivity.save_temp_last_feeling(position);
            }
        });



        // Draw imageView
        image_feeling.setImageResource(mFeelings[position]);
        linearLayout.setBackgroundColor(color);

        System.out.println("eeeee11 " + position + "  " + last_feeling + "  " + last_comment);

        if(last_feeling == position) {
            System.out.println("eeeee22 " + position + "  " + last_feeling + "  " + last_comment);
            green_circle.setImageResource(R.drawable.cercle_vert);
            commentView.setText(last_comment);
        }

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

    public void updateView() {

        final int position = getArguments().getInt(KEY_POSITION, -1);


        if(sharedPreferences!=null){
            last_feeling = sharedPreferences.getInt("FEELING_-1", -1);
            last_comment = sharedPreferences.getString("COMMENT_-1",null);
        } else {
            last_feeling=-1;
            last_comment=null;
        }

       /* int last_feeling = getArguments().getInt(KEY_LAST_FEELING, -1);
        String last_comment = getArguments().getString(KEY_LAST_FEELING,null);*/

        System.out.println("eeeeeff " + position + "  " + last_feeling + "  " + last_comment);

        if(last_feeling == position) {
            green_circle.setImageResource(R.drawable.cercle_vert);
            commentView.setText(last_comment);
        } else {
            if(green_circle!=null)
                green_circle.setImageResource(0);
            if(commentView!=null)
                commentView.setText(null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToMainActivity();
        this.createCallbackToMainActivity_test();
    }

    public void update_textview(String comment){
        commentView.setText(comment);
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
        /*test_imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallbackMainActivity_test.display_test();
            }
        });*/