package com.android.rahul.myselfieapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaColumns;
import com.android.rahul.myselfieapp.Storage.MediaProvider;
import com.android.rahul.myselfieapp.Utility.FileUtility;
import com.kinvey.android.Client;

import java.io.File;

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
    @Bind(R.id.et_status)
    AppCompatEditText etStatus;


    private static final String TAG = "MainActivity";

    Client mKinveyClient;

    @OnClick(R.id.btn)
    void onClickBtn() {
        showCameraActivity();
    }

    @OnClick(R.id.btn_show_file_names)
    void onClickShowFileNames() {
        showFileNames();
    }

    @OnClick(R.id.btn_gallery)
    void onClickGallery() {
        showGalery();
    }

    @OnClick(R.id.btn_save)
    void onClickSubmit() {
        saveData();
    }

    @OnClick(R.id.btn_update)
    void onClickUpdate() {
        updateData();
    }

    @OnClick(R.id.btn_query)
    void onClickQuery() {
        queryData();
    }

    @OnClick(R.id.btn_media_from_kinvey)
    void onClickMediaFromKinvey() {
        getKinveyMedia();
    }

    @OnClick(R.id.btn_sign_out)
    void onClickSignOut() {
        performSignOut();
    }


    String projection[] = {MediaColumns._ID, MediaColumns._PATH, MediaColumns._UPLOAD_STATUS};

    void saveData() {
        String id = etId.getText().toString();
        String text = etText.getText().toString();
        int status = Integer.parseInt(etStatus.getText().toString());


        ContentValues cv = new ContentValues();
        cv.put(MediaColumns._ID, id);
        cv.put(MediaColumns._PATH, text);
        cv.put(MediaColumns._UPLOAD_STATUS, status);


        Uri insertUri = getContentResolver().insert(MediaProvider.MediaLists.CONTENT_URI, cv);
        Log.d(TAG, "new InsertUri:" + insertUri);


    }

    void getKinveyMedia() {

    }

    void performSignOut(){
        mKinveyClient.user().logout().execute();
        clearStorage();
        showLoginScreen();

    }

    void clearStorage(){
        //empty Db
        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        int rowsDeleted = getContentResolver().delete(uri,null,null);
        Log.d(TAG,"rowsDeleted:"+rowsDeleted);
        //empty images and Videos
        FileUtility.deleteMediaFiles(getApplicationContext());
    }

    void showLoginScreen(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    void updateData() {
        String id = etId.getText().toString();
        String text = etText.getText().toString();
        int status = Integer.parseInt(etStatus.getText().toString());

        long _id = Long.parseLong(id);
        Uri uri = MediaProvider.MediaLists.withId(_id);
        ContentValues cv = new ContentValues();
        cv.put(MediaColumns._ID, id);
        cv.put(MediaColumns._PATH, text);
        cv.put(MediaColumns._UPLOAD_STATUS, status);


        String where = MediaColumns._ID + "= ?";
        String selectionArgs[] = {id};
        int rowsUpdated = getContentResolver().update(uri, cv, where, selectionArgs);

        Log.d(TAG, "rowsUpdated:" + rowsUpdated);
    }

    void queryData() {
        String id = etId.getText().toString();

        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String mId = cursor.getString(cursor.getColumnIndex(MediaColumns._ID));
                String mPath = cursor.getString(cursor.getColumnIndex(MediaColumns._PATH));
                int mStatus = cursor.getInt(cursor.getColumnIndex(MediaColumns._UPLOAD_STATUS));

                Log.d(TAG, "Query: mId:" + mId + ",mPath:" + mPath + ",status:" + mStatus);
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

    private void init() {
        ButterKnife.bind(this);

    }

    private void showCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    void showFileNames() {

        File fileDir = getFilesDir();
        File[] fileArray = fileDir.listFiles();
        int i = 0;
        while (i < fileArray.length) {
            File file = fileArray[i];
            String fileName = file.getName();
            Log.d("File name=", fileName);
            ++i;
        }
    }

    void showGalery() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }


}
