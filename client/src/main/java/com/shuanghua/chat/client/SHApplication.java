package com.shuanghua.chat.client;

/**
 * Created by yinhaide on 2016/2/22.
 */

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.shuanghua.chat.client.activity.ChatFragment;
import com.shuanghua.chat.common.Constant;
import com.shuanghua.chat.common.receiver.TCPReceiver;
import com.shuanghua.chat.common.service.TCPService;

import java.util.ArrayList;
import java.util.List;

public class SHApplication extends Application implements ChatFragment.OnTCPActionListener{

    public static Context applicationContext;
    private static SHApplication instance;
    private TCPService tcpService;
    private TCPReceiver tcpReceiver;
    private List<TCPReceiver.OnTCPReceiverListener> onTCPReceiverListenList;;

    private ServiceConnection tcpConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            tcpService = ((TCPService.MyBinder) iBinder).getService();
            tcpService.connect("192.168.1.68", 8888, new TCPService.OnTCPListener() {
                @Override
                public void onConnect() {
                    // 连接上就登陆
                    String userid = System.currentTimeMillis()+"";     // 以当前
                    String jsonSend = "{\"route\":\"Register\",\"id\":\""+userid+"\",\"class\":0}";
                    SHApplication.getInstance().getTCPService().send(jsonSend);
                }
            });
            Log.e("SHApplication","ServiceConnection");
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            tcpService = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        onTCPReceiverListenList = new ArrayList<>();
        bindTcpService ();
        startTCPReceiver ();
    }

    public void addOnTCPReceiverListener (TCPReceiver.OnTCPReceiverListener listener) {
        this.onTCPReceiverListenList.add(listener);
    }

    public void rmOnTcpReceiverListener (TCPReceiver.OnTCPReceiverListener listener) {
        this.onTCPReceiverListenList.remove(listener);
    }

    private void startTCPReceiver () {
        IntentFilter filter = new IntentFilter();
        filter.addAction (Constant.TCPRECEIVERINTENT);
        tcpReceiver = new TCPReceiver(new TCPReceiver.OnTCPReceiverListener() {
            @Override
            public void getJsonString(String json) {

                for (TCPReceiver.OnTCPReceiverListener listener: onTCPReceiverListenList) {
                    listener.getJsonString(json);
                }
            }
        });
        registerReceiver(tcpReceiver, filter);
    }

    private void bindTcpService () {
        Intent bindIntent = new Intent (applicationContext, TCPService.class);
        bindIntent.setAction(Constant.TCPSERVERINTENT);
        bindService(bindIntent, tcpConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindService () {

        unbindService(tcpConnection);
    }

    public static SHApplication getInstance() {
        return instance;
    }

    @Override
    public TCPService getTCPService() {

        return tcpService;
    }

}
