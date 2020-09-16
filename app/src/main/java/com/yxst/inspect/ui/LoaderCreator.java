package com.yxst.inspect.ui;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

public final class LoaderCreator {
    private static final WeakHashMap<String,Indicator> LOAD_MAP = new WeakHashMap<>();
    static AVLoadingIndicatorView create(String type, Context context){
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if(LOAD_MAP.get(type)==null){
                Indicator indicator = getIndicator(type);
                LOAD_MAP.put(type,indicator);
        }
        avLoadingIndicatorView.setIndicator(LOAD_MAP.get(type));
        return avLoadingIndicatorView;
    }
    private static Indicator getIndicator(String name){
        if(name==null || name.isEmpty()){
            return null;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        if(!name.contains(".")){
            final String packageName = AVLoadingIndicatorView.class.getPackage().getName();
            stringBuilder.append(packageName)
            .append(".indicators")
            .append(".");
        }
        stringBuilder.append(name);
        try {
            final Class<?> drawbleView = Class.forName(stringBuilder.toString());
            return (Indicator) drawbleView.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
