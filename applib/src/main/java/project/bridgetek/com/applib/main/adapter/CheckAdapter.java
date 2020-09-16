package project.bridgetek.com.applib.main.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.MyControl.DG_ListView;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.CountString;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;
import project.bridgetek.com.bridgelib.toos.Logger;

/**
 * Created by Cong Zhizhong on 18-6-14.
 */

public class CheckAdapter extends BaseAdapter {
    public static List<CheckItemInfo> LIST;
    Activity mContext;
    boolean mOpen;
    private int mCurrentItem = -1;
    private BlackDao mBlackDao;

    public CheckAdapter(List<CheckItemInfo> list, Activity context, boolean open) {
        this.LIST = list;
        this.mContext = context;
        this.mOpen = open;
        mBlackDao = BlackDao.getInstance(context);
    }

    @Override
    public int getCount() {
        return LIST.size();
    }

    @Override
    public CheckItemInfo getItem(int position) {
        return LIST.get(position);
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
            viewHolder.mIcShang = view.findViewById(R.id.ic_shang);
            viewHolder.mIcXia = view.findViewById(R.id.ic_xia);
            viewHolder.mLlOpen = view.findViewById(R.id.ll_open);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CheckItemInfo item = getItem(position);
        viewHolder.mLabelName.setText(item.getMobjectName());
        int mobjectCodeNumCount = mBlackDao.getMobjectCodeNumCount(item.getTaskID(), item.getLabelID(), item.getMobjectCode());
        int mobjectCodeCheckCount = mBlackDao.getMobjectCodeCheckCount(LocalUserInfo.getInstance(mContext).getUserInfo(Constants.TASKID), item.getLabelID(), item.getMobjectCode());
        viewHolder.mCompLete.setText(CountString.LEFT_BRACKETS + mobjectCodeCheckCount + CountString.SEMICOLON + mobjectCodeNumCount + CountString.RIGHT_BRACKETS);
        if (mCurrentItem == position) {
            List<CheckItemInfo> t0001 = mBlackDao.getCheckItem(false, LocalUserInfo.getInstance(mContext).getUserInfo(Constants.TASKID), LIST.get(position).getLineID(), LIST.get(position).getLabelID(), LIST.get(position).getMobjectCode());
//            int i = dip2px(mContext, 43);
//            if (t0001.size() <= 10) {
//                LinearLayout.LayoutParams para;
//                para = (LinearLayout.LayoutParams) viewHolder.mLayout.getLayoutParams();
//                para.height = i * t0001.size();
//                viewHolder.mLvMobjectName.setLayoutParams(para);
//            } else {
//                LinearLayout.LayoutParams para;
//                para = (LinearLayout.LayoutParams) viewHolder.mLayout.getLayoutParams();
//                para.height = i * 10;
//                viewHolder.mLvMobjectName.setLayoutParams(para);
//            }
            MobjectAdapter mobjectAdapter = new MobjectAdapter(t0001, mContext, mBlackDao);
            Logger.i(LIST.size());
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
        final ViewHolder finalViewHolder = viewHolder;
        if (mOpen) {
            viewHolder.mLlCount.setVisibility(View.GONE);
        } else {
            viewHolder.mLlCount.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            viewHolder.mIcShang.setBackgroundResource(R.mipmap.btn_up_arrow_gray_enabled);
        }
        if (position == (LIST.size() - 1)) {
            viewHolder.mIcXia.setBackgroundResource(R.mipmap.btn_down_arrow_gray_normal);
        }
        viewHolder.mIcShang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    Collections.swap(LIST, position, position - 1);
                    notifyDataSetChanged();
                }
            }
        });
        viewHolder.mIcXia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != (LIST.size() - 1)) {
                    Collections.swap(LIST, position, position + 1);
                    notifyDataSetChanged();
                }
            }
        });
        viewHolder.mLlOpen.setTag(position);
        viewHolder.mLabelName.setTypeface(HiApplication.BOLD);
        viewHolder.mCompLete.setTypeface(HiApplication.MEDIUM);
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.mLvMobjectName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckItemInfo item1 = (CheckItemInfo) finalViewHolder1.mLvMobjectName.getAdapter().getItem(position);
                showDia(mContext, item1, mBlackDao);
            }
        });
        return view;
    }

    public void showDia(Activity context, CheckItemInfo checkItemInfo, BlackDao mBlackDao) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_check, null, false);
        final Dialog builder = new Dialog(context, R.style.update_dialog);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        TextView tvPolice = view.findViewById(R.id.tv_police);
        TextView tvLimit = view.findViewById(R.id.tv_limit);
        TextView tvInterval = view.findViewById(R.id.tv_interval);
        TextView tvNextTime = view.findViewById(R.id.tv_next_time);
        TextView tvNextInterval = view.findViewById(R.id.tv_next_interval);
        TextView tvClose = view.findViewById(R.id.tv_close);
        TextView tvResult = view.findViewById(R.id.tv_result);
        TextView tvLevel = view.findViewById(R.id.tv_level);
        String alarmType;
        List<String> list = LocalUserInfo.getInstance(context).getDataList(Constants.EXCEPTIONLEVE);
        List<CheckItem> checkAbnorItem = mBlackDao.getCheckAbnorItem(checkItemInfo.getTaskID(), checkItemInfo.getCheckItemID());
        if (checkItemInfo.getAlarmType_ID().equals("0")) {
            alarmType = context.getString(R.string.check_upper_limit_text);
        } else if (checkItemInfo.getAlarmType_ID().equals("1")) {
            alarmType = context.getString(R.string.check_lower_limit_text);
        } else if (checkItemInfo.getAlarmType_ID().equals("2")) {
            alarmType = context.getString(R.string.check_within_text);
        } else {
            alarmType = context.getString(R.string.check_external_text);
        }
        if (!list.isEmpty() & !checkAbnorItem.isEmpty()) {
            int i = Integer.parseInt(checkAbnorItem.get(0).getExceptionLevel());
            if (i != 0) {
                tvLevel.setText(list.get(i - 1));
            } else {
                tvLevel.setText(context.getString(R.string.snap_adapter_btnormal_text));
            }
        }
        tvPolice.setText(alarmType);
        tvResult.setText(checkAbnorItem.isEmpty() ? "" : checkAbnorItem.get(0).getResultValue());
        if (!checkAbnorItem.isEmpty() && checkAbnorItem.get(0).getResultValue().equals("")) {
            String mObjectStatus = checkAbnorItem.get(0).getMObjectStatus();
            String state = "-[运行]";
            if (mObjectStatus.equals("0")) {
                state = "-[运行]";
            } else if (mObjectStatus.equals("1")) {
                state = "-[停机]";
            } else if (mObjectStatus.equals("3")) {
                state = "-[检修]";
            } else if (mObjectStatus.equals("4")) {
                state = "-[备用]";
            }
            tvResult.setText(state);
            tvLevel.setText(CountString.CONNECTOR);
        }
        String limit1 = "";
        String limit2 = "";
        String limit3 = "";
        String limit4 = "";
        if (checkItemInfo.getLowerLimit1() != -100 || checkItemInfo.getUpperLimit1() != -100) {
            limit1 = (checkItemInfo.getLowerLimit1() == -100 ? "" : checkItemInfo.getLowerLimit1()) + CountString.WAVE + (checkItemInfo.getUpperLimit1() == -100 ? "" : checkItemInfo.getUpperLimit1()) + ",";
        }
        if (checkItemInfo.getLowerLimit2() != -100 || checkItemInfo.getUpperLimit2() != -100) {
            limit2 = (checkItemInfo.getLowerLimit2() == -100 ? "" : checkItemInfo.getLowerLimit2()) + CountString.WAVE + (checkItemInfo.getUpperLimit2() == -100 ? "" : checkItemInfo.getUpperLimit2()) + ",";
        }
        if (checkItemInfo.getLowerLimit3() != -100 || checkItemInfo.getUpperLimit3() != -100) {
            limit3 = (checkItemInfo.getLowerLimit3() == -100 ? "" : checkItemInfo.getLowerLimit3()) + CountString.WAVE + (checkItemInfo.getUpperLimit3() == -100 ? "" : checkItemInfo.getUpperLimit3()) + ",";
        }
        if (checkItemInfo.getLowerLimit4() != -100 || checkItemInfo.getUpperLimit4() != -100) {
            limit4 = (checkItemInfo.getLowerLimit4() == -100 ? "" : checkItemInfo.getLowerLimit4()) + CountString.WAVE + (checkItemInfo.getUpperLimit4() == -100 ? "" : checkItemInfo.getUpperLimit4());
        }
        tvLimit.setText(limit1 + limit2 + limit3 + limit4);
        tvInterval.setText(checkItemInfo.getTaskPlanStartTime() + CountString.WAVE + checkItemInfo.getTaskPlanEndTime());
        tvNextTime.setText(checkItemInfo.getNextTaskDate());
        tvNextInterval.setText(checkItemInfo.getNextTaskDateTimePeriods());
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();
        Display display = context.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        builder.setContentView(view, layoutParams);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    class ViewHolder {
        TextView mLabelName;
        TextView mCompLete;
        ImageView mIcShang, mIcXia;
        ImageView mIcDown;
        DG_ListView mLvMobjectName;
        LinearLayout mLayout, mLlCount, mLlOpen;
    }
}
