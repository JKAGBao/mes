package project.bridgetek.com.applib.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import project.bridgetek.com.applib.R;
import project.bridgetek.com.applib.main.activity.DetailsActivity;
import project.bridgetek.com.applib.main.bean.CheckItem;
import project.bridgetek.com.applib.main.bean.CheckItemInfo;
import project.bridgetek.com.applib.main.toos.BlackDao;
import project.bridgetek.com.bridgelib.toos.Constants;

/**
 * Created by Cong Zhizhong on 18-7-3.
 */

public class AbnorFraAdapter extends BaseAdapter {
    private List<CheckItemInfo> mList;
    private Context mContext;
    private BlackDao mBlackDao;
    private String mResultId;

    public AbnorFraAdapter(List<CheckItemInfo> list, Context context, BlackDao blackDao, String ID) {
        this.mList = list;
        this.mContext = context;
        this.mBlackDao = blackDao;
        this.mResultId = ID;
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
        ViewHolder viewHolder = null;
        View view = convertView;
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
            viewHolder.mLlCancel = view.findViewById(R.id.ll_cancel);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CheckItemInfo item = getItem(position);
        List<CheckItem> accurate = mBlackDao.getCheckAccurate(item.getCheckItemID(), mResultId);
        if (accurate.size() > 0) {
            if (accurate.get(0).getException_YN().equals("1")) {
                viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_adapter_tv_result_text));
                viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.test_history_shape));
            } else {
                viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_tvresult_text));
                viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.work_title));
            }
        } else {
            viewHolder.mTvResult.setText(mContext.getResources().getString(R.string.upcom_watch_tvresult_text));
            viewHolder.mTvResult.setTextColor(mContext.getResources().getColor(R.color.work_title));
        }
        viewHolder.mRbAbnormal.setChecked(true);
        viewHolder.mRbNormal.setChecked(false);
        viewHolder.mRbNormal.setEnabled(false);
        viewHolder.mBtNormal.setBackgroundColor(Constants.GRAY);
        viewHolder.mBtAbnormal.setBackgroundColor(Constants.RED);
        viewHolder.mTvCheckItemDesc.setText(mContext.getString(R.string.task_over_abnoradapter_tvcheckitemdesc_text) + item.getCheckItemDesc());
        viewHolder.mTvESTStandard.setText(mContext.getString(R.string.task_over_abnoradapter_tveststandard_text) + item.getESTStandard());
        viewHolder.mLlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(Constants.CHECKINFO, item.getCheckItemID());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        RadioButton mRbAbnormal, mRbNormal;
        Button mBtNormal, mBtAbnormal;
        TextView mTvCheckItemDesc, mTvESTStandard;
        TextView mTvResult;
        LinearLayout mLlCancel;
    }
}
