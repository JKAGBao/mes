package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created By YuanCheng on 2019/6/10 19:44
 */
@Entity
public class Cycle {
    @Id
    private Long ID;
    @NotNull
    private Long PlanID;
    private String BeginTime;
    private String EndTime;
    @Generated(hash = 651797845)
    public Cycle(Long ID, @NotNull Long PlanID, String BeginTime, String EndTime) {
        this.ID = ID;
        this.PlanID = PlanID;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
    }
    @Generated(hash = 1618046672)
    public Cycle() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public Long getPlanID() {
        return this.PlanID;
    }
    public void setPlanID(Long PlanID) {
        this.PlanID = PlanID;
    }
    public String getBeginTime() {
        return this.BeginTime;
    }
    public void setBeginTime(String BeginTime) {
        this.BeginTime = BeginTime;
    }
    public String getEndTime() {
        return this.EndTime;
    }
    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }
    
}
