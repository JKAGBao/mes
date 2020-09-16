package com.flir.flironeexampleapplication.util;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.flir.flironeexampleapplication.GLPreviewActivity;
import com.flir.flironeexampleapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bridge on 18-8-24.
 */

public class ThermalFragment extends android.app.Fragment {
    private ImageView mIcBack;
    private RecyclerView mRlList;
    private ThermalAdapter mThermalAdapter;
    private TextView mTvSort;
    private boolean mIsWatch = true;
    private String mText, mFileName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_thermal_album, container, false);
        initUI(view);
        setOnclick();
        return view;
    }

    public void initUI(View view) {
        mIcBack = view.findViewById(R.id.ic_back);
        mRlList = view.findViewById(R.id.rl_list);
        mTvSort = view.findViewById(R.id.tv_sort);
        List<String> sd = getImagePathFromSD();
        mThermalAdapter = new ThermalAdapter(getActivity(), sd);
        mRlList.setAdapter(mThermalAdapter);
        mRlList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
    }

    public void setOnclick() {
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GLPreviewActivity activity = (GLPreviewActivity) getActivity();
                activity.setView();
            }
        });
        mTvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThermalAdapter.setWatch(mIsWatch);
                mThermalAdapter.notifyDataSetChanged();
                if (mIsWatch) {
                    mTvSort.setText(R.string.test_temporary_bt_preservation_text);
                } else {
                    mTvSort.setText(R.string.test_testhistory_tv_sort_text);
                    for (int i = 0; i < mThermalAdapter.mStringList.size(); i++) {
                        delFile(mThermalAdapter.mStringList.get(i));
                    }
                    List<String> sd = getImagePathFromSD();
                    mThermalAdapter = new ThermalAdapter(getActivity(), sd);
                    mRlList.setAdapter(mThermalAdapter);
                    mRlList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
                }
                mIsWatch = !mIsWatch;
            }
        });
    }

    private List<String> getImagePathFromSD() {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        List<File> fileList = new ArrayList<File>();
        String fileName = getActivity().getExternalCacheDir().getPath();
        if (fileName != null) {
            File dir = new File(fileName + "/recordtest/thermal");
            if (!dir.exists()) {
                dir.mkdir();
            }
            fileName = dir + "/";
        }
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(fileName);
        if (fileAll.exists() && fileAll.isDirectory()) {
            File[] files = fileAll.listFiles();
            for (int i = 0; i < files.length; i++) {
                fileList.add(files[i]);
            }
            Collections.sort(fileList, new FileComparator());
        }
        for (int i = 0; i < fileList.size(); i++) {
            imagePathList.add(fileList.get(i).getPath());
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    public boolean delFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
