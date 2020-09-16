package project.bridgetek.com.applib.main.toos;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private static AudioPlayer mAudioPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener;
    private String filePath;

    private AudioPlayer() {
    }

    public static AudioPlayer instance() {
        if (mAudioPlayer == null) {
            mAudioPlayer = new AudioPlayer();
        }
        return mAudioPlayer;
    }

    public void playAudio(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (TextUtils.isEmpty(filePath)) return;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (!TextUtils.equals(this.filePath, filePath) && this.onCompletionListener != null) {//停止前一个动画
            this.onCompletionListener.onCompletion(mediaPlayer);
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            Log.i("CONG", "playAudio: " + duration);
            mediaPlayer.start();
            this.onCompletionListener = onCompletionListener;
            this.filePath = filePath;
        } catch (IOException ioe) {
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(mediaPlayer);
            }

            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void stopAudio() {
        try {
            if (this.onCompletionListener != null) {
                this.onCompletionListener.onCompletion(mediaPlayer);
            }
            if (mediaPlayer != null)
                mediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = null;
        } catch (Exception e) {
        }
    }

    public void stopAudio(String filePath) {
        if (TextUtils.equals(this.filePath, filePath)) {
            stopAudio();
        }
    }
}
