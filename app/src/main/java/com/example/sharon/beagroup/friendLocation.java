package com.example.sharon.beagroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class friendLocation extends PageView{
    public friendLocation(Context context){
        super(context);
        Log.d("測試find","到friendLocation");
        View view = LayoutInflater.from(context).inflate(R.layout.friend_location, null);
        TextView textView = (TextView) view.findViewById(R.id.friend1);
        textView.setText("好友位置");
        Log.d("測試find","到好友位置下");
        addView(view);
    }

    @Override
    public void refresh() {

    }
}
