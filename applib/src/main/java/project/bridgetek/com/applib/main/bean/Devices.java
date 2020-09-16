package project.bridgetek.com.applib.main.bean;


import java.io.Serializable;

/**
 * Created by bridge on 18-7-6.
 */

public class Devices implements Serializable {
    String DeviceCode;
    String DeviceName;
    String DeviceLocation;
    String UpdateTime;

    public Devices() {
    }

    public Devices(String deviceCode, String deviceName, String deviceLocation, String updateTime) {

        DeviceCode = deviceCode;
        DeviceName = deviceName;
        DeviceLocation = deviceLocation;
        UpdateTime = updateTime;
    }

    public String getDeviceCode() {
        return DeviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        DeviceCode = deviceCode;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceLocation() {
        return DeviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        DeviceLocation = deviceLocation;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }
}
