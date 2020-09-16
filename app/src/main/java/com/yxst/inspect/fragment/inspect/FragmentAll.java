package com.yxst.inspect.fragment.inspect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.fragment.bean.InspectLine;

import java.util.ArrayList;
import java.util.List;

public class FragmentAll extends Fragment {
    private RecyclerView rv;
    private List<InspectLine> adapterData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapterData = new ArrayList<InspectLine>();

        InspectLine line = new InspectLine("制成一号线","05-09 10:10","05-19 12:00","万香椿","待检");
        InspectLine line2 = new InspectLine("制成二线","05-09 10:10","05-19 12:00","万香椿-万","待检");
        InspectLine line3 = new InspectLine("制成三线","05-09 10:10","05-19 12:00","万香椿","待检");
        InspectLine line4 = new InspectLine("制成四线","05-09 10:10","05-19 12:00","万香椿","待检");
        adapterData.add(line);
        adapterData.add(line2);
        adapterData.add(line3);
        adapterData.add(line4);
        Log.e("123456","LineData:"+adapterData.size());
        View view = inflater.inflate(R.layout.fragment_inspect_all,container,false);

        rv =  (RecyclerView)view.findViewById(R.id.rv_all);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new RvAdapter(adapterData));


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("size","FragmentAll------onDestroyView");

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder>{
        List<InspectLine> data;
        public RvAdapter(List<InspectLine> data) {
            this.data = data;
        }
        @NonNull
        @Override
        public RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_all,viewGroup,false);
            RvViewHolder holder = new RvAdapter.RvViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RvViewHolder holder, int i) {
                InspectLine line = data.get(i);
                holder.tvTitle.setText(line.getLineName());
                holder.tvStartTime.setText(line.getStartTime());
                holder.tvEndTime.setText(line.getStopTime());
                holder.tvPerson.setText(line.getPerson());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class RvViewHolder extends RecyclerView.ViewHolder{
            TextView tvTitle;
            TextView tvStartTime;
            TextView tvEndTime;
            TextView tvPerson;
            public RvViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
                tvStartTime = (TextView)itemView.findViewById(R.id.tv_start);
                tvEndTime = (TextView)itemView.findViewById(R.id.tv_endtime);
                tvPerson = (TextView)itemView.findViewById(R.id.tv_person);
            }
        }
    }
}
