package project.bridgetek.com.applib.main.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.flir.flironeexampleapplication.GLPreviewActivity;
import com.flir.flironeexampleapplication.util.StatusBarUtils;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.MessageEventWave;
import project.bridgetek.com.applib.main.fragment.OscFragment2;
import project.bridgetek.com.applib.main.toos.ClientManager;
import project.bridgetek.com.applib.main.toos.CurrentConfig;
import project.bridgetek.com.applib.main.toos.MessageEventValue;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

public class VibraTionActivity extends AppCompatActivity implements OscFragment2.OnFragmentInteractionListener {
    private LinearLayout mLlName, mLlConnect;
    private ImageView mImgGain,imSetting;
    LocalUserInfo mUserInfo;
    boolean mGain = true;
    private TextView tvLineName;
    private TextView mTvNotConnect, mTvConnect, mTvSensor, mTvSignal, mTvQuantity;
    private TextView mTvSpeedValue, mTvAccelerationValue, mTvTemperatureValue;
    private TextView mTvSpeedText, mTvAccelerationText, mTvTemperatureText;
    private TextView mTvSpeed, mTvAcceleration, mTvTemperature;
//    private Button mBtTemperaturePhoto,mBtAccelerationPhoto,mBtSpeedPhoto;
    private Button mBtPreservation, mBtRetest;
    private BluetoothDevice mDevice;
    private UUID mService = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    private UUID mCharacter2 = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    private UUID mCharacter1 = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private UUID mCharacter3 = UUID.fromString("0000fff3-0000-1000-8000-00805f9b34fb");
    private UUID mCharacterBat = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    private UUID mServiceBat = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    private boolean mInSampling = false;
    private boolean mIsStart = false;
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
    private Thread thread;
    ProgressDialog mProgressDialog, mDialog;
    ProgressDialog mPro;
    private boolean mConnected = false;
    //private NoScrollViewPager mvpOsc;
    private LinearLayout mLlAcceleration, mLlSpeed;
    private float mACCList;
    private float mVELList;
    private float mTMP;
    String mac;
    private LinearLayout mLlAbout, mLlCount;
    private boolean isStart = false, isHold = false;
    private boolean mIsConnect = false;
    private String mExtra = "";
    private boolean mIsBack = true;
    private boolean mIsToast = true;
    private TextView mLineName;
    MessageEventValue messageEventValue;
    private TextView mTvPeakValue, mTvVirtualValue, mTvPeakPeak;
    private LinearLayout mLlChart;
    private CurrentConfig mConfig;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                //Toast.makeText(VibraTionActivity.this, "" + mConfig.sample_type, Toast.LENGTH_SHORT).show();
                //Toast.makeText(VibraTionActivity.this, "" + mConfig.sample_type, Toast.LENGTH_SHORT).show();
                if (mConfig.sample_type.equals(CountString.TMP)) {
                    mTvTemperatureValue.setText(String.valueOf(messageEventValue.getTemperature()));
                    mTMP = messageEventValue.getTemperature();
                } else if (mConfig.sample_type.equals(CountString.ACC)) {
                    mTvAccelerationValue.setText(Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())));
                    mVELList = 0;
                    mACCList = messageEventValue.getPeakValue();
                    mTvTemperatureValue.setText(String.valueOf(messageEventValue.getTemperature()));
                    mTMP = messageEventValue.getTemperature();
                    mTvPeakValue.setText(getString(R.string.test_vibration_tv_peak_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())));
                    mTvVirtualValue.setText(getString(R.string.test_vibration_tv_valid_text) + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())));
                    mTvPeakPeak.setText(getString(R.string.test_vibra_tv_peakvalue_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeak())));
                } else {
                    mTvSpeedValue.setText(Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())));
                    mACCList = 0;
                    mVELList = messageEventValue.getRmsValue();
                    mTvTemperatureValue.setText(String.valueOf(messageEventValue.getTemperature()));
                    mTMP = messageEventValue.getTemperature();
                    mTvPeakValue.setText(getString(R.string.test_vibration_tv_peak_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())));
                    mTvVirtualValue.setText(getString(R.string.test_vibration_tv_valid_text) + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())));
                    mTvPeakPeak.setText(getString(R.string.test_vibra_tv_peakvalue_text) + Storage.formateRate(String.valueOf(messageEventValue.getPeak())));
                }
            } else if (msg.what == 2) {
                mDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_vibra_tion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
        boolean opened = ClientManager.getClient().isBluetoothOpened();
        if (!opened) {
            ClientManager.getClient().openBluetooth();
        }
        boolean isOpen = isLocServiceEnable(this);
        if(!isOpen){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("需要开启定位功能！")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
    }
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    public void setFont() {
        mBtPreservation.setTypeface(HiApplication.MEDIUM);
        mBtRetest.setTypeface(HiApplication.MEDIUM);
//        mBtSpeedPhoto.setTypeface(HiApplication.MEDIUM);
        //   mBtAccelerationPhoto.setTypeface(HiApplication.MEDIUM);
        //  mBtTemperaturePhoto.setTypeface(HiApplication.MEDIUM);
        mTvTemperature.setTypeface(HiApplication.BOLD);
        mTvAcceleration.setTypeface(HiApplication.BOLD);
        mTvSpeed.setTypeface(HiApplication.BOLD);
        mTvTemperatureText.setTypeface(HiApplication.MEDIUM);
        mTvAccelerationText.setTypeface(HiApplication.MEDIUM);
        mTvSpeedText.setTypeface(HiApplication.MEDIUM);
        mTvTemperatureValue.setTypeface(HiApplication.BOLD);
        mTvAccelerationValue.setTypeface(HiApplication.BOLD);
        mTvSpeedValue.setTypeface(HiApplication.BOLD);
        mTvNotConnect.setTypeface(HiApplication.BOLD);
        mTvConnect.setTypeface(HiApplication.BOLD);
        mTvSensor.setTypeface(HiApplication.BOLD);
        mTvSignal.setTypeface(HiApplication.BOLD);
        mTvQuantity.setTypeface(HiApplication.BOLD);
        mLineName.setTypeface(HiApplication.BOLD);
    }

    public void initUI() {
        mConfig = loadConfig();
        mUserInfo = LocalUserInfo.getInstance(VibraTionActivity.this);
        imSetting = findViewById(R.id.line_setting);
        mBtRetest = findViewById(R.id.bt_retest);
        mBtPreservation = findViewById(R.id.bt_preservation);
        mTvSpeedValue = findViewById(R.id.tv_speed_value);
        mTvAccelerationValue = findViewById(R.id.tv_acceleration_value);
        mTvTemperatureValue = findViewById(R.id.tv_temperature_value);
//        mBtSpeedPhoto = findViewById(R.id.bt_speed_photo);
   //     mBtAccelerationPhoto = findViewById(R.id.bt_acceleration_photo);
  //      mBtTemperaturePhoto = findViewById(R.id.bt_temperature_photo);
        mTvSpeedText = findViewById(R.id.tv_speed_text);
        mTvAccelerationText = findViewById(R.id.tv_acceleration_text);
        mTvTemperatureText = findViewById(R.id.tv_temperature_text);
        mTvSpeed = findViewById(R.id.tv_speed);
        mTvAcceleration = findViewById(R.id.tv_acceleration);
        mTvTemperature = findViewById(R.id.tv_temperature);
        mLlName = findViewById(R.id.ll_name);
        mLlName.setVisibility(View.GONE);
        mLlConnect = findViewById(R.id.ll_connect);
        mTvNotConnect = findViewById(R.id.tv_notConnect);
        mTvConnect = findViewById(R.id.tv_Connect);
        mTvSensor = findViewById(R.id.tv_sensor);
        mTvSignal = findViewById(R.id.tv_signal);
        mTvQuantity = findViewById(R.id.tv_quantity);
        mLlAbout = findViewById(R.id.ll_about);
        mLlAbout.setVisibility(View.VISIBLE);
        mIsBack = false;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        OscFragment2 chartFragment = OscFragment2.newInstance("", "");
        transaction.replace(R.id.ll_about, chartFragment);
        transaction.commit();
//        mLlAbout.setVisibility(View.GONE);
        mLlCount = findViewById(R.id.ll_count);
        mLineName = findViewById(R.id.line_name);
        mLlAcceleration = findViewById(R.id.ll_acceleration);
        mLlSpeed = findViewById(R.id.ll_speed);
        mTvPeakPeak = findViewById(R.id.tv_peak_peak);
        mTvPeakValue = findViewById(R.id.tv_peak_value);
        mTvVirtualValue = findViewById(R.id.tv_virtual_value);
        mLlChart = findViewById(R.id.ll_chart);
        mLlSpeed.setVisibility(View.GONE);
        setFont();

        mProgressDialog = Storage.getProCan(VibraTionActivity.this, getString(R.string.upcom_measure_progress_text));
        mDialog = Storage.getPro(VibraTionActivity.this, getString(R.string.test_vibration_dialog_text));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsToast = true;
        mac = mUserInfo.getUserInfo(Constants.DEVICEADDRESS);
        if (mac.equals("") || TextUtils.isEmpty(mac)) {
          //  Toast.makeText(this, R.string.test_vibration_toast_nomac_text, Toast.LENGTH_SHORT).show();
        } else {
            int status = ClientManager.getClient().getConnectStatus(mConfig.sensor_mac);
            if (status == com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED) {
                ClientManager.getClient().clearRequest(mDevice.getAddress(), 0);
                mLlName.setVisibility(View.VISIBLE);
                mLlConnect.setVisibility(View.GONE);
                mIsConnect = true;
                mTvSensor.setText(mDevice.getName());
                if (TextUtils.isEmpty(mDevice.getName())) {
                    mTvSensor.setText(mUserInfo.getUserInfo(Constants.DEVICENAME));
                }
                //setGattProfile(profile);
                //feedMultiple();
                //mProgressDialog.dismiss();
                return;
            }
            getBLE();
        }
    }
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
    public void getBLE() {
        boolean opened = ClientManager.getClient().isBluetoothOpened();
        mConfig.setMac(mac);
        if (opened) {
            setConnect();
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
                //    Toast.makeText(VibraTionActivity.this, R.string.test_vibration_toast_nomac_text, Toast.LENGTH_SHORT).show();
                    return;
                }
                setConnect();
            }
        }

    };

    public void setConnect() {
        mProgressDialog.show();
        mDevice = BluetoothUtils.getRemoteDevice(mConfig.sensor_mac);
        mDevice.fetchUuidsWithSdp();
        ClientManager.getClient().registerConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(2)
                .setConnectTimeout(4000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(9000)
                .build();

        ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
            //  @SuppressLint("MissingPermission")
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
                    ClientManager.getClient().clearRequest(mDevice.getAddress(), 0);
                    mLlName.setVisibility(View.VISIBLE);
                    mLlConnect.setVisibility(View.GONE);
                    mIsConnect = true;
                    mTvSensor.setText(mDevice.getName());
                    if (TextUtils.isEmpty(mDevice.getName())) {
                        mTvSensor.setText(mUserInfo.getUserInfo(Constants.DEVICENAME));
                    }
                    setGattProfile(profile);
                    //feedMultiple();
                    mProgressDialog.dismiss();
                } else {
                    mIsConnect = false;
                    mLlName.setVisibility(View.GONE);
                    mLlConnect.setVisibility(View.VISIBLE);
//                    mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                    mBtRetest.setText(R.string.upcom_measure_bt_retest_text);
                    mProgressDialog.dismiss();
                    if (mIsToast) {
                        Toast.makeText(VibraTionActivity.this, R.string.upcom_measure_toast_client_text, Toast.LENGTH_SHORT).show();
                    }
                    mIsToast = false;
                }
            }
        });
    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                // Toast.makeText(VibraTionActivity.this, "1", Toast.LENGTH_SHORT).show();
                ClientManager.getClient().readRssi(mConfig.sensor_mac, mBleReadRssiResponse);
                ClientManager.getClient().read(mConfig.sensor_mac, mServiceBat, mCharacterBat, mReadRspBattery);
            } else if (status == STATUS_DISCONNECTED) {
                int statu = ClientManager.getClient().getConnectStatus(mDevice.getAddress());
                if (statu == com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED) return;
                mIsConnect = false;
                mLlName.setVisibility(View.GONE);
                mLlConnect.setVisibility(View.VISIBLE);
                getBLE();
                if (isStart) {
                    mInSampling = false;
                    mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                    mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                    mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                    ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
                    isHold = true;
                    isStart = false;
                }
                // Toast.makeText(VibraTionActivity.this, "2", Toast.LENGTH_SHORT).show();
                //setConnect();
            }
        }
    };

    public void setOnclick() {
        imSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VibraTionActivity.this,VibraSettingActivity.class);
                intent.putExtra("config",mConfig);
                startActivityForResult(intent,2);
            }
        });

//        mIcBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mIsBack) {
//                    finish();
//                } else {
//                    mLlCount.setVisibility(View.VISIBLE);
//                    mLlAbout.setVisibility(View.GONE);
//                    mLineName.setText(getResources().getString(R.string.test_vibration_line_name_text));
//                    mIsBack = true;
//                }
//            }
//        });
        mLlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInSampling) {
                    if (mIsStart) {
                        mIsStart = false;
                        mInSampling = false;
                        Intent intent = new Intent(VibraTionActivity.this, BluetoothSetActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VibraTionActivity.this, R.string.upcom_measure_toast_stop_text, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(VibraTionActivity.this, BluetoothSetActivity.class);
                    startActivity(intent);
                }
            }
        });
        mLlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInSampling) {
                    Toast.makeText(VibraTionActivity.this, R.string.upcom_measure_toast_stop_text, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(VibraTionActivity.this, BluetoothSetActivity.class);
                    startActivity(intent);
                }
            }
        });
        //热成像 TODO
//        mBtTemperaturePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPro = new ProgressDialog(VibraTionActivity.this);
//                mPro.setMessage(getString(R.string.testdelegate_progress_text));
//                mPro.setCancelable(false);
//                mPro.show();
//                mInSampling = false;
//                mBtRetest.setText(R.string.upcom_measure_set_retest_text);
//                mBtPreservation.setText(R.string.over_undete_btcomplete_text);
//                mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
//                mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
//                ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
//                isStart = false;
//                Intent intent = new Intent(VibraTionActivity.this, GLPreviewActivity.class);
//                startActivityForResult(intent, 1);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        VibraTionActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mPro.dismiss();
//                                mPro = null;
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });

        mBtRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mac.equals("") || TextUtils.isEmpty(mac)) {
           //         Toast.makeText(VibraTionActivity.this, R.string.test_vibration_toast_nomac_text, Toast.LENGTH_SHORT).show();
                } else {
                    boolean opened = ClientManager.getClient().isBluetoothOpened();
                    if (!opened) {
                        Toast.makeText(VibraTionActivity.this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int status = ClientManager.getClient().getConnectStatus(mConfig.sensor_mac);
                    if (status != com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED) {
                        setConnect();
                    }
                    if (mIsConnect) {
                        if (!isStart) {
                            ClientManager.getClient().notify(mDevice.getAddress(), mService, mCharacter2, mNotifyRsp);
                            mBtRetest.setText(R.string.test_vibration_activity_start_text);
                            mIsStart = true;
                            mBtPreservation.setText(R.string.test_vibration_activity_hold_text);
                            mBtPreservation.setBackgroundColor(getResources().getColor(R.color.measure_bt_preservation));
                            mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                            mInSampling = true;
                            isHold = false;
                 //           mImgGain.setBackgroundResource(R.mipmap.btn_down_arrow);

                            mGain = true;
                            mDialog.show();
                            setDialog();
                        } else {
                            mInSampling = false;
                            mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                            mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                            mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                            mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                            ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
                            isHold = true;
                        }
                        isStart = !isStart;
                    } else {
                        Toast.makeText(VibraTionActivity.this, R.string.upcom_measure_toast_connect_text, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        mBtPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                if (isHold) {
//                    mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
//                    mBtPreservation.setBackgroundColor(getResources().getColor(R.color.measure_bt_preservation));
//                    String VibFeatures = "";
//                    if ((!mConfig.sample_type.equals(CountString.TMP) && messageEventValue == null) || (mConfig.sample_type.equals(CountString.TMP) && mTMP == 0)) {
//                        Toast.makeText(VibraTionActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (!mConfig.sample_type.equals(CountString.TMP) && messageEventValue != null) {
//                        VibFeatures = "[{FeatureName:PeakValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getPeakValue())) + "},{FeatureName:EffectiveValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getRmsValue())) +
//                                "},{FeatureName:P2PValue,FeatureValue:" + Storage.formateRate(String.valueOf(messageEventValue.getPeak())) + "}]";
//                    }
//                    Intent intent = new Intent(VibraTionActivity.this, TemporaryActivity.class);
//                    intent.putExtra(CountString.TMP, mTMP);
//                    intent.putExtra(CountString.ACC, mACCList);
//                    intent.putExtra(CountString.VEL, mVELList);
//                    intent.putExtra(Constants.PHOTO, mExtra);
//                    intent.putExtra(Constants.REANALYSE, mConfig.sample_freq);
//                    intent.putExtra(Constants.VIBFEATURES, VibFeatures);
//                    intent.putExtra(Constants.NUMS, mConfig.sample_nums);
//                    startActivity(intent);
//                    isHold = false;
//                }
            }
        });
//        mBtSpeedPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (mConfig.sample_type.equals("VEL")) {
////                    Intent intent = new Intent(VibraTionActivity.this, ChartActivity.class);
//////                    intent.putExtra(Constants.INTENTFLOAT, (Serializable) mACCList);
////                    //intent.putExtra(Constants.TYPE, getResources().getString(R.string.test_vibration_rb_signal2_text));
////                    intent.putExtra(Constants.TYPE, (Serializable) mConfig);
////                    startActivityForResult(intent, 3);
////                }
//                if (mConfig.sample_type.equals(CountString.VEL)) {
//                    mIsBack = false;
//                    mLineName.setText(getResources().getString(R.string.test_vibration_rb_signal2_text));
//                    mLlAbout.setVisibility(View.VISIBLE);
//                    mLlCount.setVisibility(View.GONE);
//                    mLlChart.setVisibility(View.VISIBLE);
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    OscFragment2 chartFragment = OscFragment2.newInstance("", "");
//                    transaction.replace(R.id.ll_about, chartFragment);
//                    transaction.commit();
//                }
//            }
//        });
        //TODO
//        mBtAccelerationPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (mConfig.sample_type.equals("ACC")) {
////                    Intent intent = new Intent(VibraTionActivity.this, ChartActivity.class);
////                  //  intent.putExtra(Constants.INTENTFLOAT, (Serializable) mVELList);
////                    //intent.putExtra(Constants.TYPE, getResources().getString(R.string.test_vibration_rb_signal1_text));
////                    intent.putExtra(Constants.TYPE, (Serializable) mConfig);
////                    startActivityForResult(intent, 3);
////                }
//                if (mConfig.sample_type.equals(CountString.ACC)) {
//                    mIsBack = false;
//                    mLineName.setText(getResources().getString(R.string.test_vibration_rb_signal1_text));
//                    mLlAbout.setVisibility(View.VISIBLE);
//                    mLlChart.setVisibility(View.VISIBLE);
//                    mLlCount.setVisibility(View.GONE);
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    OscFragment2 chartFragment = OscFragment2.newInstance("", "");
//                    transaction.replace(R.id.ll_about, chartFragment);
//                    transaction.commit();
//                }
//            }
//        });
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

        // Log.e("wzl------------", "startSample" + "????????");
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
            arrayOfByte[2] = 16;
        }
        //if (mConfig.isTmp) {
        //  arrayOfByte[2] = ((byte)(arrayOfByte[2] | 0x10));
        //}
        arrayOfByte[3] = ((byte) (mConfig.sample_freq >> 8));
        arrayOfByte[4] = ((byte) (mConfig.sample_freq & 0xFF));
        arrayOfByte[5] = ((byte) (mConfig.sample_nums >> 8));
        arrayOfByte[6] = ((byte) (mConfig.sample_nums & 0xFF));
        //mConfig.sample_emi = Math.round(mConfig.sample_emi * 65535 / 100);
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
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(VibraTionActivity.this);
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
        //   Log.e("notifyWatchTimer---wzl", "timeout");
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
                    handler.obtainMessage(0).sendToTarget();
                    EventBus.getDefault().post(messageEventValue);
//                    } else {
                    if (!mIsBack) {
                        MessageEventWave messageEventWave = new MessageEventWave(mRecvBytes, mRecvType, mRecvRate, mRecvNums, mRecvCoef, mRecvTemp);
                        EventBus.getDefault().post(messageEventWave);
                    }
                    //   }

                }
            }
        };
        mRecvTimer.schedule(mRecvTimerTask, 200L);
    }

    private final BleReadResponse mReadRspBattery = new BleReadResponse() {
        @Override
        public void onResponse(int paramAnonymousInt, byte[] paramAnonymousArrayOfByte) {
            if (paramAnonymousInt == 0) {
                Log.i("batt:", ByteUtils.byteToString(paramAnonymousArrayOfByte));
                paramAnonymousInt = paramAnonymousArrayOfByte[0] & 0xFF;
                if (paramAnonymousInt >= 90) {
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text);
                } else if (paramAnonymousInt >= 75 && paramAnonymousInt < 90) {
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text2);
                } else if (paramAnonymousInt >= 55 && paramAnonymousInt < 75) {
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text3);
                } else if (paramAnonymousInt >= 30 && paramAnonymousInt < 55) {
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text4);
                } else if (paramAnonymousInt >= 10 && paramAnonymousInt < 30) {
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text5);
                } else {
                    mTvQuantity.setText(R.string.setup_bluetooth_tvquantity_text6);
                }
                if (paramAnonymousInt <= 20) {
                    mUserInfo.setUserInfo(Constants.SENSORTIME, TimeType.dateToString());
                } else {
                    mUserInfo.setUserInfo(Constants.SENSORTIME, "");
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
        if (mDevice != null) {
            ClientManager.getClient().unnotify(mDevice.getAddress(), mService, mCharacter2, mUnnotifyRsp);
            ClientManager.getClient().disconnect(mDevice.getAddress());
            ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
            invalidateOptionsMenu();
        }
        ClientManager.getClient().unregisterBluetoothStateListener(mBluetoothStateListener);
        if (mRecvTimer != null) {
            mRecvTimer.cancel();
            mRecvTimer = null;
        }
        if (mRecvTimerTask != null) {
            mRecvTimerTask.cancel();
            mRecvTimerTask = null;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (mPro != null) {
            mPro.dismiss();
            mPro = null;
        }
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
            String str = data.getStringExtra(Constants.INTENTFLOAT);
            if (!str.equals("")) {
                mExtra = data.getStringExtra(Constants.CW);
                String substring = str.substring(0, str.indexOf("º"));
                mTMP = Float.parseFloat(substring);
                mTvTemperatureValue.setText(str);
                mInSampling = false;
                mBtRetest.setText(R.string.upcom_measure_set_retest_text);
                mBtPreservation.setText(R.string.over_undete_btcomplete_text);
                mBtRetest.setBackgroundColor(getResources().getColor(R.color.theme));
                mBtPreservation.setBackgroundColor(getResources().getColor(R.color.theme));
                ClientManager.getClient().unnotify(mConfig.sensor_mac, mService, mCharacter2, mUnnotifyRsp);
                isHold = true;
            }
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            CurrentConfig config = data.getParcelableExtra("config");
             mConfig = config;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            OscFragment2 chartFragment = OscFragment2.newInstance("", "");
            transaction.replace(R.id.ll_about, chartFragment);
            transaction.commit();
            if (mConfig.sample_type.equals(CountString.ACC)) {
                mLlAcceleration.setVisibility(View.VISIBLE);
                mLlSpeed.setVisibility(View.GONE);
            } else if (mConfig.sample_type.equals(CountString.VEL)) {
                mLlAcceleration.setVisibility(View.GONE);
                mLlSpeed.setVisibility(View.VISIBLE);
            } else {
                mLlAcceleration.setVisibility(View.GONE);
                mLlSpeed.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Object onFragmentInteraction(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Object paramObject) {
        if ((paramInt1 == 0) && (paramInt2 == 101)) {     // get config
            return mConfig;
        }
        return null;
    }

    private void feedMultiple() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("osc", "runOnUiThread");

                        ClientManager.getClient().readRssi(mConfig.sensor_mac, mBleReadRssiResponse);
                        ClientManager.getClient().read(mConfig.sensor_mac, mServiceBat, mCharacterBat, mReadRspBattery);

                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException localInterruptedException) {
                            localInterruptedException.printStackTrace();
                        }

                    }
                });
            }

        });
        thread.start();
    }

    public static long unsigned4BytesToInt(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos;
        firstByte = (0x000000FF & ((int) buf[index]));
        secondByte = (0x000000FF & ((int) buf[index + 1]));
        thirdByte = (0x000000FF & ((int) buf[index + 2]));
        fourthByte = (0x000000FF & ((int) buf[index + 3]));
        index = index + 4;
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    public void setDialog() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
