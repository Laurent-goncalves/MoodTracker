package com.g.laurent.moodtracker.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.g.laurent.moodtracker.Models.FeelingsChronology;
import com.g.laurent.moodtracker.Models.ListViewAdapter;
import com.g.laurent.moodtracker.R;

public class ChronoFragment extends Fragment {

    private int[] colors;
    private String[] chrono_texts;
    private ListView mListView;
    private FeelingsChronology mFeelingsChronology;
    private SharedPreferences sharedPreferences;

    public ChronoFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chrono, container, false);
        mListView = view.findViewById(R.id.listview_chrono);

        // Recover sharedPreferences and colors table for the different feelings
        if(getContext()!=null)
            sharedPreferences = getContext().getSharedPreferences("chrono", Context.MODE_PRIVATE);

        // Create table of values for color of each feelings and texts for chronology
        colors = getResources().getIntArray(R.array.colorPagesViewPager);
        chrono_texts = getResources().getStringArray(R.array.text_chrono_list);

        // Create list of feelings in chronological order
        mFeelingsChronology = new FeelingsChronology(chrono_texts.length, sharedPreferences);

        // For each feeling from mFeelingsChronology, adjust the relativelayout and the icon comment
        create_layout_chronofragment();

        // Inflate the layout for this fragment
        return view;
    }

    private void create_layout_chronofragment(){

        int screen_width = this.getResources().getDisplayMetrics().widthPixels;
        int screen_height= this.getResources().getDisplayMetrics().heightPixels - getToolBarHeight();

        if(colors.length>0 && mFeelingsChronology.number_of_feelings_saved()>0){ // if there is at least one feeling saved

            final ListViewAdapter adapter = new ListViewAdapter( // create the ListViewAdapter for displaying the list of feelings
                    getContext(), chrono_texts,mFeelingsChronology, colors,screen_width,screen_height);

            mListView.setAdapter(adapter);
        } else { // if no feeling are saved, display a message "Aucune chronologie !"
            Toast toast = Toast.makeText(getContext(), "Aucune chronologie !", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    public int getToolBarHeight() {
        TypedArray ta;
        int[] attrs = new int[] {R.attr.actionBarSize};
        if(getContext()!=null) {
            ta = getContext().obtainStyledAttributes(attrs);
            int toolBarHeight = ta.getDimensionPixelSize(0, -1);
            ta.recycle();
            return toolBarHeight;
        } else
            return 0;
    }

}