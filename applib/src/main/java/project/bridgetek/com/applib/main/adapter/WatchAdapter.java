package project.bridgetek.com.applib.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.AbnormalActivity;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;
import project.bridgetek.com.bridgelib.toos.LocalUserInfo;

/**
 * Created by Cong Zhizhong on 18-6-15.
 */

public class WatchAdapter extends BaseAdapter {
    List<CheckItemInfo> mList;
    Activity mContext;
    private BlackDao mBlackDao;
    private String mTaskid;
    private boolean autonomy;
    private boolean change;

    public WatchAdapter(List<CheckItemInfo> list, Activity context, String TaskID, boolean autonomy, boolean change) {
        this.mList = list;
        this.mContext = context;
        mBlackDao = BlackDao.getInstance(context);
        this.mTaskid = TaskID;
        this.autonomy = autonomy;
        this.change = change;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_watch, null);
            viewHolder = new ViewHolder();
            viewHolder.mRbAbnormal = view.findViewById(R.id.rb_abnormal);
            viewHolder.mRbNormal = view.findViewById(R.id.rb_normal);
            viewHolder.mBtAbnormal = view.findViewById(R.id.bt_abnormal);
            viewHolder.mBtNormal = view.findViewById(R.id.bt_normal);
            viewHolder.mTvCheckItemDesc = view.findViewById(R.id.tv_CheckItemDesc);
            viewHolder.mTvESTStandard = view.findViewById(R.id.tv_ESTStandard);
            viewHolder.mTvResult = view.findViewById(R.id.tv_result);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CheckItemInfo item = getItem(position);
        if (item.isSubmit()) {
            viewHolder.mBtAbnormal.setBackgroundColor(mContext.getResources().getColor(R.color.crimson));
            viewHolder.mRbAbnormal.setChecked(true);
            viewHolder.mRbNormal.setChecked(false);
            viewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
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
        List<CheckItem> accurate = mBlackDao.getCheckAccurate(item.getCheckItemID(), LocalUserInfo.getInstance(mContext).getUserInfo(Constants.CYCLETIME));
        if (accurate.size() > 0) {
            if (accurate.get(0).getException_YN().equals("0")) {
                viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_tvresult_text));
                viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.work_title));
            } else {
                viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_adapter_tv_result_text));
                viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.test_history_shape));
            }
        } else {
            viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_tvresult_text));
            viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.work_title));
        }
        viewHolder.mTvCheckItemDesc.setText(mContext.getString(R.string.task_over_abnoradapter_tvcheckitemdesc_text) + item.getCheckItemDesc());
        viewHolder.mTvESTStandard.setText(mContext.getString(R.string.task_over_abnoradapter_tveststandard_text) + item.getESTStandard());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mRbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.mRbNormal.setChecked(!normal[0]);
                finalViewHolder.mRbAbnormal.setChecked(false);
                normal[0] = !normal[0];
                if (finalViewHolder.mRbNormal.isChecked()) {
                    finalViewHolder.mBtAbnormal.setBackgroundColor(Constants.GRAY);
                    finalViewHolder.mBtNormal.setBackgroundColor(Constants.LIMEGREEN);
                    item.setCheckOrderNo(4);
                } else {
                    item.setCheckOrderNo(1);
                    finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                }
                if (item.isSubmit()) {
                    item.setSubmit(false);
                    if (!autonomy) {
                        List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mTaskid, item.getCheckItemID());
                        mBlackDao.delCheckAbnorItem(item1.get(0).getResult_ID());
                    }
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
                    }
                    if (normal[0]) {
                        normal[0] = !normal[0];
                    }
                    abnormal[0] = !abnormal[0];
                    Intent intent = new Intent(mContext, AbnormalActivity.class);
                    intent.putExtra(Constants.REEXCEPTION, mList.get(position));
                    intent.putExtra(Constants.AUTONOMY, autonomy);
                    intent.putExtra(Constants.CHANGE, change);
                    mContext.startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(mContext, AbnormalActivity.class);
                    intent.putExtra(Constants.ABNORMODIFY, true);
                    intent.putExtra(Constants.AUTONOMY, autonomy);
                    intent.putExtra(Constants.CHANGE, change);
                    intent.putExtra(Constants.REEXCEPTION, mList.get(position));
                    mContext.startActivityForResult(intent, 1);
                }
            }
        });
        viewHolder.mBtNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.mRbNormal.setChecked(!normal[0]);
                finalViewHolder.mRbAbnormal.setChecked(false);
                normal[0] = !normal[0];
                if (finalViewHolder.mRbNormal.isChecked()) {
                    finalViewHolder.mBtAbnormal.setBackgroundColor(Constants.GRAY);
                    finalViewHolder.mBtNormal.setBackgroundColor(Constants.LIMEGREEN);
                    item.setCheckOrderNo(4);
                } else {
                    item.setCheckOrderNo(1);
                    finalViewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
                }
                if (item.isSubmit()) {
                    item.setSubmit(false);
                    if (!autonomy) {
                        List<CheckItem> item1 = mBlackDao.getCheckAbnorItem(mTaskid, item.getCheckItemID());
                        mBlackDao.delCheckAbnorItem(item1.get(0).getResult_ID());
                    }
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
                    }
                    if (normal[0]) {
                        normal[0] = !normal[0];
                    }
                    abnormal[0] = !abnormal[0];
                    Intent intent = new Intent(mContext, AbnormalActivity.class);
                    intent.putExtra(Constants.REEXCEPTION, mList.get(position));
                    intent.putExtra(Constants.AUTONOMY, autonomy);
                    intent.putExtra(Constants.CHANGE, change);
                    mContext.startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(mContext, AbnormalActivity.class);
                    intent.putExtra(Constants.ABNORMODIFY, true);
                    intent.putExtra(Constants.AUTONOMY, autonomy);
                    intent.putExtra(Constants.CHANGE, change);
                    intent.putExtra(Constants.REEXCEPTION, mList.get(position));
                    mContext.startActivityForResult(intent, 1);
                }
            }
        });
        viewHolder.mTvCheckItemDesc.setTypeface(HiApplication.MEDIUM);
        viewHolder.mTvESTStandard.setTypeface(HiApplication.MEDIUM);
        viewHolder.mBtNormal.setTypeface(HiApplication.MEDIUM);
        viewHolder.mBtAbnormal.setTypeface(HiApplication.MEDIUM);
        viewHolder.mTvResult.setTypeface(HiApplication.MEDIUM);
        return view;
    }

    class ViewHolder {
        RadioButton mRbAbnormal, mRbNormal;
        Button mBtNormal, mBtAbnormal;
        TextView mTvCheckItemDesc, mTvESTStandard;
        TextView mTvResult;
    }
}
