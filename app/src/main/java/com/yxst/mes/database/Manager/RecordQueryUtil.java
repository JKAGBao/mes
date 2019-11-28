package com.yxst.mes.database.Manager;

import android.content.Context;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.RecordDao;
import com.yxst.mes.database.model.InspectDeviceValue;
import com.yxst.mes.database.model.Record;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/6/27 14:38
 */
public class RecordQueryUtil {

    public static List<Record> getRecordListByItemId(Context context,Long itemId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(context,"User");
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
                RecordDao.Properties.InspectionItemID.eq(itemId)
                ,RecordDao.Properties.LineID.eq(lineId)
                ,RecordDao.Properties.BeginTime.le(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.EndTime.ge(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.UserId.eq(userId)).list();
        return records;
    }
    public static Record getRecordByItemId(Context cxt,Long itemId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
                RecordDao.Properties.InspectionItemID.eq(itemId)
                ,RecordDao.Properties.LineID.eq(lineId)
                ,RecordDao.Properties.BeginTime.le(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.EndTime.ge(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.UserId.eq(userId)).list();
        return records.size()==0?null:records.get(0);
    }
    public static List<Record> getRecordListByDeviceId(Context ctx,Long deviceId){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
                RecordDao.Properties.EquipmentID.eq(deviceId)
                ,RecordDao.Properties.BeginTime.le(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.EndTime.ge(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.UserId.eq(userId)).list();
        return records;
    }
    public static List<Record> getRecordListByDeviceId(Context ctx,Long deviceId,Long lineId,int status){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
                RecordDao.Properties.EquipmentID.eq(deviceId)
                ,RecordDao.Properties.LineID.eq(lineId)
                ,RecordDao.Properties.BeginTime.le(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.EndTime.ge(TimeUtil.dateTimeFormat(new Date()))
                ,RecordDao.Properties.CheckStatus.eq(status)
                ,RecordDao.Properties.UserId.eq(userId)).list();
        return records;
    }
    public static List<Record> getRecordListByStatus(Context ctx,int status){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
               RecordDao.Properties.CheckStatus.eq(status)
                ,RecordDao.Properties.UserId.eq(userId)).list();
        return records;
    }
    public static List<Record> getRecordListByUserId(Context ctx,long userId ){
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
               RecordDao.Properties.UserId.eq(userId)).list();
        return records;
    }
    public static void insertRecord(Record record){
        DatabaseManager.getInstance().getRecordDao().insert(record);
    }
    public static void updateRecord(Record record){
        DatabaseManager.getInstance().getRecordDao().update(record);
    }
    public static void deleteRecord(Record record){
        DatabaseManager.getInstance().getRecordDao().update(record);
    }
    public static void deleteRecordByTime(Long userId,Date date){
        List<Record> records = DatabaseManager.getInstance().getRecordDao().queryBuilder().where(
                RecordDao.Properties.UserId.eq(userId)
                ,RecordDao.Properties.BeginTime.le(date)
                ).list();
        for (Record record:records){
            DatabaseManager.getInstance().getRecordDao().delete(record);
        }
    }
}
