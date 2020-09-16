package com.yxst.inspect;

import com.yxst.inspect.activity.adapter.PrimaryItem;
import com.yxst.inspect.fragment.bean.Item;
import com.yxst.inspect.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class ConfigInfo {
    public static String WSDL_URI =  MyApplication.BASE_URL+"/FilesServer/WebService.asmx";//wsdl 的uri
    private static String WSDL_URI_YUN =  MyApplication.BASE_URL+"/FileServer/WebService.asmx";//wsdl 的uri
    //漳县地址接口
//    public static final String BASE_URL = "http://www.uxhdb.com";


    //   public static final String BASE_URL = "http://zx.qlssn.com:9800"
    //   public static String VERSION_UPDATE_URL = "http://60.164.211.4:9800/AppUpdate/version/update.xml";
    public static String VERSION_UPDATE_URL = "http://www.uxhdb.com/INSPECT/qls-zx/version.xml";
    public static String updateContent =
            "1、WIFI切换暂停避免热点关闭\n2、测温测振异常关闭的情况更改\n" +
                    "3、设备一对多卡时，流程优化\n4、巡检完，确定跳转到已检设备中";
    //Item巡检项的 检测标准值
    public static final String ITEM_CHECK_NORMAL = "正常";
    public static final String ITEM_CHECK_ABNORMAL = "异常";
    public static final int UPLOAD_IMAGE = 1;
    public static final int UNDONE_IMAGE = 0;
    public static final int CHECKED_STATUS = 2;//已巡检2/3
    public static final int ITEM_UNCHECK_STATUS = 1;//待巡检
    public static final int ITEM_UPLOAD_STATUS = 1;
    public static final int ITEM_UNUPLOAD_STATUS = 0;
    public static final int ITME_CHECKED_STATUS = 3;


    /*
   巡检项的 检查类型
   1 测振型
   2 观察型
   3 测温型
   4 抄表型
   5 测温测振型
    */
    public static final int ITME_TYPE_VIBRATE = 1;
    public static final int ITME_TYPE_OBSERVE = 2;
    public static final int ITME_TYPE_TEMPERATURE = 3;
    public static final int ITME_TYPE_READMETER = 4;
    public static final int ITME_TYPE_VIBRATE_TEMP = 5;

    public static final int BIND_DEVICE = 10;
    public static final int LINE = 0;
    public static final int TEST_VIB = 1;
    public static final int WAIT_INSPECT = 2;
    public static final int FINISH_INSPECT = 3;
    public static final int WAIT_LUBE = 4;
    public static final int UN_LUBE = 5;
    public static final int UN_INSPECT= 6;
    public static final int MONITOR= 7;
    public static final int SETTING = 8;
    public static final int BLUE = 9;
    private final String[] ITEM_LIST =
            {"线路管理","测温测振","待检设备","已检设备","润滑设备","漏滑设备","漏检设备","设备监测","设置","蓝牙检测"};

    private final int[] ITME_IMAGE = {R.drawable.main1,R.drawable.deviceinfo,R.drawable.waitinspect,R.drawable.locked64,
            R.drawable.leakinpect64, R.drawable.main,R.drawable.line,R.drawable.monitor,R.drawable.main3,R.drawable.deviceinfo};
    public final List<Item> ITEMS = new ArrayList<Item>();
    public final List<PrimaryItem> ITEMS_PRIMARY = new ArrayList<PrimaryItem>();

    private final int[] ITEMS_COLOR = {R.color.colorPriamryOne,R.color.colorPriamryOne,R.color.colorPriamryOne,R.color.colorPriamryOne};
    private final void initItem(){
        for(int i=0;i<ITEM_LIST.length;i++){
            Item item = new Item(ITEM_LIST[i],R.drawable.item);
            ITEMS.add(item);
        }
        for(int i=0;i<ITEM_LIST.length;i++){
            PrimaryItem pitem = new PrimaryItem(ITEM_LIST[i],ITME_IMAGE[i],ITEMS_COLOR[i%4]);
            ITEMS_PRIMARY.add(pitem);
        }
//        for(int i=0;i<ItemName.values().length;i++){
//                PrimaryItem item = new PrimaryItem(ItemName)
//        }

    }
    private ConfigInfo(){
        initItem();
    }
    public static ConfigInfo getInstance(){
            return Holder.CONFIG_INFO;
    }
    private static final class Holder{
        private static final ConfigInfo CONFIG_INFO = new ConfigInfo();

    }
}
