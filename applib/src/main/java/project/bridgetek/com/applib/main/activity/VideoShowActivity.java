package project.bridgetek.com.applib.main.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.flir.flironeexampleapplication.util.StatusBarUtils;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;

public class VideoShowActivity extends Activity {
    private VideoView mVideoView;
    private String mPath;
    boolean isNetWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.theme);
        initUI();
    }

    public void initUI() {
        mVideoView = findViewById(R.id.vv_abnormal);
        mPath = getIntent().getStringExtra(Constants.PATH);
        isNetWork = getIntent().getBooleanExtra(Constants.ISFIRST, false);
        if (isNetWork) {
            HttpProxyCacheServer proxy = HiApplication.getInstance(VideoShowActivity.this);
            String proxyUrl = proxy.getProxyUrl(mPath);
            mVideoView.setVideoPath(proxyUrl);
        } else {
            Uri uri = Uri.parse(mPath);
            mVideoView.setVideoURI(uri);
        }
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        mVideoView.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VideoShowActivity.this, R.string.excdelegate_videoshow_toast_over_text, Toast.LENGTH_SHORT).show();
            VideoShowActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
