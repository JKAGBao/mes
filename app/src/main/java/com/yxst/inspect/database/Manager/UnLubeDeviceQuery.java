package com.yxst.inspect.database.Manager;

import android.content.Context;

import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.UnLubeDao;
import com.yxst.inspect.database.model.UnLube;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/8/20 17:56
 */
public class UnLubeDeviceQuery {

    public static void deleteByUserId(Long UserId){
        List<UnLube> list = findByUserID(UserId);
        if(list.size()!=0){
            for(UnLube device:list){
                DatabaseManager.getInstance().getUnLubeDao().delete(device);
            }
        }
    }
    public static List<UnLube> findByUserID(Long UserId){
        List<UnLube> list = DatabaseManager.getInstance().getUnLubeDao().queryBuilder().where(
                UnLubeDao.Properties.UserID.eq(UserId)
        ).list();
        return list;
    }
    public static UnLube findByDeviceId(Context context,Long deviceID,Long lineID){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<UnLube> list = DatabaseManager.getInstance().getUnLubeDao().queryBuilder().where(
                UnLubeDao.Properties.UserID.eq(id)
                ,UnLubeDao.Properties.EquipmentID.eq(deviceID)
                ,UnLubeDao.Properties.ZoneID.eq(lineID)
                ,UnLubeDao.Properties.BeginTime.le(new Date())
                ,UnLubeDao.Properties.EndTime.ge(new Date())
        ).list();
        return list.size()!=0?list.get(0):null;
    }
    public static List<UnLube> findByTime(Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
//        List<UnLube> undetectList = DatabaseManager.getInstance().getUnLubeDao().queryBuilder().where(
//                UnLubeDao.Properties.BeginTime.le(new Date())
//                ,UnLubeDao.Properties.EndTime.ge(new Date())
//        ).list();
        List<UnLube> undetectList = DatabaseManager.getInstance().getUnLubeDao().loadAll();
        return undetectList;
    }
    public static void saveWithList(List<UnLube> lubeDeviceList){
        DatabaseManager.getInstance().getUnLubeDao()
                //        .saveInTx(placeList);
                .insertInTx(lubeDeviceList);
    }
}
