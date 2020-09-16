package project.bridgetek.com.applib.main.adapter;

import android.app.Activity;
import android.app.Dialog;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.ResultFileInfo;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.PopueWindow;

/**
 * Created by bridge on 18-6-25.
 */

public class MeterAdapter extends RecyclerView.Adapter<RoomViewHolder> {

    private Activity mCtx;
    private LayoutInflater mInflater;
    List<ResultFileInfo> mDailyRoomList;
    ImageView mImageView;
    int mPress = 0;
    public static boolean ISHIDE = false;
    public static List<ResultFileInfo> LIST = new ArrayList<>();
    TextView mTvCancel;

    public MeterAdapter(Activity mCtx, List<ResultFileInfo> list, ImageView mImageView, TextView tvCancel) {
        super();
        this.mCtx = mCtx;
        this.mImageView = mImageView;
        mInflater = LayoutInflater.from(mCtx);
        mDailyRoomList = list;
        mTvCancel = tvCancel;
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
        } else {
            File file = new File(s.getFileName());
            Glide.with(mCtx).load(file).into(holder.imgAbnormal);
            holder.imgADD.setVisibility(View.GONE);
        }
        holder.imgAbnormal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ISHIDE = true;
                mImageView.setVisibility(View.VISIBLE);
                mTvCancel.setVisibility(View.VISIBLE);
                mPress = 1;
                notifyDataSetChanged();
                return true;
            }
        });
        holder.imgAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPress == 0) {
                    if (s.getFileType() == Constants.PHOTO || s.getFileType().equals(Constants.PHOTO)) {
                        PopueWindow popueWindow = new PopueWindow(mCtx);
                        popueWindow.showPopueWindow();
                        ISHIDE = false;
                        mImageView.setVisibility(View.GONE);
                        mTvCancel.setVisibility(View.GONE);
                        LIST.clear();
                        notifyDataSetChanged();
                    } else {
                        getView(s.getFileName());
                    }
                } else if (mPress == 1) {
                    if (s.getFileType() == Constants.PHOTO || s.getFileType().equals(Constants.PHOTO)) {
                        PopueWindow popueWindow = new PopueWindow(mCtx);
                        popueWindow.showPopueWindow();
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
