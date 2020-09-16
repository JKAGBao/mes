package project.bridgetek.com.applib.main.bean;

public class OfflineReason {
    private String Result;
    private String Result_Des;

    public OfflineReason() {
    }

    public OfflineReason(String result, String result_Des) {
        Result = result;
        Result_Des = result_Des;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getResult_Des() {
        return Result_Des;
    }

    public void setResult_Des(String result_Des) {
        Result_Des = result_Des;
    }
}
