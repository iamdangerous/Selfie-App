package com.android.rahul.myselfieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.rahul.myselfieapp.Activity.DetailActivity;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaColumns;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rkrde on 10-11-2016.
 */

public class GalleryCursorAdapter extends CursorRecyclerViewAdapter<GalleryCursorAdapter.ViewHolder>{

    Context context;
    Cursor cursor;
    public GalleryCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        this.cursor = cursor;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
//        MyListItem myListItem = MyListItem.fromCursor(cursor);
//        viewHolder.mTextView.setText(myListItem.getName());
        int fromKinvey = cursor.getInt(cursor.getColumnIndex(MediaColumns._FROM_KINVEY));
        if(fromKinvey==1){
            String url = cursor.getString(cursor.getColumnIndex(MediaColumns._URL));
            Glide.with(context).load(url).into(viewHolder.imageView);
        }else {
            String filePath = cursor.getString(cursor.getColumnIndex(MediaColumns._PATH));
            File file =new File(context.getFilesDir().getAbsolutePath()+"/"+filePath);
            Glide.with(context).load(file).into(viewHolder.imageView);
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.setAction(context.getString(R.string.detail_actity));
                intent.putExtra(context.getString(R.string.currentPos),viewHolder.getAdapterPosition());
                context.startActivity(intent);
            }
        });


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_view)
        AppCompatImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
