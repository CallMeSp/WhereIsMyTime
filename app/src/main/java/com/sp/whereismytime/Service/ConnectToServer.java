package com.sp.whereismytime.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sp.whereismytime.base.Constants;
import com.sp.whereismytime.base.LogUtil;
import com.sp.whereismytime.model.OneDayInfo;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/3/27.
 */

public class ConnectToServer extends Service {
    private Binder binder=new MyBinder();
    private static final String TAG = "ConnectToServer";
    private Socket socket=null;
    private ServiceListener serviceListener;
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            LogUtil.log(TAG,"start connect to server");
            socket= IO.socket(Constants.ipAddress);
            socket.connect();
            //while (!socket.connected()){}
            //LogUtil.log(TAG,"isconnect:"+socket.connected());
        } catch (URISyntaxException e) {
            LogUtil.log(TAG,e.toString());
            e.printStackTrace();
        }
        socket.on("synchronizeToClient", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtil.log(TAG,"接收到一条同步数据:"+(int)args[0]);
                serviceListener.updateCount((int)args[0]);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class MyBinder extends Binder{
        public void synchronizeToServere(OneDayInfo info){
            LogUtil.log(TAG,"SynchronizeToServere"+socket.connected());
            socket.emit("updateInDb","pnotexit",info.getCount(),info.getDate());
        }
        public void synchronizeToClient(OneDayInfo oneDayInfo,final ServiceListener listener){
            socket.emit("queryFromServer","pnotexit",oneDayInfo.getCount(),oneDayInfo.getDate());
            serviceListener=listener;
        }

    }
}
