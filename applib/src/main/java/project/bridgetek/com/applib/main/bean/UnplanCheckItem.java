package project.bridgetek.com.applib.main.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class UnplanCheckItem {
    @JSONField(name = "Result_ID")
    private String Result_ID;
    @JSONField(name = "MobjectName")
    private String MobjectName;
    @JSONField(name = "MeasurePosition")
    private String MeasurePosition;
    @JSONField(name = "MeasurePoint")
    private String MeasurePoint;
    @JSONField(name = "Complete_TM")
    private String Complete_TM;
    @JSONField(name = "UserID")
    private String UserID;
    @JSONField(name = "UserName")
    private String UserName;
    @JSONField(name = "ResultValue")
    private String ResultValue;
    @JSONField(name = "TempResultValue")
    private String TempResultValue;
    @JSONField(name = "SignalType")
    private String SignalType;
    @JSONField(name = "Rate")
    private Integer Rate;
    @JSONField(name = "Points")
    private Integer Points;
    @JSONField(name = "VibFeatures")
    private String VibFeatures;
    @JSONField(name = "Memo_TX")
    private String Memo_TX;

    public UnplanCheckItem(String result_ID, String mobjectName, String measurePosition, String measurePoint, String complete_TM, String userID, String userName, String resultValue, String tempResultValue, String signalType, Integer rate, Integer points, String vibFeatures, String memo_TX) {
        Result_ID = result_ID;
        MobjectName = mobjectName;
        MeasurePosition = measurePosition;
        MeasurePoint = measurePoint;
        Complete_TM = complete_TM;
        UserID = userID;
        UserName = userName;
        ResultValue = resultValue;
        TempResultValue = tempResultValue;
        SignalType = signalType;
        Rate = rate;
        Points = points;
        VibFeatures = vibFeatures;
        Memo_TX = memo_TX;
    }

    public String getResult_ID() {
        return Result_ID;
    }

    public void setResult_ID(String result_ID) {
        Result_ID = result_ID;
    }

    public String getMobjectName() {
        return MobjectName;
    }

    public void setMobjectName(String mobjectName) {
        MobjectName = mobjectName;
    }

    public String getMeasurePosition() {
        return MeasurePosition;
    }

    public void setMeasurePosition(String measurePosition) {
        MeasurePosition = measurePosition;
    }

    public String getMeasurePoint() {
        return MeasurePoint;
    }

    public void setMeasurePoint(String measurePoint) {
        MeasurePoint = measurePoint;
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

    public String getTempResultValue() {
        return TempResultValue;
    }

    public void setTempResultValue(String tempResultValue) {
        TempResultValue = tempResultValue;
    }

    public String getSignalType() {
        return SignalType;
    }

    public void setSignalType(String signalType) {
        SignalType = signalType;
    }

    public Integer getRate() {
        return Rate;
    }

    public void setRate(Integer rate) {
        Rate = rate;
    }

    public Integer getPoints() {
        return Points;
    }

    public void setPoints(Integer points) {
        Points = points;
    }

    public String getVibFeatures() {
        return VibFeatures;
    }

    public void setVibFeatures(String vibFeatures) {
        VibFeatures = vibFeatures;
    }

    public String getMemo_TX() {
        return Memo_TX;
    }

    public void setMemo_TX(String memo_TX) {
        Memo_TX = memo_TX;
    }
}
