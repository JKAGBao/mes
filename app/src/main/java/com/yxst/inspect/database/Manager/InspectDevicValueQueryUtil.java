package com.yxst.inspect.database.Manager;

import android.content.Context;

import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectDeviceDao;
import com.yxst.inspect.database.dao.InspectDeviceValueDao;
import com.yxst.inspect.database.model.InspectDevice;
import com.yxst.inspect.database.model.InspectDeviceValue;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/6/14 10:47
 */
public class InspectDevicValueQueryUtil {
    public static void saveInspectDeviceValue(Context ctx,InspectDevice device,int uploadStatus){
       InspectDeviceValue value = InspectDevicValueQueryUtil.getByDeviceId(ctx,device.getEquipmentID(),device.getLineID());
       if(value==null){
           InspectDeviceValue dValue = new InspectDeviceValue();
           dValue.setBeginTime(device.getBeginTime());
           dValue.setEndTime(device.getEndTime());
           dValue.setEquipmentID(device.getEquipmentID());
           dValue.setLineID(device.getLineID());
           dValue.setUserID(device.getUserID());
           dValue.setUploadStatus(uploadStatus);
      //     dValue.setInspectStatus(inspectStatus);
           DatabaseManager.getInstance().getInspectDeviceValueDao().insert(dValue);
       }else{
           value.setBeginTime(device.getBeginTime());
           value.setEndTime(device.getEndTime());
           value.setEquipmentID(device.getEquipmentID());
           value.setLineID(device.getLineID());
           value.setUserID(device.getUserID());
           value.setUploadStatus(uploadStatus);
           DatabaseManager.getInstance().getInspectDeviceValueDao().update(value);

       }
    }
    public static void saveShowStatus(Context ctx,InspectDevice device,int showStatus){
        InspectDeviceValue value = InspectDevicValueQueryUtil.getByDeviceId(ctx,device.getEquipmentID(),device.getLineID());
        if(value==null){
            InspectDeviceValue dValue = new InspectDeviceValue();
            dValue.setBeginTime(device.getBeginTime());
            dValue.setEndTime(device.getEndTime());
            dValue.setEquipmentID(device.getEquipmentID());
            dValue.setLineID(device.getLineID());
            dValue.setUserID(device.getUserID());
            dValue.setShowStatus(showStatus);
            //     dValue.setInspectStatus(inspectStatus);
            DatabaseManager.getInstance().getInspectDeviceValueDao().insert(dValue);
        }else{
            value.setBeginTime(device.getBeginTime());
            value.setEndTime(device.getEndTime());
            value.setEquipmentID(device.getEquipmentID());
            value.setLineID(device.getLineID());
            value.setUserID(device.getUserID());
            value.setShowStatus(showStatus);
            DatabaseManager.getInstance().getInspectDeviceValueDao().update(value);

        }
    }
    public static void saveStatus(Context ctx,InspectDevice device,int uploadStatus,int showStatus){
        InspectDeviceValue value = InspectDevicValueQueryUtil.getByDeviceId(ctx,device.getEquipmentID(),device.getLineID());
        if(value==null){
            InspectDeviceValue dValue = new InspectDeviceValue();
            dValue.setBeginTime(device.getBeginTime());
            dValue.setEndTime(device.getEndTime());
            dValue.setEquipmentID(device.getEquipmentID());
            dValue.setLineID(device.getLineID());
            dValue.setUserID(device.getUserID());
            dValue.setShowStatus(showStatus);
            dValue.setUploadStatus(uploadStatus);
            DatabaseManager.getInstance().getInspectDeviceValueDao().insert(dValue);
        }else{
            value.setBeginTime(device.getBeginTime());
            value.setEndTime(device.getEndTime());
            value.setEquipmentID(device.getEquipmentID());
            value.setLineID(device.getLineID());
            value.setUserID(device.getUserID());
            value.setShowStatus(showStatus);
            value.setUploadStatus(uploadStatus);
            DatabaseManager.getInstance().getInspectDeviceValueDao().update(value);

        }
    }
    public static InspectDeviceValue getByDeviceId(Context ctx,Long deviceId,Long lineId){
        Long userID = SharedPreferenceUtil.getId(ctx,"User");
        List<InspectDeviceValue> valueList = DatabaseManager.getInstance().getInspectDeviceValueDao().queryBuilder().where(
                InspectDeviceValueDao.Properties.EquipmentID.eq(deviceId)
                ,InspectDeviceValueDao.Properties.LineID.eq(lineId)
                ,InspectDeviceValueDao.Properties.BeginTime.le(new Date())
                ,InspectDeviceValueDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceValueDao.Properties.UserID.eq(userID)
        ).list();
        return valueList.size()==0?null:valueList.get(0);
    }

    public static List<InspectDevice> getInpectDeviceListByTime(Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.BeginTime.le(new Date()),
                        InspectDeviceDao.Properties.EndTime.ge(new Date()),
                        InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices;
    }

    public static List<InspectDeviceValue> getValuesByUserId(Long userId){
        List<InspectDeviceValue> devices = DatabaseManager.getInstance().getInspectDeviceValueDao().queryBuilder()
                .where(InspectDeviceValueDao.Properties.UserID.eq(userId)).list();
        return devices;
    }

    public static void deleteByUserID(Long userId){
        List<InspectDeviceValue> inspectDeviceList = InspectDevicValueQueryUtil.getValuesByUserId(userId);
        for (InspectDeviceValue inspectDevice:inspectDeviceList){
            DatabaseManager.getInstance().getInspectDeviceValueDao().delete(inspectDevice);
        }
    }

}
