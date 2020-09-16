package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.DetailsAdapter;
import project.bridgetek.com.applib.main.adapter.ExceptionDetailAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class DetailsActivity extends Activity {
    private ImageView mIcBack;
    private TextView mTvDevice, mTvAbnormal, mTvDevices;
    private RecyclerView mRlList;
    private BlackDao mBlackDao;
    private List<ResultFileInfo> mList = new ArrayList<>();
    private DetailsAdapter mDetailsAdapter;
    private TextView mTvMobjectinfo, mTvException, mDepict;
    private TextView mLineName;
    private boolean mIsServer;
    private ExceptionDetailAdapter mDetailAdapter;
    private LocalUserInfo mUserInfo;
    private String mCheckItemID, mTaskID;
    private CheckItem mAccurate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mBlackDao = BlackDao.getInstance(this);
        mUserInfo = LocalUserInfo.getInstance(this);
        mIcBack = findViewById(R.id.ic_back);
        mTvDevice = findViewById(R.id.tv_device);
        mTvDevices = findViewById(R.id.tv_devices);
        mRlList = findViewById(R.id.rl_list);
        mTvAbnormal = findViewById(R.id.tv_abnormal);
        mTvMobjectinfo = findViewById(R.id.tv_Mobjectinfo);
        mTvException = findViewById(R.id.tv_Exception);
        mDepict = findViewById(R.id.depict);
        mLineName = findViewById(R.id.line_name);
        setFont();
        mCheckItemID = getIntent().getStringExtra(Constants.CHECKINFO);
        mTaskID = mUserInfo.getUserInfo(Constants.OVERTASKID);
        mAccurate = mBlackDao.getCheckItemAccurate(mCheckItemID, mTaskID);
        mList = mBlackDao.getCheckFile(mAccurate.getResult_ID());
        mDetailsAdapter = new DetailsAdapter(DetailsActivity.this, mList);
        mRlList.setAdapter(mDetailsAdapter);
        mRlList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        setName();
    }

    public void setFont() {
        mTvMobjectinfo.setTypeface(HiApplication.BOLD);
        mTvException.setTypeface(HiApplication.BOLD);
        mDepict.setTypeface(HiApplication.BOLD);
        mTvAbnormal.setTypeface(HiApplication.MEDIUM);
        mTvDevice.setTypeface(HiApplication.MEDIUM);
        mTvDevices.setTypeface(HiApplication.MEDIUM);
        mLineName.setTypeface(HiApplication.BOLD);
    }

    public void setName() {
        mTvDevice.setText(mAccurate.getMobjectName());
        mTvAbnormal.setText(mAccurate.getMobjectName() + mAccurate.getComplete_TM());
        mTvDevices.setText(mAccurate.getMemo_TX());
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
