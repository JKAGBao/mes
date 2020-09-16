package project.bridgetek.com.bridgelib.toos;

import java.security.PublicKey;

import retrofit2.http.PUT;

/**
 * Created by Cong Zhizhong on 18-6-6.
 */

public final class Constants {
    public static String API = "http://121.196.220.45:8090";
    public static final String LOGIN = "/api/Account/Login";
    public static final String TASKS = "/api/Task/Tasks";
    public static final String ACCOUNT_DEVICES = "/api/Account/Devices";
    public static final String ACCOUNTLINES = "/api/Account/AccountLines";
    public static final String CHECKITEMS = "/api/Task/CheckItems";
    public static final String CHECKITEM = "/api/Result/CheckItem";
    public static final String RESULT_EXCEPTION = "/api/Result/Exception";
    public static final String LABEL = "/api/Result/Label";
    public static final String EXCEPTION_DETAIL = "/exception/";
    public static final String EXCEPTIONS = "/api/Account/Exceptions";
    public static final String APKVERSION = "/Apk/GetApkVersionByName";
    public static final String SERVERTEST = "/api/Account/ServerTest";
    public static final String LABELCHECKITEMS = "/api/Task/LabelCheckItems";
    public static final String UNPLANCHECKITEM = "/api/Result/UnplanCheckItem";
    public static String CURRENTUSER = "";
    /**
     * 主页地边栏颜色
     */
    public static final int CLICKCOLOR = 0xFFFDC52D;
    public static final int COLOR = 0xFF929192;
    //设备列表Adapter颜色
    public static final int CANOTPOINT = 0xFFEEE9E9;
    //单选按钮颜色
    public static final int GRAY = 0XFFa9a9a9;
    public static final int RED = 0XFFdc143c;
    public static final int LIMEGREEN = 0XFF8fc31f;
    public static String STARTSTOP = "";
    public static boolean ISLIANJIE = true;
    /**
     * 传值时用的键
     */
    public static final String SERVICETIME = "serviceTime";
    public static final String SENSORTIME = "sensorTime";
    public static final String INITIALOPEN = "initialOpen";
    public static final String TASKID = "TaskID";
    public static final String OVERTASKID = "OverTaskID";
    public static final String LABELID = "LabelID";
    public static final String REEXCEPTION = "Exception";
    public static final String AUTONOMY = "autonomy";
    public static final String LINEID = "LineID";
    public static final String LINENAME = "LineName";
    public static final String ISFIRST = "IsFirst";
    public static final String PATH = "path";
    public static final String CB = "CB";
    public static final String ZD = "CZ";
    public static final String CW = "CW";
    public static final String GC = "GC";
    public static final String WATCH = "Watch";
    public static final String DEVICE = "Device";
    public static final String ACCOUNTID = "accountid";
    public static final String USERNAME = "username";
    public static final String BEDGEID = "badgeid";
    public static final String GROUPNAME = "groupname";
    public static final String CHECKITEMINFO = "checkItemInfo";
    public static final String CHECKINFO = "checkInfo";
    public static final String TASKIDS = "taskids";
    public static final String ACCOUNT = "account";
    public static final String ID = "id";
    public static final String PASSWORD = "password";
    public static final String GROUPID = "groupid";
    public static final String DATETIME_TYPE = "yyyy/MM/dd HH:mm:ss";
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_TYPE = "HH:mm";
    public static final String PHOTO = "photo";
    public static final String TYPE = "type";
    public static final String RESULTFILE = "ResultFile";
    public static final String SEARCHKEYWORDS = "searchKeywords";
    public static final String WAVEDATA = "WaveData";
    public static final String INTENTFLOAT = "float";
    public static final String CONVERT = "convert";
    public static final String START_TM = "Start_TM";
    public static final String PROTIME = "pro_time";
    public static final String PROUSER = "pro_user";
    public static final String SKIP = "skip";
    public static final String CYCLETIME = "cycleTime";
    public static final String DEVICENAME = "deviceName";
    public static final String DEVICEADDRESS = "deviceAddress";
    public static final String SERVCERIP = "SERVCERIP";
    public static final String LOGINACCOUTS = "LoginAccouts";
    public static final String ABNORMODIFY = "modify";
    public static final String REANALYSE = "RgAnalyse";
    public static final String ISRECORD = "isRecord";
    public static final String LABELCODE = "LabelCode";
    public static final String DEPARTMENTNAME = "departmentname";
    public static final String COMPANYNAME = "companyname";
    public static final String EXCEPTIONLEVE = "Exceptionleve";
    public static final String CHANGE = "change";
    public static final String EQUIPMENTSTATUS = "EquipmentStatus";
    public static final String EQUIPMENTSTATUSENABLED = "EquipmentStatusEnabled";
    public static final String NUMS = "Nums";
    public static final String VIBFEATURES = "vibfeatures";
    /**
     * 数据库存储时用到的键
     */
    public static final String TASKNAME = "TaskName";
    public static final String TASKTYPE = "TaskType";
    public static final String TASKPLANSTARTTIME = "TaskPlanStartTime";
    public static final String TASKPLANENDTIME = "TaskPlanEndTime";
    public static final String TASKUPDATETIME = "TaskUpdateTime";
    public static final String SUBMIT = "submit";
    public static final String KEY_ID = "_id";
    public static final String LABLETYPE = "LableType";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String TEMPERATURE = "temperature";
    public static final String FILE_LOG_PATH = "/storage/emulated/0/Android/data/project.bridgetek.com.bridgetekapp/cache/recordtest/log";
    public static final String PERMISSIONS = "permissions";
    public static final String LABLE = "Lable";
    public static final String FREEPATROL = "FreePatrol";
    public static final String ONLINE = "Online";
    //状态检测服务器
    public static final String SETSERVICESTATE = "set_service_state";
    public static final String OVERVIEW = "/api/devstatus/overview";
    public static final String DEVSTATUSDEVS = "/api/devstatus/devs";
    public static final String ADDATTENTION = "/api/dev/addattention";
    public static final String REMOVEATTENTION = "/api/dev/removeattention";
    public static final String DEVDETAIL = "/api/devstatus/devdetail_V2";
    public static final String ATTENTIONLIST = "/api/dev/attentionlist";
    public static final String MOBJECTINFOS = "/api/devstatus/MobjectInfos";
    public static final String TREENODES = "/api/devstatus/treeNodes";
    public static final String TREND = "/api/devstatus/trend";
    public static final String STATEDAY = "yyyy-MM-dd";
    public static final String OFFREASONITEM = "/api/devstatus/GetOffReasonItem";
    public static final String OFFLINEREASON = "/api/devstatus/SendOfflineReason";
    public static final String DEVLOGIN = "/api/dev";
    public static boolean FIRST_CONNECTION = false;
    /**
     * workbench
     */
    public static final String MARK = "Mark";
    public static final String DEVCODE = "devCode";
    public static final String HOMEPAGE = "HomePage";
    //设备搜索
    public static final String DEVICESEARCHSEARCH = "device_search_search";
}
