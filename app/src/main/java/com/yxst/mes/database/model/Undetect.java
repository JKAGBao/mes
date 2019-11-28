package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.yxst.mes.database.dao.DaoSession;
import com.yxst.mes.database.dao.PlaceDao;
import com.yxst.mes.database.dao.UndetectDao;

/**
 * PlanID	2
 * LineID	1
 * EquipmentID	650
 * EquipmentCode	"ZX.2.02.0015"
 * EquipmentName	"窑尾废气风机"
 * ZoneName	"生料粉磨
 * Created By YuanCheng on 2019/6/5 15:48
 */
@Entity
public class Undetect {
        @Id
        private Long id;
        private Long LineID;
        private Long EquipmentID;
        private String EquipmentCode;
        private String EquipmentName;
        private String ZoneName;//区
        private Date BeginTime;
        private Date EndTime;
        @ToMany(referencedJoinProperty = "EquipmentID")
        private List<Place> PlaceList;
        /** Used to resolve relations */
        /** Used to resolve relations */
        @Generated(hash = 2040040024)
        private transient DaoSession daoSession;
        /** Used for active entity operations. */
        @Generated(hash = 508624948)
        private transient UndetectDao myDao;
        @Generated(hash = 951940390)
        public Undetect(Long id, Long LineID, Long EquipmentID, String EquipmentCode,
                String EquipmentName, String ZoneName, Date BeginTime, Date EndTime) {
            this.id = id;
            this.LineID = LineID;
            this.EquipmentID = EquipmentID;
            this.EquipmentCode = EquipmentCode;
            this.EquipmentName = EquipmentName;
            this.ZoneName = ZoneName;
            this.BeginTime = BeginTime;
            this.EndTime = EndTime;
        }
        @Generated(hash = 66526915)
        public Undetect() {
        }
        public Long getId() {
            return this.id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public Long getLineID() {
            return this.LineID;
        }
        public void setLineID(Long LineID) {
            this.LineID = LineID;
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
        public String getZoneName() {
            return this.ZoneName;
        }
        public void setZoneName(String ZoneName) {
            this.ZoneName = ZoneName;
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
        /**
         * To-many relationship, resolved on first access (and after reset).
         * Changes to to-many relations are not persisted, make changes to the target entity.
         */
        @Generated(hash = 749889186)
        public List<Place> getPlaceList() {
            if (PlaceList == null) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                    throw new DaoException("Entity is detached from DAO context");
                }
                PlaceDao targetDao = daoSession.getPlaceDao();
                List<Place> PlaceListNew = targetDao._queryUndetect_PlaceList(id);
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
        @Generated(hash = 41610700)
        public void __setDaoSession(DaoSession daoSession) {
            this.daoSession = daoSession;
            myDao = daoSession != null ? daoSession.getUndetectDao() : null;
        }
        

}
