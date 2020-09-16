package com.yxst.inspect.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.MyApplication;
import com.yxst.inspect.R;
import com.yxst.inspect.activity.adapter.SettingAdapter;
import com.yxst.inspect.database.model.LubeItem;
import com.yxst.inspect.nfc.NFCSettingActivity;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SettingActivity extends BaseActivity implements SettingAdapter.OnItemClickListener {

    private SettingAdapter adapter;
    private static final int INSPECT_RESULT = 0;
    private List<LubeItem> items;
    @BindView(R.id.ll_identify)LinearLayout llIdentify;
    @BindView(R.id.ll_severip) LinearLayout llServeIp;
    @BindView(R.id.ll_imagepath) LinearLayout llImagePath;
    @BindView(R.id.tv_ip) TextView tvIp;
    @BindView(R.id.st_blue)
    Switch stBlue;
    @BindView(R.id.tv_imagepath) TextView tvImagePath;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_info) TextView tvInfo;
    @BindView(R.id.tv_name) TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitle("配置");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();
    }
    @OnClick(R.id.ll_identify)
    public void onViewClicked(View view){
        Intent rfid = new Intent(SettingActivity.this,NFCSettingActivity.class);
        startActivity(rfid);
    }
    private void initView(){
        rlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,UserInfoActivity.class);
                startActivity(intent);
            }
        });
        String loginAcount = SharedPreferenceUtil.getName(this,"User");
        String name = SharedPreferenceUtil.getRealName(this,"User");
        String headImg = SharedPreferenceUtil.getHeadImg(this,"User");
        Log.e("head",headImg+",");
        tvName.setText(name);
        tvInfo.setText(loginAcount);
        String url = "http://60.164.211.4:9800/FilesServer"+headImg;
        Glide.with(this).load(url)
                .thumbnail(0.2f)
              //  .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .override(70,70)
                .into(ivHead);
        tvImagePath.setText(MyApplication.IMAGE_PATH);
        tvIp.setText(MyApplication.BASE_URL);
        boolean isOpen = SharedPreferenceUtil.isOpenBlueVib(this,"isBlue");
        stBlue.setChecked(isOpen);
        stBlue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences sp = SharedPreferenceUtil.getSharedPreference(SettingActivity.this,"user");
                SharedPreferences.Editor editor = sp.edit();
                if(isChecked){
                    editor.putBoolean("isBlue",true);
                }else{
                    editor.putBoolean("isBlue",false);
                }
                editor.commit();
            }
        });
        llImagePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,IPUpdateActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        llServeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,IPUpdateActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        llImagePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ImagePathActivity.class);
                startActivityForResult(intent, 2);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            switch (requestCode){
                case 1:
                    String ip = data.getExtras().getString("ip");
                    tvIp.setText(MyApplication.BASE_URL);
                    Log.e("2457",ip+"===");
                    break;
                case 2:
                    String path = data.getExtras().getString("path");
                    tvImagePath.setText(MyApplication.IMAGE_PATH);
                    Log.e("2457",path+"===");
                    break;
                default:
                    break;
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
           adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(1,intent);
    }


    @Override
    public void onClick(View view, Long lineId, int position) {

    }
}
