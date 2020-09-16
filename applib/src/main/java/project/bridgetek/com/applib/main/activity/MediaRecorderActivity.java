package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flir.flironeexampleapplication.util.StatusBarUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.DeleteFileUtil;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.Logger;

public class MediaRecorderActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = "MainActivity";
    private SurfaceView mSurfaceview;
    private Button mBtnStartStop, mBtComplete;
    private Button mBtnPlay;
    private boolean mStartedFlg = false;//是否正在录像
    private boolean mIsPlay = false;//是否正在播放录像
    private boolean mIsfirst = false;
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private MediaPlayer mEdiaPlayer;
    private String mPath = null;
    private TextView mTextview;
    private int mText = 10;
    private android.os.Handler handler = new android.os.Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mText--;
            mTextview.setText(mText + "");
            handler.postDelayed(this, 1000);
            if (mText == 0) {
                if (mStartedFlg) {
                    try {
                        handler.removeCallbacks(runnable);
                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();
                        mRecorder = null;
                        mBtnStartStop.setText(getResources().getString(R.string.upcom_measure_bt_retest_text));
                        if (mCamera != null) {
                            mCamera.release();
                            mCamera = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mStartedFlg = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        mBtnStartStop = (Button) findViewById(R.id.btnStartStop);
        mBtnPlay = (Button) findViewById(R.id.btnPlayVideo);
        mBtComplete = findViewById(R.id.bt_complete);
        mTextview = (TextView) findViewById(R.id.text);
        mBtnStartStop.setTypeface(HiApplication.MEDIUM);
        mBtnPlay.setTypeface(HiApplication.MEDIUM);
        mBtComplete.setTypeface(HiApplication.MEDIUM);
        mTextview.setTypeface(HiApplication.BOLD);
        mIsfirst = getIntent().getBooleanExtra(Constants.ISFIRST, false);
        if (mIsfirst) {
            mPath = getIntent().getStringExtra(Constants.PATH);
            playback();
        }
        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record();
            }
        });
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playback();
            }
        });
        mBtnPlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mIsfirst) {
                    Toast.makeText(MediaRecorderActivity.this, R.string.upcom_mediarecorder_activity_toast_text, Toast.LENGTH_SHORT).show();
                } else {
                    DeleteFileUtil.deleteFile(mPath, MediaRecorderActivity.this);
                    mPath = null;
                }
                return true;
            }
        });
        mBtComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mPath) || mPath.equals("")) {
                    Toast.makeText(MediaRecorderActivity.this, R.string.upcom_mediarecorder_toast_record_text, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (mStartedFlg) {
                    Toast.makeText(MediaRecorderActivity.this, R.string.upcom_measure_toast_stop_text, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("mPath", mPath);
                    intent.putExtra(Constants.PATH, mPath);
                    setResult(2, intent);//返回值调用函数，其中2为resultCode，返回值的标志
                    finish();
                }
            }
        });
        mSurfaceHolder = mSurfaceview.getHolder();
        //mSurfaceHolder.addCallback(this);
        mSurfaceHolder.addCallback(mCallBack);
        // setType必须设置，要不出错.
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * 录制视频
     */
    public void Record() {
        if (mIsPlay) {
            if (mEdiaPlayer != null) {
                mIsPlay = false;
                mEdiaPlayer.stop();
                mEdiaPlayer.reset();
                mEdiaPlayer.release();
                mEdiaPlayer = null;
            }
        }
        if (!mStartedFlg) {
            mText = 10;
            handler.postDelayed(runnable, 1000);
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
            }
            if (mCamera == null) {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            if (mCamera != null) {
                mCamera.setDisplayOrientation(90);
                mCamera.unlock();
                mRecorder.setCamera(mCamera);
            }
            try {
                // 这两项需要放在setOutputFormat之前
                mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // Set output file format
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                // 这两项需要放在setOutputFormat之后
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                mRecorder.setVideoSize(640, 480);
                mRecorder.setVideoFrameRate(30);
                mRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
                mRecorder.setOrientationHint(90);
                //设置记录会话的最大持续时间（毫秒）
                mRecorder.setMaxDuration(10 * 1000);
                mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                mPath = getExternalCacheDir().getPath();
                if (mPath != null) {
                    File dir = new File(mPath + "/recordtest");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    mPath = dir + "/" + getDate() + ".mp4";
                    mRecorder.setOutputFile(mPath);
                    mRecorder.prepare();
                    mRecorder.start();
                    mStartedFlg = true;
                    Logger.i(mPath);
                    mBtnStartStop.setText(getResources().getString(R.string.test_vibration_activity_start_text));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //stop
            if (mStartedFlg) {
                try {
                    handler.removeCallbacks(runnable);
                    mRecorder.stop();
                    mRecorder.reset();
                    mRecorder.release();
                    mRecorder = null;
                    mBtnStartStop.setText(getResources().getString(R.string.upcom_measure_bt_retest_text));
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mStartedFlg = false;
        }
    }

    /**
     * 回放录制视频
     */
    public void playback() {
        if (mPath == null || mPath.equals("")) {
            Toast.makeText(MediaRecorderActivity.this, R.string.upcom_mediarecorder_activity_toast_playback_text, Toast.LENGTH_SHORT).show();
        } else {
            mIsPlay = true;
            if (mEdiaPlayer == null) {
                mEdiaPlayer = new MediaPlayer();
            }
            mEdiaPlayer.reset();
            Uri uri = Uri.parse(mPath);
            mEdiaPlayer = MediaPlayer.create(MediaRecorderActivity.this, uri);
            mEdiaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mEdiaPlayer.setDisplay(mSurfaceHolder);
            try {
                mEdiaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mEdiaPlayer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        Logger.d(date);
        return date;
    }

    /**
     * 获取SD mPath
     *
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }

        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceview = null;
        mSurfaceHolder = null;
        handler.removeCallbacks(runnable);
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
            Logger.d("surfaceDestroyed release mRecorder");
        }
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        if (mEdiaPlayer != null) {
            mEdiaPlayer.release();
            mEdiaPlayer = null;
        }
    }

    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (mSurfaceHolder.getSurface() == null) {
                return;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            stopCamera();
        }
    };

    private void initCamera() {
        if (mCamera != null) {
            stopCamera();
        }
        //默认启动后置摄像头
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (mCamera == null) {
            Toast.makeText(this, R.string.upcom_mediarecorder_activity_toast_camera_text, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            setCameraParams();
            //启动相机预览
            mCamera.startPreview();
        } catch (IOException e) {
            Logger.e("Error starting mCamera preview: " + e.getMessage());
        }

    }

    private void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            //设置相机的很速屏幕
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                params.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
            } else {
                params.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
            }
            //设置聚焦模式
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            //缩短Recording启动时间
            params.setRecordingHint(true);
            //是否支持影像稳定能力，支持则开启
            if (params.isVideoStabilizationSupported())
                params.setVideoStabilization(true);
            mCamera.setParameters(params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCamera();
    }
}
