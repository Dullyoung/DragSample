package com.dullyoung.fakedingtalk;

import androidx.recyclerview.widget.RecyclerView;

/*
 *  Created  in  2021/6/7
 */
public interface PicItemTouchHelper {

    //数据交换
    void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

    //数据删除
    void onItemDelete(RecyclerView.ViewHolder source);

    //drag或者swipe选中
    void onItemSelect(RecyclerView.ViewHolder source);

    //状态清除
    void onItemClear(RecyclerView.ViewHolder source);
}

