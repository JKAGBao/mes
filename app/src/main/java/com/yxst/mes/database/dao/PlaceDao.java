package com.yxst.mes.database.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.yxst.mes.database.model.Place;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PLACE".
*/
public class PlaceDao extends AbstractDao<Place, Long> {

    public static final String TABLENAME = "PLACE";

    /**
     * Properties of entity Place.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "_id");
        public final static Property UserID = new Property(1, Long.class, "UserID", false, "USER_ID");
        public final static Property LineID = new Property(2, Long.class, "LineID", false, "LINE_ID");
        public final static Property EquipmentID = new Property(3, Long.class, "EquipmentID", false, "EQUIPMENT_ID");
        public final static Property PlaceID = new Property(4, Long.class, "PlaceID", false, "PLACE_ID");
        public final static Property PlaceCode = new Property(5, String.class, "PlaceCode", false, "PLACE_CODE");
        public final static Property PlaceName = new Property(6, String.class, "PlaceName", false, "PLACE_NAME");
        public final static Property Description = new Property(7, String.class, "Description", false, "DESCRIPTION");
        public final static Property InspectStatus = new Property(8, int.class, "inspectStatus", false, "INSPECT_STATUS");
        public final static Property BeginTime = new Property(9, java.util.Date.class, "BeginTime", false, "BEGIN_TIME");
        public final static Property EndTime = new Property(10, java.util.Date.class, "EndTime", false, "END_TIME");
    }

    private Query<Place> device_PlaceListQuery;
    private Query<Place> inspectDevice_PlaceListQuery;
    private Query<Place> undetect_PlaceListQuery;

    public PlaceDao(DaoConfig config) {
        super(config);
    }
    
    public PlaceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PLACE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: ID
                "\"USER_ID\" INTEGER," + // 1: UserID
                "\"LINE_ID\" INTEGER," + // 2: LineID
                "\"EQUIPMENT_ID\" INTEGER NOT NULL ," + // 3: EquipmentID
                "\"PLACE_ID\" INTEGER," + // 4: PlaceID
                "\"PLACE_CODE\" TEXT," + // 5: PlaceCode
                "\"PLACE_NAME\" TEXT," + // 6: PlaceName
                "\"DESCRIPTION\" TEXT," + // 7: Description
                "\"INSPECT_STATUS\" INTEGER NOT NULL ," + // 8: inspectStatus
                "\"BEGIN_TIME\" INTEGER," + // 9: BeginTime
                "\"END_TIME\" INTEGER);"); // 10: EndTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PLACE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Place entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        Long UserID = entity.getUserID();
        if (UserID != null) {
            stmt.bindLong(2, UserID);
        }
 
        Long LineID = entity.getLineID();
        if (LineID != null) {
            stmt.bindLong(3, LineID);
        }
        stmt.bindLong(4, entity.getEquipmentID());
 
        Long PlaceID = entity.getPlaceID();
        if (PlaceID != null) {
            stmt.bindLong(5, PlaceID);
        }
 
        String PlaceCode = entity.getPlaceCode();
        if (PlaceCode != null) {
            stmt.bindString(6, PlaceCode);
        }
 
        String PlaceName = entity.getPlaceName();
        if (PlaceName != null) {
            stmt.bindString(7, PlaceName);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(8, Description);
        }
        stmt.bindLong(9, entity.getInspectStatus());
 
        java.util.Date BeginTime = entity.getBeginTime();
        if (BeginTime != null) {
            stmt.bindLong(10, BeginTime.getTime());
        }
 
        java.util.Date EndTime = entity.getEndTime();
        if (EndTime != null) {
            stmt.bindLong(11, EndTime.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Place entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        Long UserID = entity.getUserID();
        if (UserID != null) {
            stmt.bindLong(2, UserID);
        }
 
        Long LineID = entity.getLineID();
        if (LineID != null) {
            stmt.bindLong(3, LineID);
        }
        stmt.bindLong(4, entity.getEquipmentID());
 
        Long PlaceID = entity.getPlaceID();
        if (PlaceID != null) {
            stmt.bindLong(5, PlaceID);
        }
 
        String PlaceCode = entity.getPlaceCode();
        if (PlaceCode != null) {
            stmt.bindString(6, PlaceCode);
        }
 
        String PlaceName = entity.getPlaceName();
        if (PlaceName != null) {
            stmt.bindString(7, PlaceName);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(8, Description);
        }
        stmt.bindLong(9, entity.getInspectStatus());
 
        java.util.Date BeginTime = entity.getBeginTime();
        if (BeginTime != null) {
            stmt.bindLong(10, BeginTime.getTime());
        }
 
        java.util.Date EndTime = entity.getEndTime();
        if (EndTime != null) {
            stmt.bindLong(11, EndTime.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Place readEntity(Cursor cursor, int offset) {
        Place entity = new Place( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // UserID
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // LineID
            cursor.getLong(offset + 3), // EquipmentID
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // PlaceID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // PlaceCode
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // PlaceName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Description
            cursor.getInt(offset + 8), // inspectStatus
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)), // BeginTime
            cursor.isNull(offset + 10) ? null : new java.util.Date(cursor.getLong(offset + 10)) // EndTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Place entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setLineID(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setEquipmentID(cursor.getLong(offset + 3));
        entity.setPlaceID(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setPlaceCode(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPlaceName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDescription(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setInspectStatus(cursor.getInt(offset + 8));
        entity.setBeginTime(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
        entity.setEndTime(cursor.isNull(offset + 10) ? null : new java.util.Date(cursor.getLong(offset + 10)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Place entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Place entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Place entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "PlaceList" to-many relationship of Device. */
    public List<Place> _queryDevice_PlaceList(Long EquipmentID) {
        synchronized (this) {
            if (device_PlaceListQuery == null) {
                QueryBuilder<Place> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.EquipmentID.eq(null));
                device_PlaceListQuery = queryBuilder.build();
            }
        }
        Query<Place> query = device_PlaceListQuery.forCurrentThread();
        query.setParameter(0, EquipmentID);
        return query.list();
    }

    /** Internal query to resolve the "PlaceList" to-many relationship of InspectDevice. */
    public List<Place> _queryInspectDevice_PlaceList(Long EquipmentID) {
        synchronized (this) {
            if (inspectDevice_PlaceListQuery == null) {
                QueryBuilder<Place> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.EquipmentID.eq(null));
                inspectDevice_PlaceListQuery = queryBuilder.build();
            }
        }
        Query<Place> query = inspectDevice_PlaceListQuery.forCurrentThread();
        query.setParameter(0, EquipmentID);
        return query.list();
    }

    /** Internal query to resolve the "PlaceList" to-many relationship of Undetect. */
    public List<Place> _queryUndetect_PlaceList(Long EquipmentID) {
        synchronized (this) {
            if (undetect_PlaceListQuery == null) {
                QueryBuilder<Place> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.EquipmentID.eq(null));
                undetect_PlaceListQuery = queryBuilder.build();
            }
        }
        Query<Place> query = undetect_PlaceListQuery.forCurrentThread();
        query.setParameter(0, EquipmentID);
        return query.list();
    }

}