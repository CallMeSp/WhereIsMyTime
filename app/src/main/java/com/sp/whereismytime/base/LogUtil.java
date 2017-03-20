package com.sp.whereismytime.base;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/21.
 */

public class LogUtil {
    public static void log(String...strings){
        int x=strings.length;
        for (int i=1;i<x;i++){
            Log.e(strings[0],strings[i] );
        }
    }
}
