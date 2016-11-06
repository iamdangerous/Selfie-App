package com.android.rahul.myselfieapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.rahul.myselfieapp.Adapter.GalleryAdapter;
import com.android.rahul.myselfieapp.R;
import com.kinvey.android.Client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryActivity extends BaseActivity {

    @Bind(R.id.recycler_view)    RecyclerView recyclerView;

    GalleryAdapter adapter;
    List<File> fileList;
    Client mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        mClient = getClient();
//        if(null == savedInstanceState){
//            fileList =  loadFile();
//        }
        fileList = loadFile();

        setAdapter();


    }

    private void setAdapter(){
        adapter = new GalleryAdapter(this,fileList,mClient);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<File> loadFile(){
        List<File> mFileList = new ArrayList<>();
        File fileDir = getFilesDir();
        File[] fileArray = fileDir.listFiles();
        int i=0;
        while (i<fileArray.length){
            File file = fileArray[i];
            String fileName = file.getName();
            Log.d("File name=",fileName);

            if(fileName.startsWith("cam")){
                mFileList.add(file);
            }
            ++i;
        }
        return mFileList;

    }
}
