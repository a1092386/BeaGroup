package com.example.sharon.beagroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class friendList extends PageView{
    public friendList(Context context){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list, null);
        //TextView textView = (TextView) view.findViewById(R.id.friend2);
        //textView.setText("好友列表");
        addView(view);
    }

    @Override
    public void refresh() {

    }
}
