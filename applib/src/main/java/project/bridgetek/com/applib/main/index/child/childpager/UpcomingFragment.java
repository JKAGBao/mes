package project.bridgetek.com.applib.main.index.child.childpager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.RegionActivity;
import project.bridgetek.com.applib.main.activity.SnapActivity;
import project.bridgetek.com.applib.main.activity.WatchActivity;
import project.bridgetek.com.applib.main.adapter.TaskAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.bean.body.CheckItemBody;
import project.bridgetek.com.applib.main.bean.body.LabelBody;
import project.bridgetek.com.applib.main.toos.BackGroup;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.RetrofitManager;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;
import retrofit2.Call;

public class UpcomingFragment extends SupportFragment {
    private static final String ARG_TYPE = "0";
    private static final int NUM = 5;
    private TaskAdapter mTaskAdapter;
    private ListView mLvTask;
    private List<TaskInfo> mTaskInfos = new ArrayList<>();
    List<CheckItem> item;
    List<Label> mLabelList;
    private BlackDao mBlackDao;
    private LocalUserInfo mLocalUserInfo;
    private TextView mTvRecovery;
    private String mType, mTaskid, mLineid, mLinename;
    private String mStartTimt;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView mImgLoad;
    private FloatingActionButton mActionButton;
    LocationReceiver locationReceiver;
    int labnum, labcount;
    boolean labComplete = false, taskComplete = false;
    boolean isSucceed = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(_mActivity, R.string.task_upcom_toast_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                mProgress.setProgress(progress);
                mTvSpeed.setText(progress + "%");
                mTvProgress.setText(allcount + "/" + count);
            } else if (msg.what == 4) {
                dialog.dismiss();
                //    Toast.makeText(_mActivity, getString(R.string.upcoming_upload_text) + allcount + getString(R.string.upcoming_num_text) + getString(R.string.upcoming_notupload_text) + (count - allcount) + getString(R.string.upcoming_num_text), Toast.LENGTH_SHORT).show();
                if (labComplete) {
                    taskComplete = false;
                    if (count == 0 && labcount == 0) {
                        Toast.makeText(_mActivity, "无需要上传的数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isSucceed) {
                        getDialog(true, "数据上传成功！", "提交了" + taskcount + getString(R.string.upcoming_num_text) + "点检结果," + labnum + getString(R.string.upcoming_num_text) + "到位数据！");
                    } else {
                        getDialog(false, "数据上传失败！", "提交了" + count + getString(R.string.upcoming_num_text) + "点检结果," + labcount + getString(R.string.upcoming_num_text) + "到位数据！" + "有" + (count - taskcount) + getString(R.string.upcoming_num_text) + "点检结果，" + (labcount - labnum) + getString(R.string.upcoming_num_text) + "到位数据提交失败！");
                    }
                }
            } else if (msg.what == 5) {
                if (taskComplete) {
                    if (count == 0 && labcount == 0) {
                        Toast.makeText(_mActivity, "无需要上传的数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    labComplete = false;
                    if (isSucceed) {
                        getDialog(true, "数据上传成功！", "提交了" + taskcount + getString(R.string.upcoming_num_text) + "点检结果," + labnum + getString(R.string.upcoming_num_text) + "到位数据！");
                    } else {
                        getDialog(false, "数据上传失败！", "提交了" + count + getString(R.string.upcoming_num_text) + "点检结果," + labcount + getString(R.string.upcoming_num_text) + "到位数据！" + "有" + (count - taskcount) + getString(R.string.upcoming_num_text) + "点检结果，" + (labcount - labnum) + getString(R.string.upcoming_num_text) + "到位数据提交失败！");
                    }
                }
            }
        }
    };
    AlertDialog dialog;
    private ProgressBar mProgress;
    private TextView mTvSpeed, mTvProgress;
    int count, allcount, taskcount;
    private int progress;

    public static UpcomingFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, title);
        UpcomingFragment fragment = new UpcomingFragment();
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
        View view = inflater.inflate(R.layout.delegate_task_upcoming, container, false);
        initView(view);
        setOnclick();
        return view;
    }

    @Override
    public void onResume() {
        if (locationReceiver == null) {
            locationReceiver = new LocationReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("location.reportsucc");
            _mActivity.registerReceiver(locationReceiver, filter);
        }
        getTask();
        getRecovery();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationReceiver != null) {
            _mActivity.unregisterReceiver(locationReceiver);
            locationReceiver = null;
        }
    }

    private void initView(View view) {
        mBlackDao = BlackDao.getInstance(_mActivity);
        mLocalUserInfo = LocalUserInfo.getInstance(_mActivity);
        mLvTask = view.findViewById(R.id.lv_task);
        mTvRecovery = view.findViewById(R.id.tv_recovery);
        mImgLoad = view.findViewById(R.id.img_load);
        mActionButton = view.findViewById(R.id.fab_percentage);
        mImgLoad.setVisibility(View.GONE);
        mTvRecovery.setTypeface(HiApplication.MEDIUM);
        mTvRecovery.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
    }

    private void setOnclick() {
        mLvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(_mActivity, RegionActivity.class);
                intent.putExtra(Constants.LINEID, mTaskAdapter.getItem(position).getLineID());
                intent.putExtra(Constants.LINENAME, mTaskAdapter.getItem(position).getLineName());
                Constants.STARTSTOP = mTaskAdapter.getItem(position).getEquipmentStatusEnabled();
                mLocalUserInfo.setUserInfo(Constants.TASKID, mTaskAdapter.getItem(position).getTaskID());
                mLocalUserInfo.setUserInfo(Constants.LABLETYPE, mTaskAdapter.getItem(position).getLableType());
                mLocalUserInfo.setUserInfo(Constants.CYCLETIME, mTaskAdapter.getItem(position).getTaskPlanStartTime());
                mLocalUserInfo.setUserInfo(Constants.TASKTYPE, mTaskAdapter.getItem(position).getTaskType());
                startActivity(intent);
            }
        });
        mTvRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType.equals(Constants.ONE)) {
                    startActivity(new Intent(_mActivity, WatchActivity.class));
                } else {
                    Intent intent = new Intent(_mActivity, SnapActivity.class);
                    intent.putExtra(Constants.LINEID, mLineid);
                    intent.putExtra(Constants.LINENAME, mLinename);
                    startActivity(intent);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(_mActivity);
                Logger.i("netWorkConnected: " + netWorkConnected);
                if (netWorkConnected) {
                    _mActivity.startService(new Intent(_mActivity, BackGroup.class));
                } else {
                    Toast.makeText(_mActivity, R.string.excdelegate_fragment_toast_fableft_text, Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                getUpNum();
//                doUpImg();
            }
        });
    }

    public void getTask() {
        mTaskInfos.clear();
//        String info = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
//        List<AccounTlines> contrast = mBlackDao.getContrast(info);
//        String a = "(";
//        for (int i = 0; i < contrast.size(); i++) {
//            if (i == contrast.size() - 1) {
//                a = a + "'" + contrast.get(i).getLineId() + "'";
//            } else {
//                a = a + "'" + contrast.get(i).getLineId() + "',";
//            }
//        }
//        String b = a + ")";
        String userInfo = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        List<TaskInfo> info1 = mBlackDao.getTaskInfo(0, NUM, ARG_TYPE, userInfo);
        List<TaskInfo> info2 = mBlackDao.getSpotTaskInfo(0, NUM, userInfo);
        List<TaskInfo> info3 = mBlackDao.getTaskInfo(0, NUM, "3", userInfo);
        List<TaskInfo> list = new ArrayList<>();
        for (int i = 0; i < info1.size(); i++) {
            int l0001 = mBlackDao.getLineNumCount(info1.get(i).getTaskID());
            int t001 = mBlackDao.getTaskCheckCount(info1.get(i).getTaskID());
            if (t001 >= l0001 && t001 != 0) {
                list.add(info1.get(i));
            } else {
                mTaskInfos.add(info1.get(i));
            }
        }
        for (int i = 0; i < info2.size(); i++) {
            int l0001 = mBlackDao.getLineNumCount(info2.get(i).getTaskID());
            int t001 = mBlackDao.getTaskCheckCount(info2.get(i).getTaskID());
            if (t001 >= l0001 && t001 != 0) {
                list.add(info2.get(i));
            } else {
                mTaskInfos.add(info2.get(i));
            }
        }
        list.addAll(info3);
        mTaskInfos.addAll(list);
        if (mTaskInfos.size() < 1) {
            mLvTask.setVisibility(View.GONE);
            mImgLoad.setVisibility(View.VISIBLE);
        } else {
            mLvTask.setVisibility(View.VISIBLE);
            mImgLoad.setVisibility(View.GONE);
        }
        mTaskAdapter = new TaskAdapter(_mActivity, mTaskInfos, mBlackDao);
        mLvTask.setAdapter(mTaskAdapter);
    }

    public void getRecovery() {
        mType = mLocalUserInfo.getUserInfo(Constants.TYPE);
        mTaskid = mLocalUserInfo.getUserInfo(Constants.TASKID);
        mLineid = mLocalUserInfo.getUserInfo(Constants.LINEID);
        mLinename = mLocalUserInfo.getUserInfo(Constants.LINENAME);
        mStartTimt = mLocalUserInfo.getUserInfo(Constants.PROTIME);
        String userInfo = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        String prouser = mLocalUserInfo.getUserInfo(Constants.PROUSER);
        if (!userInfo.equals(prouser)) {
            mTvRecovery.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(mType) || "".equals(mType)) {
            mTvRecovery.setVisibility(View.GONE);
        } else {
            if (mType.equals(Constants.ONE)) {
                long time = TimeType.getNowTime();
                long time1 = TimeType.getTime(mStartTimt);
                long l = time - time1;
                if (l > 600) {
                    mTvRecovery.setVisibility(View.GONE);
                } else {
                    List<CheckItemInfo> mobject = mBlackDao.getCheckMobject();
                    if (mobject.size() > 0) {
                        mTvRecovery.setVisibility(View.VISIBLE);
                        mTvRecovery.setText(getString(R.string.upcom_activity_tvrecovery_text) + mobject.get(0).getMobjectName());
                    } else {
                        mTvRecovery.setVisibility(View.GONE);
                    }
                }
            } else {
                List<CheckItemInfo> item = mBlackDao.getSnapCheckItem(mTaskid);
                if (item.size() > 0) {
                    mTvRecovery.setVisibility(View.VISIBLE);
                    mTvRecovery.setText(getString(R.string.upcom_activity_tvrecovery_text) + mLinename);
                } else {
                    mTvRecovery.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 上传进度对话框
     */
    public void showProgressBar() {
        count = 0;
        allcount = 0;
        taskcount = 0;
        List<CheckItem> item = new ArrayList<>();
        for (int i = 0; i < mTaskInfos.size(); i++) {
            TaskInfo taskInfo = mTaskInfos.get(i);
            List<CheckItem> checkItem = mBlackDao.getCheckItem(taskInfo.getTaskID());
            for (int j = 0; j < checkItem.size(); j++) {
                if (!checkItem.get(j).isSubmit()) {
                    item.add(checkItem.get(j));
                }
            }
        }
        count = count + item.size();
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
        builder.setCancelable(false);
        builder.setTitle(R.string.upcoming_builder_title_text);
        LayoutInflater layoutInflater = LayoutInflater.from(_mActivity);
        View view = layoutInflater.inflate(R.layout.progressbar, null);
        mProgress = view.findViewById(R.id.progress);
        mTvSpeed = view.findViewById(R.id.tv_speed);
        mTvProgress = view.findViewById(R.id.tv_progress);
        progress = (int) (((float) allcount / count) * 100);
        mProgress.setProgress(progress);
        mTvSpeed.setText(progress + "%");
        mTvProgress.setText(allcount + "/" + count);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    public void getUpNum() {
        item = new ArrayList<>();
        for (int i = 0; i < mTaskInfos.size(); i++) {
            if (!mTaskInfos.get(i).isSubmit()) {
                item.addAll(mBlackDao.getCheckItem(mTaskInfos.get(i).getTaskID()));
            }
        }
        labComplete = false;
        labnum = 0;
        mLabelList = new ArrayList<>();
        for (int i = 0; i < mTaskInfos.size(); i++) {
            mLabelList.addAll(mBlackDao.getLabel(mTaskInfos.get(i).getTaskID()));
        }
        labcount = mLabelList.size();
        doUpload(0);
        getLabel(0);
    }

    public void doUpload(int position) {
        taskComplete = false;
        isSucceed = true;
        boolean netWorkConnected = NetworkUtil.isNetWorkConnected(_mActivity);
        if (netWorkConnected) {
            if (mTaskInfos.size() > 0) {
                if (item.size() > position) {
                    setUpdata(item.get(position), position);
                } else {
                    taskComplete = true;
                    handler.obtainMessage(4).sendToTarget();
                }
            } else {
                handler.obtainMessage(4).sendToTarget();
            }
        } else {
            Toast.makeText(_mActivity, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpdata(final CheckItem checkItem, final int position) {
        if (checkItem.isSubmit()) {
            doUpload(position + 1);
            return;
        }
        List<ResultFileInfo> mList = new ArrayList<>();
        mList = mBlackDao.getCheckFile(checkItem.getResult_ID());
        List<ResultFileInfo> infos = Storage.setBase64(mList);
        CheckItemBody checkItemBody = new CheckItemBody(checkItem, infos);
        if (checkItem.getExceptionID().equals("1")) {
            List<Float> chart = mBlackDao.getChart(checkItem.getResult_ID());
            checkItemBody.setWaveData(chart);
        }
        Call<String> stringCall = RetrofitManager.instance(true).upCheckItem(checkItemBody);
        stringCall.enqueue(new RetrofitManager.SimpleCallback<String>() {
            @Override
            public void onResponseSuccess(String result) {
                if (result.equals("true")) {
                    // isSucceed = false;
                    mBlackDao.setCheckSubmit(checkItem.getResult_ID());
                    allcount++;
                    taskcount++;
                    progress = (int) (((float) allcount / count) * 100);
                } else {
                    Logger.e(checkItem + "上传失败");
                    allcount++;
                    isSucceed = false;
                }
                handler.obtainMessage(3).sendToTarget();
                doUpload(position + 1);
            }

            @Override
            public void onResponseFailure() {
                super.onResponseFailure();
                isSucceed = false;
                allcount++;
                handler.obtainMessage(3).sendToTarget();
                doUpload(position + 1);
            }
        });

    }

    public class LocationReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals("location.reportsucc")) {
                String extra = intent.getStringExtra("key");
                if (extra.equals("6")) {
                    getTask();
                    getRecovery();
                    getDialog(true, "同步成功", intent.getStringExtra("error"));
                    swipeRefreshLayout.setRefreshing(false);
                } else if (extra.equals("2") || extra.equals("3") || extra.equals("5")) {
                    getDialog(false, "同步失败", intent.getStringExtra("error"));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    }

    public void getLabel(int position) {
        if (mLabelList.size() > position) {
            setUpLabel(mLabelList.get(position), position);
        } else {
            labComplete = true;
            handler.obtainMessage(5).sendToTarget();
        }
    }

    public void setUpLabel(final Label label, final int position) {
        Call<LabelBody> labelBodyCall = RetrofitManager.instance(true).upLabel(label);
        labelBodyCall.enqueue(new RetrofitManager.SimpleCallback<LabelBody>() {
            @Override
            public void onResponseSuccess(LabelBody result) {
                if (result.getRetcode() == 0) {
                    mBlackDao.delLabel(label.getId(), label.getTaskID());
                    labnum++;
                } else {
                    isSucceed = false;
                }
                getLabel(position + 1);
            }

            @Override
            public void onResponseFailure() {
                isSucceed = false;
                getLabel(position + 1);
                super.onResponseFailure();

            }
        });
    }

    public void getDialog(boolean message, String msg, String count) {
        View view = LayoutInflater.from(_mActivity).inflate(R.layout.dialog_upload, null, false);
        final Dialog builder = new Dialog(_mActivity, R.style.update_dialog);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        TextView tvTaskName = view.findViewById(R.id.tv_taskName);
        Button btPreservation = view.findViewById(R.id.bt_preservation);
        ImageView imgMessage = view.findViewById(R.id.img_message);
        TextView tvUpload = view.findViewById(R.id.tv_upload);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        if (message) {
            imgMessage.setBackgroundResource(R.drawable.ic_success);
            tvUpload.setText("成功");
        } else {
            imgMessage.setBackgroundResource(R.drawable.ic_fail);
            tvUpload.setText("失败");
        }
        btPreservation.setTypeface(HiApplication.MEDIUM);
        tvTaskName.setText(msg);
        tvMessage.setText(count);
        btPreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.setContentView(view);
        builder.show();
    }
}
