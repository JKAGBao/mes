package com.yxst.mes.database.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;

@Entity
public class PlaceValue {
    @Id
    private Long ID;
    private Long UserID;
    private Long LineID;
    private Long EquipmentID;
    private Long PlaceID;
    private int inspectStatus = 0;//完成状态0未巡检完；1巡检完成
    private int uplaodStatus = 0;//完成状态0未上传；1已上传
    private Date BeginTime;
    private Date EndTime;
    @Generated(hash = 285537201)
    public PlaceValue(Long ID, Long UserID, Long LineID, Long EquipmentID,
            Long PlaceID, int inspectStatus, int uplaodStatus, Date BeginTime,
            Date EndTime) {
        this.ID = ID;
        this.UserID = UserID;
        this.LineID = LineID;
        this.EquipmentID = EquipmentID;
        this.PlaceID = PlaceID;
        this.inspectStatus = inspectStatus;
        this.uplaodStatus = uplaodStatus;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
    }
    @Generated(hash = 1620436159)
    public PlaceValue() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public Long getUserID() {
        return this.UserID;
    }
    public void setUserID(Long UserID) {
        this.UserID = UserID;
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
    public Long getPlaceID() {
        return this.PlaceID;
    }
    public void setPlaceID(Long PlaceID) {
        this.PlaceID = PlaceID;
    }
    public int getInspectStatus() {
        return this.inspectStatus;
    }
    public void setInspectStatus(int inspectStatus) {
        this.inspectStatus = inspectStatus;
    }
    public int getUplaodStatus() {
        return this.uplaodStatus;
    }
    public void setUplaodStatus(int uplaodStatus) {
        this.uplaodStatus = uplaodStatus;
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
