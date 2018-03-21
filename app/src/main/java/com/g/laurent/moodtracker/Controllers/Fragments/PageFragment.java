package com.g.laurent.moodtracker.Controllers.Fragments;


import android.content.Context;
import android.content.Intent;
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
    protected callbackMainActivity mCallbackMainActivity;
    int position;

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

        // Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_page, container, false);

        // Get widgets from layout and serialise it
        ImageView image_feeling = (ImageView) result.findViewById(R.id.fragment_page_feeling);
        FrameLayout frameLayout = (FrameLayout) result.findViewById(R.id.fragment_page_back);

        // Get data from Bundle (created in method newInstance)
        position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);

        image_feeling.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallbackMainActivity.save_temp_last_feeling(position);
            }
        });

        image_feeling.setImageResource(mFeelings[position]);

        // Update widgets with it
        frameLayout.setBackgroundColor(color);

        Log.e(getClass().getSimpleName(), "onCreateView called for fragment number "+position);
        return result;

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
    }
}
