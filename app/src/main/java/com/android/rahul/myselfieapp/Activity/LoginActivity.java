package com.android.rahul.myselfieapp.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.rahul.myselfieapp.R;
import com.android.rahul.myselfieapp.Utility.Constants;
import com.kinvey.android.Client;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.KinveyJsonError;
import com.kinvey.java.core.KinveyJsonResponseException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity  implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.btn_sign_in)    AppCompatButton btnSignIn;
    @Bind(R.id.btn_sign_up) AppCompatButton btnSignUp;
    @Bind(R.id.et_user)    AppCompatEditText etUser;
    @Bind(R.id.et_pass) AppCompatEditText etPass;

    @Bind(R.id.til_user)
    TextInputLayout tilUser;
    @Bind(R.id.til_pass)
    TextInputLayout tilPass;


    Client client;
    String[] perms = {Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int REQUEST_CODE = 100;

    private static final String TAG = "LoginActivity";

    ProgressDialog progressDialog;

    @OnClick(R.id.btn_sign_in)
    void onClickBtnSignIn(){

        String userName = etUser.getText().toString();
        String password = etPass.getText().toString();


        if(!checkValidInputs(userName,password))
            return;

        progressDialog.show();
        performSignIn(userName,password);
    }

    boolean checkValidInputs(String userName,String password){
        if(userName.isEmpty()){
            tilUser.setError(getString(R.string.enter_username));
            return false;
        }

        if(password.isEmpty()){
            tilPass.setError(getString(R.string.enter_pass));
            return false;
        }
        return true;
    }


    @OnClick(R.id.btn_sign_up)
    void onClickBtnSignUp(){
        String userName = etUser.getText().toString();
        String password = etPass.getText().toString();

        if(!checkValidInputs(userName,password))
            return;

        progressDialog.show();
        performSignUp(userName,password);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = getClient();
        if(client.user().isUserLoggedIn())
        {
            showMainActivity();
        }
        setContentView(R.layout.activity_login);
        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        askPermission();

    }

    void askPermission(){


        int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int permissionCheckMic = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheckCamera == PackageManager.PERMISSION_GRANTED ||
                permissionCheckMic == PackageManager.PERMISSION_GRANTED||
                permissionCheckStorage == PackageManager.PERMISSION_GRANTED)
            {

        }else {
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_video_perm),
                    REQUEST_CODE , perms);
        }



    }

    private  void showMainActivity(){
        Intent intent = new Intent(this,GalleryActivity.class);
        startActivity(intent);
        finish();
    }

    private void init(){
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(10);
        progressDialog.setCancelable(false);
        //progressDialog.setIndeterminate(true);
    }

    private void performSignIn(String userName,String password){
        client.user().login(userName,password,callback);
    }

    private void performSignUp(String userName,String password){
        client.user().create(userName,password,callback);
    }

    KinveyClientCallback<User> callback = new KinveyClientCallback<User>() {
        @Override
        public void onSuccess(User user) {

            progressDialog.dismiss();
            Log.d(TAG,"success");
            showMainActivity();

        }

        @Override
        public void onFailure(Throwable throwable) {
            progressDialog.dismiss();
            Log.d(TAG,"Fail");
            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG,"permission granted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG,"permission denied");
    }
}
