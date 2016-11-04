package com.android.rahul.myselfieapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.rahul.myselfieapp.R;
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

    Client client;

    private static final String TAG = "LoginActivity";


    @OnClick(R.id.btn_sign_in)
    void onClickBtnSignIn(){

        String userName = etUser.getText().toString();
        String password = etPass.getText().toString();

        performSignIn(userName,password);
    }


    @OnClick(R.id.btn_sign_up)
    void onClickBtnSignUp(){
        String userName = etUser.getText().toString();
        String password = etPass.getText().toString();

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
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void init(){
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            Log.d(TAG,"success");
            showMainActivity();

        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.d(TAG,"Fail");
//            throwable.m
//                    if(throwable instanceof KinveyJsonResponseException){
//                        KinveyJsonError kinveyJsonError = ((KinveyJsonResponseException)throwable).getDetails();
//                        kinveyJsonError.get()
//                    }
            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

}
