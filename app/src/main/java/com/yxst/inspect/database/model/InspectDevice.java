package com.yxst.inspect.database.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import java.util.Date;
import java.util.List;
import com.yxst.inspect.database.dao.DaoSession;
import com.yxst.inspect.database.dao.PlaceDao;
import com.yxst.inspect.database.dao.InspectDeviceDao;

/*
    UserID\\\":400
    LineID		1
    EquipmentID	1262
    EquipmentCode	"ZX.4.08.0076"
    EquipmentName	"桥式起重机"
    EquipmentModel	"QD"
 */
@Entity
public class InspectDevice {
    @Id
   private Long Id;
   private Long UserID;
   private Long EquipmentID;
    @OrderBy
    private Long LineID;
   private String EquipmentCode;
   private String EquipmentName;
   private String EquipmentModel;
   private String RFID;
   private int CheckStatus;//巡检状态：完成巡检2，
   private int uploadStatus = 0;
   private Date startInpectDate;
   private Date finishInpectDate;
   private Date BeginTime;
   private Date EndTime;
   private int RunStates;
   @ToMany(referencedJoinProperty = "EquipmentID")
   private List<Place> PlaceList;
/** Used to resolve relations */
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 818775346)
private transient InspectDeviceDao myDao;
@Generated(hash = 2046606107)
public InspectDevice(Long Id, Long UserID, Long EquipmentID, Long LineID,
        String EquipmentCode, String EquipmentName, String EquipmentModel,
        String RFID, int CheckStatus, int uploadStatus, Date startInpectDate,
        Date finishInpectDate, Date BeginTime, Date EndTime, int RunStates) {
    this.Id = Id;
    this.UserID = UserID;
    this.EquipmentID = EquipmentID;
    this.LineID = LineID;
    this.EquipmentCode = EquipmentCode;
    this.EquipmentName = EquipmentName;
    this.EquipmentModel = EquipmentModel;
    this.RFID = RFID;
    this.CheckStatus = CheckStatus;
    this.uploadStatus = uploadStatus;
    this.startInpectDate = startInpectDate;
    this.finishInpectDate = finishInpectDate;
    this.BeginTime = BeginTime;
    this.EndTime = EndTime;
    this.RunStates = RunStates;
}
@Generated(hash = 1207461801)
public InspectDevice() {
}
public Long getId() {
    return this.Id;
}
public void setId(Long Id) {
    this.Id = Id;
}
public Long getUserID() {
    return this.UserID;
}
public void setUserID(Long UserID) {
    this.UserID = UserID;
}
public Long getEquipmentID() {
    return this.EquipmentID;
}
public void setEquipmentID(Long EquipmentID) {
    this.EquipmentID = EquipmentID;
}
public Long getLineID() {
    return this.LineID;
}
public void setLineID(Long LineID) {
    this.LineID = LineID;
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
public int getCheckStatus() {
    return this.CheckStatus;
}
public void setCheckStatus(int CheckStatus) {
    this.CheckStatus = CheckStatus;
}
public int getUploadStatus() {
    return this.uploadStatus;
}
public void setUploadStatus(int uploadStatus) {
    this.uploadStatus = uploadStatus;
}
public Date getStartInpectDate() {
    return this.startInpectDate;
}
public void setStartInpectDate(Date startInpectDate) {
    this.startInpectDate = startInpectDate;
}
public Date getFinishInpectDate() {
    return this.finishInpectDate;
}
public void setFinishInpectDate(Date finishInpectDate) {
    this.finishInpectDate = finishInpectDate;
}
public Date getBeginTime() {
    return this.BeginTime;
}
public void setBeginTime(Date BeginTime) {
    this.BeginTime = BeginTime;
}
public Date getEndTime() {
    return this.EndTime;
}
public void setEndTime(Date EndTime) {
    this.EndTime = EndTime;
}
public int getRunStates() {
    return this.RunStates;
}
public void setRunStates(int RunStates) {
    this.RunStates = RunStates;
}
/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 2007575969)
public List<Place> getPlaceList() {
    if (PlaceList == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PlaceDao targetDao = daoSession.getPlaceDao();
        List<Place> PlaceListNew = targetDao._queryInspectDevice_PlaceList(Id);
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
@Generated(hash = 300594551)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getInspectDeviceDao() : null;
}



}
