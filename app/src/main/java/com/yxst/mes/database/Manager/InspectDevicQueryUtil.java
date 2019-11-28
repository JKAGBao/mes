package com.yxst.mes.database.Manager;

import android.content.Context;
import android.util.Log;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.InspectDeviceDao;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.util.SharedPreferenceUtil;

import org.greenrobot.greendao.Property;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/6/14 10:47
 */
public class InspectDevicQueryUtil {

    public static List<InspectDevice> getInpectDeviceListByStatus(int inpestStatus,int uploadStatus, Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.BeginTime.le(new Date()),
                        InspectDeviceDao.Properties.EndTime.ge(new Date())
                        ,InspectDeviceDao.Properties.InspectStatus.ge(inpestStatus)
                        ,InspectDeviceDao.Properties.UploadStatus.eq(uploadStatus)
                        ,InspectDeviceDao.Properties.UserID.eq(id)).list();
        Log.e("FinishActivity",devices.size()+"");

        return devices;
    }
    public static List<InspectDevice> getInpectDeviceListByTime(Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.BeginTime.le(new Date()),
                        InspectDeviceDao.Properties.EndTime.ge(new Date()),
                        InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices;
    }
    public static List<InspectDevice> getInpectDevicesByStatus(Context context,int inspectStatus){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.BeginTime.le(new Date()),
                        InspectDeviceDao.Properties.EndTime.ge(new Date()),
                        InspectDeviceDao.Properties.InspectStatus.eq(inspectStatus),
                        InspectDeviceDao.Properties.UserID.eq(id))
                .orderAsc(InspectDeviceDao.Properties.UploadStatus).list();

        return devices;
    }
    public static List<InspectDevice> getByInspectStatus(Context context,Long deviceID,Long lineID,int inspectStatus){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.BeginTime.le(new Date()),
                        InspectDeviceDao.Properties.EndTime.ge(new Date()),
                        InspectDeviceDao.Properties.EquipmentID.eq(deviceID),
                        InspectDeviceDao.Properties.LineID.eq(lineID),
                        InspectDeviceDao.Properties.InspectStatus.eq(inspectStatus),
                        InspectDeviceDao.Properties.UserID.eq(id))
                .list();

        return devices;
    }
    public static List<InspectDevice> getInpectDeviceListByUserId(Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices;
    }
    public static List<InspectDevice> getInspectDeviceListByUserId(Long userId){
        List<InspectDevice> devices = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder()
                .where(InspectDeviceDao.Properties.UserID.eq(userId)).list();
        return devices;
    }
    public static InspectDevice getInpectDeviceByDeviceIDNo(Context context,Long deviceId){
        Long id = SharedPreferenceUtil.getId(context,"User");
       List<InspectDevice> devices =  DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.EquipmentID.eq(deviceId)
                , InspectDeviceDao.Properties.BeginTime.le(new Date())//le小于等于
                , InspectDeviceDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices.size()==0?null:devices.get(0) ;
    }
    public static InspectDevice getInpectDeviceByDeviceID(Context context,Long deviceId,Long lineId){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices =  DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.EquipmentID.eq(deviceId)
                ,InspectDeviceDao.Properties.LineID.eq(lineId)
                , InspectDeviceDao.Properties.BeginTime.le(new Date())//le小于等于
                , InspectDeviceDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices.size()==0?null:devices.get(0) ;
    }
    public static InspectDevice getInpectDeviceBy(Context context,Long deviceId,Long lineId,int status){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices =  DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.EquipmentID.eq(deviceId)
                ,InspectDeviceDao.Properties.LineID.eq(lineId)
                ,InspectDeviceDao.Properties.UploadStatus.eq(status)
                , InspectDeviceDao.Properties.BeginTime.le(new Date())//le小于等于
                , InspectDeviceDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices.size()==0?null:devices.get(0) ;
    }
    public static List<InspectDevice> getByUnLineID(Context context,Long lineID,int status){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices =  DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.LineID.eq(lineID)
                ,InspectDeviceDao.Properties.InspectStatus.eq(status)
                , InspectDeviceDao.Properties.BeginTime.le(new Date())//le小于等于
                , InspectDeviceDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices ;
    }
    public static InspectDevice getInpectDeviceByLineID(Context context,Long lineID){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices =  DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.LineID.eq(lineID)
                , InspectDeviceDao.Properties.BeginTime.le(new Date())//le小于等于
                , InspectDeviceDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices.size()==0?null:devices.get(0) ;
    }
    public static List<InspectDevice> getByLineID(Context context,Long lineID){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<InspectDevice> devices =  DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.LineID.eq(lineID)
                , InspectDeviceDao.Properties.BeginTime.le(new Date())//le小于等于
                , InspectDeviceDao.Properties.EndTime.ge(new Date())
                ,InspectDeviceDao.Properties.UserID.eq(id)).list();

        return devices;
    }

    public static void updateInpectDevice(InspectDevice inspectDevice){
         DatabaseManager.getInstance().getInspectDeviceDao().update(inspectDevice);
    }
    public static void saveByList(List<InspectDevice> inspectDeviceList){
        DatabaseManager.getInstance().getInspectDeviceDao()
             //   .insertOrReplaceInTx(inspectDeviceList);
         //       .updateInTx(inspectDeviceList);
               .insertInTx(inspectDeviceList);

    }
    public static void deleteByUserID(Long userId){
        List<InspectDevice> inspectDeviceList = DatabaseManager.getInstance().getInspectDeviceDao().queryBuilder().where(
                InspectDeviceDao.Properties.UserID.eq(userId)).list();
        for (InspectDevice inspectDevice:inspectDeviceList){
            DatabaseManager.getInstance().getInspectDeviceDao().delete(inspectDevice);
        }
    }
    public static void updateInspectDeviceFinishStatus(Context ctx){
        List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(ctx);;
            for(InspectDevice device:inspectDevices){

                List<PlaceValue> finishPlaces = PlaceQueryUtil.getPlaceValueByStatus(ctx,device.getEquipmentID(),device.getLineID(),ConfigInfo.ITEM_INSPECT_STATUS);
                List<Place> places = PlaceQueryUtil.getPlaceByDeviceId(ctx,device.getEquipmentID(),device.getLineID());
           //     Toast.makeText(ctx, "place.size():"+places.size()+","+finishPlaces.size(), Toast.LENGTH_SHORT).show();
                if(finishPlaces.size() == 0 || places.size() == 0){
                    continue ;
                }
                if(finishPlaces.size()==places.size()){
                    device.setInspectStatus(2);
                    DatabaseManager.getInstance().getInspectDeviceDao().update(device);
                    InspectDevicValueQueryUtil.saveInspectDeviceValue(ctx,device,ConfigInfo.ITEM_UNUPLOAD_STATUS);

                }
            }
    }
    public static void updateInspectDeviceStatus(Context ctx){
        List<InspectDevice> inspectDevices = InspectDevicQueryUtil.getInpectDeviceListByTime(ctx);;
        for(InspectDevice device:inspectDevices){
            List<PlaceValue> finishPlaces = PlaceQueryUtil.getPlaceValueByStatus(ctx,device.getEquipmentID(),device.getLineID(),ConfigInfo.ITEM_INSPECT_STATUS);
            if(finishPlaces.size()!=0){
                device.setInspectStatus(1);//1表示正在巡检，可以显示出来
                DatabaseManager.getInstance().getInspectDeviceDao().update(device);
                InspectDevicValueQueryUtil.saveInspectDeviceValue(ctx,device,ConfigInfo.ITEM_UNUPLOAD_STATUS);
            }

        }
    }
}
