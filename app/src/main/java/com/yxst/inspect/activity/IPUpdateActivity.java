package com.yxst.inspect.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.yxst.inspect.MyApplication;
import com.yxst.inspect.R;
import com.yxst.inspect.util.SharedPreferenceUtil;

public class IPUpdateActivity extends Activity {
    private EditText etIp;
    private TextView tvSave;
    private TextView tvIp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow_ip);
        etIp = findViewById(R.id.et_ip);
        tvIp = findViewById(R.id.tv_ip);
        etIp.setText(MyApplication.BASE_URL);
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = etIp.getText().toString();
                SharedPreferences sp = SharedPreferenceUtil.getSharedPreference(IPUpdateActivity.this,"user");
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("Ip", ip.equals("")? MyApplication.BASE_URL:ip);
                if(!ip.equals("")){
                    MyApplication.BASE_URL = ip;
                }
                Intent intent = new Intent();
                intent.putExtra("ip",ip);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
            tvIp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

    }
}
