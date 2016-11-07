package com.android.rahul.myselfieapp.Utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Service.MyUploadService;
import com.google.common.io.Files;
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


    public static void uploadMediaFile(Context context, File file, String fileName){
        try {
            byte data[] = Files.toByteArray(file);
            uploadMediaByteArray(context,data,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void uploadMediaByteArray(Context context,byte data[],String fileName){
        UpdateEntity entity = new UpdateEntity();
        entity.putFile("attachment", new LinkedFile(fileName));
        entity.getFile("attachment").setInput(new ByteArrayInputStream(data));
        MyUploadService.startActionUpload(context,entity);
    }

}
