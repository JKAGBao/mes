package project.bridgetek.com.applib.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.AbnormalActivity;
import project.bridgetek.com.applib.main.activity.MeasureActivity;
import project.bridgetek.com.applib.main.activity.MeterActivity;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by Cong Zhizhong on 18-7-3.
 */

public class InterimAdapter extends BaseAdapter {
    List<CheckItemInfo> mList;
    Activity mContext;

    public InterimAdapter(List<CheckItemInfo> list, Activity context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CheckItemInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = convertView;
        final boolean[] normal = {false};
        final boolean[] abnormal = {false};
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_interim, null);
            viewHolder = new ViewHolder();
            viewHolder.mRbAbnormal = view.findViewById(R.id.rb_abnormal);
            viewHolder.mRbNormal = view.findViewById(R.id.rb_normal);
            viewHolder.mBtAbnormal = view.findViewById(R.id.bt_abnormal);
            viewHolder.mBtNormal = view.findViewById(R.id.bt_normal);
            viewHolder.mTvCheckItemDesc = view.findViewById(R.id.tv_CheckItemDesc);
            viewHolder.mTvESTStandard = view.findViewById(R.id.tv_ESTStandard);
            viewHolder.mLlCount = view.findViewById(R.id.ll_count);
            viewHolder.mTvStar = view.findViewById(R.id.tv_star);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CheckItemInfo item = getItem(position);
        if (item.isSubmit()) {
            if (item.getCheckOrderNo() == 3) {
                viewHolder.mBtAbnormal.setBackgroundColor(mContext.getResources().getColor(R.color.crimson));
                viewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                viewHolder.mRbAbnormal.setChecked(true);
                viewHolder.mRbNormal.setChecked(false);
                viewHolder.mRbNormal.setEnabled(false);
            } else {
                viewHolder.mBtNormal.setBackgroundColor(Constants.LIMEGREEN);
                viewHolder.mBtAbnormal.setBackgroundColor(mContext.getResources().getColor(R.color.noselect_bt));
                viewHolder.mRbAbnormal.setChecked(false);
                viewHolder.mRbNormal.setChecked(true);
                viewHolder.mRbAbnormal.setEnabled(false);
            }
        } else {
            if (item.getCheckOrderNo() == 4) {
                viewHolder.mBtNormal.setBackgroundColor(Constants.LIMEGREEN);
                viewHolder.mBtAbnormal.setBackgroundColor(mContext.getResources().getColor(R.color.noselect_bt));
                viewHolder.mRbAbnormal.setChecked(false);
                viewHolder.mRbNormal.setChecked(true);
            } else {
                viewHolder.mRbAbnormal.setChecked(false);
                viewHolder.mRbNormal.setChecked(false);
                viewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                viewHolder.mBtAbnormal.setBackgroundColor(Constants.GRAY);
            }
        }
        if (item.getCheckType().equals(Constants.GC)) {
            viewHolder.mLlCount.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlCount.setVisibility(View.GONE);
        }
        viewHolder.mTvCheckItemDesc.setText(mContext.getString(R.string.task_over_abnoradapter_tvcheckitemdesc_text) + item.getCheckItemDesc());
        viewHolder.mTvESTStandard.setText(mContext.getString(R.string.task_over_abnoradapter_tveststandard_text) + item.getESTStandard());
        if (item.getCheckType().equals(Constants.GC)) {
            viewHolder.mTvStar.setVisibility(View.GONE);
        } else {
            if (item.isSubmit()) {
                viewHolder.mTvStar.setVisibility(View.GONE);
            } else {
                viewHolder.mTvStar.setVisibility(View.VISIBLE);
            }
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mRbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSubmit()) {
                    finalViewHolder.mRbNormal.setChecked(!normal[0]);
                    finalViewHolder.mRbAbnormal.setChecked(false);
                    if (finalViewHolder.mRbNormal.isChecked()) {
                        finalViewHolder.mBtAbnormal.setBackgroundColor(Constants.GRAY);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.LIMEGREEN);
                        item.setCheckOrderNo(4);
                    } else {
                        item.setCheckOrderNo(1);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                    }
                    normal[0] = !normal[0];
                }

            }
        });
        viewHolder.mRbAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSubmit()) {
                    finalViewHolder.mRbAbnormal.setChecked(!abnormal[0]);
                    if (finalViewHolder.mRbNormal.isChecked()) {
                        finalViewHolder.mRbNormal.setChecked(false);
                        item.setCheckOrderNo(1);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                    }
                    if (normal[0]) {
                        normal[0] = !normal[0];
                    }
                    abnormal[0] = !abnormal[0];
                    if (finalViewHolder.mRbAbnormal.isChecked()) {
                        finalViewHolder.mBtAbnormal.setBackgroundColor(Constants.RED);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                    }
                    if (item.getCheckType().equals(Constants.GC)) {
                        Intent intent = new Intent(mContext, AbnormalActivity.class);
                        intent.putExtra(Constants.REEXCEPTION, mList.get(position));
                        mContext.startActivityForResult(intent, 1);
                    }
                }
            }
        });
        viewHolder.mBtNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSubmit()) {
                    finalViewHolder.mRbNormal.setChecked(!normal[0]);
                    finalViewHolder.mRbAbnormal.setChecked(false);
                    if (finalViewHolder.mRbNormal.isChecked()) {
                        finalViewHolder.mBtAbnormal.setBackgroundColor(Constants.GRAY);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.LIMEGREEN);
                        item.setCheckOrderNo(4);
                    } else {
                        item.setCheckOrderNo(1);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                    }
                    normal[0] = !normal[0];
                }

            }
        });
        viewHolder.mBtAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSubmit()) {
                    finalViewHolder.mRbAbnormal.setChecked(!abnormal[0]);
                    if (finalViewHolder.mRbNormal.isChecked()) {
                        finalViewHolder.mRbNormal.setChecked(false);
                        item.setCheckOrderNo(1);
                    }
                    if (normal[0]) {
                        normal[0] = !normal[0];
                    }
                    abnormal[0] = !abnormal[0];
                    if (finalViewHolder.mRbAbnormal.isChecked()) {
                        finalViewHolder.mBtAbnormal.setBackgroundColor(Constants.RED);
                        finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                    }
                    if (item.getCheckType().equals(Constants.GC)) {
                        Intent intent = new Intent(mContext, AbnormalActivity.class);
                        intent.putExtra(Constants.REEXCEPTION, mList.get(position));
                        mContext.startActivityForResult(intent, 1);
                    }
                }
            }
        });
        viewHolder.mTvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CheckItemInfo> meter = new ArrayList<>();
                List<CheckItemInfo> list = new ArrayList<>();
                if (item.getCheckType().equals(Constants.ZD) || item.getCheckType().equals(Constants.CW)) {
                    list.add(item);
                    Intent intent = new Intent(mContext, MeasureActivity.class);
                    intent.putExtra(Constants.ISFIRST, true);
                    intent.putExtra(Constants.ZD, (Serializable) list);
                    mContext.startActivityForResult(intent, 1);
                } else if (item.getCheckType().equals(Constants.CB)) {
                    meter.add(item);
                    Intent intent = new Intent(mContext, MeterActivity.class);
                    intent.putExtra(Constants.ISFIRST, true);
                    intent.putExtra(Constants.CB, (Serializable) meter);
                    intent.putExtra(Constants.ZD, (Serializable) list);
                    mContext.startActivityForResult(intent, 1);
                }
            }
        });
        viewHolder.mTvStar.setTypeface(HiApplication.MEDIUM);
        viewHolder.mTvCheckItemDesc.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvESTStandard.setTypeface(HiApplication.REGULAR);
        viewHolder.mBtNormal.setTypeface(HiApplication.REGULAR);
        viewHolder.mBtAbnormal.setTypeface(HiApplication.REGULAR);
        return view;
    }

    class ViewHolder {
        RadioButton mRbAbnormal, mRbNormal;
        Button mBtNormal, mBtAbnormal;
        TextView mTvCheckItemDesc, mTvESTStandard, mTvStar;
        LinearLayout mLlCount;
    }
}
