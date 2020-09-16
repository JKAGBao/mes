package project.bridgetek.com.bridgelib.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import project.bridgetek.com.bridgelib.app.Black;

/**
 * Created by warming on 2017/12/25.
 */

public class DimenUtil {
    public static int getScreenWidth(){
        final Resources resources= Black.getApplicationContext().getResources();
        final DisplayMetrics dm=resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        final Resources resources= Black.getApplicationContext().getResources();
        final DisplayMetrics dm=resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
