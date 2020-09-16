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
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;

public class AbnorDetailsActivity extends Activity {
    private ImageView mIcBack;
    private TextView mTvDevice, mTvAbnormal, mTvDevices;
    private RecyclerView mRlList;
    private ReException mReException;
    private BlackDao mBlackDao;
    private List<ResultFileInfo> mList = new ArrayList<>();
    private DetailsAdapter mDetailsAdapter;
    private TextView mTvMobjectinfo, mTvException, mDepict;
    private TextView mLineName;
    private boolean mIsServer;
    private ExceptionDetailAdapter mDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abnor_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mBlackDao = BlackDao.getInstance(this);
        mIcBack = findViewById(R.id.ic_back);
        mTvDevice = findViewById(R.id.tv_device);
        mTvDevices = findViewById(R.id.tv_devices);
        mRlList = findViewById(R.id.rl_list);
        mTvAbnormal = findViewById(R.id.tv_abnormal);
        mTvMobjectinfo = findViewById(R.id.tv_Mobjectinfo);
        mTvException = findViewById(R.id.tv_Exception);
        mDepict = findViewById(R.id.depict);
        mLineName = findViewById(R.id.line_name);
        mReException = (ReException) getIntent().getSerializableExtra(Constants.REEXCEPTION);
        //Devices deviceName = mBlackDao.getDeviceName(mReException.getMobjectCode());
        mIsServer = getIntent().getBooleanExtra(Constants.CONVERT, false);
        mTvDevice.setText(mReException.getMobjectName());
        mTvAbnormal.setText(mReException.getExceptionTitle());
        mTvDevices.setText(mReException.getMemo());
        setFont();
        if (mIsServer) {
            List<String> files = mReException.getFiles();
            if (files != null) {
                for (int i = 0; i < files.size(); i++) {
                    String s = files.get(i);
                    String substring = s.substring(s.indexOf("."));
                    if (substring.equals(".jpg")) {
                        mList.add(new ResultFileInfo(CountString.PHOTO, Constants.API + Constants.EXCEPTION_DETAIL + files.get(i), "", ""));
                    } else if (substring.equals(".AAC")) {
                        mList.add(new ResultFileInfo(CountString.CORD, Constants.API + Constants.EXCEPTION_DETAIL + files.get(i), "", ""));
                    } else if (substring.equals(".mp4")) {
                        mList.add(new ResultFileInfo(CountString.CORDER, Constants.API + Constants.EXCEPTION_DETAIL + files.get(i), "", ""));
                    }
                }
            }
            mDetailAdapter = new ExceptionDetailAdapter(AbnorDetailsActivity.this, mList);
            mRlList.setAdapter(mDetailAdapter);
            mRlList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mList = mBlackDao.getAbnorFile(mReException.getException_ID());
            mDetailsAdapter = new DetailsAdapter(AbnorDetailsActivity.this, mList);
            mRlList.setAdapter(mDetailsAdapter);
            mRlList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        }
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

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
