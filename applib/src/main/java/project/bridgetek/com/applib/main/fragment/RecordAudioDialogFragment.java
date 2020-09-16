package project.bridgetek.com.applib.main.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.melnykov.fab.FloatingActionButton;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.bridgelib.toos.Logger;
import project.bridgetek.com.bridgelib.toos.Record;

/**
 * Created by Cong Zhizhong on 18-6-23.
 */

public class RecordAudioDialogFragment extends DialogFragment {
    private int mRecordPromptCount = 0;
    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;
    long mTimewhenpaused = 0;
    private FloatingActionButton mFabRecord;
    private Chronometer mChronometerTime;
    private Button mIvClose;
    private OnAudioCancelListener mListener;
    private RelativeLayout mRlTouming;
    private Record mRecord;
    public static String PATH;
    boolean mIsStop = false;
    boolean mIsStart = false;

    public static RecordAudioDialogFragment newInstance() {
        RecordAudioDialogFragment dialogFragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_record_audio, null);
        initView(view);
        mFabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
            }
        });
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    private void initView(View view) {
        mRlTouming = view.findViewById(R.id.rl_touming);
        mRlTouming.setBackgroundColor(00000000);
        mChronometerTime = (Chronometer) view.findViewById(R.id.record_audio_chronometer_time);
        mFabRecord = view.findViewById(R.id.record_audio_fab_record);
        mIvClose = (Button) view.findViewById(R.id.record_audio_iv_close);
        mChronometerTime.setOnChronometerTickListener(new OnChronometerTickListenerImpl());
    }

    private void onRecord(boolean start) {
        if (start) {
            mFabRecord.setImageResource(R.drawable.ic_media_stop);
            Toast.makeText(getActivity(), R.string.upcom_abnormal_record_toast_text, Toast.LENGTH_SHORT).show();
            mRecord = Record.getInstance(getActivity());
            mRecord.StartRecord();
            mChronometerTime.setBase(SystemClock.elapsedRealtime());
            mChronometerTime.start();
            mIsStop = false;
            mIsStart = true;
        } else {
            mFabRecord.setImageResource(R.drawable.ic_mic_white_36dp);
            PATH = mRecord.StopRecord();
            mChronometerTime.stop();
            mIsStop = true;
        }
    }

    public void stop() {
        Logger.i("stop: ");
        if (mIsStart) {
            if (!mIsStop) {
                PATH = mRecord.StopRecord();
                mChronometerTime.stop();
            }
        }
    }

    public void setOnCancelListener(OnAudioCancelListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    onRecord(mStartRecording);
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsStart) {
            mRecord.StopRecord();
            PATH = null;
        }

    }

    public interface OnAudioCancelListener {
        void onCancel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public class OnChronometerTickListenerImpl implements // 计时监听事件，随时随地的监听时间的变化
            Chronometer.OnChronometerTickListener {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            String time = chronometer.getText().toString().trim();
            Logger.i("onChronometerTick: " + time);
            if ("00: 30".equals(time) || "00:30".equals(time)) {// 判断五秒之后，让手机震动
                mFabRecord.setImageResource(R.drawable.ic_mic_white_36dp);
                PATH = mRecord.StopRecord();
                mChronometerTime.stop();
                mIsStop = true;
            }
        }
    }
}