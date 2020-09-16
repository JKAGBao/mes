package project.bridgetek.com.applib.main.toos;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.bridgetek.com.applib.main.bean.Check;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Devices;
import project.bridgetek.com.applib.main.bean.Equipment;
import project.bridgetek.com.applib.main.bean.Task;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by Cong Zhizhong on 18-6-22.
 */

public class BackGroup extends Service {
    private List<TaskInfo> mTaskInfos = new ArrayList<>();
    private List<TaskInfo> mInfoList = new ArrayList<>();
    private BlackDao mBlackDao;
    private LocalUserInfo mInstance;
    private List<CheckItemInfo> mList = new ArrayList<>();
    private String mAccountID, mBadgeid;
    private String errorMessage = "";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getCheck();
                // getDevices();
            } else if (msg.what == 2) {
                mInstance.setUserInfo(Constants.SERVICETIME, TimeType.dateToString());
                //  Toast.makeText(BackGroup.this, R.string.group_toast_task, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                mInstance.setUserInfo(Constants.SERVICETIME, TimeType.dateToString());
                //   Toast.makeText(BackGroup.this, R.string.group_toast_check, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 5) {
                mInstance.setUserInfo(Constants.SERVICETIME, TimeType.dateToString());
                //    Toast.makeText(BackGroup.this, R.string.group_toast_contrast, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 6) {
                Log.i("CONG", "handleMessage: 444");
                mInstance.setUserInfo(Constants.SERVICETIME, "");
                //setLabel();
            }
            if (msg.what != 1) {
                Intent intent = new Intent();
                intent.putExtra("key", "" + msg.what);
                intent.putExtra("error", errorMessage);
                intent.setAction("location.reportsucc");
                sendBroadcast(intent);
            }
        }
    };
    private String mUsername, mGroupname, mUserCode;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAccountID = mInstance.getUserInfo(Constants.ACCOUNTID);
        mBadgeid = mInstance.getUserInfo(Constants.BEDGEID);
        mUsername = mInstance.getUserInfo(Constants.USERNAME);
        mGroupname = mInstance.getUserInfo(Constants.GROUPNAME);
        mUserCode = mInstance.getUserInfo(Constants.GROUPID);
        boolean netWorkConnected = NetworkUtil.isNetWorkConnected(this);
        if (netWorkConnected) {
            getTask();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void getTask() {
        if (!(TextUtils.isEmpty(mAccountID)) && !(mAccountID.equals(""))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(Constants.ACCOUNTID, mAccountID);
                        String json = jsonObject.toString();
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.TASKS, json);
                        String data = loadDataFromWeb.getResult();
                        Task task = JSONObject.parseObject(data, Task.class);
                        if (task.getTasks_num() == -1) {
                            errorMessage = task.getErrorMessage();
                            handler.sendEmptyMessageDelayed(2, 2000);
                            return;
                        }
                        if (task.getTasks_num() == 0) {
                            errorMessage = "没有需要同步的任务";
                            handler.sendEmptyMessageDelayed(2, 2000);
                            return;
                        }
                        List<TaskInfo> list = new ArrayList<>();
                        if (task.getTasks() != null) {
                            list = task.getTasks();
                        }
                        mTaskInfos = mBlackDao.getAllTask();
                        mInfoList.clear();
                        for (int i = 0; i < list.size(); i++) {
                            TaskInfo taskInfo = list.get(i);
                            boolean s = false;
                            for (int y = 0; y < mTaskInfos.size(); y++) {
                                if (taskInfo.getTaskID().equals(mTaskInfos.get(y).getTaskID()) && "1".equals(mTaskInfos.get(y).getSync())) {
                                    s = true;
                                }
                            }
                            if (!s) {
                                mBlackDao.addBlackNum(taskInfo, mAccountID);
                                mInfoList.add(taskInfo);
                            }
                        }
                        mBlackDao.delRepeatData();
                        mBlackDao.delTaskInfo();
                        handler.obtainMessage(1).sendToTarget();
                    } catch (Exception e) {
                        errorMessage = "接口访问出错";
                        handler.sendEmptyMessageDelayed(2, 2000);
                        Logger.e("run: " + e);
                    }
                }
            }).start();
        }
    }

    public void getCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < mInfoList.size(); i++) {
                        list.add(mInfoList.get(i).getTaskID());
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TASKIDS, list);
                    String json = jsonObject.toString();
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.CHECKITEMS, json);
                    String result = loadDataFromWeb.getResult();
                    Check check = JSONObject.parseObject(result, Check.class);
                    if (check.getItemnum() == -1) {
                        errorMessage = check.getErrorMessage();
                        handler.sendEmptyMessageDelayed(3, 1700);
                        return;
                    }
                    if (check.getItemnum() != check.getCheckitems().size()) {
                        errorMessage = "任务数量不相符！";
                        handler.sendEmptyMessageDelayed(3, 1700);
                        return;
                    }
                    if (check.getItemnum() == 0) {
                        errorMessage = "没有需要同步的任务！";
                        handler.sendEmptyMessageDelayed(6, 1700);
                        return;
                    }
                    if (check.getCheckitems() != null) {
                        mList = check.getCheckitems();
                    }
                    if (mList.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mBlackDao.delCheckItem(list.get(i));
                        }
//                        mBlackDao.cleCheckItemInfo();
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        mBlackDao.addCheckItemInfo(mList.get(i));
                    }
                    for (int i = 0; i < mInfoList.size(); i++) {
                        mBlackDao.setTaskSync(mInfoList.get(i));
                    }
                    errorMessage = "";
                    handler.sendEmptyMessageDelayed(6, 1700);
                } catch (Exception e) {
                    Logger.e(e);
                    errorMessage = "接口访问出错";
                    handler.sendEmptyMessageDelayed(3, 1700);
                }
            }
        }).start();
    }

    public void getDevices() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constants.ACCOUNTID, mAccountID);
                    LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.ACCOUNT_DEVICES, map);
                    String data = loadDataFromWeb.getData();
                    Equipment task = JSONObject.parseObject(data, Equipment.class);
                    List<Devices> devices = new ArrayList<>();
                    if (task.getDevices() != null) {
                        devices = task.getDevices();
                    }
                    if (devices.size() > 0) {
                        mBlackDao.cleDevices();
                    }
                    for (int i = 0; i < devices.size(); i++) {
                        mBlackDao.addDevices(devices.get(i), task.getAccountId());
                    }
                    handler.obtainMessage(4).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(5).sendToTarget();
                    Logger.e("run: " + e);
                }
            }
        }).start();
    }

//    public void setLabel() {
//        for (int i = 0; i < mInfoList.size(); i++) {
//            TaskInfo taskInfo = mInfoList.get(i);
//            List<CheckItemInfo> info = mBlackDao.getCheckItemInfo(taskInfo.getLineID());
//            for (int j = 0; j < info.size(); j++) {
//                Label label = new Label(info.get(j).getLabelID(), info.get(j).getLabelCode(), taskInfo.getTaskType(), taskInfo.getLineID(), "", "", mAccountID, mUsername, mUserCode, mGroupname,
//                        info.get(j).getShiftName(), info.get(j).getGroupName(), 0, taskInfo.getTaskID());
//                mBlackDao.addLabel(label);
//            }
//        }
//    }

    @Override
    public void onCreate() {
        mBlackDao = BlackDao.getInstance(this);
        mInstance = LocalUserInfo.getInstance(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
