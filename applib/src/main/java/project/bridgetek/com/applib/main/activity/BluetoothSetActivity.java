package project.bridgetek.com.applib.main.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleReadRssiResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.BluetoothAdapter;
import project.bridgetek.com.applib.main.toos.ClientManager;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

public class BluetoothSetActivity extends Activity {
    private ImageView mIcBack, mIcSensor;
    private TextView mLineName, mTvSensor, mTvSignal, mTvQuantity, mTvTaskName;
    private Button mBtScaveng;
    private List<SearchResult> mDevices = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private ListView mListView;
    ProgressDialog mProgressDialog, mDialog;
    LocalUserInfo mUserInfo;
    private String mBlueNmae, mName, mAddress;
    private BluetoothDevice mDevice;
    private String mDeviceName;
    private Thread thread;
    private UUID mServiceBat = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    private UUID mCharacterBat = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    private LinearLayout mLlSignal, mLlCount;
    private TextView mTvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
        initUI();
        setOnclick();
    }

    public void setFont() {
        mLineName.setTypeface(HiApplication.BOLD);
        mTvSensor.setTypeface(HiApplication.BOLD);
        mTvSignal.setTypeface(HiApplication.BOLD);
        mTvQuantity.setTypeface(HiApplication.BOLD);
        mTvTaskName.setTypeface(HiApplication.BOLD);
        mBtScaveng.setTypeface(HiApplication.REGULAR);
        mTvTips.setTypeface(HiApplication.BOLD);
    }

    public void initUI() {
        mUserInfo = LocalUserInfo.getInstance(BluetoothSetActivity.this);
        mIcBack = findViewById(R.id.ic_back);
        mIcSensor = findViewById(R.id.ic_sensor);
        mLineName = findViewById(R.id.line_name);
        mTvSensor = findViewById(R.id.tv_sensor);
        mTvSignal = findViewById(R.id.tv_signal);
        mTvQuantity = findViewById(R.id.tv_quantity);
        mTvTaskName = findViewById(R.id.tv_taskName);
        mBtScaveng = findViewById(R.id.bt_scaveng);
        mListView = findViewById(R.id.lv_bluetooth);
        mLlSignal = findViewById(R.id.ll_signal);
        mTvTips = findViewById(R.id.tv_tips);
        mLlCount = findViewById(R.id.ll_count);
        mLlCount.setVisibility(View.GONE);
        mTvTips.setVisibility(View.VISIBLE);
        mName = mUserInfo.getUserInfo(Constants.DEVICENAME);
        mAddress = mUserInfo.getUserInfo(Constants.DEVICEADDRESS);
        if (TextUtils.isEmpty(mAddress)) {
            Constants.FIRST_CONNECTION = true;
        }
        mLlSignal.setVisibility(View.INVISIBLE);
        setFont();
        mBluetoothAdapter = new BluetoothAdapter(BluetoothSetActivity.this, mDevices);
        mListView.setAdapter(mBluetoothAdapter);
        mProgressDialog = Storage.getPro(BluetoothSetActivity.this, getString(R.string.setup_bluetooth_progress_text));
        mDialog = Storage.getPro(BluetoothSetActivity.this, getString(R.string.setup_bluetooth_dialog_text));
        Log.e("mm","mAddress="+mAddress);
        if (!mAddress.equals("")) {
            setName(mName, mAddress);
        } else {
            searchDevice();
        }
        ClientManager.getClient().registerBluetoothStateListener(mBluetoothStateListener);
    }

    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (!openOrClosed) {
                mProgressDialog.dismiss();
                ClientManager.getClient().stopSearch();
            }
        }

    };

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mBtConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String string = mTvSensor.getText().toString();
//                if (string.equals("") || TextUtils.isEmpty(string)) {
//                    Toast.makeText(BluetoothSetActivity.this, R.string.setup_bluetooth_toast_notsensor_text, Toast.LENGTH_SHORT).show();
//                } else {
//                    int status = ClientManager.getClient().getConnectStatus(mAddress);
//                    if (status == 0) {
//                        mDialog.show();
//                        setConnect();
//                    } else {
//                        Toast.makeText(BluetoothSetActivity.this, R.string.setup_bluetooth_toast_yessensor_text, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        mBtScaveng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevice();
            }
        });
    }

    public void setConnect() {
        mDevice = BluetoothUtils.getRemoteDevice(mAddress);
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(2)
                .setConnectTimeout(1000)
                .setServiceDiscoverRetry(2)
                .setServiceDiscoverTimeout(1000)
                .build();

        ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                BluetoothLog.v(String.format("profile:\n%s", profile));
                BluetoothLog.v(String.format("profile:\n%s", new Object[]{profile}));
                if (code == 0) {
                    ClientManager.getClient().refreshCache(mDevice.getAddress());
                    mDialog.dismiss();
                    Toast.makeText(BluetoothSetActivity.this, R.string.setup_bluetooth_toast_yessignal_text, Toast.LENGTH_LONG).show();
                    mDeviceName = mDevice.getAddress();
                    mIcSensor.setBackgroundResource(R.drawable.ic_sensor_green_normal);
                    mLlSignal.setVisibility(View.VISIBLE);
                    feedMultiple();
                    mUserInfo.setUserInfo(Constants.DEVICENAME, mDevice.getName());
                    mUserInfo.setUserInfo(Constants.DEVICEADDRESS, mDevice.getAddress());
                    mLlCount.setVisibility(View.VISIBLE);
                    mTvTips.setVisibility(View.GONE);
                } else {
                    mDialog.dismiss();
                    Toast.makeText(BluetoothSetActivity.this, R.string.setup_bluetooth_toast_nosignal_text, Toast.LENGTH_SHORT).show();
                    mIcSensor.setBackgroundResource(R.drawable.ic_sensor);
                }
            }
        });
    }

    public void setName(String name, String address) {
//        mName = name;
//        if (address.equals(mDeviceName)) {
//            mIcSensor.setBackgroundResource(R.drawable.ic_sensor_green_normal);
//            mLlSignal.setVisibility(View.VISIBLE);
//        } else {
//            mIcSensor.setBackgroundResource(R.drawable.ic_sensor);
//            mLlSignal.setVisibility(View.INVISIBLE);
//        }
        mAddress = address;
        String[] split = mAddress.split(":");
        String s1 = split[split.length - 1];
        mTvSensor.setText(name + ":" + s1);
        setConnect();
        mDialog.show();
    }

    private void searchDevice() {
        boolean opened = ClientManager.getClient().isBluetoothOpened();
        if (!opened) {
            Toast.makeText(BluetoothSetActivity.this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(2000, 1)
                .searchBluetoothClassicDevice(2000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        ClientManager.getClient().search(request, mSearchResponse);
    }

    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            BluetoothLog.w("MainActivity.onSearchStarted");
            mProgressDialog.show();
            mDevices.clear();
            mBluetoothAdapter.notifyDataSetChanged();
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            BluetoothLog.w("MainActivity.onDeviceFounded " + device.device.getAddress());
            if (!mDevices.contains(device)) {
                if (device.getName().indexOf(CountString.ACCSENSE) != -1 || device.getName().indexOf(CountString.JS100) != -1) {
                    mDevices.add(device);
                }
            }
            Logger.i("onDeviceFounded: ");
            mBluetoothAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSearchStopped() {
            BluetoothLog.w("MainActivity.onSearchStopped");
            mProgressDialog.dismiss();
            mBluetoothAdapter.notifyDataSetChanged();
            if (mDevices.size() < 1) {
                Toast.makeText(BluetoothSetActivity.this, R.string.setup_bluetooth_toast_fail_text, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onSearchCanceled() {
            BluetoothLog.w("MainActivity.onSearchCanceled");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog = null;
        ClientManager.getClient().unregisterBluetoothStateListener(mBluetoothStateListener);
        if (mDevice != null) {
            if (Constants.FIRST_CONNECTION) {
                ClientManager.getClient().disconnect(mDeviceName);
            }
        }
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
                        ClientManager.getClient().readRssi(mDevice.getAddress(), mBleReadRssiResponse);
                        ClientManager.getClient().read(mDevice.getAddress(), mServiceBat, mCharacterBat, mReadRspBattery);
//                        try {
//                            Thread.sleep(2000L);
//                        } catch (InterruptedException localInterruptedException) {
//                            localInterruptedException.printStackTrace();
//                        }

                    }
                });
            }

        });
        thread.start();
    }

    private final BleReadRssiResponse mBleReadRssiResponse = new BleReadRssiResponse() {
        @Override
        public void onResponse(int paramAnonymousInt, Integer paramAnonymousInteger) {
            if (paramAnonymousInt == 0) {
                Logger.i(paramAnonymousInteger.intValue());
                if (paramAnonymousInteger.intValue() < -51) {
                    mTvSignal.setText(R.string.upcom_measure_tv_signal_text);
                } else if (paramAnonymousInteger.intValue() >= -61) {
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text2);
                } else if (paramAnonymousInteger.intValue() >= -71) {
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text3);
                } else if (paramAnonymousInteger.intValue() >= -81) {
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text4);
                } else {
                    mTvSignal.setText(R.string.setup_bluetooth_tvsignal_text5);
                }
                invalidateOptionsMenu();
            }
        }
    };

    private final BleReadResponse mReadRspBattery = new BleReadResponse() {
        @Override
        public void onResponse(int paramAnonymousInt, byte[] paramAnonymousArrayOfByte) {
            if (paramAnonymousInt == 0) {
                Logger.i(ByteUtils.byteToString(paramAnonymousArrayOfByte));
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
                invalidateOptionsMenu();
            }
        }
    };

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