package com.example.sharon.beagroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

public class BackgroundWork extends AsyncTask<String, Void, String>{ //背景執行的工作，並提供方法能夠與UI thread溝通互動
    AlertDialog alertDialog;
    Context context;
    String login_code="";
    String signup_code="";
    SharedPreferences preferences;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    MyFirebaseMessagingService FMS;
    BackgroundWork(Context ctx){
        context = ctx;
    }

    @Override
    protected String  doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://140.113.73.42/login.php";
        String signup_url = "http://140.113.73.42/signup.php";
        if(type.equals("login")){
            try {
                String user_id = params[1];
                String password = params[2];
                URL url  = new URL(login_url); //URL物件
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection(); //創建http連接
                httpURLConnection.setRequestMethod("POST"); //以POST傳送
                httpURLConnection.setDoInput(true); //是否向httpURLConnection輸出:true
                httpURLConnection.setDoOutput(true); //是否從httpURLConnection讀入:true
                OutputStream outputStream = httpURLConnection.getOutputStream(); //得到subprocess的輸出流
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8")); //outputStreamWriter為character變成byte stream的橋樑，在此轉成UTF-8；放入緩衝區(可加快速度)
                String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id, "UTF-8")
                        +"&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8"); //設定資料POST的格式，且將讀取到的參數assign給要傳到php的變數(較前面的)
                bufferedWriter.write(post_data);//將post_data寫入緩衝區
                bufferedWriter.flush(); //刷新該stream中的緩衝，將缓衝數據寫到目的文件中(login.php)
                bufferedWriter.close(); //關閉stream
                outputStream.close(); //關閉此outputStream並釋放與此stream有關的所有系統資源

                InputStream inputStream = httpURLConnection.getInputStream(); //獲取subprocess的輸入流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1")); //InputStreamReader為byte stream轉character，且轉成iso-8859-1；並放在緩衝區
                String result = "";

                while((login_code = bufferedReader.readLine()) != null){ //從緩衝區讀取數據
                    if(login_code.equals("1")) { //資料正確
                        Log.w("測試firebase", "到login success"+"帳號："+user_id+"密碼："+password+"login_code:"+login_code);
                        String user_mail = user_id+"@beagrouop.com";
                        //mAuth.signInWithEmailAndPassword(user_mail, password);
                        result += "Login success";

                    }
                    else if(login_code.equals("0")) {  //資料錯誤
                        Log.w("測試firebase", "到login failed" + "帳號：" + user_id + "密碼：" + password + "login_code:" + login_code);
                        result += "Login failed";
                    }


                }
                bufferedReader.close(); //關閉該stream並釋放與之關聯的所有系統資源
                inputStream.close(); //關閉該InputStream並釋放與該流關聯的所有系統資源
                httpURLConnection.disconnect(); //關閉連線

                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (type.equals("signup")){

            try {
                String name = params[1];
                String password = params[2];
                String email = params[3];
                String id = params[4];
                String sex = params[5];
                URL url  = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("usr_id","UTF-8")+"="+URLEncoder.encode(id, "UTF-8")
                        +"&"+URLEncoder.encode("usr_name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")
                        +"&"+URLEncoder.encode("usr_password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")
                        +"&"+URLEncoder.encode("usr_email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")
                        +"&"+URLEncoder.encode("usr_sex", "UTF-8")+"="+URLEncoder.encode(sex, "UTF-8");  //資料POST的格式
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";

                uploadLocation upload = new uploadLocation();
                assignGroupID assignGID = new assignGroupID();

                while((signup_code = bufferedReader.readLine()) != null){
                    if(signup_code.equals("1")) { //資料正確

                        //Erika 2018.10.18 first sign up upload default usrLocation: unknown
                        //Log.d("backgroundWork.java","upload.execute(id, unknown)");
                        upload.execute(id, "unknown");
                        assignGID.execute(id, "NULL");
                        result += "Signup  success";
                    }
                    else  //資料錯誤
                        result+="signup  failed";
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }





        return  null;


    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(aVoid);


        //Toast.makeText(context, result, Toast.LENGTH_LONG).show(); //以Toast顯示結果(登入成功/失敗、註冊成功/失敗)
        if(result.equals("Login success")) { //若登入成功則結束此context

            Log.w("測試","1");
            mFirestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            FMS = new MyFirebaseMessagingService(context);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String user_id = SaveSharedPreference.getID(context);
            SaveSharedPreference.setLog(context, true);

            //FirebaseInstanceId.getInstance().getInstanceId();
            //String tokenID = FMS.getToken(context);
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("Background", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String uid = mAuth.getUid();
                            String token = task.getResult().getToken();

                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("tokenID", token);
                            tokenMap.put("uid",uid);
                            Log.w("測試","2");
                            mFirestore.collection("Users").document(user_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.w("放上雲端", "取得token  " + token + "讚讚");
                                    Intent intent = new Intent();
                                    intent.setClass(context, MainActivity.class);
                                    ((Activity) context).finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }






                            });
                            //SaveSharedPreference.setToken(context, token);
                        }
                    });



                    //String tokenID = SaveSharedPreference.getToken(context);
                    //Map<String, Object> tokenMap = new HashMap<>();
                    //tokenMap.put("tokenID", tokenID);
            //Log.w("Backgroundwork", "取得token  "+tokenID+"讚讚");
                    /*mFirestore.collection("Users").document(user_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ((Activity) context).finish();
                        }
                    });*/
                }

            //((Activity) context).finish();

        if(result.equals("Signup  success")) { //若註冊成功則結束此context
            ((Activity) context).finish();
        }

    }
}
