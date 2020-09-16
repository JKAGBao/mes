package project.bridgetek.com.bridgelib.app;

import android.os.Handler;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;


public class Configurator {
    // 配置信息存储的集合
    private static final HashMap<Object, Object> BLACK_CONFIGS = new HashMap<>();

    // 字体图标
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    // 封装全局唯一的Handler
    private static final Handler HANDLER = new Handler();

    //私有化构造方法，将配置开始置为false
    private Configurator() {
        BLACK_CONFIGS.put(ConfigType.CONFIG_READY, false);
        BLACK_CONFIGS.put(ConfigType.HANDLER, HANDLER);
    }

    // 静态内部类实例化单例
    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    // 外部获取唯一单例的方法
    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    // 直接返回配置信息的集合
    final HashMap<Object, Object> getLatteConfigs() {
        return BLACK_CONFIGS;
    }

    // 完成配置之后置为true
    public final void configure() {
        initIcons();
        Logger.addLogAdapter(new AndroidLogAdapter());
        BLACK_CONFIGS.put(ConfigType.CONFIG_READY, true);
    }

    // 配置api信息
    public final Configurator withApiHost(String host) {
        BLACK_CONFIGS.put(ConfigType.API_HOST, host);
        return this;
    }

    // 配置字体图标库
    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }

    // 初始化字体图标库
    public final void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }


    // 检查配置流程是否完成
    private void checkConfiguration() {
        final boolean isReady = (boolean) BLACK_CONFIGS.get(ConfigType.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Config is not ready");
        }
    }

    // 从外部获取配置信息时先检查是否配置完成
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = BLACK_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) BLACK_CONFIGS.get(key);
    }

}
