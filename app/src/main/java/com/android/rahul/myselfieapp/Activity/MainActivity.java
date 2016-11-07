package com.android.rahul.myselfieapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.Fragment.BlankFragment;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaColumns;
import com.android.rahul.myselfieapp.Storage.MediaProvider;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.btn)
    AppCompatButton btn;
    @Bind(R.id.et_id)
    AppCompatEditText etId;
    @Bind(R.id.et_text)
    AppCompatEditText etText;


    private static final String TAG = "MainActivity";

    Client mKinveyClient;

    @OnClick(R.id.btn)
    void onClickBtn(){
        showCameraActivity();
    }

    @OnClick(R.id.btn_show_file_names)
    void onClickShowFileNames(){
        showFileNames();
    }

    @OnClick(R.id.btn_gallery)
    void onClickGallery(){
        showGalery();
    }

    @OnClick(R.id.btn_save)
    void onClickSubmit(){
        saveData();
    }

    @OnClick(R.id.btn_update)
    void onClickUpdate(){
        updateData();
    }

    @OnClick(R.id.btn_query)
    void onClickQuery(){
        queryData();
    }


    String projection []={ MediaColumns._ID,MediaColumns._PATH};

    void saveData(){
        String id = etId.getText().toString();
        String text = etText.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(MediaColumns._ID, id);
        cv.put(MediaColumns._PATH, text);

        Uri insertUri = getContentResolver().insert(MediaProvider.MediaLists.CONTENT_URI, cv);
        Log.d(TAG,"new InsertUri:"+insertUri);
    }

    void updateData(){
        String id = etId.getText().toString();
        String text = etText.getText().toString();
    }
    void queryData(){
        String id = etId.getText().toString();

        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);

        if (cursor.moveToFirst()) {
            do {

                String mId = cursor.getString(cursor.getColumnIndex(MediaColumns._ID));
                String mPath = cursor.getString(cursor.getColumnIndex(MediaColumns._PATH));
                Log.d(TAG,"Query: mId:"+mId+",mPath:"+mPath);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKinveyClient = getClient();
        init();
    }
    private void init(){
        ButterKnife.bind(this);

    }

    private void showCameraActivity(){
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

    void showFileNames(){

        File fileDir = getFilesDir();
        File[] fileArray = fileDir.listFiles();
        int i=0;
        while (i<fileArray.length){
            File file = fileArray[i];
            String fileName = file.getName();
            Log.d("File name=",fileName);
            ++i;
        }
    }

    void showGalery(){
        Intent intent = new Intent(this,GalleryActivity.class);
        startActivity(intent);
    }


}
