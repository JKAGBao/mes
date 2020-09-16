package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.MeterAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.ClientManager;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.Conversion;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.DeleteFileUtil;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.PopueWindow;

public class MeterActivity extends Activity {
    private ImageView mIcBack;
    private TextView mLineName, mTvEquipment, mTvInspect, mTvStandard, mTvParmLower, mTvParmUpper;
    private Switch mStDerail;
    private EditText mEtMeter, mEtRemarks;
    private RecyclerView mRecyclerView;
    private ImageView mIvClear;
    private RelativeLayout mRelativeLayout;
    private List<ResultFileInfo> mList = new ArrayList<>();
    private MeterAdapter mAbnormalAdapter;
    private Button mBtPreservation;
    private List<ResultFileInfo> mPhotolist = new ArrayList<>();
    private BlackDao mBlackdao;
    private CheckItemInfo mCheckItemInfo;
    private LocalUserInfo mInstance;
    private List<CheckItemInfo> mCBlist = new ArrayList<>();
    private List<CheckItemInfo> mZDlist = new ArrayList<>();
    private String mAccountid, mUsername, mGroupname, mTaskID;
    private boolean mIsWhatch;
    private TextView mTvRemark, mTvMeter;
    private String mStartTime;
    private TextView mTvCancel;
    String UserCode;
    String TaskType;
    private boolean mIsFNC = false;
    List<CheckItem> item1 = new ArrayList<>();
    ProgressDialog mDialog;
    private boolean isGai = false;
    private TextView mTvLevel;
    private ImageView mImgState;
    private boolean isClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        mDialog = Storage.getPro(MeterActivity.this, getString(R.string.app_toos_storage_progress_text));
        initUI();
        setOnclick();
    }

    public void initUI() {
        mStartTime = TimeType.dateToString();
        Intent intent = getIntent();
        mIsFNC = intent.getBooleanExtra(Constants.SUBMIT, false);
        if (mIsFNC) {
            isGai = intent.getBooleanExtra(Constants.CHANGE, false);
            CheckItemInfo checkItemInfo = (CheckItemInfo) intent.getSerializableExtra(Constants.CHECKINFO);
            mCBlist.add(checkItemInfo);
        } else {
            mIsWhatch = intent.getBooleanExtra(Constants.ISFIRST, false);
            mCBlist = (List<CheckItemInfo>) intent.getSerializableExtra(Constants.CB);
            mZDlist = (List<CheckItemInfo>) intent.getSerializableExtra(Constants.ZD);
        }
        mInstance = LocalUserInfo.getInstance(MeterActivity.this);
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mTvEquipment = findViewById(R.id.tv_equipment);
        mTvInspect = findViewById(R.id.tv_inspect);
        mStDerail = findViewById(R.id.st_derail);
        mEtMeter = findViewById(R.id.et_meter);
        mEtRemarks = findViewById(R.id.et_remarks);
        mRecyclerView = findViewById(R.id.rl_list);
        mIvClear = findViewById(R.id.iv_clear);
        mRelativeLayout = findViewById(R.id.rl_cont);
        mTvStandard = findViewById(R.id.tv_standard);
        mTvParmLower = findViewById(R.id.tv_parmLower);
        mTvParmUpper = findViewById(R.id.tv_parmUpper);
        mBtPreservation = findViewById(R.id.bt_preservation);
        mTvRemark = findViewById(R.id.tv_remark);
        mTvMeter = findViewById(R.id.tv_meter);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvLevel = findViewById(R.id.tv_level);
        mImgState = findViewById(R.id.img_state);
        mEtMeter.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtMeter.setFocusable(true);
        mEtMeter.setFocusableInTouchMode(true);
        mEtMeter.requestFocus();
        mBlackdao = BlackDao.getInstance(MeterActivity.this);
        if ("1".equals(Constants.STARTSTOP)) {
            mImgState.setVisibility(View.VISIBLE);
        } else {
            mImgState.setVisibility(View.GONE);
        }
        setName();
        mList.add(new ResultFileInfo("photo", null, null, null, null));
        mIvClear.setVisibility(View.GONE);
        mTvCancel.setVisibility(View.GONE);
        mAbnormalAdapter = new MeterAdapter(MeterActivity.this, mList, mIvClear, mTvCancel);
        mRecyclerView.setAdapter(mAbnormalAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mEtMeter.addTextChangedListener(new TextWatcher() {
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
                    return;
                }
                String s = setException(mCheckItemInfo, Float.parseFloat(editable.toString().trim()));
                mTvLevel.setTextColor(getResources().getColor(R.color.over_tips));
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
        if (mIsFNC) {
            setResut();
        }
        Font();
    }

    public void Font() {
        mLineName.setTypeface(HiApplication.BOLD);
        mTvEquipment.setTypeface(HiApplication.BOLD);
        mTvInspect.setTypeface(HiApplication.BOLD);
        mEtMeter.setTypeface(HiApplication.REGULAR);
        mTvStandard.setTypeface(HiApplication.BOLD);
        mTvParmLower.setTypeface(HiApplication.BOLD);
        mTvParmUpper.setTypeface(HiApplication.BOLD);
        mEtRemarks.setTypeface(HiApplication.REGULAR);
        mTvMeter.setTypeface(HiApplication.BOLD);
        mTvRemark.setTypeface(HiApplication.BOLD);
        mBtPreservation.setTypeface(HiApplication.MEDIUM);
        mTvCancel.setTypeface(HiApplication.MEDIUM);
    }

    public void setResut() {
        item1 = mBlackdao.getCheckAbnorItem(mTaskID, mCheckItemInfo.getCheckItemID());
        if (item1.size() > 0) {
            mEtMeter.setText(item1.get(0).getResultValue());
            mEtRemarks.setText(item1.get(0).getMemo_TX());
            List<ResultFileInfo> file = mBlackdao.getCheckFile(item1.get(0).getResult_ID());
            mList.addAll(0, file);
            for (int i = 0; i < file.size(); i++) {
                if (file.get(i).getFileType().equals(CountString.PHOTO)) {
                    mPhotolist.add(file.get(i));
                }
            }
        }
    }

    public void setName() {
        mCheckItemInfo = mCBlist.get(0);
        List<CheckItem> accurate = mBlackdao.getCheckAccurate(mCheckItemInfo.getCheckItemID(), mCheckItemInfo.getTaskPlanStartTime());
        mLineName.setText(mCheckItemInfo.getLabelName());
        mTvEquipment.setText(mCheckItemInfo.getMobjectName());
        mTvInspect.setText(getString(R.string.task_over_abnoradapter_tvcheckitemdesc_text) + mCheckItemInfo.getCheckItemDesc());
        mTvStandard.setText(getString(R.string.upcom_meter_activity_tvstanard_text) + (accurate.isEmpty() ? "" : accurate.get(0).getResultValue()));
        mTvParmLower.setText(getString(R.string.upcom_meter_activity_tvparmlower_text) + mCheckItemInfo.getESTStandard());
        mTvParmUpper.setText(getString(R.string.upcom_meter_activity_tvparmupper_text) + (accurate.isEmpty() ? "" : accurate.get(0).getComplete_TM()));
        mAccountid = mInstance.getUserInfo(Constants.ACCOUNTID);
        mUsername = mInstance.getUserInfo(Constants.USERNAME);
        UserCode = mInstance.getUserInfo(Constants.GROUPID);
        TaskType = mInstance.getUserInfo(Constants.TASKTYPE);
        mGroupname = mInstance.getUserInfo(Constants.GROUPNAME);
        mTaskID = mInstance.getUserInfo(Constants.TASKID);
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = TimeType.dateToString();
                String time = mInstance.getUserInfo(Constants.START_TM);
                String userInfo = mInstance.getUserInfo(Constants.LABLE);
                long time1 = TimeType.getTime(string);
                long time2 = TimeType.getTime(time);
                int i = (int) (time1 - time2);
                mBlackdao.setLabel(userInfo, string, i);
                String mac = mInstance.getUserInfo(Constants.DEVICEADDRESS);
                if (mac != null && !mac.equals("")) {
                    Constants.ISLIANJIE = false;
                    BluetoothDevice mDevice = BluetoothUtils.getRemoteDevice(mac);
                    ClientManager.getClient().disconnect(mDevice.getAddress());
                    // ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mBleConnectStatusListener);
                    invalidateOptionsMenu();
                }
                MeterActivity.this.finish();
            }
        });
        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.removeAll(MeterAdapter.LIST);
                mAbnormalAdapter = new MeterAdapter(MeterActivity.this, mList, mIvClear, mTvCancel);
                mRecyclerView.setAdapter(mAbnormalAdapter);
                for (int i = 0; i < mAbnormalAdapter.LIST.size(); i++) {
                    if (MeterAdapter.LIST.get(i).getFileType().equals(CountString.PHOTO)) {
                        String[] split = mAbnormalAdapter.LIST.get(i).getFileName().split("/");
                        String s1 = split[split.length - 2];
                        if (s1.equals("recordtest")) {
                            DeleteFileUtil.deleteFile(mAbnormalAdapter.LIST.get(i).getFileName(), MeterActivity.this);
                        }
                    } else {
                        DeleteFileUtil.deleteFile(mAbnormalAdapter.LIST.get(i).getFileName(), MeterActivity.this);
                    }
                }
                mAbnormalAdapter.LIST.clear();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvClear.setVisibility(View.GONE);
                mTvCancel.setVisibility(View.GONE);
                mAbnormalAdapter.ISHIDE = false;
                mAbnormalAdapter.notifyDataSetChanged();
                mAbnormalAdapter.LIST.clear();
            }
        });
        mBtPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClick) {
                    return;
                }
                isClick = false;
                String ResultValue = mEtMeter.getText().toString();
                String Memo_TX = mEtRemarks.getText().toString();
                String format = TimeType.dateToString();
                String Exception_YN = "0";
                if (ResultValue.length() < 1) {
                    Toast.makeText(MeterActivity.this, R.string.upcom_meter_toast_value_text, Toast.LENGTH_SHORT).show();
                    isClick = true;
                } else {
                    float b = Float.parseFloat(ResultValue);
                    Exception_YN = setException(mCheckItemInfo, b);
                    long nowTime = TimeType.getNowTime();
                    long time = TimeType.getTime(mStartTime);
                    CheckItem checkItem = new CheckItem("", mCheckItemInfo.getTaskID(), mCheckItemInfo.getLabelID(), mCheckItemInfo.getMobjectCode(), mCheckItemInfo.getMobjectName(), mCheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mUsername, ResultValue, null, "0", Exception_YN.equals("0") ? "0" : "1", "", Memo_TX, (int) (nowTime - time), mCheckItemInfo.getGroupName(), "", "", false
                            , mCheckItemInfo.getTaskItemID(), TaskType, mCheckItemInfo.getLineID(), UserCode, mGroupname, Exception_YN, mCheckItemInfo.getShiftName(), "", null, "");
                    if (mIsFNC) {
                        if (isGai) {
                            List<CheckItem> item1 = mBlackdao.getCheckAbnorItem(mCheckItemInfo.getTaskID(), mCheckItemInfo.getCheckItemID());
                            if (!item1.isEmpty()) {
                                mBlackdao.delCheckItemFile(item1.get(0).getResult_ID());
                                for (int i = 0; i < mPhotolist.size(); i++) {
                                    mPhotolist.get(i).setCheckItem_ID(item1.get(0).getResult_ID() + "");
                                    mBlackdao.addResultFile(mPhotolist.get(i));
                                }
                                mBlackdao.setChangeSubmit(checkItem, item1.get(0).getResult_ID());
                            }
                            finish();
                        } else {
                            checkItem.setTaskType("9");
                            doUpload(checkItem);
                        }
                    } else {
                        int i1 = mBlackdao.addCheckItem(checkItem);
                        if (i1 == 0) {
                            Toast.makeText(MeterActivity.this, R.string.upcom_meter_toast_fail_text, Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < mPhotolist.size(); i++) {
                                mPhotolist.get(i).setCheckItem_ID("" + i1);
                                mBlackdao.addResultFile(mPhotolist.get(i));
                                Logger.i("onClick: " + mPhotolist.get(i).getCheckItem_ID());
                            }
                        }
                        if (mIsWhatch) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                            intent.putExtras(bundle);
                            setResult(2, intent);
                            finish();
                        } else {
                            getJump();
                        }
                    }
                }
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
        mImgState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeterActivity.this, DeviceStateActivity.class);
                intent.putExtra(Constants.CHECKITEMINFO, mCBlist.get(0));
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
                    List<ResultFileInfo> infos = Storage.setBase64(mPhotolist);
                    target.put(Constants.RESULTFILE, infos);
                    String string = target.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.CHECKITEM, string);
                    String result = loadDataFromWeb.getupload();
                    if (result.equals("true")) {
                        mBlackdao.setCheckAutonomy(checkItem.getTask_ID(), checkItem.getCheckItem_ID());
                        mBlackdao.setLabelTM(TimeType.dateToString());
                        MeterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                finish();
                            }
                        });
                    } else {
                        MeterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isClick = true;
                                mDialog.dismiss();
                                Toast.makeText(MeterActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    MeterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Toast.makeText(MeterActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        if (requestCode == 0) {
            //判断返回的数据data是否为空，在三星s5、华为p7等机型上面有data为空的现象
            if (data != null && data.getData() != null) {
                uri = data.getData();
            }
            //如果data数据为空，就令uri==之前指定保存的照片的photoUri
            if (uri == null) {
                if (PopueWindow.PHOTOURL != null) {
                    uri = PopueWindow.PHOTOURL;//裁剪照片的方法
                    Logger.i(uri);
                }
            }
            if (uri != null) {
                String absoluteImagePath = uri.getPath();
                String s = Conversion.fileToBase64(absoluteImagePath);
                if (!TextUtils.isEmpty(s)) {
                    mPhotolist.add(0, new ResultFileInfo(CountString.PHOTO, absoluteImagePath, s, null, null));
                    mList.add(0, new ResultFileInfo(CountString.PHOTO, absoluteImagePath, s, null, null));
                    mAbnormalAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == 10) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
            }
            if (uri != null) {
                String absoluteImagePath = getAbsoluteImagePath(uri);
                String s = Conversion.fileToBase64(absoluteImagePath);
                if (!TextUtils.isEmpty(s)) {
                    mPhotolist.add(0, new ResultFileInfo(CountString.PHOTO, absoluteImagePath, s, null, null));
                    mList.add(0, new ResultFileInfo(CountString.PHOTO, absoluteImagePath, s, null, null));
                    mAbnormalAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == 2 && resultCode == 5) {
            startActivity(new Intent(MeterActivity.this, WatchActivity.class));
            finish();
        }
    }

    protected String getAbsoluteImagePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,
                proj,       // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);      // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横向
        } else {
            //竖向
        }
    }

    public void getDialog() {
        View view = LayoutInflater.from(MeterActivity.this).inflate(R.layout.dialog_skip, null, false);
        final Dialog builder = new Dialog(MeterActivity.this, R.style.update_dialog);
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
//                for (int i = 0; i < mCBlist.size(); i++) {
//                    CheckItemInfo mcheckItemInfo = mCBlist.get(i);
//                    CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mUsername, "", null, "1", "0", "", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false
//                            , mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "");
//                    mBlackdao.addCheckItem(checkItem);
//                }
//                for (int i = 0; i < mZDlist.size(); i++) {
//                    CheckItemInfo mcheckItemInfo = mZDlist.get(i);
//                    CheckItem checkItem = new CheckItem("", mcheckItemInfo.getTaskID(), mcheckItemInfo.getLabelID(), mcheckItemInfo.getMobjectCode(), mcheckItemInfo.getMobjectName(), mcheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mUsername, "", null, "1", "0", "", "", (int) (nowTime - time), mcheckItemInfo.getGroupName(), "", "", false
//                            , mcheckItemInfo.getTaskItemID(), TaskType, mcheckItemInfo.getLineID(), UserCode, mGroupname, "0", mcheckItemInfo.getShiftName(), "");
//                    mBlackdao.addCheckItem(checkItem);
//                }
//                mCBlist.clear();
//                mZDlist.clear();
                if (mIsWhatch) {
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
        mCBlist.remove(mCheckItemInfo);
        //去掉一项记录得循环去除
        if (mCBlist.size() < 1) {
            if (mZDlist.size() < 1) {
                mBlackdao.delMobject(mCheckItemInfo.getMobjectCode());
                Intent intent = new Intent(MeterActivity.this, WatchActivity.class);
                intent.putExtra(Constants.ISRECORD, true);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MeterActivity.this, MeasureActivity.class);
                intent.putExtra(Constants.ZD, (Serializable) mZDlist);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(MeterActivity.this, MeterActivity.class);
            intent.putExtra(Constants.CB, (Serializable) mCBlist);
            intent.putExtra(Constants.ZD, (Serializable) mZDlist);
            startActivity(intent);
        }
        MeterActivity.this.finish();
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
            mBlackdao.setLabel(userInfo, string, i);
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
    protected void onDestroy() {
        mInstance.setUserInfo(Constants.PROTIME, TimeType.dateToString());
        super.onDestroy();
    }
}
