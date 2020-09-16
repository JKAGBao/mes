package project.bridgetek.com.applib.main.bean;

import java.util.List;

/**
 * Created by Cong Zhizhong on 18-7-17.
 */

public class SearchException {
    int nums;
    List<ReException> exceptions;

    public SearchException() {
    }

    public SearchException(int nums, List<ReException> exceptions) {
        this.nums = nums;
        this.exceptions = exceptions;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public List<ReException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ReException> exceptions) {
        this.exceptions = exceptions;
    }
}
