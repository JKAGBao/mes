package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;

public class TestDetailActivity extends Activity {
    private ImageView mIcBack;
    private TextView mLineName;
    private TextView mTvMobjectinfo, mTvDevice;
    private TextView mTvSignal, mTvSignalValue, mTvDirection, mTvDirectionValue;
    private TextView mTvTime, mTvTimeValue, mTvRemarks, mTvRemarksValue;
    private Button mBtAccelerationPhoto, mBtSpeedPhoto, mBtTemperaturePhoto;
    private TextView mTvAcceleration, mTvAccelerationValue, mTvAccelerationText;
    private TextView mTvSpeed, mTvSpeedValue, mTvSpeedText;
    private TextView mTvTemperature, mTvTemperatureValue, mTvTemperatureText;
    private CheckItem mCheckItem;
    private BlackDao mBlackDao;
    private List<ResultFileInfo> mList = new ArrayList<>();
    private LinearLayout mLlAcceleration, mLlSpeed, mLlTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void setName() {
        mTvDevice.setText(mCheckItem.getMobjectName());
        mTvSignalValue.setText(mCheckItem.getPDADevice());
        mTvDirectionValue.setText(mCheckItem.getExceptionTransfer_YN());
        mTvTimeValue.setText(mCheckItem.getComplete_TM());
        mTvRemarksValue.setText(mCheckItem.getMemo_TX());
        if (!mCheckItem.getCheckItem_ID().equals("0.0")) {
            mTvSpeedValue.setText(mCheckItem.getCheckItem_ID() + CountString.SPEED);
        } else {
            mLlSpeed.setVisibility(View.GONE);
        }
        if (!mCheckItem.getLabelID().equals("0.0")) {
            mTvAccelerationValue.setText(mCheckItem.getLabelID() + CountString.ACCELERATION);
        } else {
            mLlAcceleration.setVisibility(View.GONE);
        }
        if (!mCheckItem.getResultValue().equals("0.0")) {
            mTvTemperatureValue.setText(mCheckItem.getResultValue() + CountString.TEMPERATURE);
        } else {
            mLlTemperature.setVisibility(View.GONE);
        }
    }

    public void setFont() {
        mBtAccelerationPhoto.setTypeface(HiApplication.MEDIUM);
        mBtSpeedPhoto.setTypeface(HiApplication.MEDIUM);
        mBtTemperaturePhoto.setTypeface(HiApplication.MEDIUM);
        mTvAcceleration.setTypeface(HiApplication.BOLD);
        mTvAccelerationValue.setTypeface(HiApplication.BOLD);
        mTvAccelerationText.setTypeface(HiApplication.MEDIUM);
        mTvSpeed.setTypeface(HiApplication.BOLD);
        mTvSpeedValue.setTypeface(HiApplication.BOLD);
        mTvSpeedText.setTypeface(HiApplication.MEDIUM);
        mTvTemperature.setTypeface(HiApplication.BOLD);
        mTvTemperatureValue.setTypeface(HiApplication.BOLD);
        mTvTemperatureText.setTypeface(HiApplication.MEDIUM);
        mLineName.setTypeface(HiApplication.BOLD);
        mTvMobjectinfo.setTypeface(HiApplication.BOLD);
        mTvDevice.setTypeface(HiApplication.BOLD);
        mTvSignal.setTypeface(HiApplication.BOLD);
        mTvSignalValue.setTypeface(HiApplication.BOLD);
        mTvDirection.setTypeface(HiApplication.BOLD);
        mTvDirectionValue.setTypeface(HiApplication.BOLD);
        mTvTime.setTypeface(HiApplication.BOLD);
        mTvTimeValue.setTypeface(HiApplication.BOLD);
        mTvRemarks.setTypeface(HiApplication.BOLD);
        mTvRemarksValue.setTypeface(HiApplication.BOLD);

    }

    public void initUI() {
        mCheckItem = (CheckItem) getIntent().getSerializableExtra(Constants.CHECKINFO);
        mBlackDao = BlackDao.getInstance(TestDetailActivity.this);
        mLineName = findViewById(R.id.line_name);
        mTvMobjectinfo = findViewById(R.id.tv_Mobjectinfo);
        mTvDevice = findViewById(R.id.tv_device);
        mTvSignal = findViewById(R.id.tv_signal);
        mTvSignalValue = findViewById(R.id.tv_signal_value);
        mTvDirection = findViewById(R.id.tv_direction);
        mTvDirectionValue = findViewById(R.id.tv_direction_value);
        mTvTime = findViewById(R.id.tv_time);
        mTvTimeValue = findViewById(R.id.tv_time_value);
        mTvRemarks = findViewById(R.id.tv_remarks);
        mTvRemarksValue = findViewById(R.id.tv_remarks_value);
        mBtAccelerationPhoto = findViewById(R.id.bt_acceleration_photo);
        mBtSpeedPhoto = findViewById(R.id.bt_speed_photo);
        mBtTemperaturePhoto = findViewById(R.id.bt_temperature_photo);
        mTvAcceleration = findViewById(R.id.tv_acceleration);
        mTvAccelerationValue = findViewById(R.id.tv_acceleration_value);
        mTvAccelerationText = findViewById(R.id.tv_acceleration_text);
        mTvSpeed = findViewById(R.id.tv_speed);
        mTvSpeedValue = findViewById(R.id.tv_speed_value);
        mTvSpeedText = findViewById(R.id.tv_speed_text);
        mTvTemperature = findViewById(R.id.tv_temperature);
        mTvTemperatureValue = findViewById(R.id.tv_temperature_value);
        mTvTemperatureText = findViewById(R.id.tv_temperature_text);
        mLlAcceleration = findViewById(R.id.ll_acceleration);
        mLlSpeed = findViewById(R.id.ll_speed);
        mIcBack = findViewById(R.id.ic_back);
        mLlTemperature = findViewById(R.id.ll_temperature);
        setFont();
        setName();
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtTemperaturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList = mBlackDao.getCheckFile(mCheckItem.getResult_ID());
                if (mList.size() > 0) {
                    getView(mList.get(0).getFileName());
                }
            }
        });
        mBtAccelerationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Float> chart = mBlackDao.getChart(mCheckItem.getResult_ID());
                if (chart.size() > 0) {
                    Intent intent = new Intent(TestDetailActivity.this, ChartActivity.class);
                    intent.putExtra(Constants.INTENTFLOAT, (Serializable) chart);
                    intent.putExtra(Constants.REANALYSE, mCheckItem.getRate());
                    intent.putExtra(Constants.NUMS, mCheckItem.getPoints());
                    intent.putExtra(Constants.TYPE, getResources().getString(R.string.test_vibration_rb_signal1_text));
                    startActivity(intent);
                } else {
                    Toast.makeText(TestDetailActivity.this, R.string.test_detail_toast_nosize_text, Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(TestDetailActivity.this, "暂不可用", Toast.LENGTH_SHORT).show();
            }
        });
        mBtSpeedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Float> chart = mBlackDao.getChart(mCheckItem.getResult_ID());
                if (chart.size() > 0) {
                    Intent intent = new Intent(TestDetailActivity.this, ChartActivity.class);
                    intent.putExtra(Constants.INTENTFLOAT, (Serializable) chart);
                    intent.putExtra(Constants.REANALYSE, mCheckItem.getRate());
                    intent.putExtra(Constants.NUMS, mCheckItem.getPoints());
                    intent.putExtra(Constants.TYPE, getResources().getString(R.string.test_vibration_rb_signal2_text));
                    startActivity(intent);
                } else {
                    Toast.makeText(TestDetailActivity.this, R.string.test_detail_toast_nosize_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getView(String s) {
        final Dialog dia;
        dia = new Dialog(TestDetailActivity.this, R.style.edit_AlertDialog_style);
        View view = LayoutInflater.from(TestDetailActivity.this).inflate(R.layout.start_dialog, null, false);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.star_image);
        imageView.enable();
        File file = new File(s);
        Glide.with(TestDetailActivity.this).load(file).into(imageView);
        dia.setCanceledOnTouchOutside(true);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });
        dia.show();
        Display display = TestDetailActivity.this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dia.setContentView(view, layoutParams);
    }

}
