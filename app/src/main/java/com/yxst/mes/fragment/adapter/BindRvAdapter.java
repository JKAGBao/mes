package com.yxst.mes.fragment.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxst.mes.R;
import com.yxst.mes.database.model.Device;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * 排班安排界面数据的填充的Adapter
 */
public class BindRvAdapter extends RecyclerView.Adapter<BindRvAdapter.BindViewHolder>{
    private OnClickListener mOnClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;

    }
    private final List<Device> DEVICES ;
    public BindRvAdapter(List<Device> devices){
        this.DEVICES = devices;
    }
    @NonNull
    @Override
    public BindViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_bind,viewGroup,false);
        return new BindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BindViewHolder sViewHolder, int position) {
        Device device = DEVICES.get(position);
        sViewHolder.tvModel.setText(device.getEquipmentModel());
        sViewHolder.tvName.setText(device.getEquipmentName());
        final Long deviceId = device.getEquipmentID();
        if(device.getRFID()!=null && !"".equals(device.getRFID())){
            sViewHolder.btnObtain.setText("更改RF卡");


        }else{
            sViewHolder.btnObtain.setText("绑定RF卡");
    }

        sViewHolder.llBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    int position = sViewHolder.getLayoutPosition();
                    mOnClickListener.onClick(sViewHolder.llBind,sViewHolder.btnObtain,deviceId,position);
                }
            }
        });
        sViewHolder.btnObtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    int position = sViewHolder.getLayoutPosition();
                    mOnClickListener.onClick(sViewHolder.llBind,sViewHolder.btnObtain,deviceId,position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return DEVICES.size();
    }

    class BindViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_bind_name) TextView tvName;
        @BindView(R.id.btn_bind_obtain) Button btnObtain;
        @BindView((R.id.ll_bind)) LinearLayout llBind;
        @BindView(R.id.tv_model) TextView tvModel;
        public BindViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickListener{
        public void onClick(View view,View btn,Long deviceId, int Position);
    }
}

