package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
public class Item {
    @Id
    private Long Id;
    private Long UserID;
    private Long LineID;
    private Long EquipmentID;
    private Long ItemID;
    private int InspType;
    private int CheckType;//1
    private String CheckContent;
    private Long PlanID;
    private int ValueType;
    private int Operation;
    private String StandardValue;
    private int Unit;
    private float InstrumentRate;
    private int RunStatus;
    private int inpsectStatus;//
    private Long PlaceID;//	"1"
    private String PlaceName;//"电机"
    private String CheckValue;
    private int CheckStatus;//record.setCheckStatus(3);//1待2漏3已巡检
    private Date BeginTime;
    private Date EndTime;
    @Generated(hash = 1336654452)
    public Item(Long Id, Long UserID, Long LineID, Long EquipmentID, Long ItemID,
            int InspType, int CheckType, String CheckContent, Long PlanID,
            int ValueType, int Operation, String StandardValue, int Unit,
            float InstrumentRate, int RunStatus, int inpsectStatus, Long PlaceID,
            String PlaceName, String CheckValue, int CheckStatus, Date BeginTime,
            Date EndTime) {
        this.Id = Id;
        this.UserID = UserID;
        this.LineID = LineID;
        this.EquipmentID = EquipmentID;
        this.ItemID = ItemID;
        this.InspType = InspType;
        this.CheckType = CheckType;
        this.CheckContent = CheckContent;
        this.PlanID = PlanID;
        this.ValueType = ValueType;
        this.Operation = Operation;
        this.StandardValue = StandardValue;
        this.Unit = Unit;
        this.InstrumentRate = InstrumentRate;
        this.RunStatus = RunStatus;
        this.inpsectStatus = inpsectStatus;
        this.PlaceID = PlaceID;
        this.PlaceName = PlaceName;
        this.CheckValue = CheckValue;
        this.CheckStatus = CheckStatus;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
    }
    @Generated(hash = 1470900980)
    public Item() {
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
    public Long getItemID() {
        return this.ItemID;
    }
    public void setItemID(Long ItemID) {
        this.ItemID = ItemID;
    }
    public int getInspType() {
        return this.InspType;
    }
    public void setInspType(int InspType) {
        this.InspType = InspType;
    }
    public int getCheckType() {
        return this.CheckType;
    }
    public void setCheckType(int CheckType) {
        this.CheckType = CheckType;
    }
    public String getCheckContent() {
        return this.CheckContent;
    }
    public void setCheckContent(String CheckContent) {
        this.CheckContent = CheckContent;
    }
    public Long getPlanID() {
        return this.PlanID;
    }
    public void setPlanID(Long PlanID) {
        this.PlanID = PlanID;
    }
    public int getValueType() {
        return this.ValueType;
    }
    public void setValueType(int ValueType) {
        this.ValueType = ValueType;
    }
    public int getOperation() {
        return this.Operation;
    }
    public void setOperation(int Operation) {
        this.Operation = Operation;
    }
    public String getStandardValue() {
        return this.StandardValue;
    }
    public void setStandardValue(String StandardValue) {
        this.StandardValue = StandardValue;
    }
    public int getUnit() {
        return this.Unit;
    }
    public void setUnit(int Unit) {
        this.Unit = Unit;
    }
    public float getInstrumentRate() {
        return this.InstrumentRate;
    }
    public void setInstrumentRate(float InstrumentRate) {
        this.InstrumentRate = InstrumentRate;
    }
    public int getRunStatus() {
        return this.RunStatus;
    }
    public void setRunStatus(int RunStatus) {
        this.RunStatus = RunStatus;
    }
    public int getInpsectStatus() {
        return this.inpsectStatus;
    }
    public void setInpsectStatus(int inpsectStatus) {
        this.inpsectStatus = inpsectStatus;
    }
    public Long getPlaceID() {
        return this.PlaceID;
    }
    public void setPlaceID(Long PlaceID) {
        this.PlaceID = PlaceID;
    }
    public String getPlaceName() {
        return this.PlaceName;
    }
    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }
    public String getCheckValue() {
        return this.CheckValue;
    }
    public void setCheckValue(String CheckValue) {
        this.CheckValue = CheckValue;
    }
    public int getCheckStatus() {
        return this.CheckStatus;
    }
    public void setCheckStatus(int CheckStatus) {
        this.CheckStatus = CheckStatus;
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
