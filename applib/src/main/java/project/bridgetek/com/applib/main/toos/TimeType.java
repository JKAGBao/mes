package project.bridgetek.com.applib.main.toos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import project.bridgetek.com.bridgelib.toos.Constants;

/**
 * Created by Cong Zhizhong on 18-6-27.
 */

public class TimeType {

    //当前时间字符串
    public static String dateToString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATETIME_TYPE);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 时间只要日期后面的
     *
     * @param strTime 时间字符
     * @return
     * @throws ParseException
     */
    public static String stringToDate(String strTime)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATETIME_TYPE);
        Date date = null;
        date = formatter.parse(strTime);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(Constants.TIME_TYPE);// HH:mm:ss
        String format = simpleDateFormat1.format(date);
        return format;
    }

    public static String toData(String strTime)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATETIME);
        Date date = null;
        date = formatter.parse(strTime);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(Constants.TIME_TYPE);// HH:mm:ss
        String format = simpleDateFormat1.format(date);
        return format;
    }

    //要去掉年份的时间
    public static String getDate(String strTime)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATETIME_TYPE);
        Date date = null;
        date = formatter.parse(strTime);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd HH:mm");// HH:mm:ss
        String format = simpleDateFormat1.format(date);
        return format;
    }

    //时间戳转换成想要的时间格式
    public static String stringToDat(long cc_time)
            throws ParseException {
        String strTime = getStrTime(cc_time);
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATETIME_TYPE);
        Date date = null;
        date = formatter.parse(strTime);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(Constants.TIME_TYPE);// HH:mm:ss
        String format = simpleDateFormat1.format(date);
        return format;
    }

    //字符串转时间戳
    public static long getTime(String user_time) {
        long re_time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_TYPE);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = Long.parseLong(str.substring(0, 10));
        } catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }

    public static long getUpperTime(String user_time) {
        long re_time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = Long.parseLong(str.substring(0, 10));
        } catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }

    public static long getTimeType(String user_time) {
        long re_time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = Long.parseLong(str.substring(0, 10));
        } catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }

    //时间戳转化成字符串
    public static String getStrTime(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_TYPE);
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    //系统当前时间
    public static long getNowTime() {
        //获取系统时间的10位时间戳
        long time = System.currentTimeMillis() / 1000;
        return time;
    }

    public static String getStrState(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.STATEDAY);
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
