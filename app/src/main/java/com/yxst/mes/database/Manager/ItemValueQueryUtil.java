package com.yxst.mes.database.Manager;

import android.content.Context;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.ItemValueDao;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.model.ItemValue;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/*
 * Created By YuanCheng on 2019/6/25 15:00
 */
public class ItemValueQueryUtil {

    public static List<ItemValue> getItemValueByPIdAndStatus(Context ctx,Long placeId,Long lineId,int inspectStatus){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<ItemValue> items = DatabaseManager.getInstance().getItemValueDao()
                .queryBuilder().where(
                        ItemValueDao.Properties.PlaceID.eq(placeId)
                        ,ItemValueDao.Properties.LineID.eq(lineId)
                        ,ItemValueDao.Properties.UserID.eq(userId)
                        ,ItemValueDao.Properties.InpsectStatus.eq(inspectStatus)
                        ,ItemValueDao.Properties.BeginTime.le(new Date())
                        ,ItemValueDao.Properties.EndTime.ge(new Date())
                ).list();
        return items;
    }
    public static ItemValue getItemValueByItemId(Context ctx,Long itemId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<ItemValue> items = DatabaseManager.getInstance().getItemValueDao().queryBuilder().where(
                        ItemValueDao.Properties.ItemID.eq(itemId)
                        ,ItemValueDao.Properties.LineID.eq(lineId)
                        ,ItemValueDao.Properties.UserID.eq(userId)
                        ,ItemValueDao.Properties.BeginTime.le(new Date())
                        ,ItemValueDao.Properties.EndTime.ge(new Date())
                ).list();
        return items.size()==0?null:items.get(0);
    }
    public static List<ItemValue> getByItemId(Context ctx,Long itemId,Long lineId,int status){
        Long userId = SharedPreferenceUtil.getId(ctx,"User");
        List<ItemValue> items = DatabaseManager.getInstance().getItemValueDao().queryBuilder().where(
                ItemValueDao.Properties.ItemID.eq(itemId)
                ,ItemValueDao.Properties.LineID.eq(lineId)
                ,ItemValueDao.Properties.UserID.eq(userId)
                ,ItemValueDao.Properties.InpsectStatus.eq(status)
                ,ItemValueDao.Properties.BeginTime.le(new Date())
                ,ItemValueDao.Properties.EndTime.ge(new Date())
        ).list();
        return items;
    }

         public static void deletItemValue(Long userId){
            List<ItemValue> itemValueList = DatabaseManager.getInstance().getItemValueDao().queryBuilder().where(
                    ItemValueDao.Properties.UserID.eq(userId)
            ).list();
            for (ItemValue value : itemValueList){
                DatabaseManager.getInstance().getItemValueDao().delete(value);
            }
         }
        public static void saveItemValue(Context ctx,Item item,String checkValue){
            ItemValue itemValue =ItemValueQueryUtil.getItemValueByItemId(ctx,item.getItemID(),item.getLineID());
            if(itemValue==null){
                ItemValue value = new ItemValue();
                value.setCheckValue(checkValue);
                value.setInpsectStatus(ConfigInfo.ITEM_INSPECT_STATUS);
                value.setBeginTime(item.getBeginTime());
                value.setEndTime(item.getEndTime());
                value.setItemID(item.getItemID());
                value.setEquipmentID(item.getEquipmentID());
                value.setPlaceID(item.getPlaceID());
                value.setLineID(item.getLineID());
                value.setUserID(item.getUserID());
                DatabaseManager.getInstance().getItemValueDao().insert(value);
            }else{
                itemValue.setCheckValue(checkValue);
                itemValue.setInpsectStatus(ConfigInfo.ITEM_INSPECT_STATUS);
                itemValue.setBeginTime(item.getBeginTime());
                itemValue.setEndTime(item.getEndTime());
                itemValue.setItemID(item.getItemID());
                itemValue.setEquipmentID(item.getEquipmentID());
                itemValue.setPlaceID(item.getPlaceID());
                itemValue.setLineID(item.getLineID());
                itemValue.setUserID(item.getUserID());
                DatabaseManager.getInstance().getItemValueDao().update(itemValue);
            }

        }

}
