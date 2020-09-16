package project.bridgetek.com.bridgelib.app;

import android.content.Context;
import android.os.Handler;

public class Black {
    // 传入全局context
    public static Configurator init(Context context) {
        Configurator.getInstance()
                .getLatteConfigs()
                .put(ConfigType.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }

    // 获取Configurator的单例
    private static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    // 对外提供配置信息
    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    // 对外提供唯一的Handler
    public static Handler getHandler() {
        return getConfiguration(ConfigType.HANDLER);
    }

    // 对外提供全局context
    public static Context getApplicationContext() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }

}
