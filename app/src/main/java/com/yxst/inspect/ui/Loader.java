package com.yxst.inspect.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;

import com.wang.avi.AVLoadingIndicatorView;
import com.yxst.inspect.R;

import java.util.ArrayList;

public class Loader {
    private static final int LOAD_SIZE_SCALE = 6;
    private static final int LOAD_SIZE_OFFSET = 10;
    private static ArrayList<AppCompatDialog> dialogs = new ArrayList<AppCompatDialog>();
    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

   public static void showLoading(Context context,Enum<LoaderStyle> type){
       showLoading(context,type.name());
   }
    public static void showLoading(Context context,String type){
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog );
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type,context);
        dialog.setContentView(avLoadingIndicatorView);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int devicewidth = metrics.widthPixels;
        int deviceheight = metrics.heightPixels;
        final Window dialogwindow = dialog.getWindow();
        if(dialogwindow!=null){
            WindowManager.LayoutParams params = dialogwindow.getAttributes();
            params.width = devicewidth/LOAD_SIZE_SCALE;
            params.height = deviceheight/LOAD_SIZE_SCALE+deviceheight/LOAD_SIZE_OFFSET;
            params.gravity = Gravity.CENTER;
            Log.e("value",deviceheight+","+devicewidth+","+params.width+","+params.height);
        }
        dialogs.add(dialog);
        dialog.show();
    }
    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }
    public static void stopLoading(){
        for(AppCompatDialog dialog:dialogs){
            if(dialog!=null){
                if(dialog.isShowing()){
                    dialog.cancel();
                }
            }

        }
        }

}
