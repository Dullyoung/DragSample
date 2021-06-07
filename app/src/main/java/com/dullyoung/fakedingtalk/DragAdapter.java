package com.dullyoung.fakedingtalk;

import android.content.ClipData;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/*
 *  Created  in  2021/6/7
 */
public class DragAdapter extends RecyclerView.Adapter<DragAdapter.DragHolder> implements PicItemTouchHelper {

    List<DragItemInfo> mDragItemInfos;

    public List<DragItemInfo> getData() {
        return mDragItemInfos;
    }

    public DragAdapter(@NonNull List<DragItemInfo> dragItemInfos) {
        mDragItemInfos = dragItemInfos;
    }

    @NonNull
    @Override
    public DragHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drag, parent, false);
        return new DragHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DragAdapter.DragHolder holder, int position) {
        if (position % 2 == 0) {
            holder.mTextView.setBackgroundColor(0xffff9b27);
        } else {
            holder.mTextView.setBackgroundColor(0xffe4e5e9);
        }
        getData().get(position).setId(position);
        holder.mRootCl.setVisibility(View.VISIBLE);
        holder.mTextView.setText(getData().get(position).getName());
        holder.mRootCl.setOnLongClickListener(v -> {
            if (TextUtils.isEmpty(getData().get(position).getName())) {
                return false;
            }
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            holder.mRootCl.setVisibility(View.INVISIBLE);
            holder.mRootCl.startDrag(null, shadowBuilder, getData().get(position), 0);
            return true;
        });
    }


    public String TAG = "aaaa";

    @Override
    public int getItemCount() {
        return mDragItemInfos.size();
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();

        int toPosition = target.getAdapterPosition();
        if (fromPosition < getData().size() && toPosition < getData().size()) {
            //交换数据位置
            Collections.swap(getData(), fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);
    }

    @Override
    public void onItemDelete(RecyclerView.ViewHolder source) {
        int position = source.getAdapterPosition();
        getData().remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder source) {
        int fromPosition = source.getAdapterPosition();


        //当拖拽选中时放大选中的view
        source.itemView.setScaleX(1.1f);
        source.itemView.setScaleY(1.1f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder source) {
        //拖拽结束后恢复view的状态
        source.itemView.setScaleX(1.0f);
        source.itemView.setScaleY(1.0f);
    }

    public static class DragHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mRootCl;
        TextView mTextView;

        public DragHolder(@NonNull View itemView) {
            super(itemView);
            mRootCl = itemView.findViewById(R.id.cl_root);
            mTextView = itemView.findViewById(R.id.tv_text);
        }
    }

}
