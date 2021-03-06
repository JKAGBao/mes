package com.yxst.inspect.fragment.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.model.Device;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By YuanCheng on 2019/6/3 11:18
 */
public class FinishAdapter extends RecyclerView.Adapter<FinishAdapter.RvViewHolder> {
    private OnItemClickListener mOnItemClickListener;
    //声明接口
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    List<Device> data;
    public FinishAdapter(List<Device> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_finish,viewGroup,false);
        RvViewHolder holder = new FinishAdapter.RvViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RvViewHolder holder, int i) {
        final Device device = data.get(i);
        //TODO
     //   List<Place> places  = device.getPlaceList();
        holder.tvTitle.setText(device.getEquipmentName());
        holder.tvStartTime.setText(new SimpleDateFormat("MM/dd HH:mm").format(new Date()));
        holder.tvEndTime.setText(new SimpleDateFormat("MM/dd HH:mm").format(new Date()));
//        holder.tvTotal.setText(places.size()+"");
//        List<Place> finishSize = new ArrayList<>();
//        for(int p=0;p<places.size();p++){
//            Place place = places.get(p);
//            if(place.getInspectStatus()==1){
//                finishSize.add(place);
//            }
//        }
//       holder.tvSize.setText(finishSize.size()+"");
        holder.llPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onClick(device.getEquipmentID(),position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_wait_title)
        TextView tvTitle;
        @BindView(R.id.tv_wait_start)
        TextView tvStartTime;
        @BindView(R.id.tv_wait_end)
        TextView tvEndTime;
        @BindView(R.id.ll_visibile)
        LinearLayout llPlace;
        @BindView(R.id.tv_progress1)TextView tvTotal;
        @BindView(R.id.tv_progress2) TextView tvSize;
        public RvViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public interface OnItemClickListener {
        void onClick(Long deviceId, int position);
    }
}

