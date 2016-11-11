package com.android.rahul.myselfieapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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

}
