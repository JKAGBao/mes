package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

/**
 * Created by Cong Zhizhong on 18-6-20.
 */

public class RegionAdapter extends BaseAdapter {
    List<CheckItemInfo> mList;
    Context mContext;
    BlackDao mBlackDao;
    String mStarttime;

    public RegionAdapter(List<CheckItemInfo> list, Context context, BlackDao blackDao, String starttime) {
        this.mList = list;
        this.mContext = context;
        this.mBlackDao = blackDao;
        this.mStarttime = starttime;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_region, null);
            viewHolder = new ViewHolder();
            viewHolder.mCompLete = view.findViewById(R.id.compLete);
            viewHolder.mLabelName = view.findViewById(R.id.LabelName);
            viewHolder.mStartTime = view.findViewById(R.id.startTime);
            viewHolder.mOverTime = view.findViewById(R.id.overTime);
            viewHolder.mImgOver = view.findViewById(R.id.img_over);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CheckItemInfo item = getItem(position);
        List<CheckItemInfo> labelTime = mBlackDao.getLabelTime(item.getTaskID(), item.getLabelID());
        String s = "";
        if (!labelTime.isEmpty()) {
            for (int i = 0; i < labelTime.size(); i++) {
                try {
                    String time = TimeType.toData(labelTime.get(i).getTaskPlanStartTime()) + CountString.WAVE + TimeType.toData(labelTime.get(i).getTaskPlanEndTime());
                    if (i < labelTime.size() - 1) {
                        s = s + time + ",";
                    } else {
                        s = s + time;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        int labelIDNumCount = mBlackDao.getLabelIDNumCount(item.getTaskID(), item.getLabelID());
        int labelCheckCount = mBlackDao.getLabelCheckCount(LocalUserInfo.getInstance(mContext).getUserInfo(Constants.TASKID), item.getLabelID());
        viewHolder.mLabelName.setText(item.getLabelName());
        viewHolder.mLabelName.setTextColor(mContext.getResources().getColor(R.color.work_title));
        viewHolder.mStartTime.setText(s);
        viewHolder.mOverTime.setText(CountString.UNKNOWN);
        viewHolder.mImgOver.setBackgroundResource(R.drawable.ic_round_red);
        if (labelCheckCount > 0) {
            viewHolder.mImgOver.setBackgroundResource(R.drawable.ic_round_orange);
        }
        if (labelCheckCount >= labelIDNumCount) {
            List<CheckItem> item1 = mBlackDao.getRegioCheckItem(LocalUserInfo.getInstance(mContext).getUserInfo(Constants.TASKID), item.getLabelID());
            String overTime = null;
            try {
                viewHolder.mImgOver.setBackgroundResource(R.drawable.ic_round_green);
                viewHolder.mLabelName.setTextColor(mContext.getResources().getColor(R.color.region_over));
                overTime = TimeType.stringToDate(item1.get(0).getComplete_TM());
                viewHolder.mOverTime.setText(overTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        viewHolder.mCompLete.setText(labelCheckCount + CountString.SEMICOLON + labelIDNumCount);
        viewHolder.mLabelName.setTypeface(HiApplication.BOLD);
        viewHolder.mStartTime.setTypeface(HiApplication.BOLD);
        viewHolder.mOverTime.setTypeface(HiApplication.BOLD);
        viewHolder.mCompLete.setTypeface(HiApplication.BOLD);
        return view;
    }

    class ViewHolder {
        TextView mLabelName;
        TextView mCompLete;
        TextView mStartTime;
        TextView mOverTime;
        ImageView mImgOver;
    }
}
