package com.android.rahul.myselfieapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.kinvey.android.Client;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.IOException;
import java.security.PublicKey;

public class MyUploadService extends IntentService {

//    public static final int MEDIA_FILE = 1;
//    public static final int  MEDIA_BYTE = 2;

    public static final String COLLECTION_NAME = "Markets";


    Client mClient;
    String TAG = "MyUploadSrvice";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPLOAD = "com.android.rahul.myselfieapp.action.UPLOAD";
//    private static final String ACTION_BAZ = "com.android.rahul.myselfieapp.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.android.rahul.myselfieapp.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.android.rahul.myselfieapp.extra.PARAM2";


    public MyUploadService() {
        super("MyUploadService");
    }

    public MyUploadService(Client client) {
        super("MyUploadService");
        this.mClient = client;
    }

    public static void startActionUpload(Context context, UpdateEntity entity) {
        Intent intent = new Intent(context, MyUploadService.class);
        intent.setAction(ACTION_UPLOAD);
        intent.putExtra(EXTRA_PARAM1, entity);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {
                UpdateEntity entity = intent.getParcelableExtra(EXTRA_PARAM1);
                handleActionFoo(entity);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(final UpdateEntity entity) {
        mClient.linkedData(COLLECTION_NAME,UpdateEntity.class)
                .save(entity, new KinveyClientCallback<UpdateEntity>() {
                    @Override
                    public void onSuccess(UpdateEntity updateEntity) {
                        Log.d(TAG,"success entity");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(TAG,"fail entity");
                    }
                }, new UploaderProgressListener() {
                    @Override
                    public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
                        Log.d(TAG,"progressChanged file");
                    }

                    @Override
                    public void onSuccess(FileMetaData fileMetaData) {
                        Log.d(TAG,"success file");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(TAG,"failure file");
                    }
                })
        ;    }


}
