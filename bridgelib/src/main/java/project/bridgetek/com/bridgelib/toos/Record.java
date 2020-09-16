package project.bridgetek.com.bridgelib.toos;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cong Zhizhong on 18-6-14.
 */

public class Record {
    private static MediaRecorder recorder;
    private Context mcontext;
    private static Record mrecord = null;
    private String mpath;

    private Record(Context context) {
        recorder = new MediaRecorder();
        this.mcontext = context.getApplicationContext();
        initIa();
    }

    public static Record getInstance(Context context) {
        if (recorder == null) {
            mrecord = new Record(context);
        }
        return mrecord;
    }

    public void initIa() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置录音的输入源(麦克)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//设置音频格式(amr)
        String file = createRecorderFile();//创建保存录音的文件夹
        recorder.setOutputFile(file); //设置录音保存的文件
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频编码
        recorder.setMaxDuration(30 * 1000);
        try {
            recorder.prepare();//准备录音
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String createRecorderFile() {
        mpath = mcontext.getExternalCacheDir().getPath();
        Log.i("CONG", "onClick: " + mpath);
        if (mpath != null) {
            File dir = new File(mpath + "/recordtest");
            Log.i("CONG", "onClick: " + dir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            mpath = dir + "/" + getCurrentTime() + ".AAC";
        }
        return mpath;
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    public void StartRecord() {
        if (recorder != null) {
            recorder.start(); //开始录音
        }
    }

    public String StopRecord() {
        if (recorder != null) {
            try {
                recorder.stop(); //停止录音
                recorder.release();//释放资源
                recorder = null;
                return mpath;
            } catch (Exception e) {
                Log.i("CONG", "StopRecord: ");
                return null;
            }
        }
        return null;
    }
}
