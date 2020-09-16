package project.bridgetek.com.applib.main.bean.body;

import java.util.List;

import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;

public class CheckItemBody {
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
    List<ResultFileInfo> ResultFile;
    List<Float> WaveData;

    public CheckItemBody(CheckItem checkItem, List<ResultFileInfo> resultFile) {
        Result_ID = checkItem.getResult_ID();
        Task_ID = checkItem.getTask_ID();
        LabelID = checkItem.getLabelID();
        MobjectCode = checkItem.getMobjectCode();
        MobjectName = checkItem.getMobjectName();
        CheckItem_ID = checkItem.getCheckItem_ID();
        Start_TM = checkItem.getStart_TM();
        Complete_TM = checkItem.getComplete_TM();
        UserID = checkItem.getUserID();
        UserName = checkItem.getUserName();
        ResultValue = checkItem.getResultValue();
        Rate = checkItem.getRate();
        this.MObjectStatus = checkItem.getMObjectStatus();
        Exception_YN = checkItem.getException_YN();
        ExceptionID = checkItem.getExceptionID();
        Memo_TX = checkItem.getMemo_TX();
        Time_NR = checkItem.getTime_NR();
        GroupName = checkItem.getGroupName();
        this.PDADevice = checkItem.getPDADevice();
        ExceptionTransfer_YN = checkItem.getExceptionTransfer_YN();
        this.submit = checkItem.isSubmit();
        TaskItemID = checkItem.getTaskItemID();
        TaskType = checkItem.getTaskType();
        LineId = checkItem.getLineId();
        UserCode = checkItem.getUserCode();
        UserPostId = checkItem.getUserPostId();
        ExceptionLevel = checkItem.getExceptionLevel();
        ShiftName = checkItem.getShiftName();
        VibFeatures = checkItem.getVibFeatures();
        this.Points = checkItem.getPoints();
        this.SignalType = checkItem.getSignalType();
        ResultFile = resultFile;
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

    public String getVibFeatures() {
        return VibFeatures;
    }

    public void setVibFeatures(String vibFeatures) {
        VibFeatures = vibFeatures;
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

    public List<ResultFileInfo> getResultFile() {
        return ResultFile;
    }

    public void setResultFile(List<ResultFileInfo> resultFile) {
        ResultFile = resultFile;
    }

    public List<Float> getWaveData() {
        return WaveData;
    }

    public void setWaveData(List<Float> waveData) {
        WaveData = waveData;
    }
}
