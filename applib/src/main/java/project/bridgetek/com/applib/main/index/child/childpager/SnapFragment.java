package project.bridgetek.com.applib.main.index.child.childpager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.adapter.SnapAdapter;
import project.bridgetek.com.applib.main.bean.Check;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.bean.body.LabelBody;
import project.bridgetek.com.applib.main.bean.onFragmentTriggerResult;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Scaveng;

public class SnapFragment extends SupportFragment implements onFragmentTriggerResult {
    private static final String ARG_TYPE = "3";
    private SnapAdapter mSnapAdapter;
    private ListView mLvTask;
    private List<CheckItemInfo> mTaskInfos = new ArrayList<>();
    private List<TaskInfo> mList = new ArrayList<>();
    private BlackDao mBlackDao;
    private String mTitle;
    private LocalUserInfo mLocalUserInfo;
    private String mMusername, mGroupname;
    String UserCode;
    private ImageView mImgLoad;
    private String mTaskID;
    private ProgressDialog mDialog;
    private String mAccountID;
    private boolean isScan = true;

    FloatingActionButton mFabDown;


    public void onDown() {
        if (isScan) {
            Scaveng scaveng = new Scaveng(_mActivity, 10);
            scaveng.getScav();
        } else {
            upLabel();
        }
    }


    public static SnapFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, title);
        SnapFragment fragment = new SnapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delegate_task_snap, container, false);
        ButterKnife.bind(this, view);
        mDialog = Storage.getPro(_mActivity, getString(R.string.delegate_exception_dialog_text));
        initView(view);
        mTaskID = mLocalUserInfo.getUserInfo(Constants.TASKID);
        mAccountID = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        mMusername = mLocalUserInfo.getUserInfo(Constants.USERNAME);
        mGroupname = mLocalUserInfo.getUserInfo(Constants.GROUPNAME);
        UserCode = mLocalUserInfo.getUserInfo(Constants.GROUPID);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void initView(View view) {
        mLocalUserInfo = LocalUserInfo.getInstance(_mActivity);
        mBlackDao = BlackDao.getInstance(_mActivity);
        mLvTask = view.findViewById(R.id.lv_task);
        mImgLoad = view.findViewById(R.id.img_load);
        mImgLoad.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentTriggerResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                final String content = data.getStringExtra("codedContent");
                boolean mate = false;
                Logger.i(data);
                onTask(content);
            }
        }
    }

    public void onTask(final String task) {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("labelCode", Storage.getLabelCode(task));
                    jsonObject.put("accountId", mAccountID);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.LABELCHECKITEMS, json);
                    String result = loadDataFromWeb.getResult();
                    Check check = JSONObject.parseObject(result, Check.class);
                    if (check.getCheckitems() != null) {
                        if (check.getCheckitems().size() > 0) {
                            setLabel(check.getCheckitems().get(0), task);
                            mBlackDao.delSnapItem();
                            for (int i = 0; i < check.getCheckitems().size(); i++) {
                                check.getCheckitems().get(i).setTaskID(check.getCheckitems().get(i).getLabelName());
                                mBlackDao.addCheckItemInfo(check.getCheckitems().get(i));
                            }
                        }
                    }
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initUI();
                            mDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    public void setLabel(CheckItemInfo info, String LabelCode) {
        Label label = new Label(info.getLabelID(), LabelCode, "9", info.getLineID(), TimeType.dateToString(), TimeType.dateToString(), mAccountID, mMusername, UserCode, mGroupname,
                info.getShiftName(), info.getGroupName(), 0, info.getTaskID());
        mBlackDao.addLabel(label);
    }

    public void upLabel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Label snapLabel = mBlackDao.getSnapLabel();
                    long time1 = TimeType.getTime(snapLabel.getEnd_TM());
                    long time2 = TimeType.getTime(snapLabel.getStart_TM());
                    int i = (int) (time1 - time2);
                    snapLabel.setTimeCount(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.LABELID, snapLabel);
                    JSONObject object = jsonObject.getJSONObject(Constants.LABELID);
                    String string = object.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.LABEL, string);
                    String result = loadDataFromWeb.getupload();
                    LabelBody labelBody = JSONObject.parseObject(result, LabelBody.class);
                    if (labelBody.getRetcode() == 0) {
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBlackDao.delSnapItem();
                                mBlackDao.delLabel(snapLabel.getId(), snapLabel.getTaskID());
                                initUI();
                            }
                        });
                    } else {
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(_mActivity, R.string.snap_uplabel_toast_text, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(_mActivity, R.string.snap_uplabel_toast_text, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Logger.e("run: " + e);
                }
            }
        }).start();
    }

    public void initUI() {
        mTaskInfos.clear();
        mTaskInfos = mBlackDao.getSnapItemInfo();
        if (mTaskInfos.size() > 0) {
            mLvTask.setVisibility(View.VISIBLE);
            mImgLoad.setVisibility(View.GONE);
            isScan = false;
            mFabDown.setImageResource(R.drawable.fab_close);
        } else {
            mLvTask.setVisibility(View.GONE);
            mImgLoad.setVisibility(View.VISIBLE);
            isScan = true;
            mFabDown.setImageResource(R.drawable.ic_scan);
        }
        mSnapAdapter = new SnapAdapter(mTaskInfos, _mActivity, true);
        mLvTask.setAdapter(mSnapAdapter);
    }
}
