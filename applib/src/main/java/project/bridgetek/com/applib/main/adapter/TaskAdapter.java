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
import project.bridgetek.com.applib.main.bean.TaskInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by Cong Zhizhong on 18-6-13.
 */

public class TaskAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskInfo> mList;
    private String mStarTime, mEndTime, mSpotTime;
    private BlackDao mBlackdao;

    public TaskAdapter(Context mContext, List<TaskInfo> mList, BlackDao blackDao) {
        this.mContext = mContext;
        this.mList = mList;
        this.mBlackdao = blackDao;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TaskInfo getItem(int position) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_taskinfo, null);
            viewHolder = new ViewHolder();
            viewHolder.mWorkname = view.findViewById(R.id.workname);
            viewHolder.mTime = view.findViewById(R.id.time);
            viewHolder.mPercent = view.findViewById(R.id.baifenbi);
            viewHolder.mTvOver = view.findViewById(R.id.tv_over);
            viewHolder.mImgSpot = view.findViewById(R.id.img_spot);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TaskInfo item = getItem(position);
        try {
            mStarTime = TimeType.getDate(item.getTaskPlanStartTime());
            mEndTime = TimeType.getDate(item.getTaskPlanEndTime());
            mSpotTime = TimeType.getDate(item.getTaskPlanEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (item.getTaskType().equals("1")) {
            viewHolder.mImgSpot.setBackgroundResource(R.mipmap.ic_spot);
            viewHolder.mTime.setText(mStarTime + CountString.CONNECTOR + mSpotTime);
        } else {
            viewHolder.mImgSpot.setBackgroundResource(R.mipmap.ic_patrol);
            viewHolder.mTime.setText(mStarTime + CountString.CONNECTOR + mEndTime);
        }
        int l0001 = mBlackdao.getLineNumCount(item.getTaskID());
        int t001 = mBlackdao.getTaskCheckCount(item.getTaskID());
        if (t001 >= l0001 && t001 != 0) {
            viewHolder.mTvOver.setBackgroundColor(mContext.getResources().getColor(R.color.upcom_frament_tv_completed));
            viewHolder.mTvOver.setText(R.string.upcom_taskadapter_tvover_text);
            viewHolder.mWorkname.setTextColor(mContext.getResources().getColor(R.color.region_over));
        } else if (t001 < l0001 && t001 != 0) {
            viewHolder.mTvOver.setBackgroundColor(mContext.getResources().getColor(R.color.upcom_frament_tv_inhand));
            viewHolder.mTvOver.setText(R.string.upcom_taskadapter_tvover_text2);
        } else {
            viewHolder.mTvOver.setBackgroundColor(mContext.getResources().getColor(R.color.upcom_frament_tv_notstart));
            viewHolder.mTvOver.setText(R.string.task_upcoming_tv_refer_text);
        }
        viewHolder.mWorkname.setText(item.getLineName());
        viewHolder.mPercent.setText(t001 + CountString.SEMICOLON + l0001);
        viewHolder.mWorkname.setTypeface(HiApplication.BOLD);
        viewHolder.mTvOver.setTypeface(HiApplication.REGULAR);
        viewHolder.mTime.setTypeface(HiApplication.BOLD);
        viewHolder.mPercent.setTypeface(HiApplication.BOLD);
        return view;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        boolean isEnabled = false;
//        int t001 = mBlackdao.getTaskCheckCount(getItem(position).getTaskID());
//        int l0001 = mBlackdao.getLineNumCount(getItem(position).getLineID());
//        if (t001 >= l0001 && t001 != 0) {
//            isEnabled = false;
//        } else {
//            isEnabled = true;
//        }
//        return isEnabled;
//    }

    class ViewHolder {
        TextView mWorkname, mTvOver;
        TextView mTime;
        TextView mPercent;
        ImageView mImgSpot;
    }
}
