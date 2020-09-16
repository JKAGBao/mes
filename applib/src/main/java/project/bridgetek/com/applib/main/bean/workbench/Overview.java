package project.bridgetek.com.applib.main.bean.workbench;

/**
 * Created by czz on 19-5-5.
 */

public class Overview {

    /**
     * zhengChang : 2
     * liXian : 1
     * gaoJing : 0
     */

    private String zhengChang;
    private String liXian;
    private String gaoJing;

    public Overview() {
    }

    public Overview(String zhengChang, String liXian, String gaoJing) {
        this.zhengChang = zhengChang;
        this.liXian = liXian;
        this.gaoJing = gaoJing;
    }

    public String getZhengChang() {
        return zhengChang;
    }

    public void setZhengChang(String zhengChang) {
        this.zhengChang = zhengChang;
    }

    public String getLiXian() {
        return liXian;
    }

    public void setLiXian(String liXian) {
        this.liXian = liXian;
    }

    public String getGaoJing() {
        return gaoJing;
    }

    public void setGaoJing(String gaoJing) {
        this.gaoJing = gaoJing;
    }
}
