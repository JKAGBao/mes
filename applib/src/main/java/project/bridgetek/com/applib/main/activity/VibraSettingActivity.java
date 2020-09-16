package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flir.flironeexampleapplication.util.StatusBarUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.toos.CurrentConfig;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;

public class VibraSettingActivity extends AppCompatActivity {
    private TextView mTvSignalText, mTvAnalyseText, mTvSamplingText;
    private TextView mTvSemaphore, mTvAnalyse, mTvSampling, mTvVibra;
    private RadioButton mRbSignal1, mRbSignal2, mRbSignal3;
    private RadioButton mRbAnalyse1, mRbAnalyse2, mRbAnalyse3, mRbAnalyse4, mRbAnalyse5;
    private RadioButton mRbSampling1, mRbSampling2, mRbSampling3, mRbSampling4;
    private LinearLayout mLlGain, mLlChoose;
    private ImageView mIcBack;
    private RadioGroup mRgSignal, mRgAnalyse, mRgSampling;
    private boolean isStart = false, isHold = false;
    boolean mGain = true;
    private LinearLayout mLlChart;
    private CurrentConfig mConfig;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrasetting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
        initUI();
        initRadio(mConfig);
        mRgSignal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isStart) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        if (rb.isChecked()) {
                            mTvSemaphore.setText(rb.getText());
                            if (mTvSemaphore.getText().equals(getString(R.string.test_vibra_rb_signal_thermal_text))) {
                                mConfig.setType(CountString.ACC);
//                                mLlAcceleration.setVisibility(View.VISIBLE);
//                                mLlSpeed.setVisibility(View.GONE);
                            } else if (mTvSemaphore.getText().equals(getString(R.string.test_vibra_rb_signal2_thermal_text))) {
                                mConfig.setType(CountString.VEL);
//                                mLlAcceleration.setVisibility(View.GONE);
//                                mLlSpeed.setVisibility(View.VISIBLE);
                            } else {
                                mConfig.setType(CountString.TMP);
//                                mLlAcceleration.setVisibility(View.GONE);
//                                mLlSpeed.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }
                }
            }
        });
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("config",mConfig);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mRgAnalyse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isStart) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        if (rb.isChecked()) {
                            mTvAnalyse.setText(rb.getText());
                            if (mTvAnalyse.getText().equals(getString(R.string.test_vibration_rb_analyse1_text))) {
                                mConfig.setFreq(200);
                            } else if (mTvAnalyse.getText().equals(getString(R.string.test_vibration_rb_analyse2_text))) {
                                mConfig.setFreq(500);
                            } else if (mTvAnalyse.getText().equals(getString(R.string.test_vibration_rb_analyse3_text))) {
                                mConfig.setFreq(2000);
                            } else if (mTvAnalyse.getText().equals(getString(R.string.test_vibration_rb_analyse4_text))) {
                                mConfig.setFreq(5000);
                            } else if (mTvAnalyse.getText().equals(getString(R.string.test_vibration_rb_analyse5_text))) {
                                mConfig.setFreq(1000);
                            }
                            break;
                        }
                    }
                }
            }
        });
        mRgSampling.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isStart) {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        if (rb.isChecked()) {
                            mTvSampling.setText(rb.getText());
                            if (mTvSampling.getText().equals(getString(R.string.test_vibration_rb_sampling1))) {
                                mConfig.setNums(256);
                            } else if (mTvSampling.getText().equals(getString(R.string.test_vibration_rb_sampling2_text))) {
                                mConfig.setNums(512);
                            } else if (mTvSampling.getText().equals(getString(R.string.test_vibration_rb_sampling3_text))) {
                                mConfig.setNums(1024);
                            } else if (mTvSampling.getText().equals(getString(R.string.test_vibration_rb_sampling4))) {
                                mConfig.setNums(2048);
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
   //     super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("config",mConfig);
        setResult(RESULT_OK,intent);
        finish();
    }


    public void setFont() {
        mTvAnalyseText.setTypeface(HiApplication.BOLD);
        mTvSignalText.setTypeface(HiApplication.BOLD);
        mTvSamplingText.setTypeface(HiApplication.BOLD);
        mRbSampling4.setTypeface(HiApplication.REGULAR);
        mRbSampling3.setTypeface(HiApplication.REGULAR);
        mRbSampling2.setTypeface(HiApplication.REGULAR);
        mRbSampling1.setTypeface(HiApplication.REGULAR);
        mRbAnalyse1.setTypeface(HiApplication.REGULAR);
        mRbAnalyse2.setTypeface(HiApplication.REGULAR);
        mRbAnalyse3.setTypeface(HiApplication.REGULAR);
        mRbAnalyse5.setTypeface(HiApplication.REGULAR);
        mRbAnalyse4.setTypeface(HiApplication.REGULAR);
        mRbSignal1.setTypeface(HiApplication.REGULAR);
        mRbSignal2.setTypeface(HiApplication.REGULAR);
        mRbSignal3.setTypeface(HiApplication.REGULAR);
        mTvSemaphore.setTypeface(HiApplication.BOLD);
        mTvAnalyse.setTypeface(HiApplication.BOLD);
        mTvVibra.setTypeface(HiApplication.BOLD);
    }
    private void initUI(){
        CurrentConfig config = getIntent().getParcelableExtra("config");
      //  Toast.makeText(this, ""+config.sample_emi, Toast.LENGTH_SHORT).show();
        mConfig = config;

        mTvSignalText = findViewById(R.id.tv_signal_text);
        mLlGain = findViewById(R.id.ll_gain);
        mTvSemaphore = findViewById(R.id.tv_semaphore);
        mTvAnalyse = findViewById(R.id.tv_analyse);
        mTvSampling = findViewById(R.id.tv_sampling);
        mTvVibra = findViewById(R.id.tv_vibra);
        mRbSignal1 = findViewById(R.id.rb_signal1);
        mRbSignal2 = findViewById(R.id.rb_signal2);
        mRbSignal3 = findViewById(R.id.rb_signal3);
        mRbAnalyse1 = findViewById(R.id.rb_analyse1);
        mRbAnalyse2 = findViewById(R.id.rb_analyse2);
        mRbAnalyse3 = findViewById(R.id.rb_analyse3);
        mRbAnalyse5 = findViewById(R.id.rb_analyse5);
        mRbAnalyse4 = findViewById(R.id.rb_analyse4);
        mRbSampling1 = findViewById(R.id.rb_sampling1);
        mRbSampling2 = findViewById(R.id.rb_sampling2);
        mRbSampling3 = findViewById(R.id.rb_sampling3);
        mRbSampling4 = findViewById(R.id.rb_sampling4);
        mRgAnalyse = findViewById(R.id.rg_analyse);
        mRgSampling = findViewById(R.id.rg_sampling);
        mRgSignal = findViewById(R.id.rg_signal);
        mTvAnalyseText = findViewById(R.id.tv_analyse_text);
        mTvSamplingText = findViewById(R.id.tv_sampling_text);
        mLlChoose = findViewById(R.id.ll_choose);
        mIcBack = findViewById(R.id.ic_back);
      //  mLlChoose.setVisibility(View.GONE);

        mLlGain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    if (mGain) {
                        mLlChoose.setVisibility(View.VISIBLE);
                        mLlChart.setVisibility(View.GONE);
                    } else {
                        mLlChoose.setVisibility(View.GONE);
                    }
                    mGain = !mGain;
                } else {
                    Toast.makeText(VibraSettingActivity.this, R.string.test_vibration_toast_nostop_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*
    初始化多选框按钮
     */
    private void initRadio(CurrentConfig mConfig) {
        if(mConfig.sample_type.equals(CountString.ACC)){
            mRbSignal1.setChecked(true);
            mTvSemaphore.setText(mRbSignal1.getText());
        }else if(mConfig.sample_type.equals(CountString.VEL)){
            mRbSignal2.setChecked(true);
            mTvSemaphore.setText(mRbSignal2.getText());
        }else if(mConfig.sample_type.equals(CountString.TMP)){
            mRbSignal3.setChecked(true);
            mTvSemaphore.setText(mRbSignal3.getText());
        }
        if(mConfig.sample_freq == 200){
            mRbAnalyse1.setChecked(true);
            mTvAnalyse.setText(mRbAnalyse1.getText());
        }else if(mConfig.sample_freq == 500){
            mRbAnalyse2.setChecked(true);
            mTvAnalyse.setText(mRbAnalyse2.getText());
        }else if(mConfig.sample_freq == 1000){
            mRbAnalyse3.setChecked(true);
            mTvAnalyse.setText(mRbAnalyse3.getText());
        }else if(mConfig.sample_freq == 2000){
            mRbAnalyse4.setChecked(true);
            mTvAnalyse.setText(mRbAnalyse4.getText());
        }else if(mConfig.sample_freq == 5000){
            mRbAnalyse5.setChecked(true);
            mTvAnalyse.setText(mRbAnalyse5.getText());
        }

         if(mConfig.sample_nums == 256){
            mRbSampling1.setChecked(true);
            mTvSampling.setText(mRbSampling1.getText());
         }else if(mConfig.sample_nums == 512){
             mRbSampling2.setChecked(true);
             mTvSampling.setText(mRbSampling2.getText());
         }else if(mConfig.sample_nums == 1024){
             mRbSampling3.setChecked(true);
             mTvSampling.setText(mRbSampling3.getText());
         }else if(mConfig.sample_nums == 2048){
             mRbSampling4.setChecked(true);
             mTvSampling.setText(mRbSampling4.getText());
         }
    }


    private CurrentConfig loadConfig() {
        CurrentConfig currentConfig = null;
        File file = new File("/data/data/com.example.ldw.wls/databases/currentconfig");
        if (!file.exists()) {
            file.delete();
            currentConfig = new CurrentConfig();
            currentConfig.setType("ACC");
            currentConfig.setFreq(1000);
            currentConfig.setNums(512);
            currentConfig.setEmi(95);
            currentConfig.isTmp(false);
            currentConfig.setMac("");
            currentConfig.setName("");
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
}
