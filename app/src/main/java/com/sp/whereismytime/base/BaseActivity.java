package com.sp.whereismytime.base;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.sp.whereismytime.KeepAliveActivity;
import com.sp.whereismytime.adapter.DBHelper;
import com.sp.whereismytime.model.OneDayInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Administrator on 2017/3/12.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    ScreenObserver observer;

    private Context context=this;

    private LocalBroadcastManager localBroadcastManager;

    private volatile static int count=0;//解锁次数

    private volatile String starttime,endtime,newstart,firstopentime;

    private volatile int year,month,date;

    private  static ArrayList<String> TimeUseCount=new ArrayList<String>();

    private DBHelper dbHelper;

    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.log(TAG,"oncreate");
        super.onCreate(savedInstanceState);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        //初始化DBHelper
        dbHelper=new DBHelper(this);
        cursor=dbHelper.select();
        observer=new ScreenObserver(this);
        observer.startObserver(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                LogUtil.log(TAG,"ON!!!!!!!!!!!!!!!!!!!!!!!");
                firstopentime=getCurrentTime();
                if (starttime==null){
                    starttime=getCurrentTime();
                }
            }
            @Override
            public void onScreenOff() {

                if (starttime!=null){
                    endtime=getCurrentTime();
                }
                TimeUseCount.add("from: "+starttime+" to: "+endtime);
                LogUtil.log(TAG,"OFF!!!!!!!!!!!!!!!!!!!!!!!"+"  this use:"+starttime+"  "+endtime+"  size"+TimeUseCount.size());
                KeepAliveActivity.Start(context);
            }
            @Override
            public void onUserPresent() {
                count++;
                starttime=getCurrentTime();
                LogUtil.log(TAG,"UserPresent!!!!!!!!!!!!!!!!!!!!!!!"+TimeUseCount.size());
            }
            @Override
            public void onNewDayCome() {
                OneDayInfo info=new OneDayInfo();
                setDate();
                LogUtil.log(TAG,"new day come"+year+month+date);
                info.setDate(year+"年"+month+"月"+date+"日");
                info.setCount(count);
                dbHelper.insert(info);
                cursor.requery();
                count=0;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        observer.shutdownObserver();
    }
    //获取系统当前时间
    private String getCurrentTime(){
        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss");
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
        return starttime;
    }
    //设定前一天的日期
    protected void setDate(){
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        date=calendar.get(Calendar.DATE);
        if (date==1){
            if (month!=1){
                month--;
                if (month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    date=31;
                }else if (month==4||month==6||month==9||month==11){
                    date=30;
                }else if (month==2){
                    if (year%4==0&&(year%100!=0||year%400==0)){
                        date=29;
                    }else {
                        date=28;
                    }
                }
            }else {
                year--;
                month=12;
                date=31;
            }
        }else {
            date--;
        }
    }
}
