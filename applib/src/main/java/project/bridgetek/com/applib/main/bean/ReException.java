package project.bridgetek.com.applib.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Cong Zhizhong on 18-6-26.
 */

public class ReException implements Serializable {
    String Exception_ID;
    String ExceptionTitle;
    String Task_ID;
    String Label_ID;
    String MobjectName;
    String MobjectCode;
    String CheckItem_ID;
    String UserID;
    String UserName;
    String ResultValue;
    Integer Rate;
    String WaveData;
    String Memo;
    String GroupName;
    String PDADevice;
    String status;
    String Found_TM;
    Integer Duration_TM;
    String updatetime;
    List<String> files;
    boolean submit;

    public ReException() {
    }

    public ReException(String exception_ID, String exceptionTitle, String task_ID, String label_ID, String mobjectName, String mobjectCode, String checkItem_ID, String userID, String userName, String resultValue, Integer rate, String waveData, String memo, String groupName, String PDADevice, String status, String found_TM, Integer duration_TM, String updatetime, boolean submit) {
        Exception_ID = exception_ID;
        ExceptionTitle = exceptionTitle;
        Task_ID = task_ID;
        Label_ID = label_ID;
        MobjectName = mobjectName;
        MobjectCode = mobjectCode;
        CheckItem_ID = checkItem_ID;
        UserID = userID;
        UserName = userName;
        ResultValue = resultValue;
        Rate = rate;
        WaveData = waveData;
        Memo = memo;
        GroupName = groupName;
        this.PDADevice = PDADevice;
        this.status = status;
        Found_TM = found_TM;
        Duration_TM = duration_TM;
        this.updatetime = updatetime;
        this.submit = submit;
    }

    public ReException(String exception_ID, String exceptionTitle, String task_ID, String label_ID, String mobjectName, String mobjectCode, String checkItem_ID, String userID, String userName, String resultValue, Integer rate, String waveData, String memo, String groupName, String PDADevice, String status, String found_TM, Integer duration_TM, String updatetime, List<String> files, boolean submit) {
        Exception_ID = exception_ID;
        ExceptionTitle = exceptionTitle;
        Task_ID = task_ID;
        Label_ID = label_ID;
        MobjectName = mobjectName;
        MobjectCode = mobjectCode;
        CheckItem_ID = checkItem_ID;
        UserID = userID;
        UserName = userName;
        ResultValue = resultValue;
        Rate = rate;
        WaveData = waveData;
        Memo = memo;
        GroupName = groupName;
        this.PDADevice = PDADevice;
        this.status = status;
        Found_TM = found_TM;
        Duration_TM = duration_TM;
        this.updatetime = updatetime;
        this.files = files;
        this.submit = submit;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getMobjectName() {
        return MobjectName;
    }

    public void setMobjectName(String mobjectName) {
        MobjectName = mobjectName;
    }

    public String getExceptionTitle() {
        return ExceptionTitle;
    }

    public void setExceptionTitle(String exceptionTitle) {
        ExceptionTitle = exceptionTitle;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public String getException_ID() {
        return Exception_ID;
    }

    public void setException_ID(String exception_ID) {
        Exception_ID = exception_ID;
    }

    public String getTask_ID() {
        return Task_ID;
    }

    public void setTask_ID(String task_ID) {
        Task_ID = task_ID;
    }

    public String getLabel_ID() {
        return Label_ID;
    }

    public void setLabel_ID(String label_ID) {
        Label_ID = label_ID;
    }

    public String getMobjectCode() {
        return MobjectCode;
    }

    public void setMobjectCode(String mobjectCode) {
        MobjectCode = mobjectCode;
    }

    public String getCheckItem_ID() {
        return CheckItem_ID;
    }

    public void setCheckItem_ID(String checkItem_ID) {
        CheckItem_ID = checkItem_ID;
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

    public String getWaveData() {
        return WaveData;
    }

    public void setWaveData(String waveData) {
        WaveData = waveData;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFound_TM() {
        return Found_TM;
    }

    public void setFound_TM(String found_TM) {
        Found_TM = found_TM;
    }

    public Integer getDuration_TM() {
        return Duration_TM;
    }

    public void setDuration_TM(Integer duration_TM) {
        Duration_TM = duration_TM;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
