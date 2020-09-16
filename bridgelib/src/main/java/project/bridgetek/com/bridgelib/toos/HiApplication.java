package project.bridgetek.com.bridgelib.toos;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by Cong Zhizhong on 18-7-10.
 */

public class HiApplication {
    public static Typeface BOLD, REGULAR, MEDIUM;
    private static HttpProxyCacheServer proxy;
    public static Context CONTEXT;

    public static void getTypeface(Context context) {
        BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansHans-Bold.otf");
        REGULAR = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansHans-Regular.otf");
        MEDIUM = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansHans-Medium.otf");
        CONTEXT = context;
    }

    public static HttpProxyCacheServer getInstance(Context context) {
        if (proxy == null) {
            proxy = new HttpProxyCacheServer(context);
        }
        return proxy;
    }

    public static Context getApplication() {
        return CONTEXT;
    }
}
