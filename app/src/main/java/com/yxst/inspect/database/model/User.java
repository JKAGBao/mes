package com.yxst.inspect.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb="user")
public class User {
    @Id(autoincrement = true)
    private Long UserID;
    private String LoginName;
    private String LoginPwdCode;
    private String RoleCode;
    private String RoleNameCN;
    private String RealName;
    private String DeptName;
    private String HeadImg;
    private int DeptID;
    @Generated(hash = 468489348)
    public User(Long UserID, String LoginName, String LoginPwdCode, String RoleCode,
            String RoleNameCN, String RealName, String DeptName, String HeadImg,
            int DeptID) {
        this.UserID = UserID;
        this.LoginName = LoginName;
        this.LoginPwdCode = LoginPwdCode;
        this.RoleCode = RoleCode;
        this.RoleNameCN = RoleNameCN;
        this.RealName = RealName;
        this.DeptName = DeptName;
        this.HeadImg = HeadImg;
        this.DeptID = DeptID;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getUserID() {
        return this.UserID;
    }
    public void setUserID(Long UserID) {
        this.UserID = UserID;
    }
    public String getLoginName() {
        return this.LoginName;
    }
    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }
    public String getLoginPwdCode() {
        return this.LoginPwdCode;
    }
    public void setLoginPwdCode(String LoginPwdCode) {
        this.LoginPwdCode = LoginPwdCode;
    }
    public String getRoleCode() {
        return this.RoleCode;
    }
    public void setRoleCode(String RoleCode) {
        this.RoleCode = RoleCode;
    }
    public String getRoleNameCN() {
        return this.RoleNameCN;
    }
    public void setRoleNameCN(String RoleNameCN) {
        this.RoleNameCN = RoleNameCN;
    }
    public String getRealName() {
        return this.RealName;
    }
    public void setRealName(String RealName) {
        this.RealName = RealName;
    }
    public String getDeptName() {
        return this.DeptName;
    }
    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }
    public String getHeadImg() {
        return this.HeadImg;
    }
    public void setHeadImg(String HeadImg) {
        this.HeadImg = HeadImg;
    }
    public int getDeptID() {
        return this.DeptID;
    }
    public void setDeptID(int DeptID) {
        this.DeptID = DeptID;
    }

   
}
