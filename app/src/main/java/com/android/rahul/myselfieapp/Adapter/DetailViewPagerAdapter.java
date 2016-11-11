package com.android.rahul.myselfieapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.rahul.myselfieapp.Activity.DetailFragment;
import com.android.rahul.myselfieapp.Fragment.MyImageFragment;
import com.android.rahul.myselfieapp.Fragment.VideoFragment;

/**
 * Created by rkrde on 10-11-2016.
 */

public class DetailViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    int items,currentPos;

    public DetailViewPagerAdapter(FragmentManager fm,
                                  Context context,
                                  int items,
                                  int currentPos) {
        super(fm);
        this.context = context;
        this.items = items;
        this.currentPos = currentPos;
    }

    @Override
    public Fragment getItem(int position) {

        return    DetailFragment.newInstance(currentPos+position);
    }

    @Override
    public int getCount() {
        return items;
    }
}
