package com.sp.whereismytime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Administrator on 2017/3/23.
 */

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity_main);
    }

    public static void Start(Context context){
        Intent intent=new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }
}
