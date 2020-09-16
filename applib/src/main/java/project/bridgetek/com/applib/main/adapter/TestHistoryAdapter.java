package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.TestDetailActivity;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by Cong Zhizhong on 18-7-31.
 */

public class TestHistoryAdapter extends BaseAdapter {
    List<CheckItem> mItemList;
    Context mContext;
    boolean mIsWatch = false;
    public List<CheckItem> mList = new ArrayList<>();

    public TestHistoryAdapter(List<CheckItem> itemList, Context context) {
        mItemList = itemList;
        mContext = context;
    }

    public void setWatch(boolean isWatch) {
        mIsWatch = isWatch;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public CheckItem getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_test_history, null);
            viewHolder = new ViewHolder();
            viewHolder.mTvUser = view.findViewById(R.id.tv_User);
            viewHolder.mTvTime = view.findViewById(R.id.tv_time);
            viewHolder.mTvInspect = view.findViewById(R.id.tv_inspect);
            viewHolder.mTvMObjectStatus = view.findViewById(R.id.tv_MObjectStatus);
            viewHolder.mTvMessage = view.findViewById(R.id.tv_message);
            viewHolder.mImgRefer = view.findViewById(R.id.img_refer);
            viewHolder.mTvVibration = view.findViewById(R.id.tv_vibration);
            viewHolder.mTvRemark = view.findViewById(R.id.tv_remark);
            viewHolder.mTvAbnormal = view.findViewById(R.id.tv_abnormal);
            viewHolder.mLlCount = view.findViewById(R.id.ll_count);
            viewHolder.mCheckBox = view.findViewById(R.id.checkBox);
            viewHolder.mLlConnect = view.findViewById(R.id.ll_connect);
            viewHolder.mImgCollect = view.findViewById(R.id.img_collect);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTvUser.setTypeface(HiApplication.BOLD);
        viewHolder.mTvInspect.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvTime.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMObjectStatus.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvMessage.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvAbnormal.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvRemark.setTypeface(HiApplication.REGULAR);
        viewHolder.mTvVibration.setTypeface(HiApplication.REGULAR);
        if (mIsWatch) {
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mCheckBox.setVisibility(View.GONE);
        }
        final CheckItem item = getItem(position);
        if (item.getException_YN().equals("1")) {
            viewHolder.mLlCount.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlCount.setVisibility(View.GONE);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.mCheckBox.isChecked()) {
                    mList.add(item);
                } else {
                    mList.remove(item);
                }
            }
        });
        if (item.isSubmit()) {
            viewHolder.mImgCollect.setBackgroundResource(R.mipmap.ic_carry_out);
        } else {
            viewHolder.mImgCollect.setBackgroundResource(R.mipmap.ic_carry_out_gray);
        }
        viewHolder.mLlConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsWatch) {
                    finalViewHolder.mCheckBox.setChecked(!finalViewHolder.mCheckBox.isChecked());
                    if (finalViewHolder.mCheckBox.isChecked()) {
                        mList.add(item);
                    } else {
                        mList.remove(item);
                    }
                } else {
                    Intent intent = new Intent(mContext, TestDetailActivity.class);
                    intent.putExtra(Constants.CHECKINFO, item);
                    mContext.startActivity(intent);
                }
            }
        });
        viewHolder.mTvInspect.setText(item.getMobjectName() + item.getPDADevice() + item.getComplete_TM() + item.getMemo_TX());
        viewHolder.mTvUser.setText(item.getMobjectName() + item.getPDADevice() + item.getComplete_TM() + item.getMemo_TX());
        viewHolder.mTvMObjectStatus.setText(item.getPDADevice() + item.getExceptionTransfer_YN());
        viewHolder.mTvTime.setText(item.getComplete_TM());
        viewHolder.mTvMessage.setText(item.getMemo_TX());
        return view;
    }

    class ViewHolder {
        TextView mTvUser, mTvInspect, mTvTime, mTvMObjectStatus;
        TextView mTvMessage;
        ImageView mImgRefer;
        TextView mTvVibration, mTvRemark, mTvAbnormal;
        LinearLayout mLlCount, mLlConnect;
        CheckBox mCheckBox;
        ImageView mImgCollect;
    }
}
