package project.bridgetek.com.applib.main.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.flir.flironeexampleapplication.GLPreviewActivity;
import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.MessageEventWave;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.fragment.OscFragment2;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.ClientManager;
import project.bridgetek.com.applib.main.toos.CurrentConfig;
import project.bridgetek.com.applib.main.toos.MessageEventValue;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

public class MeasureActivity extends AppCompatActivity implements OscFragment2.OnFragmentInteractionListener {
    private LinearLayout mLlName, mLlConnect;
    private ImageView mIcBack;
    private TextView mLineName, mTvEquipment, mTvInspect;
    private List<CheckItemInfo> mZDlist = new ArrayList<>();
    private Switch mStDerail;
    private CheckItemInfo mCheckItemInfo;
    private CheckItemInfo mEshCheckItem = new CheckItemInfo();
    private Button mBtPreservation;
    private String mAccountid, mUsername, mGroupname, mTaskID;
    private LocalUserInfo mInstance;
    private BlackDao mBlackDao;
    private boolean mIsWatch;
    private TextView mTvQuantity, mTvSignal, mTvSensor, mTvConnect, mTvNotConnect;
    private Button mBtRetest, mBtTemperaturePhoto, mBtAccelerationPhoto, mBtSpeedPhoto;
    private TextView mTvSpeed, mTvAcceleration, mTvTemperature;
    private EditText mTvSpeedValue, mTvAccelerationValue, mTvTemperatureValue;
    private TextView mTvSpeedText, mTvAccelerationText, mTvTemperatureText;
    private TextView mTvSpeedSection, mTvAccelerationSection, mTvTemperatureSection;
    private TextView mTvSection1, mTvSection2, mTvSection4;
    private String mStartTime;
    private ResultFileInfo mFileInfo = new ResultFileInfo("", "", "", "");
    private BluetoothDevice mDevice;
    private UUID mService = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    private UUID mCharacter2 = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    private CurrentConfig mConfig;
    private UUID mCharacter1 = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private UUID mCharacter3 = UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb");
    private UUID mCharacterBat = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    private UUID mServiceBat = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    private boolean mInSampling = false;
    private boolean[] mPacketRecvFlag = new boolean[256];
    private int mRecvTemp;
    private int mRecvType;
    private int mRecvRate;
    private int mRecvNums;
    private int mTotalPacket;
    private int mRecvCoef;
    private byte[] mRecvBytes = new byte[8192];
    private Timer mRecvTimer = null;
    private TimerTask mRecvTimerTask = null;
    private int mBatteryLevel = 0;
    private int mRssiLevel = 0;
    private boolean isStart = false, isHold = false;
    private boolean mIsConnect = false;
    private boolean mIsBack = true;
    private boolean mIsToast = true;
    private LinearLayout mLlAbout, mLlCount;
    String ResultValue = "";
    ProgressDialog mDialog, mProgress;
    ProgressDialog mPro, mProgressDialog;
    //private NoScrollViewPager mvpOsc;
    private boolean isOsc = true;
    String mac;
    MessageEventValue messageEventValue;
    private TextView mTvPeakValue, mTvVirtualValue, mTvPeakPeak;
    private TextView mTvEststandard;
    private LinearLayout mLlChart;
    private TextView mTvTips;
    String UserCode;
    String TaskType;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                //Toast.makeText(VibraTionActivity.this, "" + mConfig.sample_type, Toast.LENGTH_SHORT).show();
                if (mConfig.sample_type.equals(CountString.TMP)) {
                    mTvTemperatureValue.setText(String.valueOf(messageEventValue.getTemperature()));
                } else if (mConfig.sample_type.equals(CountString.ACC)) {
                    mTvSpeedValue.setText(Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())));
                    mTvPeakValue.setText(getString(R.string.test_vibration_tv_peak_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())));
                    mTvVirtualValue.setText(getString(R.string.test_vibration_tv_valid_text) + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())));
                    mTvPeakPeak.setText(getString(R.string.test_vibra_tv_peakvalue_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeak())));
                } else {
                    mTvAccelerationValue.setText(Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())));
                    mTvPeakValue.setText(getString(R.string.test_vibration_tv_peak_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())));
                    mTvVirtualValue.setText(getString(R.string.test_vibration_tv_valid_text) + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())));
                    mTvPeakPeak.setText(getString(R.string.test_vibra_tv_peakvalue_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeak())));
                }
            } else if (msg.what == 2) {
                mDialog.dismiss();
            } else if (msg.what == 1) {
                if (mProgress != null) {
                    mProgress.dismiss();
                }
            }
        }
    };
    private LinearLayout mLlSpeed, mLlAcceleration, mLlTemprature;
    private boolean mIsFNC = false;
    private boolean mIsGai = false;
    List<CheckItem> item1 = new ArrayList<>();
    TextView mTvLevel;
    ImageView mImgThermal;
    private ImageView mImgState;
    private boolean link, reconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_measure);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        Constants.ISLIANJIE = true;
        initUI();
        setOnclick();
        //registerBoradcastRecever();
    }

    public void setFont() {
        mTvTemperatureSection.setTypeface(HiApplication.REGULAR);
        mTvAccelerationSection.setTypeface(HiApplication.REGULAR);
        mTvSpeedSection.setTypeface(HiApplication.REGULAR);
        mTvTemperatureText.setTypeface(HiApplication.MEDIUM);
        mTvAccelerationText.setTypeface(HiApplication.MEDIUM);
        mTvSpeedText.setTypeface(HiApplication.MEDIUM);
        mTvTemperatureValue.setTypeface(HiApplication.BOLD);
        mTvAccelerationValue.setTypeface(HiApplication.BOLD);
        mTvSpeedValue.setTypeface(HiApplication.BOLD);
        mTvTemperature.setTypeface(HiApplication.BOLD);
        mTvAcceleration.setTypeface(HiApplication.BOLD);
        mTvSpeed.setTypeface(HiApplication.BOLD);
        mBtSpeedPhoto.setTypeface(HiApplication.MEDIUM);
        mBtAccelerationPhoto.setTypeface(HiApplication.MEDIUM);
        mBtTemperaturePhoto.setTypeface(HiApplication.REGULAR);
        mTvNotConnect.setTypeface(HiApplication.BOLD);
        mTvConnect.setTypeface(HiApplication.BOLD);
        mTvSensor.setTypeface(HiApplication.BOLD);
        mTvSignal.setTypeface(HiApplication.BOLD);
        mTvQuantity.setTypeface(HiApplication.BOLD);
        mLineName.setTypeface(HiApplication.BOLD);
        mTvEquipment.setTypeface(HiApplication.BOLD);
        mTvInspect.setTypeface(HiApplication.BOLD);
        mBtPreservation.setTypeface(HiApplication.MEDIUM);
        mBtRetest.setTypeface(HiApplication.MEDIUM);
        mTvSection1.setTypeface(HiApplication.REGULAR);
        mTvSection2.setTypeface(HiApplication.REGULAR);
        mTvSection4.setTypeface(HiApplication.REGULAR);
        mTvEststandard.setTypeface(HiApplication.MEDIUM);
        mTvTips.setTypeface(HiApplication.BOLD);
    }

    public void setEditListener() {
        mTvTemperatureValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //mTvLevel.setText(editable.toString().trim());
                if (editable.toString().trim().length() < 1) {
                    ResultValue = "";
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.measure_bt_preservation));
                    return;
                }
                if (!mBtPreservation.getText().toString().equals(getString(R.string.test_vibration_activity_hold_text))) {
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                }
                isHold = true;
                mTvLevel.setTextColor(getResources().getColor(R.color.over_tips));
                String trim = editable.toString().trim();
                int index = trim.indexOf("º");
                if (index > 1) {
                    ResultValue = trim.substring(0, trim.indexOf("º"));
                } else {
                    ResultValue = editable.toString().trim();
                }
                String s = setException(mCheckItemInfo, Float.parseFloat(ResultValue));
                switch (s) {
                    case "0":
                        mTvLevel.setTextColor(getResources().getColor(R.color.work_title));
                        mTvLevel.setText("正常");
                        break;
                    case "1":
                        mTvLevel.setText("预警");
                        break;
                    case "2":
                        mTvLevel.setText("警告");
                        break;
                    case "3":
                        mTvLevel.setText("报警");
                        break;
                    case "4":
                        mTvLevel.setText("危险");
                        break;
                }
            }
        });
        mTvSpeedValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //mTvLevel.setText(editable.toString().trim());
                if (editable.toString().trim().length() < 1) {
                    ResultValue = "";
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.measure_bt_preservation));
                    return;
                }
                if (!mBtPreservation.getText().toString().equals(getString(R.string.test_vibration_activity_hold_text))) {
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                }
                isHold = true;
                mTvLevel.setTextColor(getResources().getColor(R.color.over_tips));
                ResultValue = editable.toString().trim();
                String s = setException(mCheckItemInfo, Float.parseFloat(ResultValue));
                switch (s) {
                    case "0":
                        mTvLevel.setText("正常");
                        mTvLevel.setTextColor(getResources().getColor(R.color.work_title));
                        break;
                    case "1":
                        mTvLevel.setText("预警");
                        break;
                    case "2":
                        mTvLevel.setText("警告");
                        break;
                    case "3":
                        mTvLevel.setText("报警");
                        break;
                    case "4":
                        mTvLevel.setText("危险");
                        break;
                }
            }
        });
        mTvAccelerationValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //mTvLevel.setText(editable.toString().trim());
                if (editable.toString().trim().length() < 1) {
                    ResultValue = "";
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.measure_bt_preservation));
                    return;
                }
                if (!mBtPreservation.getText().toString().equals(getString(R.string.test_vibration_activity_hold_text))) {
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                }
                isHold = true;
                mTvLevel.setTextColor(getResources().getColor(R.color.over_tips));
                ResultValue = editable.toString().trim();
                String s = setException(mCheckItemInfo, Float.parseFloat(ResultValue));
                switch (s) {
                    case "0":
                        mTvLevel.setText("正常");
                        mTvLevel.setTextColor(getResources().getColor(R.color.work_title));
                        break;
                    case "1":
                        mTvLevel.setText("预警");
                        break;
                    case "2":
                        mTvLevel.setText("警告");
                        break;
                    case "3":
                        mTvLevel.setText("报警");
                        break;
                    case "4":
                        mTvLevel.setText("危险");
                        break;
                }
            }
        });
    }

    public void initUI() {
        mStartTime = TimeType.dateToString();
        mTvSpeedSection = findViewById(R.id.tv_speed_section);
        mTvAccelerationSection = findViewById(R.id.tv_acceleration_section);
        mTvTemperatureSection = findViewById(R.id.tv_temperature_section);
        mTvSpeed = findViewById(R.id.tv_speed);
        mTvAcceleration = findViewById(R.id.tv_acceleration);
        mTvTemperature = findViewById(R.id.tv_temperature);
        mTvSpeedValue = findViewById(R.id.tv_speed_value);
        mTvAccelerationValue = findViewById(R.id.tv_acceleration_value);
        mTvTemperatureValue = findViewById(R.id.tv_temperature_value);
        mTvSpeedText = findViewById(R.id.tv_speed_text);
        mTvAccelerationText = findViewById(R.id.tv_acceleration_text);
        mTvTemperatureText = findViewById(R.id.tv_temperature_text);
        mBtRetest = findViewById(R.id.bt_retest);
        mBtTemperaturePhoto = findViewById(R.id.bt_temperature_photo);
        mBtAccelerationPhoto = findViewById(R.id.bt_acceleration_photo);
        mBtSpeedPhoto = findViewById(R.id.bt_speed_photo);
        mLlName = findViewById(R.id.ll_name);
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mLlConnect = findViewById(R.id.ll_connect);
        mStDerail = findViewById(R.id.st_derail);
        mTvEquipment = findViewById(R.id.tv_equipment);
        mTvInspect = findViewById(R.id.tv_inspect);
        mBtPreservation = findViewById(R.id.bt_preservation);
        mTvQuantity = findViewById(R.id.tv_quantity);
        mTvSignal = findViewById(R.id.tv_signal);
        mTvSensor = findViewById(R.id.tv_sensor);
        mTvConnect = findViewById(R.id.tv_Connect);
        mTvNotConnect = findViewById(R.id.tv_notConnect);
        mTvSection1 = findViewById(R.id.tv_section1);
        mTvSection2 = findViewById(R.id.tv_section2);
        mTvSection4 = findViewById(R.id.tv_section4);
        mLlAbout = findViewById(R.id.ll_about);
        mLlAbout.setVisibility(View.GONE);
        mLlCount = findViewById(R.id.ll_count);
        mLlName.setVisibility(View.GONE);
        mTvTips = findViewById(R.id.tv_tips);
        mTvLevel = findViewById(R.id.tv_level);
        mImgThermal = findViewById(R.id.img_thermal);
        mImgState = findViewById(R.id.img_state);
        mBlackDao = BlackDao.getInstance(MeasureActivity.this);
        Intent intent = getIntent();
        mIsFNC = intent.getBooleanExtra(Constants.SUBMIT, false);
        setEditListener();
        if (mIsFNC) {
            mIsGai = intent.getBooleanExtra(Constants.CHANGE, false);
            CheckItemInfo checkItemInfo = (CheckItemInfo) intent.getSerializableExtra(Constants.CHECKINFO);
            mZDlist.add(checkItemInfo);
        } else {
            mZDlist = (List<CheckItemInfo>) intent.getSerializableExtra(Constants.ZD);
            mIsWatch = intent.getBooleanExtra(Constants.ISFIRST, false);
        }
        mInstance = LocalUserInfo.getInstance(MeasureActivity.this);
        mAccountid = mInstance.getUserInfo(Constants.ACCOUNTID);
        mUsername = mInstance.getUserInfo(Constants.USERNAME);
        UserCode = mInstance.getUserInfo(Constants.GROUPID);
        TaskType = mInstance.getUserInfo(Constants.TASKTYPE);
        mGroupname = mInstance.getUserInfo(Constants.GROUPNAME);
        mTaskID = mInstance.getUserInfo(Constants.TASKID);
        mCheckItemInfo = mZDlist.get(0);
        for (int i = 1; i < mZDlist.size(); i++) {
            if (mCheckItemInfo.getSyncSamplingTag() != null & mZDlist.get(i).getSyncSamplingTag() != null) {
                if (mCheckItemInfo.getSyncSamplingTag().equals(mZDlist.get(i).getSyncSamplingTag())) {
                    mEshCheckItem = mZDlist.get(i);
                }
            }
        }
        mLlSpeed = findViewById(R.id.ll_speed);
        mLlAcceleration = findViewById(R.id.ll_acceleration);
        mLlTemprature = findViewById(R.id.ll_temprature);
        mTvPeakPeak = findViewById(R.id.tv_peak_peak);
        mTvPeakValue = findViewById(R.id.tv_peak_value);
        mTvVirtualValue = findViewById(R.id.tv_virtual_value);
        mTvEststandard = findViewById(R.id.tv_ESTStandard);
        mLlChart = findViewById(R.id.ll_chart);
        if ("1".equals(Constants.STARTSTOP)) {
            mImgState.setVisibility(View.VISIBLE);
        } else {
            mImgState.setVisibility(View.GONE);
        }
        mLlChart.setVisibility(View.GONE);
        mLlSpeed.setVisibility(View.GONE);
        mLlAcceleration.setVisibility(View.GONE);
        mLlTemprature.setVisibility(View.GONE);
        if (mCheckItemInfo.getCheckType().equals(Constants.CW)) {
            mLlTemprature.setVisibility(View.VISIBLE);
        } else {
            if (mCheckItemInfo.getZhenDong_Type().equals(CountString.ZHENDONG_TYPE)) {
                mLlSpeed.setVisibility(View.VISIBLE);
            } else {
                mLlAcceleration.setVisibility(View.VISIBLE);
            }
        }
        if (mIsGai) {
            item1 = mBlackDao.getCheckAbnorItem(mTaskID, mCheckItemInfo.getCheckItemID());
            if (!item1.isEmpty()) {
                if (mCheckItemInfo.getCheckType().equals(Constants.CW)) {
                    mTvTemperatureValue.setText(item1.get(0).getResultValue());
                } else {
                    if (mCheckItemInfo.getZhenDong_Type().equals(CountString.ZHENDONG_TYPE)) {
                        mTvSpeedValue.setText(item1.get(0).getResultValue());
                    } else {
                        mTvAccelerationValue.setText(item1.get(0).getResultValue());
                    }
                }
            }
        }
        setFont();
        setName();
        mProgressDialog = Storage.getPro(MeasureActivity.this, getString(R.string.upcom_measure_progress_text));
        mDialog = Storage.getPro(MeasureActivity.this, getString(R.string.test_vibration_dialog_text));
        mProgress = Storage.getPro(MeasureActivity.this, getString(R.string.test_temporary_progress_text));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsToast = true;
        reconnect = false;
        mac = mInstance.getUserInfo(Constants.DEVICEADDRESS);
        setConfig();
        if (mac.equals("") || TextUtils.isEmpty(mac)) {
            Toast.makeText(this, R.string.test_vibration_toast_nomac_text, Toast.LENGTH_SHORT).show();
        } else {
            //mProgressDialog.show();
            int status = ClientManager.getClient().getConnectStatus(mConfig.sensor_mac);
            if (status == com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED) {
                mDevice = BluetoothUtils.getRemoteDevice(mConfig.sensor_mac);
                ClientManager.getClient().registerConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
                ClientManager.getClient().clearRequest(mDevice.getAddress(), 0);
                mLlName.setVisibility(View.VISIBLE);
                mLlConnect.setVisibility(View.GONE);
                mTvSensor.setText(mDevice.getName());
                if (TextUtils.isEmpty(mDevice.getName())) {
                    mTvSensor.setText(mInstance.getUserInfo(Constants.DEVICENAME));
                }
                mIsConnect = true;
                //setGattProfile(profile);
                // mProgressDialog.dismiss();
                mTvTips.setVisibility(View.GONE);
                return;
            }
            getBLE();
        }
    }

    public void getBLE() {
        boolean opened = ClientManager.getClient().isBluetoothOpened();
        if (opened) {
            setlianjie();
        } else {
            ClientManager.getClient().openBluetooth();
            ClientManager.getClient().registerBluetoothStateListener(mBluetoothStateListener);
        }
    }

    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (openOrClosed) {
                if (mac.equals("") || TextUtils.isEmpty(mac)) {
                    Toast.makeText(MeasureActivity.this, R.string.test_vibration_toast_nomac_text, Toast.LENGTH_SHORT).show();
                    return;
                }
                setlianjie();
            }
        }

    };

    public void setConfig() {
        mConfig = loadConfig();
        setConfigNews();
        mConfig.setMac(mac);
        if (mCheckItemInfo.getCheckType().equals(Constants.CW)) {
            mConfig.setType(CountString.TMP);
        } else {
            if (mCheckItemInfo.getZhenDong_Type().equals(CountString.ZHENDONG_TYPE)) {
                mConfig.setType(CountString.ACC);
            } else {
                mConfig.setType(CountString.VEL);
            }
        }
    }

    public void setlianjie() {
        if (link) return;
        link = true;
        mProgressDialog.show();
        mDevice = BluetoothUtils.getRemoteDevice(mConfig.sensor_mac);
        ClientManager.getClient().registerConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(3000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(9000)
                .build();

        ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                BluetoothLog.v(String.format("profile:\n%s", profile));
//                      mTvTitle.setText(String.format("%s", mDevice.getAddress()));
//                      mPbar.setVisibility(View.GONE);
//                      mListView.setVisibility(View.VISIBLE);
                BluetoothLog.v(String.format("profile:\n%s", new Object[]{profile}));
                if (code == 0) {
                    if (Constants.FIRST_CONNECTION) {
                        ClientManager.getClient().refreshCache(mDevice.getAddress());
                        Constants.FIRST_CONNECTION = false;
                    }
                    mProgressDialog.dismiss();
                    link = false;
                    ClientManager.getClient().clearRequest(mDevice.getAddress(), 0);
                    mLlName.setVisibility(View.VISIBLE);
                    mLlConnect.setVisibility(View.GONE);
                    mTvSensor.setText(mDevice.getName());
                    if (TextUtils.isEmpty(mDevice.getName())) {
                        mTvSensor.setText(mInstance.getUserInfo(Constants.DEVICENAME));
                    }
                    mIsConnect = true;
                    //setGattProfile(profile);
                    // mProgressDialog.dismiss();
                    mTvTips.setVisibility(View.GONE);
                } else {
                    mProgressDialog.dismiss();
                    link = false;
                    mLlName.setVisibility(View.GONE);
                    mIsConnect = false;
                    mLlConnect.setVisibility(View.VISIBLE);
                    // mProgressDialog.dismiss();
                    if (mIsToast) {
                        Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_client_text, Toast.LENGTH_SHORT).show();
                    }
                    mIsToast = false;
                }
            }
        });
    }

    private BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                reconnect = false;
                // Toast.makeText(VibraTionActivity.this, "1", Toast.LENGTH_SHORT).show();
                ClientManager.getClient().readRssi(mConfig.sensor_mac, mBleReadRssiResponse);
                ClientManager.getClient().read(mConfig.sensor_mac, mServiceBat, mCharacterBat, mReadRspBattery);
            } else if (status == STATUS_DISCONNECTED) {
                if (reconnect) {
                    return;
                }
                reconnect = true;
                // Toast.makeText(VibraTionActivity.this, "2", Toast.LENGTH_SHORT).show();
                int statu = ClientManager.getClient().getConnectStatus(mConfig.sensor_mac);
                if (statu == com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED) return;
                mLlName.setVisibility(View.GONE);
                mLlConnect.setVisibility(View.VISIBLE);
                mIsConnect = false;
                getBLE();
                if (isStart) {
                    mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                    mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                    mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                    ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
                    isHold = true;
                    isStart = false;
                }
            }
        }
    };

    public void setOnclick() {
        mImgThermal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView(mFileInfo.getFileName());
            }
        });
        mBtRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mac.equals("") || TextUtils.isEmpty(mac)) {
                    Toast.makeText(MeasureActivity.this, R.string.test_vibration_toast_nomac_text, Toast.LENGTH_SHORT).show();
                } else {
                    mInSampling = true;
                    boolean opened = ClientManager.getClient().isBluetoothOpened();
                    if (!opened) {
                        Toast.makeText(MeasureActivity.this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int status = ClientManager.getClient().getConnectStatus(mConfig.sensor_mac);
                    if (status != com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED) {
                        setlianjie();
                    }
                    if (mIsConnect) {
                        if (!isStart) {
                            ClientManager.getClient().notify(mDevice.getAddress(), mService, mCharacter2, mNotifyRsp);
                            mBtRetest.setText(R.string.test_vibration_activity_start_text);
                            mBtPreservation.setText(R.string.test_vibration_activity_hold_text);
                            mBtPreservation.setBackgroundColor(getResources().getColor(R.color.measure_bt_preservation));
                            mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                            isHold = false;
                            mDialog.show();
                            setDialog();
                        } else {
                            mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                            mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                            mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                            mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                            ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
                            isHold = true;
                        }
                        isStart = !isStart;
                    } else {
                        Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_connect_text, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBack) {
                    String string = TimeType.dateToString();
                    String time = mInstance.getUserInfo(Constants.START_TM);
                    String userInfo = mInstance.getUserInfo(Constants.LABLE);
                    long time1 = TimeType.getTime(string);
                    long time2 = TimeType.getTime(time);
                    int i = (int) (time1 - time2);
                    mBlackDao.setLabel(userInfo, string, i);
                    if (mDevice != null && !mDevice.equals("")) {
                        Constants.ISLIANJIE = false;
                        ClientManager.getClient().disconnect(mDevice.getAddress());
                        ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
                        //invalidateOptionsMenu();
                    }
                    finish();
                } else {
                    mLlChart.setVisibility(View.GONE);
                    mLlCount.setVisibility(View.VISIBLE);
                    mLlAbout.setVisibility(View.GONE);
                    mLineName.setText(getResources().getString(R.string.test_vibration_line_name_text));
                    mIsBack = true;
                }
            }
        });
        mLlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_stop_text, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MeasureActivity.this, BluetoothSetActivity.class);
                    startActivity(intent);
                }
            }
        });
        mLlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_stop_text, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MeasureActivity.this, BluetoothSetActivity.class);
                    startActivity(intent);
                }
            }
        });
        mStDerail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();
            }
        });
        mBtPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_stop_text, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ResultValue)) {
                    Toast.makeText(MeasureActivity.this, "无结果值", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isHold) {
                    mProgress.show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isHold) {
                            if (mDevice != null) {
                                ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
                                //ClientManager.getClient().disconnect(mDevice.getAddress());
                                ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
                                //invalidateOptionsMenu();
                            }
                            String ExceptionYN = null;
                            String ExceptionID = "";
                            float b = 0;
                            if ("".equals(ResultValue)) {
                                if (messageEventValue == null) {
                                    MeasureActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgress.dismiss();
                                            Toast.makeText(MeasureActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return;
                                }
                            }
                            //是否产生异常还有窗内和窗外
                            try {
                                if (mConfig.sample_type.equals(CountString.TMP)) {
                                    if (ResultValue.equals("")) {
                                        ResultValue = messageEventValue.getTemperature() + "";
                                        b = messageEventValue.getTemperature();
                                    } else {
                                        b = Float.parseFloat(ResultValue);
                                    }
                                } else if (mConfig.sample_type.equals(CountString.ACC)) {
                                    if (ResultValue.equals("")) {
                                        ResultValue = messageEventValue.getRmsValue() + "";
                                        b = messageEventValue.getRmsValue();
                                    } else {
                                        b = Float.parseFloat(ResultValue);
                                    }
                                } else {
                                    if (ResultValue.equals("")) {
                                        ResultValue = messageEventValue.getPeakValue() + "";
                                        b = messageEventValue.getPeakValue();
                                    } else {
                                        b = Float.parseFloat(ResultValue);
                                    }
                                }
                                ExceptionYN = setException(mCheckItemInfo, b);
                            } catch (Exception e) {
                                Log.e("CONG", "run: ", e);
                            }

                            //加上标识，这个检查项是否有图表数据
                            if (mCheckItemInfo.getZhendong_PP() != null) {
                                if (mCheckItemInfo.getZhendong_PP().equals("1")) {
                                    ExceptionID = "1";
                                }
                            } else {
                                ExceptionID = "";
                            }
                            String VibFeatures = "";
                            if (!mConfig.sample_type.equals(CountString.TMP) && messageEventValue != null) {
                                VibFeatures = "[{FeatureName:PeakValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())) + "},{FeatureName:EffectiveValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())) +
                                        "},{FeatureName:P2PValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getPeak())) + "}]";
                            }
                            String format = TimeType.dateToString();
                            long nowTime = TimeType.getNowTime();
                            long time = TimeType.getTime(mStartTime);
                            CheckItem checkItem = new CheckItem("", mCheckItemInfo.getTaskID(), mCheckItemInfo.getLabelID(), mCheckItemInfo.getMobjectCode(), mCheckItemInfo.getMobjectName(), mCheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mUsername, ResultValue, mConfig.sample_freq, "0", ExceptionYN.equals("0") ? "0" : "1", ExceptionID, "", (int) (nowTime - time), mCheckItemInfo.getGroupName(), "", "", false,
                                    mCheckItemInfo.getTaskItemID(), TaskType, mCheckItemInfo.getLineID(), UserCode, mGroupname, ExceptionYN, mCheckItemInfo.getShiftName(), VibFeatures, mConfig.sample_nums, mCheckItemInfo.getZhenDong_Type());
                            if (mIsFNC) {
                                if (mIsGai) {
                                    if (!item1.isEmpty()) {
                                        mBlackDao.setChangeSubmit(checkItem, item1.get(0).getResult_ID());
                                    }
                                    if (!mFileInfo.getFileName().equals("")) {
                                        mBlackDao.delCheckItemFile(item1.get(0).getResult_ID());
                                        mFileInfo.setCheckItem_ID(item1.get(0).getResult_ID() + "");
                                        mBlackDao.addResultFile(mFileInfo);
                                    }
                                    if (mCheckItemInfo.getZhendong_PP() != null) {
                                        if (mCheckItemInfo.getZhendong_PP().equals("1") & !OscFragment2.mList.isEmpty()) {
                                            mBlackDao.delTastChart(checkItem.getResult_ID());
                                            for (int j = 0; j < OscFragment2.mList.size(); j++) {
                                                mBlackDao.addChart("" + item1.get(0).getResult_ID(), OscFragment2.mList.get(j));
                                            }
                                        }
                                    }
                                    finish();
                                } else {
                                    checkItem.setTaskType("9");
                                    doUpload(checkItem);
                                }
                            } else {
                                int i = mBlackDao.addCheckItem(checkItem);
                                if (!mFileInfo.getFileName().equals("")) {
                                    mFileInfo.setCheckItem_ID(i + "");
                                    mBlackDao.addResultFile(mFileInfo);
                                }
                                if (mCheckItemInfo.getZhendong_PP() != null) {
                                    if (mCheckItemInfo.getZhendong_PP().equals("1")) {
                                        for (int j = 0; j < OscFragment2.mList.size(); j++) {
                                            mBlackDao.addChart("" + i, OscFragment2.mList.get(j));
                                        }
                                    }
                                }
                                setmEsh();
                                if (mIsWatch) {
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                                    intent.putExtras(bundle);
                                    setResult(2, intent);
                                    finish();
                                } else {
                                    getJump();
                                }
                                handler.obtainMessage(1).sendToTarget();
                            }
                        }
                    }
                }).start();
            }
        });
        mBtTemperaturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckItemInfo.getCheckType().equals(Constants.CW)) {
                    mPro = new ProgressDialog(MeasureActivity.this);
                    mPro.setMessage(getString(R.string.testdelegate_progress_text));
                    mPro.setCancelable(false);
                    mPro.show();
                    mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                    mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                    mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                    ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
                    isStart = false;
                    Intent intent = new Intent(MeasureActivity.this, GLPreviewActivity.class);
                    startActivityForResult(intent, 1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            MeasureActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPro.dismiss();
                                    mPro = null;
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        mBtSpeedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<Float> list = new ArrayList<>();
//                Intent intent = new Intent(MeasureActivity.this, ChartActivity.class);
//                intent.putExtra(Constants.INTENTFLOAT, (Serializable) list);
//                intent.putExtra(Constants.TYPE, getResources().getString(R.string.test_vibration_rb_signal2_text));
//                startActivityForResult(intent, 3);
                if (mCheckItemInfo.getZhendong_PP().equals("1")) {
                    mIsBack = false;
                    isOsc = false;
                    mLineName.setText(getResources().getString(R.string.test_vibration_rb_signal2_text));
                    mLlAbout.setVisibility(View.VISIBLE);
                    mLlChart.setVisibility(View.VISIBLE);
                    mLlCount.setVisibility(View.GONE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    OscFragment2 chartFragment = OscFragment2.newInstance("", "");
                    transaction.replace(R.id.ll_about, chartFragment);
                    transaction.commit();
                } else {
                    Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_bt_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtAccelerationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<Float> list = new ArrayList<>();
//                Intent intent = new Intent(MeasureActivity.this, ChartActivity.class);
//                intent.putExtra(Constants.INTENTFLOAT, (Serializable) list);
//                intent.putExtra(Constants.TYPE, getResources().getString(R.string.test_vibration_rb_signal1_text));
//                startActivityForResult(intent, 3);
                if (mCheckItemInfo.getZhendong_PP().equals("1")) {
                    mIsBack = false;
                    isOsc = false;
                    mLineName.setText(getResources().getString(R.string.test_vibration_rb_signal1_text));
                    mLlAbout.setVisibility(View.VISIBLE);
                    mLlChart.setVisibility(View.VISIBLE);
                    mLlCount.setVisibility(View.GONE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    OscFragment2 chartFragment = OscFragment2.newInstance("", "");
                    transaction.replace(R.id.ll_about, chartFragment);
                    transaction.commit();
                } else {
                    Toast.makeText(MeasureActivity.this, R.string.upcom_measure_toast_bt_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mImgState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeasureActivity.this, DeviceStateActivity.class);
                intent.putExtra(Constants.CHECKITEMINFO, mZDlist.get(0));
                startActivityForResult(intent, 2);
            }
        });
    }

    public void setmEsh() {
        if (messageEventValue == null) {
            return;
        }
        if ("".equals(mEshCheckItem.getCheckType()) || mEshCheckItem.getCheckType() == null) {
            return;
        }
        String ExceptionYN = null;
        String ExceptionID = "";
        String ResultValue = "";
        float b = 0;
        //是否产生异常还有窗内和窗外
        try {
            if ("CW".equals(mEshCheckItem.getCheckType())) {
                if (ResultValue.equals("")) {
                    ResultValue = messageEventValue.getTemperature() + "";
                    b = messageEventValue.getTemperature();
                } else {
                    b = Float.parseFloat(ResultValue);
                }
            } else {
                if ("A".equals(mEshCheckItem.getZhenDong_Type())) {
                    if (ResultValue.equals("")) {
                        ResultValue = messageEventValue.getRmsValue() + "";
                        b = messageEventValue.getPeakValue();
                    } else {
                        b = Float.parseFloat(ResultValue);
                    }
                } else {
                    if (ResultValue.equals("")) {
                        ResultValue = messageEventValue.getPeakValue() + "";
                        b = messageEventValue.getRmsValue();
                    } else {
                        b = Float.parseFloat(ResultValue);
                    }
                }
            }
            ExceptionYN = setException(mEshCheckItem, b);
        } catch (Exception e) {
            Log.e("CONG", "run: ", e);
        }

        //加上标识，这个检查项是否有图表数据
        if (mEshCheckItem.getZhendong_PP() != null) {
            if (mEshCheckItem.getZhendong_PP().equals("1")) {
                ExceptionID = "1";
            }
        } else {
            ExceptionID = "";
        }
        String VibFeatures = "";
        if (!mEshCheckItem.getCheckType().equals(Constants.CW) && messageEventValue != null) {
            VibFeatures = "[{FeatureName:PeakValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())) + "},{FeatureName:EffectiveValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())) +
                    "},{FeatureName:P2PValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getPeak())) + "}]";
        }
        String format = TimeType.dateToString();
        long nowTime = TimeType.getNowTime();
        long time = TimeType.getTime(mStartTime);
        CheckItem checkItem = new CheckItem("", mEshCheckItem.getTaskID(), mEshCheckItem.getLabelID(), mEshCheckItem.getMobjectCode(), mEshCheckItem.getMobjectName(), mEshCheckItem.getCheckItemID(), mStartTime, format, mAccountid, mUsername, ResultValue, mConfig.sample_freq, "0", ExceptionYN.equals("0") ? "0" : "1", ExceptionID, "", (int) (nowTime - time), mEshCheckItem.getGroupName(), "", "", false,
                mEshCheckItem.getTaskItemID(), TaskType, mEshCheckItem.getLineID(), UserCode, mGroupname, ExceptionYN, mEshCheckItem.getShiftName(), VibFeatures, mConfig.sample_nums, mCheckItemInfo.getZhenDong_Type());
        int i = mBlackDao.addCheckItem(checkItem);
        if (!mFileInfo.getFileName().equals("")) {
            mFileInfo.setCheckItem_ID(i + "");
            mBlackDao.addResultFile(mFileInfo);
        }
        if (mEshCheckItem.getZhendong_PP() != null) {
            if (mEshCheckItem.getZhendong_PP().equals("1")) {
                for (int j = 0; j < OscFragment2.mList.size(); j++) {
                    mBlackDao.addChart("" + i, OscFragment2.mList.get(j));
                }
            }
        }
        mZDlist.remove(mEshCheckItem);
    }

    public void doUpload(final CheckItem checkItem) {
        MeasureActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.show();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.CHECKINFO, checkItem);
                    JSONObject target = jsonObject.getJSONObject(Constants.CHECKINFO);
                    String string = target.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.CHECKITEM, string);
                    String result = loadDataFromWeb.getupload();
                    if (result.equals("true")) {
                        mBlackDao.setCheckAutonomy(checkItem.getTask_ID(), checkItem.getCheckItem_ID());
                        mBlackDao.setLabelTM(TimeType.dateToString());
                        MeasureActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.dismiss();
                                finish();
                            }
                        });
                    } else {
                        MeasureActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.dismiss();
                                Toast.makeText(MeasureActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    MeasureActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.dismiss();
                            Toast.makeText(MeasureActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public String setException(CheckItemInfo checkItem, float b) {
        String Exception_YN = "0";
        float UpperLimit1 = checkItem.getUpperLimit1();
        float UpperLimit2 = checkItem.getUpperLimit2();
        float UpperLimit3 = checkItem.getUpperLimit3();
        float UpperLimit4 = checkItem.getUpperLimit4();
        float LowerLimit1 = checkItem.getLowerLimit1();
        float LowerLimit2 = checkItem.getLowerLimit2();
        float LowerLimit3 = checkItem.getLowerLimit3();
        float LowerLimit4 = checkItem.getLowerLimit4();
        try {
            if (checkItem.getAlarmType_ID().equals("0")) {
                if (UpperLimit1 != -100 & b > UpperLimit1) {
                    Exception_YN = "1";
                }
                if (UpperLimit2 != -100 & b > UpperLimit2) {
                    Exception_YN = "2";
                }
                if (UpperLimit3 != -100 & b > UpperLimit3) {
                    Exception_YN = "3";
                }
                if (UpperLimit4 != -100 & b > UpperLimit4) {
                    Exception_YN = "4";
                }
            } else if (checkItem.getAlarmType_ID().equals("1")) {
                if (LowerLimit1 != -100 & b < LowerLimit1) {
                    Exception_YN = "1";
                }
                if (LowerLimit2 != -100 & b < LowerLimit2) {
                    Exception_YN = "2";
                }
                if (LowerLimit3 != -100 & b < LowerLimit3) {
                    Exception_YN = "3";
                }
                if (LowerLimit4 != -100 & b < LowerLimit4) {
                    Exception_YN = "4";
                }
            } else if (checkItem.getAlarmType_ID().equals("2")) {
                if (UpperLimit1 != -100 & LowerLimit1 != -100 & b < UpperLimit1 & b > LowerLimit1) {
                    Exception_YN = "1";
                }
                if (UpperLimit2 != -100 & LowerLimit2 != -100 & b < UpperLimit2 & b > LowerLimit2) {
                    Exception_YN = "2";
                }
                if (UpperLimit3 != -100 & LowerLimit3 != -100 & b < UpperLimit3 && b > LowerLimit3) {
                    Exception_YN = "3";
                }
                if (UpperLimit4 != -100 & LowerLimit4 != -100 & b < UpperLimit4 & b > LowerLimit4) {
                    Exception_YN = "4";
                }
            } else if (checkItem.getAlarmType_ID().equals("3")) {
                if (UpperLimit1 != -100 & LowerLimit1 != -100 & (b > UpperLimit1 || b < LowerLimit1)) {
                    Exception_YN = "1";
                }
                if (UpperLimit2 != -100 & LowerLimit2 != -100 & (b > UpperLimit2 || b < LowerLimit2)) {
                    Exception_YN = "2";
                }
                if (UpperLimit3 != -100 & LowerLimit3 != -100 & (b > UpperLimit3 || b < LowerLimit3)) {
                    Exception_YN = "3";
                }
                if (UpperLimit4 != -100 & LowerLimit4 != -100 & (b > UpperLimit4 || b < LowerLimit4)) {
                    Exception_YN = "4";
                }
            }
        } catch (Exception e) {
            Logger.e("onClick: " + e);
        }
        return Exception_YN;
    }

    public void setName() {
        List<CheckItem> accurate = mBlackDao.getCheckAccurate(mCheckItemInfo.getCheckItemID(), mCheckItemInfo.getTaskPlanStartTime());
        if (mCheckItemInfo.getCheckType().equals(Constants.CW)) {
            mTvTemperatureSection.setText(getString(R.string.upcom_meter_activity_tvparmupper_text) + (accurate.isEmpty() ? "" : accurate.get(0).getComplete_TM()));
            mTvSection4.setText(getString(R.string.upcom_meter_activity_tvstanard_text) + (accurate.isEmpty() ? "" : accurate.get(0).getResultValue()));
        } else {
            if (mCheckItemInfo.getZhenDong_Type().equals(CountString.ZHENDONG_TYPE)) {
                mTvSection1.setText(getString(R.string.upcom_meter_activity_tvstanard_text) + (accurate.isEmpty() ? "" : accurate.get(0).getResultValue()));
                mTvSpeedSection.setText(getString(R.string.upcom_meter_activity_tvparmupper_text) + (accurate.isEmpty() ? "" : accurate.get(0).getComplete_TM()));
            } else {
                mTvSection2.setText(getString(R.string.upcom_meter_activity_tvstanard_text) + (accurate.isEmpty() ? "" : accurate.get(0).getResultValue()));
                mTvAccelerationSection.setText(getString(R.string.upcom_meter_activity_tvparmupper_text) + (accurate.isEmpty() ? "" : accurate.get(0).getComplete_TM()));
            }
        }
        if (mCheckItemInfo.getESTStandard() != null) {
            mTvEststandard.setText(CountString.LEFT_BRACKETS + mCheckItemInfo.getESTStandard() + CountString.RIGHT_BRACKETS);
        }
        mLineName.setText(mCheckItemInfo.getLabelName());
        mTvEquipment.setText(mCheckItemInfo.getMobjectName());
        mTvInspect.setText(mCheckItemInfo.getCheckItemDesc());
    }

    public void getDialog() {
        View view = LayoutInflater.from(MeasureActivity.this).inflate(R.layout.dialog_skip, null, false);
        final Dialog builder = new Dialog(MeasureActivity.this, R.style.update_dialog);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        TextView tvTaskName = view.findViewById(R.id.tv_taskName);
        Button btPreservation = view.findViewById(R.id.bt_preservation);
        Button btComplete = view.findViewById(R.id.bt_complete);
        tvTaskName.setTypeface(HiApplication.MEDIUM);
        btComplete.setTypeface(HiApplication.MEDIUM);
        btPreservation.setTypeface(HiApplication.MEDIUM);
        btPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String format = TimeType.dateToString();
//                long nowTime = TimeType.getNowTime();
//                long time = TimeType.getTime(mStartTime);
//                for (int i = 0; i < mZDlist.size(); i++) {
//                    CheckItemInfo mcheckItemInfo = mZDlist.get(i);
//                    CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mUsername, "", null, "1", "0", "", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false,
//                            mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "");
//                    mBlackDao.addCheckItem(checkItem);
//                }
//                mZDlist.clear();
                if (mIsWatch) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                    intent.putExtras(bundle);
                    setResult(2, intent);
                    finish();
                } else {
                    getJump();
                }
                builder.dismiss();
            }
        });
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStDerail.setChecked(true);
                builder.dismiss();
            }
        });
        builder.setContentView(view);
        builder.show();
    }

    public void getJump() {
        mZDlist.remove(mCheckItemInfo);
        if (mZDlist.size() < 1) {
            mBlackDao.delMobject(mCheckItemInfo.getMobjectCode());
            Intent intent = new Intent(MeasureActivity.this, WatchActivity.class);
            intent.putExtra(Constants.ISRECORD, true);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MeasureActivity.this, MeasureActivity.class);
            intent.putExtra(Constants.ZD, (Serializable) mZDlist);
            startActivity(intent);
        }
        MeasureActivity.this.finish();
    }

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID paramAnonymousUUID1, UUID paramAnonymousUUID2, byte[] paramAnonymousArrayOfByte) {
            // Log.e("osc---wzl---", " " + paramAnonymousArrayOfByte.length + ": " + ByteUtils.byteToString(paramAnonymousArrayOfByte));
            if ((paramAnonymousUUID1.equals(mService)) && (paramAnonymousUUID2.equals(mCharacter2))) {
                int i = paramAnonymousArrayOfByte[0] & 0xFF;
                mPacketRecvFlag[i] = true;
                if (i == 0) {
                    //Log.i("osc", "isTmp:" + mConfig.isTmp);
                    short temp = (short) (paramAnonymousArrayOfByte[1] << 8 & 0xFF00 | paramAnonymousArrayOfByte[2] & 0xFF);
                    mRecvTemp = temp;
                    mRecvType = paramAnonymousArrayOfByte[3] & 0xFF;
                    mRecvRate = paramAnonymousArrayOfByte[4] << 8 & 0xFF00 | paramAnonymousArrayOfByte[5] & 0xFF;
                    mRecvNums = paramAnonymousArrayOfByte[6] << 8 & 0xFF00 | paramAnonymousArrayOfByte[7] & 0xFF;
                    mTotalPacket = paramAnonymousArrayOfByte[8] & 0xFF;
                    mRecvCoef = paramAnonymousArrayOfByte[12] << 24 & 0xFF000000 | paramAnonymousArrayOfByte[11] << 16 & 0xFF0000 | paramAnonymousArrayOfByte[10] << 8 & 0xFF00 | paramAnonymousArrayOfByte[9] & 0xFF;
                    Log.i("osc", "totl:" + mTotalPacket);
                    Log.i("osc", "coef:" + Float.intBitsToFloat(mRecvCoef));
                    Log.i("osc", "type:" + mRecvType);
                    Log.i("osc", "rate:" + mRecvRate);
                    Log.i("osc", "nums:" + mRecvNums);
                    //        Log.i("wzlosc", " temp:" + mRecvTemp + " type:" + mRecvType);
                } else {

                    ByteUtils.copy(mRecvBytes, paramAnonymousArrayOfByte, (i - 1) * 19, 1);
                }
                notifyWatchTimer();
            }
        }

        @Override
        public void onResponse(int paramAnonymousInt) {
            if (paramAnonymousInt == 0) {
                Log.i("osc", "open notify" + "????????");
                startSample(mConfig.sample_type);
            }
        }
    };

    private void startSample(String strSampleType) {
        byte[] arrayOfByte = new byte[20];

        arrayOfByte[0] = 'S';
        arrayOfByte[1] = 8;
        arrayOfByte[2] = 0;
        if (strSampleType.compareTo("ACC") == 0) {
            //arrayOfByte[2] = 1;
            arrayOfByte[2] = 17;  //ACC|TMP
        } else if (strSampleType.compareTo("VEL") == 0) {
            //arrayOfByte[2] = 2;
            arrayOfByte[2] = 18;  //VEL|TMP
        } else if (strSampleType.compareTo("DIS") == 0) {
            arrayOfByte[2] = 4;
        } else if (strSampleType.compareTo("HAC") == 0) {
            arrayOfByte[2] = 8;
        } else if (strSampleType.compareTo("TMP") == 0) {
            if ("CZ".equals(mEshCheckItem.getCheckType())) {
                if (mEshCheckItem.getZhenDong_Type().equals("S")) {
                    arrayOfByte[2] = 18;
                } else {
                    arrayOfByte[2] = 17;
                }
            } else {
                arrayOfByte[2] = 16;
            }
        }
        //if (mConfig.isTmp) {
        //  arrayOfByte[2] = ((byte)(arrayOfByte[2] | 0x10));
        //}
        arrayOfByte[3] = ((byte) (mConfig.sample_freq >> 8));
        arrayOfByte[4] = ((byte) (mConfig.sample_freq & 0xFF));
        arrayOfByte[5] = ((byte) (mConfig.sample_nums >> 8));
        arrayOfByte[6] = ((byte) (mConfig.sample_nums & 0xFF));
        arrayOfByte[7] = ((byte) (mConfig.sample_emi >> 8));
        arrayOfByte[8] = ((byte) (mConfig.sample_emi & 0xFF));
        arrayOfByte[9] = ((byte) (arrayOfByte[2] ^ arrayOfByte[3] ^ arrayOfByte[4] ^ arrayOfByte[5] ^ arrayOfByte[6] ^ arrayOfByte[7] ^ arrayOfByte[8]));
        ClientManager.getClient().write(mConfig.sensor_mac, mService, mCharacter1, arrayOfByte, mWriteRsp);

    }

    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int paramAnonymousInt) {
            if (paramAnonymousInt == 0) {
                ClientManager.getClient().read(mConfig.sensor_mac, mService, mCharacter1, mReadRsp);
            }
        }
    };

    private final BleReadResponse mReadRsp = new BleReadResponse() {
        @Override
        public void onResponse(int paramAnonymousInt, byte[] paramAnonymousArrayOfByte) {
            if (paramAnonymousInt == 0) {
                Log.i("osc", "read response");
                if (paramAnonymousArrayOfByte[0] == 'S') {
                    if (paramAnonymousArrayOfByte[1] == 3) {
                        if ((paramAnonymousArrayOfByte[2] == 'E') && (paramAnonymousArrayOfByte[3] == 'R') && (paramAnonymousArrayOfByte[4] == 'R')) {
                            Logger.i("cmd err");
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MeasureActivity.this);
                            alertDialog.setTitle("");
                            alertDialog.setMessage("");
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int) {
                                }
                            });
                            alertDialog.show();
//                            mbtnPlay.setText(com.example.ldw.wls.R.string.btnStart);
                            mInSampling = false;
                            ClientManager.getClient().unnotify(mConfig.sensor_mac, mService, mCharacter2, mUnnotifyRsp);
                        }
                    } else {
                        if ((paramAnonymousArrayOfByte[1] == 2) && (paramAnonymousArrayOfByte[2] == 'O') && (paramAnonymousArrayOfByte[3] == 'K')) {
                            Logger.i("cmd ok");
                            int i = 0;
                            while (i < 256) {
                                mPacketRecvFlag[i] = false;
                                i += 1;
                            }
                        }
                    }
                }
            }
        }
    };

    private final BleUnnotifyResponse mUnnotifyRsp = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int paramAnonymousInt) {
            if (paramAnonymousInt == 0) {
                Log.i("ble", "close notify");
            }
        }
    };

    private void notifyWatchTimer() {
        Log.i("osc", "timeout");
        if (mRecvTimer != null) {
            mRecvTimer.cancel();
            mRecvTimer = null;
        }
        if (mRecvTimerTask != null) {
            mRecvTimerTask.cancel();
            mRecvTimerTask = null;
        }
        mRecvTimer = new Timer();
        mRecvTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!mInSampling) {
                    return;
                }
                byte[] resend = new byte[20];
                int i = 0;
                int j = 0;
                for (i = 0; i < resend.length; i++) {
                    resend[i] = (byte) 0xFF;
                }

                for (i = 0; i < mTotalPacket; i++) {
                    if (mPacketRecvFlag[i] != true) {
                        Log.i("osc", "flag:" + i + " " + mPacketRecvFlag[i]);
                        resend[j++] = (byte) i;
                        if (j == 20) {
                            break;
                        }
                    }
                }
                if (j > 0) {
                    ClientManager.getClient().write(mConfig.sensor_mac, mService, mCharacter3, (byte[]) resend, mWriteRsp3);
                } else {
                    Log.i("osc", "recv finish");
                    i = 0;
                    while (i < mTotalPacket) {
                        mPacketRecvFlag[i] = false;
                        i += 1;
                    }
                    mTotalPacket = 0;
                    ClientManager.getClient().readRssi(mConfig.sensor_mac, mBleReadRssiResponse);
                    ClientManager.getClient().read(mConfig.sensor_mac, mServiceBat, mCharacterBat, mReadRspBattery);
                    startSample(mConfig.sample_type);
                    //i = 0;
                    //while (i < mRecvNums)
                    //{
                    //  j = (short)(int)(Math.sin(1005.3096491487338D * i / (mRecvRate * 2.56F)) * Math.random() * 10.0D);
                    //  mRecvBytes[(i * 2)] = ((byte)(j >> 8));
                    //  mRecvBytes[(i * 2 + 1)] = ((byte)j);
                    //  i += 1;
                    //}
                    //mRecvCoef = Float.floatToIntBits(1.0E-4F);
//                    if ((mvpOsc.getCurrentItem() == 0) || (mvpOsc.getCurrentItem() == 1))
//                    {
                    messageEventValue = new MessageEventValue(mRecvBytes, mRecvType, mRecvRate, mRecvNums, mRecvCoef, mRecvTemp);
                    //mList.add(messageEventValue);
                    handler.obtainMessage(0).sendToTarget();
                    EventBus.getDefault().post(messageEventValue);
                    if (!mCheckItemInfo.getCheckType().equals(Constants.CW)) {
                        if (mCheckItemInfo.getZhendong_PP() != null) {
                            if (mCheckItemInfo.getZhendong_PP().equals("1") || (mEshCheckItem.getZhendong_PP() != null && mEshCheckItem.getZhendong_PP().equals("1"))) {
                                MessageEventWave messageEventWave = new MessageEventWave(mRecvBytes, mRecvType, mRecvRate, mRecvNums, mRecvCoef, mRecvTemp);
                                EventBus.getDefault().post(messageEventWave);
                                if (isOsc) {
                                    setLine(messageEventWave);
                                }
                            }
                        }
                    }
//                        MessageEventWave messageEventWave = new MessageEventWave(mRecvBytes, mRecvType, mRecvRate, mRecvNums, mRecvCoef, mRecvTemp);
//                        EventBus.getDefault().post(messageEventWave);
                    //   }

                }
            }
        };
        mRecvTimer.schedule(mRecvTimerTask, 200L);
    }

    public void setLine(MessageEventWave messageEventWave) {
        OscFragment2.mList.clear();
        ArrayList waveArrayList = new ArrayList();
        float[] arrayOfWave = new float[messageEventWave.getSampleNums()];
        float[] arrayOfFft = new float[messageEventWave.getSampleNums() / 2];
        float[] arrayOfValue = new float[12];
        if (messageEventWave.isWatch()) {
            messageEventWave.getFloat(arrayOfWave, arrayOfFft, arrayOfValue);
        } else {
            messageEventWave.getFloatWave(arrayOfWave, arrayOfFft, arrayOfValue);
        }
//        mUserConfig = ((CurrentConfig) mListener.onFragmentInteraction(0, 101, 0.0F, 0.0F, null));
//        if (mUserConfig.sample_type.equals("ACC")) {
//            mtvVunit.setText("m/s^2");
//        } else if (mUserConfig.sample_type.equals("VEL")) {
//            mtvVunit.setText("mm/s");
//        }
        float ms = 1000.0F / messageEventWave.getSampleRate();
        Log.i("osc", "ffts.length " + arrayOfWave.length + " ms:" + ms);
        for (int i = 0; i < arrayOfWave.length; i++) {
            OscFragment2.mList.add(arrayOfWave[i]);
            waveArrayList.add(new Entry(i * ms, arrayOfWave[i]));
        }
    }

    private final BleReadResponse mReadRspBattery = new BleReadResponse() {
        @Override
        public void onResponse(int paramAnonymousInt, byte[] paramAnonymousArrayOfByte) {
            if (paramAnonymousInt == 0) {
                Log.i("batt:", ByteUtils.byteToString(paramAnonymousArrayOfByte));
                paramAnonymousInt = paramAnonymousArrayOfByte[0] & 0xFF;
                if (paramAnonymousInt >= 90) {
                    mBatteryLevel = 100;
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text);
                } else if (paramAnonymousInt >= 75 && paramAnonymousInt < 90) {
                    mBatteryLevel = 80;
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text2);
                } else if (paramAnonymousInt >= 55 && paramAnonymousInt < 75) {
                    mBatteryLevel = 60;
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text3);
                } else if (paramAnonymousInt >= 30 && paramAnonymousInt < 55) {
                    mBatteryLevel = 40;
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text4);
                } else if (paramAnonymousInt >= 10 && paramAnonymousInt < 30) {
                    mBatteryLevel = 20;
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text5);
                } else {
                    mBatteryLevel = 10;
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text6);
                }
                if (paramAnonymousInt <= 20) {
                    mInstance.setUserInfo(Constants.SENSORTIME, TimeType.dateToString());
                } else {
                    mInstance.setUserInfo(Constants.SENSORTIME, "");
                }
                invalidateOptionsMenu();
            }
        }
    };

    private final BleReadRssiResponse mBleReadRssiResponse = new BleReadRssiResponse() {
        @Override
        public void onResponse(int paramAnonymousInt, Integer paramAnonymousInteger) {
            if (paramAnonymousInt == 0) {
                Log.i("osc", "rssi:" + paramAnonymousInteger);
                if (paramAnonymousInteger.intValue() > -51) {
                    mTvSignal.setText(R.string.upcom_measure_tv_signal_text);
                    mRssiLevel = 4;
                } else if (paramAnonymousInteger.intValue() >= -61) {
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text2);
                    mRssiLevel = 3;
                } else if (paramAnonymousInteger.intValue() >= -71) {
                    mRssiLevel = 2;
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text3);
                } else if (paramAnonymousInteger.intValue() >= -81) {
                    mRssiLevel = 1;
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text4);
                } else {
                    mRssiLevel = 0;
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text5);
                }
                invalidateOptionsMenu();
            }
        }
    };

    private final BleWriteResponse mWriteRsp3 = new BleWriteResponse() {
        @Override
        public void onResponse(int paramAnonymousInt) {
            if (paramAnonymousInt == 0) {
            }
        }
    };

    private CurrentConfig loadConfig() {
        CurrentConfig currentConfig = null;
        File file = new File("/data/data/com.example.ldw.wls/databases/currentconfig");
        if (!file.exists()) {
            file.delete();
            currentConfig = new CurrentConfig();
            currentConfig.sample_type = "ACC";
            currentConfig.sample_freq = 1000;
            currentConfig.sample_nums = 512;
            currentConfig.sample_emi = 95;
            currentConfig.isTmp = false;
            currentConfig.sensor_mac = "";
            currentConfig.sensor_name = "";
            return currentConfig;
        }
        String json = null;
        try {
            json = new RandomAccessFile(file, "rwd").readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (CurrentConfig) new Gson().fromJson(json, CurrentConfig.class);
    }

    public void setConfigNews() {
        if (mCheckItemInfo.getZhenDong_Freq() != null) {
            switch (mCheckItemInfo.getZhenDong_Freq()) {
                case "0":
                    mConfig.setFreq(100);
                    break;
                case "1":
                    mConfig.setFreq(200);
                    break;
                case "2":
                    mConfig.setFreq(500);
                    break;
                case "3":
                    mConfig.setFreq(1000);
                    break;
                case "4":
                    mConfig.setFreq(2000);
                    break;
                case "5":
                    mConfig.setFreq(5000);
                    break;
                case "6":
                    mConfig.setFreq(10000);
                    break;
            }
        }
        if (mCheckItemInfo.getZhenDong_Points() != null) {
            switch (mCheckItemInfo.getZhenDong_Points()) {
                case "0":
                    mConfig.setNums(256);
                    break;
                case "1":
                    mConfig.setNums(512);
                    break;
                case "2":
                    mConfig.setNums(1024);
                    break;
                case "3":
                    mConfig.setNums(2048);
                    break;
                case "4":
                    mConfig.setNums(4096);
                    break;
            }
        }
    }

    public void setGattProfile(BleGattProfile paramBleGattProfile) {
        List<BleGattService> services = paramBleGattProfile.getServices();
        for (BleGattService service : services) {
            UUID uuid = service.getUUID();
            //mService = service.getUUID();
            //Log.e("wzl----", "s: " + mService.toString() + "     uuid:" + uuid.toString());
            if (uuid.toString().compareTo("0000fff0-0000-1000-8000-00805f9b34fb") == 0) {
                mService = service.getUUID();

                List<BleGattCharacter> characters = service.getCharacters();
//                Log.e("wzl---", "chracter length = " + characters.size());
                for (BleGattCharacter character : characters) {
                    Log.e("osc", "c: " + character.getUuid().toString());
                    String str = character.getUuid().toString();
                    if (str.compareTo("0000fff1-0000-1000-8000-00805f9b34fb") == 0) {
                        mCharacter1 = character.getUuid();
                    } else if (str.compareTo("0000fff2-0000-1000-8000-00805f9b34fb") == 0) {
                        mCharacter2 = character.getUuid();
                    } else if (str.compareTo("0000fff3-0000-1000-8000-00805f9b34fb") == 0) {
                        mCharacter3 = character.getUuid();
                    }
                }
            } else if (uuid.toString().compareTo("0000180f-0000-1000-8000-00805f9b34fb") == 0) {
                List<BleGattCharacter> characters = service.getCharacters();
                for (BleGattCharacter character : characters) {
                    Log.i("osc", "c: " + character.getUuid().toString());
                    if (character.getUuid().toString().compareTo("00002a19-0000-1000-8000-00805f9b34fb") == 0) {
                        mCharacterBat = character.getUuid();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
//        if (mIsOff) {
        mInstance.setUserInfo(Constants.PROTIME, TimeType.dateToString());
        if (mDevice != null) {
            ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
            ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
        }
        ClientManager.getClient().unregisterBluetoothStateListener(mBluetoothStateListener);
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (mPro != null) {
            mPro.dismiss();
            mPro = null;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mRecvTimer != null) {
            mRecvTimer.cancel();
            mRecvTimer = null;
        }
        if (mRecvTimerTask != null) {
            mRecvTimerTask.cancel();
            mRecvTimerTask = null;
        }
        //}
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            String extra = data.getStringExtra(Constants.CW);
            String str = data.getStringExtra(Constants.INTENTFLOAT);
            if (!str.equals("")) {
                ResultValue = str.substring(0, str.indexOf("º"));
                mTvTemperatureValue.setText(ResultValue);
                mFileInfo.setFileName(extra);
                mFileInfo.setFileType(CountString.PHOTO);
                if (extra.equals("")) {
                    mImgThermal.setVisibility(View.GONE);
                } else {
                    mImgThermal.setVisibility(View.VISIBLE);
                    Glide.with(MeasureActivity.this).load(extra).into(mImgThermal);
                }
                mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
            }
        } else if (requestCode == 2 && resultCode == 5) {
            startActivity(new Intent(MeasureActivity.this, WatchActivity.class));
            finish();
        }
    }

    @Override
    public Object onFragmentInteraction(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Object paramObject) {
        if ((paramInt1 == 0) && (paramInt2 == 101)) {     // get config
            return mConfig;
        }
        return null;
    }

    public void setDialog() {
        handler.sendEmptyMessageDelayed(2, 2500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //mIsOff = true;
            String string = TimeType.dateToString();
            String time = mInstance.getUserInfo(Constants.START_TM);
            String userInfo = mInstance.getUserInfo(Constants.LABLE);
            long time1 = TimeType.getTime(string);
            long time2 = TimeType.getTime(time);
            int i = (int) (time1 - time2);
            mBlackDao.setLabel(userInfo, string, i);
            if (mDevice != null && !mDevice.equals("")) {
                Constants.ISLIANJIE = false;
                ClientManager.getClient().disconnect(mDevice.getAddress());
                ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
                invalidateOptionsMenu();
            }
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void getView(String s) {
        final Dialog dia;
        dia = new Dialog(MeasureActivity.this, R.style.edit_AlertDialog_style);
        View view = LayoutInflater.from(MeasureActivity.this).inflate(R.layout.start_dialog, null, false);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.star_image);
        imageView.enable();
        File file = new File(s);
        Glide.with(MeasureActivity.this).load(file).into(imageView);
        dia.setCanceledOnTouchOutside(true);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });
        dia.show();
        Display display = MeasureActivity.this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dia.setContentView(view, layoutParams);
    }
}
