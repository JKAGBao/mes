package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/*
ID	1
InspectionType	1
LineCode	"1001"
LineName	"专业巡检一"
 */
@Entity
public class InspectLine {
    @Id
    private Long ID;
    private Long UserID;
    private Long LineID;
    private String LineName;
    private String InspectionTypeName;
    private Date BeginTime;
    private Date EndTime;
    @Generated(hash = 622454729)
    public InspectLine(Long ID, Long UserID, Long LineID, String LineName,
            String InspectionTypeName, Date BeginTime, Date EndTime) {
        this.ID = ID;
        this.UserID = UserID;
        this.LineID = LineID;
        this.LineName = LineName;
        this.InspectionTypeName = InspectionTypeName;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
    }
    @Generated(hash = 829788501)
    public InspectLine() {
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
    public String getLineName() {
        return this.LineName;
    }
    public void setLineName(String LineName) {
        this.LineName = LineName;
    }
    public String getInspectionTypeName() {
        return this.InspectionTypeName;
    }
    public void setInspectionTypeName(String InspectionTypeName) {
        this.InspectionTypeName = InspectionTypeName;
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
