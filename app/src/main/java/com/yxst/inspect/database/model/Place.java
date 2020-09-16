package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class
Place {
    @Id
    private Long ID;
    private Long UserID;
    private Long LineID;
    @NotNull
    private Long EquipmentID;
    private Long PlaceID;
    private String PlaceCode;
    private String PlaceName;
    private String Description;
    private int CheckStatus;//完成状态0未巡检完；1巡检完成
    private Date BeginTime;
    private Date EndTime;
//    @ToMany(referencedJoinProperty = "PlaceID" )
//    private List<Item> ItemList;
    @Generated(hash = 215383151)
    public Place(Long ID, Long UserID, Long LineID, @NotNull Long EquipmentID,
            Long PlaceID, String PlaceCode, String PlaceName, String Description,
            int CheckStatus, Date BeginTime, Date EndTime) {
        this.ID = ID;
        this.UserID = UserID;
        this.LineID = LineID;
        this.EquipmentID = EquipmentID;
        this.PlaceID = PlaceID;
        this.PlaceCode = PlaceCode;
        this.PlaceName = PlaceName;
        this.Description = Description;
        this.CheckStatus = CheckStatus;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
    }
    @Generated(hash = 1170019414)
    public Place() {
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
    public String getPlaceCode() {
        return this.PlaceCode;
    }
    public void setPlaceCode(String PlaceCode) {
        this.PlaceCode = PlaceCode;
    }
    public String getPlaceName() {
        return this.PlaceName;
    }
    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }
    public String getDescription() {
        return this.Description;
    }
    public void setDescription(String Description) {
        this.Description = Description;
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
