package com.yxst.inspect.database.Manager;

import android.content.Context;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectImageDao;
import com.yxst.inspect.database.model.InspectImage;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/7/1 19:34
 */
public class InspectImageQueryUtil {
    public static List<InspectImage> getListByItemId(Context cxt,Long itemId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<InspectImage> imageList = DatabaseManager.getInstance().getInspectImageDao().queryBuilder()
                .where(InspectImageDao.Properties.BeginTime.le(new Date()),
                        InspectImageDao.Properties.EndTime.ge(new Date()),
                        InspectImageDao.Properties.InspectionItemID.eq(itemId),
                        InspectImageDao.Properties.LineID.eq(lineId)
                        ,InspectImageDao.Properties.UserId.eq(userId)).list();
        return imageList;
    }
    public static List<InspectImage> getImageByUploadStatus(Context cxt,Long deviceID,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<InspectImage> imageList = DatabaseManager.getInstance().getInspectImageDao().queryBuilder()
                .where(InspectImageDao.Properties.EquipmentID.eq(deviceID),
                        InspectImageDao.Properties.LineID.eq(lineId),
                        InspectImageDao.Properties.IsUploadImage.eq(ConfigInfo.UNDONE_IMAGE)
                        ,InspectImageDao.Properties.UserId.eq(userId)).list();
        return imageList;
    }
    public static List<InspectImage> getImageByServerStatus(Context cxt,Long deviceID,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<InspectImage> imageList = DatabaseManager.getInstance().getInspectImageDao().queryBuilder()
                .where(InspectImageDao.Properties.EquipmentID.eq(deviceID),
                        InspectImageDao.Properties.LineID.eq(lineId),
                        InspectImageDao.Properties.IsUploadServer.eq(ConfigInfo.UNDONE_IMAGE)
                        ,InspectImageDao.Properties.UserId.eq(userId)).list();
        return imageList;
    }
    public static InspectImage getImageByURL(String urlPath){
        InspectImage dbImage = DatabaseManager.getInstance().getInspectImageDao().queryBuilder()
                .where(InspectImageDao.Properties.LocalURL.eq(urlPath)).unique();
        return dbImage;
    }
    public static void updateImage(InspectImage image){
        DatabaseManager.getInstance().getInspectImageDao().update(image);
    }
    public static void insertImage(InspectImage image){
        DatabaseManager.getInstance().getInspectImageDao().insert(image);
    }
}
