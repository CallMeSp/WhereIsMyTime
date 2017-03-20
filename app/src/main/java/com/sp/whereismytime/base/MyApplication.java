package com.sp.whereismytime.base;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by my on 2016/12/22.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
    }
}
