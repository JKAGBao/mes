package com.flir.flironeexampleapplication.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cong Zhizhong on 18-7-21.
 */

public class WaterMark {

    /**
     * 路径拿图片
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
//获取资源图片
        return BitmapFactory.decodeFile(imagePath, opt);
    }

    /**
     * 文字中间绘制
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
                                          int size, int color, int a, int b) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(bitmap, text, paint, bounds,
                (bitmap.getWidth() + a) / 2 + 5,
                (bitmap.getHeight() + bounds.height() * 2 + b) / 2 + 5);
    }

    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(bitmap, text, paint, bounds,
                5,
                bitmap.getHeight() - 10);
    }


    //图片上绘制文字
    public static Bitmap drawTextToBitmap(Bitmap bitmap, String text,
                                          Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * dip转pix
     *
     * @param
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 绘制图片
     *
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                                int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        // TODO
       // canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        return newb;
    }

    /**
     * 图片绘制在中间
     *
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark, int a, int b) {
        int width = watermark.getWidth();
        int height = watermark.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) 0.4, (float) 0.4);
        Bitmap newbm = Bitmap.createBitmap(watermark, 0, 0, width, height, matrix, true);
        return createWaterMaskBitmap(src, newbm,
                (src.getWidth() - newbm.getWidth() + a) / 2,
                (src.getHeight() - newbm.getHeight() + b) / 2);
    }

    /**
     * 图片保存到本地
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap bitmap) {
        String fileName;
        File file;
        String bitName = getNowTime() + ".JPEG";
        fileName = context.getExternalCacheDir().getPath();
        if (fileName != null) {
            File dir = new File(fileName + "/recordtest/thermal");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileName = dir + "/" + bitName;
        }
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("qwe", "saveBitmap: " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("qwe", "saveBitmap: " + e);
        }
        return fileName;
    }

    /**
     * 图片保存到相册
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static void savePhoto(Context context, Bitmap bitmap) {
        String fileName;
        File file;
        String bitName = getNowTime() + ".JPEG";
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("qwe", "saveBitmap: " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("qwe", "saveBitmap: " + e);
        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }

    //系统当前时间
    public static long getNowTime() {
        //获取系统时间的10位时间戳
        long time = System.currentTimeMillis() / 1000;
        return time;
    }

    //当前时间字符串
    public static String dateToString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
