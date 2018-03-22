package com.g.laurent.moodtracker.Models;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;
import com.g.laurent.moodtracker.R;

public class PageAdapter extends FragmentPagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    private int[] colors;
    private String[] comment;
    private Boolean[] selected;

    // 2 - Default Constructor
    public PageAdapter(FragmentManager mgr, int[] colors, Boolean[] selected, String[] comment) {
        super(mgr);
        this.colors = colors;
        this.comment = comment;
        this.selected = selected;
    }

    @Override
    public int getCount() {
        return(5); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        // 4 - Page to return

        /*for(int i = 0;i<=4;i++)
            System.out.println("eeee " + selected[i] + " " + comment[i]);*/

        return(PageFragment.newInstance(position, this.colors[position], selected[position], comment[position]));
    }

}
