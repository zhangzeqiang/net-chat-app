package com.shuanghua.chat.server;

/**
 * Created by yinhaide on 2016/2/22.
 */

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.shuanghua.chat.server.fragment.ChatFragment;
import com.shuanghua.chat.common.Constant;
import com.shuanghua.chat.common.service.TCPService;

public class SHApplication extends Application implements ChatFragment.OnTCPActionListener {

    public static Context applicationContext;
    private static SHApplication instance;
    private TCPService tcpService;

    private ServiceConnection tcpConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            tcpService = ((TCPService.MyBinder) iBinder).getService();
            tcpService.connect("192.168.1.68", 8888, new TCPService.OnTCPListener () {

                @Override
                public void onConnect() {

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
        bindTcpService ();
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
