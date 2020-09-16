package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.activity.workbench.StateActivity;
import project.bridgetek.com.applib.main.adapter.MyFragmentAdapter;
import project.bridgetek.com.applib.main.adapter.workbench.PointAdapter;
import project.bridgetek.com.applib.main.bean.workbench.Devdetail;
import project.bridgetek.com.applib.main.bean.workbench.Devs;
import project.bridgetek.com.applib.main.toos.Mapping;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.MyControl.MyViewPager;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceDetailsFragment extends Fragment {
    private ProgressDialog mDialog;
    private String mDeviceId;
    Devdetail devs;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private MyFragmentAdapter mFragmentAdapter;
    Mapping mapping;
    @BindView(R2.id.ic_back)
    ImageView mIcBack;
    @BindView(R2.id.tab_theme)
    TabLayout mTabTheme;
    @BindView(R2.id.vp_content)
    MyViewPager mVpContent;
    @BindView(R2.id.lv_point)
    ListView mLvPoint;
    @BindView(R2.id.tv_device_name)
    TextView mTvDeviceName;
    @BindView(R2.id.tv_device_code)
    TextView mTvDeviceCode;
    @BindView(R2.id.tv_pl_name)
    TextView mTvPlNmae;
    @BindView(R2.id.tv_company)
    TextView mTvCompany;
    @BindView(R2.id.tv_state)
    TextView mTvState;
    @BindView(R2.id.img_collect)
    ImageView mImgCollect;

    @OnClick(R2.id.img_collect)
    public void setCollect() {
        if (devs != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.API + Constants.ADDATTENTION;
                    if (devs.getIsAttention().equals("1")) {
                        url = Constants.API + Constants.REMOVEATTENTION;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("accoundId", Constants.CURRENTUSER);
                        jsonObject.put("devCodes", new ArrayList<String>() {{
                            add(mDeviceId);
                        }});
                        String json = jsonObject.toString();
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(url, json);
                        final String data = loadDataFromWeb.getResult();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data.equals("true")) {
                                    if (devs.getIsAttention().equals("1")) {
                                        devs.setIsAttention("0");
                                        Toast.makeText(getActivity(), R.string.device_detail_no_attention_text, Toast.LENGTH_SHORT).show();
                                    } else {
                                        devs.setIsAttention("1");
                                        Toast.makeText(getActivity(), R.string.device_detail_yes_attention_text, Toast.LENGTH_SHORT).show();
                                    }
                                    if (devs.getIsAttention().equals("1")) {
                                        mImgCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_collect));
                                    } else {
                                        mImgCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nocollect));
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
    }

    @OnClick(R2.id.ic_back)
    public void onBack() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            getActivity().finish();
        }
    }

    @OnClick(R2.id.ll_trend)
    public void setTrend() {
        StateActivity activity = (StateActivity) getActivity();
        activity.addFragment("detail", DeviceHistoryFragment.newInstance(mDeviceId), "history");
    }

    public static DeviceDetailsFragment newInstance(String DeviceId) {
        DeviceDetailsFragment fragment = new DeviceDetailsFragment();
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
        mapping = new Mapping();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_details, container, false);
        ButterKnife.bind(this, view);
        initUI();
        mFragmentList.add(AlarmFragment.newInstance(mDeviceId, 1));
        mFragmentList.add(AlarmFragment.newInstance(mDeviceId, 2));
        mFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), getActivity(), mFragmentList, new String[]{getString(R.string.device_detail_now_alarm), getString(R.string.device_detail_history_alarm)});
        mVpContent.setAdapter(mFragmentAdapter);
        //绑定
        mTabTheme.setupWithViewPager(mVpContent);
        reflex(mTabTheme);
        return view;
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
                                mTvDeviceName.setText(getContext().getString(R.string.check_label_tv_text) + getContext().getString(R.string.semicolon) + mapping.getValue(devs.getDevName()));
                                mTvDeviceCode.setText(getContext().getString(R.string.delegate_exception_tv_devicecode_text) + getContext().getString(R.string.semicolon) + mapping.getValue(devs.getDevCode()));
                                mTvPlNmae.setText(getContext().getString(R.string.device_details_tv_pl_text) + mapping.getValue(devs.getPlName()));
                                mTvCompany.setText(getContext().getString(R.string.delegate_exception_tv_company_text) + getContext().getString(R.string.semicolon) + mapping.getValue(devs.getOrgName()));
                                if (devs.getDevStatus().equals("1")) {
                                    mTvState.setBackgroundResource(R.drawable.shape_round_textview_blue);
                                } else if (devs.getDevStatus().equals("0")) {
                                    mTvState.setBackgroundResource(R.drawable.shape_round_textview_gray);
                                } else {
                                    mTvState.setBackgroundResource(R.drawable.shape_round_textview_red);
                                }
                                if (devs.getIsAttention().equals("1")) {
                                    mImgCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_collect));
                                } else {
                                    mImgCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nocollect));
                                }
                                if (devs.getPoints() != null) {
                                    setPoint(devs.getPoints());
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

    //设置下划线和字体
    public void reflex(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dp2px(22);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        mTextView.setTypeface(HiApplication.REGULAR);
                        tabView.setPadding(0, 0, 0, 0);
                        int width = 0;
                        width = dp2px(80);
                        //设置tab左右间距为22dp
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setPoint(List<Devs.Dev.PointsBean> list) {
        PointAdapter pointAdapter = new PointAdapter(list, getContext(), devs.getDevCode());
        mLvPoint.setAdapter(pointAdapter);
    }
}
