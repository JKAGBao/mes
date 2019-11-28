package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/*
ID	1
RFIDid	"1423433867"
DataBaseName	"123"
SampleName	"1#出磨水泥"
BindTime	"2019-10-08T09:25:53.087"
 */
@Entity
public class Sample {
    @Id
    private Long id;
    private Long ID;
    private String RFIDid;
    private String DataBaseName;
    private String SampleName;
    private Date BindTime;
    @Generated(hash = 1040570923)
    public Sample(Long id, Long ID, String RFIDid, String DataBaseName,
            String SampleName, Date BindTime) {
        this.id = id;
        this.ID = ID;
        this.RFIDid = RFIDid;
        this.DataBaseName = DataBaseName;
        this.SampleName = SampleName;
        this.BindTime = BindTime;
    }
    @Generated(hash = 976859954)
    public Sample() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getRFIDid() {
        return this.RFIDid;
    }
    public void setRFIDid(String RFIDid) {
        this.RFIDid = RFIDid;
    }
    public String getDataBaseName() {
        return this.DataBaseName;
    }
    public void setDataBaseName(String DataBaseName) {
        this.DataBaseName = DataBaseName;
    }
    public String getSampleName() {
        return this.SampleName;
    }
    public void setSampleName(String SampleName) {
        this.SampleName = SampleName;
    }
    public Date getBindTime() {
        return this.BindTime;
    }
    public void setBindTime(Date BindTime) {
        this.BindTime = BindTime;
    }


}
