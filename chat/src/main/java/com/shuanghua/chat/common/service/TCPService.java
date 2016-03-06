package com.shuanghua.chat.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.shuanghua.chat.common.Constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by ZJH on 2016/3/2.
 */
public class TCPService extends Service {

    @Override
    public void onCreate () {
        super.onCreate();
    }

    @Override
    public IBinder onBind (Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public TCPService getService () {
            return TCPService.this;
        }
    }

    private final IBinder binder = new MyBinder ();

    Socket socket;
    /**
     * 启动tcp监听(接收数据,数据到达时广播)
     */
    public int connect (final String ip, final int port) {

        // 连接
        new Thread(new Runnable () {

            @Override
            public void run() {
                try {// 创建一个Socket对象，并指定服务端的IP及端口号
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), 5000);

                    // 接听接收数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            receive(port);
                        }
                    }).start();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return 0;
    }

    /**
     * 启动tcp监听(接收数据,数据到达时广播)
     */
    public int connect (final String ip, final int port, final OnTCPListener onTCPListener) {

        // 连接
        new Thread(new Runnable () {

            @Override
            public void run() {
                try {// 创建一个Socket对象，并指定服务端的IP及端口号
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), 5000);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (onTCPListener != null) {
                                onTCPListener.onConnect();
                            }
                        }
                    }).start();

                    // 接听接收数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            receive(port);
                        }
                    }).start();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return 0;
    }

    /**
     * 关闭socket
     */
    public int close () {

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 接收报文
     */
    public void receive (int port) {
        try {
            //实例化的端口号要和发送时的socket一致，否则收不到data
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    "UTF-8"));
            // 读取每一行的数据.注意大部分端口操作都需要交互数据。
            while (true) {
                char[] buff = new char[1024*10];

                int size = rd.read(buff);

                if (size >= 0) {
                    String result = new String(buff, 0, size);
                    // 发送广播
                    Log.v("TCPService", "接收到:" + result);
                    String[] arr_json = result.split("\\^SHU\\^");

                    for (int i=0;i<arr_json.length;i++) {
                        if (!arr_json[i].equals("")) {  // 不为空
                            broadCast(arr_json[i]);
                        }
                    }
                } else {
                    break;
                }
            }
            rd.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送tcp数据报(启动监听后)
     */
    public int send (String buf) {

        // ^SHD^为数据报头
        buf = "^SHD^"+buf;
        class MyRunnable implements Runnable {

            String buff;
            MyRunnable (String buf) {
                buff = buf;
            }

            @Override
            public void run() {
                try {
                    /** 或创建一个报文，使用BufferedWriter写入,看你的需求 **/
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream()));
                    writer.write(buff);
                    writer.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Runnable runnable = new MyRunnable(buf);
        new Thread(runnable).start();
        return 0;
    }

    protected void broadCast (String buff) {
        Intent intent = new Intent();
        intent.setAction(Constant.TCPRECEIVERINTENT);
        intent.putExtra("json", buff);
        sendBroadcast (intent);
    }

    public static interface OnTCPListener {

        public void onConnect();
    }
}
