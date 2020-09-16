package project.bridgetek.com.applib.main.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.AbnormalActivity;
import project.bridgetek.com.applib.main.activity.VideoShowActivity;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.AudioPlayer;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;

/**
 * Created by Cong Zhizhong on 18-6-19.
 */

public class AbnormalAdapter extends RecyclerView.Adapter<RoomViewHolder> {
    private AbnormalActivity mCtx;
    private LayoutInflater mInflater;
    List<ResultFileInfo> mDailyRoomList;
    ImageView mImageView;
    public static boolean ISHIDE = false;
    int mPress = 0;
    TextView mTvCancel;
    public static List<ResultFileInfo> LIST = new ArrayList<>();
    AudioPlayer audioPlayer = AudioPlayer.instance();

    public AbnormalAdapter(AbnormalActivity mCtx, List<ResultFileInfo> list, ImageView mImageView, TextView tvCancel) {
        super();
        this.mCtx = mCtx;
        this.mImageView = mImageView;
        this.mTvCancel = tvCancel;
        mInflater = LayoutInflater.from(mCtx);
        mDailyRoomList = list;
    }

    @Override
    public int getItemCount() {
        return mDailyRoomList.size();
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup viewgroup, int i) {
        View v = mInflater.inflate(R.layout.item_abnormal, viewgroup, false);
        RoomViewHolder simpleViewHolder = new RoomViewHolder(v);
        simpleViewHolder.setIsRecyclable(true);
        return simpleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder holder, final int position) {
        final ResultFileInfo s = mDailyRoomList.get(position);
        if (ISHIDE) {
            if (s.getFileType().equals(Constants.PHOTO)) {
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.checkBox.setChecked(false);
                holder.checkBox.setVisibility(View.VISIBLE);
            }
            mPress = 1;
        } else {
            holder.checkBox.setVisibility(View.GONE);
            mPress = 0;
        }
        if (s.getFileType() == Constants.PHOTO || s.getFileType().equals(Constants.PHOTO)) {
            holder.imgADD.setBackground(mCtx.getResources().getDrawable(R.drawable.ic_add));
            holder.imgAbnormal.setImageDrawable(null);
            holder.imgAbnormal.setBackgroundColor(mCtx.getResources().getColor(R.color.abnormal_img));
        } else if (s.getFileType() == CountString.CORD || s.getFileType().equals(CountString.CORD)) {
            holder.imgADD.setVisibility(View.VISIBLE);
            holder.imgADD.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_voice));
            holder.imgAbnormal.setImageDrawable(null);
            holder.imgAbnormal.setBackgroundColor(mCtx.getResources().getColor(R.color.theme));
        } else if (s.getFileType().equals(CountString.CORDER)) {
            holder.imgADD.setVisibility(View.VISIBLE);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(s.getFileName());
            Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧图片
            holder.imgAbnormal.setImageBitmap(bitmap);
            mmr.release();
            holder.imgADD.setBackground(mCtx.getResources().getDrawable(R.drawable.btn_play_video));
        } else {
            holder.imgADD.setVisibility(View.GONE);
            File file = new File(s.getFileName());
            Glide.with(mCtx).load(file).into(holder.imgAbnormal);
        }
        holder.imgAbnormal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ISHIDE = true;
                mPress = 1;
                mImageView.setVisibility(View.VISIBLE);
                mTvCancel.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                return true;
            }
        });
        holder.imgAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPress == 0) {
                    if (s.getFileType() == Constants.PHOTO || s.getFileType().equals(Constants.PHOTO)) {
                        mCtx.showDialog();
                        ISHIDE = false;
                        mImageView.setVisibility(View.GONE);
                        mTvCancel.setVisibility(View.GONE);
                        LIST.clear();
                        notifyDataSetChanged();
                    } else if (s.getFileType() == CountString.CORD || s.getFileType().equals(CountString.CORD)) {
                        final Dialog builder;
                        View view = LayoutInflater.from(mCtx).inflate(R.layout.dialog_toast_cord, null, false);
                        builder = new Dialog(mCtx, R.style.update_dialog);
                        builder.setCancelable(true);
                        builder.setCanceledOnTouchOutside(true);
                        ImageView img_photo = view.findViewById(R.id.img_photo_gif);
                        RelativeLayout alertClose = view.findViewById(R.id.alert_close);
                        alertClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                                audioPlayer.release();
                            }
                        });
                        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (event.getAction() == KeyEvent.ACTION_UP
                                        && keyCode == KeyEvent.KEYCODE_BACK
                                        && event.getRepeatCount() == 0) {
                                    dialog.dismiss();
                                    // 取消查询，将回调取消
                                    audioPlayer.release();
                                }
                                return false;
                            }
                        });
                        builder.show();
                        Display display = mCtx.getWindowManager().getDefaultDisplay();
                        int width = display.getWidth();
                        int height = display.getHeight();
                        //设置dialog的宽高为屏幕的宽高
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
                        builder.setContentView(view, layoutParams);
                        audioPlayer.playAudio(s.getFileName(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                builder.dismiss();
                            }
                        });
                    } else if (s.getFileType() == CountString.CORDER || s.getFileType().equals(CountString.CORDER)) {
                        Intent intent = new Intent(mCtx, VideoShowActivity.class);
                        intent.putExtra(Constants.PATH, s.getFileName());
                        mCtx.startActivity(intent);
                    } else {
                        getView(s.getFileName());
                    }
                } else if (mPress == 1) {
                    if (s.getFileType() == Constants.PHOTO || s.getFileType().equals(Constants.PHOTO)) {
                        mCtx.showDialog();
                        ISHIDE = false;
                        mImageView.setVisibility(View.GONE);
                        mTvCancel.setVisibility(View.GONE);
                        LIST.clear();
                        notifyDataSetChanged();
                    } else {
                        holder.checkBox.setChecked(!holder.checkBox.isChecked());
                        if (holder.checkBox.isChecked()) {
                            LIST.add(s);
                        } else {
                            LIST.remove(s);
                        }
                    }
                }
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    LIST.add(s);
                } else {
                    LIST.remove(s);
                }
            }
        });
        // Glide.with(mCtx).load(s).into(holder);
    }

    public void getView(String s) {
        final Dialog dia;
        dia = new Dialog(mCtx, R.style.edit_AlertDialog_style);
        View view = LayoutInflater.from(mCtx).inflate(R.layout.start_dialog, null, false);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.star_image);
        imageView.enable();
        File file = new File(s);
        Glide.with(mCtx).load(file).into(imageView);
        dia.setCanceledOnTouchOutside(true);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });
        dia.show();
        Display display = mCtx.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dia.setContentView(view, layoutParams);
    }
}

class RoomViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgAbnormal;
    public ImageView imgADD;
    public CheckBox checkBox;

    public RoomViewHolder(View layout) {
        super(layout);
        checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);
        checkBox.setChecked(false);
        imgAbnormal = (ImageView) layout.findViewById(R.id.tv_abnormal);
        imgADD = layout.findViewById(R.id.iv_add);
    }
}
