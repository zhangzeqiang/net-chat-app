package com.shuanghua.chat.client.activity;

import android.os.Bundle;
import android.util.Log;

import com.shuanghua.chat.client.SHApplication;
import com.shuanghua.chat.common.receiver.TCPReceiver;
import com.shuanghua.chat.common.service.TCPService;
import com.shuanghua.chat.common.ui.BaseChatFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.chat.OnOperationListener;
import org.kymjs.chat.adapter.ChatAdapter;
import org.kymjs.chat.bean.Emojicon;
import org.kymjs.chat.bean.Faceicon;
import org.kymjs.chat.bean.Message;
import org.kymjs.chat.emoji.DisplayRules;

import java.util.Date;

/**
 * Created by ZJH on 2016/2/22.
 */
public class ChatFragment extends BaseChatFragment {

    TCPReceiver.OnTCPReceiverListener onTCPReceiverListener;

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        onTCPReceiverListener = new TCPReceiver.OnTCPReceiverListener() {
            @Override
            public void getJsonString(String json) {
                String msg = "";
                try {
                    Log.v ("json", json);
                    JSONObject jsonObject = new JSONObject(json);

                    String s_action = jsonObject.getString("action");
                    if (s_action.equals("Register")) {
                        // 注册返回后发送通知抢单请求
                        String s_errcode = jsonObject.getString("errcode");
                        if (s_errcode.equals("1")) {    // 登录成功
                            msg = "登录成功";
                        } else {
                            msg = "登录失败";
                        }
                    } else if (s_action.equals("Acknowledge")) {
                        // 正在请求客服
                        String s_errcode =jsonObject.getString ("errcode");
                        if (s_errcode.equals("1")) {
                            // 未有客服提供服务
                            msg = "请耐心等待,正在为你寻找客服... ";
                        } else if (s_errcode.equals("2")){
                            // 无客服在线
                            msg = "亲~,客服都不在线... ";
                        }
                    } else if (s_action.equals("Send")) {
                        // 消息交流
                        String s_errcode = jsonObject.getString("errcode");
                        if (s_errcode.equals("1")) {
                            // 成功发送
                        } else if (s_errcode.equals("2")){
                            // 有新消息
                            msg = jsonObject.getJSONObject("data").getString("msg");
                        }
                    } else if (s_action.equals("Bind")) {
                        // 抢单提示
                        String s_errcode = jsonObject.getString("errcode");
                        if (s_errcode.equals("1")) {
                            // 有新客服接单
                            String serverid = jsonObject.getJSONObject("data").getString("serverid");
                            msg = "客服"+serverid+",为你服务.";
                        } else if (s_errcode.equals("2")) {
                            // 客服挂线
                            msg = "欢迎下次光临,本次服务完成.";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 网络数据到达
                if (!msg.equals("")) {
                    Message message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                            "Tom", "avatar", "Jerry",
                            "avatar", msg, false, true, new Date());
                    uiHolder.datas.add(message);
                    uiHolder.adapter.refresh(uiHolder.datas);
                }
            }
        };
        startTCPReceiver();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopTCPReceiver();
    }

    private void startTCPReceiver () {
        // 添加监听
        SHApplication.getInstance().addOnTCPReceiverListener(onTCPReceiverListener);
    }

    private void stopTCPReceiver () {
        // 移除监听
        SHApplication.getInstance().rmOnTcpReceiverListener(onTCPReceiverListener);
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
                    String jsonSend = "{\"route\":\"Send\",\"class\":0,\"msg\":\""+content+"\"}";
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
                public void selectedEmoji(Emojicon emoji) {
                    uiHolder.box.getEditTextBox().append(emoji.getValue());
                }

                @Override
                public void selectedBackSpace(Emojicon back) {
                    DisplayRules.backspace(uiHolder.box.getEditTextBox());
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

        void getJsonString (String json);
    }

    public static interface OnTCPActionListener {

        TCPService getTCPService ();
    }
}
