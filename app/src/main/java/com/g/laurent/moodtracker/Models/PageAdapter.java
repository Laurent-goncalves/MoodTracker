package com.g.laurent.moodtracker.Models;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;


public class PageAdapter extends FragmentStatePagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    private int[] colors;
    private int last_feeling;
    private String last_comment;
    private int position;
    private Bundle bundle;

    // 2 - Default Constructor
    public PageAdapter(FragmentManager mgr, int[] colors, int last_feeling, String last_comment, Bundle bundle) {
        super(mgr);
        this.colors = colors;
        this.last_feeling = last_feeling;
        this.last_comment = last_comment;
        this.bundle=bundle;
    }

    @Override
    public int getCount() {

        return(colors.length); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        //this.position=position;
        return(PageFragment.newInstance(position, this.colors[position], last_feeling, last_comment,bundle));
    }

    @Override
    public int getItemPosition(Object object) {
        /*
        PageFragment pageFragment = (PageFragment) object;
        if (pageFragment != null) {
            pageFragment.update();
        }*/
        return PagerAdapter.POSITION_NONE;
    }


    /*@Override
    public void startUpdate(ViewGroup container) {
        container.
                setOnClickListener(null);

    }*/
}
