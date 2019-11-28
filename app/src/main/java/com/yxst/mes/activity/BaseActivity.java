package com.yxst.mes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yxst.mes.rx.manager.VisitManager;
import com.yxst.mes.util.TitleBarUtil;

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

