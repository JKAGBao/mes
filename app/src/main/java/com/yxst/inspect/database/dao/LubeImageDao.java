package com.yxst.inspect.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.yxst.inspect.database.model.LubeImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LUBE_IMAGE".
*/
public class LubeImageDao extends AbstractDao<LubeImage, Long> {

    public static final String TABLENAME = "LUBE_IMAGE";

    /**
     * Properties of entity LubeImage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FatID = new Property(1, Long.class, "FatID", false, "FAT_ID");
        public final static Property PlanID = new Property(2, Long.class, "PlanID", false, "PLAN_ID");
        public final static Property ZoneID = new Property(3, Long.class, "ZoneID", false, "ZONE_ID");
        public final static Property LubricationItemID = new Property(4, Long.class, "LubricationItemID", false, "LUBRICATION_ITEM_ID");
        public final static Property BeginTime = new Property(5, java.util.Date.class, "BeginTime", false, "BEGIN_TIME");
        public final static Property EndTime = new Property(6, java.util.Date.class, "EndTime", false, "END_TIME");
        public final static Property UploadPath = new Property(7, String.class, "UploadPath", false, "UPLOAD_PATH");
        public final static Property ImgURL = new Property(8, String.class, "ImgURL", false, "IMG_URL");
        public final static Property LocalURL = new Property(9, String.class, "LocalURL", false, "LOCAL_URL");
        public final static Property UserId = new Property(10, Long.class, "userId", false, "USER_ID");
        public final static Property IsUploadServer = new Property(11, int.class, "isUploadServer", false, "IS_UPLOAD_SERVER");
        public final static Property IsUploadImage = new Property(12, int.class, "isUploadImage", false, "IS_UPLOAD_IMAGE");
    }


    public LubeImageDao(DaoConfig config) {
        super(config);
    }
    
    public LubeImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LUBE_IMAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FAT_ID\" INTEGER," + // 1: FatID
                "\"PLAN_ID\" INTEGER," + // 2: PlanID
                "\"ZONE_ID\" INTEGER," + // 3: ZoneID
                "\"LUBRICATION_ITEM_ID\" INTEGER," + // 4: LubricationItemID
                "\"BEGIN_TIME\" INTEGER," + // 5: BeginTime
                "\"END_TIME\" INTEGER," + // 6: EndTime
                "\"UPLOAD_PATH\" TEXT," + // 7: UploadPath
                "\"IMG_URL\" TEXT," + // 8: ImgURL
                "\"LOCAL_URL\" TEXT," + // 9: LocalURL
                "\"USER_ID\" INTEGER," + // 10: userId
                "\"IS_UPLOAD_SERVER\" INTEGER NOT NULL ," + // 11: isUploadServer
                "\"IS_UPLOAD_IMAGE\" INTEGER NOT NULL );"); // 12: isUploadImage
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LUBE_IMAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LubeImage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long FatID = entity.getFatID();
        if (FatID != null) {
            stmt.bindLong(2, FatID);
        }
 
        Long PlanID = entity.getPlanID();
        if (PlanID != null) {
            stmt.bindLong(3, PlanID);
        }
 
        Long ZoneID = entity.getZoneID();
        if (ZoneID != null) {
            stmt.bindLong(4, ZoneID);
        }
 
        Long LubricationItemID = entity.getLubricationItemID();
        if (LubricationItemID != null) {
            stmt.bindLong(5, LubricationItemID);
        }
 
        java.util.Date BeginTime = entity.getBeginTime();
        if (BeginTime != null) {
            stmt.bindLong(6, BeginTime.getTime());
        }
 
        java.util.Date EndTime = entity.getEndTime();
        if (EndTime != null) {
            stmt.bindLong(7, EndTime.getTime());
        }
 
        String UploadPath = entity.getUploadPath();
        if (UploadPath != null) {
            stmt.bindString(8, UploadPath);
        }
 
        String ImgURL = entity.getImgURL();
        if (ImgURL != null) {
            stmt.bindString(9, ImgURL);
        }
 
        String LocalURL = entity.getLocalURL();
        if (LocalURL != null) {
            stmt.bindString(10, LocalURL);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(11, userId);
        }
        stmt.bindLong(12, entity.getIsUploadServer());
        stmt.bindLong(13, entity.getIsUploadImage());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LubeImage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long FatID = entity.getFatID();
        if (FatID != null) {
            stmt.bindLong(2, FatID);
        }
 
        Long PlanID = entity.getPlanID();
        if (PlanID != null) {
            stmt.bindLong(3, PlanID);
        }
 
        Long ZoneID = entity.getZoneID();
        if (ZoneID != null) {
            stmt.bindLong(4, ZoneID);
        }
 
        Long LubricationItemID = entity.getLubricationItemID();
        if (LubricationItemID != null) {
            stmt.bindLong(5, LubricationItemID);
        }
 
        java.util.Date BeginTime = entity.getBeginTime();
        if (BeginTime != null) {
            stmt.bindLong(6, BeginTime.getTime());
        }
 
        java.util.Date EndTime = entity.getEndTime();
        if (EndTime != null) {
            stmt.bindLong(7, EndTime.getTime());
        }
 
        String UploadPath = entity.getUploadPath();
        if (UploadPath != null) {
            stmt.bindString(8, UploadPath);
        }
 
        String ImgURL = entity.getImgURL();
        if (ImgURL != null) {
            stmt.bindString(9, ImgURL);
        }
 
        String LocalURL = entity.getLocalURL();
        if (LocalURL != null) {
            stmt.bindString(10, LocalURL);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(11, userId);
        }
        stmt.bindLong(12, entity.getIsUploadServer());
        stmt.bindLong(13, entity.getIsUploadImage());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LubeImage readEntity(Cursor cursor, int offset) {
        LubeImage entity = new LubeImage( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // FatID
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // PlanID
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // ZoneID
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // LubricationItemID
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // BeginTime
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // EndTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // UploadPath
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ImgURL
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // LocalURL
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // userId
            cursor.getInt(offset + 11), // isUploadServer
            cursor.getInt(offset + 12) // isUploadImage
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LubeImage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFatID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setPlanID(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setZoneID(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setLubricationItemID(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setBeginTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setEndTime(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setUploadPath(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setImgURL(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLocalURL(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUserId(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setIsUploadServer(cursor.getInt(offset + 11));
        entity.setIsUploadImage(cursor.getInt(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LubeImage entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LubeImage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LubeImage entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}