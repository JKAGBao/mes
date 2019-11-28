package com.yxst.mes.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yxst.mes.R;
import com.yxst.mes.net.RestConfig;

import org.jetbrains.annotations.NotNull;

import constacne.UiType;
import listener.Md5CheckResultListener;
import listener.OnInitUiListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * Created By YuanCheng on 2019/7/29 15:16
 */
public class VersionUtil {

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
    /*
    版本名称
     */
    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }
    //   private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";



    private static String updateContent =
            "1、下拉刷新bug修改\n2、巡检项自动上传完善\n" +
                    "3、支持手动上传\n4、停机英文提示改为中文";
    public static void versionUpdate(final int code,String versionName,String apkUrl,final Context ctx){
        String updateTitle = "发现新版本"+versionName;
        // 启动应用后删除安装包
        UpdateAppUtils.getInstance().deleteInstalledApk();
        //测试版本更新
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.setCheckWifi(true);
        updateConfig.setShowNotification(true);
        updateConfig.setNotifyImgRes(R.drawable.ic_update_logo);
        updateConfig.setAlwaysShow(true);
     //   updateConfig.setForce(true);
        updateConfig.setThisTimeShow(true);
        updateConfig.setNeedCheckMd5(true);
        updateConfig.setApkSavePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mes");
        UiConfig uiConfig = new UiConfig();
        uiConfig.setUiType(UiType.PLENTIFUL);

        UiConfig uiConfig1 = new UiConfig();
        uiConfig1.setUiType(UiType.CUSTOM);
        uiConfig1.setCustomLayoutId(R.layout.view_update_dialog_custom);
        //自定义更新
        UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .uiConfig(uiConfig)
                .updateConfig(updateConfig)
//                .setMd5CheckResultListener(new Md5CheckResultListener() {
//                        @Override
//                        public void onResult(boolean result) {
//                            Toast.makeText(ctx, "result="+result, Toast.LENGTH_SHORT).show();
//                            Log.e("version","md5:"+result);
//                        }
//                    })
//                .setOnInitUiListener(new OnInitUiListener() {
//                                         @Override
//                                         public void onInitUpdateUi(@org.jetbrains.annotations.Nullable View view, @org.jetbrains.annotations.NotNull UpdateConfig updateConfig, @org.jetbrains.annotations.NotNull UiConfig uiConfig) {
//                                            TextView title =  view.findViewById(R.id.tv_update_title);
//                                                     title.setText("版本更新啦");
//
//
//                                            TextView version = view.findViewById(R.id.tv_version_name);
//                                                     version.setText("V2.0.0");
//                                         }
//                                     })
                .update();

    }
}
