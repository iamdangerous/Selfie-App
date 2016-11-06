package com.android.rahul.myselfieapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.Fragment.BlankFragment;
import com.android.rahul.myselfieapp.Fragment.CamImageFragment;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Service.MyUploadService;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;
import com.kinvey.java.cache.CachePolicy;
import com.kinvey.java.cache.InMemoryLRUCache;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.IOException;

import butterknife.ButterKnife;

public class CameraActivity extends BaseActivity implements
        BlankFragment.BlankFragmentListener {

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
                        .replace(R.id.container, BlankFragment.newInstance())
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
    public void saveEntity(UpdateEntity updateEntity) {
        Intent msgIntent = new Intent(this, MyUploadService.class);
        msgIntent.putExtra(getString(R.string.entity), updateEntity);
        startService(msgIntent);
    }

}
