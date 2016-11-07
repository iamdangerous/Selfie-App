package com.android.rahul.myselfieapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;

import com.android.rahul.myselfieapp.Application.MyApplication;
import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;
import com.kinvey.java.LinkedResources.LinkedFile;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MyUploadService extends IntentService {

//    public static final int MEDIA_FILE = 1;
//    public static final int  MEDIA_BYTE = 2;

    public static final String COLLECTION_NAME = "Markets";


    private  Client mClient;
    String TAG = "MyUploadSrvice";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPLOAD = "com.android.rahul.myselfieapp.action.UPLOAD";
//    private static final String ACTION_BAZ = "com.android.rahul.myselfieapp.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.android.rahul.myselfieapp.extra.PARAM1";
    private static final String EXTRA_URI = "extra_uri";
    private static final String EXTRA_FILE_NAME = "extra_file_name";


//    private static final String EXTRA_PARAM2 = "com.android.rahul.myselfieapp.extra.PARAM2";

    private static Uri globalUri;

    public MyUploadService() {
        super("MyUploadService");
    }

    public MyUploadService(Client client) {
        super("MyUploadService");
        this.mClient = client;
    }

    public static void startActionUpload(Context context,
                                         byte[] data,
                                         Uri uri,
                                         String fileName,
                                         Client client
    ) {
        Intent intent = new Intent(context, MyUploadService.class);
        intent.setAction(ACTION_UPLOAD);
        intent.putExtra(EXTRA_PARAM1,data);
        intent.putExtra(EXTRA_FILE_NAME,fileName);

        intent.setData(uri);
//        mClient = client;
        new MyUploadService(client);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {

                if(Constants.isNetWork(getApplicationContext())){

                    //set status to uploading !!
//                    globalUri =  Uri.parse(intent.getStringExtra(EXTRA_URI).toString());
                    globalUri = intent.getData();
                    Constants.updateUploadStatus(getApplicationContext(),globalUri,1);

//                    UpdateEntity entity = (UpdateEntity) intent.get(EXTRA_PARAM1);
                    String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
                    byte data[] = intent.getByteArrayExtra(EXTRA_PARAM1);

                    UpdateEntity entity = new UpdateEntity();
                    entity.putFile("attachment", new LinkedFile(fileName));
                    entity.getFile("attachment").setInput(new ByteArrayInputStream(data));

                    handleActionFoo(entity);
                }

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(final UpdateEntity entity) {

        MyApplication myApplication = (MyApplication) getApplication();
        mClient = myApplication.getClient();
        mClient.linkedData(COLLECTION_NAME,UpdateEntity.class)
                .save(entity, new KinveyClientCallback<UpdateEntity>() {
                    @Override
                    public void onSuccess(UpdateEntity updateEntity) {
                        Log.d(TAG,"success entity");

//                        getContentResolver().update()
                        Constants.updateUploadStatus(getApplicationContext(),globalUri,2);

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(TAG,"fail entity");
                        Constants.updateUploadStatus(getApplicationContext(),globalUri,0);
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
