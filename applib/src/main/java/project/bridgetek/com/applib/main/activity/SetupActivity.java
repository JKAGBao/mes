package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.ApkVersion;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.toos.ActivityCollectorUtil;
import project.bridgetek.com.applib.main.toos.BackGroup;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.applib.main.toos.UpdateManager;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.DeleteFileUtil;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Tools;

public class SetupActivity extends Activity {
    ApkVersion mVersion;
    private Button mLlCancel, mBtn1;
    private ImageView mIcBack;
    private LinearLayout mLlSynchro, mLayout, mLlFit, mLlWrite, mLlSetup, mLlClear, mLlAbout, mLlRenew;
    private TextView mTvSynchro, mTvCancel, mTvFit, mTvWrite, mTvSetup, mTvClear, mTvAbout, mTvRenew;
    private TextView mLineName;
    LocationReceiver locationReceiver;
    private LocalUserInfo mLocalUserInfo;
    private String mAccountid;
    ProgressDialog mProgressDialog, mDialog;
    List<String> mList;
    BlackDao mBlackDao;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mDialog.dismiss();
                Toast.makeText(SetupActivity.this, R.string.setup_activity_toast_clear_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                UpdateManager manager = new UpdateManager(SetupActivity.this, Constants.API + mVersion.getFilePath(), false);
                manager.checkUpdateInfo();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        ActivityCollectorUtil.addActivity(this);
        initUI();
        setOnclick();
    }

    public void setFont() {
        mTvSynchro.setTypeface(HiApplication.REGULAR);
        mTvCancel.setTypeface(HiApplication.REGULAR);
        mTvFit.setTypeface(HiApplication.REGULAR);
        mTvWrite.setTypeface(HiApplication.REGULAR);
        mTvSetup.setTypeface(HiApplication.REGULAR);
        mTvClear.setTypeface(HiApplication.REGULAR);
        mTvAbout.setTypeface(HiApplication.REGULAR);
        mLineName.setTypeface(HiApplication.BOLD);
        mTvRenew.setTypeface(HiApplication.REGULAR);
    }

    public void initUI() {
        mIcBack = findViewById(R.id.ic_back);
        mLlSynchro = findViewById(R.id.ll_synchro);
        mLayout = findViewById(R.id.ll_cancel);
        mLlFit = findViewById(R.id.ll_fit);
        mLlWrite = findViewById(R.id.ll_write);
        mLlSetup = findViewById(R.id.ll_setup);
        mLlClear = findViewById(R.id.ll_clear);
        mLlAbout = findViewById(R.id.ll_about);
        mTvSynchro = findViewById(R.id.tv_synchro);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvFit = findViewById(R.id.tv_fit);
        mTvWrite = findViewById(R.id.tv_write);
        mTvSetup = findViewById(R.id.tv_setup);
        mTvClear = findViewById(R.id.tv_clear);
        mTvAbout = findViewById(R.id.tv_about);
        mLineName = findViewById(R.id.line_name);
        mTvRenew = findViewById(R.id.tv_renew);
        mLlRenew = findViewById(R.id.ll_renew);
        locationReceiver = new LocationReceiver();
        mBlackDao = BlackDao.getInstance(SetupActivity.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("location.reportsucc");
        registerReceiver(locationReceiver, filter);
        mLocalUserInfo = LocalUserInfo.getInstance(this);
        mAccountid = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        mProgressDialog = Storage.getPro(SetupActivity.this, getString(R.string.setup_activity_progress_dialog_text));
        mDialog = Storage.getPro(SetupActivity.this, getString(R.string.setup_activity_dialog_text));
        setFont();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList = mLocalUserInfo.getDataList(Constants.SERVCERIP);
        if (mList.size() > 0) {
            mTvSetup.setText(getString(R.string.setup_activity_tv_setup_text) + CountString.LEFT_BRACKETS + mList.get(mList.size() - 1) + CountString.RIGHT_BRACKETS);
        }
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLlSynchro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAccountid) || mAccountid.equals("")) {
                    Toast.makeText(SetupActivity.this, R.string.setup_activity_toast_nosynchro_text, Toast.LENGTH_SHORT).show();
                } else {
                    boolean netWorkConnected = NetworkUtil.isNetWorkConnected(SetupActivity.this);
                    if (netWorkConnected) {
                        mProgressDialog.show();
                        startService(new Intent(SetupActivity.this, BackGroup.class));
                    } else {
                        Toast.makeText(SetupActivity.this, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mLlAbout.setOnClickListener(new OnClickListener() {
            @Override
            //关于
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        mLlSetup.setOnClickListener(new OnClickListener() {
            @Override
            //服务器设置
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, SetServerActivity.class);
                startActivity(intent);
            }
        });
        mLlWrite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, NfcActivity.class);
                startActivity(intent);
            }
        });
        mLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSave();
                Intent intent = new Intent(SetupActivity.this, LoginActivity.class);
                startActivity(intent);
                ActivityCollectorUtil.finishAllActivity();
                finish();
            }
        });
        mLlFit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, BluetoothSetActivity.class);
                startActivity(intent);
            }
        });
        mLlClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SetupActivity.this, DelActivity.class);
//                startActivity(intent);
                setDelTask();
            }
        });
        mLlRenew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(SetupActivity.this);
                if (netWorkConnected) {
                    int vision = Tools.getVersion(SetupActivity.this);
                    getApkVersion(vision);
                }
            }
        });
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
                        handler.obtainMessage(2).sendToTarget();
                    } else {
                        SetupActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SetupActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }).start();
    }

    public class LocationReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals("location.reportsucc")) {
                String extra = intent.getStringExtra("key");
                if (extra.equals("6")) {
                    mProgressDialog.dismiss();
                    getDialog(true, "同步成功", intent.getStringExtra("error"));
                } else if (extra.equals("2") || extra.equals("3") || extra.equals("5")) {
                    getDialog(false, "同步失败", intent.getStringExtra("error"));
                    mProgressDialog.dismiss();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationReceiver);
    }

    private void getImagePathFromSD() {
        // 得到sd卡内image文件夹的路径   File.separator(/)
        List<File> fileList = new ArrayList<File>();
        String fileName = getExternalCacheDir().getPath();
        if (fileName != null) {
            File dir = new File(fileName + "/recordtest");
            if (!dir.exists()) {
                dir.mkdir();
            }
            fileName = dir + "/";
        }
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(fileName);
        if (fileAll.exists() && fileAll.isDirectory()) {
            File[] files = fileAll.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].lastModified() / 1000 + 86400 < TimeType.getNowTime()) {
                    fileList.add(files[i]);
                }
            }
        }
        for (int i = 0; i < fileList.size(); i++) {
            DeleteFileUtil.delFile(fileList.get(i).getPath(), SetupActivity.this);
        }
        handler.obtainMessage(1).sendToTarget();
    }

    public void getDelFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getImagePathFromSD();
            }
        }).start();
    }

    public void setDelTask() {
        List<CheckItem> checkItems = mBlackDao.setDelete();
        if (checkItems.size() > 0) {
            getDialog();
        } else {
            mDialog.show();
            setClear();
        }
    }

    public void setSave() {
        mLocalUserInfo.setUserInfo(Constants.ACCOUNTID, "");
        mLocalUserInfo.setUserInfo(Constants.ACCOUNT, "");
        mLocalUserInfo.setUserInfo(Constants.BEDGEID, "");
        mLocalUserInfo.setUserInfo(Constants.USERNAME, "");
        mLocalUserInfo.setUserInfo(Constants.GROUPID, "");
        mLocalUserInfo.setUserInfo(Constants.GROUPNAME, "");
        mLocalUserInfo.setUserInfo(Constants.DEPARTMENTNAME, "");
        mLocalUserInfo.setUserInfo(Constants.COMPANYNAME, "");
    }

    public void getDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(R.string.setup_dialog_text)//设置对话框的内容
                .setNegativeButton(R.string.upcom_abnormal_tv_cancel_text, null)
                .setPositiveButton(R.string.test_temporary_bt_preservation_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.show();
                        setClear();
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void setClear() {
        mBlackDao.cleMobject();
        mBlackDao.cleCheckItemInfo();
        mBlackDao.cleDevices();
        mBlackDao.cleTaskInfo();
        mBlackDao.cleCheckItem();
        mBlackDao.cleResultFile();
        mBlackDao.cleChart();
        mBlackDao.cleLabel();
        mBlackDao.cleLineMobject();
        getDelFile();
    }

    public void getDialog(boolean message, String msg, String count) {
        View view = LayoutInflater.from(SetupActivity.this).inflate(R.layout.dialog_upload, null, false);
        final Dialog builder = new Dialog(SetupActivity.this, R.style.update_dialog);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        TextView tvTaskName = view.findViewById(R.id.tv_taskName);
        Button btPreservation = view.findViewById(R.id.bt_preservation);
        ImageView imgMessage = view.findViewById(R.id.img_message);
        TextView tvUpload = view.findViewById(R.id.tv_upload);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        if (message) {
            imgMessage.setBackgroundResource(R.drawable.ic_success);
            tvUpload.setText("成功");
        } else {
            imgMessage.setBackgroundResource(R.drawable.ic_fail);
            tvUpload.setText("失败");
        }
        btPreservation.setTypeface(HiApplication.MEDIUM);
        tvTaskName.setText(msg);
        tvMessage.setText(count);
        btPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.setContentView(view);
        builder.show();
    }
}
