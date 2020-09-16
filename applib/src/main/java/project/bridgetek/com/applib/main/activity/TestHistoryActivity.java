package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.TestHistoryAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.bean.UnplanCheckItem;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

public class TestHistoryActivity extends Activity {
    private BlackDao mBlackDao;
    private LocalUserInfo mUserInfo;
    private String mAccountid = "-10";
    private List<CheckItem> mItemList = new ArrayList<>();
    private TestHistoryAdapter mHistoryAdapter;
    private ImageView mIcBack;
    private TextView mLineName;
    private ListView mLvHistory;
    private List<Float> mFloatList = new ArrayList<>();
    private TextView mTvSort;
    private boolean mIsWatch = true;
    private ImageView mImgHistory;
    private TextView mTvScaveng;

    AlertDialog dialog;
    private ProgressBar mProgress;
    private TextView mTvSpeed, mTvProgress;
    int count, allcount, taskcount;
    private int progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                mProgress.setProgress(progress);
                mTvSpeed.setText(progress + "%");
                mTvProgress.setText(allcount + "/" + count);
            } else if (msg.what == 4) {
                mHistoryAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Toast.makeText(TestHistoryActivity.this, getString(R.string.upcoming_upload_text) + taskcount + getString(R.string.upcoming_num_text) + getString(R.string.upcoming_notupload_text) + (count - taskcount) + getString(R.string.upcoming_num_text), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
        setOnclick();
    }

    public void initUI() {
        mBlackDao = BlackDao.getInstance(TestHistoryActivity.this);
        mUserInfo = LocalUserInfo.getInstance(TestHistoryActivity.this);
        mItemList = mBlackDao.getTastCheckItem(mAccountid);
        mIcBack = findViewById(R.id.ic_back);
        mLineName = findViewById(R.id.line_name);
        mLvHistory = findViewById(R.id.lv_history);
        mTvSort = findViewById(R.id.tv_sort);
        mImgHistory = findViewById(R.id.img_history);
        mTvScaveng = findViewById(R.id.tv_scaveng);
        Logger.i(mItemList);
        mHistoryAdapter = new TestHistoryAdapter(mItemList, this);
        mLvHistory.setAdapter(mHistoryAdapter);
        if (mItemList.size() > 0) {
            mImgHistory.setVisibility(View.GONE);
        } else {
            mImgHistory.setVisibility(View.VISIBLE);
        }
        mLineName.setTypeface(HiApplication.BOLD);
        mTvSort.setTypeface(HiApplication.BOLD);
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TestHistoryActivity.this, TestDetailActivity.class);
                intent.putExtra(Constants.CHECKINFO, mHistoryAdapter.getItem(position));
                startActivity(intent);
            }
        });
        mTvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistoryAdapter.setWatch(mIsWatch);
                mHistoryAdapter.notifyDataSetChanged();
                if (mIsWatch) {
                    mTvSort.setText(getString(R.string.test_temporary_bt_preservation_text));
                } else {
                    mTvSort.setText(R.string.test_testhistory_tv_sort_text);
                    for (int i = 0; i < mHistoryAdapter.mList.size(); i++) {
                        mBlackDao.delTastCheckItem(mHistoryAdapter.mList.get(i).getResult_ID());
                        mBlackDao.delTastResultFile(mHistoryAdapter.mList.get(i).getResult_ID());
                        mBlackDao.delTastChart(mHistoryAdapter.mList.get(i).getResult_ID());
                    }
                    mItemList = mBlackDao.getTastCheckItem(mAccountid);
                    mHistoryAdapter = new TestHistoryAdapter(mItemList, TestHistoryActivity.this);
                    mLvHistory.setAdapter(mHistoryAdapter);
                    if (mItemList.size() > 0) {
                        mImgHistory.setVisibility(View.GONE);
                    } else {
                        mImgHistory.setVisibility(View.VISIBLE);
                    }
                }
                mIsWatch = !mIsWatch;
            }
        });

        mTvScaveng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(TestHistoryActivity.this);
                if (netWorkConnected) {
                    showProgressBar();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < mItemList.size(); i++) {
                                    CheckItem checkItem = mItemList.get(i);
                                    if (!checkItem.isSubmit()) {
                                        String ResultValue = "";
                                        String SignalType = "";
                                        if (!checkItem.getCheckItem_ID().equals("0.0")) {
                                            ResultValue = checkItem.getCheckItem_ID();
                                            SignalType = "A";
                                        }
                                        if (!checkItem.getLabelID().equals("0.0")) {
                                            ResultValue = checkItem.getLabelID();
                                            SignalType = "S";
                                        }
                                        UnplanCheckItem checkItem1 = new UnplanCheckItem(checkItem.getResult_ID(), checkItem.getMobjectName(), checkItem.getExceptionTransfer_YN()
                                                , checkItem.getPDADevice(), checkItem.getComplete_TM(), checkItem.getUserCode(), checkItem.getUserName(), ResultValue, checkItem.getResultValue()
                                                , SignalType, checkItem.getRate(), checkItem.getPoints(), checkItem.getVibFeatures(), checkItem.getMemo_TX());
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put(Constants.CHECKINFO, checkItem1);
                                        JSONObject target = jsonObject.getJSONObject(Constants.CHECKINFO);
                                        List<ResultFileInfo> mList = new ArrayList<>();
                                        mList = mBlackDao.getCheckFile(checkItem.getResult_ID());
                                        List<ResultFileInfo> infos = Storage.setBase64(mList);
                                        target.put(Constants.RESULTFILE, infos);
                                        List<Float> chart = mBlackDao.getChart(checkItem.getResult_ID());
                                        target.put(Constants.WAVEDATA, chart);
                                        String string = target.toString();
                                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.UNPLANCHECKITEM, string);
                                        String result = loadDataFromWeb.getupload();
                                        if (result.equals("true")) {
                                            checkItem.setSubmit(true);
                                            mBlackDao.setCheckSubmit(checkItem.getResult_ID());
                                            allcount++;
                                            taskcount++;
                                            progress = (int) (((float) allcount / count) * 100);
                                        } else {
                                            Logger.e(checkItem + "上传失败");
                                            allcount++;
                                        }
                                        handler.obtainMessage(3).sendToTarget();
                                    }
                                }
                                handler.obtainMessage(4).sendToTarget();
                            } catch (Exception e) {
                                allcount++;
                                Logger.e(e);
                                handler.obtainMessage(4).sendToTarget();
                            }

                        }
                    }).start();
                } else {
                    Toast.makeText(TestHistoryActivity.this, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 上传进度对话框
     */
    public void showProgressBar() {
        count = 0;
        allcount = 0;
        taskcount = 0;
        List<CheckItem> item = new ArrayList<>();
        for (int j = 0; j < mItemList.size(); j++) {
            if (!mItemList.get(j).isSubmit()) {
                item.add(mItemList.get(j));
            }
        }
        count = count + item.size();
        AlertDialog.Builder builder = new AlertDialog.Builder(TestHistoryActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.upcoming_builder_title_text);
        LayoutInflater layoutInflater = LayoutInflater.from(TestHistoryActivity.this);
        View view = layoutInflater.inflate(R.layout.progressbar, null);
        mProgress = view.findViewById(R.id.progress);
        mTvSpeed = view.findViewById(R.id.tv_speed);
        mTvProgress = view.findViewById(R.id.tv_progress);
        progress = (int) (((float) allcount / count) * 100);
        mProgress.setProgress(progress);
        mTvSpeed.setText(progress + "%");
        mTvProgress.setText(allcount + "/" + count);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}
