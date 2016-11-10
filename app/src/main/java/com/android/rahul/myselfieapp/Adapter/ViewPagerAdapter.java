package com.android.rahul.myselfieapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.rahul.myselfieapp.Fragment.MyImageFragment;
import com.android.rahul.myselfieapp.Fragment.VideoFragment;

/**
 * Created by rkrde on 10-11-2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    int items;

    public ViewPagerAdapter(FragmentManager fm,Context context, int items) {
        super(fm);
        this.context = context;
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return    MyImageFragment.newInstance();
            case 1:
                return VideoFragment.newInstance();
            default:
                return MyImageFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return items;
    }
}
