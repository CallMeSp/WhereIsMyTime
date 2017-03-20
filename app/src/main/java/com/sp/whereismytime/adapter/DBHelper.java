package com.sp.whereismytime.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sp.whereismytime.base.LogUtil;
import com.sp.whereismytime.model.OneDayInfo;

/**
 * Created by Administrator on 2017/3/20.
 */

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase=getWritableDatabase();

    private static final String TAG = "DBHelper";

    private static final String DB_NAME="HistoryUse.db";

    private static final String TABLE_NAME="myHistory";

    private static final int DB_VERSION=1;
    public DBHelper(Context context) {//(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.log(TAG,"onCreate()");
        db.execSQL("create table "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,date TEXT,count INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(OneDayInfo info){
        LogUtil.log(TAG,"insert");
        ContentValues contentValues=new ContentValues();
        contentValues.put("date",info.getDate());
        contentValues.put("count",info.getCount());
        long row=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return row;
    }

    public Cursor select(){
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME,null,null,null,null,null,null);
        return cursor;
    }

}
