package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.yxst.mes.database.dao.DaoSession;
import com.yxst.mes.database.dao.PlaceDao;
import com.yxst.mes.database.dao.DeviceDao;

/*
 LineID	1
    EquipmentID	1262
    EquipmentCode	"ZX.4.08.0076"
    EquipmentName	"桥式起重机"
    EquipmentModel	"QD"
 */
@Entity
public class Device {
    @Id
    private Long id;
   private Long EquipmentID;
   private String EquipmentCode;
   private String EquipmentName;
   private String EquipmentModel;
   private String RFID;
   private int inspectStatus = 0;
   private Long LineID;
   @ToMany(referencedJoinProperty = "EquipmentID")
   private List<Place> PlaceList;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 371273952)
private transient DeviceDao myDao;
@Generated(hash = 821190609)
public Device(Long id, Long EquipmentID, String EquipmentCode,
        String EquipmentName, String EquipmentModel, String RFID,
        int inspectStatus, Long LineID) {
    this.id = id;
    this.EquipmentID = EquipmentID;
    this.EquipmentCode = EquipmentCode;
    this.EquipmentName = EquipmentName;
    this.EquipmentModel = EquipmentModel;
    this.RFID = RFID;
    this.inspectStatus = inspectStatus;
    this.LineID = LineID;
}
@Generated(hash = 1469582394)
public Device() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public Long getEquipmentID() {
    return this.EquipmentID;
}
public void setEquipmentID(Long EquipmentID) {
    this.EquipmentID = EquipmentID;
}
public String getEquipmentCode() {
    return this.EquipmentCode;
}
public void setEquipmentCode(String EquipmentCode) {
    this.EquipmentCode = EquipmentCode;
}
public String getEquipmentName() {
    return this.EquipmentName;
}
public void setEquipmentName(String EquipmentName) {
    this.EquipmentName = EquipmentName;
}
public String getEquipmentModel() {
    return this.EquipmentModel;
}
public void setEquipmentModel(String EquipmentModel) {
    this.EquipmentModel = EquipmentModel;
}
public String getRFID() {
    return this.RFID;
}
public void setRFID(String RFID) {
    this.RFID = RFID;
}
public int getInspectStatus() {
    return this.inspectStatus;
}
public void setInspectStatus(int inspectStatus) {
    this.inspectStatus = inspectStatus;
}
public Long getLineID() {
    return this.LineID;
}
public void setLineID(Long LineID) {
    this.LineID = LineID;
}
/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1590188388)
public List<Place> getPlaceList() {
    if (PlaceList == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PlaceDao targetDao = daoSession.getPlaceDao();
        List<Place> PlaceListNew = targetDao._queryDevice_PlaceList(id);
        synchronized (this) {
            if (PlaceList == null) {
                PlaceList = PlaceListNew;
            }
        }
    }
    return PlaceList;
}
/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1928636522)
public synchronized void resetPlaceList() {
    PlaceList = null;
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1755220927)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getDeviceDao() : null;
}

}
