package com.yxst.inspect.fragment;

public enum ItemName {
    //{"待检设备","开始巡检","设备信息","待润滑","开始润滑","实时监控","排班安排","绑定设备","报修清单"};
    WAIT_LIST("待检设备",0),
    INSPECT("开始巡检",1),
    DEVICE_INFO("设备信息",2),
    WAIT_LUBE("待润滑",3),
    LUBE("开始润滑",4),
    MONITOR("实时监控",5),
    ARRANGE("排班安排",6),
    BIND_DEVICE("绑定设备",7),
    REPAIR_LIST("报修清单",8);

    private
    String name;
    private int id;
    ItemName(String name,int id){
        this.name = name;
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
}
