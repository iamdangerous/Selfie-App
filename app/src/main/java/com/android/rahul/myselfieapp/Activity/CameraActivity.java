package com.android.rahul.myselfieapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.android.rahul.myselfieapp.Adapter.ViewPagerAdapter;
import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.Fragment.CamImageFragment;
import com.android.rahul.myselfieapp.Fragment.MyImageFragment;
import com.android.rahul.myselfieapp.Fragment.VideoFragment;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Service.MyUploadService;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CameraActivity extends BaseActivity implements
        MyImageFragment.ImageFragmentListener,
        VideoFragment.VideoFragmentListener {

    String TAG = "CameraActivity";


    ViewPagerAdapter viewPagerAdapter;
    boolean camAvail=true;
    Client mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
        checkCameraAvail();

        if(camAvail){
            if (null == savedInstanceState) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MyImageFragment.newInstance())
                        .commit();

//                getFragmentManager().beginTransaction()
//                        .replace(R.id.container, CamImageFragment.newInstance())
//                        .commit();
//                setAdapter();
            }
        }

    }

    void setAdapter(){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext(),2);
//        viewPager.setAdapter(viewPagerAdapter);
    }
    private void init(){
        ButterKnife.bind(this);
        mClient = getClient();

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void checkCameraAvail(){
        if(!Constants.checkCameraHardware(getApplicationContext()))
        {
            camAvail=false;
            Constants.showAwesomeToast(getApplicationContext(),getString(R.string.cam_not_avail));
            finishAffinity();
        }
    }


    @Override
    public void uploadMediaImage(byte[] data,String fileName) {
//        Intent msgIntent = new Intent(this, MyUploadService.class);
//        msgIntent.putExtra(getString(R.string.entity), updateEntity);
//        startService(msgIntent);
//        MyUploadService myUploadService = new MyUploadService(mClient);
        MyUploadService myUploadService = new MyUploadService(mClient);
        Constants.uploadMediaByteArray(getApplicationContext(),data,fileName,mClient);
    }

    @Override
    public void showVideoFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, VideoFragment.newInstance())
                .commit();
    }

    @Override
    public void showCameraFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MyImageFragment.newInstance())
                .commit();
    }
}
