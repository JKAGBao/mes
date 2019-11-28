package com.yxst.mes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.yxst.mes.database.Manager.CustomOpenHelper;
import com.yxst.mes.database.dao.CycleDao;
import com.yxst.mes.database.dao.DaoMaster;
import com.yxst.mes.database.dao.DaoSession;
import com.yxst.mes.database.dao.DeviceDao;
import com.yxst.mes.database.dao.GradeDao;
import com.yxst.mes.database.dao.InspectDeviceDao;
import com.yxst.mes.database.dao.InspectDeviceValueDao;
import com.yxst.mes.database.dao.InspectImageDao;
import com.yxst.mes.database.dao.InspectLineDao;
import com.yxst.mes.database.dao.ItemDao;
import com.yxst.mes.database.dao.ItemValueDao;
import com.yxst.mes.database.dao.LineDao;
import com.yxst.mes.database.dao.LubeDeviceDao;
import com.yxst.mes.database.dao.LubeImageDao;
import com.yxst.mes.database.dao.LubeItemDao;
import com.yxst.mes.database.dao.PlaceDao;
import com.yxst.mes.database.dao.PlaceValueDao;
import com.yxst.mes.database.dao.PlanDao;
import com.yxst.mes.database.dao.RecordDao;
import com.yxst.mes.database.dao.SampleDao;
import com.yxst.mes.database.dao.UnLubeDao;
import com.yxst.mes.database.dao.UndetectDao;
import com.yxst.mes.database.dao.UserDao;

/*
数据库操作类：初始化、获取Dao
 */
public class DatabaseManager {

    private static DaoSession daoSession = null;
    //private DaoMaster.DevOpenHelper openHelper;
    private CustomOpenHelper openHelper;
    private SQLiteDatabase db;
    private UserDao userDao = null;
    private DatabaseManager(){}
    /*
    获取实例
     */
    public static DatabaseManager getInstance(){
        return Holder.INSTANCE;
    }
    private static final class Holder{
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }
    public static DaoSession getDaoSession(){
        return daoSession;
    }
    public DatabaseManager init(Context context){
        initSession(context);
        return this;
    }
    private void initSession(Context context){
    //    final ReleaseOpenHelper openHelper = new ReleaseOpenHelper(context,"mestest.db");
         openHelper = new CustomOpenHelper(context,"mes.db",null);
         try{
             db = openHelper.getWritableDatabase();
         }catch (SQLiteException e){
             db = openHelper.getReadableDatabase();
         }
         daoSession = new DaoMaster(db).newSession();
    }
    public void deleteSQL(){
        DaoMaster daoMaster = new DaoMaster(db);
        DaoMaster.dropAllTables(daoMaster.getDatabase(),true);
        DaoMaster.createAllTables(daoMaster.getDatabase(),true);

    }
    public final LineDao getLineDao(){
        return  daoSession.getLineDao();
    }
    public final InspectLineDao getInspectLineDao(){
        return  daoSession.getInspectLineDao();
    }
    public final DeviceDao getDeviceDao(){
        return daoSession.getDeviceDao();
    }
    public final ItemDao getItemDao(){
        return daoSession.getItemDao();
    }
    public final UserDao getDao(){
        return  daoSession.getUserDao();
    }
    public final RecordDao getRecordDao(){
        return  daoSession.getRecordDao();
    }
    public final PlaceDao getPlaceDao(){
        return  daoSession.getPlaceDao();
    }
    public final PlaceValueDao getPlaceValueDao(){
        return  daoSession.getPlaceValueDao();
    }
    public final UndetectDao getUndetectDao() {
        return daoSession.getUndetectDao();
    }
    public final PlanDao getPlanDao(){
        return daoSession.getPlanDao();
    }
    public final CycleDao getCycleDao(){
        return daoSession.getCycleDao();
    }
    public final InspectDeviceDao getInspectDeviceDao(){
        return daoSession.getInspectDeviceDao();
    }
    public final InspectDeviceValueDao getInspectDeviceValueDao(){
        return daoSession.getInspectDeviceValueDao();
    }
    public final GradeDao getGradeDao(){ return daoSession.getGradeDao();}
    public final InspectImageDao getInspectImageDao(){ return  daoSession.getInspectImageDao();}
    public final UserDao getUserDao(){ return  daoSession.getUserDao();}
    public final ItemValueDao getItemValueDao(){ return  daoSession.getItemValueDao(); }
    public final LubeDeviceDao getLubeDeviceDao(){
        return daoSession.getLubeDeviceDao();
    }
    public final UnLubeDao getUnLubeDao(){
        return daoSession.getUnLubeDao();
    }

    public final LubeItemDao getLubeItemDao(){
        return daoSession.getLubeItemDao();
    }
    public final LubeImageDao getLubeImageDao(){
        return daoSession.getLubeImageDao();
    }
    public final SampleDao getSampleDao(){
        return daoSession.getSampleDao();
    }

}

