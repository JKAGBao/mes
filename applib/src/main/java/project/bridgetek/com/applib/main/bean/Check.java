package project.bridgetek.com.applib.main.bean;

import java.util.List;

/**
 * Created by bridge on 18-6-14.
 */

public class Check {
    int itemnum;
    List<CheckItemInfo> checkitems;
    String errorMessage;

    public Check(int itemnum, List<CheckItemInfo> checkitems, String errorMessage) {
        this.itemnum = itemnum;
        this.checkitems = checkitems;
        this.errorMessage = errorMessage;
    }

    public Check() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getItemnum() {
        return itemnum;
    }

    public void setItemnum(int itemnum) {
        this.itemnum = itemnum;
    }

    public List<CheckItemInfo> getCheckitems() {
        return checkitems;
    }

    public void setCheckitems(List<CheckItemInfo> checkitems) {
        this.checkitems = checkitems;
    }
}
