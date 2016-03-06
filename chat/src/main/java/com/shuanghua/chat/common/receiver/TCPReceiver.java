package com.shuanghua.chat.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shuanghua.chat.common.Constant;

/**
 * Created by ZJH on 2016/3/2.
 */
public class TCPReceiver extends BroadcastReceiver {

    OnTCPReceiverListener onTCPReceiverListener;

    public TCPReceiver () {
        onTCPReceiverListener = new OnTCPReceiverListener() {
            @Override
            public void getJsonString(String json) {

            }
        };
    }

    public TCPReceiver (OnTCPReceiverListener onTCPReceiverListener) {
        this.onTCPReceiverListener = onTCPReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Constant.TCPRECEIVERINTENT)) {
            String json = intent.getStringExtra("json");
            Log.v("Receiver", json);
            onTCPReceiverListener.getJsonString(json);
        }
    }

    public static interface OnTCPReceiverListener {

        void getJsonString(String json);
    }
}
