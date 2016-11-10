package com.android.rahul.myselfieapp.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Service.MyUploadService;
import com.android.rahul.myselfieapp.Storage.MediaColumns;
import com.android.rahul.myselfieapp.Storage.MediaProvider;
import com.google.api.client.util.ArrayMap;
import com.google.common.io.Files;
import com.kinvey.android.Client;
import com.kinvey.java.LinkedResources.LinkedFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by rkrde on 03-11-2016.
 */

public class Constants {

    public static final String API_KEY = "kid_S1w5ugKlg";
    public static final String APP_SECRET = "be2cfa240e9c48bb97cbb24bbd9f9c10";
    public static final String COLLECTION = "Markets";
    public static final String DOWNLOAD_URL = "_downloadURL";
    public static final String FILE_NAME = "_filename";
    public static final String KINVEY_ID = "_kiinvey_id";


    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static void showAwesomeToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }


    public static void uploadMediaFile(Context context, File file, String fileName,Client client){
        try {
            byte data[] = Files.toByteArray(file);
            uploadMediaByteArray(context,data,fileName,client);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void uploadMediaByteArray(Context context, byte data[], String fileName, Client client){

        //Insertion
        int status = 0;//fail
        ContentValues cv = new ContentValues();
        cv.put(MediaColumns._PATH, fileName);
        cv.put(MediaColumns._UPLOAD_STATUS, status);
        Uri insertUri = context.getContentResolver().insert(MediaProvider.MediaLists.CONTENT_URI, cv);
        Log.d("uploadNedia","new InsertUri:"+insertUri);

        if(isNetWork(context)){
            MyUploadService.startActionUpload(context,data,insertUri,fileName,client);
        }

    }

    public static boolean isNetWork(Context context){
        //Check Internet
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void updateUploadStatus(Context context, Uri uri, int status){
        String uriPath = uri.getPath(); //      /media/10
        String id = uriPath.substring(7); //    10

        ContentValues cv  = new ContentValues();
        cv.put(MediaColumns._UPLOAD_STATUS, status);
        String where  = MediaColumns._ID +"= ?";
        String selectionArgs[] = {id};
        int rowsUpdated = context.getContentResolver().update(uri,cv,where,selectionArgs);

        Log.d("setUploadStatus: ","rowsUpdated:"+rowsUpdated);
    }

    public static boolean isDbEmpty(Context context)
    {
        String[] projection = {MediaColumns._PATH};
        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public static void smartBulkInsert(Context context,List<ArrayMap<String, String>> list)
    {
        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        String mProjection[] = {MediaColumns._ID,MediaColumns._KINVEY_ID};
        String mSelection = MediaColumns._KINVEY_ID +" = ?";

        for(int i=0;i<list.size();++i){
            String kinveyId = list.get(i).get(Constants.KINVEY_ID);
            String mSelectionArgs[] = {kinveyId};
            Log.d("Input KinveyId",kinveyId);
            Cursor cursor =context.getContentResolver().query(uri,null,mSelection,mSelectionArgs,null);
            int colCount = cursor.getColumnCount();

//            int mId = cursor.getInt(0);
//            String mKinveyId = cursor.getString(1);
//
//            Log.d("Column Name","[0]:"+cursor.getColumnName(0)+",[1]:"+cursor.getColumnName(1));
//            Log.d("--->","mId:"+mId+",mKinveyId:"+mKinveyId);

            if(cursor.moveToFirst()){
                Log.d("Hello","111");
            }else {
                Log.d("Hello","222");
            }
            cursor.close();


            Log.i("File list:","kinveyId:"+kinveyId+",cursor col:"+colCount);
//            if(colCount>1){
//                //insert
//                String fileName = list.get(i).get(Constants.FILE_NAME);
//                Log.wtf("File Removed:","kinveyId:"+kinveyId+",fileName:"+fileName);
//                list.remove(i);
//            }
        }

//        bulkInsertMedia(context,list);


    }

    public static void bulkInsertMedia(Context context, List<ArrayMap<String, String>> list)
    {
        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        ContentValues[] contentValues = new ContentValues[list.size()];

        for(int i=0;i<list.size();++i)
        {

            String kinveyId = list.get(i).get(Constants.KINVEY_ID);
            String downloadUrl = list.get(i).get(Constants.DOWNLOAD_URL);
            String fileName = list.get(i).get(Constants.FILE_NAME);
            int fromKinvey = 1;
            int uploadStatus = -1;
            int downloadStatus = 0;




            contentValues[i] = new ContentValues();
            contentValues[i].put(MediaColumns._UPLOAD_STATUS,uploadStatus);
            contentValues[i].put(MediaColumns._PATH,fileName);
            contentValues[i].put(MediaColumns._URL,downloadUrl);
            contentValues[i].put(MediaColumns._DOWN_STATUS,downloadStatus);
            contentValues[i].put(MediaColumns._FROM_KINVEY,fromKinvey);
            contentValues[i].put(MediaColumns._KINVEY_ID,kinveyId);

        }
        int rowsInserted = context.getContentResolver().bulkInsert(uri,contentValues);
        Log.d("Constants:","bulkInsert New Rows Inserted:"+rowsInserted);
    }
}
