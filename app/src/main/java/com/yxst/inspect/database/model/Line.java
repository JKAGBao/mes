package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/*
ID	1
InspectionType	1
LineCode	"1001"
LineName	"专业巡检一"
 */
@Entity
public class Line {

    @Id
    private Long ID;
    private int InspectionType;
    private String InspectionTypeName;
    private String LineCode;
    private String LineName;
    private String RFID;
    @Generated(hash = 2077308530)
    public Line(Long ID, int InspectionType, String InspectionTypeName,
            String LineCode, String LineName, String RFID) {
        this.ID = ID;
        this.InspectionType = InspectionType;
        this.InspectionTypeName = InspectionTypeName;
        this.LineCode = LineCode;
        this.LineName = LineName;
        this.RFID = RFID;
    }
    @Generated(hash = 1133511183)
    public Line() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public int getInspectionType() {
        return this.InspectionType;
    }
    public void setInspectionType(int InspectionType) {
        this.InspectionType = InspectionType;
    }
    public String getInspectionTypeName() {
        return this.InspectionTypeName;
    }
    public void setInspectionTypeName(String InspectionTypeName) {
        this.InspectionTypeName = InspectionTypeName;
    }
    public String getLineCode() {
        return this.LineCode;
    }
    public void setLineCode(String LineCode) {
        this.LineCode = LineCode;
    }
    public String getLineName() {
        return this.LineName;
    }
    public void setLineName(String LineName) {
        this.LineName = LineName;
    }
    public String getRFID() {
        return this.RFID;
    }
    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    
}
