package project.bridgetek.com.applib.main.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.MessageEventWave;
import project.bridgetek.com.applib.main.fragment.OscFragment2;
import project.bridgetek.com.applib.main.toos.CurrentConfig;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;

public class ChartActivity extends AppCompatActivity implements OscFragment2.OnFragmentInteractionListener {
    private CurrentConfig mConfig;
    private LinearLayout mLlChart;
    private List<Float> mList;
    private ImageView mIcBack;
    private TextView mLineName, mTvDerail;
    private String mType;
    int mRecvType = 0;
    private byte[] mRecvBytes = new byte[8192];
    private float[] mFloats;
    private short[] mShorts;
    private float mCoef = 5.0F;
    int mReanaLyes, mNums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        Intent intent = getIntent();
        mReanaLyes = intent.getIntExtra(Constants.REANALYSE, 0);
        mNums = intent.getIntExtra(Constants.NUMS, 0);
        mList = (List<Float>) intent.getSerializableExtra(Constants.INTENTFLOAT);
        mType = intent.getStringExtra(Constants.TYPE);
        mConfig = loadConfig();
        mLlChart = findViewById(R.id.ll_chart);
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mTvDerail = findViewById(R.id.tv_derail);
        mLineName.setText(mType);
        if (mType.equals(getResources().getString(R.string.test_vibration_rb_signal1_text))) {
            mTvDerail.setText(CountString.ACCELERATION);
            mConfig.sample_type = CountString.ACC;
            mRecvType = 17;
        } else {
            mTvDerail.setText(CountString.SPEED);
            mConfig.sample_type = CountString.VEL;
            mRecvType = 18;
        }
        mFloats = new float[mList.size()];
        mShorts = new short[mList.size()];
        mLineName.setTypeface(HiApplication.BOLD);
        mCoef = Float.intBitsToFloat(1017370378);
        for (int i = 0; i < mList.size(); i++) {
            mFloats[i] = mList.get(i);
            mShorts[i] = (short) (mFloats[i] / mCoef);
        }
        mTvDerail.setTypeface(HiApplication.BOLD);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    MessageEventWave messageEventWave = new MessageEventWave(mFloats, mRecvType, mReanaLyes, mNums, 1017370378, 55536);
                    EventBus.getDefault().post(messageEventWave);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        OscFragment2 chartFragment = OscFragment2.newInstance("", "");
        transaction.replace(R.id.ll_chart, chartFragment);
        transaction.commit();
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private CurrentConfig loadConfig() {
        CurrentConfig currentConfig = null;
        File file = new File("/data/data/com.example.ldw.wls/databases/currentconfig");
        if (!file.exists()) {
            file.delete();
            currentConfig = new CurrentConfig();
            currentConfig.sample_type = CountString.ACC;
            currentConfig.sample_freq = mReanaLyes;
            currentConfig.sample_nums = mNums;
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

    @Override
    public Object onFragmentInteraction(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Object paramObject) {
        if ((paramInt1 == 0) && (paramInt2 == 101)) {     // get config
            return mConfig;
        }
        return null;
    }
}