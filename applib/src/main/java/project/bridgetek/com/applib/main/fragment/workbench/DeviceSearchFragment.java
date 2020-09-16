package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.R2;
import project.bridgetek.com.applib.main.activity.workbench.StateActivity;
import project.bridgetek.com.applib.main.adapter.workbench.SearchAdapter;
import project.bridgetek.com.applib.main.bean.workbench.DeviceSearch;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.MyControl.MyListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceSearchFragment extends Fragment implements MyListView.LoadListener {
    @BindView(R2.id.line_name)
    TextView mLineName;
    @BindView(R2.id.et_search)
    AutoCompleteTextView mEtSearch;
    @BindView(R2.id.lv_devices)
    MyListView mLvDevices;
    private int mPages = 1;
    private ProgressDialog mDialog;
    private List<DeviceSearch> mList = new ArrayList<>();
    private SearchAdapter mAdapter;
    boolean isConduct = true;
    private LocalUserInfo mUserInfo;
    List<String> stringList = new ArrayList<>();

    @OnClick(R2.id.img_search)
    public void onSearch() {
        mPages = 1;
        mList.clear();
        mAdapter.notifyDataSetChanged();
        getDevices();
    }

    @OnClick(R2.id.ic_back)
    public void onBack() {
        getFragmentManager().popBackStackImmediate();
    }

    @OnItemClick(R2.id.lv_devices)
    public void onItemClick(int position) {
        StateActivity activity = (StateActivity) getActivity();
        activity.addFragment("device_search", DeviceDetailsFragment.newInstance(mList.get(position - 1).getDevCode()), "detail");
    }

    public DeviceSearchFragment() {
        // Required empty public constructor
    }

    public static DeviceSearchFragment newInstance() {
        DeviceSearchFragment fragment = new DeviceSearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_search, container, false);
        ButterKnife.bind(this, view);
        mUserInfo = LocalUserInfo.getInstance(getActivity());
        initUI();
        return view;
    }

    public void initUI() {
        mDialog = Storage.getPro(getContext(), getString(R.string.delegate_exception_dialog_text));
        mLvDevices.setInterface(this);
        mLineName.setText(getString(R.string.delegate_exception_device_text) + getString(R.string.more_device_et_search_text));
        mAdapter = new SearchAdapter(mList, getActivity());
        stringList.addAll(getLocal(mEtSearch, Constants.DEVICESEARCHSEARCH));
        mLvDevices.setAdapter(mAdapter);
    }

    public void setLocal() {
        if (!mEtSearch.getText().toString().equals("")) {
            if (stringList.size() >= 5) {
                stringList.remove(0);
            }
            stringList.add(mEtSearch.getText().toString());
            mUserInfo.setDataList(Constants.DEVICESEARCHSEARCH, pastLeep(stringList));
        }
    }

    public List<String> pastLeep(List<String> list) {
        List<String> listNew = new ArrayList<>();
        Set set = new HashSet();
        for (String str : list) {
            if (set.add(str)) {
                listNew.add(str);
            }
        }
        return listNew;
    }

    public List<String> getLocal(AutoCompleteTextView text, String string) {
        List<String> list = mUserInfo.getDataList(string);
        if (list.size() > 0) {
            String[] strings = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                strings[i] = list.get(list.size() - i - 1);
            }
            ArrayListAdapter adapter = new ArrayListAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings);
            text.setAdapter(adapter);
        }
        return list;
    }


    @Override
    public void onLoad() {
        mPages++;
        getDevices();
    }

    @Override
    public void pullLoad() {
        if (isConduct) {
            mPages = 1;
            mList.clear();
            getDevices();
        }
    }

    public void getDevices() {
        if (mEtSearch.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
        } else {
            isConduct = false;
            mDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("devCodeOrName", mEtSearch.getText().toString());
                        jsonObject.put("pageIndex", mPages);
                        String json = jsonObject.toString();
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.MOBJECTINFOS, json);
                        String data = loadDataFromWeb.getResult();
                        final List<DeviceSearch> devs = JSONArray.parseArray(data, DeviceSearch.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (devs != null && !devs.isEmpty()) {
                                    setLocal();
                                    mList.addAll(devs);
                                }
                                mAdapter.notifyDataSetChanged();
                                mLvDevices.loadComplete();
                                mDialog.dismiss();
                                isConduct = true;
                            }
                        });
                    } catch (Exception e) {
                        Logger.e(e);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                mLvDevices.loadComplete();
                                isConduct = true;
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
