package project.bridgetek.com.applib.main.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.VideoShowActivity;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.applib.main.toos.AudioPlayer;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;

/**
 * Created by bridge on 18-8-6.
 */

public class ExceptionDetailAdapter extends RecyclerView.Adapter<RoomViewHolder> {
    private Activity mContext;
    private List<ResultFileInfo> mList;
    AudioPlayer audioPlayer = AudioPlayer.instance();

    public ExceptionDetailAdapter(Activity mContext, List<ResultFileInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_abnormal, parent, false);
        RoomViewHolder simpleViewHolder = new RoomViewHolder(v);
        simpleViewHolder.setIsRecyclable(true);
        return simpleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        final ResultFileInfo s = mList.get(position);
        if (s.getFileType() == CountString.CORD || s.getFileType().equals(CountString.CORD)) {
            holder.imgADD.setVisibility(View.VISIBLE);
            holder.imgADD.setBackground(mContext.getResources().getDrawable(R.drawable.btn_voice));
            holder.imgAbnormal.setBackgroundColor(mContext.getResources().getColor(R.color.theme));
        } else if (s.getFileType().equals(CountString.CORDER)) {
            holder.imgADD.setVisibility(View.VISIBLE);
            holder.imgADD.setBackground(mContext.getResources().getDrawable(R.drawable.btn_play_video));
//            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            //后面这个是传请求Headers，如果有需要可以添加
//            mmr.setDataSource(s.getFileName(), new HashMap());
//            Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧图片
//            holder.imgAbnormal.setImageBitmap(bitmap);
//            mmr.release();
            holder.imgAbnormal.setBackgroundColor(mContext.getResources().getColor(R.color.theme));
        } else {
            Glide.with(mContext).load(s.getFileName()).into(holder.imgAbnormal);
        }

        holder.imgAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.getFileType() == CountString.CORD || s.getFileType().equals(CountString.CORD)) {
                    final Dialog builder;
                    View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_toast_cord, null, false);
                    builder = new Dialog(mContext, R.style.update_dialog);
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
                    Display display = mContext.getWindowManager().getDefaultDisplay();
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
                    Intent intent = new Intent(mContext, VideoShowActivity.class);
                    intent.putExtra(Constants.PATH, s.getFileName());
                    intent.putExtra(Constants.ISFIRST, true);
                    mContext.startActivity(intent);
                } else {
                    getView(s.getFileName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void getView(String s) {
        final Dialog dia;
        dia = new Dialog(mContext, R.style.edit_AlertDialog_style);
        View view = LayoutInflater.from(mContext).inflate(R.layout.start_dialog, null, false);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.star_image);
        imageView.enable();
        Glide.with(mContext).load(s).into(imageView);
        dia.setCanceledOnTouchOutside(true);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });
        dia.show();
        Display display = mContext.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dia.setContentView(view, layoutParams);
    }
}
