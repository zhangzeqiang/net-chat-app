package com.shuanghua.chat.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shuanghua.utils.tools.AlartWindows;

import org.kymjs.chat.OnOperationListener;
import org.kymjs.chat.R;
import org.kymjs.chat.adapter.ChatAdapter;
import org.kymjs.chat.bean.Emojicon;
import org.kymjs.chat.bean.Faceicon;
import org.kymjs.chat.bean.Message;
import org.kymjs.chat.emoji.DisplayRules;
import org.kymjs.chat.widget.KJChatKeyboard;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天窗口Fragment基类
 * Created by ZJH on 2016/2/22.
 */
abstract public class BaseChatFragment extends Fragment {

    public UiHolder uiHolder;

    public BaseChatFragment() {
        uiHolder = new UiHolder();
        uiHolder.datas = new ArrayList<Message>();
        uiHolder.onChatItemClickListener = null;
        uiHolder.onOperationListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        uiHolder.layout = inflater.inflate(R.layout.fragment_chat, container, false);
        initListener();
        initWidget();
        return uiHolder.layout;
    }

    /**
     * 初始化事件监听
     */
    protected void initListener () {
        if (uiHolder.onChatItemClickListener == null) {
            uiHolder.onChatItemClickListener = EventFactory.getOnChatItemClickListener(uiHolder, getActivity());
        }
        if(uiHolder.onOperationListener == null) {
            uiHolder.onOperationListener = EventFactory.getOnOperationListener(uiHolder, getActivity());
        }
    }

    private void initWidget () {
        uiHolder.box = (KJChatKeyboard) uiHolder.layout.findViewById(R.id.chat_msg_input_box);
        uiHolder.mRealListView = (ListView) uiHolder.layout.findViewById(org.kymjs.chat.R.id.chat_listview);
        uiHolder.mRealListView.setSelector(android.R.color.transparent);

        /** 初始化表情输入键盘 */
        initMessageInputToolBox();

        /** 初始化列表界面 */
        initListView();
    }

    /**
     * 初始化ListView界面
     */
    protected void initListView() {
        byte[] emoji = new byte[]{
                (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81
        };
        Message message = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SUCCESS, "\ue415", "avatar", "Jerry", "avatar",
                new String(emoji), false, true, new Date(System.currentTimeMillis()
                - (1000 * 60 * 60 * 24) * 8));
        Message message1 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry", "avatar",
                "以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",
                true, true, new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 8));
        Message message2 = new Message(Message.MSG_TYPE_PHOTO,
                Message.MSG_STATE_FAIL, "Tom", "avatar", "Jerry", "avatar",
                "http://static.oschina.net/uploads/space/2015/0611/103706_rpPc_1157342.png",
                false, true, new Date(
                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 7));
        Message message6 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_FAIL, "Tom", "avatar", "Jerry", "avatar",
                "test send fail", true, false, new Date(
                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 6));
        Message message7 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SENDING, "Tom", "avatar", "Jerry", "avatar",
                "<a href=\"http://kymjs.com\">自定义链接</a>也是支持的", true, true, new Date(System.currentTimeMillis()
                - (1000 * 60 * 60 * 24) * 6));

        uiHolder.datas.add(message);
        uiHolder.datas.add(message1);
        uiHolder.datas.add(message2);
        uiHolder.datas.add(message6);
        uiHolder.datas.add(message7);

        uiHolder.adapter = new ChatAdapter(
                getActivity(),
                uiHolder.datas,
                uiHolder.onChatItemClickListener
        );
        uiHolder.mRealListView.setAdapter(uiHolder.adapter);
    }

    /**
     * 初始化表情包
     */
    protected void initMessageInputToolBox() {
        uiHolder.box.setOnOperationListener(uiHolder.onOperationListener);

        /** 加载表情包 */
        List<String> faceCagegory = new ArrayList<>();
        // File faceList = FileUtils.getSaveFolder("chat");
        File faceList = new File("");
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    faceCagegory.add(folder.getAbsolutePath());
                }
            }
        }

        uiHolder.box.setFaceData(faceCagegory);

        /** 点击listView空白处隐藏输入键盘 */
        uiHolder.mRealListView.setOnTouchListener(EventFactory.getOnTouchListener(uiHolder, getActivity()));
    }

    public static class EventFactory {

        /**
         * 表情键盘事件监听
         * @param uiHolder
         * @param activity
         * @return
         */
        public static OnOperationListener getOnOperationListener (final UiHolder uiHolder, final Activity activity) {

            return new OnOperationListener() {

                @Override
                public void send (String content){
                    Message message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                            "Tom", "avatar", "Jerry",
                            "avatar", content, true, true, new Date());
                    uiHolder.datas.add(message);
                    uiHolder.adapter.refresh(uiHolder.datas);
                }

                @Override
                public void selectedFace (Faceicon content){

                }

                @Override
                public void selectedEmoji (Emojicon emoji){
                    uiHolder.box.getEditTextBox().append(emoji.getValue());
                }

                @Override
                public void selectedBackSpace (Emojicon back){
                    DisplayRules.backspace(uiHolder.box.getEditTextBox());
                }

                @Override
                public void selectedFunction ( int index){
                    switch (index) {
                        case 0:
                            AlartWindows.showText(activity, "选择图片");
                            break;
                        case 1:
                            AlartWindows.showText(activity, "跳转相机");
                            break;
                    }
                }
            };
        }

        /**
         * @return 聊天列表内存点击事件监听器
         */
        public static ChatAdapter.OnChatItemClickListener getOnChatItemClickListener(final UiHolder uiHolder, final Activity activity) {
            return new ChatAdapter.OnChatItemClickListener() {

                @Override
                public void onPhotoClick(int position) {
                    KJLoger.debug(uiHolder.datas.get(position).getContent() + "点击图片的");
                    AlartWindows.showText(activity, uiHolder.datas.get(position).getToUserAvatar()+uiHolder.datas.get(position).getFromUserAvatar());
                }

                @Override
                public void onTextClick(int position) {
                    AlartWindows.showText(activity, uiHolder.datas.get(position).getContent());
                }

                @Override
                public void onTextLongClick(int position) {
                    AlartWindows.showText(activity, "长按文本");
                }

                @Override
                public void onFaceClick(int position) {
                    AlartWindows.showText(activity, uiHolder.datas.get(position).getContent());
                }

                @Override
                public void onAvatarClick(int position) {
                    AlartWindows.showText(activity, "点击头像");
                }

                @Override
                public void onSendFailClick(int position) {
                    AlartWindows.showText(activity, "重传");
                }
            };
        }

        /**
         * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
         *
         * @return 会隐藏输入法键盘的触摸事件监听器
         */
        public static View.OnTouchListener getOnTouchListener(final UiHolder uiHolder, final Activity activity) {

            return new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    uiHolder.box.hideLayout();
                    uiHolder.box.hideKeyboard(activity);
                    return false;
                }
            };
        }

    }

    public class UiHolder {

        /** 整体布局 */
        View layout;

        /** 表情键盘 */
        public KJChatKeyboard box;

        /** 聊天列表 */
        public ListView mRealListView;

        public List<Message> datas;
        public ChatAdapter adapter;

        /** 输入键盘操作监听 */
        public OnOperationListener onOperationListener;

        /** 聊天列表内操作事件监听 */
        public ChatAdapter.OnChatItemClickListener onChatItemClickListener;
    }
}
