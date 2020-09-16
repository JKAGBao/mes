package com.yxst.inspect.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxst.inspect.R;
import com.yxst.inspect.fragment.bean.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
类名命名规则：使用界面+功能模块+控件名称
 */
public class ItemRvAdapter extends RecyclerView.Adapter<ItemRvAdapter.ItemViewHolder> {
    private List<Item> list ;
    public ItemRvAdapter(List<Item> list) {
        this.list = list;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_device_item,viewGroup,false);
        return new ItemViewHolder(view);
    }
    /*
    展示数据
     */
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder viewHolder, int position) {
        viewHolder.mImageView.setBackgroundResource(list.get(position).imageView);
        viewHolder.mText.setText(list.get(position).name);
           //activity调用setOnItemClickListener() 如果调用接口不为空执行下面逻辑
           if (mOnItemClickListener != null) {
               viewHolder.mText.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //返回对应view的信息
                       int pos = viewHolder.getLayoutPosition();
                       mOnItemClickListener.onItemClick(viewHolder.mText,pos);
                   } });
               viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //返回对应view的信息
                       int pos = viewHolder.getLayoutPosition();
                       mOnItemClickListener.onItemClick(viewHolder.mText,pos);
                   }
               });
//               viewHolder.mText.setOnClickListener(this);
//               viewHolder.mImageView.setOnClickListener(this);
               viewHolder.mText.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View view) {
                       int pos = viewHolder.getLayoutPosition();
                       mOnItemClickListener.onItemLongClick(viewHolder.mText,pos);
                       return false;
                   } }); }

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

