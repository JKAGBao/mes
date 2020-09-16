package project.bridgetek.com.applib.main.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.MyFragmentAdapter;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.fragment.AbnormalFragment;
import project.bridgetek.com.applib.main.fragment.UndetectedFragment;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class OverActivity extends AppCompatActivity {
    private TextView mTextView;
    private TabLayout mTabTheme;
    private ViewPager mVpContent;
    private String mLineID, mLineName, mTaskID;
    private LocalUserInfo mLocalUserInfo;
    private ImageView mIcBack;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private MyFragmentAdapter mFragmentAdapter;
    private TextView mTvTaskName, mTvCheckSize, mTvAbnormalSize, mTvUndeteSize;
    private BlackDao mBlackDao;
    private List<CheckItemInfo> mAbnorlist = new ArrayList<>();
    private List<CheckItemInfo> mUndetelist = new ArrayList<>();
    private TextView tvNotupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
        mAbnorlist = mBlackDao.getReEx(mTaskID);
        mUndetelist = mBlackDao.getUndetected(mTaskID, mLineID);
        int lineNumCount = mBlackDao.getLineNumCount(mTaskID);
        int overUpload = mBlackDao.getOverUpload(mTaskID);
        tvNotupload.setText(overUpload + "");
        mTvCheckSize.setText(lineNumCount + "");
        mTvAbnormalSize.setText(mAbnorlist.size() + "");
        mTvUndeteSize.setText(mUndetelist.size() + "");
        mFragmentList.add(AbnormalFragment.newInstance(mTaskID, mLineID, mAbnorlist));
        mFragmentList.add(UndetectedFragment.newInstance(mTaskID, mLineID, mUndetelist));
        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), OverActivity.this, mFragmentList, new String[]{getString(R.string.upcom_over_frament_title_text), getString(R.string.upcom_over_frament_title_text2)});
        mVpContent.setAdapter(mFragmentAdapter);
        //绑定
        mTabTheme.setupWithViewPager(mVpContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initUI() {
        mBlackDao = BlackDao.getInstance(this);
        mTextView = findViewById(R.id.line_name);
        mTabTheme = findViewById(R.id.tab_theme);
        mVpContent = findViewById(R.id.vp_content);
        mTvTaskName = findViewById(R.id.tv_taskName);
        mTvCheckSize = findViewById(R.id.tv_checkSize);
        mTvAbnormalSize = findViewById(R.id.tv_abnormalSize);
        mTvUndeteSize = findViewById(R.id.tv_undeteSize);
        tvNotupload = findViewById(R.id.tv_notupload);
        mIcBack = findViewById(R.id.ic_back);
        mLocalUserInfo = LocalUserInfo.getInstance(OverActivity.this);
        Intent intent = getIntent();
        mLineID = intent.getStringExtra(Constants.LINEID);
        mLineName = intent.getStringExtra(Constants.LINENAME);
        mTaskID = mLocalUserInfo.getUserInfo(Constants.OVERTASKID);
        mTextView.setText(mLineName);
        mTvTaskName.setText(getString(R.string.upcom_over_activity_tvtaskname_text) + mLineName);
        mTextView.setTypeface(HiApplication.BOLD);
        mTvTaskName.setTypeface(HiApplication.BOLD);
        mTvCheckSize.setTypeface(HiApplication.BOLD);
        mTvAbnormalSize.setTypeface(HiApplication.BOLD);
        mTvUndeteSize.setTypeface(HiApplication.BOLD);
        tvNotupload.setTypeface(HiApplication.BOLD);
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
