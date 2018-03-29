package com.g.laurent.moodtracker.Models;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;


public class PageAdapter extends FragmentStatePagerAdapter {

    private int[] colors;
    private int last_feeling;
    private String last_comment;

    public PageAdapter(FragmentManager mgr, int[] colors,int last_feeling, String last_comment) {
        super(mgr);
        this.colors = colors;
        this.last_feeling = last_feeling;
        this.last_comment = last_comment;
    }

    @Override
    public int getCount() {return(colors.length);} // Number of fragments (feelings) to show

    @Override
    public Fragment getItem(int position) {return(PageFragment.newInstance(position, this.colors[position], last_feeling, last_comment));}

    @Override
    public int getItemPosition(Object object) {return PagerAdapter.POSITION_NONE;}

}
