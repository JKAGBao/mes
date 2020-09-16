package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.adapter.AbnormalAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Devices;
import project.bridgetek.com.applib.main.bean.DropDown;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.fragment.RecordAudioDialogFragment;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.DropListView;
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
import project.bridgetek.com.bridgelib.toos.Record;

public class AbnormalActivity extends Activity {
    private Drawable mDropDrawable, mRiseDrawable;
    private TextView mLineName, mTvEquipment, mTvInspect, mTvDevices;
    private EditText mTvDevice;
    private TextView mTvDepict;
    private ImageView mIcBack;
    private RecyclerView mRlList;
    private List<ResultFileInfo> mList = new ArrayList<>();
    private List<ResultFileInfo> mPhotolist = new ArrayList<>();
    private List<ResultFileInfo> mRecordlist = new ArrayList<>();
    private List<ResultFileInfo> mRecorderlist = new ArrayList<>();
    private AbnormalAdapter mAbnormaladapter;
    private RelativeLayout mRlCount;
    private ImageView mIvClear;
    private Button mBtPreservation;
    private BlackDao mBlackDao;
    private EditText mEtAbnormal, mEtDevices;
    private EditText mEtExceptionTitle;
    private CheckItemInfo mCheckItemInfo = new CheckItemInfo();
    private LocalUserInfo mInstance;
    private String mTaskID, mAccountid, mUsername, mGroupname;
    private boolean mIsWhatch;
    private LinearLayout mLlConnect, mLlCount;
    private Devices mDevices = new Devices();
    private TextView mTvMobjectinfo, mTvAbnormal, mDepict;
    private String mStartTime;
    private TextView mTvCancel;
    private boolean isGai = false;
    List<CheckItem> item;
    String UserCode;
    String TaskType;
    List<DropDown> dropDown;
    @BindView(R2.id.ll_result)
    LinearLayout mLlResult;
    @BindView(R2.id.ll_noresult)
    LinearLayout mLlNoResult;
    @BindView(R2.id.tv_result)
    TextView mTvResult;
    @BindView(R2.id.lv_result)
    DropListView mLvResult;
    @BindView(R2.id.spinner_simple)
    Spinner spinnerSimple;
    String value;
    String exceptionLevel = "1";
    @BindView(R2.id.et_result)
    EditText mEtResult;
    private boolean autonomy;
    ProgressDialog mDialog;
    private boolean change;
    private boolean abnormodify;

    @OnClick(R2.id.tv_result)
    public void setResult() {
        setOpen(mTvResult, mLvResult);
    }

    @OnItemClick(R2.id.lv_result)
    public void onItemResult(int position) {
        setChange(mTvResult, mLvResult, dropDown, position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnormal);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        ButterKnife.bind(this);
        mDropDrawable = getResources().getDrawable(R.drawable.btn_down_arrow);
        mRiseDrawable = getResources().getDrawable(R.drawable.btn_upper_arrow);
        mDropDrawable.setBounds(0, 0, mDropDrawable.getIntrinsicWidth(), mDropDrawable.getIntrinsicHeight());
        mRiseDrawable.setBounds(0, 0, mRiseDrawable.getIntrinsicWidth(), mRiseDrawable.getIntrinsicHeight());
        mDialog = Storage.getPro(AbnormalActivity.this, getString(R.string.app_toos_storage_progress_text));
        initUI();
        setOnclick();
    }

    public void initUI() {
        mInstance = LocalUserInfo.getInstance(AbnormalActivity.this);
        mStartTime = TimeType.dateToString();
        mIsWhatch = getIntent().getBooleanExtra(Constants.ISFIRST, false);
        isGai = getIntent().getBooleanExtra(Constants.CHANGE, false);
        autonomy = getIntent().getBooleanExtra(Constants.AUTONOMY, false);
        abnormodify = getIntent().getBooleanExtra(Constants.ABNORMODIFY, false);
        if (!mIsWhatch) {
            mCheckItemInfo = (CheckItemInfo) getIntent().getSerializableExtra(Constants.REEXCEPTION);
            dropDown = JSONObject.parseArray(mCheckItemInfo.getObserveOptions(), DropDown.class);
        }
        if (dropDown.isEmpty()) {
            mLlResult.setVisibility(View.GONE);
            mLvResult.initUI(dropDown);
            mLlNoResult.setVisibility(View.VISIBLE);
            setSimple();
        } else {
            mLlResult.setVisibility(View.VISIBLE);
            mLlNoResult.setVisibility(View.GONE);
            mLvResult.initUI(dropDown);
        }
        mAccountid = mInstance.getUserInfo(Constants.ACCOUNTID);
        mUsername = mInstance.getUserInfo(Constants.USERNAME);
        mGroupname = mInstance.getUserInfo(Constants.GROUPNAME);
        UserCode = mInstance.getUserInfo(Constants.GROUPID);
        TaskType = mInstance.getUserInfo(Constants.TASKTYPE);
        mTaskID = mInstance.getUserInfo(Constants.TASKID);
        mLineName = findViewById(R.id.line_name);
        mIcBack = findViewById(R.id.ic_back);
        mRlList = findViewById(R.id.rl_list);
        mRlCount = findViewById(R.id.rl_cont);
        mIvClear = findViewById(R.id.iv_clear);
        mTvEquipment = findViewById(R.id.tv_equipment);
        mTvInspect = findViewById(R.id.tv_inspect);
        mBtPreservation = findViewById(R.id.bt_preservation);
        mEtAbnormal = findViewById(R.id.et_abnormal);
        mLlConnect = findViewById(R.id.ll_connect);
        mLlCount = findViewById(R.id.ll_count);
        mTvDevices = findViewById(R.id.tv_devices);
        mTvDevice = findViewById(R.id.tv_device);
        mEtDevices = findViewById(R.id.et_devices);
        mTvAbnormal = findViewById(R.id.tv_abnormal);
        mTvMobjectinfo = findViewById(R.id.tv_Mobjectinfo);
        mEtExceptionTitle = findViewById(R.id.et_ExceptionTitle);
        mDepict = findViewById(R.id.depict);
        mTvDepict = findViewById(R.id.tv_depict);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvDevice.addTextChangedListener(textWatcher);
        mBlackDao = BlackDao.getInstance(AbnormalActivity.this);
        if (isGai) {
            item = mBlackDao.getCheckAbnorItem(mTaskID, mCheckItemInfo.getCheckItemID());
            List<ResultFileInfo> file = mBlackDao.getCheckFile(item.get(0).getResult_ID());
            mList.addAll(file);
            for (int i = 0; i < file.size(); i++) {
                if (file.get(i).getFileType().equals(CountString.PHOTO)) {
                    mPhotolist.add(file.get(i));
                } else if (file.get(i).getFileType().equals(CountString.CORDER)) {
                    mRecorderlist.add(file.get(i));
                } else {
                    mRecordlist.add(file.get(i));
                }
            }

            mEtAbnormal.setText(item.get(0).getMemo_TX());
        }
        if (abnormodify) {
            item = mBlackDao.getCheckAbnorItem(mTaskID, mCheckItemInfo.getCheckItemID());
            List<ResultFileInfo> file = mBlackDao.getCheckFile(item.get(0).getResult_ID());
            mList.addAll(file);
            for (int i = 0; i < file.size(); i++) {
                if (file.get(i).getFileType().equals(CountString.PHOTO)) {
                    mPhotolist.add(file.get(i));
                } else if (file.get(i).getFileType().equals(CountString.CORDER)) {
                    mRecorderlist.add(file.get(i));
                } else {
                    mRecordlist.add(file.get(i));
                }
            }
            if (!item.isEmpty()) {
                mEtAbnormal.setText(item.get(0).getMemo_TX());
                if (dropDown.isEmpty()) {
                    mEtResult.setText(item.get(0).getResultValue());
                    spinnerSimple.setSelection(Integer.parseInt(item.get(0).getExceptionLevel()) - 1, true);
                } else {
                    mLvResult.setValue(item.get(0).getResultValue());
                    mLvResult.setStatus(item.get(0).getExceptionLevel());
                }
            }
        }
        if (mIsWhatch) {
            mLlConnect.setVisibility(View.GONE);
            mLlCount.setVisibility(View.VISIBLE);
        } else {
            mLlCount.setVisibility(View.GONE);
            mLlConnect.setVisibility(View.VISIBLE);
        }
        mIvClear.setVisibility(View.GONE);
        mTvCancel.setVisibility(View.GONE);
        if (!mIsWhatch) {
            setName();
        }
        mList.add(new ResultFileInfo(Constants.PHOTO, null, null, null));
        mAbnormaladapter = new AbnormalAdapter(AbnormalActivity.this, mList, mIvClear, mTvCancel);
        mRlList.setAdapter(mAbnormaladapter);
        mRlList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        setFont();
    }

    public void setFont() {
        mTvDevices.setTypeface(HiApplication.REGULAR);
        mLineName.setTypeface(HiApplication.BOLD);
        mTvEquipment.setTypeface(HiApplication.BOLD);
        mTvInspect.setTypeface(HiApplication.BOLD);
        mTvDepict.setTypeface(HiApplication.BOLD);
        mEtAbnormal.setTypeface(HiApplication.REGULAR);
        mBtPreservation.setTypeface(HiApplication.MEDIUM);
        mTvDevice.setTypeface(HiApplication.REGULAR);
        mTvMobjectinfo.setTypeface(HiApplication.BOLD);
        mTvAbnormal.setTypeface(HiApplication.BOLD);
        mEtExceptionTitle.setTypeface(HiApplication.REGULAR);
        mDepict.setTypeface(HiApplication.BOLD);
        mEtDevices.setTypeface(HiApplication.REGULAR);
        mTvCancel.setTypeface(HiApplication.MEDIUM);
    }

    public void setName() {
        mTvEquipment.setText(mCheckItemInfo.getLabelName() + CountString.CONNECTOR + mCheckItemInfo.getMobjectName());
        mTvInspect.setText(mCheckItemInfo.getCheckItemDesc());
    }

    public void setOnclick() {
        mTvDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AbnormalActivity.this, DevicesActivity.class), 2);
            }
        });
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.removeAll(AbnormalAdapter.LIST);
                mPhotolist.removeAll(AbnormalAdapter.LIST);
                mRecorderlist.removeAll(AbnormalAdapter.LIST);
                mRecordlist.removeAll(AbnormalAdapter.LIST);
                mAbnormaladapter = new AbnormalAdapter(AbnormalActivity.this, mList, mIvClear, mTvCancel);
                mRlList.setAdapter(mAbnormaladapter);
                for (int i = 0; i < AbnormalAdapter.LIST.size(); i++) {
                    if (AbnormalAdapter.LIST.get(i).getFileType().equals(CountString.PHOTO)) {
                        String[] split = AbnormalAdapter.LIST.get(i).getFileName().split("/");
                        String s1 = split[split.length - 2];
                        if (s1.equals("recordtest")) {
                            DeleteFileUtil.deleteFile(AbnormalAdapter.LIST.get(i).getFileName(), AbnormalActivity.this);
                        }
                    } else {
                        DeleteFileUtil.deleteFile(AbnormalAdapter.LIST.get(i).getFileName(), AbnormalActivity.this);
                    }
                }
                mAbnormaladapter.LIST.clear();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvClear.setVisibility(View.GONE);
                mTvCancel.setVisibility(View.GONE);
                mAbnormaladapter.ISHIDE = false;
                mAbnormaladapter.notifyDataSetChanged();
                mAbnormaladapter.LIST.clear();
            }
        });
        //完成保存数据
        mBtPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsWhatch) {
                    String string = mEtAbnormal.getText().toString();
                    mPhotolist.addAll(mRecorderlist);
                    mPhotolist.addAll(mRecordlist);
                    String format = TimeType.dateToString();
                    long nowTime = TimeType.getNowTime();
                    long time = TimeType.getTime(mStartTime);
                    if (dropDown.isEmpty()) {
                        value = mEtResult.getText().toString();
                    } else {
                        value = mLvResult.getValue();
                        exceptionLevel = mLvResult.getStatus();
                    }
                    if (value.equals("")) {
                        value = "预警";
                    }
                    CheckItem checkItem = new CheckItem("", mCheckItemInfo.getTaskID(), mCheckItemInfo.getLabelID(), mCheckItemInfo.getMobjectCode(), mCheckItemInfo.getMobjectName(), mCheckItemInfo.getCheckItemID(), mStartTime, format, mAccountid, mUsername, value, null, "3", "1", "", string, (int) (nowTime - time), mCheckItemInfo.getGroupName(), "", "", false
                            , mCheckItemInfo.getTaskItemID(), TaskType, mCheckItemInfo.getLineID(), UserCode, mGroupname, exceptionLevel, mCheckItemInfo.getShiftName(), "", null, "");
                    if (autonomy) {
                        if (isGai) {
                            List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mCheckItemInfo.getTaskID(), mCheckItemInfo.getCheckItemID());
                            if (!item1.isEmpty()) {
                                mBlackDao.delCheckItemFile(item1.get(0).getResult_ID());
                                for (int i = 0; i < mPhotolist.size(); i++) {
                                    mPhotolist.get(i).setCheckItem_ID(item1.get(0).getResult_ID() + "");
                                    mBlackDao.addResultFile(mPhotolist.get(i));
                                }
                                mBlackDao.setChangeSubmit(checkItem, item1.get(0).getResult_ID());
                            }
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                            intent.putExtras(bundle);
                            setResult(3, intent);
                            finish();
                        } else {
                            checkItem.setTaskType("9");
                            doUpload(checkItem);
                        }
                    } else {
                        if (abnormodify) {
                            List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mCheckItemInfo.getTaskID(), mCheckItemInfo.getCheckItemID());
                            if (!item1.isEmpty()) {
                                mBlackDao.delCheckItemFile(item1.get(0).getResult_ID());
                                for (int i = 0; i < mPhotolist.size(); i++) {
                                    mPhotolist.get(i).setCheckItem_ID(item1.get(0).getResult_ID() + "");
                                    mBlackDao.addResultFile(mPhotolist.get(i));
                                }
                                mBlackDao.setChangeSubmit(checkItem, item1.get(0).getResult_ID());
                            }
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                            intent.putExtras(bundle);
                            setResult(2, intent);
                            finish();
                        } else {
                            int i1 = mBlackDao.addCheckItem(checkItem);
                            for (int i = 0; i < mPhotolist.size(); i++) {
                                mPhotolist.get(i).setCheckItem_ID(i1 + "");
                                mBlackDao.addResultFile(mPhotolist.get(i));
                            }
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                            intent.putExtras(bundle);
                            setResult(2, intent);
                            finish();
                        }
                    }
                } else {
                    mDevices.setDeviceName(mTvDevice.getText().toString());
                    if (TextUtils.isEmpty(mDevices.getDeviceName()) || "".equals(mDevices.getDeviceName())) {
                        Toast.makeText(AbnormalActivity.this, R.string.upcom_abnormal_toast_text, Toast.LENGTH_SHORT).show();
                    } else {
                        String etDevices = mEtDevices.getText().toString();
                        String string1 = mEtExceptionTitle.getText().toString();
                        String string = TimeType.dateToString();
                        mPhotolist.addAll(mRecordlist);
                        mPhotolist.addAll(mRecorderlist);
                        ReException exception = new ReException("", string1, mCheckItemInfo.getTaskID(), mCheckItemInfo.getLabelID(), mDevices.getDeviceName(), mDevices.getDeviceCode(), mCheckItemInfo.getCheckItemID(), mAccountid, mUsername, "", null, "", etDevices, mGroupname, "", "4", string, null, null, false);
                        int a = mBlackDao.addReException(exception);
                        for (int i = 0; i < mPhotolist.size(); i++) {
                            mPhotolist.get(i).setException_ID(a + "");
                            int i1 = mBlackDao.addResultFile(mPhotolist.get(i));
                            Logger.i(i1);
                        }
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.REEXCEPTION, exception);
                        intent.putExtras(bundle);
                        setResult(2, intent);
                        finish();
                    }
                }

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
                        mBlackDao.setCheckAutonomy(checkItem.getTask_ID(), checkItem.getCheckItem_ID());
                        mBlackDao.setLabelTM(TimeType.dateToString());
                        AbnormalActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.WATCH, mCheckItemInfo);
                                intent.putExtras(bundle);
                                setResult(3, intent);
                                finish();
                            }
                        });
                    } else {
                        AbnormalActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(AbnormalActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    AbnormalActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Toast.makeText(AbnormalActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
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
                    uri = PopueWindow.PHOTOURL;
                }
            }
            if (uri != null) {
                Log.i("CONG", "onClick2: " + uri);
                String absoluteImagePath = uri.getPath();
                //String absoluteImagePath = getAbsoluteImagePath(uri);
                String s = Conversion.fileToBase64(absoluteImagePath);
                if (!TextUtils.isEmpty(s)) {
                    getRecord(new ResultFileInfo(CountString.PHOTO, absoluteImagePath, "", mCheckItemInfo.getCheckItemID()));
                    mAbnormaladapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == 10) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
            }
            if (uri != null) {
                Log.i("CONG", "onClick3: " + uri);
                String absoluteImagePath = getAbsoluteImagePath(uri);
                String s = Conversion.fileToBase64(absoluteImagePath);
                if (!TextUtils.isEmpty(s)) {
                    getRecord(new ResultFileInfo(CountString.PHOTO, absoluteImagePath, "", mCheckItemInfo.getCheckItemID()));
                    mAbnormaladapter.notifyDataSetChanged();
                }
            }
        }
        //录像返回
//        else if (requestCode == 100 && resultCode == RESULT_OK) {
//            uri = data.getData();
//            String absoluteImagePath = getAbsoluteImagePath(uri);
//            if (absoluteImagePath != null) {
//                getRecord(new ResultFileInfo(CountString.CORDER, absoluteImagePath, "", mCheckItemInfo.getCheckItemID()));
//            }
//        }
        else if (requestCode == 100 && resultCode == 2) {
            String path = data.getStringExtra(Constants.PATH);
            if (path != null) {
                getRecord(new ResultFileInfo(CountString.CORDER, path, "", mCheckItemInfo.getCheckItemID()));
            }
        } else if (requestCode == 2 && resultCode == 3) {
            Bundle extras = data.getExtras();
            mDevices = (Devices) extras.getSerializable(Constants.DEVICE);
            mTvDevice.setText(mDevices.getDeviceName());
            mEtExceptionTitle.setText(mDevices.getDeviceName() + TimeType.dateToString());
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        // 输入文本之前的状态
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        // 输入文本中的状态
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //temp = s; //temp = s   用于记录当前正在输入文本的个数
            String string = s.toString();
            mEtExceptionTitle.setText(string + TimeType.dateToString());
        }

        // 输入文本之后的状态
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //添加到列表中
    public void getRecord(ResultFileInfo resultFileInfo) {
        if (resultFileInfo.getFileType() == CountString.PHOTO || resultFileInfo.getFileType().equals(CountString.PHOTO)) {
            if (mPhotolist.size() >= 8) {
                Toast.makeText(this, R.string.upcom_abnormal_toast_photo_text, Toast.LENGTH_SHORT).show();
            } else {
                mPhotolist.add(resultFileInfo);
                mList.add(0, resultFileInfo);
                mAbnormaladapter.notifyDataSetChanged();
            }
        } else if (resultFileInfo.getFileType() == CountString.CORDER || resultFileInfo.getFileType().equals(CountString.CORDER)) {
            if (mRecorderlist.size() >= 1) {
                Toast.makeText(this, R.string.upcom_abnormal_toast_recorder_text, Toast.LENGTH_SHORT).show();
            } else {
                mRecorderlist.add(resultFileInfo);
                mList.add(0, resultFileInfo);
                mAbnormaladapter.notifyDataSetChanged();
            }
        } else if (resultFileInfo.getFileType() == CountString.CORD || resultFileInfo.getFileType().equals(CountString.CORD)) {
            if (mRecordlist.size() >= 4) {
                Toast.makeText(this, R.string.upcom_abnormal_toast_record_text, Toast.LENGTH_SHORT).show();
            } else {
                mRecordlist.add(resultFileInfo);
                mList.add(0, resultFileInfo);
                mAbnormaladapter.notifyDataSetChanged();
            }
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

    public void showDialog() {
        View view = LayoutInflater.from(AbnormalActivity.this).inflate(R.layout.dialog_toast, null, false);
        final Dialog builder = new Dialog(AbnormalActivity.this, R.style.update_dialog);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);
        ImageView imgVideo = view.findViewById(R.id.img_video);
        ImageView imgRecord = view.findViewById(R.id.img_record);
        imgVideo.setVisibility(View.GONE);
        RelativeLayout alertClose = view.findViewById(R.id.alert_close);
        alertClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopueWindow popueWindow = new PopueWindow(AbnormalActivity.this);
                popueWindow.showPopueWindow();
                builder.dismiss();
            }
        });
        imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        fragment.stop();
                        String path = RecordAudioDialogFragment.PATH;
                        if (path != null) {
                            getRecord(new ResultFileInfo(CountString.CORD, path, "", mCheckItemInfo.getCheckItemID()));
                        } else {
                            Record.getInstance(AbnormalActivity.this).StopRecord();
                        }
                        fragment.dismiss();
                    }
                });
                builder.dismiss();
            }
        });
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
//                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024*2);//录像
//                startActivityForResult(intent, 100);
//                builder.dismiss();
                startActivityForResult(new Intent(AbnormalActivity.this, MediaRecorderActivity.class), 100);
                builder.dismiss();
            }
        });
        builder.show();
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        builder.setContentView(view, layoutParams);
    }

    public void setOpen(TextView textView, DropListView dropListView) {
        int visibility = dropListView.getVisibility();
        if (visibility == 8) {
            dropListView.setVisibility(View.VISIBLE);
            textView.setCompoundDrawables(null, null, mRiseDrawable, null);
        } else {
            dropListView.setVisibility(View.GONE);
            textView.setCompoundDrawables(null, null, mDropDrawable, null);
        }
    }

    public void setChange(TextView textView, DropListView dropListView, List<DropDown> list, int position) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(false);
            if (i == position) {
                list.get(i).setSelect(true);
            }
        }
        dropListView.setValue(list.get(position).getOptionValue());
        dropListView.setStatus(list.get(position).getOptionStatus());
        dropListView.change();
        textView.setText(list.get(position).getOptionName());
        setOpen(textView, dropListView);
    }

    public void setSimple() {
        List<String> list = mInstance.getDataList(Constants.EXCEPTIONLEVE);
        String[] spinnerItems = null;
        if (!list.isEmpty()) {
            spinnerItems = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                spinnerItems[i] = list.get(i);
            }
        }
        final String[] finalSpinnerItems = spinnerItems;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AbnormalActivity.this, android.R.layout.simple_spinner_item, finalSpinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSimple.setAdapter(spinnerAdapter);
        //选择监听
        spinnerSimple.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                exceptionLevel = (pos + 1) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}

