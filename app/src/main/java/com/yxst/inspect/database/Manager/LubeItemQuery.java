package com.yxst.inspect.database.Manager;

import android.content.Context;

import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.LubeItemDao;
import com.yxst.inspect.database.model.LubeItem;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/8/20 17:56
 */
public class LubeItemQuery {

    public static void deleteByUserId(Long UserId){
        List<LubeItem> list = findByUserID(UserId);
        if(list.size()!=0){
            for(LubeItem item:list){
                DatabaseManager.getInstance().getLubeItemDao().delete(item);
            }
        }
    }
    public static List<LubeItem> findByUserID(Long UserId){
        List<LubeItem> list = DatabaseManager.getInstance().getLubeItemDao().queryBuilder().where(
                LubeItemDao.Properties.UserID.eq(UserId)
        ).list();
        return list;
    }
    public static List<LubeItem> findByTime(Context context){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<LubeItem> list = DatabaseManager.getInstance().getLubeItemDao().queryBuilder().where(
                LubeItemDao.Properties.UserID.eq(id)
                ,LubeItemDao.Properties.BeginTime.le(new Date())
                ,LubeItemDao.Properties.EndTime.ge(new Date())
        ).list();
        return list;
    }
    public static List<LubeItem> findByDeviceID(Context context,Long deviceId,Long lineId){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<LubeItem> list = DatabaseManager.getInstance().getLubeItemDao().queryBuilder().where(
                LubeItemDao.Properties.UserID.eq(id)
                ,LubeItemDao.Properties.EquipmentID.eq(deviceId)
                ,LubeItemDao.Properties.ZoneID.eq(lineId)
                ,LubeItemDao.Properties.BeginTime.le(new Date())
                ,LubeItemDao.Properties.EndTime.ge(new Date())
        ).list();
        return list;
    }
    public static LubeItem findByItemID(Context context,Long itemId,Long lineId){
        Long id = SharedPreferenceUtil.getId(context,"User");
        List<LubeItem> list = DatabaseManager.getInstance().getLubeItemDao().queryBuilder().where(
                LubeItemDao.Properties.UserID.eq(id)
                ,LubeItemDao.Properties.LubricationItemID.eq(itemId)
                ,LubeItemDao.Properties.ZoneID.eq(lineId)
                ,LubeItemDao.Properties.BeginTime.le(new Date())
                ,LubeItemDao.Properties.EndTime.ge(new Date())
        ).list();
        return list.get(0) ;
    }
    public static void saveWithList(List<LubeItem> lubeDeviceList){
        DatabaseManager.getInstance().getLubeItemDao()
                //        .saveInTx(placeList);
                .insertInTx(lubeDeviceList);
    }
    public static void update(LubeItem item){
        DatabaseManager.getInstance().getLubeItemDao()
                        .saveInTx(item);
             //   .update(item);
    }
}
