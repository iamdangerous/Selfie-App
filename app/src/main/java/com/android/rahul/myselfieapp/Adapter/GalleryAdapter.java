package com.android.rahul.myselfieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.rahul.myselfieapp.R;
import com.bumptech.glide.Glide;
import com.kinvey.android.Client;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rkrde on 06-11-2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>  {

    String TAG = "GalleryAdapter";
    Context context;
    List<File> stringList;
    private Client client;
    public GalleryAdapter(Context context, List<File> stringList, Client client){
        this.context = context;
        this.stringList = stringList;
        this.client = client;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final File file = stringList.get(position);
        Glide.with(context).load(file).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.file().upload(file,uploaderProgressListener);


            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_view)
        AppCompatImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    private UploaderProgressListener uploaderProgressListener = new UploaderProgressListener() {
        @Override
        public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
            Log.i(TAG, "upload progress: " + mediaHttpUploader.getUploadState());
        }

        @Override
        public void onSuccess(FileMetaData fileMetaData) {
            Log.i(TAG,"success");
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.i(TAG,"fail");
        }
    };
}
