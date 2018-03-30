package com.g.laurent.moodtracker.Models;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.g.laurent.moodtracker.Controllers.Fragments.PageFragment;


public class PageAdapter extends FragmentStatePagerAdapter {

    private int[] colors;
    private Feeling feeling;

    public PageAdapter(FragmentManager mgr, int[] colors, Feeling feeling) {
        super(mgr);
        this.colors = colors;
        this.feeling = feeling;
    }

    @Override
    public int getCount() {return(colors.length);} // Number of fragments (feelings) to show

    @Override
    public Fragment getItem(int position) {return(PageFragment.newInstance(position, this.colors[position], feeling));}

    @Override
    public int getItemPosition(Object object) {return PagerAdapter.POSITION_NONE;}

}
