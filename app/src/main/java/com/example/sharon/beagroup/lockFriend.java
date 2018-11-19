package com.example.sharon.beagroup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;



public class lockFriend extends AppCompatActivity{

    EditText searchID;
    TextView textView;
    String[] UserInfo = new String[3];
    String result="";
    CircleImageView circleImageView;
    String group_id;
    Button buttonLock;
    String friendCheckCode;
    Button button;
    private String uid;
    private FirebaseFirestore mFirestore;
    private DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_friend);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        searchID = (EditText) findViewById(R.id.searchText);
        textView = (TextView)findViewById(R.id.searchName);
        circleImageView = (CircleImageView)findViewById(R.id.searchImage);
        mFirestore = FirebaseFirestore.getInstance();
        button = (Button)findViewById(R.id.searching);

        group_id = SaveSharedPreference.getGroup_ID(this);
        buttonLock = (Button)findViewById(R.id.button_lock);


        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String findID = searchID.getText().toString();
                PushNotification pushNotification = new PushNotification(lockFriend.this);
                pushNotification.onPush(findID);
            }
        });

        String dataFrom = getIntent().getStringExtra("from_user_id");
        /*if(!dataFrom.equals("")){
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(this)
                            .setTitle("確認邀請")
                            .setMessage("是否同意邀請")
                            .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("拒絕", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

            // Show the AlertDialog.
            AlertDialog alertDialog = alertDialogBuilder.show();
        }*/

    }

    public void openJSON(View view){

        Log.d("測試lockFriend", "");
        getJSON("http://140.113.73.42/search.php");
    }


    public void getJSON(final String urlWebService){
        Log.d("測試lockFriend", "到getJSON");
        MyApplication myApplication = (MyApplication) getApplicationContext();
        class  GetJSON extends AsyncTask<Void, Void, String>{

            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(account.this);
            //String currentUser = preferences.getString("USER", "null");
            String IDText = searchID.getText().toString();

            //checkFriendID checkFID = new checkFriendID();
            //MyApplication myApplication = (MyApplication) getApplicationContext();



            @Override
            protected void onPreExecute() {
                Log.d("測試lockFriend", "到onPreExecute");
                super.onPreExecute();
                String IDText = searchID.getText().toString();
                checkFriendID checkFID = new checkFriendID(lockFriend.this);
                checkFID.execute(group_id, IDText);
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("測試lockFriend", "到onPostExecute");
                super.onPostExecute(s);
                try {
                    Log.d("測試lockFriend", "到try");
                    loadIntoView(s);
                    //checkFID.execute(group_id, IDText);
                    Log.d("lockFriend.java","checkFID.execute()");
                    //String isFriend = myApplication.getFriendCheck();
                    String isFriend = SaveSharedPreference.getFriendCheckCode(getApplicationContext());
                    if (isFriend != ""){
                        if (isFriend == "1"){
                            Log.d("lockFriend", IDText + " is already a friend.");
                        }else{
                            Log.d("lockFriend", IDText + " is not a friend."+isFriend+"test");
                        }
                    }else{
                        Log.d("lockFriend", "friendCheck is null"+isFriend+"test");
                    }
                    //myApplication.setFriendCheck("");
                    SaveSharedPreference.setFriendCheckCode(lockFriend.this, "");
                    Log.d("lockFriend", "after friendCheck, setting FriendCheckCode to default value: "+ SaveSharedPreference.getFriendCheckCode(lockFriend.this));

                }catch (JSONException e){
                    //e.printStackTrace();
                    Log.d("測試lockFriend", "到catch");
                    textView.setText("ID錯誤");
                    buttonLock.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getApplicationContext(), "ID錯誤", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            protected String doInBackground(Void... voids) {

                try{
                    Log.d("測試lockFriend", "到doInBackground");
                    URL url = new URL(urlWebService);           //creating a URL
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();           //Opening the URL using HttpURLConnection
                    httpURLConnection.setRequestMethod("POST"); //以POST傳送
                    httpURLConnection.setDoInput(true); //是否向httpURLConnection輸出:true
                    httpURLConnection.setDoOutput(true); //是否從httpURLConnection讀入:true
                    OutputStream outputStream = httpURLConnection.getOutputStream(); //得到subprocess的輸出流
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(IDText, "UTF-8");
                    StringBuilder sb = new StringBuilder();         //StringBuilder object to read the string from the service
                    bufferedWriter.write(post_data);//將post_data寫入緩衝區
                    bufferedWriter.flush(); //刷新該stream中的緩衝，將缓衝數據寫到目的文件中(login.php)
                    bufferedWriter.close(); //關閉stream
                    outputStream.close(); //關閉此outputStream並釋放與此stream有關的所有系統資源
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));            //use a buffered reader to read the string from service
                    String json;            //A simple string to read values from each line

                    while ((json = bufferedReader.readLine()) != null) {            //reading until we don't find null

                        //appending it to string builder

                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();            //creating asynctask object and executing it
        getJSON.execute();
    }

    //將JSON的值存入array
    private void loadIntoView(String json) throws JSONException{
        Log.d("測試lockFriend", "loadIntoView");

        //buttonLock.setVisibility(View.VISIBLE);
        showFollowingButton();
        String gender = "";
        if(!json.equals("")) {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < 2; i++) {

                JSONObject obj = jsonArray.getJSONObject(0);
                if (i == 0) {
                    UserInfo[i] = obj.getString("name");
                } else if (i == 1) {
                    UserInfo[i] = obj.getString("sex");
                    gender = UserInfo[1];
                }
            }
            textView.setText(UserInfo[0]);
            if(gender.equals("F"))
                circleImageView.setImageResource(R.drawable.girl);
            else if(gender.equals("M"))
                circleImageView.setImageResource(R.drawable.boy);
        }
        Log.d("測試lockFriend", "loadIntoView最後");



        /*if(!result.equals("error")){
            textView.setText("HIhi"+UserInfo[0]);
        }*/
    }
    public void showFollowingButton(){
        //Erika 2018.10.19 追蹤按鈕(目前無邀請功能，直接追蹤)，文字可視整合過任意修改。
        Log.d("測試lockFriend", "到showfFollowingButton");
        friendCheckCode = SaveSharedPreference.getFriendCheckCode(getApplicationContext());
        if (friendCheckCode == "1"){
            Log.d("測試lockFriend", "到if");
            buttonLock.setVisibility(View.VISIBLE);
            buttonLock.setText(R.string.following);//已追蹤
            buttonLock.setClickable(false);//希望可以做到按鈕便灰色的，比較好辨識
        }else{
            Log.d("測試lockFriend", "到else");
            buttonLock.setVisibility(View.VISIBLE);
            buttonLock.setText(getString(R.string.follow));//開始追蹤
            buttonLock.setClickable(true);
        }
    }

    public void lockFriend(View view){
        String IDText = searchID.getText().toString();
        InsertFriendList insertFriendList = new InsertFriendList();
        insertFriendList.execute(group_id, IDText);
        buttonLock.setVisibility(View.VISIBLE);
        buttonLock.setText(R.string.following);//已追蹤
        buttonLock.setClickable(false);//希望可以做到按鈕便灰色的，比較好辨識
    }
}


