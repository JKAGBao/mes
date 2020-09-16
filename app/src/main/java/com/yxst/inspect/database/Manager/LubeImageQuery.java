package com.yxst.inspect.database.Manager;

import android.content.Context;

import com.yxst.inspect.ConfigInfo;
import com.yxst.inspect.database.DatabaseManager;
import com.yxst.inspect.database.dao.InspectImageDao;
import com.yxst.inspect.database.dao.LubeImageDao;
import com.yxst.inspect.database.model.InspectImage;
import com.yxst.inspect.database.model.LubeImage;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created By YuanCheng on 2019/7/1 19:34
 */
public class LubeImageQuery {
    public static List<LubeImage> getListByItemId(Context cxt,Long itemId,Long lineId){
        Long userId = SharedPreferenceUtil.getId(cxt,"User");
        List<LubeImage> imageList = DatabaseManager.getInstance().getLubeImageDao().queryBuilder()
                .where(LubeImageDao.Properties.BeginTime.le(new Date()),
                        LubeImageDao.Properties.EndTime.ge(new Date()),
                        LubeImageDao.Properties.LubricationItemID.eq(itemId),
                        LubeImageDao.Properties.ZoneID.eq(lineId)
                        ,LubeImageDao.Properties.UserId.eq(userId)).list();
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
    public static LubeImage getImageByURL(String urlPath){
        LubeImage dbImage = DatabaseManager.getInstance().getLubeImageDao().queryBuilder()
                .where(LubeImageDao.Properties.LocalURL.eq(urlPath)).unique();
        return dbImage;
    }
    public static void updateImage(LubeImage image){
        DatabaseManager.getInstance().getLubeImageDao().update(image);
    }
    public static void insertImage(LubeImage image){
        DatabaseManager.getInstance().getLubeImageDao().insert(image);
    }
}
