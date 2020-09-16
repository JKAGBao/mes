package project.bridgetek.com.applib.main.index.child.childpager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import com.alibaba.fastjson.JSONObject;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.OverActivity;
import project.bridgetek.com.applib.main.adapter.OverAdapter;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.bean.body.LabelBody;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.NetworkUtil;
import project.bridgetek.com.applib.main.toos.Storage;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

public class OverFragment extends SupportFragment {
    private static final String ARG_TYPE = "3";
    private String mTitle;
    private ListView mLvTask;
    private FloatingActionButton mFabPercentage;
    private BlackDao mBlackDao;
    private List<TaskInfo> mTaskInfos = new ArrayList<>();
    private OverAdapter mOverAdapter;
    private LocalUserInfo mLocalUserInfo;
    private String mAccountid;
    ProgressDialog mProgressDialog;
    private List<Label> mLabelList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView mImgLoad;
    private boolean upload = true;
    int labnum, labcount;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                getTask();
                mOverAdapter = new OverAdapter(_mActivity, mTaskInfos, mBlackDao);
                mLvTask.setAdapter(mOverAdapter);
                mProgressDialog.dismiss();
            } else if (msg.what == 1) {
                getTask();
                mOverAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            } else if (msg.what == 3) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(_mActivity, R.string.task_upcom_toast_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 5) {
                mProgress.setProgress(progress);
                mTvSpeed.setText(progress + "%");
                mTvProgress.setText(allcount + "/" + count);
            } else if (msg.what == 4) {
                dialog.dismiss();
                if (count == 0 && labcount == 0) {
                    Toast.makeText(_mActivity, "无需要上传的数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (upload) {
                    getDialog(true, "数据上传成功！", "提交了" + taskcount + getString(R.string.upcoming_num_text) + "点检结果," + labcount + getString(R.string.upcoming_num_text) + "到位数据！");
                } else {
                    getDialog(false, "数据上传失败！", "提交了" + count + getString(R.string.upcoming_num_text) + "点检结果," + labcount + getString(R.string.upcoming_num_text) + "到位数据！" + "有" + (count - taskcount) + getString(R.string.upcoming_num_text) + "点检结果，" + (labcount - labnum) + getString(R.string.upcoming_num_text) + "到位数据未完成提交！");
                }
                //   Toast.makeText(_mActivity, getString(R.string.upcoming_upload_text) + allcount + getString(R.string.upcoming_num_text) + getString(R.string.upcoming_notupload_text) + (count - allcount) + getString(R.string.upcoming_num_text), Toast.LENGTH_SHORT).show();
            }
        }
    };

    AlertDialog dialog;
    private ProgressBar mProgress;
    private TextView mTvSpeed, mTvProgress;
    int count, allcount, taskcount;
    private int progress;

    public static OverFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, title);
        OverFragment fragment = new OverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(ARG_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delegate_task_over, container, false);
        initView(view);
        setOnclick();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskInfos.clear();
        mAccountid = mLocalUserInfo.getUserInfo(Constants.ACCOUNTID);
        getTask();
        mOverAdapter = new OverAdapter(_mActivity, mTaskInfos, mBlackDao);
        mLvTask.setAdapter(mOverAdapter);
        //getLabel();
    }

    private void initView(View view) {
        mLvTask = view.findViewById(R.id.lv_task);
        mFabPercentage = view.findViewById(R.id.fab_percentage);
        mImgLoad = view.findViewById(R.id.img_load);
        mImgLoad.setVisibility(View.GONE);
        mBlackDao = BlackDao.getInstance(_mActivity);
        mLocalUserInfo = LocalUserInfo.getInstance(_mActivity);
        mProgressDialog = Storage.getProgress(_mActivity);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.theme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
    }

    public void getLabel() {
        labnum = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                labcount = mLabelList.size();
                for (int j = 0; j < mLabelList.size(); j++) {
                    Label label = mLabelList.get(j);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.LABELID, label);
                    JSONObject object = jsonObject.getJSONObject(Constants.LABELID);
                    String string = object.toString();
                    try {
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.LABEL, string);
                        String result = loadDataFromWeb.getupload();
                        LabelBody labelBody = JSONObject.parseObject(result, LabelBody.class);
                        if (labelBody.getRetcode() == 0) {
                            mBlackDao.delLabel(label.getId(), label.getTaskID());
                            labnum++;
                        } else {
                            upload = false;
                        }
                    } catch (Exception e) {
                        upload = false;
                        Logger.e("run: " + e);
                    }
                }
            }
        }).start();
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

    public void setOnclick() {
        mFabPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(_mActivity);
                upload = true;
                if (netWorkConnected) {
                    showProgressBar();
                    mLabelList.clear();
                    for (int i = 0; i < mTaskInfos.size(); i++) {
                        mLabelList.addAll(mBlackDao.getLabel(mTaskInfos.get(i).getTaskID()));
                    }
                    getLabel();
                    if (mTaskInfos.size() > 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < mTaskInfos.size(); i++) {
                                        if (!mTaskInfos.get(i).isSubmit()) {
                                            TaskInfo taskInfo = mTaskInfos.get(i);
                                            List<CheckItem> item = mBlackDao.getCheckItem(mTaskInfos.get(i).getTaskID());
                                            boolean isSucceed = true;
                                            for (int j = 0; j < item.size(); j++) {
                                                CheckItem checkItem = item.get(j);
                                                if (!checkItem.isSubmit()) {
                                                    JSONObject jsonObject = new JSONObject();
                                                    jsonObject.put(Constants.CHECKINFO, checkItem);
                                                    JSONObject target = jsonObject.getJSONObject(Constants.CHECKINFO);
                                                    List<ResultFileInfo> mList = new ArrayList<>();
                                                    mList = mBlackDao.getCheckFile(item.get(j).getResult_ID());
                                                    List<ResultFileInfo> infos = Storage.setBase64(mList);
                                                    target.put(Constants.RESULTFILE, infos);
                                                    if (checkItem.getExceptionID().equals("1")) {
                                                        List<Float> chart = mBlackDao.getChart(checkItem.getResult_ID());
                                                        target.put(Constants.WAVEDATA, chart);
                                                    }
                                                    String string = target.toString();
                                                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.CHECKITEM, string);
                                                    String result = loadDataFromWeb.getupload();
                                                    if (!result.equals("true")) {
                                                        isSucceed = false;
                                                        upload = false;
                                                        allcount++;
                                                    } else {
                                                        mBlackDao.delTastResultFile(checkItem.getResult_ID());
                                                        mBlackDao.delTastChart(checkItem.getResult_ID());
                                                        mBlackDao.setCheckSubmit(checkItem.getResult_ID());
                                                        allcount++;
                                                        taskcount++;
                                                        progress = (int) (((float) allcount / count) * 100);
                                                    }
                                                } else {
                                                    mBlackDao.delTastResultFile(checkItem.getResult_ID());
                                                    mBlackDao.delTastChart(checkItem.getResult_ID());
                                                    mBlackDao.setCheckSubmit(checkItem.getResult_ID());
                                                }
                                                handler.obtainMessage(5).sendToTarget();
                                            }
                                            if (isSucceed) {
                                                mBlackDao.setTaskSubmit(taskInfo.getTaskID());
                                            }
                                        }
                                    }
                                    handler.obtainMessage(0).sendToTarget();
                                    handler.obtainMessage(4).sendToTarget();
                                } catch (Exception e) {
                                    upload = false;
                                    Log.i("CONG", "run: " + e);
                                    handler.obtainMessage(4).sendToTarget();
                                }
                            }
                        }).start();
                    } else {
                        handler.obtainMessage(4).sendToTarget();
                    }
                } else {
                    Toast.makeText(_mActivity, R.string.mactivity_excdelegate_toast_connect_text, Toast.LENGTH_SHORT).show();
                }

            }
        });
        mLvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(_mActivity, OverActivity.class);
                intent.putExtra(Constants.LINEID, mOverAdapter.getItem(position).getLineID());
                intent.putExtra(Constants.LINENAME, mOverAdapter.getItem(position).getLineName());
                mLocalUserInfo.setUserInfo(Constants.OVERTASKID, mOverAdapter.getItem(position).getTaskID());
                mLocalUserInfo.setUserInfo(Constants.CYCLETIME, mOverAdapter.getItem(position).getTaskPlanStartTime());
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getTask();
                    handler.sendEmptyMessageDelayed(1, 2000);
                } catch (Exception e) {
                    handler.obtainMessage(3).sendToTarget();
                    e.printStackTrace();
                }
            }
        });
    }

    public void getTask() {
//        String a = "(";
//        for (int i = 0; i < mContrast.size(); i++) {
//            if (i == mContrast.size() - 1) {
//                a = a + "'" + mContrast.get(i).getLineId() + "'";
//            } else {
//                a = a + "'" + mContrast.get(i).getLineId() + "',";
//            }
//        }
//        String b = a + ")";
        mTaskInfos = mBlackDao.getOverTask(0, 20, ARG_TYPE, mAccountid);
        if (mTaskInfos.size() < 1) {
            mLvTask.setVisibility(View.GONE);
            mImgLoad.setVisibility(View.VISIBLE);
        } else {
            mLvTask.setVisibility(View.VISIBLE);
            mImgLoad.setVisibility(View.GONE);
        }
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
