package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.yxst.mes.database.dao.DaoSession;
import com.yxst.mes.database.dao.CycleDao;
import com.yxst.mes.database.dao.PlanDao;

/**
 * LineID	1
 * PlanID	2
 * PlanName	"专业巡检一专业巡检1"
 * EquipmentID	null
 * InspectionCycle	1
 * InspectionCycleUnit	2
 * InspectionType	2
 * PlanType	1
 * BaseDate	"2019-05-13T00:00:00"
 * InspectionClass	1
 * Created By YuanCheng on 2019/6/10 19:40
 */
@Entity
public class Plan {

    private Long LineID;
    @Id
    private Long PlanID;
    private String PlanName;
    private Long EquipmentID;
    private int InspectionCycle;
    private int InspectionCycleUnit;
    private int InspectionType;//1普通巡检，2.专业巡检
    private int PlanType;//1.单次执行 2.重复执行
    private Date BaseDate;
    private int InspectionClass;//1线路巡检，2设备巡检
    @ToMany(referencedJoinProperty = "PlanID")
    private List<Cycle> CycleList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 317818512)
    private transient PlanDao myDao;
    @Generated(hash = 153635719)
    public Plan(Long LineID, Long PlanID, String PlanName, Long EquipmentID,
            int InspectionCycle, int InspectionCycleUnit, int InspectionType,
            int PlanType, Date BaseDate, int InspectionClass) {
        this.LineID = LineID;
        this.PlanID = PlanID;
        this.PlanName = PlanName;
        this.EquipmentID = EquipmentID;
        this.InspectionCycle = InspectionCycle;
        this.InspectionCycleUnit = InspectionCycleUnit;
        this.InspectionType = InspectionType;
        this.PlanType = PlanType;
        this.BaseDate = BaseDate;
        this.InspectionClass = InspectionClass;
    }
    @Generated(hash = 592612124)
    public Plan() {
    }
    public Long getLineID() {
        return this.LineID;
    }
    public void setLineID(Long LineID) {
        this.LineID = LineID;
    }
    public Long getPlanID() {
        return this.PlanID;
    }
    public void setPlanID(Long PlanID) {
        this.PlanID = PlanID;
    }
    public String getPlanName() {
        return this.PlanName;
    }
    public void setPlanName(String PlanName) {
        this.PlanName = PlanName;
    }
    public Long getEquipmentID() {
        return this.EquipmentID;
    }
    public void setEquipmentID(Long EquipmentID) {
        this.EquipmentID = EquipmentID;
    }
    public int getInspectionCycle() {
        return this.InspectionCycle;
    }
    public void setInspectionCycle(int InspectionCycle) {
        this.InspectionCycle = InspectionCycle;
    }
    public int getInspectionCycleUnit() {
        return this.InspectionCycleUnit;
    }
    public void setInspectionCycleUnit(int InspectionCycleUnit) {
        this.InspectionCycleUnit = InspectionCycleUnit;
    }
    public int getInspectionType() {
        return this.InspectionType;
    }
    public void setInspectionType(int InspectionType) {
        this.InspectionType = InspectionType;
    }
    public int getPlanType() {
        return this.PlanType;
    }
    public void setPlanType(int PlanType) {
        this.PlanType = PlanType;
    }
    public Date getBaseDate() {
        return this.BaseDate;
    }
    public void setBaseDate(Date BaseDate) {
        this.BaseDate = BaseDate;
    }
    public int getInspectionClass() {
        return this.InspectionClass;
    }
    public void setInspectionClass(int InspectionClass) {
        this.InspectionClass = InspectionClass;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1101875016)
    public List<Cycle> getCycleList() {
        if (CycleList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CycleDao targetDao = daoSession.getCycleDao();
            List<Cycle> CycleListNew = targetDao._queryPlan_CycleList(PlanID);
            synchronized (this) {
                if (CycleList == null) {
                    CycleList = CycleListNew;
                }
            }
        }
        return CycleList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1241010702)
    public synchronized void resetCycleList() {
        CycleList = null;
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
    @Generated(hash = 2098727688)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanDao() : null;
    }

}
