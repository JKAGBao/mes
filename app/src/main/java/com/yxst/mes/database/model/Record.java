package com.yxst.mes.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Record {
    @Id
    private Long id;
    private String CheckValue;
    private int CheckConclusion;
    private String CheckUser;//实际检测人
    private Long UserId;
    private String Remark;
    private String InputDate;//检测日期
    private String DangerTitle;//巡检项名称
    private Long EquipmentID;
    private Long PlanId;
    private Long LineID;
    private Long InspectionItemID;
    private String BeginTime;//开始检测时间
    private String EndTime;//
    //以下未用到
    private int CheckStatus;//2表示已上传
    private Date InspectionTime;//巡检时间
    private String UpdateUser;
    private Date UpdateTime;//上传时间
    private int ClassCode;//巡检班次
    private String CreateUser;
    private Date CreateTime;
    @Generated(hash = 1551551822)
    public Record(Long id, String CheckValue, int CheckConclusion, String CheckUser,
            Long UserId, String Remark, String InputDate, String DangerTitle,
            Long EquipmentID, Long PlanId, Long LineID, Long InspectionItemID,
            String BeginTime, String EndTime, int CheckStatus, Date InspectionTime,
            String UpdateUser, Date UpdateTime, int ClassCode, String CreateUser,
            Date CreateTime) {
        this.id = id;
        this.CheckValue = CheckValue;
        this.CheckConclusion = CheckConclusion;
        this.CheckUser = CheckUser;
        this.UserId = UserId;
        this.Remark = Remark;
        this.InputDate = InputDate;
        this.DangerTitle = DangerTitle;
        this.EquipmentID = EquipmentID;
        this.PlanId = PlanId;
        this.LineID = LineID;
        this.InspectionItemID = InspectionItemID;
        this.BeginTime = BeginTime;
        this.EndTime = EndTime;
        this.CheckStatus = CheckStatus;
        this.InspectionTime = InspectionTime;
        this.UpdateUser = UpdateUser;
        this.UpdateTime = UpdateTime;
        this.ClassCode = ClassCode;
        this.CreateUser = CreateUser;
        this.CreateTime = CreateTime;
    }
    @Generated(hash = 477726293)
    public Record() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCheckValue() {
        return this.CheckValue;
    }
    public void setCheckValue(String CheckValue) {
        this.CheckValue = CheckValue;
    }
    public int getCheckConclusion() {
        return this.CheckConclusion;
    }
    public void setCheckConclusion(int CheckConclusion) {
        this.CheckConclusion = CheckConclusion;
    }
    public String getCheckUser() {
        return this.CheckUser;
    }
    public void setCheckUser(String CheckUser) {
        this.CheckUser = CheckUser;
    }
    public Long getUserId() {
        return this.UserId;
    }
    public void setUserId(Long UserId) {
        this.UserId = UserId;
    }
    public String getRemark() {
        return this.Remark;
    }
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
    public String getInputDate() {
        return this.InputDate;
    }
    public void setInputDate(String InputDate) {
        this.InputDate = InputDate;
    }
    public String getDangerTitle() {
        return this.DangerTitle;
    }
    public void setDangerTitle(String DangerTitle) {
        this.DangerTitle = DangerTitle;
    }
    public Long getEquipmentID() {
        return this.EquipmentID;
    }
    public void setEquipmentID(Long EquipmentID) {
        this.EquipmentID = EquipmentID;
    }
    public Long getPlanId() {
        return this.PlanId;
    }
    public void setPlanId(Long PlanId) {
        this.PlanId = PlanId;
    }
    public Long getLineID() {
        return this.LineID;
    }
    public void setLineID(Long LineID) {
        this.LineID = LineID;
    }
    public Long getInspectionItemID() {
        return this.InspectionItemID;
    }
    public void setInspectionItemID(Long InspectionItemID) {
        this.InspectionItemID = InspectionItemID;
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
    public int getCheckStatus() {
        return this.CheckStatus;
    }
    public void setCheckStatus(int CheckStatus) {
        this.CheckStatus = CheckStatus;
    }
    public Date getInspectionTime() {
        return this.InspectionTime;
    }
    public void setInspectionTime(Date InspectionTime) {
        this.InspectionTime = InspectionTime;
    }
    public String getUpdateUser() {
        return this.UpdateUser;
    }
    public void setUpdateUser(String UpdateUser) {
        this.UpdateUser = UpdateUser;
    }
    public Date getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(Date UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public int getClassCode() {
        return this.ClassCode;
    }
    public void setClassCode(int ClassCode) {
        this.ClassCode = ClassCode;
    }
    public String getCreateUser() {
        return this.CreateUser;
    }
    public void setCreateUser(String CreateUser) {
        this.CreateUser = CreateUser;
    }
    public Date getCreateTime() {
        return this.CreateTime;
    }
    public void setCreateTime(Date CreateTime) {
        this.CreateTime = CreateTime;
    }
   
}
