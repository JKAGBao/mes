package project.bridgetek.com.applib.main.fragment.workbench;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.adapter.workbench.AlarmAdapter;
import project.bridgetek.com.applib.main.bean.workbench.Devdetail;
import project.bridgetek.com.bridgelib.MyControl.DG_ListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.StringConverter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {
    private String mDeviceId;
    private int mMark;
    @BindView(R2.id.lv_alarm)
    DG_ListView mLvAlarm;
    private AlarmAdapter mAlarmAdapter;
    private List<Devdetail.CurWarningEntitysBean> mList = new ArrayList<>();

    public AlarmFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance(String DeviceId, int mark) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(Constants.DEVICE, DeviceId);
        args.putInt(Constants.KEY_ID, mark);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeviceId = getArguments().getString(Constants.DEVICE);
        mMark = getArguments().getInt(Constants.KEY_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAlarmAdapter = new AlarmAdapter(getActivity(), mList, mMark);
        mLvAlarm.setAdapter(mAlarmAdapter);
        initUI();
    }

    public void initUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    jsonObject.put("devCode", mDeviceId);
                    jsonObject.put("curPageInex", 1);
                    jsonObject.put("currOffPageInex", 1);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.DEVDETAIL, json);
                    final String data = loadDataFromWeb.getResult();
                    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new StringConverter()).create();
                    final Devdetail devs = gson.fromJson(data, Devdetail.class);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (devs != null) {
                                if (mMark == 1) {
                                    if (devs.getCurWarningEntitys() != null) {
                                        mList.addAll(devs.getCurWarningEntitys());
                                    }
                                } else {
                                    if (devs.getCurOfflineEntitys() != null) {
                                        mList.addAll(devs.getCurOfflineEntitys());
                                    }
                                }
                            }
                            mAlarmAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }).start();
    }
}
