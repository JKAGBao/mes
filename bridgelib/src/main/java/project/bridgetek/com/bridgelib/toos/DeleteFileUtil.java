package project.bridgetek.com.bridgelib.toos;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import project.bridgetek.com.bridgelib.R;

/**
 * Created by Cong Zhihzong on 18-6-14.
 */

public class DeleteFileUtil {
    public static boolean deleteFile(String fileName, Context context) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Toast.makeText(context, context.getString(R.string.bridgelib_toos_delete_success_text) + fileName + context.getString(R.string.bridgelib_toos_delete_yes_text), Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, context.getString(R.string.bridgelib_toos_delete_success_text) + fileName + context.getString(R.string.bridgelib_toos_delete_no_text), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(context, context.getString(R.string.bridgelib_toos_delete_success_text) + context.getString(R.string.bridgelib_toos_delete_no_text) + fileName + context.getString(R.string.bridgelib_toos_delete_nonentity_text), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean delFile(String fileName, Context context) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
