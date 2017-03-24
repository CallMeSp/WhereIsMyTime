package com.sp.whereismytime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Administrator on 2017/3/23.
 */

public class ShowHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showhistoryactivity_main);
    }

    public static void Start(Context context){
        Intent intent=new Intent(context,ShowHistoryActivity.class);
        context.startActivity(intent);
    }
}
