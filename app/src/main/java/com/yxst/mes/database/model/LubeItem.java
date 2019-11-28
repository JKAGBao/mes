package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created By YuanCheng on 2019/8/8 18:38
 * PlanID	4
 * ZoneID	2
 * EquipmentID	636
 * LubricationItemID	10
 * FatID	3
 * BeginTime	"2019-08-08T00:01:00"
 * EndTime	"2019-08-08T23:59:00"
 * ZoneName	"润滑线路1"
 * FatName	"00号减速机润滑脂"
 * FatModel	"00号减速机润滑脂"
 * LubPlace	"轴承bearing"
 * LubTypeName	"浸油"
 * FirstfatNum	20
 * PerAddNum	3
 * Unit	"L"

 */
@Entity
public class LubeItem {
    @Id
    private Long id;
    private Long UserID;
    private Long PlanID;
    private Long ZoneID;
    private Long EquipmentID;
    private Long LubricationItemID;
    private Long FatID;
    private Date BeginTime;
    private Date EndTime;
    private String ZoneName;
    private String FatName;
    private String FatModel;
    private String LubPlace;
    private String LubTypeName;
    private int FirstfatNum;
    private int PerAddNum;
    private int RealNum;
    private String Unit;
    @Generated(hash = 539821517)
    public LubeItem(Long id, Long UserID, Long PlanID, Long ZoneID,
            Long EquipmentID, Long LubricationItemID, Long FatID, Date BeginTime,
            Date EndTime, String ZoneName, String FatName, String FatModel,
            String LubPlace, String LubTypeName, int FirstfatNum, int PerAddNum,
            int RealNum, String Unit) {
        this.id = id;
        this.UserID = UserID;
        this.PlanID = PlanID;
        this.ZoneID = ZoneID;
        this.EquipmentID = EquipmentID;
        this.LubricationItemID = LubricationItemID;
        this.FatID = FatID;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
        this.ZoneName = ZoneName;
        this.FatName = FatName;
        this.FatModel = FatModel;
        this.LubPlace = LubPlace;
        this.LubTypeName = LubTypeName;
        this.FirstfatNum = FirstfatNum;
        this.PerAddNum = PerAddNum;
        this.RealNum = RealNum;
        this.Unit = Unit;
    }
    @Generated(hash = 1896598386)
    public LubeItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserID() {
        return this.UserID;
    }
    public void setUserID(Long UserID) {
        this.UserID = UserID;
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
    public Long getEquipmentID() {
        return this.EquipmentID;
    }
    public void setEquipmentID(Long EquipmentID) {
        this.EquipmentID = EquipmentID;
    }
    public Long getLubricationItemID() {
        return this.LubricationItemID;
    }
    public void setLubricationItemID(Long LubricationItemID) {
        this.LubricationItemID = LubricationItemID;
    }
    public Long getFatID() {
        return this.FatID;
    }
    public void setFatID(Long FatID) {
        this.FatID = FatID;
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
    public String getZoneName() {
        return this.ZoneName;
    }
    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }
    public String getFatName() {
        return this.FatName;
    }
    public void setFatName(String FatName) {
        this.FatName = FatName;
    }
    public String getFatModel() {
        return this.FatModel;
    }
    public void setFatModel(String FatModel) {
        this.FatModel = FatModel;
    }
    public String getLubPlace() {
        return this.LubPlace;
    }
    public void setLubPlace(String LubPlace) {
        this.LubPlace = LubPlace;
    }
    public String getLubTypeName() {
        return this.LubTypeName;
    }
    public void setLubTypeName(String LubTypeName) {
        this.LubTypeName = LubTypeName;
    }
    public int getFirstfatNum() {
        return this.FirstfatNum;
    }
    public void setFirstfatNum(int FirstfatNum) {
        this.FirstfatNum = FirstfatNum;
    }
    public int getPerAddNum() {
        return this.PerAddNum;
    }
    public void setPerAddNum(int PerAddNum) {
        this.PerAddNum = PerAddNum;
    }
    public int getRealNum() {
        return this.RealNum;
    }
    public void setRealNum(int RealNum) {
        this.RealNum = RealNum;
    }
    public String getUnit() {
        return this.Unit;
    }
    public void setUnit(String Unit) {
        this.Unit = Unit;
    }
   
    
}
