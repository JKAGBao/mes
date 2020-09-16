package com.yxst.inspect.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.yxst.inspect.database.model.InspectLine;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "INSPECT_LINE".
*/
public class InspectLineDao extends AbstractDao<InspectLine, Long> {

    public static final String TABLENAME = "INSPECT_LINE";

    /**
     * Properties of entity InspectLine.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "_id");
        public final static Property UserID = new Property(1, Long.class, "UserID", false, "USER_ID");
        public final static Property LineID = new Property(2, Long.class, "LineID", false, "LINE_ID");
        public final static Property LineName = new Property(3, String.class, "LineName", false, "LINE_NAME");
        public final static Property InspectionTypeName = new Property(4, String.class, "InspectionTypeName", false, "INSPECTION_TYPE_NAME");
        public final static Property BeginTime = new Property(5, java.util.Date.class, "BeginTime", false, "BEGIN_TIME");
        public final static Property EndTime = new Property(6, java.util.Date.class, "EndTime", false, "END_TIME");
    }


    public InspectLineDao(DaoConfig config) {
        super(config);
    }
    
    public InspectLineDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"INSPECT_LINE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: ID
                "\"USER_ID\" INTEGER," + // 1: UserID
                "\"LINE_ID\" INTEGER," + // 2: LineID
                "\"LINE_NAME\" TEXT," + // 3: LineName
                "\"INSPECTION_TYPE_NAME\" TEXT," + // 4: InspectionTypeName
                "\"BEGIN_TIME\" INTEGER," + // 5: BeginTime
                "\"END_TIME\" INTEGER);"); // 6: EndTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"INSPECT_LINE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, InspectLine entity) {
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
 
        String LineName = entity.getLineName();
        if (LineName != null) {
            stmt.bindString(4, LineName);
        }
 
        String InspectionTypeName = entity.getInspectionTypeName();
        if (InspectionTypeName != null) {
            stmt.bindString(5, InspectionTypeName);
        }
 
        java.util.Date BeginTime = entity.getBeginTime();
        if (BeginTime != null) {
            stmt.bindLong(6, BeginTime.getTime());
        }
 
        java.util.Date EndTime = entity.getEndTime();
        if (EndTime != null) {
            stmt.bindLong(7, EndTime.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, InspectLine entity) {
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
 
        String LineName = entity.getLineName();
        if (LineName != null) {
            stmt.bindString(4, LineName);
        }
 
        String InspectionTypeName = entity.getInspectionTypeName();
        if (InspectionTypeName != null) {
            stmt.bindString(5, InspectionTypeName);
        }
 
        java.util.Date BeginTime = entity.getBeginTime();
        if (BeginTime != null) {
            stmt.bindLong(6, BeginTime.getTime());
        }
 
        java.util.Date EndTime = entity.getEndTime();
        if (EndTime != null) {
            stmt.bindLong(7, EndTime.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public InspectLine readEntity(Cursor cursor, int offset) {
        InspectLine entity = new InspectLine( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // UserID
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // LineID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // LineName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // InspectionTypeName
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // BeginTime
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)) // EndTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, InspectLine entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setLineID(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setLineName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setInspectionTypeName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBeginTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setEndTime(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(InspectLine entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(InspectLine entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(InspectLine entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}