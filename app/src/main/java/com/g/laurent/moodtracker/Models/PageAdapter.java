package com.g.laurent.moodtracker.Models;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;


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
        return(colors.length); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        return(PageFragment.newInstance(position, this.colors[position], selected[position], comment[position]));
    }

    /*@Override
    public void startUpdate(ViewGroup container) {
        container.
                setOnClickListener(null);

    }*/
}
