package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.Tools;

public class AboutActivity extends Activity {
    private ImageView mIcBack;
    private TextView mLineName;
    private TextView mTvIntroduce, mTvEdition, mTvCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mTvIntroduce = findViewById(R.id.tv_introduce);
        mTvEdition = findViewById(R.id.tv_edition);
        mTvCompany = findViewById(R.id.tv_company);
        String vision = Tools.getVersionName(this);
        mTvEdition.setText(CountString.LEFT_BRACKETS + getString(R.string.setup_about_tv_edition_text) + vision + CountString.RIGHT_BRACKETS);
        setFont();
    }

    public void setFont() {
        mTvCompany.setTypeface(HiApplication.MEDIUM);
        mTvEdition.setTypeface(HiApplication.MEDIUM);
        mTvIntroduce.setTypeface(HiApplication.MEDIUM);
        mLineName.setTypeface(HiApplication.BOLD);
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
