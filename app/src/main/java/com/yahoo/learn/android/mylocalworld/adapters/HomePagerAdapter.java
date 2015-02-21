package com.yahoo.learn.android.mylocalworld.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by ankurj on 2/20/2015.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private int mPageCount;
    private String mTabTitles[];
    private Fragment[] mFragments;

    public HomePagerAdapter(FragmentManager fm, Fragment[] fragments, String [] tabTitles) {
        super(fm);

        assert (tabTitles.length == fragments.length);
        mTabTitles = tabTitles;
        mFragments = fragments;
        mPageCount = tabTitles.length;
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTabTitles[position];
    }
}