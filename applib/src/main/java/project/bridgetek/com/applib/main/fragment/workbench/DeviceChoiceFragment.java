package project.bridgetek.com.applib.main.fragment.workbench;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import project.bridgetek.com.applib.main.adapter.workbench.ChoiceAdapter;
import project.bridgetek.com.applib.main.adapter.workbench.ChoiceDeviceAdapter;
import project.bridgetek.com.applib.main.bean.workbench.TreeNode;
import project.bridgetek.com.applib.main.toos.Card.MyLayoutManager;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * A simple {@link Fragment} subclass.
 * 设备树碎片
 */
public class DeviceChoiceFragment extends Fragment {
    private ChoiceAdapter mAdapter;
    private ChoiceDeviceAdapter mDeviceAdapter;
    private String mark;
    private String mIndicate;
    private List<TreeNode.ChildNodesBean> mList = new ArrayList<>();
    private String nodeCode = "#";
    private String nodeId;
    private String nodeType;
    private List<TreeNode.ChildNodesBean> mBeanList = new ArrayList<>();
    private ProgressDialog mDialog;
    @BindView(R2.id.rl_choice)
    RecyclerView mRlChoice;
    @BindView(R2.id.lv_devices)
    ListView mLvDevices;
    @BindView(R2.id.tv_tips)
    TextView mTvTips;
    @BindView(R2.id.ll_select)
    LinearLayout mLlSelect;
    @BindView(R2.id.cb_celect)
    CheckBox mCbCelect;
    @BindView(R2.id.tv_define)
    TextView mTvDefine;

    @OnClick(R2.id.tv_device_query)
    public void setReturn() {
        if (mList.size() > 0) {
            mList.remove(mList.get(mList.size() - 1));
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setSelection(false);
                if (i == mList.size() - 1) {
                    mList.get(i).setSelection(true);
                }
            }
            mAdapter.notifyDataSetChanged();
            if (mList.size() > 0) {
                nodeCode = mList.get(mList.size() - 1).getChildNodeCode();
                nodeType = mList.get(mList.size() - 1).getChildNodeType();
                nodeId = mList.get(mList.size() - 1).getChildNodeId();
            } else {
                nodeCode = "#";
                nodeType = "";
                nodeId = "";
            }
            setDevices();
        }
    }

    @OnClick(R2.id.ll_select)
    public void setSelect() {
        mCbCelect.setChecked(!mCbCelect.isChecked());
        if (mCbCelect.isChecked()) {
            for (int i = 0; i < mBeanList.size(); i++) {
                if (mBeanList.get(i).getChildNodeType().equals("MObject")) {
                    mBeanList.get(i).setSelection(true);
                }
            }
        } else {
            for (int i = 0; i < mBeanList.size(); i++) {
                if (mBeanList.get(i).getChildNodeType().equals("MObject")) {
                    mBeanList.get(i).setSelection(false);
                }
            }
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    @OnClick(R2.id.tv_define)
    public void setDefine() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mBeanList.size(); i++) {
            if (mBeanList.get(i).isSelection()) {
                list.add(mBeanList.get(i).getChildNodeCode());
            }
        }
        if (list.size() > 0) {
            StateActivity activity = (StateActivity) getActivity();
            activity.addFragment("choice", DeviceSelectFragment.newInstance(list), "select");
        } else {
            Toast.makeText(getContext(), R.string.device_choice_toast_celect_text, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.ic_back)
    public void onBack() {
        getFragmentManager().popBackStackImmediate();
    }

    @OnItemClick(R2.id.lv_devices)
    public void onItemOnclick(int position) {
        TreeNode.ChildNodesBean childNodesBean = mBeanList.get(position);
        mCbCelect.setChecked(false);
        if (childNodesBean.getChildNodeType().equals("MObject")) {
            if (mIndicate.equals("1")) {
                mBeanList.get(position).setSelection(!mBeanList.get(position).isSelection());
                mDeviceAdapter.notifyDataSetChanged();
                setAllCelect();
            }
//            else {
//                Intent intent = new Intent(getActivity(), DeviceArchiceActivity.class);
//                intent.putExtra(Constants.TASKIDS, childNodesBean.getChildNodeCode());
//                startActivity(intent);
//                getActivity().finish();
//            }
        } else {
            setLevel(childNodesBean);
            nodeCode = childNodesBean.getChildNodeCode();
            nodeId = childNodesBean.getChildNodeId();
            nodeType = childNodesBean.getChildNodeType();
            setDevices();
        }
    }

    public DeviceChoiceFragment() {
        // Required empty public constructor
    }

    public static DeviceChoiceFragment newInstance(String mark, String indicate) {
        DeviceChoiceFragment fragment = new DeviceChoiceFragment();
        Bundle args = new Bundle();
        args.putString(Constants.MARK, mark);
        args.putString(Constants.DEVCODE, indicate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mark = getArguments().getString(Constants.MARK);
        mIndicate = getArguments().getString(Constants.DEVCODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_choice, container, false);
        ButterKnife.bind(this, view);
        mDialog = Storage.getPro(getContext(), getString(R.string.delegate_exception_dialog_text));
        mAdapter = new ChoiceAdapter(mList, getContext());
        mRlChoice.setAdapter(mAdapter);
        MyLayoutManager layoutManager = new MyLayoutManager(getContext(), false);
        mRlChoice.setLayoutManager(layoutManager);
        mDeviceAdapter = new ChoiceDeviceAdapter(mBeanList, getContext(), mIndicate);
        mLvDevices.setAdapter(mDeviceAdapter);
        setDevices();
        setOnclick();
        return view;
    }

    public void setLevel(TreeNode.ChildNodesBean bean) {
        mList.add(bean);
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelection(false);
            if (i == mList.size() - 1) {
                mList.get(i).setSelection(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setOnclick() {
        mAdapter.setRecyclerItemClickListener(new ChoiceAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int position, List<TreeNode.ChildNodesBean> dataBeanList) {
                List<TreeNode.ChildNodesBean> list = new ArrayList<>();
                for (int i = 0; i < position + 1; i++) {
                    mList.get(i).setSelection(false);
                    list.add(mList.get(i));
                }
                list.get(list.size() - 1).setSelection(true);
                mList.clear();
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                nodeCode = mList.get(mList.size() - 1).getChildNodeCode();
                nodeType = mList.get(mList.size() - 1).getChildNodeType();
                nodeId = mList.get(mList.size() - 1).getChildNodeId();
                setDevices();
            }
        });
    }

    public void setDevices() {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nodeCode", nodeCode);
                    jsonObject.put("nodeId", nodeId);
                    jsonObject.put("nodeType", nodeType);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.TREENODES, json);
                    String data = loadDataFromWeb.getResult();
                    final TreeNode devs = JSONObject.parseObject(data, TreeNode.class);
                    if (devs.getChildNodes() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean isMObject = false;
                                mBeanList.clear();
                                mBeanList.addAll(devs.getChildNodes());
                                mDeviceAdapter.notifyDataSetChanged();
                                for (int i = 0; i < mBeanList.size(); i++) {
                                    if (mBeanList.get(i).getChildNodeType().equals("MObject")) {
                                        isMObject = true;
                                        break;
                                    }
                                }
                                if (isMObject) {
                                    mTvTips.setVisibility(View.VISIBLE);
                                    if (mIndicate.equals("1")) {
                                        mLlSelect.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    mTvTips.setVisibility(View.GONE);
                                    mLlSelect.setVisibility(View.GONE);
                                }
                                if (mIndicate.equals("1")) {
                                    mTvDefine.setVisibility(View.VISIBLE);
                                } else {
                                    mTvDefine.setVisibility(View.GONE);
                                }
                                mDialog.dismiss();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                            }
                        });
                    }
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

    public void setAllCelect() {
        boolean allCelect = true;
        for (int i = 0; i < mBeanList.size(); i++) {
            if (!mBeanList.get(i).isSelection()) {
                allCelect = false;
                break;
            }
        }
        mCbCelect.setChecked(allCelect);
    }
}
