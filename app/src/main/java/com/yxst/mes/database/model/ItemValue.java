package com.yxst.mes.database.model;

import com.yxst.mes.fragment.ConfigInfo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

/*
ID	6
EquipmentID	636
InspType	2
CheckType	1
CheckContent	"振动值"
ValueType	null
Operation	5
StandardValue	"12.5"
Unit	"10"
InstrumentRate	null
RunStatus	1
Field	"CheckUnit"
PlaceCode	"1"
PlaceName	"电机"
 */
@Entity
public class ItemValue {
    @Id
    private Long Id;
    private Long UserID;
    private Long LineID;
    private Long EquipmentID;
    private Long PlaceID;
    private Long ItemID;
    private Date BeginTime;
    private Date EndTime;
    private String checkValue;
    private int inpsectStatus =ConfigInfo.ITEM_UNINSPECT_STATUS;//
    @Generated(hash = 231435869)
    public ItemValue(Long Id, Long UserID, Long LineID, Long EquipmentID,
            Long PlaceID, Long ItemID, Date BeginTime, Date EndTime,
            String checkValue, int inpsectStatus) {
        this.Id = Id;
        this.UserID = UserID;
        this.LineID = LineID;
        this.EquipmentID = EquipmentID;
        this.PlaceID = PlaceID;
        this.ItemID = ItemID;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
        this.checkValue = checkValue;
        this.inpsectStatus = inpsectStatus;
    }
    @Generated(hash = 991982422)
    public ItemValue() {
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
    public Long getItemID() {
        return this.ItemID;
    }
    public void setItemID(Long ItemID) {
        this.ItemID = ItemID;
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
    public String getCheckValue() {
        return this.checkValue;
    }
    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }
    public int getInpsectStatus() {
        return this.inpsectStatus;
    }
    public void setInpsectStatus(int inpsectStatus) {
        this.inpsectStatus = inpsectStatus;
    }
   
   
}
