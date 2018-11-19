package com.example.sharon.beagroup;

import android.support.constraint.ConstraintLayout;

import android.content.Context;

public abstract class PageView extends ConstraintLayout {
    public PageView(Context context) {
        super(context);
    }
    public abstract void refresh();
}
