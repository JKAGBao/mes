package com.yxst.inspect.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yxst.inspect.MyApplication;
import com.yxst.inspect.R;
import com.yxst.inspect.util.SharedPreferenceUtil;

public class ImagePathActivity extends Activity {
    private EditText etPath;
    private TextView tvSave;
    private TextView tvPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepath);
        etPath = findViewById(R.id.et_path);
        etPath.setText(MyApplication.IMAGE_PATH);
        tvPath = findViewById(R.id.tv_path);
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = etPath.getText().toString();
                SharedPreferences sp = SharedPreferenceUtil.getSharedPreference(ImagePathActivity.this,"user");
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("ImagePath", path.equals("")? MyApplication.IMAGE_PATH:path);
                if(!path.equals("")){
                    MyApplication.IMAGE_PATH = path;
                }
                Intent intent = new Intent();
                intent.putExtra("path",path);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
