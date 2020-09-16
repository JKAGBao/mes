package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.NfcActivity;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.applib.main.toos.TimeType;
import project.bridgetek.com.bridgelib.toos.Constants;
import project.bridgetek.com.bridgelib.toos.HiApplication;

/**
 * Created by Cong Zhizhong on 18-6-15.
 */

public class MobjectAdapter extends BaseAdapter {
    List<CheckItemInfo> mList;
    Context mContext;
    BlackDao mBlackDao;

    public MobjectAdapter(List<CheckItemInfo> list, Context context, BlackDao blackDao) {
        this.mList = list;
        this.mContext = context;
        this.mBlackDao = blackDao;
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
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_mobject, null);
            viewHolder = new ViewHolder();
            viewHolder.tvMobjectName = view.findViewById(R.id.tvMobjectName);
            viewHolder.tvOver = view.findViewById(R.id.tvOver);
            viewHolder.mLlcout = view.findViewById(R.id.ll_count);
            viewHolder.mTvResule = view.findViewById(R.id.tv_resule);
            viewHolder.mImgOver = view.findViewById(R.id.img_over);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CheckItemInfo item = getItem(position);
        if (item.getCheckType() != null) {
            if (item.getCheckType().equals(Constants.GC)) {
                viewHolder.tvMobjectName.setText(R.string.upcom_check_mobject_tvgc_text);
            } else if (item.getCheckType().equals(Constants.CB)) {
                viewHolder.tvMobjectName.setText(R.string.upcom_check_mobject_tvcb_text);
            } else if (item.getCheckType().equals(Constants.ZD)) {
                viewHolder.tvMobjectName.setText(R.string.upcom_check_mobject_tvzd_text);
            } else {
                viewHolder.tvMobjectName.setText(R.string.upcom_check_mobject_tvcw_text);
            }
        } else {
            viewHolder.tvMobjectName.setText(R.string.upcom_check_mobject_tvgc_text);
        }
        List<CheckItem> item1 = mBlackDao.getResultCheckItem(item.getTaskID(), item.getTaskItemID());
        if (item1.size() > 0) {
            if (item1.get(0).getResultValue().equals("")) {
                viewHolder.mTvResule.setVisibility(View.GONE);
                viewHolder.mImgOver.setVisibility(View.VISIBLE);
                viewHolder.mImgOver.setBackgroundResource(R.mipmap.ic_carry_out_gray);
            } else {
                viewHolder.mTvResule.setVisibility(View.VISIBLE);
                viewHolder.mImgOver.setVisibility(View.VISIBLE);
                viewHolder.mImgOver.setBackgroundResource(R.mipmap.ic_carry_out);
            }
        } else {
            viewHolder.mTvResule.setVisibility(View.GONE);
            viewHolder.mImgOver.setVisibility(View.INVISIBLE);
        }
        viewHolder.mTvResule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeType = TimeType.getTimeType(item.getTaskPlanEndTime());
                long nowTime = TimeType.getNowTime();
                if (nowTime - timeType < 1800) {
                    Intent intent = new Intent(new Intent(mContext, NfcActivity.class));
                    intent.putExtra(Constants.SUBMIT, true);
                    intent.putExtra(Constants.CHECKINFO, item);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, R.string.mobject_change_time, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.tvOver.setText(item.getCheckItemDesc());
        viewHolder.tvOver.setTypeface(HiApplication.MEDIUM);
        viewHolder.tvMobjectName.setTypeface(HiApplication.MEDIUM);
        return view;
    }

    class ViewHolder {
        TextView tvMobjectName;
        TextView tvOver;
        LinearLayout mLlcout;
        TextView mTvResule;
        ImageView mImgOver;
    }
}
