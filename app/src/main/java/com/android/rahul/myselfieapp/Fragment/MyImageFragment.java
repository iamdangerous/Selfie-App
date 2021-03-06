package com.android.rahul.myselfieapp.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.android.rahul.myselfieapp.Views.CameraPreview;
import com.kinvey.java.LinkedResources.LinkedFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyImageFragment extends Fragment {
    Camera camera;
    CameraPreview cameraPreview;

    ImageFragmentListener mListener;

    @Bind(R.id.camera_preview)
    FrameLayout frameLayout;

    @OnClick(R.id.btn_capture)
    void onClickCapture() {
        if (camera != null) {
            camera.takePicture(null, null, mPicture);
        }
    }

    @OnClick(R.id.btn_video)
    void onClickVideo() {
        mListener.showVideoFragment();
    }


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = "BlankFrag";
    private String mParam1;
    private String mParam2;

    public MyImageFragment() {
        // Required empty public constructor
    }

    public static MyImageFragment newInstance() {
        MyImageFragment fragment = new MyImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, v);
        openCamera();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(camera!=null)
//            camera.stopPreview();
    }

    @Override
    public void onStop() {
        super.onStop();
//        if(camera!=null)
//            camera.release();
    }

    void openCamera() {
        camera = getCameraInstance(getContext());
        if (null == camera) {
            Log.d(TAG, "camera is null");
            return;
        }


//        if(null == savedInstanceState){
        setCameraOrientation(camera);
        setZoomFeature();
        Context context = getContext();
        if (null == context) {
            Log.d(TAG, "context is null");
            return;
        }

        cameraPreview = new CameraPreview(context, camera);
        frameLayout.addView(cameraPreview);

        Log.d(TAG, "all good");
    }

    void setZoomFeature() {
        Camera.Parameters params = camera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // Autofocus mode is supported
            // set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
// set Camera parameters
            camera.setParameters(params);
        }

        if (params.getMaxNumMeteringAreas() > 0) { // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(-100, -100, 100, 100);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
            params.setMeteringAreas(meteringAreas);
        }
    }

    void setCameraOrientation(Camera camera) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

//            String filename = "pic.jpg";

            String filename = "cam_" + Build.MANUFACTURER + "_" + Build.PRODUCT
                    + "_" + new SimpleDateFormat("yyyyMMdd'-'HHmmss").format(new Date());

            filename = filename.replaceAll(" ", "_") + ".jpg";

            File file = new File(getContext().getFilesDir(), filename);

            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

//            createEntity(data,filename);
            mListener.uploadMediaImage(data, filename);
//            Constants.uploadMediaByteArray(getContext(),data,filename);

            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.WEBP, 50, fos);
                fos.flush();
//                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
            camera.startPreview();
        }
    };


    public static Camera getCameraInstance(Context context) {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Toast.makeText(context, "Camera not avail", Toast.LENGTH_SHORT).show();
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImageFragmentListener) {
            mListener = (ImageFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //
    public interface ImageFragmentListener {
        void uploadMediaImage(byte data[], String fileName);

        void showVideoFragment();
    }
}
