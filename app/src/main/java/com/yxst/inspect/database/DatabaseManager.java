package com.yxst.inspect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.yxst.inspect.database.Manager.CustomOpenHelper;
import com.yxst.inspect.database.dao.CycleDao;
import com.yxst.inspect.database.dao.DaoMaster;
import com.yxst.inspect.database.dao.DaoSession;
import com.yxst.inspect.database.dao.DeviceDao;
import com.yxst.inspect.database.dao.GradeDao;
import com.yxst.inspect.database.dao.InspectDeviceDao;
import com.yxst.inspect.database.dao.InspectDeviceValueDao;
import com.yxst.inspect.database.dao.InspectImageDao;
import com.yxst.inspect.database.dao.InspectLineDao;
import com.yxst.inspect.database.dao.ItemDao;
import com.yxst.inspect.database.dao.LineDao;
import com.yxst.inspect.database.dao.LubeDeviceDao;
import com.yxst.inspect.database.dao.LubeImageDao;
import com.yxst.inspect.database.dao.LubeItemDao;
import com.yxst.inspect.database.dao.PlaceDao;
import com.yxst.inspect.database.dao.PlanDao;
import com.yxst.inspect.database.dao.RecordDao;
import com.yxst.inspect.database.dao.SampleDao;
import com.yxst.inspect.database.dao.UnLubeDao;
import com.yxst.inspect.database.dao.UndetectDao;
import com.yxst.inspect.database.dao.UserDao;

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
    public final UndetectDao getUndetectDao() { return daoSession.getUndetectDao(); }
    public final PlanDao getPlanDao(){
        return daoSession.getPlanDao();
    }
    public final CycleDao getCycleDao(){
        return daoSession.getCycleDao();
    }
    public final InspectDeviceDao getInspectDeviceDao(){
        return daoSession.getInspectDeviceDao();
    }
    public final InspectDeviceValueDao getInspectDeviceValueDao(){ return daoSession.getInspectDeviceValueDao(); }
    public final GradeDao getGradeDao(){ return daoSession.getGradeDao();}
    public final InspectImageDao getInspectImageDao(){ return  daoSession.getInspectImageDao();}
    public final UserDao getUserDao(){ return  daoSession.getUserDao();}
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

