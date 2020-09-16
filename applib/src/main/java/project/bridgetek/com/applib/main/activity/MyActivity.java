package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.toos.ActivityCollectorUtil;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class MyActivity extends Activity {
    private ImageView mIcBack;
    private TextView mLineName;
    private TextView mTvUsername, mTvBadgeid, mTvGroupname;
    private LocalUserInfo mUserInfo;
    private String mUsername, mBadgeid, mGroupname;
    private TextView mTvUsernameText, mTvBadgeidText, mTvGroupnameText;
    private TextView mTvElectric, mTvElectricTime;
    private TextView mTvMeet, mTvMeetTime;
    private TextView mTvWeather, mTvWeatherTime;
    private LinearLayout mLlScaveng;
    private TextView mTvScaveng;
    private String mAccountid;
    private LinearLayout mLlElectric, mLlWeather, mLlMeet;
    private TextView mTvCompany;
    private String mCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        ActivityCollectorUtil.addActivity(this);
        initUI();
        setOnclick();
    }

    public void setFont() {
        mLineName.setTypeface(HiApplication.BOLD);
        mTvUsername.setTypeface(HiApplication.REGULAR);
        mTvBadgeid.setTypeface(HiApplication.REGULAR);
        mTvGroupname.setTypeface(HiApplication.REGULAR);
        mTvUsernameText.setTypeface(HiApplication.REGULAR);
        mTvBadgeidText.setTypeface(HiApplication.REGULAR);
        mTvGroupnameText.setTypeface(HiApplication.REGULAR);
        mTvElectric.setTypeface(HiApplication.REGULAR);
        mTvElectricTime.setTypeface(HiApplication.REGULAR);
        mTvMeet.setTypeface(HiApplication.REGULAR);
        mTvMeetTime.setTypeface(HiApplication.REGULAR);
        mTvWeather.setTypeface(HiApplication.REGULAR);
        mTvWeatherTime.setTypeface(HiApplication.REGULAR);
        mTvScaveng.setTypeface(HiApplication.BOLD);
    }

    public void initUI() {
        mUserInfo = LocalUserInfo.getInstance(MyActivity.this);
        mUsername = mUserInfo.getUserInfo(Constants.USERNAME);
        mBadgeid = mUserInfo.getUserInfo(Constants.GROUPID);
        mGroupname = mUserInfo.getUserInfo(Constants.DEPARTMENTNAME);
        mAccountid = mUserInfo.getUserInfo(Constants.ACCOUNTID);
        mCompany = mUserInfo.getUserInfo(Constants.COMPANYNAME);
        mLineName = findViewById(R.id.line_name);
        mTvUsernameText = findViewById(R.id.tv_username_text);
        mTvBadgeidText = findViewById(R.id.tv_badgeid_text);
        mTvGroupnameText = findViewById(R.id.tv_groupname_text);
        mTvElectric = findViewById(R.id.tv_electric);
        mTvElectricTime = findViewById(R.id.tv_electric_time);
        mTvMeet = findViewById(R.id.tv_meet);
        mTvMeetTime = findViewById(R.id.tv_meet_time);
        mTvWeather = findViewById(R.id.tv_weather);
        mTvWeatherTime = findViewById(R.id.tv_weather_time);
        mTvCompany = findViewById(R.id.tv_company);
        mIcBack = findViewById(R.id.ic_back);
        mTvUsername = findViewById(R.id.tv_username);
        mTvBadgeid = findViewById(R.id.tv_badgeid);
        mTvGroupname = findViewById(R.id.tv_groupname);
        mLlScaveng = findViewById(R.id.ll_scaveng);
        mTvScaveng = findViewById(R.id.tv_scaveng);
        mLlElectric = findViewById(R.id.ll_electric);
        mLlWeather = findViewById(R.id.ll_weather);
        mLlMeet = findViewById(R.id.ll_meet);
        mLlMeet.setVisibility(View.GONE);
        String info = mUserInfo.getUserInfo(Constants.SENSORTIME);
        String info1 = mUserInfo.getUserInfo(Constants.SERVICETIME);
        if (TextUtils.isEmpty(info1) || info1.equals("")) {
            mLlWeather.setVisibility(View.GONE);
        } else {
            mLlWeather.setVisibility(View.VISIBLE);
            mTvWeatherTime.setText(info1);
        }
        if (TextUtils.isEmpty(info) || info.equals("")) {
            mLlElectric.setVisibility(View.GONE);
        } else {
            mLlElectric.setVisibility(View.VISIBLE);
            mTvElectricTime.setText(info);
        }
        if (TextUtils.isEmpty(mAccountid) || mAccountid.equals("")) {
            mTvScaveng.setText(R.string.login_account_bt_text);
        } else {
            mTvScaveng.setText(R.string.setup_my_activity_tvscaveng_text);
        }
        setName();
        setFont();
    }

    public void setName() {
        mTvUsername.setText(mUsername);
        mTvGroupname.setText(mGroupname);
        mTvBadgeid.setText(mBadgeid);
        mTvCompany.setText(mCompany);
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLlScaveng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAccountid) || mAccountid.equals("")) {
                    List<String> list = mUserInfo.getDataList(Constants.SERVCERIP);
                    if (list.size() > 0) {
                        startActivity(new Intent(MyActivity.this, LoginActivity.class));
                    } else {
                        Intent intent = new Intent(MyActivity.this, SetServerActivity.class);
                        intent.putExtra(Constants.SERVCERIP, true);
                        startActivity(intent);
                    }
                } else {
                    setSave();
                    ActivityCollectorUtil.finishAllActivity();
                    startActivity(new Intent(MyActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public void setSave() {
        mUserInfo.setUserInfo(Constants.ACCOUNTID, "");
        mUserInfo.setUserInfo(Constants.ACCOUNT, "");
        mUserInfo.setUserInfo(Constants.BEDGEID, "");
        mUserInfo.setUserInfo(Constants.USERNAME, "");
        mUserInfo.setUserInfo(Constants.GROUPID, "");
        mUserInfo.setUserInfo(Constants.GROUPNAME, "");
        mUserInfo.setUserInfo(Constants.DEPARTMENTNAME, "");
        mUserInfo.setUserInfo(Constants.COMPANYNAME, "");
        mUserInfo.setDataList(Constants.PERMISSIONS, new ArrayList<String>());
        mUserInfo.setDataList(Constants.EXCEPTIONLEVE, new ArrayList<String>());
        mUserInfo.setDataList(Constants.EQUIPMENTSTATUS, new ArrayList<String>());
    }
}
