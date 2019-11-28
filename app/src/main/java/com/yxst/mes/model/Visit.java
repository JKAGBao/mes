package com.yxst.mes.model;

import android.content.Context;
import android.os.Build;

import com.yxst.mes.util.NetworkUtil;
import com.yxst.mes.util.SharedPreferenceUtil;
import com.yxst.mes.util.TimeUtil;

import java.util.Date;

/**
 * Created By YuanCheng on 2019/8/6 14:05
 */
public class Visit {
    /*
    IP:192.164.6.32
    InputTime:2019-08-02 14:28
    CategoryID:2
    OperateTypeID:3
    OperateType:访问
    SysModule:MenuName
    LogMessage:访问menu
    RequestUrl:
    MenuCode:01
    UserID:1
    UserName:mm
    LogMac:
    IPAddressName:
    Host:
    Browser:APP
    ExecuteResult:1
    IsDel:false
     */
    private String IP;
    private String InputTime;//2019-08-02 14:28
    private int CategoryID=2;
    private int OperateTypeID=3;
    private String OperateType="访问";
    private String SysModule;//:MenuName
    private String LogMessage;//:访问menu
    private String RequestUrl;//:
    private int MenuCode =01;
    private Long UserID;//:1
    private String UserName;//:mm
    private String LogMac = "";
    private String IPAddressName;
    private String Host;
    private String Browser="InspectAPP";
    private int ExecuteResult=1;
    private boolean IsDel = false;
    public Visit(Context ctx){
        IP=NetworkUtil.getIP(ctx);
        UserID = SharedPreferenceUtil.getId(ctx,"User");
        UserName = SharedPreferenceUtil.getName(ctx,"User");
        InputTime = TimeUtil.pageDateFormat(new Date());
        IPAddressName=NetworkUtil.getNetType(ctx);
        Host=Build.MODEL;
        RequestUrl=ctx.getClass().getName();
    }
    public Visit(Context ctx,String title){
        this(ctx);
        SysModule=title;
        LogMessage="访问【"+title+"】";
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getInputTime() {
        return InputTime;
    }

    public void setInputTime(String inputTime) {
        InputTime = inputTime;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public int getOperateTypeID() {
        return OperateTypeID;
    }

    public void setOperateTypeID(int operateTypeID) {
        OperateTypeID = operateTypeID;
    }

    public String getOperateType() {
        return OperateType;
    }

    public void setOperateType(String operateType) {
        OperateType = operateType;
    }

    public String getSysModule() {
        return SysModule;
    }

    public void setSysModule(String sysModule) {
        SysModule = sysModule;
    }

    public String getLogMessage() {
        return LogMessage;
    }

    public void setLogMessage(String logMessage) {
        LogMessage = logMessage;
    }

    public String getRequestUrl() {
        return RequestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        RequestUrl = requestUrl;
    }

    public int getMenuCode() {
        return MenuCode;
    }

    public void setMenuCode(int menuCode) {
        MenuCode = menuCode;
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getLogMac() {
        return LogMac;
    }

    public void setLogMac(String logMac) {
        LogMac = logMac;
    }

    public String getIPAddressName() {
        return IPAddressName;
    }

    public void setIPAddressName(String IPAddressName) {
        this.IPAddressName = IPAddressName;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getBrowser() {
        return Browser;
    }

    public void setBrowser(String browser) {
        Browser = browser;
    }

    public int getExecuteResult() {
        return ExecuteResult;
    }

    public void setExecuteResult(int executeResult) {
        ExecuteResult = executeResult;
    }

    public boolean isDel() {
        return IsDel;
    }

    public void setDel(boolean del) {
        IsDel = del;
    }
}
