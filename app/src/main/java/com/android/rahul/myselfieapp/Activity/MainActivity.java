package com.android.rahul.myselfieapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn)
    AppCompatButton btn;

    private static final String TAG = "MainActivity";

    Client mKinveyClient;

    @OnClick(R.id.btn)
    void onClickBtn(){
//        mKinveyClient.ping(new KinveyPingCallback() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//                Log.d(TAG,"success ping");
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                Log.d(TAG,"fail ping");
//            }
//        });

        showCameraActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    private void init(){
        ButterKnife.bind(this);

    }

    private void showCameraActivity(){
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }
}
