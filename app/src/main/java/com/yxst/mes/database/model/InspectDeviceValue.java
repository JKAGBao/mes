package com.yxst.mes.database.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;

/*
    UserID\\\":400
    LineID		1
    EquipmentID	1262
    EquipmentCode	"ZX.4.08.0076"
    EquipmentName	"桥式起重机"
    EquipmentModel	"QD"
 */
@Entity
public class InspectDeviceValue {
    @Id
   private Long Id;
   private Long UserID;
   private Long EquipmentID;
   private Long LineID;
   private Long NfcCode;
   private int inspectStatus = 0;
   private int uploadStatus = 0;
   private int showStatus = 0;
   private Date BeginTime;
   private Date EndTime;
@Generated(hash = 317981788)
public InspectDeviceValue(Long Id, Long UserID, Long EquipmentID, Long LineID,
        Long NfcCode, int inspectStatus, int uploadStatus, int showStatus,
        Date BeginTime, Date EndTime) {
    this.Id = Id;
    this.UserID = UserID;
    this.EquipmentID = EquipmentID;
    this.LineID = LineID;
    this.NfcCode = NfcCode;
    this.inspectStatus = inspectStatus;
    this.uploadStatus = uploadStatus;
    this.showStatus = showStatus;
    this.BeginTime = BeginTime;
    this.EndTime = EndTime;
}
@Generated(hash = 1456113610)
public InspectDeviceValue() {
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
public Long getEquipmentID() {
    return this.EquipmentID;
}
public void setEquipmentID(Long EquipmentID) {
    this.EquipmentID = EquipmentID;
}
public Long getLineID() {
    return this.LineID;
}
public void setLineID(Long LineID) {
    this.LineID = LineID;
}
public Long getNfcCode() {
    return this.NfcCode;
}
public void setNfcCode(Long NfcCode) {
    this.NfcCode = NfcCode;
}
public int getInspectStatus() {
    return this.inspectStatus;
}
public void setInspectStatus(int inspectStatus) {
    this.inspectStatus = inspectStatus;
}
public int getUploadStatus() {
    return this.uploadStatus;
}
public void setUploadStatus(int uploadStatus) {
    this.uploadStatus = uploadStatus;
}
public int getShowStatus() {
    return this.showStatus;
}
public void setShowStatus(int showStatus) {
    this.showStatus = showStatus;
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
