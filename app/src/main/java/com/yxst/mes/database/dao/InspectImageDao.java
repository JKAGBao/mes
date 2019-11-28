package com.yxst.mes.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.yxst.mes.database.model.InspectImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "INSPECT_IMAGE".
*/
public class InspectImageDao extends AbstractDao<InspectImage, Long> {

    public static final String TABLENAME = "INSPECT_IMAGE";

    /**
     * Properties of entity InspectImage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property EquipmentID = new Property(1, Long.class, "EquipmentID", false, "EQUIPMENT_ID");
        public final static Property PlanID = new Property(2, Long.class, "PlanID", false, "PLAN_ID");
        public final static Property LineID = new Property(3, Long.class, "LineID", false, "LINE_ID");
        public final static Property InspectionItemID = new Property(4, Long.class, "InspectionItemID", false, "INSPECTION_ITEM_ID");
        public final static Property BeginTime = new Property(5, java.util.Date.class, "BeginTime", false, "BEGIN_TIME");
        public final static Property EndTime = new Property(6, java.util.Date.class, "EndTime", false, "END_TIME");
        public final static Property UploadPath = new Property(7, String.class, "UploadPath", false, "UPLOAD_PATH");
        public final static Property ImgURL = new Property(8, String.class, "ImgURL", false, "IMG_URL");
        public final static Property LocalURL = new Property(9, String.class, "LocalURL", false, "LOCAL_URL");
        public final static Property UserId = new Property(10, Long.class, "userId", false, "USER_ID");
        public final static Property IsUploadServer = new Property(11, int.class, "isUploadServer", false, "IS_UPLOAD_SERVER");
        public final static Property IsUploadImage = new Property(12, int.class, "isUploadImage", false, "IS_UPLOAD_IMAGE");
    }


    public InspectImageDao(DaoConfig config) {
        super(config);
    }
    
    public InspectImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"INSPECT_IMAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"EQUIPMENT_ID\" INTEGER," + // 1: EquipmentID
                "\"PLAN_ID\" INTEGER," + // 2: PlanID
                "\"LINE_ID\" INTEGER," + // 3: LineID
                "\"INSPECTION_ITEM_ID\" INTEGER," + // 4: InspectionItemID
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
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"INSPECT_IMAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, InspectImage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long EquipmentID = entity.getEquipmentID();
        if (EquipmentID != null) {
            stmt.bindLong(2, EquipmentID);
        }
 
        Long PlanID = entity.getPlanID();
        if (PlanID != null) {
            stmt.bindLong(3, PlanID);
        }
 
        Long LineID = entity.getLineID();
        if (LineID != null) {
            stmt.bindLong(4, LineID);
        }
 
        Long InspectionItemID = entity.getInspectionItemID();
        if (InspectionItemID != null) {
            stmt.bindLong(5, InspectionItemID);
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
    protected final void bindValues(SQLiteStatement stmt, InspectImage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long EquipmentID = entity.getEquipmentID();
        if (EquipmentID != null) {
            stmt.bindLong(2, EquipmentID);
        }
 
        Long PlanID = entity.getPlanID();
        if (PlanID != null) {
            stmt.bindLong(3, PlanID);
        }
 
        Long LineID = entity.getLineID();
        if (LineID != null) {
            stmt.bindLong(4, LineID);
        }
 
        Long InspectionItemID = entity.getInspectionItemID();
        if (InspectionItemID != null) {
            stmt.bindLong(5, InspectionItemID);
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
    public InspectImage readEntity(Cursor cursor, int offset) {
        InspectImage entity = new InspectImage( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // EquipmentID
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // PlanID
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // LineID
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // InspectionItemID
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
    public void readEntity(Cursor cursor, InspectImage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEquipmentID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setPlanID(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setLineID(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setInspectionItemID(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
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
    protected final Long updateKeyAfterInsert(InspectImage entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(InspectImage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(InspectImage entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}