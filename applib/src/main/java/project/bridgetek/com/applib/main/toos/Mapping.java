package project.bridgetek.com.applib.main.toos;

public class Mapping {
    public String getValue(String val) {
        if (val == null || val.equals("null") || val.equals("")) {
            val = "--";
        }
        return val;
    }
}
