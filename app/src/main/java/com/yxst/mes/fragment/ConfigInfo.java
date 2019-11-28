package com.yxst.mes.fragment;

import com.yxst.mes.R;
import com.yxst.mes.activity.adapter.PrimaryItem;
import com.yxst.mes.fragment.bean.Item;

import java.util.ArrayList;
import java.util.List;

public class ConfigInfo {
    //Item巡检项的 检测标准值
    public static final String ITEM_CHECK_NORMAL = "正常";
    public static final String ITEM_CHECK_ABNORMAL = "异常";
    public static final int UPLOAD_IMAGE = 1;
    public static final int UNDONE_IMAGE = 0;
    public static final int ITEM_INSPECT_STATUS = 1;
    public static final int ITEM_UNINSPECT_STATUS = 0;
    public static final int ITEM_UPLOAD_STATUS = 1;
    public static final int ITEM_UNUPLOAD_STATUS = 0;
    public static final int ITME_CHECK_STATUS = 1;
    public static final int ITME_UNCHECK_STATUS = 2;
    public static final int ITME_CHECKED_STATUS = 3;
    public static final int RECORD_UPLOAD_STATUS = 2;

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
    private final String[] ITEM_LIST =
            {"线路管理","测温测振","待检设备","已检设备","润滑设备","漏滑设备","漏检设备","设备监测","设置",""};

    private final int[] ITME_IMAGE = {R.drawable.main1,R.drawable.deviceinfo,R.drawable.waitinspect,R.drawable.locked64,
            R.drawable.leakinpect64, R.drawable.main,R.drawable.line,R.drawable.monitor,R.drawable.main3,R.color.colorPrimary};
    public final List<Item> ITEMS = new ArrayList<Item>();
    public final List<PrimaryItem> ITEMS_PRIMARY = new ArrayList<PrimaryItem>();

    private final int[] ITEMS_COLOR = {R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorPrimaryDark,R.color.colorPrimary};
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
