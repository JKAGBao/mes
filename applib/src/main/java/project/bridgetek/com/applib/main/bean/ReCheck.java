package project.bridgetek.com.applib.main.bean;

import java.util.List;

/**
 * Created by Cong Zhizhong on 18-7-10.
 */

public class ReCheck {
    List<CheckItemInfo> checkItemInfo;

    public ReCheck(List<CheckItemInfo> ceshi) {
        this.checkItemInfo = ceshi;
    }

    public ReCheck() {
    }

    public List<CheckItemInfo> getCheckItemInfo() {
        return checkItemInfo;
    }

    public void setCheckItemInfo(List<CheckItemInfo> checkItemInfo) {
        this.checkItemInfo = checkItemInfo;
    }
}
