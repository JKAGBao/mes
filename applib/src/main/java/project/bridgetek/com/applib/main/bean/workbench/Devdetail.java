package project.bridgetek.com.applib.main.bean.workbench;

import java.io.Serializable;
import java.util.List;

/**
 * Created by czz on 19-5-6.
 */

public class Devdetail implements Serializable {

    /**
     * OrgName : 西昌钢钒
     * PlName : 钢铁厂电动房
     * devName : 上水泵
     * devCode : 20190201
     * devStatus : 1
     * devImgPath :
     * points : [{"pointName":"内部测试","getDate":"2019/4/3 18:10:00","signals":[{"signalTypeName":"1","value":"0.109","unit":"m/s2"},{"signalTypeName":"2","value":"0.128","unit":"mm/s"},{"signalTypeName":"16","value":"21","unit":"℃"}]}]
     * curWarningEntitys : [{"warningBegTime":"2019/4/3 18:10:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：内部测试 传感器编码：3178\n信号类型：温度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 18:10:00"},{"warningBegTime":"2019/4/3 18:00:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：内部测试 传感器编码：3178\n信号类型：速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 18:00:00"},{"warningBegTime":"2019/4/3 18:00:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：内部测试 传感器编码：3178\n信号类型：加速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 18:00:00"},{"warningBegTime":"2019/4/3 17:55:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：测点1 传感器编码：3166\n信号类型：速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 17:55:00"},{"warningBegTime":"2019/4/3 17:55:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：测点1 传感器编码：3166\n信号类型：温度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 17:55:00"},{"warningBegTime":"2019/4/3 17:55:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：测点1 传感器编码：3166\n信号类型：加速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 17:55:00"},{"warningBegTime":"2019/4/3 17:25:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：内部测试 传感器编码：3178\n信号类型：速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 17:25:00"},{"warningBegTime":"2019/4/3 17:15:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：测点1 传感器编码：3166\n信号类型：加速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 17:15:00"},{"warningBegTime":"2019/4/3 16:50:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：测点1 传感器编码：3166\n信号类型：加速度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 16:50:00"},{"warningBegTime":"2019/4/3 16:50:00","warningContent":"公司：西昌钢钒 机组：钢铁厂电动房\n设备：上水泵 测点：测点1 传感器编码：3166\n信号类型：温度！ 告警描述：无数据上报！\n最近取值时间：2019/4/3 16:50:00"}]
     * hisWarningEntitys : []
     */

    private String OrgName;
    private String PlName;
    private String devName;
    private String devCode;
    private String devStatus;
    private String devImgPath;
    private List<Devs.Dev.PointsBean> points;
    private List<CurWarningEntitysBean> curWarningEntitys;
    private List<CurWarningEntitysBean> curOfflineEntitys;
    private String isAttention;

    public Devdetail() {
    }

    public Devdetail(String orgName, String plName, String devName, String devCode, String devStatus, String devImgPath, List<Devs.Dev.PointsBean> points, List<CurWarningEntitysBean> curWarningEntitys, List<CurWarningEntitysBean> curOfflineEntitys, String isAttention) {
        OrgName = orgName;
        PlName = plName;
        this.devName = devName;
        this.devCode = devCode;
        this.devStatus = devStatus;
        this.devImgPath = devImgPath;
        this.points = points;
        this.curWarningEntitys = curWarningEntitys;
        this.curOfflineEntitys = curOfflineEntitys;
        this.isAttention = isAttention;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String OrgName) {
        this.OrgName = OrgName;
    }

    public String getPlName() {
        return PlName;
    }

    public void setPlName(String PlName) {
        this.PlName = PlName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getDevImgPath() {
        return devImgPath;
    }

    public void setDevImgPath(String devImgPath) {
        this.devImgPath = devImgPath;
    }

    public List<Devs.Dev.PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<Devs.Dev.PointsBean> points) {
        this.points = points;
    }

    public List<CurWarningEntitysBean> getCurWarningEntitys() {
        return curWarningEntitys;
    }

    public void setCurWarningEntitys(List<CurWarningEntitysBean> curWarningEntitys) {
        this.curWarningEntitys = curWarningEntitys;
    }

    public List<CurWarningEntitysBean> getCurOfflineEntitys() {
        return curOfflineEntitys;
    }

    public void setCurOfflineEntitys(List<CurWarningEntitysBean> curOfflineEntitys) {
        this.curOfflineEntitys = curOfflineEntitys;
    }

    public static class CurWarningEntitysBean implements Serializable {
        /**
         * warningBegTime : 2019/4/3 18:10:00
         * warningContent : 公司：西昌钢钒 机组：钢铁厂电动房
         * 设备：上水泵 测点：内部测试 传感器编码：3178
         * 信号类型：温度！ 告警描述：无数据上报！
         * 最近取值时间：2019/4/3 18:10:00
         */

        private String warningBegTime;
        private String warningContent;
        private String reasonContent;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public CurWarningEntitysBean() {
        }

        public CurWarningEntitysBean(String warningBegTime, String warningContent, String reasonContent, String id) {
            this.warningBegTime = warningBegTime;
            this.warningContent = warningContent;
            this.reasonContent = reasonContent;
            this.id = id;
        }

        public String getReasonContent() {
            return reasonContent;
        }

        public void setReasonContent(String reasonContent) {
            this.reasonContent = reasonContent;
        }

        public String getWarningBegTime() {
            return warningBegTime;
        }

        public void setWarningBegTime(String warningBegTime) {
            this.warningBegTime = warningBegTime;
        }

        public String getWarningContent() {
            return warningContent;
        }

        public void setWarningContent(String warningContent) {
            this.warningContent = warningContent;
        }
    }
}
