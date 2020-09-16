package project.bridgetek.com.applib.main.bean.body;

/**
 * Created by czz on 19-5-17.
 */

public class SplitDetail {
    private String key;
    private String value;

    public SplitDetail(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
