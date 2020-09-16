package com.yxst.inspect.activity.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
类名命名规则：使用界面+功能模块+控件名称
 */
public class PrimaryRvAdapter extends RecyclerView.Adapter<PrimaryRvAdapter.ItemViewHolder> {
    private List<PrimaryItem> list ;
    private Context ctx;
    public PrimaryRvAdapter(List<PrimaryItem> list, Context context) {
        this.list = list;
        ctx = context;

    }
    //Item点击事件的回调监听
    private OnItemClickListener mOnItemClickListener;
     // 回调方法 将接口传递进来
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    /*
    ViewHolder和界面布局关联，创建ViewHolder
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_rv_primary,viewGroup,false);
        return new ItemViewHolder(view);
    }
    /*
    展示数据
     */
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder viewHolder, final int position) {
    //    viewHolder.llPrimary.setBackgroundColor(ctx.getResources().getColor(list.get(position).background));
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
//        float density = dm.density;
//        int screenwidth = (int)(width/density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/2,width/2);
        viewHolder.llPrimary.setLayoutParams(params);
        viewHolder.mImageView.setImageResource(list.get(position).imageView);
        viewHolder.mText.setText(list.get(position).name);

           //activity调用setOnItemClickListener() 如果调用接口不为空执行下面逻辑
           if (mOnItemClickListener != null) {
viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //返回对应view的信息
        int pos = viewHolder.getLayoutPosition();
        mOnItemClickListener.onItemClick(viewHolder.mText,pos);
    }
});
               viewHolder.mText.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //返回对应view的信息
                       int pos = viewHolder.getLayoutPosition();
                       mOnItemClickListener.onItemClick(viewHolder.mText,pos);
                   } });
               viewHolder.mText.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View view) {
                       int pos = viewHolder.getLayoutPosition();
                       mOnItemClickListener.onItemLongClick(viewHolder.mText,pos);
                       return false;
                   } });

           }

               }

       @Override
       public int getItemCount() {
           return list.size();
       }

//    @Override
//    public void onClick(View v) {
//        //返回对应view的信息
////        int pos = mViewHolder.getLayoutPosition();
////        mOnItemClickListener.onItemClick(mViewHolder.mText,pos);
//    }

    /*
     ViewHolder创建
     */
        final class ItemViewHolder extends RecyclerView.ViewHolder{

               @BindView(R.id.tv_item_name) TextView mText;
               @BindView(R.id.iv_device_item) ImageView mImageView;
               @BindView(R.id.ll_primary) LinearLayout llPrimary;

               public ItemViewHolder(@NonNull View itemView) {
                   super(itemView);
                   ButterKnife.bind(this,itemView);
               }
           }
        /*
        自定义回调监听一个接口 实现单击和长点击事件
         */
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

        }

