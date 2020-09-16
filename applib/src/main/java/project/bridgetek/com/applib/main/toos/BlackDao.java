package project.bridgetek.com.applib.main.toos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.bean.Devices;
import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.bean.ReException;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.bridgelib.toos.Constants;

/**
 * Created by Cong Zhizhong on 18-6-12.
 */

public class BlackDao {

    //把数据库创建出来
    private BlackNumDbHelper mDbHelper;
    //black_num表名
    private String mTaskInfo = "taskInfo";
    private String mCheckItemInfo = "checkItemInfo";
    private String mResultFile = "resultFile";
    private String mException = "exception";
    private String mCheckItem = "checkItem";
    private String mDeviceInfo = "deviceInfo";
    private String mCheckMobject = "checkMobject";
    private String mChart = "chart";
    private String mLabel = "label";
    private String mLineMobject = "lineMobject";

    //单例模式
    //不能让每一个类都能new一个  那样就不是同一个对象了 所以首先构造函数要私有化    以上下文作为参数
    private BlackDao(Context ctx) {
        //由于数据库只需要调用一次，所以在单例中建出来
        mDbHelper = new BlackNumDbHelper(ctx, "jzdapp.db", null, 1);
    }

    //public static 为静态类型  要调用就要有一个静态的变量    为私有的
    private static BlackDao minstance;

    //既然BlackDao类是私有的  那么别的类就不能够调用    那么就要提供一个public static（公共的  共享的）的方法
    //方法名为getInstance 参数为上下文    返回值类型为BlackDao
    //要加上一个synchronized（同步的）
    //如果同时有好多线程 同时去调用getInstance()方法  就可能会出现一些创建（new）多个BlackDao的现象  所以要加上synchronized
    public static synchronized BlackDao getInstance(Context ctx) {
        //就可以判断  如果为空 就创建一个， 如果不为空就还用原来的  这样整个应用程序中就只能获的一个实例
        if (minstance == null) {
            minstance = new BlackDao(ctx);
        }
        return minstance;
    }

    /**
     * 添加任务列表
     *
     * @param taskInfo
     */
    public void addBlackNum(TaskInfo taskInfo, String userId) {
        //获得一个可写的数据库的一个引用
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TASKID, taskInfo.getTaskID());
        values.put(Constants.LINEID, taskInfo.getLineID());
        values.put(Constants.TASKNAME, taskInfo.getTaskName());
        values.put(Constants.LINENAME, taskInfo.getLineName());
        values.put(Constants.TASKTYPE, taskInfo.getTaskType());
        values.put(Constants.LABLETYPE, taskInfo.getLableType());
        if (TextUtils.isEmpty(taskInfo.getTaskPlanStartTime()) || taskInfo.getTaskPlanStartTime().equals("")) {
            values.put(Constants.TASKPLANSTARTTIME, TimeType.getTimeType(taskInfo.getTaskUpdateTime()));
        } else {
            values.put(Constants.TASKPLANSTARTTIME, TimeType.getTimeType(taskInfo.getTaskPlanStartTime()));
        }
        if (TextUtils.isEmpty(taskInfo.getTaskPlanEndTime()) || taskInfo.getTaskPlanEndTime().equals("")) {
            values.put(Constants.TASKPLANENDTIME, TimeType.getTimeType(taskInfo.getTaskUpdateTime()));
        } else {
            values.put(Constants.TASKPLANENDTIME, TimeType.getTimeType(taskInfo.getTaskPlanEndTime()));
        }
        values.put(Constants.TASKUPDATETIME, TimeType.getTimeType(taskInfo.getTaskUpdateTime()));
        values.put(Constants.SUBMIT, false);
        values.put(Constants.ACCOUNTID, userId);
        values.put(Constants.EQUIPMENTSTATUSENABLED, taskInfo.getEquipmentStatusEnabled());
        // 参数一：表名，参数三，是插入的内容
        // 参数二：只要能保存 values中是有内容的，第二个参数可以忽略
        db.insert(mTaskInfo, null, values);
    }

    /**
     * 存储蓝牙振动的图形数据
     *
     * @param ResultID 结果id
     * @param message  每条数据值
     */
    public void addChart(String ResultID, float message) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("WaveData", message);
        values.put("Result_ID", ResultID);
        // 参数一：表名，参数三，是插入的内容
        // 参数二：只要能保存 values中是有内容的，第二个参数可以忽略
        db.insert(mChart, null, values);
    }

    /**
     * 根据ID拿取数据
     *
     * @param ResultID 结果的唯一ID
     * @return
     */
    public List<Float> getChart(String ResultID) {
        List<Float> list = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from chart where  Result_ID=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{ResultID});
        while (cursor.moveToNext()) {
            float waveDta = cursor.getFloat(cursor.getColumnIndex("WaveData"));
            list.add(waveDta);
        }
        //关闭cursor
        cursor.close();
        return list;
    }

    /**
     * 储存设备
     *
     * @param devices
     * @param account
     * @param
     */
    public void addDevices(Devices devices, String account) {
        //获得一个可写的数据库的一个引用
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("accountId", account);
        values.put("DeviceCode", devices.getDeviceCode());
        values.put("DeviceName", devices.getDeviceName());
        values.put("DeviceLocation", devices.getDeviceLocation());
        values.put("UpdateTime", devices.getUpdateTime());
        // 参数一：表名，参数三，是插入的内容
        // 参数二：只要能保存 values中是有内容的，第二个参数可以忽略
        db.insert(mDeviceInfo, null, values);
    }

    /**
     * 储存标牌到位信息
     *
     * @param label 标牌类
     */
    public int addLabel(Label label) {
        //获得一个可写的数据库的一个引用
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LabelID", label.getLabelID());
        values.put("LabelCode", label.getLabelCode());
        values.put("TaskType", label.getTaskType());
        values.put("LineId", label.getLineId());
        values.put("Start_TM", label.getStart_TM());
        values.put("End_TM", label.getEnd_TM());
        values.put("User_ID", label.getUser_ID());
        values.put("UserName", label.getUserName());
        values.put("UserCode", label.getUserCode());
        values.put("UserPostId", label.getUserPostId());
        values.put("ShiftName", label.getShiftName());
        values.put("GroupName", label.getGroupName());
        values.put("TimeCount", label.getTimeCount());
        values.put("TaskID", label.getTaskID());
        // 参数一：表名，参数三，是插入的内容
        // 参数二：只要能保存 values中是有内容的，第二个参数可以忽略
        db.insert(mLabel, null, values);
        Cursor cursor = db.rawQuery("select last_insert_rowid() from label", null);
        int strid = 0;
        if (cursor.moveToFirst()) {
            strid = cursor.getInt(0);
        }
        cursor.close();
        return strid;
    }

    //修改标牌的起始时间
    public void setLabelTM(String End_TM) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("End_TM", End_TM);
        db.update("label", values, "TaskType=?", new String[]{"9"});
    }

    //修改标牌结束时间
    public void setLabel(String Id, String End_TM, int TimeCount) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("End_TM", End_TM);
        values.put("TimeCount", TimeCount);
        db.update("label", values, "_id = ?", new String[]{Id});
    }

    /**
     * 根据任务ID得到需要上传的标牌
     *
     * @param
     * @return
     */
    public List<Label> getLabel(String TaskID) {
        List<Label> labels = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from label where TaskType !=? and TaskID = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{"9", TaskID});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String LabelID = cursor.getString(cursor.getColumnIndex("LabelID"));
            String LabelCode = cursor.getString(cursor.getColumnIndex("LabelCode"));
            String TaskType = cursor.getString(cursor.getColumnIndex("TaskType"));
            String LineId = cursor.getString(cursor.getColumnIndex("LineId"));
            String Start_TM = cursor.getString(cursor.getColumnIndex("Start_TM"));
            String End_TM = cursor.getString(cursor.getColumnIndex("End_TM"));
            String User_ID = cursor.getString(cursor.getColumnIndex("User_ID"));
            String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
            String UserCode = cursor.getString(cursor.getColumnIndex("UserCode"));
            String UserPostId = cursor.getString(cursor.getColumnIndex("UserPostId"));
            String ShiftName = cursor.getString(cursor.getColumnIndex("ShiftName"));
            String GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
            int TimeCount = cursor.getInt(cursor.getColumnIndex("TimeCount"));
            String TaskId = cursor.getString(cursor.getColumnIndex("TaskID"));
            Label label = new Label(LabelID, LabelCode, TaskType, LineId, Start_TM, End_TM, User_ID, UserName, UserCode, UserPostId, ShiftName, GroupName, TimeCount, TaskId, id);
            //封装的对象添加到集合中
            labels.add(label);
        }
        //关闭cursor
        cursor.close();
        return labels;
    }

    public Label getSnapLabel() {
        Label label = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from label where TaskType =?";
        Cursor cursor = db.rawQuery(sql, new String[]{"9"});
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String LabelID = cursor.getString(cursor.getColumnIndex("LabelID"));
            String LabelCode = cursor.getString(cursor.getColumnIndex("LabelCode"));
            String TaskType = cursor.getString(cursor.getColumnIndex("TaskType"));
            String LineId = cursor.getString(cursor.getColumnIndex("LineId"));
            String Start_TM = cursor.getString(cursor.getColumnIndex("Start_TM"));
            String End_TM = cursor.getString(cursor.getColumnIndex("End_TM"));
            String User_ID = cursor.getString(cursor.getColumnIndex("User_ID"));
            String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
            String UserCode = cursor.getString(cursor.getColumnIndex("UserCode"));
            String UserPostId = cursor.getString(cursor.getColumnIndex("UserPostId"));
            String ShiftName = cursor.getString(cursor.getColumnIndex("ShiftName"));
            String GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
            int TimeCount = cursor.getInt(cursor.getColumnIndex("TimeCount"));
            String TaskId = cursor.getString(cursor.getColumnIndex("TaskID"));
            label = new Label(LabelID, LabelCode, TaskType, LineId, Start_TM, End_TM, User_ID, UserName, UserCode, UserPostId, ShiftName, GroupName, TimeCount, TaskId, id);

        }
        //关闭cursor
        cursor.close();
        return label;
    }

    /**
     * 删除已上传的标牌
     *
     * @param LabelID 标牌ID
     */
    public void delLabel(String LabelID, String TaskID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("label", "_id =? and TaskID=?", new String[]{LabelID, TaskID});
    }

    /**
     * 根据用户得到设备
     *
     * @return
     */
    public List<Devices> getDevices(String badgeid) {
        List<Devices> allBlackNum = new ArrayList<Devices>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from deviceInfo where  accountId=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{badgeid});
        while (cursor.moveToNext()) {
            String DeviceCode = cursor.getString(cursor.getColumnIndex("DeviceCode"));
            String DeviceName = cursor.getString(cursor.getColumnIndex("DeviceName"));
            String DeviceLocation = cursor.getString(cursor.getColumnIndex("DeviceLocation"));
            String UpdateTime = cursor.getString(cursor.getColumnIndex("UpdateTime"));
            Devices devices = new Devices(DeviceCode, DeviceName, DeviceLocation, UpdateTime);
            //封装的对象添加到集合中
            allBlackNum.add(devices);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 根据用户得到设备
     *
     * @return
     */
    public List<Devices> getLikeDevices(String badgeid, String contrast) {
        List<Devices> allBlackNum = new ArrayList<Devices>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from deviceInfo where DeviceName like '%" + contrast + "%' or DeviceCode like '%" + contrast + "%' or DeviceLocation like '%" + contrast + "%' and accountId=?";
        Cursor cursor = db.rawQuery(sql, new String[]{badgeid});
        while (cursor.moveToNext()) {
            String DeviceCode = cursor.getString(cursor.getColumnIndex("DeviceCode"));
            String DeviceName = cursor.getString(cursor.getColumnIndex("DeviceName"));
            String DeviceLocation = cursor.getString(cursor.getColumnIndex("DeviceLocation"));
            String UpdateTime = cursor.getString(cursor.getColumnIndex("UpdateTime"));
            Devices devices = new Devices(DeviceCode, DeviceName, DeviceLocation, UpdateTime);
            //封装的对象添加到集合中
            allBlackNum.add(devices);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 根据DeviceCode得到设备
     *
     * @return
     */
    public Devices getDeviceName(String mDeviceCode) {
        Devices devices = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from deviceInfo where  DeviceCode=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{mDeviceCode});
        while (cursor.moveToNext()) {
            String DeviceCode = cursor.getString(cursor.getColumnIndex("DeviceCode"));
            String DeviceName = cursor.getString(cursor.getColumnIndex("DeviceName"));
            String DeviceLocation = cursor.getString(cursor.getColumnIndex("DeviceLocation"));
            String UpdateTime = cursor.getString(cursor.getColumnIndex("UpdateTime"));
            devices = new Devices(DeviceCode, DeviceName, DeviceLocation, UpdateTime);

        }
        //关闭cursor
        cursor.close();
        return devices;
    }

    /**
     * 存储文件信息
     *
     * @param resultFileInfo 文件封装类
     */
    public int addResultFile(ResultFileInfo resultFileInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FileType", resultFileInfo.getFileType());
        values.put("FileName", resultFileInfo.getFileName());
        values.put("FileDataForBase64String", resultFileInfo.getFileDataForBase64String());
        values.put("CheckItem_ID", resultFileInfo.getCheckItem_ID());
        values.put("Exception_ID", resultFileInfo.getException_ID());
        db.insert(mResultFile, null, values);
        Cursor cursor = db.rawQuery("select last_insert_rowid() from resultFile", null);
        int strid = 0;
        if (cursor.moveToFirst()) {
            strid = cursor.getInt(0);
        }
        cursor.close();
        return strid;
    }

    /**
     * 删除异常对应的文件
     *
     * @param Exception_ID 异常id
     */
    public void delResultFile(String Exception_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("resultFile", "Exception_ID =? ", new String[]{Exception_ID});
    }

    /**
     * 上传异常时删除
     *
     * @param Exception_ID 异常id
     */
    public void delReException(String Exception_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("exception", "_id=? ", new String[]{Exception_ID});
    }

    /**
     * 检查结果拿文件
     *
     * @param CheckItemID 检查结果项ID
     * @return
     */
    public List<ResultFileInfo> getCheckFile(String CheckItemID) {
        List<ResultFileInfo> allBlackNum = new ArrayList<ResultFileInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from resultFile where CheckItem_ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{CheckItemID});
        while (cursor.moveToNext()) {
            ResultFileInfo file = getItemFile(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(file);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 异常拿文件
     *
     * @param ExceptionID 异常id
     * @return
     */
    public List<ResultFileInfo> getAbnorFile(String ExceptionID) {
        List<ResultFileInfo> allBlackNum = new ArrayList<ResultFileInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from resultFile where Exception_ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{ExceptionID});
        while (cursor.moveToNext()) {
            ResultFileInfo file = getItemFile(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(file);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 异常存储本地
     *
     * @param reException 异常类
     */
    public int addReException(ReException reException) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ExceptionTitle", reException.getExceptionTitle());
        values.put("Task_ID", reException.getTask_ID());
        values.put("LabelID", reException.getLabel_ID());
        values.put("MobjectName", reException.getMobjectName());
        values.put("MobjectCode", reException.getMobjectCode());
        values.put("CheckItem_ID", reException.getCheckItem_ID());
        values.put("UserID", reException.getUserID());
        values.put("UserName", reException.getUserName());
        values.put("ResultValue", reException.getResultValue());
        values.put("Rate", reException.getRate());
        values.put("WaveData", reException.getWaveData());
        values.put("Memo", reException.getMemo());
        values.put("GroupName", reException.getGroupName());
        values.put("PDADevice", reException.getPDADevice());
        values.put("status", reException.getStatus());
        values.put("Found_TM", reException.getFound_TM());
        values.put("Duration_TM", reException.getDuration_TM());
        values.put("updatetime", reException.getUpdatetime());
        values.put("submit", false);
        db.insert(mException, null, values);
        Cursor cursor = db.rawQuery("select last_insert_rowid() from exception", null);
        int strid = 0;
        if (cursor.moveToFirst()) {
            strid = cursor.getInt(0);
        }
        cursor.close();
        return strid;
    }

    public List<ReException> getReException(String UserID) {
        List<ReException> allBlackNum = new ArrayList<ReException>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from exception where UserID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{UserID});
        while (cursor.moveToNext()) {
            ReException itemRe = getItemRe(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(itemRe);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 储存检查结果
     *
     * @param checkItem 结果类
     */
    public int addCheckItem(CheckItem checkItem) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor query = db.query(mCheckItem, new String[]{"TaskItemID"}, "TaskItemID = ?", new String[]{checkItem.getTaskItemID()}, null, null, null);
        int strid = 0;
        if (query.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("Task_ID", checkItem.getTask_ID());
            values.put("LabelID", checkItem.getLabelID());
            values.put("MobjectCode", checkItem.getMobjectCode());
            values.put("MobjectName", checkItem.getMobjectName());
            values.put("CheckItem_ID", checkItem.getCheckItem_ID());
            values.put("Start_TM", checkItem.getStart_TM());
            values.put("Complete_TM", checkItem.getComplete_TM());
            values.put("UserID", checkItem.getUserID());
            values.put("UserName", checkItem.getUserName());
            values.put("ResultValue", checkItem.getResultValue());
            values.put("Rate", checkItem.getRate());
            values.put("MObjectStatus", checkItem.getMObjectStatus());
            values.put("Exception_YN", checkItem.getException_YN());
            values.put("ExceptionID", checkItem.getExceptionID());
            values.put("Memo_TX", checkItem.getMemo_TX());
            values.put("Time_NR", checkItem.getTime_NR());
            values.put("GroupName", checkItem.getGroupName());
            values.put("PDADevice", checkItem.getPDADevice());
            values.put("ExceptionTransfer_YN", checkItem.getExceptionTransfer_YN());
            values.put("submit", false);
            values.put("TaskItemID", checkItem.getTaskItemID());
            values.put("TaskType", checkItem.getTaskType());
            values.put("LineId", checkItem.getLineId());
            values.put("UserCode", checkItem.getUserCode());
            values.put("UserPostId", checkItem.getUserPostId());
            values.put("ExceptionLevel", checkItem.getExceptionLevel());
            values.put("ShiftName", checkItem.getShiftName());
            values.put("VibFeatures", checkItem.getVibFeatures());
            values.put("Points", checkItem.getPoints());
            values.put("SignalType", checkItem.getSignalType());
            db.insert(mCheckItem, null, values);
            Cursor cursor = db.rawQuery("select last_insert_rowid() from checkItem", null);
            if (cursor.moveToFirst()) {
                strid = cursor.getInt(0);
            }
            cursor.close();
        }
        query.close();
        return strid;
    }

    /**
     * 得到检查结果
     *
     * @param Task_ID 任务id
     * @return
     */
    public List<CheckItem> getCheckItem(String Task_ID) {
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where Task_ID=? and submit != ? order by _id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{Task_ID, "true"});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(check);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 得到往期检查结果，观察类里面用
     *
     * @param CheckItem_ID 检查项ID
     * @return
     */
    public List<CheckItem> getCheckAccurate(String CheckItem_ID, String StartTime) {
        long time = TimeType.getUpperTime(StartTime);
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where CheckItem_ID =? order by _id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{CheckItem_ID});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            long time1 = TimeType.getTime(check.getComplete_TM());
            if (time > time1) {
                allBlackNum.add(check);
            }
            //封装的对象添加到集合中
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public CheckItem getCheckItemAccurate(String CheckItem_ID, String TaskID) {
        CheckItem check = new CheckItem();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where CheckItem_ID =? and Task_ID =? ";
        Cursor cursor = db.rawQuery(sql, new String[]{CheckItem_ID, TaskID});
        while (cursor.moveToNext()) {
            check = getItemCheck(cursor);
            //封装的对象添加到集合中
        }
        //关闭cursor
        cursor.close();
        return check;
    }


    /**
     * 得到临测结果
     *
     * @param UserID 人员
     * @return
     */
    public List<CheckItem> getTastCheckItem(String UserID) {
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where Task_ID=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{UserID});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(check);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    //删除临时任务结果
    public void delTastCheck(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("checkItem", "Task_ID =? ", new String[]{Result_ID});
    }

    public void delTast(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("taskInfo", "TaskID =? ", new String[]{Result_ID});
    }

    //删除临测结果
    public void delTastCheckItem(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("checkItem", "_id =? ", new String[]{Result_ID});
    }

    //删除临测结果对应的文件
    public void delTastResultFile(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("resultFile", "CheckItem_ID =? ", new String[]{Result_ID});
    }

    //删除临测结果对应的波形数据
    public void delTastChart(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("chart", "Result_ID =? ", new String[]{Result_ID});
    }


    /**
     * 得到区域检查结果
     * 为了得到结束时间
     *
     * @param Task_ID 任务id
     * @return
     */
    public List<CheckItem> getRegioCheckItem(String Task_ID, String LabelID) {
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where Task_ID=? and LabelID=? order by _id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{Task_ID, LabelID});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(check);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 添加每一项任务
     *
     * @param checkItemInfo
     */
    public void addCheckItemInfo(CheckItemInfo checkItemInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues putMobject = getPutMobject(checkItemInfo);
        db.insert(mCheckItemInfo, null, putMobject);
    }

    public void addCheckMobject(CheckItemInfo checkItemInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = getPutMobject(checkItemInfo);
        db.insert(mCheckMobject, null, values);
    }

    /**
     * 储存展示时用的排列顺序
     *
     * @param checkItemInfo
     */
    public void addLineMobject(CheckItemInfo checkItemInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = getPutMobject(checkItemInfo);
        db.insert(mLineMobject, null, values);
    }

    /**
     * 更新排序结果时要把原来的删除
     *
     * @param LineID
     * @param LabelID
     */
    public void delLineMobject(String LineID, String LabelID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete(mLineMobject, "LineID = ? and LabelID = ?", new String[]{LineID, LabelID});
    }

    public List<CheckItemInfo> getLineMobject(String TaskID, String labelID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from lineMobject where  TaskID=? and LabelID=? order by _id ";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, labelID});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public ContentValues getPutMobject(CheckItemInfo checkItemInfo) {
        ContentValues values = new ContentValues();
        values.put("CheckItemID", checkItemInfo.getCheckItemID());
        values.put("MobjectCode", checkItemInfo.getMobjectCode());
        values.put("MobjectName", checkItemInfo.getMobjectName());
        values.put("LabelID", checkItemInfo.getLabelID());
        values.put("LabelName", checkItemInfo.getLabelName());
        values.put("TaskID", checkItemInfo.getTaskID());
        values.put("LineID", checkItemInfo.getLineID());
        values.put("CheckItemDesc", checkItemInfo.getCheckItemDesc());
        values.put("ESTStandard", checkItemInfo.getESTStandard());
        values.put("CheckType", checkItemInfo.getCheckType());
        values.put("ZhenDong_Type", checkItemInfo.getZhenDong_Type());
        values.put("Zhendong_PP", checkItemInfo.getZhendong_PP());
        values.put("AlarmType_ID", checkItemInfo.getAlarmType_ID());
        values.put("StandardValue", checkItemInfo.getStandardValue());
        values.put("ParmLowerLimit", checkItemInfo.getParmLowerLimit());
        values.put("ParmUpperLimit", checkItemInfo.getParmUpperLimit());
        values.put("CheckItemUpdateTime", checkItemInfo.getCheckItemUpdateTime());
        values.put("UpperLimit1", checkItemInfo.getUpperLimit1());
        values.put("UpperLimit2", checkItemInfo.getUpperLimit2());
        values.put("UpperLimit3", checkItemInfo.getUpperLimit3());
        values.put("UpperLimit4", checkItemInfo.getUpperLimit4());
        values.put("LowerLimit1", checkItemInfo.getLowerLimit1());
        values.put("LowerLimit2", checkItemInfo.getLowerLimit2());
        values.put("LowerLimit3", checkItemInfo.getLowerLimit3());
        values.put("LowerLimit4", checkItemInfo.getLowerLimit4());
        values.put("CheckOrderNo", checkItemInfo.getCheckOrderNo());
        values.put("submit", false);
        values.put("TaskItemID", checkItemInfo.getTaskItemID());
        values.put("LabelCode", checkItemInfo.getLabelCode());
        values.put("ObserveOptions", checkItemInfo.getObserveOptions());
        values.put("ShiftName", checkItemInfo.getShiftName());
        values.put("GroupName", checkItemInfo.getGroupName());
        values.put("TaskPlanStartTime", checkItemInfo.getTaskPlanStartTime());
        values.put("TaskPlanEndTime", checkItemInfo.getTaskPlanEndTime());
        values.put("NextTaskDate", checkItemInfo.getNextTaskDate());
        values.put("NextTaskDateTimePeriods", checkItemInfo.getNextTaskDateTimePeriods());
        values.put("ZhenDong_Freq", checkItemInfo.getZhenDong_Freq());
        values.put("ZhenDong_Points", checkItemInfo.getZhenDong_Points());
        values.put("EquipmentStatusFilter", checkItemInfo.getEquipmentStatusFilter());
        values.put("SyncSamplingTag", checkItemInfo.getSyncSamplingTag());
        values.put("state", checkItemInfo.getState());
        return values;
    }

    public void cleMobject() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from checkMobject;" +
                "update sqlite_sequence SET seq = 0 where name = 'checkMobject';");
    }

    public void delMobject(String MobjectCode) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("checkMobject", "MobjectCode =? ", new String[]{MobjectCode});
    }

    public List<CheckItemInfo> getCheckMobject() {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkMobject ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }
    /**
     * 获得存储的数量
     */
//    public int getBlackNumCount() {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Cursor cursor = db.query(mTaskInfo, new String[]{"count(*)"}, null, null, null, null, null);
//        cursor.moveToNext();
//        int count = cursor.getInt(0);// 仅查了一列，count(*) 这一刻列
//        cursor.close();
//        return count;
//    }

    /**
     * 临时任务获得任务总数
     *
     * @param TaskID
     * @return
     */
    public int getTaskNumCount(String TaskID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getLineNumCount(String TaskId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskId});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getLabelIDNumCount(String TaskID, String LabelID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID=? and LabelID=? GROUP BY TaskItemID";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, LabelID});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public List<CheckItemInfo> getLabelTime(String TaskID, String LabelID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where TaskID=? and LabelID=? GROUP BY TaskPlanStartTime";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, LabelID});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    //设备下任务总数
    public int getMobjectCodeNumCount(String TaskID, String LabelID, String MobjectCode) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID=? and LabelID=? and MobjectCode=?";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, LabelID, MobjectCode});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getTaskCheckCount(String LineID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where  Task_ID=? GROUP BY CheckItem_ID";
        Cursor cursor = db.rawQuery(sql, new String[]{LineID});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getLabelCheckCount(String LineID, String LabelID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where  Task_ID=? and LabelID=? GROUP BY TaskItemID";
        Cursor cursor = db.rawQuery(sql, new String[]{LineID, LabelID});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //设备下的完成数量
    public int getMobjectCodeCheckCount(String LineID, String LabelID, String MobjectCode) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where  Task_ID=? and LabelID=? and MobjectCode=? GROUP BY TaskItemID";
        Cursor cursor = db.rawQuery(sql, new String[]{LineID, LabelID, MobjectCode});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * 改变任务是否上交状态
     *
     * @param TaskID 任务ID
     */
    public void setTaskSubmit(String TaskID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("submit", "true");
        db.update("taskInfo", values, "TaskID=?", new String[]{TaskID});
    }

    public void setCheckSubmit(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("submit", "true");
        db.update("checkItem", values, "_id=?", new String[]{Result_ID});
    }

    public void setCheckState(int state, String TaskId, String MobjectCode) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("state", state);
        db.update("checkItemInfo", values, "TaskID=? and MobjectCode=?", new String[]{TaskId, MobjectCode});
    }

    public void setLineState(int state, String TaskId, String MobjectCode) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("state", state);
        db.update("lineMobject", values, "TaskID=? and MobjectCode=?", new String[]{TaskId, MobjectCode});
    }

    /**
     * 分页查询任务
     *
     * @return
     */
    //
    public List<TaskInfo> getTaskInfo(int pageIndex, int pageSize, String Tasktype, String accountid) {
        long Date = TimeType.getNowTime();
        //创建集合对象
        List<TaskInfo> allBlackNum = new ArrayList<TaskInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String placeHolder = makePlaceholders(Lineid.size());
        //order by _id desc 根据_id倒叙排列   使每次添加的黑名单在下次打开时显示上面     同时每页限制20个
        Cursor cursor = db.rawQuery("select * from taskInfo where TaskType=? and accountid =? and TaskPlanEndTime>" + Date + " order by TaskPlanEndTime  limit " + pageSize + " offset " + (pageIndex * pageSize) + ";", new String[]{Tasktype, accountid});
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            TaskInfo task = getItemTask(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(task);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<TaskInfo> getSpotTaskInfo(int pageIndex, int pageSize, String accountid) {
        long Date = TimeType.getNowTime();
        //创建集合对象
        List<TaskInfo> allBlackNum = new ArrayList<TaskInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String placeHolder = makePlaceholders(Lineid.size());
        //order by _id desc 根据_id倒叙排列   使每次添加的黑名单在下次打开时显示上面     同时每页限制20个
        Cursor cursor = db.rawQuery("select * from taskInfo where TaskType=? and accountid = ? and TaskPlanEndTime>" + Date + " order by TaskPlanEndTime  limit " + pageSize + " offset " + (pageIndex * pageSize) + ";", new String[]{"1", accountid});
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            TaskInfo task = getItemTask(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(task);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<TaskInfo> getTask(String TasktID) {
        long Date = TimeType.getNowTime();
        //创建集合对象
        List<TaskInfo> allBlackNum = new ArrayList<TaskInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String placeHolder = makePlaceholders(Lineid.size());
        //order by _id desc 根据_id倒叙排列   使每次添加的黑名单在下次打开时显示上面     同时每页限制20个
        String sql = "select * from taskInfo where TaskID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{TasktID});
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            TaskInfo task = getItemTask(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(task);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 得到临时任务
     *
     * @return
     */
    //
    public List<TaskInfo> getSnapTask(int pageIndex, int pageSize, String Tasktype) {
        //创建集合对象
        List<TaskInfo> allBlackNum = new ArrayList<TaskInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //order by _id desc 根据_id倒叙排列   使每次添加的黑名单在下次打开时显示上面     同时每页限制20个
        Cursor cursor = db.rawQuery("select * from taskInfo where TaskType=?  order by TaskPlanEndTime desc limit " + pageSize + " offset " + (pageIndex * pageSize) + ";", new String[]{Tasktype});
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            TaskInfo task = getItemTask(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(task);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 得到过期任务
     *
     * @param Tasktype 任务类型
     * @param Lineid   线路id
     * @return
     */
    public List<TaskInfo> getOverTask(int pageIndex, int pageSize, String Tasktype, String Lineid) {
        long Date = TimeType.getNowTime();
        List<TaskInfo> allBlackNum = new ArrayList<TaskInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //order by _id desc 根据_id倒叙排列   使每次添加的黑名单在下次打开时显示上面     同时每页限制20个
        Cursor cursor = db.rawQuery("select * from taskInfo where accountid =? and TaskPlanEndTime<" + Date + " order by TaskPlanEndTime desc limit " + pageSize + " offset " + (pageIndex * pageSize) + ";", new String[]{Lineid});
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            TaskInfo task = getItemTask(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(task);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 根据任务id得到每一项
     *
     * @param taskId
     * @return
     */
    public List<CheckItemInfo> getCheckItemInfo(String taskId) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID=? GROUP BY LabelID";
        Cursor cursor = db.rawQuery(sql, new String[]{taskId});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 获取自主点检
     *
     * @param
     * @return
     */
    public List<CheckItemInfo> getSnapItemInfo() {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskPlanStartTime=? and TaskPlanEndTime =?  GROUP BY MobjectCode";
        Cursor cursor = db.rawQuery(sql, new String[]{"", ""});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public void delSnapItem() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("checkItemInfo", "TaskPlanStartTime=? and TaskPlanEndTime =?", new String[]{"", ""});
    }

    public List<CheckItemInfo> getSnapMobjectItem(String taskId, String labelID, String mobjectCode) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql;
        Cursor cursor;
        sql = "select * from checkItemInfo where TaskID=? and LabelID=? and MobjectCode=? and TaskPlanStartTime=? and TaskPlanEndTime =?";
        cursor = db.rawQuery(sql, new String[]{taskId, labelID, mobjectCode, "", ""});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    //自主巡检改变任务完成状态
    public void setCheckAutonomy(String Task_ID, String CheckItem_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("ShiftName", "-1");
        db.update("checkItemInfo", values, "TaskID = ? and CheckItemID = ?", new String[]{Task_ID, CheckItem_ID});
    }

    //自主巡检设备下的完成数量
    public int getSnapMobject(String TaskID, String LabelID, String MobjectCode) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID=? and LabelID=? and MobjectCode=? and ShiftName = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, LabelID, MobjectCode, "-1"});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * @param checkItem
     */
    public void setChangeSubmit(CheckItem checkItem, String id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("submit", "false");
        values.put("ResultValue", checkItem.getResultValue());
        values.put("ExceptionLevel", checkItem.getExceptionLevel());
        values.put("Memo_TX", checkItem.getMemo_TX());
        values.put("VibFeatures", checkItem.getVibFeatures());
        values.put("Exception_YN", checkItem.getException_YN());
        db.update("checkItem", values, "_id = ?", new String[]{id});
    }

    /**
     * 临时任务拿检查项为了拿到设备
     *
     * @param taskId
     * @return
     */
    public List<CheckItemInfo> getSnapCheckItem(String taskId) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo  where CheckItemID  not in (select CheckItem_ID from checkItem where Task_ID=?) and TaskID=? GROUP BY MobjectCode";
        //String sql = "select * from checkItemInfo where  TaskID=? GROUP BY MobjectCode";
        Cursor cursor = db.rawQuery(sql, new String[]{taskId, taskId});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 临时任务拿检查项
     * not in 避免重复提交
     *
     * @param taskId
     * @return
     */
    public List<CheckItemInfo> getSnapCheckItemInfo(String taskId) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where TaskID=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{taskId});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<CheckItemInfo> getMobject(String taskId, String labelID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where  TaskID=? and LabelID=? GROUP BY MobjectCode";
        Cursor cursor = db.rawQuery(sql, new String[]{taskId, labelID});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<CheckItemInfo> getCheckItem(boolean isNotin, String taskId, String LineID, String labelID, String mobjectCode) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql;
        Cursor cursor;
        if (isNotin) {
            sql = "select * from checkItemInfo where TaskItemID not in (select TaskItemID from checkItem where Task_ID=? ) and TaskID=? and LabelID=? and MobjectCode=? GROUP BY TaskItemID";
            cursor = db.rawQuery(sql, new String[]{taskId, taskId, labelID, mobjectCode});
        } else {
            sql = "select * from checkItemInfo where TaskID=? and LabelID=? and MobjectCode=? ";
            cursor = db.rawQuery(sql, new String[]{taskId, labelID, mobjectCode});
        }
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<CheckItem> getCheckAbnorItem(String Task_ID, String CheckItem_ID) {
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where Task_ID=? and CheckItem_ID = ? order by _id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{Task_ID, CheckItem_ID});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(check);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    //观察项改变保存模式，方便批量提交
    public void setCheck(String Task_ID, String CheckItem_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("ExceptionID", "-10");
        db.update("checkItem", values, "Task_ID = ? and CheckItem_ID = ?", new String[]{Task_ID, CheckItem_ID});
    }

    //重新编辑异常，删除原来的异常
    public void delCheckAbnorItem(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("checkItem", "_id =? ", new String[]{Result_ID});
    }

    //删除异常所对应文件
    public void delCheckItemFile(String Result_ID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("resultFile", "CheckItem_ID =? ", new String[]{Result_ID});
    }

    //观察项得到保存过得异常，方便批量提交
    public List<CheckItemInfo> getCheckItemOver(String taskId, String LineID, String labelID, String mobjectCode) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql;
        Cursor cursor;
        sql = "select * from checkItemInfo where CheckItemID in (select CheckItem_ID from checkItem where Task_ID=? and ExceptionID = ?) and LineID=? and LabelID=? and MobjectCode=? and CheckType = ?";
        cursor = db.rawQuery(sql, new String[]{taskId, "", LineID, labelID, mobjectCode, "GC"});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    //得到待办任务没做的设备
    public List<CheckItemInfo> getMobject(String taskId, String LineID, String labelID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo where CheckItemID not in (select CheckItem_ID from checkItem where Task_ID=? ) and LineID=? and LabelID=? GROUP BY MobjectCode";
        Cursor cursor = db.rawQuery(sql, new String[]{taskId, LineID, labelID});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    /**
     * 获取异常项
     *
     * @param TaskID 任务ID
     * @return
     */
    public List<CheckItemInfo> getReEx(String TaskID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo  where TaskItemID  in (select TaskItemID from checkItem where Task_ID=? and Exception_YN=?) ";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, "1"});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public int getOverUpload(String TaskID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where Task_ID=? and submit = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, "0"});
        int count = cursor.getCount();
        //关闭cursor
        cursor.close();
        return count;
    }

    /**
     * 获取多项任务的异常项
     *
     * @param TaskID 任务ID
     * @return
     */
    public List<CheckItemInfo> getAllEx(String TaskID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo  where CheckItemID  in (select CheckItem_ID from checkItem where Task_ID in " + TaskID + " and Exception_YN=?) ";
        Cursor cursor = db.rawQuery(sql, new String[]{"1"});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<CheckItemInfo> getUndetected(String TaskID, String LineID) {
        List<CheckItemInfo> allBlackNum = new ArrayList<CheckItemInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItemInfo  where TaskItemID  not in (select TaskItemID from checkItem where Task_ID=? ) and TaskID=? GROUP BY TaskItemID";
        Cursor cursor = db.rawQuery(sql, new String[]{TaskID, TaskID});
        while (cursor.moveToNext()) {
            CheckItemInfo item = getItem(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(item);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public ResultFileInfo getItemFile(Cursor cursor) {
        String FileType = cursor.getString(cursor.getColumnIndex("FileType"));
        String FileName = cursor.getString(cursor.getColumnIndex("FileName"));
        String FileDataForBase64String = cursor.getString(cursor.getColumnIndex("FileDataForBase64String"));
        String CheckItem_ID = cursor.getString(cursor.getColumnIndex("CheckItem_ID"));
        String Exception_ID = cursor.getString(cursor.getColumnIndex("Exception_ID"));
        ResultFileInfo resultFileInfo = new ResultFileInfo(FileType, FileName, FileDataForBase64String, CheckItem_ID, Exception_ID);
        return resultFileInfo;
    }

    public CheckItem getItemCheck(Cursor cursor) {
        String Result_ID = cursor.getString(cursor.getColumnIndex("_id"));
        String Task_ID = cursor.getString(cursor.getColumnIndex("Task_ID"));
        String LabelID = cursor.getString(cursor.getColumnIndex("LabelID"));
        String MobjectCode = cursor.getString(cursor.getColumnIndex("MobjectCode"));
        String MobjectName = cursor.getString(cursor.getColumnIndex("MobjectName"));
        String CheckItemID = cursor.getString(cursor.getColumnIndex("CheckItem_ID"));
        String Start_TM = cursor.getString(cursor.getColumnIndex("Start_TM"));
        String Complete_TM = cursor.getString(cursor.getColumnIndex("Complete_TM"));
        String UserID = cursor.getString(cursor.getColumnIndex("UserID"));
        String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
        String ResultValue = cursor.getString(cursor.getColumnIndex("ResultValue"));
        Integer Rate = cursor.getInt(cursor.getColumnIndex("Rate"));
        String MObjectStatus = cursor.getString(cursor.getColumnIndex("MObjectStatus"));
        String Exception_YN = cursor.getString(cursor.getColumnIndex("Exception_YN"));
        String ExceptionID = cursor.getString(cursor.getColumnIndex("ExceptionID"));
        String Memo_TX = cursor.getString(cursor.getColumnIndex("Memo_TX"));
        Integer Time_NR = cursor.getInt(cursor.getColumnIndex("Time_NR"));
        String GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
        String PDADevice = cursor.getString(cursor.getColumnIndex("PDADevice"));
        String ExceptionTransfer_YN = cursor.getString(cursor.getColumnIndex("ExceptionTransfer_YN"));
        boolean submit = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("submit")));
        String TaskItemID = cursor.getString(cursor.getColumnIndex("TaskItemID"));
        String TaskType = cursor.getString(cursor.getColumnIndex("TaskType"));
        String LineId = cursor.getString(cursor.getColumnIndex("LineId"));
        String UserCode = cursor.getString(cursor.getColumnIndex("UserCode"));
        String UserPostId = cursor.getString(cursor.getColumnIndex("UserPostId"));
        String ExceptionLevel = cursor.getString(cursor.getColumnIndex("ExceptionLevel"));
        String ShiftName = cursor.getString(cursor.getColumnIndex("ShiftName"));
        String VibFeatures = cursor.getString(cursor.getColumnIndex("VibFeatures"));
        Integer Points = cursor.getInt(cursor.getColumnIndex("Points"));
        String SignalType = cursor.getString(cursor.getColumnIndex("SignalType"));
        CheckItem checkItem = new CheckItem(Result_ID, Task_ID, LabelID, MobjectCode, MobjectName, CheckItemID, Start_TM, Complete_TM, UserID, UserName, ResultValue
                , Rate, MObjectStatus, Exception_YN, ExceptionID, Memo_TX, Time_NR, GroupName, PDADevice, ExceptionTransfer_YN, submit, TaskItemID, TaskType, LineId, UserCode, UserPostId, ExceptionLevel, ShiftName, VibFeatures, Points, SignalType);
        return checkItem;
    }

    public CheckItemInfo getItem(Cursor cursor) {
        String CheckItemID = cursor.getString(cursor.getColumnIndex("CheckItemID"));
        String MobjectCode = cursor.getString(cursor.getColumnIndex("MobjectCode"));
        String MobjectName = cursor.getString(cursor.getColumnIndex("MobjectName"));
        String LabelID = cursor.getString(cursor.getColumnIndex("LabelID"));
        String LabelName = cursor.getString(cursor.getColumnIndex("LabelName"));
        String TaskID = cursor.getString(cursor.getColumnIndex("TaskID"));
        String LineID = cursor.getString(cursor.getColumnIndex("LineID"));
        String CheckItemDesc = cursor.getString(cursor.getColumnIndex("CheckItemDesc"));
        String ESTStandard = cursor.getString(cursor.getColumnIndex("ESTStandard"));
        String CheckType = cursor.getString(cursor.getColumnIndex("CheckType"));
        String ZhenDong_Type = cursor.getString(cursor.getColumnIndex("ZhenDong_Type"));
        String Zhendong_PP = cursor.getString(cursor.getColumnIndex("Zhendong_PP"));
        String AlarmType_ID = cursor.getString(cursor.getColumnIndex("AlarmType_ID"));
        String StandardValue = cursor.getString(cursor.getColumnIndex("StandardValue"));
        String ParmLowerLimit = cursor.getString(cursor.getColumnIndex("ParmLowerLimit"));
        String ParmUpperLimit = cursor.getString(cursor.getColumnIndex("ParmUpperLimit"));
        String CheckItemUpdateTime = cursor.getString(cursor.getColumnIndex("CheckItemUpdateTime"));
        float UpperLimit1 = cursor.getFloat(cursor.getColumnIndex("UpperLimit1"));
        float UpperLimit2 = cursor.getFloat(cursor.getColumnIndex("UpperLimit2"));
        float UpperLimit3 = cursor.getFloat(cursor.getColumnIndex("UpperLimit3"));
        float UpperLimit4 = cursor.getFloat(cursor.getColumnIndex("UpperLimit4"));
        float LowerLimit1 = cursor.getFloat(cursor.getColumnIndex("LowerLimit1"));
        float LowerLimit2 = cursor.getFloat(cursor.getColumnIndex("LowerLimit2"));
        float LowerLimit3 = cursor.getFloat(cursor.getColumnIndex("LowerLimit3"));
        float LowerLimit4 = cursor.getFloat(cursor.getColumnIndex("LowerLimit4"));
        int CheckOrderNo = cursor.getInt(cursor.getColumnIndex("CheckOrderNo"));
        boolean submit = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("submit")));
        String TaskItemID = cursor.getString(cursor.getColumnIndex("TaskItemID"));
        String LabelCode = cursor.getString(cursor.getColumnIndex("LabelCode"));
        String ObserveOptions = cursor.getString(cursor.getColumnIndex("ObserveOptions"));
        String ShiftName = cursor.getString(cursor.getColumnIndex("ShiftName"));
        String GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
        String TaskPlanStartTime = cursor.getString(cursor.getColumnIndex("TaskPlanStartTime"));
        String TaskPlanEndTime = cursor.getString(cursor.getColumnIndex("TaskPlanEndTime"));
        String NextTaskDate = cursor.getString(cursor.getColumnIndex("NextTaskDate"));
        String NextTaskDateTimePeriods = cursor.getString(cursor.getColumnIndex("NextTaskDateTimePeriods"));
        String ZhenDong_Freq = cursor.getString(cursor.getColumnIndex("ZhenDong_Freq"));
        String ZhenDong_Points = cursor.getString(cursor.getColumnIndex("ZhenDong_Points"));
        String EquipmentStatusFilter = cursor.getString(cursor.getColumnIndex("EquipmentStatusFilter"));
        String SyncSamplingTag = cursor.getString(cursor.getColumnIndex("SyncSamplingTag"));
        int state = cursor.getInt(cursor.getColumnIndex("state"));
        CheckItemInfo bean = new CheckItemInfo(CheckItemID, MobjectCode, MobjectName, LabelID, LabelName, TaskID, LineID, CheckItemDesc, ESTStandard, CheckType, ZhenDong_Type, Zhendong_PP, AlarmType_ID, StandardValue, ParmLowerLimit, ParmUpperLimit, CheckItemUpdateTime, UpperLimit1, UpperLimit2, UpperLimit3, UpperLimit4, LowerLimit1, LowerLimit2, LowerLimit3, LowerLimit4, CheckOrderNo, submit
                , TaskItemID, LabelCode, ObserveOptions, ShiftName, GroupName, TaskPlanStartTime, TaskPlanEndTime, NextTaskDate, NextTaskDateTimePeriods, ZhenDong_Freq, ZhenDong_Points, EquipmentStatusFilter, SyncSamplingTag, state);
        //封装的对象添加到集合中
        return bean;
    }

    public ReException getItemRe(Cursor cursor) {
        String Exception_ID = cursor.getString(cursor.getColumnIndex("_id"));
        String ExceptionTitle = cursor.getString(cursor.getColumnIndex("ExceptionTitle"));
        String Task_ID = cursor.getString(cursor.getColumnIndex("Task_ID"));
        String LabelID = cursor.getString(cursor.getColumnIndex("LabelID"));
        String MobjectName = cursor.getString(cursor.getColumnIndex("MobjectName"));
        String MobjectCode = cursor.getString(cursor.getColumnIndex("MobjectCode"));
        String CheckItem_ID = cursor.getString(cursor.getColumnIndex("CheckItem_ID"));
        String UserID = cursor.getString(cursor.getColumnIndex("UserID"));
        String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
        String ResultValue = cursor.getString(cursor.getColumnIndex("ResultValue"));
        Integer Rate = cursor.getInt(cursor.getColumnIndex("Rate"));
        String WaveData = cursor.getString(cursor.getColumnIndex("WaveData"));
        String Memo = cursor.getString(cursor.getColumnIndex("Memo"));
        String GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
        String PDADevice = cursor.getString(cursor.getColumnIndex("PDADevice"));
        String status = cursor.getString(cursor.getColumnIndex("status"));
        String Found_TM = cursor.getString(cursor.getColumnIndex("Found_TM"));
        Integer Duration_TM = cursor.getInt(cursor.getColumnIndex("Duration_TM"));
        String updatetime = cursor.getString(cursor.getColumnIndex("updatetime"));
        boolean submit = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("submit")));
        ReException exception = new ReException(Exception_ID, ExceptionTitle, Task_ID, LabelID, MobjectName, MobjectCode, CheckItem_ID, UserID, UserName, ResultValue, Rate, WaveData, Memo, GroupName, PDADevice, status, Found_TM, Duration_TM, updatetime, submit);
        return exception;
    }

    public TaskInfo getItemTask(Cursor cursor) {
        String TaskID = cursor.getString(cursor.getColumnIndex(Constants.TASKID));// 获得number 这列的值
        String LineID = cursor.getString(cursor.getColumnIndex(Constants.LINEID));
        String TaskUpdateTime = TimeType.getStrTime(cursor.getLong(cursor.getColumnIndex(Constants.TASKUPDATETIME)));
        String TaskName = cursor.getString(cursor.getColumnIndex(Constants.TASKNAME));
        String LineName = cursor.getString(cursor.getColumnIndex(Constants.LINENAME));
        String TaskType = cursor.getString(cursor.getColumnIndex(Constants.TASKTYPE));
        String LableType = cursor.getString(cursor.getColumnIndex(Constants.LABLETYPE));
        String TaskPlanStartTime = TimeType.getStrTime(cursor.getLong(cursor.getColumnIndex(Constants.TASKPLANSTARTTIME)));
        String TaskPlanEndTime = TimeType.getStrTime(cursor.getLong(cursor.getColumnIndex(Constants.TASKPLANENDTIME)));
        boolean submit = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Constants.SUBMIT)));
        String EquipmentStatusEnabled = cursor.getString(cursor.getColumnIndex(Constants.EQUIPMENTSTATUSENABLED));
        String Sync = cursor.getString(cursor.getColumnIndex("Sync"));
        TaskInfo bean = new TaskInfo(TaskID, LineID, TaskName, LineName, TaskType, LableType, TaskPlanStartTime, TaskPlanEndTime, TaskUpdateTime, submit, EquipmentStatusEnabled, Sync);
        return bean;
    }

    /**
     * 清楚过期24小时的任务
     *
     * @return
     */
    public List<String> delTaskInfo() {
        long Date = TimeType.getNowTime();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //创建checkItem_delete触发器
        db.execSQL("CREATE TRIGGER checkItem_delete " +
                "AFTER DELETE ON [TaskInfo]" +
                "BEGIN " +
                "DELETE from checkItem where Task_ID=old.TaskID;" +
                "END");
        //查询TaskInfo中超过24小时的数据
        db.delete("TaskInfo", "TaskType !=3 and submit == ? and TaskPlanEndTime + 86400 < " + Date, new String[]{"true"});
        //删除checkItem_delete触发器
        db.execSQL("DROP TRIGGER checkItem_delete");
        Log.v("delTaskInfo", "_____DONE_____" + Date);
        return null;
    }

    /**
     * 清除checkItemInfo数据
     */
    public void cleCheckItemInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from checkItemInfo;" +
                "update sqlite_sequence SET seq = 0 where name = 'checkItemInfo';");
    }

    public void cleDevices() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from deviceInfo;" +
                "update sqlite_sequence SET seq = 0 where name = 'deviceInfo';");
    }

    /**
     * 去除重复数据
     */
    public void delRepeatData() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "DELETE FROM taskInfo WHERE taskInfo.rowid NOT IN (SELECT MAX (taskInfo.rowid) FROM taskInfo GROUP BY TaskID)";
        db.execSQL(sql);
    }

    public List<TaskInfo> getAllTask() {
        //创建集合对象
        List<TaskInfo> allBlackNum = new ArrayList<TaskInfo>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //order by _id desc 根据_id倒叙排列   使每次添加的黑名单在下次打开时显示上面     同时每页限制20个
        Cursor cursor = db.rawQuery("select * from taskInfo ", null);
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            TaskInfo task = getItemTask(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(task);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public void setTaskSync(TaskInfo taskInfo) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Sync", "1");
        db.update("taskInfo", values, "TaskID = ?", new String[]{taskInfo.getTaskID()});
    }

    String makePlaceholders(int len) {
        if (len < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    /**
     * 删除重复的任务（待定）
     *
     * @param TaskID 任务id
     */
    public void delCheckItem(String TaskID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete("checkItemInfo", "TaskID = ?", new String[]{TaskID});
    }

    //得到检查项的检查结果已便于修改结果
    public List<CheckItem> getResultCheckItem(String Task_ID, String TaskItemID) {
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where Task_ID = ? and TaskItemID = ? order by _id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{Task_ID, TaskItemID});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(check);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public List<CheckItem> setDelete() {
        List<CheckItem> allBlackNum = new ArrayList<CheckItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from checkItem where submit != ?";
        Cursor cursor = db.rawQuery(sql, new String[]{"true"});
        while (cursor.moveToNext()) {
            CheckItem check = getItemCheck(cursor);
            //封装的对象添加到集合中
            allBlackNum.add(check);
        }
        //关闭cursor
        cursor.close();
        return allBlackNum;
    }

    public void cleTaskInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from taskInfo;" +
                "update sqlite_sequence SET seq = 0 where name = 'taskInfo';");
    }

    public void cleCheckItem() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from checkItem;" +
                "update sqlite_sequence SET seq = 0 where name = 'checkItem';");
    }

    public void cleResultFile() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from resultFile;" +
                "update sqlite_sequence SET seq = 0 where name = 'resultFile';");
    }

    public void cleChart() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from chart;" +
                "update sqlite_sequence SET seq = 0 where name = 'chart';");
    }

    public void cleLabel() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from label;" +
                "update sqlite_sequence SET seq = 0 where name = 'label';");
    }

    public void cleLineMobject() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("delete from lineMobject;" +
                "update sqlite_sequence SET seq = 0 where name = 'lineMobject';");
    }
}
