package com.g.laurent.moodtracker.Controllers.Fragments;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.g.laurent.moodtracker.Models.Feeling;
import com.g.laurent.moodtracker.Models.Feelings;
import com.g.laurent.moodtracker.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends Fragment implements Feelings {

    // Create BindViews
    @BindView(R.id.fragment_green_circle) ImageView green_circle;
    @BindView(R.id.fragment_comment) TextView commentView;
    @BindView(R.id.fragment_page_feeling) ImageView image_feeling;
    @BindView(R.id.fragment_page_back) FrameLayout frameLayout;

    // Create keys for the Bundle
    private static final String KEY_POSITION="position";
    private static final String KEY_COLOR="color";
    private static final String KEY_LAST_FEELING="feeling";
    private static final String KEY_LAST_COMMENT="comment";
    private static final String KEY_LAST_DATE = "date";

    // Declaration of a callbackMainActivity
    protected callbackMainActivity mCallbackMainActivity;

    // Declaration variables
    private int position;
    private int color;
    private Feeling feeling;

    public PageFragment() { }

    public static PageFragment newInstance(int position, int color, Feeling feeling) {

            // Method that will create a new instance of PageFragment, and add data to its bundle.
        // Creation of a new fragment
        PageFragment frag = new PageFragment();

        // Creation of the bundle ; some data are added to the bundle
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        args.putInt(KEY_LAST_FEELING,feeling.getFeeling());
        args.putString(KEY_LAST_COMMENT,feeling.getComment());
        args.putLong(KEY_LAST_DATE,feeling.getDate());
        frag.setArguments(args);

        return(frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);

        // Get data from Bundle (created in method newInstance)
        position = getArguments().getInt(KEY_POSITION, -1);
        color = getArguments().getInt(KEY_COLOR, -1);
        feeling = new Feeling(  getArguments().getInt(KEY_LAST_FEELING, -1),
                                getArguments().getInt(KEY_LAST_DATE, 0),
                                getArguments().getString(KEY_LAST_COMMENT, null));

        update_image_feeling_and_framelayout();
        update_commentView();
        update_green_circle();

        return view;
    }

    private void update_image_feeling_and_framelayout(){

        // IMAGEVIEW update and adding of a click listener
        image_feeling.setOnClickListener(v -> {
            green_circle.setImageResource(R.drawable.cerclevert);
            final MediaPlayer mp = MediaPlayer.create(getContext(), mSounds[position]);
            mp.start();
            Feeling new_feeling = new Feeling(position,System.currentTimeMillis(),commentView.getText().toString());
            mCallbackMainActivity.save_temp_last_feeling(position, new_feeling);
        });

        // FRAMELAYOUT update
        image_feeling.setImageResource(mFeelings[position]);
        frameLayout.setBackgroundColor(color);
    }

    private void update_commentView() {
        if (commentView != null) {
            if (position == feeling.getFeeling() && feeling.getComment() != null)
                commentView.setText(feeling.getComment());
            else
                commentView.setText(null);
        }
    }

    private void update_green_circle(){

        if(green_circle!=null) {
            if(feeling.getFeeling() == position)
                green_circle.setImageResource(R.drawable.cerclevert);
            else
                green_circle.setImageResource(0);
        }
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
        void save_temp_last_feeling(int position, Feeling feeling);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToMainActivity();
    }

}
