package com.yxst.mes.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.mes.R;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.LineDao;
import com.yxst.mes.database.model.InspectDevice;
import com.yxst.mes.database.model.Line;
import com.yxst.mes.database.Manager.PlaceQueryUtil;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.database.model.PlaceValue;
import com.yxst.mes.fragment.ConfigInfo;
import com.yxst.mes.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By YuanCheng on 2019/6/3 11:18
 */
public class AnInspectAdapter extends RecyclerView.Adapter<AnInspectAdapter.RvViewHolder> {
    private OnItemClickListener mOnItemClickListener;
    private  List<InspectDevice> data;
    private Context mContext;

    //声明接口
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public AnInspectAdapter(List<InspectDevice> data) {
        this.data = data;
    }
    public AnInspectAdapter(Context context, List<InspectDevice> data) {
        this.mContext = context;
        this.data = data;
    }
    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_aninspect,viewGroup,false);
        RvViewHolder holder = new AnInspectAdapter.RvViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RvViewHolder holder, int i) {
        if(data.size()==0){
            holder.llNoinspect.setVisibility(View.VISIBLE);
            holder.llInspect.setVisibility(View.GONE);
        }else{
            holder.llNoinspect.setVisibility(View.GONE);
            final InspectDevice device = data.get(i);
            List<Place> places  = PlaceQueryUtil.getPlaceByDeviceId(mContext,device.getEquipmentID(),device.getLineID());
            List<PlaceValue> placeValues  = PlaceQueryUtil.getPlaceValueByStatus(mContext,device.getEquipmentID(),device.getLineID(),ConfigInfo.ITEM_INSPECT_STATUS);
            Line line = DatabaseManager.getInstance().getLineDao().queryBuilder().where(LineDao.Properties.ID.eq(device.getLineID())).unique();
            holder.tvTitle.setText(device.getEquipmentName());
            holder.tvStatus.setText(device.getRunStates()==0?"启用":"停用");
            holder.tvModel.setText(device.getEquipmentModel());
            holder.tvLine.setText(line==null?"":line.getLineName());
            holder.tvStartTime.setText(TimeUtil.dateNoYearFormat(device.getBeginTime()));
            holder.tvEndTime.setText(TimeUtil.timeFormat(device.getEndTime()));
            holder.tvTotal.setText(places.size()+"");
         //   List<Place> finishSize = new ArrayList<>();
//            for(int p=0;p<places.size();p++){
//                Place place = places.get(p);
//                if(place.getInspectStatus()==1){
//                    finishSize.add(place);
//                }
//            }

            holder.tvSize.setText(placeValues.size()+"");
            final int position = holder.getLayoutPosition();
            holder.llDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onClick(device.getEquipmentID(),device.getLineID(),position);
                    }

                }
            });
            holder.ivMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,holder.ivMsg,device,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size()==0?1:data.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_wait_title) TextView tvTitle;
        @BindView(R.id.tv_model) TextView tvModel;
        @BindView(R.id.tv_line) TextView tvLine;
        @BindView(R.id.tv_status) TextView tvStatus;
        @BindView(R.id.tv_wait_start) TextView tvStartTime;
        @BindView(R.id.tv_wait_end) TextView tvEndTime;
        @BindView(R.id.ll_device) LinearLayout llDevice;
        @BindView(R.id.tv_progress1)TextView tvTotal;
        @BindView(R.id.tv_progress2) TextView tvSize;
        @BindView(R.id.iv_msg) ImageView ivMsg;
        @BindView(R.id.ll_noinspect) LinearLayout llNoinspect;
        @BindView(R.id.ll_inspect) LinearLayout llInspect;
        public RvViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void AddHeaderItem(List<InspectDevice> items) {
        data.addAll(data.size(),items);
//        data.addAll(0, items);
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onClick(Long deviceId, Long lineId, int position);
        void onItemClick(View view, View btn, InspectDevice device , int Position);
    }
}

