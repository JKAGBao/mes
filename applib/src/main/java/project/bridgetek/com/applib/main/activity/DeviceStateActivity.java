package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.DeviceStateAdapter;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

public class DeviceStateActivity extends Activity {

    TextView mLineName;

    ListView mLvDeviceStatus;
    private List<CheckItemInfo> mList;
    private BlackDao mBlackDao;
    CheckItemInfo checkItemInfo;
    DeviceStateAdapter mAdapter;
    LocalUserInfo mUserInfo;

    Button mBtCancel;

    Button mBtPreservation;

    public void onBack() {
        finish();
    }


    public void onCancel() {
        finish();
    }


    public void onPreservation() {
        for (int i = 0; i < mList.size(); i++) {
            CheckItemInfo checkItemInfo = mList.get(i);
            mBlackDao.setCheckState(checkItemInfo.getState(), checkItemInfo.getTaskID(), checkItemInfo.getMobjectCode());
            mBlackDao.setLineState(checkItemInfo.getState(), checkItemInfo.getTaskID(), checkItemInfo.getMobjectCode());
        }
        setResult(5);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_state);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        ButterKnife.bind(this);
        initUI();
    }

    public void initUI() {
        checkItemInfo = (CheckItemInfo) getIntent().getSerializableExtra(Constants.CHECKITEMINFO);
        mLineName.setText(checkItemInfo.getLabelName());
        mBlackDao = BlackDao.getInstance(DeviceStateActivity.this);
        mUserInfo = LocalUserInfo.getInstance(this);
        mBtCancel.setTypeface(HiApplication.BOLD);
        mBtPreservation.setTypeface(HiApplication.BOLD);
        List<String> dataList = mUserInfo.getDataList(Constants.EQUIPMENTSTATUS);
        mList = mBlackDao.getMobject(checkItemInfo.getTaskID(), checkItemInfo.getLabelID());
        mAdapter = new DeviceStateAdapter(mList, DeviceStateActivity.this, dataList);
        mLvDeviceStatus.setAdapter(mAdapter);
    }
}
