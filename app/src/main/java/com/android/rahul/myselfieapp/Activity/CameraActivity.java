package com.android.rahul.myselfieapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.rahul.myselfieapp.Fragment.CamImageFragment;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Utility.Constants;

import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {

    boolean camAvail=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
        checkCameraAvail();

        if(camAvail){
            if (null == savedInstanceState) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, CamImageFragment.newInstance())
                        .commit();
            }
        }

    }
    private void init(){
        ButterKnife.bind(this);
    }

    private void checkCameraAvail(){
        if(!Constants.checkCameraHardware(getApplicationContext()))
        {
            camAvail=false;
            Constants.showAwesomeToast(getApplicationContext(),getString(R.string.cam_not_avail));
            finishAffinity();
        }
    }


}
