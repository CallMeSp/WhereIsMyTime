package com.sp.whereismytime.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.sp.whereismytime.KeepAliveActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Administrator on 2017/3/12.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    ScreenObserver observer;
    private Context context=this;
    private LocalBroadcastManager localBroadcastManager;
    protected static int count=0;//解锁次数
    String starttime,endtime,newstart,firstopentime;
    ArrayList<String> TimeUseCount=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.log(TAG,"oncreate");
        super.onCreate(savedInstanceState);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        observer=new ScreenObserver(this);
        observer.startObserver(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                LogUtil.log(TAG,"ON!!!!!!!!!!!!!!!!!!!!!!!");
                firstopentime=getCurrentTime();
            }
            @Override
            public void onScreenOff() {
                LogUtil.log(TAG,"OFF!!!!!!!!!!!!!!!!!!!!!!!");
                if (starttime!=null){
                    endtime=getCurrentTime();
                }
                KeepAliveActivity.Start(context);
            }
            @Override
            public void onUserPresent() {
                count++;
                if (newstart!=null){
                    starttime=newstart;
                }else {
                    starttime=getCurrentTime();
                }

                newstart=getCurrentTime();

                if (endtime==null){

                }else {
                    TimeUseCount.add("from:"+starttime+" to:"+endtime);
                }

                LogUtil.log(TAG,"UserPresent!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        observer.shutdownObserver();
    }
    private String getCurrentTime(){
        SimpleDateFormat formatter=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
    //获取解锁次数
    protected int getCount(){
        return count;
    }
    //使用时间列表
    protected ArrayList<String> get(){
        return TimeUseCount;
    }
    //本次使用的开始时间
    protected String getThisUseStartTime(){
        return newstart==null?firstopentime:newstart;
    }

}
