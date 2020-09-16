package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 * 设备树选中的设备列表
 */
public class DeviceSelectFragment extends Fragment {
    private List<Devs.Dev> mList = new ArrayList<>();
    private ExceptionAdapter mExceptionAdapter;
    private List<String> mStrList;
    private ProgressDialog mDialog;
    @BindView(R2.id.lv_devices)
    ListView mLvDevice;

    @OnClick(R2.id.ic_back)
    public void onBack() {
        getFragmentManager().popBackStackImmediate();
    }

    @OnItemClick(R2.id.lv_devices)
    public void onItemOnclick(int position) {
        StateActivity activity = (StateActivity) getActivity();
        activity.addFragment("more", DeviceDetailsFragment.newInstance(mList.get(position).getDevCode()), "detail");
    }

    public DeviceSelectFragment() {
        // Required empty public constructor
    }

    public static DeviceSelectFragment newInstance(List<String> list) {
        DeviceSelectFragment fragment = new DeviceSelectFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(Constants.MARK, (ArrayList<String>) list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_select, container, false);
        ButterKnife.bind(this, view);
        mExceptionAdapter = new ExceptionAdapter(mList, getContext());
        mLvDevice.setAdapter(mExceptionAdapter);
        initUI();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = Storage.getPro(getContext(), getString(R.string.delegate_exception_dialog_text));
        mStrList = getArguments().getStringArrayList(Constants.MARK);
    }

    public void initUI() {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accoundId", Constants.CURRENTUSER);
                    jsonObject.put("devCodes", mStrList);
                    jsonObject.put("devStatus", "");
                    jsonObject.put("pageIndex", 1);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.DEVSTATUSDEVS, json);
                    String data = loadDataFromWeb.getResult();
                    final List<Devs.Dev> devs = JSONArray.parseArray(data, Devs.Dev.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            mList.clear();
                            if (devs != null) {
                                mList.addAll(devs);
                            } else {
                                Toast.makeText(getActivity(), R.string.app_login_toast_error_text, Toast.LENGTH_SHORT).show();
                            }
                            mExceptionAdapter.notifyDataSetChanged();
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
