package project.bridgetek.com.applib.main.Exception;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.activity.workbench.StateActivity;
import project.bridgetek.com.applib.main.adapter.workbench.ExceptionAdapter;
import project.bridgetek.com.applib.main.bean.workbench.Devs;
import project.bridgetek.com.applib.main.bean.workbench.Overview;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.MyControl.PullupListView;
import project.bridgetek.com.bridgelib.delegates.bottom.BottomItemDelegate;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

public class StateDelegate  extends BottomItemDelegate implements PullupListView.LoadListener {
    private List<Devs.Dev> mList;
    private ExceptionAdapter mExceptionAdapter;
    private int mPages = 1;
    boolean isConduct = true;
    private String mark = "ZC";
    private ProgressDialog mDialog;
    @BindView(R2.id.tv_normal)
    TextView mTvNormal;
    @BindView(R2.id.tv_alarm)
    TextView mTvAlarm;
    @BindView(R2.id.tv_halt)
    TextView mTvHalt;
    @BindView(R2.id.tv_more)
    TextView mtvMore;
    @BindView(R2.id.lv_devices)
    PullupListView mLvDevices;
    @BindView(R2.id.tv_bghalt)
    TextView mTvBgHalt;
    @BindView(R2.id.tv_bgalarm)
    TextView mTvBgAlarm;
    @BindView(R2.id.tv_bgnormal)
    TextView mTvBgNormal;
    TextView mTvRefresh;
    @BindView(R2.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnItemClick(R2.id.lv_devices)
    public void onItemOnclick(int position) {
        Intent intent = new Intent(_mActivity, StateActivity.class);
        intent.putExtra(Constants.MARK, 1);
        intent.putExtra(Constants.DEVCODE, mList.get(position).getDevCode());
        startActivity(intent);
    }

    @OnClick(R2.id.normal)
    public void setNamal(View view) {
        mark = "ZC";
        mPages = 1;
        setShow(mTvBgNormal);
        setDevices();
    }

    @OnClick(R2.id.alarm)
    public void setAlarm(View view) {
        mark = "GJ";
        mPages = 1;
        setShow(mTvBgAlarm);
        setDevices();
    }

    @OnClick(R2.id.halt)
    public void setHalt(View view) {
        mark = "LX";
        mPages = 1;
        setShow(mTvBgHalt);
        setDevices();
    }

    public void setShow(TextView textView) {
        mTvBgHalt.setVisibility(View.INVISIBLE);
        mTvBgAlarm.setVisibility(View.INVISIBLE);
        mTvBgNormal.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.more)
    public void setMore(View view) {
        Intent intent = new Intent(_mActivity, StateActivity.class);
        intent.putExtra("Mark", 2);
        startActivity(intent);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_state;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mDialog = Storage.getPro(_mActivity, getString(R.string.delegate_exception_dialog_text));
        mPages = 1;
        getOverview();
        mLvDevices.setInterface(this);
        mList = new ArrayList<>();
        mExceptionAdapter = new ExceptionAdapter(mList, _mActivity);
        mLvDevices.setAdapter(mExceptionAdapter);
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.default_page, null);
        mTvRefresh = emptyView.findViewById(R.id.tv_refresh);
        ((ViewGroup) mLvDevices.getParent()).addView(emptyView);
        mLvDevices.setEmptyView(emptyView);
        setDevices();
        mTvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOverview();
                setDevices();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.theme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPages = 1;
                getOverview();
                setDevices();
            }
        });
    }

    public void hideSwipe() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setDevices() {
        mDialog.show();
        isConduct = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    jsonObject.put("devCodes", new ArrayList<>());
                    jsonObject.put("devStatus", mark);
                    jsonObject.put("pageIndex", mPages);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.DEVSTATUSDEVS, json);
                    String data = loadDataFromWeb.getResult();
                    final List<Devs.Dev> devs = JSONArray.parseArray(data, Devs.Dev.class);
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isConduct = true;
                            mList.clear();
                            if (devs != null) {
                                mList.addAll(devs);
                            }
                            mExceptionAdapter.notifyDataSetChanged();
                            mLvDevices.loadComplete();
                            mDialog.dismiss();
                            hideSwipe();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isConduct = true;
                            mDialog.dismiss();
                            mLvDevices.loadComplete();
                            hideSwipe();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {

        }
    }

    public void getOverview() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.OVERVIEW, json);
                    String data = loadDataFromWeb.getResult();
                    final Overview overview = JSONObject.parseObject(data, Overview.class);
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (overview != null && overview.getZhengChang() != null) {
                                int zheng = Integer.parseInt(overview.getZhengChang());
                                int gao = Integer.parseInt(overview.getGaoJing());
                                int li = Integer.parseInt(overview.getLiXian());
                                if (zheng > 99) {
                                    mTvNormal.setText("99+");
                                } else {
                                    mTvNormal.setText(overview.getZhengChang());
                                }
                                if (gao > 99) {
                                    mTvAlarm.setText("99+");
                                } else {
                                    mTvAlarm.setText(overview.getGaoJing());
                                }
                                if (li > 99) {
                                    mTvHalt.setText("99+");
                                } else {
                                    mTvHalt.setText(overview.getLiXian());
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }).start();
    }

    @Override
    public void onLoad() {
        mPages++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    jsonObject.put("devCodes", new ArrayList<>());
                    jsonObject.put("devStatus", mark);
                    jsonObject.put("pageIndex", mPages);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.DEVSTATUSDEVS, json);
                    String data = loadDataFromWeb.getResult();
                    final List<Devs.Dev> devs = JSONArray.parseArray(data, Devs.Dev.class);
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (devs != null) {
                                mList.addAll(devs);
                                mExceptionAdapter.notifyDataSetChanged();
                            }
                            mLvDevices.loadComplete();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLvDevices.loadComplete();
                        }
                    });
                }
            }
        }).start();
        getOverview();
    }
}

//    @Override
//    public void pullLoad() {
//        if (isConduct) {
//            mPages = 1;
//            getOverview();
//            setDevices();
//        }
//    }
