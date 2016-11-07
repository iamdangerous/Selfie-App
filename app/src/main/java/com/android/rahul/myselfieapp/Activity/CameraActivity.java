package com.android.rahul.myselfieapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.Fragment.MyImageFragment;
import com.android.rahul.myselfieapp.Fragment.VideoFragment;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Service.MyUploadService;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;

import butterknife.ButterKnife;

public class CameraActivity extends BaseActivity implements
        MyImageFragment.BlankFragmentListener {

    String TAG = "CameraActivity";

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
            }
        }

    }
    private void init(){
        ButterKnife.bind(this);
        mClient = getClient();
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

}
