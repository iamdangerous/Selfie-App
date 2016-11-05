package com.android.rahul.myselfieapp.Fragment;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Views.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class BlankFragment extends Fragment {
    Camera camera;
    CameraPreview cameraPreview;

    @Bind(R.id.camera_preview)
    FrameLayout frameLayout;

    @OnClick(R.id.btn_capture)
    void onClickCapture(){
        if(camera!=null){
            camera.takePicture(null,null,mPicture);
        }
    }


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = "BlankFrag";
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }
public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this,v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        openCamera();
    }

    void openCamera(){
         camera = getCameraInstance(getContext());
        camera.setDisplayOrientation(90);
        if(null == camera)
            return;
        Context context = getContext();
        if(null == context)
            return;

        cameraPreview = new CameraPreview(context,camera);
        frameLayout.addView(cameraPreview);

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            String filename = "pic.jpg";
            File file = new File(getContext().getFilesDir(), filename);


//            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//            if (pictureFile == null){
//                Log.d(TAG, "Error creating media file, check storage permissions: " +
//                        e.getMessage());
//                return;
//            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
            camera.startPreview();
        }
    };

    public static Camera getCameraInstance(Context context){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
            Toast.makeText(context,"Camera not avail",Toast.LENGTH_SHORT).show();
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
