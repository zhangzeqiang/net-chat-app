package com.shuanghua.chat.server;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shuanghua.chat.server.activity.LoginActivity;
import com.shuanghua.chat.server.fragment.AllMessageFragment;
import com.shuanghua.chat.server.fragment.ConnectMessageFragment;

public class MainActivity extends AppCompatActivity {
    private ConnectMessageFragment connectMessageFragment;
    private AllMessageFragment allMessageFragment;
    private Button[] mRadioButtons;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private ImageButton imageButton_all, imageButton_connect;
    private LinearLayout ll_all, ll_connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();

        // tcp close,注销服务
        SHApplication.getInstance().getTCPService().close();
        SHApplication.getInstance().unBindService();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        ll_connect = (LinearLayout) findViewById(R.id.ll_connect);
        imageButton_all = (ImageButton) findViewById(R.id.imageButton_all);
        imageButton_connect = (ImageButton) findViewById(R.id.imageButton_connect);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_buttom_bg);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_btn_group);
        linearLayout.setAlpha(0.5f);
        connectMessageFragment = new ConnectMessageFragment();
        allMessageFragment = new AllMessageFragment();
        fragments = new Fragment[] { allMessageFragment, connectMessageFragment };
        mRadioButtons = new Button[2];
        mRadioButtons[0] = (Button) findViewById(R.id.main_btn_home_page);
        mRadioButtons[1] = (Button) findViewById(R.id.main_btn_connect_page);
        // 把allFragment设为选中状态
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.add(R.id.fragment_container, allMessageFragment).add(R.id.fragment_container, connectMessageFragment).hide(connectMessageFragment).show(allMessageFragment);
        trx.commit();
        mRadioButtons[0].setSelected(true);
        imageButton_all.setImageResource(R.mipmap.em_icon_shop_select);
    }


    public void onTabClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_home_page:
                mRadioButtons[0].setTextColor(Color.RED);
                mRadioButtons[1].setTextColor(Color.GRAY);
                imageButton_connect.setImageResource(R.mipmap.em_icon_shop_normal);
                imageButton_all.setImageResource(R.mipmap.em_icon_shop_select);
                index = 0;
                break;
            case R.id.imageButton_all:
                mRadioButtons[0].setTextColor(Color.RED);
                mRadioButtons[1].setTextColor(Color.GRAY);
                imageButton_connect.setImageResource(R.mipmap.em_icon_shop_normal);
                imageButton_all.setImageResource(R.mipmap.em_icon_shop_select);
                index = 0;
                break;
            case R.id.main_btn_connect_page:
                imageButton_all.setImageResource(R.mipmap.em_icon_shop_normal);
                imageButton_connect.setImageResource(R.mipmap.em_icon_shop_select);
                mRadioButtons[0].setTextColor(Color.GRAY);
                mRadioButtons[1].setTextColor(Color.RED);
                index = 1;
                break;
            case R.id.imageButton_connect:
                imageButton_all.setImageResource(R.mipmap.em_icon_shop_normal);
                imageButton_connect.setImageResource(R.mipmap.em_icon_shop_select);
                mRadioButtons[0].setTextColor(Color.GRAY);
                mRadioButtons[1].setTextColor(Color.RED);
                index = 1;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mRadioButtons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mRadioButtons[index].setSelected(true);
        currentTabIndex = index;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if (id == R.id.action_login) {
            startActivity(new Intent(this.getBaseContext(), LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
