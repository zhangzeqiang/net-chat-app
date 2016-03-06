package com.shuanghua.chat.server.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.shuanghua.chat.common.Constant;
import com.shuanghua.chat.common.receiver.TCPReceiver;
import com.shuanghua.chat.common.service.TCPService;
import com.shuanghua.chat.server.SHApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.chat.OnOperationListener;
import org.kymjs.chat.adapter.ChatAdapter;
import org.kymjs.chat.bean.Emojicon;
import org.kymjs.chat.bean.Faceicon;
import org.kymjs.chat.bean.Message;

import java.util.Date;

/**
 * Created by ZJH on 2016/2/22.
 */
public class ChatFragment extends BaseChatFragment {

    private TCPReceiver tcpReceiver;

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        startTCPReceiver();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopTCPReceiver();
    }

    private void startTCPReceiver () {
        IntentFilter filter = new IntentFilter();
        filter.addAction (Constant.TCPRECEIVERINTENT);
        tcpReceiver = new TCPReceiver(new TCPReceiver.OnTCPReceiverListener() {

            @Override
            public void getJsonString(String json) {
                try {
                    Log.v ("json", json);
                    JSONObject jsonObject = new JSONObject(json);

                    String s_action = jsonObject.getString("action");
                    if (s_action.equals("Register")) {
                        // 注册返回后发送通知抢单请求
                        String jsonSend = "{\"route\":\"Acknowledge\",\"from\":\"111\",\"class\":1,\"msg\":\""+"Test"+"\"}";
                        SendJson(jsonSend);
                    } else if (s_action.equals("Send")) {
                        // 消息交流
                    }
                    Log.v("Action", s_action);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 网络数据到达
                Message message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                        "Tom", "avatar", "Jerry",
                        "avatar", json, false, true, new Date());
                uiHolder.datas.add(message);
                uiHolder.adapter.refresh(uiHolder.datas);
            }
        });
        this.getActivity().registerReceiver(tcpReceiver, filter);
    }

    private void stopTCPReceiver () {
        this.getActivity().unregisterReceiver(tcpReceiver);
    }

    @Override
    protected void initListView() {
        byte[] emoji = new byte[]{
                (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81
        };
        /*Message message = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SUCCESS, "\ue415", "http://a0.att.hudong.com/58/59/19300001173647130466595655170.jpg", "Jerry", "http://a0.att.hudong.com/58/59/19300001173647130466595655170.jpg",
                new String(emoji), false, true, new Date(System.currentTimeMillis()
                - (1000 * 60 * 60 * 24) * 8));
        Message message1 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SUCCESS, "Tom", "http://img1.imgtn.bdimg.com/it/u=3267475150,3549642943&fm=21&gp=0.jpg", "Jerry", "http://img1.imgtn.bdimg.com/it/u=3267475150,3549642943&fm=21&gp=0.jpg",
                "以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",
                true, true, new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 8));
        Message message2 = new Message(Message.MSG_TYPE_PHOTO,
                Message.MSG_STATE_FAIL, "Tom", "http://a0.att.hudong.com/58/59/19300001173647130466595655170.jpg", "Jerry", "http://a0.att.hudong.com/58/59/19300001173647130466595655170.jpg",
                "http://static.oschina.net/uploads/space/2015/0611/103706_rpPc_1157342.png",
                false, true, new Date(
                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 7));
        Message message6 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_FAIL, "Tom", "http://img1.imgtn.bdimg.com/it/u=3267475150,3549642943&fm=21&gp=0.jpg", "Jerry", "http://img1.imgtn.bdimg.com/it/u=3267475150,3549642943&fm=21&gp=0.jpg",
                "test send fail", true, false, new Date(
                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 6));
        Message message7 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SENDING, "Tom", "http://img1.imgtn.bdimg.com/it/u=3267475150,3549642943&fm=21&gp=0.jpg", "Jerry", "http://img1.imgtn.bdimg.com/it/u=3267475150,3549642943&fm=21&gp=0.jpg",
                "<a href=\"http://kymjs.com\">自定义链接</a>也是支持的", true, true, new Date(System.currentTimeMillis()
                - (1000 * 60 * 60 * 24) * 6));

        uiHolder.datas.add(message);
        uiHolder.datas.add(message1);
        uiHolder.datas.add(message2);
        uiHolder.datas.add(message6);
        uiHolder.datas.add(message7);*/

        uiHolder.adapter = new ChatAdapter(
                getActivity(),
                uiHolder.datas,
                EventFactory.getOnChatItemClickListener(uiHolder, getActivity())
        );
        uiHolder.mRealListView.setAdapter(uiHolder.adapter);
    }

    /**
     * 初始化事件监听
     */
    protected void initListener () {
        if (uiHolder.onChatItemClickListener == null) {
            uiHolder.onChatItemClickListener = EventFactory.getOnChatItemClickListener(uiHolder, getActivity());
        }
        if(uiHolder.onOperationListener == null) {
            uiHolder.onOperationListener = new OnOperationListener() {
                @Override
                public void send(String content) {
                    // 发送
                    // String jsonSend = "{\"route\":\"Send\",\"class\":0,\"msg\":\""+content+"\"}";
                    //onTCPActionListener.getTCPService().send(jsonSend);

                    // String jsonSend = "{\"route\":\"Acknowledge\",\"class\":0,\"msg\":\""+content+"\"}";
                    String jsonSend = "{\"route\":\"Register\",\"from\":\"111\",\"class\":0,\"msg\":\""+content+"\"}";
                    SendJson(jsonSend);

                    Message message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                            "Tom", "avatar", "Jerry",
                            "avatar", content, true, true, new Date());
                    uiHolder.datas.add(message);
                    uiHolder.adapter.refresh(uiHolder.datas);
                }

                @Override
                public void selectedFace(Faceicon content) {

                }

                @Override
                public void selectedEmoji(Emojicon content) {

                }

                @Override
                public void selectedBackSpace(Emojicon back) {

                }

                @Override
                public void selectedFunction(int index) {

                }
            };
        }
    }

    private void SendJson (final String json) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SHApplication.getInstance().getTCPService().send(json);
            }
        }).start();
    }

    public static interface OnTCPReceiverListener {

        void getJsonString(String json);
    }

    public static interface OnTCPActionListener {

        TCPService getTCPService();
    }
}
