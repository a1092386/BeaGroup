package com.example.sharon.beagroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String Item[];
    String SubItem[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] item, String[] subItem) {
        this.context = applicationContext;
        this.Item = item;
        this.SubItem = subItem;
        Log.w("測試account","到建構子");
        inflter = (LayoutInflater.from(applicationContext));
        Log.w("測試","到inflter下");
    }

    @Override
    public int getCount() {
        Log.w("測試account","到getCount");
        return Item.length;
    }

    @Override
    public Object getItem(int position) {
        Log.w("測試account","到getItem");
        return null;
    }

    @Override
    public long getItemId(int position) {
        Log.w("測試account","到getItemID");
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Log.w("測試account","到getView");
        view =inflter.inflate(R.layout.activity_account_item, null);
        TextView item = (TextView) view.findViewById(R.id.item);
        TextView subitem = (TextView) view.findViewById(R.id.subitem);
        item.setText(Item[i]);
        subitem.setText(SubItem[i]);
        return view;
    }
}
