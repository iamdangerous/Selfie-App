package com.android.rahul.myselfieapp.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Utility.FileUtility;
import com.android.rahul.myselfieapp.Views.CameraPreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.android.rahul.myselfieapp.Fragment.MyImageFragment.getCameraInstance;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by rkrde on 05-11-2016.
 */

public class VideoFragment extends Fragment {

    Camera camera;
    MediaRecorder mediaRecorder;
    CameraPreview cameraPreview;
    private boolean isRecording = false;

    VideoFragmentListener mListener;


    @Bind(R.id.camera_preview)
    FrameLayout frameLayout;

    @Bind(R.id.btn_capture)
    FloatingActionButton btnCapture;


    @OnClick(R.id.btn_camera)
    void onClickCamera(){
        mListener.showCameraFragment();
    }


    @OnClick(R.id.btn_capture)
    void onClickCapture(){


        if(isRecording){
            mediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            camera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
//            btnCapture.setText(getString(R.string.start));
            btnCapture.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
            isRecording = false;
        }else {
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mediaRecorder.start();

                // inform the user that recording has started
//                btnCapture.setText(getString(R.string.stop));
                btnCapture.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));

                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, v);
        showCamPreview();
//        prepareVideoRecorder();
        return v;
    }

    boolean showCamPreview(){
        camera = getCameraInstance(getContext());
        if (null == camera)
            return false;

        Context context = getContext();
        if (null == context)
            return false;

        setCameraOrientation(camera);

        cameraPreview = new CameraPreview(context, camera);
        frameLayout.addView(cameraPreview);

        return true;
    }

    void setCameraOrientation(Camera camera) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
//        releaseCamera();
    }

    private boolean prepareVideoRecorder() {



        mediaRecorder = new MediaRecorder();

        //Step 1: unlock cam
        camera.unlock();
        mediaRecorder.setCamera(camera);

        //Step 2: set sources
        try{
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        }catch (Exception e){
            Log.wtf(TAG,"Exception:"+e.getMessage());
            return false;
        }

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        if (Build.VERSION.SDK_INT < 8) {
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        } else {
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        }

        // Step 4: Set output file

        String filename="cam_"+ Build.MANUFACTURER+"_"+Build.PRODUCT
                +"_"+new SimpleDateFormat("yyyyMMdd'-'HHmmss").format(new Date());

        filename=filename.replaceAll(" ", "_")+".mp4";

        File file = new File(getContext().getFilesDir(), filename);
        try {
            boolean fileCreated = file.createNewFile();
            Log.d(TAG,"fileCreated:"+fileCreated);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mediaRecorder.setOutputFile(FileUtility.getOutputMediaFileUri(getContext(),MEDIA_TYPE_VIDEO).toString());
        mediaRecorder.setOutputFile(file.getPath());


        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    void releaseMediaRecorder() {
        if(mediaRecorder!=null)
            mediaRecorder.release();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VideoFragmentListener) {
            mListener = (VideoFragmentListener) context;
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
    public interface VideoFragmentListener {
        void showCameraFragment();
    }
}
