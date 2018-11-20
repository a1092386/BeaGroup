package com.example.sharon.beagroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;

public class FriendAdapter extends BaseAdapter{
    Context context;
    int Photo[];
    //String Sex[];
    String Name[];
    String ID[];
    String Block[];


    int count;
    TextView name, id, blcok;
    ImageView photo;
    LayoutInflater inflter;

    public FriendAdapter(Context applicationContext, int[] Photo , String[] Name, String[] ID, String[] Block) {
        this.context = applicationContext;
        this.Photo = Photo;
        this.Name = Name;
        this.ID = ID;
        this.Block = Block;
        //this.count = count;
        inflter = (LayoutInflater.from(applicationContext));


    }

    @Override
    public int getCount() {
        return Name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view =inflter.inflate(R.layout.friend_list, null);
        photo = (ImageView) view.findViewById(R.id.photo);
        name = (TextView) view.findViewById(R.id.name);
        id = (TextView) view.findViewById(R.id.id);
        blcok = (TextView) view.findViewById(R.id.block);
        photo.setImageResource(Photo[i]);
        name.setText(Name[i]);
        id.setText(ID[i]);
        blcok.setText(Block[i]);
        return view;
    }
}

