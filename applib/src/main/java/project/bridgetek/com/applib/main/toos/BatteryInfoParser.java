package project.bridgetek.com.applib.main.toos;

/**
 * Created by DragoNar
 */

import java.util.Calendar;

/**
 * 电量信息解析类
 */
public class BatteryInfoParser {

    private int level;
    private int cycleNum;
    private Status status;
    private Calendar lastChargedDate;

    /**
     * 充电状态
     */
    static enum Status {
        UNKNOWN, LOW, FULL, CHARGING, NOT_CHARGING;

        public static Status fromByte(byte b) {
            switch (b) {
                case 1:
                    return LOW;
                case 2:
                    return CHARGING;
                case 3:
                    return FULL;
                case 4:
                    return NOT_CHARGING;
                default:
                    return UNKNOWN;
            }
        }
    }


    public BatteryInfoParser(byte[] data) {
        level = data[0];
        //预留参数/最后充电日期/电池健康信息等
        //status = Status.fromByte(data[9]);
//        cycleNum = 0xffff & (0xff & data[7] | (0xff & data[8]) << 8);
//        lastChargedDate = Calendar.getInstance();
//
//        lastChargedDate.set(Calendar.YEAR, data[1] + 2000);
//        lastChargedDate.set(Calendar.MONTH, data[2]);
//        lastChargedDate.set(Calendar.DATE, data[3]);
//
//        lastChargedDate.set(Calendar.HOUR_OF_DAY, data[4]);
//        lastChargedDate.set(Calendar.MINUTE, data[5]);
//        lastChargedDate.set(Calendar.SECOND, data[6]);
    }

    /**
     * 电池电量百分比显示
     */
    public int getLevel() {
        return level;
    }

    /**
     * 充电次数
     */
    public int getCycleNum() {
        return cycleNum;
    }

    /**
     * 充电的状态
     *
     * @see Status
     */
    public Status getStatus() {
        return status;
    }

    public String getStatusToString() {
        return status.toString();
    }

    /**
     * 上一次的充电时间
     */
    public Calendar getLastChargedDate() {
        return lastChargedDate;
    }

}