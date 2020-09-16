package com.yxst.inspect.activity.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.database.model.Line;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
/*
排班安排界面数据的填充的Adapter
 */
public class ScheduleRvAdapter extends RecyclerView.Adapter<ScheduleRvAdapter.ScheduleViewHolder>{
    private OnClickListener mOnClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
    private final List<Line> LINES ;
    public ScheduleRvAdapter(List<Line> lines){
        this.LINES = lines;
    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_shedule,viewGroup,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder sViewHolder, int position) {
        sViewHolder.tvName.setText(LINES.get(position).getLineName());
        final Long mId = LINES.get(position).getID();
        sViewHolder.btnObtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    int position = sViewHolder.getLayoutPosition();
                    mOnClickListener.onClick(sViewHolder.btnObtain,mId,position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return LINES.size();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_schedule_name)
        TextView tvName;
        @BindView(R.id.btn_shedule_obtain)
        Button btnObtain;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickListener{
        public void onClick(View view,Long lineId,int Position);
    }
}

