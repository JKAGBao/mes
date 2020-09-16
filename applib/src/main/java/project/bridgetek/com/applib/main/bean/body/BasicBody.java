package project.bridgetek.com.applib.main.bean.body;

import com.alibaba.fastjson.annotation.JSONField;

public class BasicBody {
    private int MOBJECT_ID;

    public BasicBody(int MOBJECT_ID) {
        this.MOBJECT_ID = MOBJECT_ID;
    }

    @JSONField(name = "MOBJECT_ID")
    public int getMOBJECT_ID() {
        return MOBJECT_ID;
    }

    public void setMOBJECT_ID(int MOBJECT_ID) {
        this.MOBJECT_ID = MOBJECT_ID;
    }
}
