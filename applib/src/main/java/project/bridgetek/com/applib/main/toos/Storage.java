package project.bridgetek.com.applib.main.toos;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.List;
import java.util.StringTokenizer;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.bridgelib.toos.Conversion;

/**
 * Created by Cong Zhizhong on 18-6-28.
 */

public class Storage {
    public static List<ResultFileInfo> setBase64(List<ResultFileInfo> list) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String s = Conversion.fileToBase64(list.get(i).getFileName());
                list.get(i).setFileDataForBase64String(s);
                list.get(i).setCheckItem_ID(list.get(i).getFileName());
                String[] split = list.get(i).getFileName().split("/");
                String s1 = split[split.length - 1];
                list.get(i).setFileName(s1);
            }
            return list;
        } else {
            return null;
        }
    }

    public static ProgressDialog getProgress(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.app_toos_storage_progress_text));
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static ProgressDialog getPro(Context context, String message) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static ProgressDialog getProCan(Context context, String message) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(true);
        return mProgressDialog;
    }

    public static String formateRate(String rateStr) {
        if (rateStr.indexOf(".") != -1) {
            //获取小数点的位置
            int num = 0;
            num = rateStr.indexOf(".");

            //获取小数点后面的数字 是否有两位 不足两位补足两位
            String dianAfter = rateStr.substring(0, num + 1);
            String afterData = rateStr.replace(dianAfter, "");
            if (afterData.length() < 2) {
                afterData = afterData + "0";
            } else {
                afterData = afterData;
            }
            return rateStr.substring(0, num) + "." + afterData.substring(0, 2);
        } else {
            if (rateStr == "1") {
                return "100";
            } else {
                return rateStr;
            }
        }
    }

    public static String format(String s) {
        String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str;
    }

    public static String getLabelCode(String s) {
        String split = "?";
        StringTokenizer token = new StringTokenizer(s, split);
        return token.nextToken();
    }
}
