package com.android.rahul.myselfieapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.rahul.myselfieapp.Application.MyApplication;
import com.kinvey.android.Client;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Client getClient(){
        return ((MyApplication)getApplication()).getClient();
    }
}
