package com.flir.flironeexampleapplication.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.flir.flironeexampleapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cong Zhizhong on 18-8-23.
 */

public class ThermalAdapter extends RecyclerView.Adapter<RoomViewHolder> {
    Activity mContext;
    List<String> mList;
    boolean mIsWatch = false;
    public List<String> mStringList = new ArrayList<>();

    public ThermalAdapter(Activity context, List<String> list) {
        mContext = context;
        mList = list;
    }

    public void setWatch(boolean isWatch) {
        this.mIsWatch = isWatch;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_thermal, parent, false);
        RoomViewHolder simpleViewHolder = new RoomViewHolder(v);
        simpleViewHolder.setIsRecyclable(true);
        return simpleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder holder, int position) {
        final String s = mList.get(position);
        if (mIsWatch) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        Glide.with(mContext).load(s).into(holder.imgAbnormal);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    mStringList.add(s);
                } else {
                    mStringList.remove(s);
                }
            }
        });
        holder.imgAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsWatch) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                    if (holder.checkBox.isChecked()) {
                        mStringList.add(s);
                    } else {
                        mStringList.remove(s);
                    }
                } else {
                    getView(s);
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

class RoomViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgAbnormal;
    public CheckBox checkBox;

    public RoomViewHolder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);
        checkBox.setChecked(false);
        imgAbnormal = (ImageView) itemView.findViewById(R.id.tv_abnormal);
    }
}
