package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * InspectionItemID	5
 * GradeCode	1
 * MinCheckValue	13.5
 * MaxCheckValue	999
 *
 * Created By YuanCheng on 2019/6/14 15:39
 */
@Entity
public class Grade {
    @Id
    private Long id;
    private Long InspectionItemID;
    private int GradeCode;
    private float MinCheckValue;
    private float MaxCheckValue;
    @Generated(hash = 1247251047)
    public Grade(Long id, Long InspectionItemID, int GradeCode, float MinCheckValue,
            float MaxCheckValue) {
        this.id = id;
        this.InspectionItemID = InspectionItemID;
        this.GradeCode = GradeCode;
        this.MinCheckValue = MinCheckValue;
        this.MaxCheckValue = MaxCheckValue;
    }
    @Generated(hash = 2042976393)
    public Grade() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getInspectionItemID() {
        return this.InspectionItemID;
    }
    public void setInspectionItemID(Long InspectionItemID) {
        this.InspectionItemID = InspectionItemID;
    }
    public int getGradeCode() {
        return this.GradeCode;
    }
    public void setGradeCode(int GradeCode) {
        this.GradeCode = GradeCode;
    }
    public float getMinCheckValue() {
        return this.MinCheckValue;
    }
    public void setMinCheckValue(float MinCheckValue) {
        this.MinCheckValue = MinCheckValue;
    }
    public float getMaxCheckValue() {
        return this.MaxCheckValue;
    }
    public void setMaxCheckValue(float MaxCheckValue) {
        this.MaxCheckValue = MaxCheckValue;
    }
   
    

}
