package project.bridgetek.com.applib.main.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.melnykov.fab.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.WatchAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.ClientManager;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.FlashUtils;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WatchActivity extends Activity {
    private ImageView mIvBack;
    private ListView mLvWatch;
    private FloatingActionButton mLeftFab;
    private FloatingActionButton mRightFab;
    private List<CheckItemInfo> mList = new ArrayList<>();
    private List<CheckItemInfo> mGClist = new ArrayList<>();
    private List<CheckItemInfo> mCBlist = new ArrayList<>();
    private List<CheckItemInfo> mZDlist = new ArrayList<>();
    private WatchAdapter mWatchAdapter;
    private Switch mStDerail;
    private TextView mTvEquipment, mLineName;
    private FlashUtils mFlashUtils;
    private BlackDao mBlackDao;
    private Button mBtSubmit;
    private LocalUserInfo mInstance;
    private String mAccountid, mMusername, mGroupname, mTaskID;
    private CheckItemInfo mCheckItem;
    private String mStartTime;
    private boolean isRecord = false;
    private String LabelCode;
    ProgressDialog mDialog, mProgress;
    /**
     * Camera2相机硬件操作类
     */
    private CameraManager manager = null;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession = null;
    private CaptureRequest request = null;
    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private String cameraId = null;
    private boolean isSupportFlashCamera2 = false;
    private boolean mIsNFC = false;
    private boolean mIsChange = false;
    private boolean isClick = true;
    String UserCode;
    String TaskType;
    private final CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {

        public void onConfigured(CameraCaptureSession arg0) {
            captureSession = arg0;
            CaptureRequest.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {

                    builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

                    builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
                    builder.addTarget(surface);
                    request = builder.build();
                    captureSession.capture(request, null, null);

                } catch (@SuppressLint("NewApi") CameraAccessException e) {
                    Logger.e(e.getMessage());
                }
            }
        }

        public void onConfigureFailed(CameraCaptureSession arg0) {
        }
    };
    private boolean isOpen = false;
    ImageView mImgstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        mDialog = Storage.getPro(WatchActivity.this, getString(R.string.app_toos_storage_progress_text));
        mProgress = Storage.getPro(WatchActivity.this, getString(R.string.test_temporary_progress_text));
        initUI();
        setOnclick();
    }

    public void initUI() {
        mStartTime = TimeType.dateToString();
        mInstance = LocalUserInfo.getInstance(WatchActivity.this);
        mAccountid = mInstance.getUserInfo(Constants.ACCOUNTID);
        mMusername = mInstance.getUserInfo(Constants.USERNAME);
        mGroupname = mInstance.getUserInfo(Constants.GROUPNAME);
        LabelCode = mInstance.getUserInfo(Constants.LABELCODE);
        UserCode = mInstance.getUserInfo(Constants.GROUPID);
        TaskType = mInstance.getUserInfo(Constants.TASKTYPE);
        mTaskID = mInstance.getUserInfo(Constants.TASKID);
        mBlackDao = BlackDao.getInstance(WatchActivity.this);
        isRecord = getIntent().getBooleanExtra(Constants.ISRECORD, false);
        mIvBack = findViewById(R.id.ic_back);
        mLvWatch = findViewById(R.id.lv_watch);
        mRightFab = findViewById(R.id.right_fab);
        mLeftFab = findViewById(R.id.left_fab);
        mStDerail = findViewById(R.id.st_derail);
        mTvEquipment = findViewById(R.id.tv_equipment);
        mLineName = findViewById(R.id.line_name);
        mBtSubmit = findViewById(R.id.bt_submit);
        mImgstate = findViewById(R.id.img_state);
        mBtSubmit.setTypeface(HiApplication.MEDIUM);
        mTvEquipment.setTypeface(HiApplication.BOLD);
        mLineName.setTypeface(HiApplication.BOLD);
        mIsNFC = getIntent().getBooleanExtra(Constants.SUBMIT, false);
        mInstance.setUserInfo(Constants.PROUSER, mInstance.getUserInfo(Constants.ACCOUNTID));
        if ("1".equals(Constants.STARTSTOP)) {
            mImgstate.setVisibility(View.VISIBLE);
        } else {
            mImgstate.setVisibility(View.GONE);
        }
        if (mIsNFC) {
            mCheckItem = (CheckItemInfo) getIntent().getSerializableExtra(Constants.CHECKINFO);
            mIsChange = getIntent().getBooleanExtra(Constants.CHANGE, false);
            mGClist.add(mCheckItem);
        } else {
            List<CheckItemInfo> mobject = mBlackDao.getCheckMobject();
            if (mobject.size() > 0) {
                mCheckItem = mobject.get(0);
            } else {
                String string = TimeType.dateToString();
                String time = mInstance.getUserInfo(Constants.START_TM);
                String userInfo = mInstance.getUserInfo(Constants.LABLE);
                long time1 = TimeType.getTime(string);
                long time2 = TimeType.getTime(time);
                int i = (int) (time1 - time2);
                mBlackDao.setLabel(userInfo, string, i);
                String mac = mInstance.getUserInfo(Constants.DEVICEADDRESS);
                if (mac != null && !mac.equals("")) {
                    Constants.ISLIANJIE = false;
                    BluetoothDevice mDevice = BluetoothUtils.getRemoteDevice(mac);
                    ClientManager.getClient().disconnect(mDevice.getAddress());
                    invalidateOptionsMenu();
                }
                Toast.makeText(WatchActivity.this, R.string.upcom_watch_toast_over_text, Toast.LENGTH_SHORT).show();
                WatchActivity.this.finish();
                return;
            }
            if (!isRecord) {
                setLabel(mCheckItem);
            }
            Log.i("aaaa", "initUI: " + mobject);
            mGClist.clear();
            mCBlist.clear();
            mZDlist.clear();
            List<CheckItemInfo> checkItem = mBlackDao.getCheckItem(true, mTaskID, mCheckItem.getLineID(), mCheckItem.getLabelID(), mCheckItem.getMobjectCode());
            for (int i = 0; i < checkItem.size(); i++) {
                long nowTime = TimeType.getNowTime();
                if (TimeType.getTimeType(checkItem.get(i).getTaskPlanStartTime()) < nowTime && TimeType.getTimeType(checkItem.get(i).getTaskPlanEndTime()) > nowTime) {
                    mList.add(checkItem.get(i));
                }
            }
            //判断设备起停，任务是否需要进行
            boolean halt = setState();
            List<CheckItemInfo> over = mBlackDao.getCheckItemOver(mTaskID, mCheckItem.getLineID(), mCheckItem.getLabelID(), mCheckItem.getMobjectCode());
            for (int i = 0; i < over.size(); i++) {
                over.get(i).setSubmit(true);
            }
            if (!halt) {
                mGClist.addAll(over);
            }
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getCheckType() == Constants.GC || mList.get(i).getCheckType().equals(Constants.GC)) {
                    mGClist.add(mList.get(i));
                } else if (mList.get(i).getCheckType() == Constants.CB || mList.get(i).getCheckType().equals(Constants.CB)) {
                    mCBlist.add(mList.get(i));
                } else if (mList.get(i).getCheckType() == Constants.ZD || mList.get(i).getCheckType().equals(Constants.ZD) || mList.get(i).getCheckType() == Constants.CW || mList.get(i).getCheckType().equals(Constants.CW)) {
                    mZDlist.add(mList.get(i));
                }
            }
            if (mGClist.size() == 0) {
                if (mCBlist.size() > 0) {
                    Intent intent = new Intent(WatchActivity.this, MeterActivity.class);
                    intent.putExtra(Constants.CB, (Serializable) mCBlist);
                    intent.putExtra(Constants.ZD, (Serializable) mZDlist);
                    startActivity(intent);
                } else {
                    if (mZDlist.size() > 0) {
                        Intent intent = new Intent(WatchActivity.this, MeasureActivity.class);
                        intent.putExtra(Constants.ZD, (Serializable) mZDlist);
                        startActivity(intent);
                    } else {
                        mBlackDao.delMobject(mCheckItem.getMobjectCode());
                        startActivity(new Intent(WatchActivity.this, WatchActivity.class));
                    }
                }
                WatchActivity.this.finish();
            }
            for (int i = 0; i < mGClist.size(); i++) {
                mGClist.get(i).setCheckOrderNo(4);
            }
        }
        mWatchAdapter = new WatchAdapter(mGClist, WatchActivity.this, mTaskID, mIsNFC, mIsChange);
        mLvWatch.setAdapter(mWatchAdapter);
        if (mList.size() > 0) {
            mLineName.setText(mList.get(0).getLabelName());
            mTvEquipment.setText(mList.get(0).getMobjectName());
        }
        if (isLOLLIPOP()) {
            this.manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            initCamera2();
        }
    }

    public void setLabel(CheckItemInfo info) {
        Label label = new Label(info.getLabelID(), LabelCode, TaskType, info.getLineID(), TimeType.dateToString(), "", mAccountid, mMusername, UserCode, mGroupname,
                info.getShiftName(), info.getGroupName(), 0, info.getTaskID());
        int i = mBlackDao.addLabel(label);
        mInstance.setUserInfo(Constants.LABLE, i + "");
        mInstance.setUserInfo(Constants.START_TM, TimeType.dateToString());
    }

    private void initCamera2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                for (String cameraId : this.manager.getCameraIdList()) {
                    CameraCharacteristics characteristics = this.manager.getCameraCharacteristics(cameraId);
                    // 过滤掉前置摄像头
                    Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        continue;
                    }
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map == null) {
                        continue;
                    }
                    this.cameraId = cameraId;
                    // 判断设备是否支持闪光灯
                    this.isSupportFlashCamera2 = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                }

            } catch (CameraAccessException e) {
                Logger.e(e.getMessage());
            }
        }
    }

    public void setOnclick() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = TimeType.dateToString();
                String time = mInstance.getUserInfo(Constants.START_TM);
                String userInfo = mInstance.getUserInfo(Constants.LABLE);
                long time1 = TimeType.getTime(string);
                long time2 = TimeType.getTime(time);
                int i = (int) (time1 - time2);
                mBlackDao.setLabel(userInfo, string, i);
                String mac = mInstance.getUserInfo(Constants.DEVICEADDRESS);
                if (mac != null && !mac.equals("")) {
                    Constants.ISLIANJIE = false;
                    BluetoothDevice mDevice = BluetoothUtils.getRemoteDevice(mac);
                    ClientManager.getClient().disconnect(mDevice.getAddress());
                    invalidateOptionsMenu();
                }
                WatchActivity.this.finish();
            }
        });
        mLeftFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (isLOLLIPOP()) {
                    if (!isOpen) {
                        try {
                            openCamera2Flash();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            cameraDevice.close();
                        } catch (Exception e) {

                        }
                    }
                    isOpen = !isOpen;
                } else {
                    mFlashUtils.switchFlash();
                }
            }
        });
        mRightFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLvWatch.setSelectionAfterHeaderView();
            }
        });
        mStDerail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStDerail.isChecked()) {
                    getDialog();
                }
            }
        });
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean mAchieve = true;
                final long nowTime = TimeType.getNowTime();
                final long time = TimeType.getTime(mStartTime);
                for (int i = 0; i < mGClist.size(); i++) {
                    if (mGClist.get(i).getCheckOrderNo() != 4) {
                        if (!mGClist.get(i).isSubmit()) {
                            mAchieve = false;
                        }
                    }
                }
                if (!mAchieve) {
                    isClick = true;
                    Toast.makeText(WatchActivity.this, R.string.upcom_watch_activity_toast_allow_text, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAchieve) {
                    mProgress.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < mGClist.size(); i++) {
                                    String format = TimeType.dateToString();
                                    CheckItemInfo mcheckItemInfo = mGClist.get(i);
                                    if (!mcheckItemInfo.isSubmit()) {
                                        CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mMusername, "正常", null, "0", "0", "-10", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", true,
                                                mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "", null, "");
                                        if (mIsNFC) {
                                            if (mIsChange) {
                                                List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mTaskID, mcheckItemInfo.getCheckItemID());
                                                if (!item1.isEmpty()) {
                                                    mBlackDao.setChangeSubmit(checkItem, item1.get(0).getResult_ID());
                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mProgress.dismiss();
                                                        finish();
                                                    }
                                                });
                                            } else {
                                                checkItem.setTaskType("9");
                                                doUpload(checkItem);
                                            }
                                        } else {
                                            mBlackDao.addCheckItem(checkItem);
                                        }
                                    } else {
                                        if (mIsNFC) {
                                            List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mTaskID, mcheckItemInfo.getCheckItemID());
                                            if (item1.size() > 1) {
                                                mBlackDao.delCheckAbnorItem(item1.get(1).getResult_ID());
                                            }
                                        }
                                        mBlackDao.setCheck(mTaskID, mcheckItemInfo.getCheckItemID());
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgress.dismiss();
                                        if (!mIsNFC) {
                                            Jump();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Logger.e(e);
                            }
                        }
                    }).start();
                }
            }
        });

        mImgstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchActivity.this, DeviceStateActivity.class);
                intent.putExtra(Constants.CHECKITEMINFO, mList.get(0));
                startActivityForResult(intent, 2);
            }
        });
    }

    public void doUpload(final CheckItem checkItem) {
        mDialog.show();
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
                        WatchActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                finish();
                            }
                        });
                    } else {
                        WatchActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(WatchActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    WatchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Toast.makeText(WatchActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 调用Camera2开启闪光灯
     *
     * @throws CameraAccessException
     */
    private void openCamera2Flash() throws CameraAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.openCamera(cameraId, new CameraDevice.StateCallback() {

                @Override
                public void onOpened(CameraDevice camera) {
                    cameraDevice = camera;
                    createCaptureSession();
                }

                @Override
                public void onError(CameraDevice camera, int error) {

                }

                @Override
                public void onDisconnected(CameraDevice camera) {

                }
            }, null);
        }
    }

    /**
     * createCaptureSession
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void createCaptureSession() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.surfaceTexture = new SurfaceTexture(0, false);
        }
        this.surfaceTexture.setDefaultBufferSize(1280, 720);
        this.surface = new Surface(this.surfaceTexture);
        ArrayList localArrayList = new ArrayList(1);
        localArrayList.add(this.surface);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.cameraDevice.createCaptureSession(localArrayList, this.stateCallback, null);
            }
        } catch (CameraAccessException e) {
            Logger.e(e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        if (isLOLLIPOP()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (isOpen) {
                    cameraDevice.close();
                }
            }
        } else {
            mFlashUtils.finishFlashUtils();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        isClick = true;
        mWatchAdapter.notifyDataSetChanged();
        isOpen = false;
        if (!isLOLLIPOP()) {
            mFlashUtils = FlashUtils.getInstance();
        }
        super.onResume();
    }

    public void getDialog() {
        View view = LayoutInflater.from(WatchActivity.this).inflate(R.layout.dialog_skip, null, false);
        final Dialog builder = new Dialog(WatchActivity.this, R.style.update_dialog);
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
//                long nowTime = TimeType.getNowTime();
//                long time = TimeType.getTime(mStartTime);
//                for (int i = 0; i < mGClist.size(); i++) {
//                    CheckItemInfo mcheckItemInfo = mGClist.get(i);
//                    String format = TimeType.dateToString();
//                    if (!mcheckItemInfo.isSubmit()) {
//                        CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mMusername, "", null, "1", "0", "", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false,
//                                mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "");
//                        mBlackDao.addCheckItem(checkItem);
//                    }
//                }
//                for (int i = 0; i < mCBlist.size(); i++) {
//                    CheckItemInfo mcheckItemInfo = mCBlist.get(i);
//                    String format = TimeType.dateToString();
//                    if (!mcheckItemInfo.isSubmit()) {
//                        CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mMusername, "", null, "1", "0", "", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false,
//                                mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "");
//                        mBlackDao.addCheckItem(checkItem);
//                    }
//                }
//                for (int i = 0; i < mZDlist.size(); i++) {
//                    CheckItemInfo mcheckItemInfo = mZDlist.get(i);
//                    String format = TimeType.dateToString();
//                    if (!mcheckItemInfo.isSubmit()) {
//                        CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mMusername, "", null, "1", "0", "", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false,
//                                mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "");
//                        mBlackDao.addCheckItem(checkItem);
//                    }
//                }
//                mCBlist.clear();
//                mZDlist.clear();
                Jump();
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

    public void Jump() {
        if (mCBlist.size() > 0) {
            Intent intent = new Intent(WatchActivity.this, MeterActivity.class);
            intent.putExtra(Constants.CB, (Serializable) mCBlist);
            intent.putExtra(Constants.ZD, (Serializable) mZDlist);
            startActivity(intent);
        } else {
            if (mZDlist.size() > 0) {
                Intent intent = new Intent(WatchActivity.this, MeasureActivity.class);
                intent.putExtra(Constants.ZD, (Serializable) mZDlist);
                startActivity(intent);
            } else {
                mBlackDao.delMobject(mCheckItem.getMobjectCode());
                startActivity(new Intent(WatchActivity.this, WatchActivity.class));
            }
        }
        WatchActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            Bundle extras = data.getExtras();
            CheckItemInfo ceshi = (CheckItemInfo) extras.getSerializable(Constants.WATCH);
            for (int i = 0; i < mGClist.size(); i++) {
                if (mGClist.get(i).getCheckItemID().equals(ceshi.getCheckItemID())) {
                    mGClist.get(i).setSubmit(true);
                }
            }
            mWatchAdapter.notifyDataSetChanged();
        } else if (requestCode == 1 && resultCode == 3) {
            finish();
        } else if (requestCode == 2 && resultCode == 5) {
            startActivity(new Intent(WatchActivity.this, WatchActivity.class));
            finish();
        }
    }

    private boolean isLOLLIPOP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setState() {
        boolean state = false;
        long nowTime = TimeType.getNowTime();
        long time = TimeType.getTime(mStartTime);
        String format = TimeType.dateToString();
        List<CheckItemInfo> list = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            CheckItemInfo mcheckItemInfo = mList.get(i);
            if (mcheckItemInfo.getState() == -1) {
                break;
            }
            String c = "1";
            if (mcheckItemInfo.getEquipmentStatusFilter() != null) {
                c = String.valueOf(mcheckItemInfo.getEquipmentStatusFilter().charAt(mcheckItemInfo.getState()));
            }
            if ("0".equals(c)) {
                list.add(mcheckItemInfo);
                CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mMusername, "", null, mcheckItemInfo.getState() == 2 ? "4" : mcheckItemInfo.getState() + "", "0", "-10", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false,
                        mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "", null, "");
                mBlackDao.addCheckItem(checkItem);
                state = true;
            }
        }
        if (state) {
            mList.removeAll(list);
        }
        return state;
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
            String mac = mInstance.getUserInfo(Constants.DEVICEADDRESS);
            if (mac != null && !mac.equals("")) {
                Constants.ISLIANJIE = false;
                BluetoothDevice mDevice = BluetoothUtils.getRemoteDevice(mac);
                ClientManager.getClient().disconnect(mDevice.getAddress());
                // ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
                invalidateOptionsMenu();
            }
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (isClickable()) {
            lockClick();
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    /**
     * 当前是否可以点击
     *
     * @return
     */
    protected boolean isClickable() {
        return isClick;
    }

    /**
     * 锁定点击
     */
    protected void lockClick() {
        isClick = false;
    }

    @Override
    protected void onDestroy() {
        mInstance.setUserInfo(Constants.PROTIME, TimeType.dateToString());
        super.onDestroy();
    }
}
