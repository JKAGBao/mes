package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.activity.workbench.StateActivity;
import project.bridgetek.com.applib.main.adapter.workbench.ExceptionAdapter;
import project.bridgetek.com.applib.main.bean.workbench.Devs;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.MyControl.MyListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreDeviceFragment extends Fragment implements MyListView.LoadListener {
    private List<Devs.Dev> mList = new ArrayList<>();
    private ExceptionAdapter mExceptionAdapter;
    private int mPages = 1;
    private ProgressDialog mDialog;
    @BindView(R2.id.lv_devices)
    MyListView mLvDevices;
    TextView mTvRefresh;

    @OnItemClick(R2.id.lv_devices)
    public void onItemOnclick(int position) {
        StateActivity activity = (StateActivity) getActivity();
        activity.addFragment("more", DeviceDetailsFragment.newInstance(mList.get(position - 1).getDevCode()), "detail");
    }

    @OnClick(R2.id.ll_connect)
    public void onConnect() {
        StateActivity activity = (StateActivity) getActivity();
        activity.addFragment("more", DeviceSearchFragment.newInstance(), "device_search");
    }

    @OnClick(R2.id.ic_back)
    public void onBack() {
        getActivity().finish();
    }

    @OnClick(R2.id.tv_sort)
    public void setSort() {
        StateActivity activity = (StateActivity) getActivity();
        activity.addFragment("more", DeviceChoiceFragment.newInstance("1", "1"), "choice");
    }

    public static MoreDeviceFragment newInstance() {
        MoreDeviceFragment fragment = new MoreDeviceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_device, container, false);
        ButterKnife.bind(this, view);
        mLvDevices.setInterface(this);
        mDialog = Storage.getPro(getContext(), getString(R.string.delegate_exception_dialog_text));
        mExceptionAdapter = new ExceptionAdapter(mList, getContext());
        mLvDevices.setAdapter(mExceptionAdapter);
        View emptyView = inflater.inflate(R.layout.default_page, null);
        mTvRefresh = emptyView.findViewById(R.id.tv_refresh);
        ((ViewGroup) mLvDevices.getParent()).addView(emptyView);
        mLvDevices.setEmptyView(emptyView);
        mTvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDevices();
            }
        });
        setDevices();
        return view;
    }

    public void setDevices() {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    jsonObject.put("pageIndex", mPages);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.ATTENTIONLIST, json);
                    String data = loadDataFromWeb.getResult();
                    final List<Devs.Dev> devs = JSONArray.parseArray(data, Devs.Dev.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            mList.clear();
                            if (devs != null) {
                                mList.addAll(devs);
                            }
                            mExceptionAdapter.notifyDataSetChanged();
                            mLvDevices.loadComplete();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            mLvDevices.loadComplete();
                        }
                    });
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
                    jsonObject.put("pageIndex", mPages);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.ATTENTIONLIST, json);
                    String data = loadDataFromWeb.getResult();
                    final List<Devs.Dev> devs = JSONArray.parseArray(data, Devs.Dev.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (devs != null) {
                                mList.addAll(devs);
                            }
                            mExceptionAdapter.notifyDataSetChanged();
                            mLvDevices.loadComplete();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLvDevices.loadComplete();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void pullLoad() {
        mPages = 1;
        setDevices();
    }
}
