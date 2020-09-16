package project.bridgetek.com.applib.main.toos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cong Zhizhong on 18-6-12.
 */

public class BlackNumDbHelper extends SQLiteOpenHelper {


    public BlackNumDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表：自增长的主键 ，
        db.execSQL("create table taskInfo(_id integer primary key autoincrement, TaskID varchar(100) , LineID varchar(20), TaskName varchar(20), LineName varchar(20), TaskType varchar(20), LableType varchar(20), TaskPlanStartTime long, TaskPlanEndTime long, TaskUpdateTime long, submit boolean, accountid varchar(100), EquipmentStatusEnabled varchar(10), Sync varchar(10));");
        db.execSQL("create table checkItemInfo(_id integer primary key autoincrement, CheckItemID varchar(100) , MobjectCode varchar(20), MobjectName varchar(20), LabelID varchar(20), LabelName varchar(20), TaskID varchar(100),LineID varchar(20), CheckItemDesc varchar(600), ESTStandard varchar(400), CheckType varchar(20), ZhenDong_Type varchar(20), Zhendong_PP varchar(20), AlarmType_ID varchar(20), StandardValue varchar(20), ParmLowerLimit varchar(20), ParmUpperLimit varchar(20), CheckItemUpdateTime varchar(20), UpperLimit1 float, UpperLimit2 float, UpperLimit3 float, UpperLimit4 float, LowerLimit1 float, LowerLimit2 float, LowerLimit3 float, LowerLimit4 float, CheckOrderNo int, submit boolean," +
                "TaskItemID varchar(100), LabelCode varchar(20), ObserveOptions varchar(20), ShiftName varchar(20), GroupName varchar(20), TaskPlanStartTime varchar(20), TaskPlanEndTime varchar(20), NextTaskDate varchar(20), NextTaskDateTimePeriods varchar(20), ZhenDong_Freq varchar(20), ZhenDong_Points varchar(20), EquipmentStatusFilter varchar(20), SyncSamplingTag varchar(20), state int);");
        //checkItem是存储得到结果表
        db.execSQL("create table checkItem(_id integer primary key autoincrement , Task_ID varchar(100), LabelID varchar(20), MobjectCode varchar(20), MobjectName varchar(20), CheckItem_ID varchar(20), Start_TM varchar(20), Complete_TM varchar(20), UserID varchar(20), UserName varchar(20), ResultValue varchar(20), Rate integer, MObjectStatus varchar(20), Exception_YN varchar(20), ExceptionID varchar(20), Memo_TX varchar(20), Time_NR integer, GroupName varchar(20), PDADevice varchar(20), ExceptionTransfer_YN varchar(20), submit boolean" +
                ", TaskItemID varchar(100), TaskType varchar(20), LineId varchar(100), UserCode varchar(20), UserPostId varchar(20), ExceptionLevel varchar(20), ShiftName varchar(20), VibFeatures varchar(100), Points integer, SignalType varchar(100));");
        //resultFile存储文件
        db.execSQL("create table resultFile(_id integer primary key autoincrement, FileType varchar(20) , FileName varchar(20), FileDataForBase64String varchar(400),CheckItem_ID varchar(20),Exception_ID varchar(20));");
        //存储异常
        db.execSQL("create table exception(_id integer primary key autoincrement, ExceptionTitle varchar(100), Task_ID varchar(100), LabelID varchar(20), MobjectName varchar(20), MobjectCode varchar(20), CheckItem_ID varchar(20), UserID varchar(20), UserName varchar(20), ResultValue varchar(20), Rate integer, WaveData varchar(20), Memo varchar(20), GroupName varchar(20), PDADevice varchar(20), status varchar(20), Found_TM varchar(20), Duration_TM integer, updatetime integer, submit boolean);");
        //设备储存表
        db.execSQL("create table deviceInfo(_id integer primary key autoincrement, accountId varchar(20), DeviceCode varchar(20), DeviceName varchar(20), DeviceLocation varchar(20), UpdateTime varchar(20));");
        //存储排序结果自动跳转使用，
        db.execSQL("create table checkMobject(_id integer primary key autoincrement, CheckItemID varchar(20) , MobjectCode varchar(20), MobjectName varchar(20), LabelID varchar(20), LabelName varchar(20), TaskID varchar(100),LineID varchar(20), CheckItemDesc varchar(600), ESTStandard varchar(400), CheckType varchar(20), ZhenDong_Type varchar(20), Zhendong_PP varchar(20), AlarmType_ID varchar(20), StandardValue varchar(20), ParmLowerLimit varchar(20), ParmUpperLimit varchar(20), CheckItemUpdateTime varchar(20), UpperLimit1 float, UpperLimit2 float, UpperLimit3 float, UpperLimit4 float, LowerLimit1 float, LowerLimit2 float, LowerLimit3 float, LowerLimit4 float, CheckOrderNo int, submit boolean," +
                "TaskItemID varchar(100), LabelCode varchar(20), ObserveOptions varchar(20), ShiftName varchar(20), GroupName varchar(20), TaskPlanStartTime varchar(20), TaskPlanEndTime varchar(20), NextTaskDate varchar(20), NextTaskDateTimePeriods varchar(20), ZhenDong_Freq varchar(20), ZhenDong_Points varchar(20), EquipmentStatusFilter varchar(20), SyncSamplingTag varchar(20), state int);");
        //波形数据
        db.execSQL("create table chart(_id integer primary key autoincrement, WaveData float, Result_ID varchar(20));");
        //标牌到位信息
        db.execSQL("create table label(_id integer primary key autoincrement, LabelID varchar(20), LabelCode varchar(100), TaskType varchar(20), LineId varchar(20),Start_TM varchar(20), End_TM varchar(20), User_ID varchar(20), UserName varchar(20), UserCode varchar(20), UserPostId varchar(20), ShiftName varchar(20), GroupName varchar(20), TimeCount integer, TaskID varchar(100));");
        //储存排序结果展示的时候使用
        db.execSQL("create table lineMobject(_id integer primary key autoincrement, CheckItemID varchar(20) , MobjectCode varchar(20), MobjectName varchar(20), LabelID varchar(20), LabelName varchar(20), TaskID varchar(100),LineID varchar(20), CheckItemDesc varchar(600), ESTStandard varchar(400), CheckType varchar(20), ZhenDong_Type varchar(20), Zhendong_PP varchar(20), AlarmType_ID varchar(20), StandardValue varchar(20), ParmLowerLimit varchar(20), ParmUpperLimit varchar(20), CheckItemUpdateTime varchar(20), UpperLimit1 float, UpperLimit2 float, UpperLimit3 float, UpperLimit4 float, LowerLimit1 float, LowerLimit2 float, LowerLimit3 float, LowerLimit4 float, CheckOrderNo int, submit boolean, " +
                "TaskItemID varchar(100), LabelCode varchar(20), ObserveOptions varchar(20), ShiftName varchar(20), GroupName varchar(20), TaskPlanStartTime varchar(20), TaskPlanEndTime varchar(20), NextTaskDate varchar(20), NextTaskDateTimePeriods varchar(20), ZhenDong_Freq varchar(20), ZhenDong_Points varchar(20), EquipmentStatusFilter varchar(20), SyncSamplingTag varchar(20), state int);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
