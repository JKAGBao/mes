package project.bridgetek.com.applib.main.bean;

import java.io.Serializable;

/**
 * Created by bridge on 18-6-27.
 */

public class CheckItem implements Serializable {
    String Result_ID;
    String Task_ID;
    String LabelID;
    String MobjectCode;
    String MobjectName;
    String CheckItem_ID;
    String Start_TM;
    String Complete_TM;
    String UserID;
    String UserName;
    String ResultValue;
    Integer Rate;
    String MObjectStatus;
    String Exception_YN;
    String ExceptionID;
    String Memo_TX;
    Integer Time_NR;
    String GroupName;
    String PDADevice;
    String ExceptionTransfer_YN;
    boolean submit;
    String TaskItemID;
    String TaskType;
    String LineId;
    String UserCode;
    String UserPostId;
    String ExceptionLevel;
    String ShiftName;
    String VibFeatures;
    Integer Points;
    String SignalType;

    public CheckItem() {
    }

    public CheckItem(String result_ID, String task_ID, String labelID, String mobjectCode, String mobjectName, String checkItem_ID, String start_TM, String complete_TM, String userID, String userName, String resultValue, Integer rate, String MObjectStatus, String exception_YN, String exceptionID, String memo_TX, Integer time_NR, String groupName, String PDADevice, String exceptionTransfer_YN, boolean submit, String taskItemID, String taskType, String lineId, String userCode, String userPostId, String exceptionLevel, String shiftName, String vibFeatures, Integer points, String signalType) {
        Result_ID = result_ID;
        Task_ID = task_ID;
        LabelID = labelID;
        MobjectCode = mobjectCode;
        MobjectName = mobjectName;
        CheckItem_ID = checkItem_ID;
        Start_TM = start_TM;
        Complete_TM = complete_TM;
        UserID = userID;
        UserName = userName;
        ResultValue = resultValue;
        Rate = rate;
        this.MObjectStatus = MObjectStatus;
        Exception_YN = exception_YN;
        ExceptionID = exceptionID;
        Memo_TX = memo_TX;
        Time_NR = time_NR;
        GroupName = groupName;
        this.PDADevice = PDADevice;
        ExceptionTransfer_YN = exceptionTransfer_YN;
        this.submit = submit;
        TaskItemID = taskItemID;
        TaskType = taskType;
        LineId = lineId;
        UserCode = userCode;
        UserPostId = userPostId;
        ExceptionLevel = exceptionLevel;
        ShiftName = shiftName;
        VibFeatures = vibFeatures;
        this.Points = points;
        this.SignalType = signalType;
    }

    public Integer getPoints() {
        return Points;
    }

    public void setPoints(Integer points) {
        Points = points;
    }

    public String getSignalType() {
        return SignalType;
    }

    public void setSignalType(String signalType) {
        SignalType = signalType;
    }

    public String getVibFeatures() {
        return VibFeatures;
    }

    public void setVibFeatures(String vibFeatures) {
        VibFeatures = vibFeatures;
    }

    public String getTaskItemID() {
        return TaskItemID;
    }

    public void setTaskItemID(String taskItemID) {
        TaskItemID = taskItemID;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getLineId() {
        return LineId;
    }

    public void setLineId(String lineId) {
        LineId = lineId;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getUserPostId() {
        return UserPostId;
    }

    public void setUserPostId(String userPostId) {
        UserPostId = userPostId;
    }

    public String getExceptionLevel() {
        return ExceptionLevel;
    }

    public void setExceptionLevel(String exceptionLevel) {
        ExceptionLevel = exceptionLevel;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getResult_ID() {
        return Result_ID;
    }

    public void setResult_ID(String result_ID) {
        Result_ID = result_ID;
    }

    public String getTask_ID() {
        return Task_ID;
    }

    public void setTask_ID(String task_ID) {
        Task_ID = task_ID;
    }

    public String getLabelID() {
        return LabelID;
    }

    public void setLabelID(String labelID) {
        LabelID = labelID;
    }

    public String getMobjectCode() {
        return MobjectCode;
    }

    public void setMobjectCode(String mobjectCode) {
        MobjectCode = mobjectCode;
    }

    public String getMobjectName() {
        return MobjectName;
    }

    public void setMobjectName(String mobjectName) {
        MobjectName = mobjectName;
    }

    public String getCheckItem_ID() {
        return CheckItem_ID;
    }

    public void setCheckItem_ID(String checkItem_ID) {
        CheckItem_ID = checkItem_ID;
    }

    public String getStart_TM() {
        return Start_TM;
    }

    public void setStart_TM(String start_TM) {
        Start_TM = start_TM;
    }

    public String getComplete_TM() {
        return Complete_TM;
    }

    public void setComplete_TM(String complete_TM) {
        Complete_TM = complete_TM;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getResultValue() {
        return ResultValue;
    }

    public void setResultValue(String resultValue) {
        ResultValue = resultValue;
    }

    public Integer getRate() {
        return Rate;
    }

    public void setRate(Integer rate) {
        Rate = rate;
    }

    public String getMObjectStatus() {
        return MObjectStatus;
    }

    public void setMObjectStatus(String MObjectStatus) {
        this.MObjectStatus = MObjectStatus;
    }

    public String getException_YN() {
        return Exception_YN;
    }

    public void setException_YN(String exception_YN) {
        Exception_YN = exception_YN;
    }

    public String getExceptionID() {
        return ExceptionID;
    }

    public void setExceptionID(String exceptionID) {
        ExceptionID = exceptionID;
    }

    public String getMemo_TX() {
        return Memo_TX;
    }

    public void setMemo_TX(String memo_TX) {
        Memo_TX = memo_TX;
    }

    public Integer getTime_NR() {
        return Time_NR;
    }

    public void setTime_NR(Integer time_NR) {
        Time_NR = time_NR;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getPDADevice() {
        return PDADevice;
    }

    public void setPDADevice(String PDADevice) {
        this.PDADevice = PDADevice;
    }

    public String getExceptionTransfer_YN() {
        return ExceptionTransfer_YN;
    }

    public void setExceptionTransfer_YN(String exceptionTransfer_YN) {
        ExceptionTransfer_YN = exceptionTransfer_YN;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }
}
