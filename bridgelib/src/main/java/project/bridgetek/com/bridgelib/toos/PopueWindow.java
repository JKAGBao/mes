package project.bridgetek.com.bridgelib.toos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import project.bridgetek.com.bridgelib.R;

/**
 * Created by Cong Zhizhong on 18-6-11.
 */

public class PopueWindow {
    Activity mactivity;

    public PopueWindow(Activity activity) {
        this.mactivity = activity;
    }

    public static Uri PHOTOURL;

    public void showPopueWindow() {
        View popView = View.inflate(mactivity, R.layout.popupwindow_camera_need, null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = mactivity.getResources().getDisplayMetrics().widthPixels;
        int height = mactivity.getResources().getDisplayMetrics().heightPixels * 1 / 3;
        final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
        //popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);
        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mactivity.startActivityForResult(i, 10);
                popupWindow.dismiss();
            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //设置日期的转换格式
                //设置文件名
//                String filename = getDate() + ".jpg";
//                String patt = mactivity.getExternalCacheDir().getPath();
//                File dir = new File(patt + "/recordtest");
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
                File f = new File(createRecorderFile());
                PHOTOURL = Uri.fromFile(f);
                //使用内容提供者，定义照片保存的Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, PHOTOURL);
                mactivity.startActivityForResult(intent, 0);
                popupWindow.dismiss();
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mactivity.getWindow().getAttributes();
                lp.alpha = 1.0f;
                mactivity.getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = mactivity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        mactivity.getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒
        String date = "" + year + (month + 1) + day + hour + minute + second;
        return date;
    }

    public String createRecorderFile() {
       String mpath = mactivity.getExternalCacheDir().getPath();
        Log.i("CONG", "onClick: " + mpath);
        if (mpath != null) {
            File dir = new File(mpath + "/recordtest");
            Log.i("CONG", "onClick: " + dir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            mpath = dir + "/" + getDate() + ".jpg";
        }
        return mpath;
    }
}
