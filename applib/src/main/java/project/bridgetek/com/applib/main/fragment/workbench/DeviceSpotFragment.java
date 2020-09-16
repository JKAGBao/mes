package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.adapter.workbench.ChartAdapter;
import project.bridgetek.com.applib.main.adapter.workbench.SpotAdapter;
import project.bridgetek.com.applib.main.bean.workbench.Trend;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.MyControl.HistoryListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceSpotFragment extends Fragment {
    private String mPointCode;
    private String mType;
    private String mDevCode;
    private ProgressDialog mDialog;
    private String mPointName;
    @BindView(R2.id.tv_start_time)
    TextView mTvStarTime;
    @BindView(R2.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R2.id.lv_chart)
    HistoryListView mLvChart;
    @BindView(R2.id.lv_signal)
    ListView mLvSignal;
    @BindView(R2.id.line_name)
    TextView mLineName;
    @BindView(R2.id.ll_acceleration)
    LinearLayout mLlAcceleration;
    @BindView(R2.id.cb_acceleration)
    CheckBox mCbAcceleration;
    @BindView(R2.id.ll_speed)
    LinearLayout mLlSpeed;
    @BindView(R2.id.cb_speed)
    CheckBox mCbSpeed;
    @BindView(R2.id.ll_temperature)
    LinearLayout mLlTemperature;
    @BindView(R2.id.cb_temperature)
    CheckBox mCbTemperature;
    private ChartAdapter mChartAdapter;
    private SpotAdapter mSpotAdapter;
    String unit;

    @OnClick(R2.id.ll_acceleration)
    public void onAcceleration() {
        mType = "1";
        initText();
    }

    @OnClick(R2.id.ll_speed)
    public void onSpeed() {
        mType = "2";
        initText();
    }

    @OnClick(R2.id.ll_temperature)
    public void onTemperature() {
        mType = "16";
        initText();
    }

    @OnClick(R2.id.tv_start_time)
    public void setStarTime() {
        setDialog(mTvStarTime);
    }

    @OnClick(R2.id.tv_end_time)
    public void setEndTime() {
        setDialog(mTvEndTime);
    }

    @OnClick(R2.id.ic_back)
    public void onBack() {
        getActivity().finish();
    }

    @OnClick(R2.id.tv_search)
    public void onSearch() {
        mDialog.show();
        final List<String> list = new ArrayList<>();
        list.add(mType);
        final String start = mTvStarTime.getText().toString();
        final String end = mTvEndTime.getText().toString();
        final String finalPointCode = mPointCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("devCode", mDevCode);
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
                                List<Trend.TrendDatasBean> trendDatas = new ArrayList<>();
                                trendDatas.addAll(trends.get(0).getTrendDatas());
                                if (trendDatas != null) {
                                    Collections.reverse(trendDatas);
                                    mSpotAdapter = new SpotAdapter(trendDatas, getContext(), unit);
                                    mLvSignal.setAdapter(mSpotAdapter);
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

    public DeviceSpotFragment() {
        // Required empty public constructor
    }

    public static DeviceSpotFragment newInstance(String pointCode, String trye, String devCode, String pointName) {
        DeviceSpotFragment fragment = new DeviceSpotFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ONE, pointCode);
        args.putString(Constants.TWO, trye);
        args.putString(Constants.DEVCODE, devCode);
        args.putString(Constants.THREE, pointName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mPointCode = arguments.getString(Constants.ONE);
        mType = arguments.getString(Constants.TWO);
        mDevCode = arguments.getString(Constants.DEVCODE);
        mPointName = arguments.getString(Constants.THREE);
        mDialog = Storage.getPro(getContext(), getString(R.string.delegate_exception_dialog_text));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_spot, container, false);
        ButterKnife.bind(this, view);
        initTime();
        initText();
        return view;
    }

    public void initTime() {
        long nowTime = TimeType.getNowTime();
        mTvEndTime.setText(TimeType.getStrState(nowTime + 86400));
        mTvStarTime.setText(TimeType.getStrState(nowTime - 86400));
    }

    public void initText() {
        mLineName.setText(mPointName);
        mCbTemperature.setChecked(false);
        mCbSpeed.setChecked(false);
        mCbAcceleration.setChecked(false);
        if (mType.equals("1")) {
            mCbAcceleration.setChecked(true);
            unit = CountString.ACCELERATION;
        } else if (mType.equals("2")) {
            mCbSpeed.setChecked(true);
            unit = CountString.BAT;
        } else {
            mCbTemperature.setChecked(true);
            unit = CountString.TEMPERATURE;
        }
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

}
