package com.yxst.mes.nfc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.mes.R;
import com.yxst.mes.database.DatabaseManager;
import com.yxst.mes.database.dao.DeviceDao;
import com.yxst.mes.database.dao.LineDao;
import com.yxst.mes.database.model.Device;
import com.yxst.mes.database.model.Item;
import com.yxst.mes.database.model.ItemValue;
import com.yxst.mes.database.model.Line;
import com.yxst.mes.database.Manager.ItemQueryUtil;
import com.yxst.mes.database.Manager.ItemValueQueryUtil;
import com.yxst.mes.database.model.Place;
import com.yxst.mes.util.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>  {
    private List<Place> places;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    //声明接口
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    Map<Integer,Boolean> map = new HashMap<>();
    public PlaceAdapter(Context context, List<Place> places){
        this.mContext = context;
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_lv_line,viewGroup,false);
        PlaceAdapter.ViewHolder holder = new PlaceAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Place place = places.get(position);
        Device device = DatabaseManager.getInstance().getDeviceDao().queryBuilder().where(
                 DeviceDao.Properties.LineID.eq(place.getLineID()),
                DeviceDao.Properties.EquipmentID.eq(place.getEquipmentID())).unique();
        List<Line> lines = DatabaseManager.getInstance().getLineDao().queryBuilder().where(
                LineDao.Properties.ID.eq(place.getLineID())).list();
//        List<InspectLine> lines = DatabaseManager.getInstance().getInspectLineDao().queryBuilder().where(
//                InspectLineDao.Properties.ID.eq(place.getLineID())).list();
        List<Item> items = ItemQueryUtil.getItemByPlaceId(mContext,place.getPlaceID(),place.getLineID());
        //    List<Item> finishItems = ItemQueryUtil.getItemByStatus(mContext,place.getPlaceID(),place.getLineID());
        //一个部位下 完成的Item。
        List<ItemValue> finishItems = ItemValueQueryUtil.getItemValueByPIdAndStatus(mContext,place.getPlaceID(),place.getLineID(),1);

        //1.待巡检 2 漏检 3 已巡检
        holder.tvPlaceName.setText(place.getPlaceName());
        holder.tvDeviceName.setText(device!=null?device.getEquipmentName():"");
        holder.tvLineName.setText(lines.size()!=0?lines.get(0).getLineName():"");
        holder.tvPlaceName.setText(place.getPlaceName());
        holder.tvProgress2.setText(items.size()+"");
        holder.tvProgress1.setText(finishItems.size()+"");
        holder.tvStart.setText(TimeUtil.dateNoYearFormat(place.getBeginTime()));
        holder.tvEnd.setText(TimeUtil.timeFormat(place.getEndTime()));
        if(items.size()!=0 &&finishItems.size()!=0 && items.size() == finishItems.size() ){
            map.put(position,true);
        }
        if(map.containsKey(position)){
            holder.tvFinish.setVisibility(View.VISIBLE);
        }else{
            holder.tvFinish.setVisibility(View.GONE);
        }
        holder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v,place.getPlaceName(),place.getPlaceID(),position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return places.size()==0?1:places.size();
    }
    public interface OnItemClickListener {
        void onClick(View view, String placeName, Long placeId, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_place_name)TextView tvPlaceName;
        @BindView(R.id.tv_finish)TextView tvFinish;
        @BindView(R.id.tv_progress1)TextView tvProgress1;
        @BindView(R.id.tv_progress2)TextView tvProgress2;
        @BindView(R.id.tv_start)TextView tvStart;
        @BindView(R.id.tv_end)TextView tvEnd;
        @BindView(R.id.tv_line)TextView tvLineName;
        @BindView(R.id.tv_device)TextView tvDeviceName;
        @BindView(R.id.ll_add)LinearLayout llAdd;
        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);

        }
    }

}
