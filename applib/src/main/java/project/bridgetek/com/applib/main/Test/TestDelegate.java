package project.bridgetek.com.applib.main.Test;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.flir.flironeexampleapplication.GLPreviewActivity;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.TestHistoryActivity;
import project.bridgetek.com.applib.main.activity.VibraTionActivity;
import project.bridgetek.com.bridgelib.delegates.bottom.BottomItemDelegate;
import project.bridgetek.com.bridgelib.toos.HiApplication;

// 临测
public class TestDelegate extends BottomItemDelegate {
    private TextView mTvVibration, mTvImaging, mTvHistory;
    private LinearLayout mLlHistory, mLlImaging, mLlVibration;

    @Override
    public Object setLayout() {
        return R.layout.delegate_test;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initUI(rootView);
        setOnclick();
    }

    public void initUI(View view) {
        mTvVibration = view.findViewById(R.id.tv_vibration);
        mTvImaging = view.findViewById(R.id.tv_imaging);
        mTvHistory = view.findViewById(R.id.tv_history);
        mLlHistory = view.findViewById(R.id.ll_history);
        mLlImaging = view.findViewById(R.id.ll_imaging);
        mLlVibration = view.findViewById(R.id.ll_vibration);
        mTvVibration.setTypeface(HiApplication.BOLD);
        mTvImaging.setTypeface(HiApplication.BOLD);
        mTvHistory.setTypeface(HiApplication.BOLD);
    }

    public void setOnclick() {
        mLlVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_mActivity, VibraTionActivity.class));
            }
        });
        mLlImaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_mActivity, GLPreviewActivity.class));
            }
        });
        mLlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_mActivity, TestHistoryActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
