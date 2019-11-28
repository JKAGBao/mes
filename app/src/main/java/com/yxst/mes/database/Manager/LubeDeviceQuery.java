package com.yxst.mes.database.Manager;

import android.content.Context;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.LubeDeviceDao;
import com.yxst.mes.database.model.LubeDevice;
import com.yxst.mes.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/8/20 17:56
 */
public class LubeDeviceQuery {

    public static void deleteByUserId(Long UserId){
        List<LubeDevice> list = findByUserID(UserId);
        if(list.size()!=0){
            for(LubeDevice device:list){
                DatabaseManager.getInstance().getLubeDeviceDao().delete(device);
            }
        }
    }
    public static List<LubeDevice> findByUserID(Long UserId){
        List<LubeDevice> list = DatabaseManager.getInstance().getLubeDeviceDao().queryBuilder().where(
                LubeDeviceDao.Properties.UserID.eq(UserId)
        ).list();
        return list;
    }
    public static LubeDevice findByDeviceId(Context context,Long deviceID,Long lineID){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<LubeDevice> list = DatabaseManager.getInstance().getLubeDeviceDao().queryBuilder().where(
                LubeDeviceDao.Properties.UserID.eq(id)
                ,LubeDeviceDao.Properties.EquipmentID.eq(deviceID)
                ,LubeDeviceDao.Properties.ZoneID.eq(lineID)
                ,LubeDeviceDao.Properties.BeginTime.le(new Date())
                ,LubeDeviceDao.Properties.EndTime.ge(new Date())
        ).list();
        return list.size()!=0?list.get(0):null;
    }
    public static List<LubeDevice> findByTime(Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<LubeDevice> list = DatabaseManager.getInstance().getLubeDeviceDao().queryBuilder().where(
                LubeDeviceDao.Properties.UserID.eq(id)
                ,LubeDeviceDao.Properties.BeginTime.le(new Date())
                ,LubeDeviceDao.Properties.EndTime.ge(new Date())
        ).list();
        return list;
    }
    public static void saveWithList(List<LubeDevice> lubeDeviceList){
        DatabaseManager.getInstance().getLubeDeviceDao()
                //        .saveInTx(placeList);
                .insertInTx(lubeDeviceList);
    }
}
