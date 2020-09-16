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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import project.bridgetek.com.applib.main.adapter.RegionAdapter;
import project.bridgetek.com.applib.main.bean.Check;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Card.CardManager;
import project.bridgetek.com.applib.main.toos.NfcDao;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Scaveng;

import static project.bridgetek.com.applib.main.activity.NfcActivity.ByteArrayToHexString;

public class RegionActivity extends Activity {
    private ImageView mIcBack;
    private TextView mTextView;
    private ListView mLvCheck;
    private Check mCheck;
    private RegionAdapter mRegionAdapter;
    private List<CheckItemInfo> mList = new ArrayList<>();
    private LinearLayout mBtScaveng;
    private String mLineID, mLineName;
    private BlackDao mBlackDao;
    private String mLabletype;
    private LocalUserInfo mUserInfo;
    private FrameLayout mTestlayout;
    private Resources res;
    private TextView mTvScaveng;
    private boolean mIsStart = false;
    private String mTaskID;
    private TextView mTvRecovery;
    private String mType, mTaskid, mLineid, mLinename;
    private String mStartTimt;
    private boolean isInto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mIcBack = findViewById(R.id.ic_back);
        mTextView = findViewById(R.id.line_name);
        mLvCheck = findViewById(R.id.lv_check);
        mBtScaveng = findViewById(R.id.bt_scaveng);
        mTestlayout = findViewById(R.id.testlayout);
        mTvScaveng = findViewById(R.id.tv_scaveng);
        mTvRecovery = findViewById(R.id.tv_recovery);
        mTvRecovery.setTypeface(HiApplication.MEDIUM);
        mTvRecovery.setVisibility(View.GONE);
        mUserInfo = LocalUserInfo.getInstance(RegionActivity.this);
        mLabletype = mUserInfo.getUserInfo(Constants.LABLETYPE);
        if ("EWM".equals(mLabletype)) {
            mBtScaveng.setBackgroundColor(getResources().getColor(R.color.theme));
            mTestlayout.setVisibility(View.GONE);
        } else {
            mTestlayout.setVisibility(View.VISIBLE);
            mBtScaveng.setBackgroundColor(getResources().getColor(R.color.scaveng_bt));
            mTestlayout.setBackgroundResource(R.drawable.ic_backgroud);
            mBtScaveng.setEnabled(false);
        }
        Intent intent = getIntent();
        mLineID = intent.getStringExtra(Constants.LINEID);
        mLineName = intent.getStringExtra(Constants.LINENAME);
        mTaskID = mUserInfo.getUserInfo(Constants.TASKID);
        mBlackDao = BlackDao.getInstance(RegionActivity.this);
        //mBtScaveng.setTypeface(HiApplication.MEDIUM);
        mTvScaveng.setTypeface(HiApplication.MEDIUM);
        mTextView.setText(mLineName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isInto = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList = mBlackDao.getCheckItemInfo(mTaskID);
        mRegionAdapter = new RegionAdapter(mList, RegionActivity.this, mBlackDao, mUserInfo.getUserInfo(Constants.CYCLETIME));
        mLvCheck.setAdapter(mRegionAdapter);
        List<TaskInfo> task = mBlackDao.getTask(mTaskID);
        long time = TimeType.getNowTime();
        long time1 = TimeType.getTime(task.get(0).getTaskPlanStartTime());
        long time2 = TimeType.getTime(task.get(0).getTaskPlanEndTime());
        if (time >= time1 && time <= time2) {
            mIsStart = true;
        } else {
            mIsStart = false;
        }
        if (!mLabletype.equals("EWM")) {
            NfcDao dao = new NfcDao(this);
            NfcDao.mNfcAdapter.enableForegroundDispatch(this, NfcDao.mPendingIntent, NfcDao.mIntentFilter, NfcDao.mTechList);
        }
        getRecovery();
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegionActivity.this.finish();
            }
        });
        mLvCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RegionActivity.this, CheckActivity.class);
                intent.putExtra(Constants.LINEID, mRegionAdapter.getItem(position).getLineID());
                intent.putExtra(Constants.LABELID, mRegionAdapter.getItem(position).getLabelID());
                startActivity(intent);
            }
        });
        mBtScaveng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsStart) {
                    Scaveng scaveng = new Scaveng(RegionActivity.this, 1);
                    scaveng.getScav();
                } else {
                    //Toast.makeText(RegionActivity.this, R.string.upcom_check_toast_start_text, Toast.LENGTH_SHORT).show();
                    getDialog();
                }
            }
        });
        mTvRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType.equals(Constants.ONE)) {
                    startActivity(new Intent(RegionActivity.this, WatchActivity.class));
                } else {
                    Intent intent = new Intent(RegionActivity.this, SnapActivity.class);
                    intent.putExtra(Constants.LINEID, mLineid);
                    intent.putExtra(Constants.LINENAME, mLinename);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra("codedContent");
                boolean mate = false;
                Logger.i(data);
                for (int i = 0; i < mList.size(); i++) {
                    String labelID = mList.get(i).getLabelCode();
                    if (Storage.getLabelCode(content).equals(labelID)) {
                        Intent intent = new Intent(RegionActivity.this, CheckActivity.class);
                        intent.putExtra(Constants.LINEID, mList.get(i).getLineID());
                        intent.putExtra(Constants.LABELID, mList.get(i).getLabelID());
                        mate = true;
                        intent.putExtra(Constants.SKIP, mate);
                        mUserInfo.setUserInfo(Constants.LABELCODE, content);
                        startActivity(intent);
                    }
                }
                if (!mate) {
                    Toast.makeText(this, R.string.upcom_check_toast_data_text, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        if (!mLabletype.equals("EWM")) {
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
        Log.d("NFCTAG", intent.getAction());
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
            //Toast.makeText(this, R.string.upcom_check_toast_start_text, Toast.LENGTH_SHORT).show();
            getDialog();
        }
    }

    private void showData(String data) {
        boolean mate = false;
        for (int i = 0; i < mList.size(); i++) {
            String labelID = mList.get(i).getLabelCode();
            if (data.equals(labelID)) {
                Intent intent = new Intent(RegionActivity.this, CheckActivity.class);
                intent.putExtra(Constants.LINEID, mList.get(i).getLineID());
                intent.putExtra(Constants.LABELID, mList.get(i).getLabelID());
                mate = true;
                intent.putExtra(Constants.SKIP, mate);
                mUserInfo.setUserInfo(Constants.LABELCODE, data);
                startActivity(intent);
            }
        }
        if (!mate) {
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

    public void getRecovery() {
        mType = mUserInfo.getUserInfo(Constants.TYPE);
        mTaskid = mUserInfo.getUserInfo(Constants.TASKID);
        mLineid = mUserInfo.getUserInfo(Constants.LINEID);
        mLinename = mUserInfo.getUserInfo(Constants.LINENAME);
        mStartTimt = mUserInfo.getUserInfo(Constants.PROTIME);
        String userInfo = mUserInfo.getUserInfo(Constants.ACCOUNTID);
        String prouser = mUserInfo.getUserInfo(Constants.PROUSER);
        if (!userInfo.equals(prouser)) {
            mTvRecovery.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(mType) || "".equals(mType)) {
            mTvRecovery.setVisibility(View.GONE);
        } else {
            if (mType.equals(Constants.ONE)) {
                long time = TimeType.getNowTime();
                long time1 = TimeType.getTime(mStartTimt);
                long l = time - time1;
                if (l > 600) {
                    mTvRecovery.setVisibility(View.GONE);
                } else {
                    List<CheckItemInfo> mobject = mBlackDao.getCheckMobject();
                    if (mobject.size() > 0) {
                        mTvRecovery.setVisibility(View.VISIBLE);
                        mTvRecovery.setText(getString(R.string.upcom_activity_tvrecovery_text) + mobject.get(0).getMobjectName());
                    } else {
                        mTvRecovery.setVisibility(View.GONE);
                    }
                }
            }
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
        return isInto;
    }

    /**
     * 锁定点击
     */
    protected void lockClick() {
        isInto = false;
    }
}
