package project.bridgetek.com.applib.main.bean;

import java.io.Serializable;

/**
 * Created by Cong Zhizhong on 18-6-12.
 */

public class CheckItemInfo implements Serializable {
    String CheckItemID;
    String MobjectCode;
    String MobjectName;
    String LabelID;
    String LabelName;
    String TaskID;
    String LineID;
    String CheckItemDesc;
    String ESTStandard;
    String CheckType;
    String ZhenDong_Type;
    String Zhendong_PP;
    String AlarmType_ID;
    String StandardValue;
    String ParmLowerLimit;
    String ParmUpperLimit;
    String CheckItemUpdateTime;
    float UpperLimit1 = -100;
    float UpperLimit2 = -100;
    float UpperLimit3 = -100;
    float UpperLimit4 = -100;
    float LowerLimit1 = -100;
    float LowerLimit2 = -100;
    float LowerLimit3 = -100;
    float LowerLimit4 = -100;
    int CheckOrderNo;
    boolean submit = false;
    String TaskItemID;
    String LabelCode;
    String ObserveOptions;
    String ShiftName;
    String GroupName;
    String TaskPlanStartTime;
    String TaskPlanEndTime;
    String NextTaskDate;
    String NextTaskDateTimePeriods;
    String ZhenDong_Freq;
    String ZhenDong_Points;
    String EquipmentStatusFilter;
    String SyncSamplingTag;
    //state用于记录设备状态。
    int state = -1;

    public CheckItemInfo(String checkItemID, String mobjectCode, String mobjectName, String labelID, String labelName, String taskID, String lineID, String checkItemDesc, String ESTStandard, String checkType, String zhenDong_Type, String zhendong_PP, String alarmType_ID, String standardValue, String parmLowerLimit, String parmUpperLimit, String checkItemUpdateTime, float upperLimit1, float upperLimit2, float upperLimit3, float upperLimit4, float lowerLimit1, float lowerLimit2, float lowerLimit3, float lowerLimit4, int checkOrderNo, boolean submit, String taskItemID, String labelCode, String observeOptions, String shiftName, String groupName, String taskPlanStartTime, String taskPlanEndTime, String nextTaskDate, String nextTaskDateTimePeriods
            , String zhenDong_Freq, String zhenDong_Points, String EquipmentStatusFilter, String SyncSamplingTag, int state) {
        CheckItemID = checkItemID;
        MobjectCode = mobjectCode;
        MobjectName = mobjectName;
        LabelID = labelID;
        LabelName = labelName;
        TaskID = taskID;
        LineID = lineID;
        CheckItemDesc = checkItemDesc;
        this.ESTStandard = ESTStandard;
        CheckType = checkType;
        ZhenDong_Type = zhenDong_Type;
        Zhendong_PP = zhendong_PP;
        AlarmType_ID = alarmType_ID;
        StandardValue = standardValue;
        ParmLowerLimit = parmLowerLimit;
        ParmUpperLimit = parmUpperLimit;
        CheckItemUpdateTime = checkItemUpdateTime;
        UpperLimit1 = upperLimit1;
        UpperLimit2 = upperLimit2;
        UpperLimit3 = upperLimit3;
        UpperLimit4 = upperLimit4;
        LowerLimit1 = lowerLimit1;
        LowerLimit2 = lowerLimit2;
        LowerLimit3 = lowerLimit3;
        LowerLimit4 = lowerLimit4;
        CheckOrderNo = checkOrderNo;
        this.submit = submit;
        TaskItemID = taskItemID;
        LabelCode = labelCode;
        ObserveOptions = observeOptions;
        ShiftName = shiftName;
        GroupName = groupName;
        TaskPlanStartTime = taskPlanStartTime;
        TaskPlanEndTime = taskPlanEndTime;
        NextTaskDate = nextTaskDate;
        NextTaskDateTimePeriods = nextTaskDateTimePeriods;
        ZhenDong_Freq = zhenDong_Freq;
        ZhenDong_Points = zhenDong_Points;
        this.EquipmentStatusFilter = EquipmentStatusFilter;
        this.SyncSamplingTag = SyncSamplingTag;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getEquipmentStatusFilter() {
        return EquipmentStatusFilter;
    }

    public void setEquipmentStatusFilter(String equipmentStatusFilter) {
        EquipmentStatusFilter = equipmentStatusFilter;
    }

    public String getSyncSamplingTag() {
        return SyncSamplingTag;
    }

    public void setSyncSamplingTag(String syncSamplingTag) {
        SyncSamplingTag = syncSamplingTag;
    }

    public String getZhenDong_Freq() {
        return ZhenDong_Freq;
    }

    public void setZhenDong_Freq(String zhenDong_Freq) {
        ZhenDong_Freq = zhenDong_Freq;
    }

    public String getZhenDong_Points() {
        return ZhenDong_Points;
    }

    public void setZhenDong_Points(String zhenDong_Points) {
        ZhenDong_Points = zhenDong_Points;
    }

    public String getTaskItemID() {
        return TaskItemID;
    }

    public void setTaskItemID(String taskItemID) {
        TaskItemID = taskItemID;
    }

    public String getLabelCode() {
        return LabelCode;
    }

    public void setLabelCode(String labelCode) {
        LabelCode = labelCode;
    }

    public String getObserveOptions() {
        return ObserveOptions;
    }

    public void setObserveOptions(String observeOptions) {
        ObserveOptions = observeOptions;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
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

    public String getNextTaskDate() {
        return NextTaskDate;
    }

    public void setNextTaskDate(String nextTaskDate) {
        NextTaskDate = nextTaskDate;
    }

    public String getNextTaskDateTimePeriods() {
        return NextTaskDateTimePeriods;
    }

    public void setNextTaskDateTimePeriods(String nextTaskDateTimePeriods) {
        NextTaskDateTimePeriods = nextTaskDateTimePeriods;
    }

    public String getLineID() {
        return LineID;
    }

    public void setLineID(String lineID) {
        LineID = lineID;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public CheckItemInfo() {
    }

    public String getCheckItemID() {
        return CheckItemID;
    }

    public void setCheckItemID(String checkItemID) {
        CheckItemID = checkItemID;
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

    public String getLabelID() {
        return LabelID;
    }

    public void setLabelID(String labelID) {
        LabelID = labelID;
    }

    public String getLabelName() {
        return LabelName;
    }

    public void setLabelName(String labelName) {
        LabelName = labelName;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getCheckItemDesc() {
        return CheckItemDesc;
    }

    public void setCheckItemDesc(String checkItemDesc) {
        CheckItemDesc = checkItemDesc;
    }

    public String getESTStandard() {
        return ESTStandard;
    }

    public void setESTStandard(String ESTStandard) {
        this.ESTStandard = ESTStandard;
    }

    public String getCheckType() {
        return CheckType;
    }

    public void setCheckType(String checkType) {
        CheckType = checkType;
    }

    public String getZhenDong_Type() {
        return ZhenDong_Type;
    }

    public void setZhenDong_Type(String zhenDong_Type) {
        ZhenDong_Type = zhenDong_Type;
    }

    public String getZhendong_PP() {
        return Zhendong_PP;
    }

    public void setZhendong_PP(String zhendong_PP) {
        Zhendong_PP = zhendong_PP;
    }

    public String getAlarmType_ID() {
        return AlarmType_ID;
    }

    public void setAlarmType_ID(String alarmType_ID) {
        AlarmType_ID = alarmType_ID;
    }

    public String getStandardValue() {
        return StandardValue;
    }

    public void setStandardValue(String standardValue) {
        StandardValue = standardValue;
    }

    public String getParmLowerLimit() {
        return ParmLowerLimit;
    }

    public void setParmLowerLimit(String parmLowerLimit) {
        ParmLowerLimit = parmLowerLimit;
    }

    public float getUpperLimit1() {
        return UpperLimit1;
    }

    public void setUpperLimit1(float upperLimit1) {
        UpperLimit1 = upperLimit1;
    }

    public float getUpperLimit2() {
        return UpperLimit2;
    }

    public void setUpperLimit2(float upperLimit2) {
        UpperLimit2 = upperLimit2;
    }

    public float getUpperLimit3() {
        return UpperLimit3;
    }

    public void setUpperLimit3(float upperLimit3) {
        UpperLimit3 = upperLimit3;
    }

    public float getUpperLimit4() {
        return UpperLimit4;
    }

    public void setUpperLimit4(float upperLimit4) {
        UpperLimit4 = upperLimit4;
    }

    public float getLowerLimit1() {
        return LowerLimit1;
    }

    public void setLowerLimit1(float lowerLimit1) {
        LowerLimit1 = lowerLimit1;
    }

    public float getLowerLimit2() {
        return LowerLimit2;
    }

    public void setLowerLimit2(float lowerLimit2) {
        LowerLimit2 = lowerLimit2;
    }

    public float getLowerLimit3() {
        return LowerLimit3;
    }

    public void setLowerLimit3(float lowerLimit3) {
        LowerLimit3 = lowerLimit3;
    }

    public float getLowerLimit4() {
        return LowerLimit4;
    }

    public void setLowerLimit4(float lowerLimit4) {
        LowerLimit4 = lowerLimit4;
    }

    public String getParmUpperLimit() {
        return ParmUpperLimit;
    }

    public void setParmUpperLimit(String parmUpperLimit) {
        ParmUpperLimit = parmUpperLimit;
    }

    public int getCheckOrderNo() {
        return CheckOrderNo;
    }

    public void setCheckOrderNo(int checkOrderNo) {
        CheckOrderNo = checkOrderNo;
    }

    public String getCheckItemUpdateTime() {
        return CheckItemUpdateTime;
    }

    public void setCheckItemUpdateTime(String checkItemUpdateTime) {
        CheckItemUpdateTime = checkItemUpdateTime;
    }
}
