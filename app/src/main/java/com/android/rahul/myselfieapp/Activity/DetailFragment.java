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

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaColumns;
import com.android.rahul.myselfieapp.Storage.MediaProvider;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    @Bind(R.id.tv)
    AppCompatTextView tv;
    @Bind(R.id.image_view)
    AppCompatImageView imageView;

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
     * @param param2 Parameter 2.
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
        View v =  inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,v);

        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        String mSelection = MediaColumns._ID +" = ?";
        int id = mParam1;
        String mSelectionArgs[] = {String.valueOf(id)};
        Cursor cursor = getContext().getContentResolver().query(uri, null, mSelection, mSelectionArgs, null);

        if (cursor.moveToFirst()) {
            do {

                String mId = cursor.getString(cursor.getColumnIndex(MediaColumns._ID));
                String mPath = cursor.getString(cursor.getColumnIndex(MediaColumns._PATH));
                int mStatus = cursor.getInt(cursor.getColumnIndex(MediaColumns._UPLOAD_STATUS));
                String url = cursor.getString(cursor.getColumnIndex(MediaColumns._URL));


                Log.d(TAG, "Query: mId:" + mId + ",mPath:" + mPath + ",status:" + mStatus+",url:"+url);

                Glide.with(getContext()).load(url).into(imageView);

            } while (cursor.moveToNext());
        }
        cursor.close();


        return v;
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
