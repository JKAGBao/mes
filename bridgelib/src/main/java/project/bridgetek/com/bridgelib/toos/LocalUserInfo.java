package project.bridgetek.com.bridgelib.toos;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridge on 18-6-13.
 */

public class LocalUserInfo {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "local_userinfo";
    private static SharedPreferences mSharedPreferences;
    private static LocalUserInfo mPreferenceUtils;
    private static SharedPreferences.Editor editor;

    private LocalUserInfo(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param cxt
     * @return
     */
    public static LocalUserInfo getInstance(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new LocalUserInfo(cxt);
        }
        editor = mSharedPreferences.edit();
        return mPreferenceUtils;
    }

    public void removeUserInfo()
    {
        editor.clear().commit();
    }

    public void setUserInfo(String str_name, String str_value) {
        editor.putString(str_name, str_value);
        editor.commit();
    }

    public String getUserInfo(String str_name) {
        return mSharedPreferences.getString(str_name, "");
    }

    public void setBoolean(String str_name, boolean str_value){
        editor.putBoolean(str_name, str_value);
        editor.commit();
    }

    public boolean getBoolean(String str_name){
        return mSharedPreferences.getBoolean(str_name, true);
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public void setDataList(String tag, List<String> datalist) {
        if (null == datalist)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public List<String> getDataList(String tag) {
        List<String> datalist=new ArrayList<String>();
        String strJson = mSharedPreferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<String>>() {
        }.getType());
        return datalist;
    }
}
