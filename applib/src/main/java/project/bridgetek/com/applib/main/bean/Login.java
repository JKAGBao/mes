package project.bridgetek.com.applib.main.bean;

import java.util.List;

/**
 * Created by bridge on 18-6-13.
 */

public class Login {

    /**
     * retcode : 0
     * account : webapi
     * badgeid :
     * accountid : 9eeb4e08-00aa-4948-b5c6-d3a97fdc63d3
     * username : WebApi测试
     * usercode : 10999
     * userpostid : 0
     * permissions : ["FreePatrol"]
     */

    private int retcode;
    private String account;
    private String badgeid;
    private String accountid;
    private String username;
    private String usercode;
    private String userpostid;
    private String departmentname;
    private String companyname;
    private List<String> permissions;
    private List<String> exceptionlevelnames;
    private List<String> EquipmentStatusNames;

    public List<String> getEquipmentStatusNames() {
        return EquipmentStatusNames;
    }

    public void setEquipmentStatusNames(List<String> equipmentStatusNames) {
        EquipmentStatusNames = equipmentStatusNames;
    }

    public int getRetcode() {
        return retcode;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUserpostid() {
        return userpostid;
    }

    public void setUserpostid(String userpostid) {
        this.userpostid = userpostid;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getExceptionlevelnames() {
        return exceptionlevelnames;
    }

    public void setExceptionlevelnames(List<String> exceptionlevelnames) {
        this.exceptionlevelnames = exceptionlevelnames;
    }
}
