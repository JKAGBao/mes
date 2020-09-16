package project.bridgetek.com.applib.main.bean.workbench;

import java.util.List;

/**
 * Created by czz on 19-5-5.
 */

public class Devs {
    private List<Dev> dev;

    public Devs(List<Dev> dev) {
        this.dev = dev;
    }

    public Devs() {
    }

    public List<Dev> getDev() {
        return dev;
    }

    public void setDev(List<Dev> dev) {
        this.dev = dev;
    }

    public static class Dev {
        /**
         * OrgName : 西昌钢钒
         * devName : 上水泵
         * devCode : 20190201
         * devStatus : 1
         * devImgPath :
         * points : [{"pointName":"内部测试","getDate":"2019/4/3 18:10:00","signals":[{"signalTypeName":"1","value":"0.109","unit":"m/s2"},{"signalTypeName":"2","value":"0.128","unit":"mm/s"},{"signalTypeName":"16","value":"21","unit":"℃"}]}]
         */

        private String OrgName;
        private String devName;
        private String devCode;
        private String devStatus;
        private String devImgPath;
        private String isAttention;
        private List<PointsBean> points;

        public Dev() {
        }

        public Dev(String orgName, String devName, String devCode, String devStatus, String devImgPath, String isAttention, List<PointsBean> points) {
            OrgName = orgName;
            this.devName = devName;
            this.devCode = devCode;
            this.devStatus = devStatus;
            this.devImgPath = devImgPath;
            this.isAttention = isAttention;
            this.points = points;
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

        public List<PointsBean> getPoints() {
            return points;
        }

        public void setPoints(List<PointsBean> points) {
            this.points = points;
        }

        public static class PointsBean {
            /**
             * pointName : 内部测试
             * getDate : 2019/4/3 18:10:00
             * signals : [{"signalTypeName":"1","value":"0.109","unit":"m/s2"},{"signalTypeName":"2","value":"0.128","unit":"mm/s"},{"signalTypeName":"16","value":"21","unit":"℃"}]
             */

            private String pointName;
            private String getDate;
            private List<SignalsBean> signals;
            private String pointCode;
            private boolean isSelect = false;

            public PointsBean() {
            }

            public PointsBean(String pointName, String getDate, List<SignalsBean> signals, String pointCode, boolean isSelect) {
                this.pointName = pointName;
                this.getDate = getDate;
                this.signals = signals;
                this.pointCode = pointCode;
                this.isSelect = isSelect;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getPointCode() {
                return pointCode;
            }

            public void setPointCode(String pointCode) {
                this.pointCode = pointCode;
            }

            public String getPointName() {
                return pointName;
            }

            public void setPointName(String pointName) {
                this.pointName = pointName;
            }

            public String getGetDate() {
                return getDate;
            }

            public void setGetDate(String getDate) {
                this.getDate = getDate;
            }

            public List<SignalsBean> getSignals() {
                return signals;
            }

            public void setSignals(List<SignalsBean> signals) {
                this.signals = signals;
            }

            public static class SignalsBean {
                /**
                 * signalTypeName : 1
                 * value : 0.109
                 * unit : m/s2
                 */

                private String signalTypeName;
                private String value;
                private String unit;
                private boolean isSelect = false;
                private String state;

                public SignalsBean() {
                }

                public SignalsBean(String signalTypeName, String value, String unit, boolean isSelect, String state) {
                    this.signalTypeName = signalTypeName;
                    this.value = value;
                    this.unit = unit;
                    this.isSelect = isSelect;
                    this.state = state;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public boolean isSelect() {
                    return isSelect;
                }

                public void setSelect(boolean select) {
                    isSelect = select;
                }

                public String getSignalTypeName() {
                    return signalTypeName;
                }

                public void setSignalTypeName(String signalTypeName) {
                    this.signalTypeName = signalTypeName;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }
            }
        }
    }
}
