package com.android.rahul.myselfieapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.rahul.myselfieapp.Adapter.DetailViewPagerAdapter;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailFragment.DetailFragmentListener{

    @Bind(R.id.view_pager)
    ViewPager viewPager;


    DetailViewPagerAdapter adapter;
    int curPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.getAction().equalsIgnoreCase(getString(R.string.detail_actity))){
            curPos = intent.getIntExtra(getString(R.string.currentPos),0);
        }


        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int totalItems = cursor.getCount();
        cursor.close();
        adapter = new DetailViewPagerAdapter(getSupportFragmentManager(),getApplicationContext(),totalItems);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curPos);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
