package com.android.rahul.myselfieapp.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
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
import com.google.common.io.Files;
import com.kinvey.android.Client;
import com.kinvey.java.LinkedResources.LinkedFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by rkrde on 03-11-2016.
 */

public class Constants {

    public static final String API_KEY = "kid_S1w5ugKlg";
    public static final String APP_SECRET = "be2cfa240e9c48bb97cbb24bbd9f9c10";

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
//            UpdateEntity entity = new UpdateEntity();
//            entity.putFile("attachment", new LinkedFile(fileName));
//            entity.getFile("attachment").setInput(new ByteArrayInputStream(data));
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
//        String id = etId.getText().toString();
        String uriPath = uri.getPath(); //      /media/10
        String id = uriPath.substring(6); //    10
                //        int status = Integer.parseInt(etStatus.getText().toString());

//        long _id = Long.parseLong(id);
//        Uri uri = MediaProvider.MediaLists.withId(_id);
        ContentValues cv  = new ContentValues();
//        cv.put(MediaColumns._ID, id);
//        cv.put(MediaColumns._PATH, text);
        cv.put(MediaColumns._UPLOAD_STATUS, status);


        String where  = MediaColumns._ID +"= ?";
        String selectionArgs[] = {id};
        int rowsUpdated = context.getContentResolver().update(uri,cv,where,selectionArgs);

        Log.d("setUploadStatus: ","rowsUpdated:"+rowsUpdated);
    }
}
