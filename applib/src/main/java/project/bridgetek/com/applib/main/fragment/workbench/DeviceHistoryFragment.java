package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.adapter.workbench.ChartAdapter;
import project.bridgetek.com.applib.main.adapter.workbench.PointSelectAdapter;
import project.bridgetek.com.applib.main.adapter.workbench.SignalAdapter;
import project.bridgetek.com.applib.main.bean.workbench.Devdetail;
import project.bridgetek.com.applib.main.bean.workbench.Devs;
import project.bridgetek.com.applib.main.bean.workbench.Trend;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.MyControl.HistoryListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceHistoryFragment extends Fragment {
    Devdetail devs;
    private ChartAdapter mChartAdapter;
    private ProgressDialog mDialog;
    private String mDeviceId;
    private SignalAdapter mSignalAdapter;
    private PointSelectAdapter mPointAdapter;
    private List<Devs.Dev.PointsBean> mPoinList = new ArrayList<>();
    private List<Devs.Dev.PointsBean.SignalsBean> mList = new ArrayList<>();
    @BindView(R2.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R2.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R2.id.tv_device_name)
    TextView mTvDeviceName;
    @BindView(R2.id.tv_device_code)
    TextView mTvDeviceCode;
    @BindView(R2.id.lv_point)
    HistoryListView mLvPoint;
    @BindView(R2.id.lv_signal)
    HistoryListView mLvSignal;
    @BindView(R2.id.tv_search)
    TextView mTvSearch;
    @BindView(R2.id.lv_chart)
    HistoryListView mLvChart;

    @OnClick(R2.id.ic_back)
    public void onBack() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            getActivity().finish();
        }
    }

    @OnClick(R2.id.tv_start_time)
    public void setStart() {
        setDialog(mTvStartTime);
    }

    @OnClick(R2.id.tv_end_time)
    public void setEnd() {
        setDialog(mTvEndTime);
    }

    public DeviceHistoryFragment() {
        // Required empty public constructor
    }

    public void setSignal(List<Devs.Dev.PointsBean.SignalsBean> list) {
        mList.clear();
        mList.addAll(list);
        mSignalAdapter.notifyDataSetChanged();
    }

    //搜索
    @OnClick(R2.id.tv_search)
    public void onSearch() {
        if (devs != null) {
            String pointCode = "";
            final List<String> list = new ArrayList<>();
            final String start = mTvStartTime.getText().toString();
            final String end = mTvEndTime.getText().toString();
            for (int i = 0; i < mPoinList.size(); i++) {
                if (mPoinList.get(i).isSelect()) {
                    pointCode = mPoinList.get(i).getPointCode();
                }
            }
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isSelect()) {
                    list.add(mList.get(i).getSignalTypeName());
                }
            }
            if (pointCode.equals("") || start.equals("") || end.equals("") || list.size() < 1) {
                Toast.makeText(getContext(), R.string.device_history_search_toast, Toast.LENGTH_SHORT).show();
            } else {
                mDialog.show();
                final String finalPointCode = pointCode;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("devCode", devs.getDevCode());
                            jsonObject.put("pointCode", finalPointCode);
                            jsonObject.put("signalTypes", list);
                            jsonObject.put("begTime", start);
                            jsonObject.put("endTime", end);
                            String json = jsonObject.toString();
                            LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.TREND, json);
                            String data = loadDataFromWeb.getResult();
                            final List<Trend> trends = JSONArray.parseArray(data, Trend.class);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (trends != null) {
                                        mChartAdapter = new ChartAdapter(trends, getContext());
                                        mLvChart.setAdapter(mChartAdapter);
                                        //showLineChart(trends.get(0).getTrendDatas(), "ceshi", R.color.theme);
                                    }
                                    mDialog.dismiss();
                                }
                            });
                        } catch (Exception e) {
                            Logger.e(e);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDialog.dismiss();
                                }
                            });
                        }
                    }
                }).start();
            }
        }
    }

    //信号类型表item
    @OnItemClick(R2.id.lv_signal)
    public void onItemSignal(int position) {
        mList.get(position).setSelect(!mList.get(position).isSelect());
        mSignalAdapter.notifyDataSetChanged();
    }

    //测点类型表item
    @OnItemClick(R2.id.lv_point)
    public void onItemOnclick(int position) {
        for (int i = 0; i < mPoinList.size(); i++) {
            if (i == position) {
                mPoinList.get(i).setSelect(!mPoinList.get(i).isSelect());
            } else {
                mPoinList.get(i).setSelect(false);
            }
        }
        mPointAdapter.notifyDataSetChanged();
        if (mPoinList.get(position).isSelect()) {
            setSignal(mPoinList.get(position).getSignals());
        } else {
            setSignal(new ArrayList<Devs.Dev.PointsBean.SignalsBean>());
        }
    }

    public static DeviceHistoryFragment newInstance(String DeviceId) {
        DeviceHistoryFragment fragment = new DeviceHistoryFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TASKID, DeviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeviceId = getArguments().getString(Constants.TASKID);
        mDialog = Storage.getPro(getContext(), getString(R.string.delegate_exception_dialog_text));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_history, container, false);
        ButterKnife.bind(this, view);
        mPointAdapter = new PointSelectAdapter(getContext(), mPoinList);
        mSignalAdapter = new SignalAdapter(mList, getContext());
        mLvPoint.setAdapter(mPointAdapter);
        mLvSignal.setAdapter(mSignalAdapter);
        //initChart(mChartOscw);
        initTime();
        initUI();
        return view;
    }

    public void initTime() {
        long nowTime = TimeType.getNowTime();
        mTvEndTime.setText(TimeType.getStrState(nowTime + 86400));
        mTvStartTime.setText(TimeType.getStrState(nowTime - 86400));
    }

    public void setDialog(final TextView textView) {
        //通过自定义控件AlertDialog实现
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        //设置日期简略显示 否则详细显示 包括:星期\周
        datePicker.setCalendarViewShown(false);
        //初始化当前日期
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        //设置date布局
        builder.setView(view);
        builder.setTitle(R.string.device_history_dialog_time_title);
        builder.setPositiveButton(R.string.test_temporary_bt_preservation_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //日期格式
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                int mYear = datePicker.getYear();
                int mMonth = datePicker.getMonth();
                int mDay = datePicker.getDayOfMonth();
                textView.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.upcom_abnormal_tv_cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public void initUI() {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    jsonObject.put("devCode", mDeviceId);
                    jsonObject.put("curPageInex", 1);
                    jsonObject.put("hisPageInex", 1);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.DEVDETAIL, json);
                    final String data = loadDataFromWeb.getResult();
                    devs = JSONObject.parseObject(data, Devdetail.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (devs != null) {
                                mTvDeviceName.setText(getString(R.string.check_label_tv_text) + getString(R.string.semicolon) + devs.getDevName());
                                mTvDeviceCode.setText(getString(R.string.delegate_exception_tv_devicecode_text) + getString(R.string.semicolon) + devs.getDevCode());
                                if (devs.getPoints() != null) {
                                    mPoinList.addAll(devs.getPoints());
                                    mPointAdapter.notifyDataSetChanged();
                                }
                            }
                            mDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }
}
