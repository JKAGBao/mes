package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created By YuanCheng on 2019/8/8 16:29
 *  PlanID	4
 * ZoneID	2
 * EquipmentID	636
 * EquipmentName	"增湿塔"
 * EquipmentModel	"Φ9000x36000mm"
 * BeginTime	"2019-08-08T00:01:00"
 * EndTime	"2019-08-08T23:59:00"
 */
@Entity
public class UnLube {
    @Id
    private Long id;
    private Long PlanID;
    private Long ZoneID;
    private String ZoneName;
    private Long UserID;
    private Long EquipmentID;
    private String EquipmentName;
    private String EquipmentModel;
    private Date BeginTime;
    private Date EndTime;
    @Generated(hash = 1402370720)
    public UnLube(Long id, Long PlanID, Long ZoneID, String ZoneName, Long UserID,
            Long EquipmentID, String EquipmentName, String EquipmentModel,
            Date BeginTime, Date EndTime) {
        this.id = id;
        this.PlanID = PlanID;
        this.ZoneID = ZoneID;
        this.ZoneName = ZoneName;
        this.UserID = UserID;
        this.EquipmentID = EquipmentID;
        this.EquipmentName = EquipmentName;
        this.EquipmentModel = EquipmentModel;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
    }
    @Generated(hash = 606791936)
    public UnLube() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPlanID() {
        return this.PlanID;
    }
    public void setPlanID(Long PlanID) {
        this.PlanID = PlanID;
    }
    public Long getZoneID() {
        return this.ZoneID;
    }
    public void setZoneID(Long ZoneID) {
        this.ZoneID = ZoneID;
    }
    public String getZoneName() {
        return this.ZoneName;
    }
    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
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
  
   
}
