package com.example.sharon.beagroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class friendList extends PageView{
    public friendList(Context context){
        super(context);
        Log.d("測試find","到friendList");
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list, null);
        //TextView textView = (TextView) view.findViewById(R.id.friend2);
        //textView.setText("好友列表");
        Log.d("測試find","到好友列表下");
        addView(view);
    }

    @Override
    public void refresh() {

    }
}
