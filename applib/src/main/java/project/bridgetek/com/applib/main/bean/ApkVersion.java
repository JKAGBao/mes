package project.bridgetek.com.applib.main.bean;

/**
 * Created by Cong Zhizhong on 18-9-3.
 */

public class ApkVersion {
    String name;
    String version;
    String filePath;

    public ApkVersion() {
    }

    public ApkVersion(String name, String version, String filePath) {
        this.name = name;
        this.version = version;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
