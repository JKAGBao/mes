package project.bridgetek.com.applib.main.toos;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

/**
 * 登陆注销功能
 * Created by DragoNar on 2018/7/11.
 */

public class ActivityCollectorUtil {
    public static ArrayList<Activity> mActivityList = new ArrayList<Activity>();

    /**
     * onCreate() 添加
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity);
        }
    }

    /**
     * onDestroy() 删除
     *
     * @param activity
     */
    public static void removeAllActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    /**
     * 关闭所有Activity
     */
    public static void finishAllActivity() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                Log.e("Logout", "111");
                activity.finish();
            }
        }
    }
}
