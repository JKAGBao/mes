package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.Devices;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.fragment.OscFragment2;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

public class TemporaryActivity extends Activity {
    private ImageView mIcBack;
    private TextView mLineName, mTvMobjectinfo, mTvDevices;
    private EditText mTvDevice;
    private TextView mTvSignalText, mTvDirectionText;
    private RadioGroup mRgSignal, mRgDirection;
    private RadioButton mRbInput, mRbOutput, mRbOther;
    private RadioButton mRbLevel, mRbVertical, mRbAxial;
    private TextView mTvRemarks;
    private EditText mEtAdditional;
    private Button mBtPreservation, mBtRetest;
    private String mPointposition;
    private String mDirection;
    private Devices mDevices = new Devices();
    private String mAdditional;
    private float mResultvalue;
    private String mUsername, mGroupname, mUserId;
    private String mAccountid = "-10";
    private LocalUserInfo mInstance;
    private String mStartTime;
    private BlackDao mBlackDao;
    private float mACCList;
    private float mVELList;
    private String mExtra;
    private int mReanaLyes;
    private String vibFeatures;
    private int nums;
    ProgressDialog mProgressDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mProgressDialog.dismiss();
                Toast.makeText(TemporaryActivity.this, R.string.test_temporary_toast_over_text, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void setFont() {
        mBtRetest.setTypeface(HiApplication.MEDIUM);
        mBtPreservation.setTypeface(HiApplication.MEDIUM);
        mLineName.setTypeface(HiApplication.BOLD);
        mTvMobjectinfo.setTypeface(HiApplication.BOLD);
        mTvDevice.setTypeface(HiApplication.REGULAR);
        mTvDevices.setTypeface(HiApplication.REGULAR);
        mTvSignalText.setTypeface(HiApplication.BOLD);
        mTvDirectionText.setTypeface(HiApplication.BOLD);
        mRbInput.setTypeface(HiApplication.REGULAR);
        mRbOther.setTypeface(HiApplication.REGULAR);
        mRbOutput.setTypeface(HiApplication.REGULAR);
        mRbLevel.setTypeface(HiApplication.REGULAR);
        mRbVertical.setTypeface(HiApplication.REGULAR);
        mRbAxial.setTypeface(HiApplication.REGULAR);
        mTvRemarks.setTypeface(HiApplication.BOLD);
        mEtAdditional.setTypeface(HiApplication.REGULAR);
    }

    public void initUI() {
        mPointposition = getResources().getString(R.string.test_temporary_rb_input_text);
        mDirection = getResources().getString(R.string.test_temporary_rb_level_text);
        Intent intent = getIntent();
        mResultvalue = intent.getFloatExtra(CountString.TMP, 0);
        mACCList = intent.getFloatExtra(CountString.ACC, 0);
        mVELList = intent.getFloatExtra(CountString.VEL, 0);
        mReanaLyes = intent.getIntExtra(Constants.REANALYSE, 0);
        vibFeatures = intent.getStringExtra(Constants.VIBFEATURES);
        nums = intent.getIntExtra(Constants.NUMS, 0);
        mExtra = intent.getStringExtra(Constants.PHOTO);
        mInstance = LocalUserInfo.getInstance(TemporaryActivity.this);
        mBlackDao = BlackDao.getInstance(TemporaryActivity.this);
        mUsername = mInstance.getUserInfo(Constants.USERNAME);
        mUserId = mInstance.getUserInfo(Constants.ACCOUNTID);
        mGroupname = mInstance.getUserInfo(Constants.GROUPNAME);
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mTvMobjectinfo = findViewById(R.id.tv_Mobjectinfo);
        mTvDevice = findViewById(R.id.tv_device);
        mTvDevices = findViewById(R.id.tv_devices);
        mTvSignalText = findViewById(R.id.tv_signal_text);
        mTvDirectionText = findViewById(R.id.tv_direction_text);
        mRgSignal = findViewById(R.id.rg_signal);
        mRgDirection = findViewById(R.id.rg_direction);
        mRbInput = findViewById(R.id.rb_input);
        mRbOutput = findViewById(R.id.rb_output);
        mRbOther = findViewById(R.id.rb_other);
        mRbLevel = findViewById(R.id.rb_level);
        mRbVertical = findViewById(R.id.rb_vertical);
        mRbAxial = findViewById(R.id.rb_axial);
        mTvRemarks = findViewById(R.id.tv_remarks);
        mEtAdditional = findViewById(R.id.et_additional);
        mBtPreservation = findViewById(R.id.bt_preservation);
        mBtRetest = findViewById(R.id.bt_retest);
        mProgressDialog = Storage.getPro(TemporaryActivity.this, getString(R.string.test_temporary_progress_text));
        setFont();
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevices.setDeviceName(mTvDevice.getText().toString());
                Logger.i("onClick: " + mDevices);
                if (TextUtils.isEmpty(mDevices.getDeviceName()) || mDevices.getDeviceName().equals("")) {
                    Toast.makeText(TemporaryActivity.this, R.string.upcom_abnormal_toast_text, Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog.show();
                    getText("1", mDevices, mPointposition, mDirection);
                }
            }
        });
        mBtPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevices.setDeviceName(mTvDevice.getText().toString());
                Logger.i("onClick: " + mDevices);
                if (TextUtils.isEmpty(mDevices.getDeviceName()) || mDevices.getDeviceName().equals("")) {
                    Toast.makeText(TemporaryActivity.this, R.string.upcom_abnormal_toast_text, Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog.show();
                    getText("0", mDevices, mPointposition, mDirection);
                }
            }
        });
        mRgSignal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if (rb.isChecked()) {
                        mPointposition = rb.getText().toString();
                        break;
                    }
                }
            }
        });
        mRgDirection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if (rb.isChecked()) {
                        mDirection = rb.getText().toString();
                        break;
                    }
                }
            }
        });
        mTvDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TemporaryActivity.this, DevicesActivity.class), 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 3) {
            Bundle extras = data.getExtras();
            mDevices = (Devices) extras.getSerializable(Constants.DEVICE);
            mTvDevice.setText(mDevices.getDeviceName());
        }
    }

    private void getText(final String Exception_YN, final Devices devices, final String mPointposition, final String mDirection) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String format = TimeType.dateToString();
                String ACCValue = "";
                String VELValue = "";
                ACCValue = mACCList + "";
                VELValue = mVELList + "";
                mAdditional = mEtAdditional.getText().toString();
                CheckItem checkItem = new CheckItem("", mAccountid, ACCValue, devices.getDeviceCode(), devices.getDeviceName(), VELValue, mStartTime, format, mAccountid, mUsername, mResultvalue + "", mReanaLyes, "0", Exception_YN, "", mAdditional, null, mGroupname, mPointposition, mDirection, false,
                        "", "", "", mUserId, "", "", "", vibFeatures, nums, "");
                int i = mBlackDao.addCheckItem(checkItem);
                if (OscFragment2.mList.size() > 0) {
                    for (int j = 0; j < OscFragment2.mList.size(); j++) {
                        mBlackDao.addChart("" + i, OscFragment2.mList.get(j));
                    }
                }
                OscFragment2.mList.clear();
                if (!mExtra.equals("") && !TextUtils.isEmpty(mExtra)) {
                    ResultFileInfo mFileInfo = new ResultFileInfo(CountString.PHOTO, mExtra, "", i + "", "");
                    mBlackDao.addResultFile(mFileInfo);
                }
                handler.obtainMessage(1).sendToTarget();
            }
        }).start();
    }
}
