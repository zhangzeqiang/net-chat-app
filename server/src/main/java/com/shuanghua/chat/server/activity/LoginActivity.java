package com.shuanghua.chat.server.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.shuanghua.chat.server.R;

/**
 * Created by yinhaide on 2016/2/22.
 */
public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onLoginClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                finish();
            case R.id.btn_regist:
            case R.id.tv_forget:
        }
    }
}
