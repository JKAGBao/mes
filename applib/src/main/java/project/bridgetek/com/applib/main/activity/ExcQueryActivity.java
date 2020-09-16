package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.ExcQueryAdapter;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.bean.SearchException;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

public class ExcQueryActivity extends Activity {
    protected static final int RESULT_SPEECH = 1;
    private ListView mLvAbnonrmal;
    private ImageView mIcBack;
    private List<ReException> mList = new ArrayList<>();
    private LocalUserInfo mLocalUserInfo;
    private String mAccountid;
    private ImageView mImgSearch;
    private EditText mEtSearch;
    private ExcQueryAdapter mExcQueryAdapter;
    private ImageView mImgLoad;
    private TextView mTvDevices;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout mLlCount;
    private TextView mLineName;
    private ProgressDialog mDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mImgLoad.setVisibility(View.GONE);
                mLvAbnonrmal.setVisibility(View.VISIBLE);
                mExcQueryAdapter = new ExcQueryAdapter(ExcQueryActivity.this, mList);
                mLvAbnonrmal.setAdapter(mExcQueryAdapter);
                swipeRefreshLayout.setRefreshing(false);
                mDialog.dismiss();
            } else if (msg.what == 2) {
                mImgLoad.setVisibility(View.VISIBLE);
                mLvAbnonrmal.setVisibility(View.GONE);
                mImgLoad.setBackgroundResource(R.mipmap.ic_loadfail);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ExcQueryActivity.this, R.string.app_context_tost_result_text, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else if (msg.what == 0) {
                mImgLoad.setVisibility(View.VISIBLE);
                mLvAbnonrmal.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ExcQueryActivity.this, R.string.excdelegate_excquery_toast_noabnor_text, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exc_query);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mLvAbnonrmal = findViewById(R.id.lv_abnonrmal);
        mLocalUserInfo = LocalUserInfo.getInstance(this);
        mAccountid = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        mImgSearch = findViewById(R.id.img_search);
        mEtSearch = findViewById(R.id.et_search);
        mIcBack = findViewById(R.id.ic_back);
        mImgLoad = findViewById(R.id.img_load);
        mTvDevices = findViewById(R.id.tv_devices);
        mLlCount = findViewById(R.id.ll_count);
        mLineName = findViewById(R.id.line_name);
        mLvAbnonrmal.setVisibility(View.GONE);
        mImgLoad.setVisibility(View.VISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        swipeRefreshLayout.setProgressViewEndTarget(true, 150);
        mTvDevices.setTypeface(HiApplication.MEDIUM);
        mLineName.setTypeface(HiApplication.BOLD);
        mDialog = Storage.getPro(ExcQueryActivity.this, getString(R.string.excdelegate_excquery_dialog_text));
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLvAbnonrmal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExcQueryActivity.this, AbnorDetailsActivity.class);
                intent.putExtra(Constants.REEXCEPTION, mExcQueryAdapter.getItem(position));
                intent.putExtra(Constants.CONVERT, true);
                startActivity(intent);
            }
        });
        mLlCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTask();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTask();
            }
        });
    }

    public void getTask() {
        boolean netWorkConnected = NetworkUtil.isNetWorkConnected(ExcQueryActivity.this);
        if (netWorkConnected) {
            mDialog.show();
            final String string = mEtSearch.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Constants.ACCOUNTID, mAccountid);
                        map.put(Constants.SEARCHKEYWORDS, string);
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.EXCEPTIONS, map);
                        String data = loadDataFromWeb.getData();
                        SearchException task = JSONObject.parseObject(data, SearchException.class);
                        mList = task.getExceptions();
                        if (mList.size() > 0) {
                            handler.sendEmptyMessageDelayed(1, 1000);
                        } else {
                            handler.sendEmptyMessageDelayed(0, 1000);
                        }
                    } catch (Exception e) {
                        handler.sendEmptyMessageDelayed(2, 1000);
                        Logger.e("run: " + e);
                    }
                }
            }).start();
        } else {
            Toast.makeText(ExcQueryActivity.this, R.string.excdelegate_fragment_toast_imgsearch_text, Toast.LENGTH_SHORT).show();
        }
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
}
