package com.example.sharon.beagroup;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context Context) {
        Context = mContext;
    }
}
