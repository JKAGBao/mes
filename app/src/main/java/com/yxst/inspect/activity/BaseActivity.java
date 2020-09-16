package com.yxst.inspect.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yxst.inspect.util.TitleBarUtil;

/**
 * Created By YuanCheng on 2019/8/7 15:27
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleBarUtil.titlebarColor(this);
    }
}

