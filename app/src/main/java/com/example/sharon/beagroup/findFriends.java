package com.example.sharon.beagroup;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class findFriends extends AppCompatActivity {

    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        listView = (ListView)findViewById(R.id.friendList);
        textView = (TextView)findViewById(R.id.empty);
        getJSON("http://140.113.73.42/getFriend.php");
    }

    private void getJSON(final String urlWebService){
        class  GetJSON extends AsyncTask<Void, Void, String> {
            String groupID = SaveSharedPreference.getGroup_ID(findFriends.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoView(s);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try{
                    URL url = new URL(urlWebService);           //creating a URL
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();           //Opening the URL using HttpURLConnection
                    httpURLConnection.setRequestMethod("POST"); //以POST傳送
                    httpURLConnection.setDoInput(true); //是否向httpURLConnection輸出:true
                    httpURLConnection.setDoOutput(true); //是否從httpURLConnection讀入:true
                    OutputStream outputStream = httpURLConnection.getOutputStream(); //得到subprocess的輸出流
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("group_id", "UTF-8")+"="+URLEncoder.encode(groupID, "UTF-8");
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
        JSONArray jsonArray = new JSONArray(json);
        int count = jsonArray.length();
        String  Sex[] = new String[count];
        String[] Name = new String[count];
        String[] ID = new String[count];
        String[] Block = new String[count];
        int[] Photo = new int [count];


        if(count==0){
            textView.setText("您尚未綁定好友");
        }
        else{
        JSONObject obj;
        for(int i = 0; i<count; i++){
            obj = jsonArray.getJSONObject(i);
            Sex[i] = obj.getString("sex");
            if(Sex[i].equals("F"))
                Photo[i] = R.drawable.girl;
            else
                Photo[i] = R.drawable.boy;
            Name[i] = obj.getString("name");
            ID[i] = "ID : "+obj.getString("friend_ID");
            Block[i] = obj.getString("Block");
        }


        FriendAdapter friendAdapter = new FriendAdapter(getApplicationContext(), Photo, Name, ID, Block);
        Log.w("測試friend","在findFriend");
        listView.setAdapter(friendAdapter);}

        //listView.setOnItemClickListener(onClickListView);
    }

}
