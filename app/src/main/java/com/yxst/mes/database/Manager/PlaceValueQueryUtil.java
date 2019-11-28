package com.yxst.mes.database.Manager;

import android.content.Context;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.PlaceValueDao;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/6/27 20:03
 */
public class PlaceValueQueryUtil {

    public static List<PlaceValue> getPlaceValueByStatus(Context cxt,Long deviceId,Long lineId,int status){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<PlaceValue> placeList = DatabaseManager.getInstance().getPlaceValueDao().queryBuilder().where(
                PlaceValueDao.Properties.EquipmentID.eq(deviceId)
                ,PlaceValueDao.Properties.LineID.eq(lineId)
                ,PlaceValueDao.Properties.BeginTime.le(new Date())
                ,PlaceValueDao.Properties.EndTime.ge(new Date())
                ,PlaceValueDao.Properties.UserID.eq(userId)
                ,PlaceValueDao.Properties.InspectStatus.eq(status)
        ).list();
        return placeList;
    }
    public static List<PlaceValue> getPlaceValueById(Context cxt,long placeId,long lineId,int status){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<PlaceValue> placeList = DatabaseManager.getInstance().getPlaceValueDao().queryBuilder().where(
                PlaceValueDao.Properties.PlaceID.eq(placeId)
                ,PlaceValueDao.Properties.LineID.eq(lineId)
                ,PlaceValueDao.Properties.UserID.eq(userId)
                ,PlaceValueDao.Properties.BeginTime.le(new Date())
                ,PlaceValueDao.Properties.EndTime.ge(new Date())
                ,PlaceValueDao.Properties.InspectStatus.eq(status)
        ).list();
        return placeList;
    }
    public static PlaceValue getPlaceValueByPlaceId(Context cxt,Long placeId,Long deviceId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<PlaceValue> placeList = DatabaseManager.getInstance().getPlaceValueDao().queryBuilder().where(
                PlaceValueDao.Properties.EquipmentID.eq(deviceId)
                ,PlaceValueDao.Properties.PlaceID.eq(placeId)
                ,PlaceValueDao.Properties.LineID.eq(lineId)
                ,PlaceValueDao.Properties.BeginTime.le(new Date())
                ,PlaceValueDao.Properties.EndTime.ge(new Date())
                ,PlaceValueDao.Properties.UserID.eq(userId)
        ).list();
        return placeList.size()==0?null:placeList.get(0);
    }
    public static void savePlaceValue(Context ctx,Place place){
        PlaceValue placeValues = PlaceValueQueryUtil.getPlaceValueByPlaceId(ctx,place.getPlaceID(),place.getEquipmentID(),place.getLineID());
        if(placeValues == null){
            PlaceValue value = new PlaceValue();
            value.setBeginTime(place.getBeginTime());
            value.setEndTime(place.getEndTime());
            value.setEquipmentID(place.getEquipmentID());
            value.setLineID(place.getLineID());
            value.setPlaceID(place.getPlaceID());
            value.setUserID(place.getUserID());
            value.setInspectStatus(1);
            DatabaseManager.getInstance().getPlaceValueDao().insert(value);
        }else{
            placeValues.setBeginTime(place.getBeginTime());
            placeValues.setEndTime(place.getEndTime());
            placeValues.setEquipmentID(place.getEquipmentID());
            placeValues.setLineID(place.getLineID());
            placeValues.setPlaceID(place.getPlaceID());
            placeValues.setUserID(place.getUserID());
            placeValues.setInspectStatus(1);
            DatabaseManager.getInstance().getPlaceValueDao().update(placeValues);
        }
    }

    public static List<PlaceValue> getPlaceByUserId(Long userId){

        List<PlaceValue> placeList = DatabaseManager.getInstance().getPlaceValueDao().queryBuilder().where(
                PlaceValueDao.Properties.UserID.eq(userId)
        ).list();
        return placeList;
    }

    public static void deleteWithPlaceValue(PlaceValue place){
        DatabaseManager.getInstance().getPlaceValueDao().delete(place);
    }
    public static void updatePlaceValue(PlaceValue place){
        DatabaseManager.getInstance().getPlaceValueDao().update(place);
    }
    public static void deleteByUserId(Long userId){
        List<PlaceValue> placeList = PlaceValueQueryUtil.getPlaceByUserId(userId);
        if(placeList.size()>0){
            for(PlaceValue place:placeList){
                DatabaseManager.getInstance().getPlaceValueDao().delete(place);
            }
        }
    }

}
