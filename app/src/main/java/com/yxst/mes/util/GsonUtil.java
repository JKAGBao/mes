package com.yxst.mes.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created By YuanCheng on 2019/6/26 17:31
 */
public class GsonUtil {

    public static JsonObject parseStringToJson(String s){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        JsonObject json = gson.fromJson(s ,JsonObject.class);
        return json;
    }
    public static Gson getGson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }
}
