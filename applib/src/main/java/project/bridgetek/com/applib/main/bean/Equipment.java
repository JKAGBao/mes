package project.bridgetek.com.applib.main.bean;

import java.util.List;

/**
 * Created by Cong Zhizhong on 18-7-6.
 */

public class Equipment {
    String accountId;
    int deviceCount;
    List<Devices> devices;

    public Equipment() {
    }

    public Equipment(String accountI, int deviceCount, List<Devices> devices) {
        this.accountId = accountI;
        this.deviceCount = deviceCount;
        this.devices = devices;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public List<Devices> getDevices() {
        return devices;
    }

    public void setDevices(List<Devices> devices) {
        this.devices = devices;
    }
}
