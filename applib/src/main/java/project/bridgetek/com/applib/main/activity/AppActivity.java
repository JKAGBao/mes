package project.bridgetek.com.applib.main.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.MyBottomDelegate;
import project.bridgetek.com.applib.main.bean.ApkVersion;
import project.bridgetek.com.applib.main.index.child.childpager.SnapFragment;
import project.bridgetek.com.applib.main.toos.ActivityCollectorUtil;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.UpdateManager;
import project.bridgetek.com.bridgelib.activity.ProxyActivity;
import project.bridgetek.com.bridgelib.delegates.BlackDelegate;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Tools;
import project.bridgetek.com.bridgelib.toos.permission.PermissionsManager;
import project.bridgetek.com.bridgelib.toos.permission.PermissionsResultAction;

public class AppActivity extends ProxyActivity {
    ApkVersion mVersion;
    public static SnapFragment snapFragment;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                UpdateManager manager = new UpdateManager(AppActivity.this, Constants.API + mVersion.getFilePath(), false);
                manager.checkUpdateInfo();
            }
        }
    };
    private LocalUserInfo mLocalUserInfo;
    List<String> mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                //    Toast.makeText(AppActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //    Toast.makeText(AppActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
        ActivityCollectorUtil.addActivity(this);
        boolean netWorkConnected = NetworkUtil.isNetWorkConnected(this);
        mLocalUserInfo = LocalUserInfo.getInstance(this);
        mList = mLocalUserInfo.getDataList(Constants.SERVCERIP);
        if (netWorkConnected && mList.size() > 0) {
            int vision = Tools.getVersion(this);
            getApkVersion(vision);
        }
    }

    @Override
    public BlackDelegate setRootDelegate() {
        return new MyBottomDelegate();
    }

    private void getApkVersion(final int vision) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("appName", "jzddj");
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.APKVERSION, json);
                    String result = loadDataFromWeb.getResult();
                    mVersion = JSONObject.parseObject(result, ApkVersion.class);
                    int v = Integer.parseInt(mVersion.getVersion());
                    if (v > vision) {
                        handler.obtainMessage(1).sendToTarget();
                    }
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Log.i("CONG", "onActivityResult: ");
            if (snapFragment != null) {
                snapFragment.onFragmentTriggerResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        snapFragment = null;
    }
}
