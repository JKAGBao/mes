package com.yxst.mes.database.Manager;

import android.content.Context;

import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.PlaceDao;
import com.yxst.mes.database.dao.PlaceValueDao;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/6/27 20:03
 */
public class PlaceQueryUtil {

    public static List<Place> getPlaceByStatus(Context cxt,Long deviceId,Long lineId,int status){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<Place> placeList = DatabaseManager.getInstance().getPlaceDao().queryBuilder().where(
                PlaceDao.Properties.EquipmentID.eq(deviceId)
                ,PlaceDao.Properties.LineID.eq(lineId)
                ,PlaceDao.Properties.BeginTime.le(new Date())
                ,PlaceDao.Properties.EndTime.ge(new Date())
                ,PlaceDao.Properties.UserID.eq(userId)
                ,PlaceDao.Properties.InspectStatus.eq(status)
        ).list();
        return placeList;
    }
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
    public static List<PlaceValue> getPlaceValueByStatus(Context cxt,Long lineId,int status){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<PlaceValue> placeList = DatabaseManager.getInstance().getPlaceValueDao().queryBuilder().where(
                PlaceValueDao.Properties.LineID.eq(lineId)
                ,PlaceValueDao.Properties.BeginTime.le(new Date())
                ,PlaceValueDao.Properties.EndTime.ge(new Date())
                ,PlaceValueDao.Properties.UserID.eq(userId)
                ,PlaceValueDao.Properties.InspectStatus.eq(status)
        ).list();
        return placeList;
    }

    public static List<Place> getPlaceByDeviceId(Context cxt,Long deviceId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<Place> placeList = DatabaseManager.getInstance().getPlaceDao().queryBuilder().where(
                PlaceDao.Properties.EquipmentID.eq(deviceId)
                ,PlaceDao.Properties.LineID.eq(lineId)
                ,PlaceDao.Properties.BeginTime.le(new Date())
                ,PlaceDao.Properties.EndTime.ge(new Date())
                ,PlaceDao.Properties.UserID.eq(userId)
        ).list();
        return placeList;
    }
    public static List<Place> getPlaceByLineId(Context cxt,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<Place> placeList = DatabaseManager.getInstance().getPlaceDao().queryBuilder().where(
                PlaceDao.Properties.LineID.eq(lineId)
                ,PlaceDao.Properties.BeginTime.le(new Date())
                ,PlaceDao.Properties.EndTime.ge(new Date())
                ,PlaceDao.Properties.UserID.eq(userId)
        ).list();
        return placeList;
    }

    public static List<Place> getPlaceByUserId(Long userId){

        List<Place> placeList = DatabaseManager.getInstance().getPlaceDao().queryBuilder().where(
                PlaceDao.Properties.UserID.eq(userId)
        ).list();
        return placeList;
    }
    public static List<Place> getPlaceByTime(Context cxt){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<Place> placeList = DatabaseManager.getInstance().getPlaceDao().queryBuilder().where(
                 PlaceDao.Properties.BeginTime.le(new Date())
                ,PlaceDao.Properties.EndTime.ge(new Date())
                ,PlaceDao.Properties.UserID.eq(userId)
        ).list();
        return placeList;
    }

    public static void saveWithList(List<Place> list){
        DatabaseManager.getInstance().getPlaceDao()
                //        .saveInTx(placeList);
                .insertInTx(list);
    }


    public static void deleteWithPlace(Place place){
        DatabaseManager.getInstance().getPlaceDao().delete(place);
    }
    public static void deleteByUserId(Long userId){
        List<Place> placeList = PlaceQueryUtil.getPlaceByUserId(userId);
        if(placeList.size()>0){
            for(Place place:placeList){
                PlaceQueryUtil.deleteWithPlace(place);
            }
        }
    }

}
