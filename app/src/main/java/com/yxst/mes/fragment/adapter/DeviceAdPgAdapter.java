package com.yxst.mes.fragment.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class DeviceAdPgAdapter extends PagerAdapter {
    ArrayList<ImageView> adImages;

    public DeviceAdPgAdapter(ArrayList<ImageView> images) {
        this.adImages = images;
    }

    @Override
    public int getCount() {
        return adImages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int newPosition = position % adImages.size();
        ImageView view = adImages.get(newPosition);
        //fix ->The specified child already has a parent. You must call removeView() on the child's parent first.
        if(view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view); // <- fix
        }
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((adImages.get(position%adImages.size())));
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


}
