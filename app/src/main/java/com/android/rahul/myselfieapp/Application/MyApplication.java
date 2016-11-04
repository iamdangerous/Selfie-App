package com.android.rahul.myselfieapp.Application;

import android.app.Application;

import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;

/**
 * Created by rkrde on 04-11-2016.
 */

public class MyApplication extends Application {

    private Client mClient;

    @Override
    public void onCreate() {
        super.onCreate();
        defineClient();
    }

    private void defineClient() {
        mClient = new Client.Builder(Constants.API_KEY, Constants.APP_SECRET
                , this.getApplicationContext()).build();
    }

    public Client getClient() {
        return mClient;
    }
}
