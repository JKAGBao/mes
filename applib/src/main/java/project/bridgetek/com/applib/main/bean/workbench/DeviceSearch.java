package project.bridgetek.com.applib.main.bean.workbench;

/**
 * Created by czz on 19-5-21.
 */

public class DeviceSearch {

    /**
     * devCode : 20190201
     * devName : 上水泵
     * prodLineSName : 钢铁厂电动房
     * FullName : 西昌钢钒
     */

    private String devCode;
    private String devName;
    private String prodLineSName;
    private String FullName;

    public DeviceSearch() {
    }

    public DeviceSearch(String devCode, String devName, String prodLineSName, String fullName) {
        this.devCode = devCode;
        this.devName = devName;
        this.prodLineSName = prodLineSName;
        FullName = fullName;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getProdLineSName() {
        return prodLineSName;
    }

    public void setProdLineSName(String prodLineSName) {
        this.prodLineSName = prodLineSName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }
}
