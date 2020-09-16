package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

public class SetServerActivity extends Activity {
    Button mBtn2, mBtSkip;
    LocalUserInfo mUserInfo;
    private ImageView mIcBack;
    private AutoCompleteTextView mAutoText;
    private List<String> mListString = new ArrayList<String>();
    private ArrayAdapter<String> mArrayAdapter;
    String[] str;
    boolean aBoolean, isaBoolean;
    ProgressDialog mProgressDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mProgressDialog.dismiss();
                mListString.add(mAutoText.getText().toString());
                mUserInfo.setDataList(Constants.SERVCERIP, pastLeep(mListString));
                Constants.API = CountString.HTTP + mAutoText.getText().toString();
                Toast.makeText(SetServerActivity.this, R.string.setup_setserver_toast_yesconnect_text, Toast.LENGTH_SHORT).show();
                if (isaBoolean) {
                    startActivity(new Intent(SetServerActivity.this, LoginActivity.class));
                }
                finish();
            } else if (msg.what == 2) {
                mProgressDialog.dismiss();
                Toast.makeText(SetServerActivity.this, R.string.setup_setserver_noconnect_text, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        setOnclck();
    }

    public void setOnclck() {
        mIcBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(SetServerActivity.this);
                if (netWorkConnected) {
                    if (mAutoText.getText().toString().length() == 0) {
                        Toast.makeText(SetServerActivity.this, R.string.setup_setserver_toast_empty_text, Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.show();
                        saveHistory();
                    }
                } else {
                    Toast.makeText(SetServerActivity.this, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        });
        mBtSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();
            }
        });
    }

    public List<String> pastLeep(List<String> list) {
        Collections.reverse(list);
        List<String> listNew = new ArrayList<>();
        Set set = new HashSet();
        for (String str : list) {
            if (set.add(str)) {
                listNew.add(str);
            }
        }
        Collections.reverse(listNew);
        return listNew;
    }

    private void initView() {
        aBoolean = getIntent().getBooleanExtra(Constants.SKIP, false);
        isaBoolean = getIntent().getBooleanExtra(Constants.SERVCERIP, false);
        mIcBack = findViewById(R.id.ic_back);
        mBtn2 = findViewById(R.id.btn2);
        mAutoText = findViewById(R.id.setServerIP);
        mBtSkip = findViewById(R.id.bt_skip);
        if (aBoolean) {
            mBtSkip.setVisibility(View.VISIBLE);
        } else {
            mBtSkip.setVisibility(View.GONE);
        }
        mUserInfo = LocalUserInfo.getInstance(SetServerActivity.this);
        mProgressDialog = Storage.getPro(SetServerActivity.this, getString(R.string.setup_bluetooth_dialog_text));
        List<String> ceshi123 = mUserInfo.getDataList(Constants.SERVCERIP);
        mListString = ceshi123;
        if (ceshi123.size() > 0) {
            mAutoText.setText(mListString.get(mListString.size() - 1));
            str = new String[ceshi123.size()];
            for (int i = 0; i < ceshi123.size(); i++) {
                str[i] = ceshi123.get(i);
            }
            for (String s : str) {
                mArrayAdapter = new ArrayAdapter<String>(SetServerActivity.this, android.R.layout.simple_dropdown_item_1line, str);
                mAutoText.setAdapter(mArrayAdapter);
            }
        }
    }

    /**
     * 历史IP保存
     */
    public void saveHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String map = Constants.ACCOUNTID;
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(CountString.HTTP + mAutoText.getText().toString() + Constants.SERVERTEST, map);
                    String result = loadDataFromWeb.getServer();
                    if (result.equals("OK")) {
                        handler.obtainMessage(1).sendToTarget();
                    } else {
                        handler.obtainMessage(2).sendToTarget();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(2).sendToTarget();
                    Logger.w(e);
                }
            }
        }).start();
    }

    public void getDialog() {
        View view = LayoutInflater.from(SetServerActivity.this).inflate(R.layout.dialog_skip, null, false);
        final Dialog builder = new Dialog(SetServerActivity.this, R.style.update_dialog);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        TextView tvTaskName = view.findViewById(R.id.tv_taskName);
        Button btPreservation = view.findViewById(R.id.bt_preservation);
        Button btComplete = view.findViewById(R.id.bt_complete);
        tvTaskName.setTypeface(HiApplication.MEDIUM);
        btComplete.setTypeface(HiApplication.MEDIUM);
        btPreservation.setTypeface(HiApplication.MEDIUM);
        tvTaskName.setText(R.string.setup_setserver_dialog_taskname_text);
        btPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetServerActivity.this, AppActivity.class));
                finish();
                builder.dismiss();
            }
        });
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setContentView(view);
        builder.show();
    }
}



