package project.bridgetek.com.bridgelib.toos;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

/**
 * Created by Cong Zhizhong on 18-9-3.
 */

public class Tools {
    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 2 * 获取版本号 3 * @return 当前应用的版本号 4
     */
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            int versioncode = info.versionCode;
            return versioncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
