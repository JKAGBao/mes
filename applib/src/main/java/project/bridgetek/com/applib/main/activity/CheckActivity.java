package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.CheckAdapter;
import project.bridgetek.com.applib.main.bean.Check;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Card.CardManager;
import project.bridgetek.com.applib.main.toos.NfcDao;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Scaveng;

import static project.bridgetek.com.applib.main.activity.NfcActivity.ByteArrayToHexString;

public class CheckActivity extends Activity {
    private ImageView mBack;
    private TextView mLineName, mTvSort;
    private ListView mLvCheck;
    private Check mCheck;
    private LinearLayout mBtScaveng;
    private CheckAdapter mMcheckAdapter;
    private boolean mSort = true;
    private List<CheckItemInfo> mList = new ArrayList<>();
    private List<CheckItemInfo> mSortList = new ArrayList<>();
    private String mLineID, mLabelID;
    private BlackDao mBlackDao;
    private FrameLayout mTestlayout;
    private LocalUserInfo mUserInfo;
    private String mLabletype;
    private Resources res;
    private String mTaskID;
    private TextView mTvScaveng;
    private boolean mIsStart = false;
    private boolean mIsMate;
    private String TaskType;
    private boolean isInto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        Intent intent = getIntent();
        mLineID = intent.getStringExtra(Constants.LINEID);
        mLabelID = intent.getStringExtra(Constants.LABELID);
        mIsMate = intent.getBooleanExtra(Constants.SKIP, false);
        mBlackDao = BlackDao.getInstance(CheckActivity.this);
        mBtScaveng = findViewById(R.id.bt_scaveng);
        mBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mLvCheck = findViewById(R.id.lv_check);
        mTvSort = findViewById(R.id.tv_sort);
        mTestlayout = findViewById(R.id.testlayout);
        mTvScaveng = findViewById(R.id.tv_scaveng);
        mUserInfo = LocalUserInfo.getInstance(CheckActivity.this);
        mLabletype = mUserInfo.getUserInfo(Constants.LABLETYPE);
        mTaskID = mUserInfo.getUserInfo(Constants.TASKID);
        TaskType = mUserInfo.getUserInfo(Constants.TASKTYPE);
        if (CountString.EWM.equals(mLabletype)) {
            mBtScaveng.setBackgroundColor(getResources().getColor(R.color.theme));
            mTestlayout.setVisibility(View.GONE);
        } else {
            mBtScaveng.setBackgroundColor(getResources().getColor(R.color.scaveng_bt));
            mTestlayout.setBackgroundResource(R.drawable.ic_backgroud);
            mBtScaveng.setEnabled(false);
        }
        mTvScaveng.setTypeface(HiApplication.MEDIUM);
        if (mIsMate) {
            setSkip();
            finish();
            return;
        }
    }

    public void setSkip() {
        mSortList = mBlackDao.getLineMobject(mUserInfo.getUserInfo(Constants.TASKID), mLabelID);
        List<CheckItemInfo> list1 = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLabelID);
        List<CheckItemInfo> list2 = new ArrayList<>();
        for (int i = 0; i < mSortList.size(); i++) {
            String mobjectCode = mSortList.get(i).getMobjectCode();
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(j).getMobjectCode().equals(mobjectCode)) {
                    list2.add(mSortList.get(i));
                }
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            String mobjectCode = list2.get(i).getMobjectCode();
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(j).getMobjectCode().equals(mobjectCode)) {
                    list1.remove(j);
                }
            }
        }
        mSortList.clear();
        mSortList.addAll(list2);
        mSortList.addAll(list1);
        if (mSortList.size() < 1) {
            mSortList = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLabelID);
        }
        mBlackDao.cleMobject();
        List<CheckItemInfo> list = new ArrayList<>();
        List<CheckItemInfo> mobject = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLineID, mLabelID);
        for (int i = 0; i < mSortList.size(); i++) {
            for (int j = 0; j < mobject.size(); j++) {
                if (mobject.get(j).getMobjectCode().equals(mSortList.get(i).getMobjectCode())) {
                    list.add(mSortList.get(i));
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            mBlackDao.addCheckMobject(list.get(i));
        }
        //mUserInfo.setUserInfo(Constants.START_TM, TimeType.dateToString());
        mUserInfo.setUserInfo(Constants.LABELID, mLabelID);
        mUserInfo.setUserInfo(Constants.TYPE, Constants.ONE);
//        mBlackDao.setLabelTM(mTaskID, mLabelID, TimeType.dateToString());
        startActivity(new Intent(CheckActivity.this, WatchActivity.class));
    }

    @Override
    protected void onResume() {
        mList = mBlackDao.getLineMobject(mUserInfo.getUserInfo(Constants.TASKID), mLabelID);
        List<CheckItemInfo> list1 = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLabelID);
        List<CheckItemInfo> list2 = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            String mobjectCode = mList.get(i).getMobjectCode();
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(j).getMobjectCode().equals(mobjectCode)) {
                    list2.add(mList.get(i));
                }
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            String mobjectCode = list2.get(i).getMobjectCode();
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(j).getMobjectCode().equals(mobjectCode)) {
                    list1.remove(j);
                }
            }
        }
        mList.clear();
        mList.addAll(list2);
        mList.addAll(list1);
        if (mList.size() < 1) {
            mList = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLabelID);
        }
        mSortList = mList;
        mLineName.setText(mList.get(0).getLabelName());
        mMcheckAdapter = new CheckAdapter(mList, CheckActivity.this, true);
        mLvCheck.setAdapter(mMcheckAdapter);
        super.onResume();
        if (TaskType.equals("4")) {
            mIsStart = true;
        } else {
            List<TaskInfo> task = mBlackDao.getTask(mTaskID);
            long time = TimeType.getNowTime();
            long time1 = TimeType.getTime(task.get(0).getTaskPlanStartTime());
            long time2 = TimeType.getTime(task.get(0).getTaskPlanEndTime());
            if (time >= time1 && time <= time2) {
                mIsStart = true;
            } else {
                mIsStart = false;
            }
        }
        if (!mLabletype.equals(CountString.EWM)) {
            NfcDao dao = new NfcDao(this);
            NfcDao.mNfcAdapter.enableForegroundDispatch(this, NfcDao.mPendingIntent, NfcDao.mIntentFilter, NfcDao.mTechList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInto = true;
    }

    public void setOnclick() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckActivity.this.finish();
            }
        });
        mBtScaveng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsStart) {
                    Scaveng scaveng = new Scaveng(CheckActivity.this, 0);
                    scaveng.getScav();
                } else {
                    getDialog();
                }

            }
        });
        mTvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSort) {
                    mMcheckAdapter = new CheckAdapter(mList, CheckActivity.this, !mSort);
                    mLvCheck.setAdapter(mMcheckAdapter);
                    mTvSort.setText(R.string.test_temporary_bt_preservation_text);
                } else {
                    mMcheckAdapter = new CheckAdapter(mList, CheckActivity.this, !mSort);
                    mLvCheck.setAdapter(mMcheckAdapter);
                    mTvSort.setText(R.string.upcom_check_tvsort_text);
                    mSortList = CheckAdapter.LIST;
                    mBlackDao.delLineMobject(mLineID, mLabelID);
                    for (int i = 0; i < mSortList.size(); i++) {
                        mBlackDao.addLineMobject(mSortList.get(i));
                    }
                }
                mSort = !mSort;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra("codedContent");
                Logger.i(data);
                if (Storage.getLabelCode(content).equals(mList.get(0).getLabelCode())) {
                    mBlackDao.cleMobject();
                    List<CheckItemInfo> list = new ArrayList<>();
                    List<CheckItemInfo> mobject = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLineID, mList.get(0).getLabelID());
                    for (int i = 0; i < mSortList.size(); i++) {
                        for (int j = 0; j < mobject.size(); j++) {
                            if (mobject.get(j).getMobjectCode().equals(mSortList.get(i).getMobjectCode())) {
                                list.add(mSortList.get(i));
                            }
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        mBlackDao.addCheckMobject(list.get(i));
                    }
                    //mUserInfo.setUserInfo(Constants.START_TM, TimeType.dateToString());
                    mUserInfo.setUserInfo(Constants.LABELID, mList.get(0).getLabelID());
                    mUserInfo.setUserInfo(Constants.TYPE, Constants.ONE);
                    //mBlackDao.setLabelTM(mTaskID, mLabelID, TimeType.dateToString());
                    mUserInfo.setUserInfo(Constants.LABELCODE, content);
                    startActivity(new Intent(CheckActivity.this, WatchActivity.class));
                } else {
                    Toast.makeText(this, R.string.upcom_check_toast_data_text, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        if (!mLabletype.equals(CountString.EWM)) {
            NfcDao.mNfcAdapter.disableForegroundDispatch(this);
            Logger.d("CLOSE NFC");
        }
    }

    /**
     * NFC读取显示
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(intent.getAction());
        final Parcelable i = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String data = null;
        data = CardManager.load(i, res);
        if (data == null || data.length() == 0) {
            data = ByteArrayToHexString(id);
            //mTextView.setText("卡中无数据或数据类型不符！");
            if (data == null || data.length() == 0) {
                return;
            }
        }
        if (mIsStart) {
            showData(data);
        } else {
            getDialog();
        }
    }

    private void showData(String data) {
//        if (data == null || data.length() == 0) {
//            //mTextView.setText("卡中无数据或数据类型不符！");
//            return;
//        }
        //mTextView.setText("RFID:"+data);
        if (data.equals(mList.get(0).getLabelCode())) {
            mBlackDao.cleMobject();
            List<CheckItemInfo> list = new ArrayList<>();
            List<CheckItemInfo> mobject = mBlackDao.getMobject(mUserInfo.getUserInfo(Constants.TASKID), mLineID, mList.get(0).getLabelID());
            for (int i = 0; i < mSortList.size(); i++) {
                for (int j = 0; j < mobject.size(); j++) {
                    if (mobject.get(j).getMobjectCode().equals(mSortList.get(i).getMobjectCode())) {
                        list.add(mSortList.get(i));
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {
                mBlackDao.addCheckMobject(list.get(i));
            }
            // mUserInfo.setUserInfo(Constants.START_TM, TimeType.dateToString());
            mUserInfo.setUserInfo(Constants.LABELID, mList.get(0).getLabelID());
            mUserInfo.setUserInfo(Constants.TYPE, Constants.ONE);
            // mBlackDao.setLabelTM(mTaskID, mLabelID, TimeType.dateToString());
            mUserInfo.setUserInfo(Constants.LABELCODE, data);
            startActivity(new Intent(CheckActivity.this, WatchActivity.class));
        } else {
            Toast.makeText(this, R.string.upcom_check_toast_data_text, Toast.LENGTH_SHORT).show();
        }
    }

    public void getDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(R.string.upcom_check_toast_start_text)//设置对话框的内容
                .setPositiveButton(R.string.test_temporary_bt_preservation_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
        return isInto;
    }

    /**
     * 锁定点击
     */
    protected void lockClick() {
        isInto = false;
    }
}
