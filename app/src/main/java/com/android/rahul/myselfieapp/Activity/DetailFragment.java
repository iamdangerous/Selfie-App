package com.android.rahul.myselfieapp.Activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaColumns;
import com.android.rahul.myselfieapp.Storage.MediaProvider;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    @Bind(R.id.tv)
    AppCompatTextView tv;


    String TAG = "Detail Fragment";

    private static final String ARG_PARAM1 = "param1";

    private int mParam1;
    private String mParam2;

    private DetailFragmentListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(int param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        String url="";
        String mPath="";
        int mediaType = 0;
        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        String mSelection = MediaColumns._ID +" = ?";
        int id = mParam1;
        int fromKinvey=0,downloadStatus=0,uploadedStatus=0;
        String mSelectionArgs[] = {String.valueOf(id)};
        Cursor cursor = getContext().getContentResolver().query(uri, null, mSelection, mSelectionArgs, null);
        if (cursor.moveToFirst()) {
            do {

                String mId = cursor.getString(cursor.getColumnIndex(MediaColumns._ID));
                 mPath = cursor.getString(cursor.getColumnIndex(MediaColumns._PATH));
                url = cursor.getString(cursor.getColumnIndex(MediaColumns._URL));
                mediaType = cursor.getInt(cursor.getColumnIndex(MediaColumns._MEDIA_TYPE));
                fromKinvey = cursor.getInt(cursor.getColumnIndex(MediaColumns._FROM_KINVEY));
                downloadStatus = cursor.getInt(cursor.getColumnIndex(MediaColumns._DOWN_STATUS));
                uploadedStatus = cursor.getInt(cursor.getColumnIndex(MediaColumns._UPLOAD_STATUS));


                Log.d(TAG, "Query: mId:" + mId + ",mPath:" + mPath + ",status:" + uploadedStatus+",url:"+url);

            } while (cursor.moveToNext());
        }
        cursor.close();
        if(mediaType ==0){
             v =  inflater.inflate(R.layout.fragment_detail, container, false);
        }else {
            v =  inflater.inflate(R.layout.fragment_detail_video, container, false);
        }

        ButterKnife.bind(this,v);
        if(mediaType==0){
            AppCompatImageView imageView = (AppCompatImageView) v.findViewById(R.id.image_view);

            if(fromKinvey==1 && downloadStatus==1){
                Glide.with(getContext()).load(getContext().getFilesDir().getAbsolutePath()+"/"+mPath).into(imageView);
            }else if(fromKinvey ==1 && downloadStatus ==0) {
                Glide.with(getContext()).load(url).into(imageView);
            }else if(fromKinvey ==0 )
            {
                Glide.with(getContext()).load(getContext().getFilesDir().getAbsolutePath()+"/"+mPath).into(imageView);
                showReuploadOption();
//            }else if(fromKinvey == 0 && uploadedStatus ==1) {
//                Glide.with(getContext()).load(getContext().getFilesDir().getAbsolutePath()+"/"+mPath).into(imageView);
            }


        }else {
            //load video from local file
            VideoView mVideoView = (VideoView)v. findViewById(R.id.video_view);
            String videoUri = getContext().getFilesDir().getAbsolutePath()+"/"+mPath;

            mVideoView.setVideoURI(Uri.parse(videoUri));
            mVideoView.setMediaController(new MediaController(getContext()));
            mVideoView.requestFocus();
            mVideoView.start();
        }

        return v;
    }

    void showReuploadOption(){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailFragmentListener) {
            mListener = (DetailFragmentListener) context;
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

    public interface DetailFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
