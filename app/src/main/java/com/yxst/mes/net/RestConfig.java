package com.yxst.mes.net;

public class RestConfig {
    /*
    //获取巡检线路 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetInspectionLine
    //根据id获取巡检线路 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetLineById?lineid=线路id(int)
    //根据巡检线路获取设备 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetDeviceByLine?lineid=线路id(int)
    //根据线路获取巡检计划 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetPlanByLine?lineid=线路id(int)
    //根据线路获取巡检周期 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetCycleByLine?lineid=线路id(int)
    //根据巡检线路设备获取巡检项目 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetItemByLine?lineid=线路id(int)
    //获取字典表 HTTP GET
    http://60.164.211.4/webapi/api/v1/inspection/GetDictionaries
     */
   public static final String BASE_URL = "http://60.164.211.4:9800";
 //   public static final String BASE_URL = "http://zx.qlssn.com:9800";
    public static String VERSION_UPDATE_URL = "http://60.164.211.4:9800/AppUpdate/version/update.xml";
    //设备线路
    public static final String LINT_URL = "webapi/api/v1/inspection/GetInspectionLine";
    //根据线路获取巡检设备
    public static final String DEVICE_URL = "webapi/api/v1/inspection/GetDeviceByLine?lineid=";
    //获取巡检项webapi/api/v1/inspection/GetItemByLine?lineid=1
    public static final String ITEM_URL = "webapi/api/v1/inspection/GetItemByLine";
    //获取部位
    public static final String PLACE_URL = "webapi/api/v1/inspection/GetDevicePlace";
    //漏检设备
    public static final String UNDETEDT_URL = "webapi/api/v1/inspection/GetMissedDetection";
    //根据线路获取巡检计划 HTTP GET
    public static final String PLAN_URL = "webapi/api/v1/inspection/GetPlanByLine?lineid=";
    //根据线路获取巡检周期 HTTP GET
    public static final String CYCLE_URL = "webapi/api/v1/inspection/GetCycleByLine?lineid=";
    //巡检项
    public static final String INSPETION_ULR = "webapi/api/v1/inspection/GetInspectionItem";
}
