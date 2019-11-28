package com.yxst.mes.database.Manager;

import android.content.Context;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.ItemDao;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/*
 * Created By YuanCheng on 2019/6/25 15:00
 */
public class ItemQueryUtil {
        public static List<Item> getItemByUserId(Long userId){
            List<Item> items = DatabaseManager.getInstance().getItemDao().queryBuilder().where(
                    ItemDao.Properties.UserID.eq(userId)).list();
            return items;
        }

    public static Item getItemById(Context ctx,Long itemId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao().queryBuilder().where(
                ItemDao.Properties.ItemID.eq(itemId)
                ,ItemDao.Properties.BeginTime.le(new Date())
                ,ItemDao.Properties.EndTime.ge(new Date())
                ,ItemDao.Properties.UserID.eq(userId)
                ,ItemDao.Properties.LineID.eq(lineId)
        ).list();
        return items.size()==0?null:items.get(0);
    }
    public static List<Item> getItemByDeviceId(Context ctx,Long deviceId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao().queryBuilder().where(
                ItemDao.Properties.EquipmentID.eq(deviceId)
                ,ItemDao.Properties.BeginTime.le(new Date())
                ,ItemDao.Properties.EndTime.ge(new Date())
                ,ItemDao.Properties.UserID.eq(userId)
                ,ItemDao.Properties.LineID.eq(lineId)
        ).list();
        return items;
    }
    public static List<Item> getItemByPIdAndStatus(Context ctx,Long placeId,Long lineId,int inspectStatus){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao()
                .queryBuilder().where(
                        ItemDao.Properties.InpsectStatus.eq(inspectStatus)
                        ,ItemDao.Properties.PlaceID.eq(placeId)
                        ,ItemDao.Properties.LineID.eq(lineId)
                        ,ItemDao.Properties.UserID.eq(userId)
                        ,ItemDao.Properties.BeginTime.le(new Date())
                        ,ItemDao.Properties.EndTime.ge(new Date())
                ).list();
        return items;
    }
    public static List<Item> getItemById(Context ctx,Long itemId,Long lineId,int inspectStatus){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao()
                .queryBuilder().where(
                        ItemDao.Properties.InpsectStatus.eq(inspectStatus)
                        ,ItemDao.Properties.ItemID.eq(itemId)
                        ,ItemDao.Properties.LineID.eq(lineId)
                        ,ItemDao.Properties.UserID.eq(userId)
                        ,ItemDao.Properties.BeginTime.le(new Date())
                        ,ItemDao.Properties.EndTime.ge(new Date())
                ).list();
        return items;
    }
    public static List<Item> getItemByPlaceId(Context ctx,Long placeId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao()
                .queryBuilder().where(
                        ItemDao.Properties.PlaceID.eq(placeId)
                        ,ItemDao.Properties.LineID.eq(lineId)
                        ,ItemDao.Properties.UserID.eq(userId)
                        ,ItemDao.Properties.BeginTime.le(new Date())
                        ,ItemDao.Properties.EndTime.ge(new Date())
                ).list();
        return items;
    }
    public static List<Item> getItemByStatus(Context ctx,Long placeId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao()
                .queryBuilder().where(
                        ItemDao.Properties.PlaceID.eq(placeId)
                        ,ItemDao.Properties.LineID.eq(lineId)
                        ,ItemDao.Properties.UserID.eq(userId)
                        ,ItemDao.Properties.InpsectStatus.eq(ConfigInfo.ITEM_INSPECT_STATUS)
                        ,ItemDao.Properties.BeginTime.le(new Date())
                        ,ItemDao.Properties.EndTime.ge(new Date())
                ).list();
        return items;
    }
    public static List<Item> getItemByTime(Context ctx){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<Item> items = DatabaseManager.getInstance().getItemDao().queryBuilder().where(
                        ItemDao.Properties.UserID.eq(userId)
                        ,ItemDao.Properties.BeginTime.le(new Date())
                        ,ItemDao.Properties.EndTime.ge(new Date())
                ).list();
        return items;
    }
        public static void saveWithItemList(List<Item> itemList){
             DatabaseManager.getInstance().getItemDao()
//                     .updateInTx(itemList);
 //                   .insertInTx(itemList);
             .insertOrReplaceInTx(itemList);

         }
        public static void saveItem(Item item){
            DatabaseManager.getInstance().getItemDao().insert(item);
        }
        public static void updateItem(Item item){
            DatabaseManager.getInstance().getItemDao().update(item);
        }
         public static void deleteAll(){
            DatabaseManager.getInstance().getItemDao().deleteAll();
         }
         public static void deleteWithItem(Item item){
            DatabaseManager.getInstance().getItemDao().delete(item);
         }
         public static void deleteByUserId(Long userId){
             List<Item> items = DatabaseManager.getInstance().getItemDao().queryBuilder().where(
                             ItemDao.Properties.UserID.eq(userId)).list();
             for(Item item:items){
                 DatabaseManager.getInstance().getItemDao().delete(item);
             }
         }
}
