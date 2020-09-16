package project.bridgetek.com.applib.main.bean;

/**
 * Created by Cong Zhizhong on 18-8-7.
 */

public class Label {
    String LabelID;
    String LabelCode;
    String TaskType;
    String LineId;
    String Start_TM;
    String End_TM;
    String User_ID;
    String UserName;
    String UserCode;
    String UserPostId;
    String ShiftName;
    String GroupName;
    Integer TimeCount;
    String TaskID;
    String id;

    public Label() {
    }

    public Label(String labelID, String labelCode, String taskType, String lineId, String start_TM, String end_TM, String user_ID, String userName, String userCode, String userPostId, String shiftName, String groupName, Integer timeCount, String taskID) {
        LabelID = labelID;
        LabelCode = labelCode;
        TaskType = taskType;
        LineId = lineId;
        Start_TM = start_TM;
        End_TM = end_TM;
        User_ID = user_ID;
        UserName = userName;
        UserCode = userCode;
        UserPostId = userPostId;
        ShiftName = shiftName;
        GroupName = groupName;
        TimeCount = timeCount;
        TaskID = taskID;
    }

    public Label(String labelID, String labelCode, String taskType, String lineId, String start_TM, String end_TM, String user_ID, String userName, String userCode, String userPostId, String shiftName, String groupName, Integer timeCount, String taskID, String id) {
        LabelID = labelID;
        LabelCode = labelCode;
        TaskType = taskType;
        LineId = lineId;
        Start_TM = start_TM;
        End_TM = end_TM;
        User_ID = user_ID;
        UserName = userName;
        UserCode = userCode;
        UserPostId = userPostId;
        ShiftName = shiftName;
        GroupName = groupName;
        TimeCount = timeCount;
        TaskID = taskID;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelCode() {
        return LabelCode;
    }

    public void setLabelCode(String labelCode) {
        LabelCode = labelCode;
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

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getLabelID() {
        return LabelID;
    }

    public void setLabelID(String labelID) {
        LabelID = labelID;
    }

    public String getStart_TM() {
        return Start_TM;
    }

    public void setStart_TM(String start_TM) {
        Start_TM = start_TM;
    }

    public String getEnd_TM() {
        return End_TM;
    }

    public void setEnd_TM(String end_TM) {
        End_TM = end_TM;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public Integer getTimeCount() {
        return TimeCount;
    }

    public void setTimeCount(Integer timeCount) {
        TimeCount = timeCount;
    }
}
