package project.bridgetek.com.applib.main.bean;

/**
 * Created by Cong Zhizhong on 18-6-12.
 */

public class TaskInfo {
    String TaskID;
    String LineID;
    String TaskName;
    String LineName;
    String TaskType;
    String LableType;
    String TaskPlanStartTime;
    String TaskPlanEndTime;
    String TaskUpdateTime;
    String EquipmentStatusEnabled;
    boolean submit = false;
    String Sync = "0";

    public TaskInfo() {
    }

    public TaskInfo(String taskID, String lineID, String taskName, String lineName, String taskType, String lableType, String taskPlanStartTime, String taskPlanEndTime, String taskUpdateTime, boolean submit, String EquipmentStatusEnabled) {
        TaskID = taskID;
        LineID = lineID;
        TaskName = taskName;
        LineName = lineName;
        TaskType = taskType;
        LableType = lableType;
        TaskPlanStartTime = taskPlanStartTime;
        TaskPlanEndTime = taskPlanEndTime;
        TaskUpdateTime = taskUpdateTime;
        this.submit = submit;
        this.EquipmentStatusEnabled = EquipmentStatusEnabled;
    }

    public TaskInfo(String taskID, String lineID, String taskName, String lineName, String taskType, String lableType, String taskPlanStartTime, String taskPlanEndTime, String taskUpdateTime, boolean submit, String EquipmentStatusEnabled, String Sync) {
        TaskID = taskID;
        LineID = lineID;
        TaskName = taskName;
        LineName = lineName;
        TaskType = taskType;
        LableType = lableType;
        TaskPlanStartTime = taskPlanStartTime;
        TaskPlanEndTime = taskPlanEndTime;
        TaskUpdateTime = taskUpdateTime;
        this.submit = submit;
        this.EquipmentStatusEnabled = EquipmentStatusEnabled;
        this.Sync = Sync;
    }


    public String getSync() {
        return Sync;
    }

    public void setSync(String sync) {
        Sync = sync;
    }

    public String getEquipmentStatusEnabled() {
        return EquipmentStatusEnabled;
    }

    public void setEquipmentStatusEnabled(String equipmentStatusEnabled) {
        EquipmentStatusEnabled = equipmentStatusEnabled;
    }

    public String getLableType() {
        return LableType;
    }

    public void setLableType(String lableType) {
        LableType = lableType;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getLineID() {
        return LineID;
    }

    public void setLineID(String lineID) {
        LineID = lineID;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTaskPlanStartTime() {
        return TaskPlanStartTime;
    }

    public void setTaskPlanStartTime(String taskPlanStartTime) {
        TaskPlanStartTime = taskPlanStartTime;
    }

    public String getTaskPlanEndTime() {
        return TaskPlanEndTime;
    }

    public void setTaskPlanEndTime(String taskPlanEndTime) {
        TaskPlanEndTime = taskPlanEndTime;
    }

    public String getTaskUpdateTime() {
        return TaskUpdateTime;
    }

    public void setTaskUpdateTime(String taskUpdateTime) {
        TaskUpdateTime = taskUpdateTime;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }
}
