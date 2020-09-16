package project.bridgetek.com.applib.main.toos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import project.bridgetek.com.applib.R;

/**
 * Created by Cong Zhizhong on 18-9-3.
 */

public class UpdateManager {
    // 应用程序Context
    private Context mContext;
    // 是否是最新的应用,默认为false
    private boolean isNew = false;
    private boolean intercept = false;
    // 下载安装包的网络路径
    private String apkUrl = "";
    // 保存APK的文件夹
    private static final String savePath = "/sdcard/updatedemo/";
    private static final String saveFileName = savePath
            + "jzd.apk";
    // 下载线程
    private Thread downLoadThread;
    private int progress;// 当前进度
    TextView text;
    // 进度条与通知UI刷新的handler和msg常量
    private ProgressBar mProgress;
    private TextView mTvSpeed, mTvProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    AlertDialog.Builder builder;
    int length;
    int count;
    AlertDialog dialog;

    public UpdateManager(Context context, String apkUrl, boolean isNew) {
        mContext = context;
        this.apkUrl = apkUrl;
        this.isNew = isNew;
    }

    /**
     * 检查是否更新的内容
     */
    public void checkUpdateInfo() {
        //这里的isNew本来是要从服务器获取的，我在这里先假设他需要更新
        if (isNew) {
            return;
        } else {
            showUpdateDialog();
        }
    }

    /**
     * 显示更新程序对话框，供主程序调用
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(R.string.app_updatemanager_builder_title_text);
        builder.setMessage(R.string.app_updatemanager_builder_message_text);
        builder.setPositiveButton(R.string.app_updatemanager_builder_positive_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDownloadDialog();
            }
        });
        builder.setNegativeButton(R.string.app_updatemanager_builder_negative_text,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 显示下载进度的对话框
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(R.string.app_updatemanager_builder_title_text);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progressbar, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        mTvSpeed = v.findViewById(R.id.tv_speed);
        mTvProgress = v.findViewById(R.id.tv_progress);
        builder.setView(v);
        builder.setNegativeButton(R.string.upcom_abnormal_tv_cancel_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                intercept = true;
            }
        });
        dialog = builder.create();
        dialog.show();
        downloadApk();
    }

    /**
     * 从服务器下载APK安装包
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    private Runnable mdownApkRunnable = new Runnable() {

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                length = conn.getContentLength();
                InputStream ins = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(saveFileName);
                FileOutputStream fos = new FileOutputStream(apkFile);
                count = 0;
                byte[] buf = new byte[1024];
                while (!intercept) {
                    int numread = ins.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);

                    // 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                fos.close();
                ins.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 安装APK内容
     */
    private void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (null != apkFile) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = getUriForFile(mContext, apkFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }

    public Uri getUriForFile(Context mContext, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(mContext, "project.bridgetek.com.jzd", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    mTvSpeed.setText(progress + "%");
                    mTvProgress.setText(String.format("%.2f", (double) count / 1048576) + "M" + "/" + String.format("%.2f", (double) length / 1048576) + "M");
                    break;
                case DOWN_OVER:
                    dialog.dismiss();
                    installAPK();
                    break;
                default:
                    break;
            }
        }
    };
}
