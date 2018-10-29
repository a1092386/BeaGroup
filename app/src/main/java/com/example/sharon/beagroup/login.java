package com.example.sharon.beagroup;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity {

    EditText edUserid, edUserpwd;
    Button btn_login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mFirestore;
    private static final String TAG = "loginActivity";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Activity OnCreate() order", "login.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Reference to variables
        edUserid = (EditText) findViewById(R.id.id);
        edUserpwd = (EditText) findViewById(R.id.pwd);
        btn_login = (Button)findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        boolean serviceRunning = serviceUtils.isServiceRunning(this, "com.example.sharon.beagroup.periodicallyUploadService");
        if (serviceRunning){
            Log.d("login.onCreate()", "service [periodicallyUploadService] is running");
            Intent stopIntent = new Intent(this, periodicallyUploadService.class);
            stopService(stopIntent);
            Log.d("login.onCreate()", "STOP service [periodicallyUploadService]");
        }else{
            Log.d("login.onCreate()", "service [periodicallyUploadService] isn't running");
        }

        /*mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged : signed_in: " + user.getUid());
                    //toastMessage("成功登入"+user.getEmail());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //intent.setClass(login.this, MainActivity.class);
                    startActivity(intent);

                }
                else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged : signed_out");
                    toastMessage("成功登出");
                }
            }
        };*/
        /*btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edUserid.getText().toString();
                String pass = edUserpwd.getText().toString();
                String email = id+"@beagroup.com";
                if(!id.equals("") && !pass.equals("")){
                    mAuth.signInWithEmailAndPassword(email, pass);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    toastMessage("沒有填入資料");
                }
            }
        });*/




    }
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/

    public void onLogin(View view) throws ExecutionException, InterruptedException{

        String userid = edUserid.getText().toString();
        String password = edUserpwd.getText().toString();
        SaveSharedPreference.setID(login.this, userid);
        String type = "login";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        String result = backgroundWork.execute(type, userid,password).get(); //傳參數(型態：登入、登入內容)
       /*if(result.equals("Signup  success")){ //若登入成功，跳轉至主畫面

           mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
               @Override
               public void onSuccess(GetTokenResult getTokenResult) {
                   String tokenID = getTokenResult.getToken();
                   String currentID = mAuth.getCurrentUser().getUid();

                   Map<String, Object> tokenMap = new HashMap<>();
                   tokenMap.put("tokenId", tokenID);
                   mFirestore.collection("Users").document(userid).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                           //intent.setClass(login.this, MainActivity.class);
                           startActivity(intent);
                       }
                   });
               }
           });
           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
           //intent.setClass(login.this, MainActivity.class);
           startActivity(intent);
        }*/

    }



    public void openSignup(View view){
        startActivity(new Intent(this, signup.class));
    } //按下Signup進入註冊畫面

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }









}

