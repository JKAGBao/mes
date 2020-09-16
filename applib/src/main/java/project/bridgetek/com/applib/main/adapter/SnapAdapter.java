package project.bridgetek.com.applib.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.MeasureActivity;
import project.bridgetek.com.applib.main.activity.MeterActivity;
import project.bridgetek.com.applib.main.activity.WatchActivity;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.MyControl.DG_ListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by Cong Zhizhong on 18-7-2.
 */

public class SnapAdapter extends BaseAdapter {
    public List<CheckItemInfo> mList;
    Activity mContext;
    boolean mOpen;
    private int mCurrentItem = -1;
    private BlackDao mBlackDao;

    public SnapAdapter(List<CheckItemInfo> list, Activity context, boolean open) {
        this.mList = list;
        this.mContext = context;
        this.mOpen = open;
        mBlackDao = BlackDao.getInstance(context);
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
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_check_info, null);
            viewHolder = new ViewHolder();
            viewHolder.mLabelName = view.findViewById(R.id.LabelName);
            viewHolder.mCompLete = view.findViewById(R.id.compLete);
            viewHolder.mIcDown = view.findViewById(R.id.ic_down);
            viewHolder.mLvMobjectName = view.findViewById(R.id.lv0MobjectName);
            viewHolder.mLayout = view.findViewById(R.id.count);
            viewHolder.mLlCount = view.findViewById(R.id.ll_count);
            viewHolder.mLlOpen = view.findViewById(R.id.ll_open);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CheckItemInfo item = getItem(position);
        viewHolder.mLabelName.setText(item.getMobjectName());
        int mobjectCodeNumCount = mBlackDao.getMobjectCodeNumCount(item.getTaskID(), item.getLabelID(), item.getMobjectCode());
        int mobjectCodeCheckCount = mBlackDao.getSnapMobject(item.getTaskID(), item.getLabelID(), item.getMobjectCode());
        viewHolder.mCompLete.setText(CountString.LEFT_BRACKETS + mobjectCodeCheckCount + CountString.SEMICOLON + mobjectCodeNumCount + CountString.RIGHT_BRACKETS);
        if (mCurrentItem == position) {
            List<CheckItemInfo> t0001 = mBlackDao.getSnapMobjectItem(item.getTaskID(), item.getLabelID(), item.getMobjectCode());
            SnapMobjectAdapter mobjectAdapter = new SnapMobjectAdapter(t0001, mContext);
            Logger.i(mList.size());
            viewHolder.mLvMobjectName.setAdapter(mobjectAdapter);
            viewHolder.mIcDown.setBackgroundResource(R.drawable.ic_check_close);
            viewHolder.mLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mIcDown.setBackgroundResource(R.drawable.ic_check_open);
            viewHolder.mLayout.setVisibility(View.GONE);
        }
        viewHolder.mLlOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int tag = (Integer) v.getTag();
                if (tag == mCurrentItem) {
                    mCurrentItem = -1;
                } else {
                    mCurrentItem = tag;
                }
                notifyDataSetChanged();
            }
        });
        if (mOpen) {
            viewHolder.mLlCount.setVisibility(View.GONE);
        } else {
            viewHolder.mLlCount.setVisibility(View.VISIBLE);
        }
        viewHolder.mLlOpen.setTag(position);
        viewHolder.mLabelName.setTypeface(HiApplication.BOLD);
        viewHolder.mCompLete.setTypeface(HiApplication.MEDIUM);
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.mLvMobjectName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckItemInfo item1 = (CheckItemInfo) finalViewHolder1.mLvMobjectName.getAdapter().getItem(position);
                Intent intent = null;
                if (item1.getCheckType().equals(Constants.GC)) {
                    intent = new Intent(mContext, WatchActivity.class);
                } else if (item1.getCheckType().equals(Constants.CB)) {
                    intent = new Intent(mContext, MeterActivity.class);
                } else {
                    intent = new Intent(mContext, MeasureActivity.class);
                }
                intent.putExtra(Constants.SUBMIT, true);
                intent.putExtra(Constants.CHECKINFO, item1);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView mLabelName;
        TextView mCompLete;
        ImageView mIcDown;
        DG_ListView mLvMobjectName;
        LinearLayout mLayout, mLlOpen, mLlCount;
    }
}
