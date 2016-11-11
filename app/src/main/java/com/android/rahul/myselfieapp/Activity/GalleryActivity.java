package com.android.rahul.myselfieapp.Activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;

import com.android.rahul.myselfieapp.Adapter.GalleryAdapter;
import com.android.rahul.myselfieapp.Adapter.GalleryCursorAdapter;
import com.android.rahul.myselfieapp.Entity.UpdateEntity;
import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Storage.MediaProvider;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.android.rahul.myselfieapp.Utility.FileUtility;
import com.google.api.client.util.ArrayMap;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.cache.CachePolicy;
import com.kinvey.java.cache.InMemoryLRUCache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.recycler_view)    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    String TAG = "GalleryActivity";

    @OnClick(R.id.fab)
    void onClickFab(){
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }

    private static final int LOADER_ID = 1;

    GalleryCursorAdapter mCursorAdapter;
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
        fileList = FileUtility.loadFile(getApplicationContext());

        setAdapter();
        getLoaderManager().initLoader(LOADER_ID, null,this);

        Constants.getDataFromKinvey(mClient,getApplicationContext());

    }



    private void setAdapter(){
//        adapter = new GalleryAdapter(this,fileList,mClient);

        mCursorAdapter = new GalleryCursorAdapter(this,null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mCursorAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case LOADER_ID:

                Uri uri = MediaProvider.MediaLists.CONTENT_URI;
                return new CursorLoader(this,uri,null,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId())
        {
            case LOADER_ID:
                this.mCursorAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        switch (loader.getId()){
            case LOADER_ID :
                mCursorAdapter.swapCursor(null);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                performSignOut();
                break;
            default:
        }
        return true;
    }

    void performSignOut(){
        mClient.user().logout().execute();
        clearStorage();
        showLoginScreen();
    }

    void clearStorage() {
        //empty Db
        Uri uri = MediaProvider.MediaLists.CONTENT_URI;
        int rowsDeleted = getContentResolver().delete(uri, null, null);
        Log.d(TAG, "rowsDeleted:" + rowsDeleted);
        //empty images and Videos
        FileUtility.deleteMediaFiles(getApplicationContext());
    }

    void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
