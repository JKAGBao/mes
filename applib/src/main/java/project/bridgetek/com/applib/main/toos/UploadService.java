package project.bridgetek.com.applib.main.toos;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.bean.body.LabelBody;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.DeleteFileUtil;
import project.bridgetek.com.bridgelib.toos.LoadDataFromWeb;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by Cong Zhizhong on 18-8-11.
 */

public class UploadService extends Service {
    private static final String ARG_TYPE = "4";
    private BlackDao mBlackDao;
    private LocalUserInfo mUserInfo;
    private String mAccountid;
    private List<TaskInfo> mTaskInfos = new ArrayList<>();
    private static int counter = 0;
    private static Timer timer = new Timer();
    static final int UPDATE_INTERVAL = 1000 * 60 * 60;
    private List<ReException> mList = new ArrayList<>();
    private List<ResultFileInfo> mResuList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            } else if (msg.what == 2) {

            }
        }
    };
    private List<Label> mLabelList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override

            public void run() {
                try {
                    Thread.sleep(1000 * 60);
                } catch (Exception e) {
                    Logger.e("sleep" + e);
                }
                //TODO Auto-generated method stub
                //制定时间后循环执行
                mAccountid = mUserInfo.getUserInfo(Constants.ACCOUNTID);
//                String a = "(";
//                for (int i = 0; i < mContrast.size(); i++) {
//                    if (i == mContrast.size() - 1) {
//                        a = a + "'" + mContrast.get(i).getLineId() + "'";
//                    } else {
//                        a = a + "'" + mContrast.get(i).getLineId() + "',";
//                    }
//                }
//                String b = a + ")";
                mTaskInfos = mBlackDao.getOverTask(0, 20, ARG_TYPE, mAccountid);
                boolean netWorkConnected = NetworkUtil.isNetWorkConnected(UploadService.this);
                if (netWorkConnected) {
                    getUpoladTask();
                    getUpoladException();
                    getLabel();
                }
            }

        }, 0, UPDATE_INTERVAL);
        return super.onStartCommand(intent, flags, startId);
    }

    public void getUpoladException() {
        mList = mBlackDao.getReException(mAccountid);
        for (int i = 0; i < mList.size(); i++) {
            final ReException item = mList.get(i);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(Constants.REEXCEPTION, item);
                        JSONObject target = jsonObject.getJSONObject(Constants.REEXCEPTION);
                        mResuList = mBlackDao.getAbnorFile(item.getException_ID());
                        List<ResultFileInfo> infos = Storage.setBase64(mResuList);
                        target.put(Constants.RESULTFILE, infos);
                        String string = target.toString();
                        LoadDataFromWeb loadDataFromWeb = new LoadDataFromWeb(Constants.API + Constants.RESULT_EXCEPTION, string);
                        String result = loadDataFromWeb.getupload();
                        if (result.equals("true")) {
                            //mInt = i;
                            delResultFile(mResuList, item);
                            handler.obtainMessage(0).sendToTarget();
                        } else {
                            handler.obtainMessage(2).sendToTarget();
                        }
                    } catch (Exception e) {
                        Logger.e("onClick: " + e);
                        handler.obtainMessage(2).sendToTarget();
                    }
                }
            }).start();
        }
    }

    public void delResultFile(List<ResultFileInfo> list, ReException item) {
        if (list.size() > 0) {
            mBlackDao.delResultFile(list.get(0).getException_ID());
            for (int i = 0; i < list.size(); i++) {
                DeleteFileUtil.delFile(list.get(i).getCheckItem_ID(), this);
            }
        }
        mBlackDao.delReException(item.getException_ID());
    }

    public void getUpoladTask() {
        if (mTaskInfos.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < mTaskInfos.size(); i++) {
                        if (!mTaskInfos.get(i).isSubmit()) {
                            TaskInfo taskInfo = mTaskInfos.get(i);
                            List<CheckItem> item = mBlackDao.getCheckItem(mTaskInfos.get(i).getTaskID());
                            try {
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
                                        } else {
                                            mBlackDao.delTastResultFile(checkItem.getResult_ID());
                                            mBlackDao.setCheckSubmit(checkItem.getResult_ID());
                                            mBlackDao.delTastChart(checkItem.getResult_ID());
                                        }
                                    }
                                }
                                if (isSucceed) {
                                    mBlackDao.setTaskSubmit(taskInfo.getTaskID());
                                }
                                handler.obtainMessage(0).sendToTarget();
                            } catch (Exception e) {
                                Logger.e("run: " + e);
                                handler.obtainMessage(2).sendToTarget();
                            }
                        } else {
//                            mProgressDialog.dismiss();
                        }
                    }
                }
            }).start();
        }
    }

    public void getLabel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mLabelList.clear();
                for (int i = 0; i < mTaskInfos.size(); i++) {
                    mLabelList.addAll(mBlackDao.getLabel(mTaskInfos.get(i).getTaskID()));
                }
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
                        }
                    } catch (Exception e) {
                        Logger.e("run: " + e);
                    }
                }

            }
        }).start();
    }

    @Override
    public void onCreate() {
        mBlackDao = BlackDao.getInstance(this);
        mUserInfo = LocalUserInfo.getInstance(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
