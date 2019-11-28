package com.yxst.mes.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.mes.R;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.LineDao;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.InspectDeviceValue;
import com.yxst.mes.database.model.Line;
import com.yxst.mes.database.Manager.InspectDevicValueQueryUtil;
import com.yxst.mes.database.Manager.PlaceQueryUtil;
import com.yxst.mes.database.Manager.RecordQueryUtil;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.database.model.Record;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By YuanCheng on 2019/6/3 11:18
 */
public class FinishRvAdapter extends RecyclerView.Adapter<FinishRvAdapter.RvViewHolder> {
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    //声明接口
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    List<InspectDevice> data;

    public FinishRvAdapter(List<InspectDevice> data) {
        this.data = data;
    }
    public FinishRvAdapter(Context context,List<InspectDevice> data) {
        this.mContext = context;
        this.data = data;
    }
    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_finish,viewGroup,false);
        RvViewHolder holder = new FinishRvAdapter.RvViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RvViewHolder holder, int i) {
        if(data.size()!=0){
            holder.llunfinish.setVisibility(View.GONE);
            final InspectDevice inspectDevice = data.get(i);
            List<Record> records = RecordQueryUtil.getRecordListByDeviceId(mContext,inspectDevice.getEquipmentID(),inspectDevice.getLineID(),1);
            InspectDeviceValue value = InspectDevicValueQueryUtil.getByDeviceId(mContext,inspectDevice.getEquipmentID(),inspectDevice.getLineID());
            List<PlaceValue> placeValues  = PlaceQueryUtil.getPlaceValueByStatus(mContext,inspectDevice.getEquipmentID(),inspectDevice.getLineID(),ConfigInfo.ITEM_INSPECT_STATUS);
            List<Place> places  = PlaceQueryUtil.getPlaceByDeviceId(mContext,inspectDevice.getEquipmentID(),inspectDevice.getLineID());
            Line line = DatabaseManager.getInstance().getLineDao().queryBuilder().where(LineDao.Properties.ID.eq(inspectDevice.getLineID())).unique();

            holder.tvTitle.setText(inspectDevice.getEquipmentName());
            holder.tvModel.setText(inspectDevice.getEquipmentModel());
            holder.tvLine.setText(line.getLineName());
            holder.tvStop.setText(inspectDevice.getRunStates()==0?"启用":"停用");
            holder.tvStartTime.setText(TimeUtil.dateNoYearFormat(inspectDevice.getBeginTime()));
            holder.tvEndTime.setText(TimeUtil.timeFormat(inspectDevice.getEndTime()));
            holder.tvTotal.setText(places.size()+"");
            holder.tvSize.setText((placeValues.size()+""));
            //uplaod为1，表示未上传，有数据则显示
            if(records.size()==0){
                holder.btnUplaod.setVisibility(View.GONE);
            }else{
                holder.btnUplaod.setVisibility(View.VISIBLE);
            }
            if(value!=null){
                if(value.getShowStatus()==1){
                    holder.btnUplaod.setEnabled(false);
                    holder.btnUplaod.setText("已上传");
                }else{
                    holder.btnUplaod.setEnabled(true);
                    holder.btnUplaod.setText("上传数据");
                }
            }
            holder.llPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null){
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onClick(holder.llPlace,inspectDevice.getEquipmentID(),inspectDevice.getLineID(),position);
                    }

                }
            });
            holder.btnUplaod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null){
                        int position = holder.getLayoutPosition();
                        mOnItemClickListener.onClick(holder.btnUplaod,inspectDevice.getEquipmentID(),inspectDevice.getLineID(),position);
                    }
                }
            });
        }else{
            holder.llunfinish.setVisibility(View.VISIBLE);
            holder.llfinish.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size()==0?1:data.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder{
        @BindView((R.id.ll_unfinish)) LinearLayout llunfinish;
        @BindView((R.id.ll_visibile)) LinearLayout llfinish;
        @BindView(R.id.tv_wait_title) TextView tvTitle;
        @BindView(R.id.tv_wait_start) TextView tvStartTime;
        @BindView(R.id.tv_wait_end) TextView tvEndTime;
        @BindView(R.id.ll_device) LinearLayout llPlace;
        @BindView(R.id.tv_progress1)TextView tvTotal;
        @BindView(R.id.tv_progress2) TextView tvSize;
        @BindView(R.id.btn_upload) Button btnUplaod;
        @BindView(R.id.tv_status)TextView tvStatus;
        @BindView(R.id.tv_model) TextView tvModel;
        @BindView(R.id.tv_line) TextView tvLine;
        @BindView(R.id.tv_stop) TextView tvStop;
        public RvViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view ,Long deviceId,Long lineId, int position);
    }
}

