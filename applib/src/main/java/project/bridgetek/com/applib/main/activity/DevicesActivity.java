package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.DevicesAdapter;
import project.bridgetek.com.applib.main.bean.Devices;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

import static project.bridgetek.com.applib.main.activity.ExcQueryActivity.RESULT_SPEECH;

public class DevicesActivity extends Activity {
    private BlackDao mBlackDao;
    private LocalUserInfo mLocalUserInfo;
    private String mAccountId;
    private ListView mLvDevices;
    private ImageView mIcBack;
    private DevicesAdapter mDevicesadapter;
    private List<Devices> mDevices;
    private TextView mLineName;
    private ImageView mImgSearch;
    private EditText mEtSearch;
    private ImageView mImgLoad;
    private TextView mTvDevices;
    private LinearLayout mLlCount;
    private SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (mDevices.size() < 1) {
                    mImgLoad.setVisibility(View.VISIBLE);
                } else {
                    mImgLoad.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
            } else if (msg.what == 1) {
                getTest();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mBlackDao = BlackDao.getInstance(DevicesActivity.this);
        mLocalUserInfo = LocalUserInfo.getInstance(DevicesActivity.this);
        mAccountId = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        mLvDevices = findViewById(R.id.lv_devices);
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mImgSearch = findViewById(R.id.img_search);
        mEtSearch = findViewById(R.id.et_search);
        mTvDevices = findViewById(R.id.tv_devices);
        mLlCount = findViewById(R.id.ll_count);
        mDevices = mBlackDao.getDevices(mAccountId);
        mDevicesadapter = new DevicesAdapter(mDevices, this);
        mLvDevices.setAdapter(mDevicesadapter);
        mLineName.setTypeface(HiApplication.BOLD);
        mImgLoad = findViewById(R.id.img_load);
        mImgLoad.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        swipeRefreshLayout.setProgressViewEndTarget(true, 150);
        mTvDevices.setTypeface(HiApplication.MEDIUM);
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.DEVICE, mDevicesadapter.getItem(position));
                intent.putExtras(bundle);
                setResult(3, intent);
                finish();
            }
        });
        mLlCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTest();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.obtainMessage(1).sendToTarget();
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    mEtSearch.setText(Storage.format(text.get(0)));
                }
                break;
            }
        }
    }

    public void getTest() {
        String string = mEtSearch.getText().toString();
        mDevices = mBlackDao.getLikeDevices(mAccountId, string);
        mDevicesadapter = new DevicesAdapter(mDevices, DevicesActivity.this);
        mLvDevices.setAdapter(mDevicesadapter);
        handler.obtainMessage(0).sendToTarget();
    }
}
