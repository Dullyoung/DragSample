package com.dullyoung.fakedingtalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView mRv1, mRv2;
    RecyclerView.RecycledViewPool mRecycledViewPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        setRv();
        setRV2();
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
    }

    private void initView() {
        mRv1 = findViewById(R.id.rv_list);
        mRv2 = findViewById(R.id.rv_list2);
    }


    /**
     * 只实现了从右到左，从左到右同理。
     * 需要设置同一个rvViewPool才能把一个Item给另一个item用。不过我这里直接传输的数据。可以不设置
     * 需要同一个rv拖拽交换item类似微信朋友圈选图的 就把{@link SimpleItemTouchHelperCallback#isLongPressDragEnabled()}
     * 改为return true,但是这时候不能从一个rv到另一个rv 有需求的话自己通过ondragLisitener重新处理一下就好了
     * 体验两个rv从右到左就return false;
     */
    private void setRv() {
        List<DragItemInfo> dragItemInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DragItemInfo dragItemInfo = new DragItemInfo();
            dragItemInfo.setName("");
            dragItemInfos.add(dragItemInfo);
        }
        DragAdapter dragAdapter = new DragAdapter(dragItemInfos);
        mRv1.setLayoutManager(new LinearLayoutManager(this));
        mRv1.setAdapter(dragAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(dragAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRv1);
        mRv1.setRecycledViewPool(mRecycledViewPool);
        mRv1.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.i(TAG, "开始: " + event.getY());
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i(TAG, "进入: " + event.getX() + "---" + event.getY());
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i(TAG, "退出的位置: " + event.getY());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i(TAG, "结束的数据: " + event.getLocalState());
                    int[] location = new int[2];
                    mRv2.getLocationOnScreen(location);
                    Log.i(TAG, "结束的位置: " + event.getX() + "y：" + event.getY());

                    Log.i(TAG, "停留在: " + findRVViewByXY(event.getX(), event.getY() - location[1]));

                    DragItemInfo dragItemInfo = (DragItemInfo) event.getLocalState();

                    //y>0说明还在这个rv里面 出了自身rv x、y都是0
                    if (event.getY() > 0) {
                        mRv2.getAdapter().notifyItemChanged(dragItemInfo.getId());
                    } else {
                        //通过最后落点位置 判断移动到了rv1的哪个item 将数据加进来。
                        int index = findRVViewByXY(endX, endY);
                        Log.i(TAG, "setRv: x" + endX + "___y__" + endY + "____index" + index);
                        DragAdapter dragAdapter1 = (DragAdapter) mRv1.getAdapter();
                        dragAdapter1.getData().set(index, dragItemInfo);
                        dragAdapter1.notifyItemChanged(index);
                        DragAdapter dragAdapter2 = (DragAdapter) mRv2.getAdapter();
                        dragAdapter2.getData().set(dragItemInfo.getId(), new DragItemInfo());
                        dragAdapter2.notifyItemChanged(dragItemInfo.getId());
                    }
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    float x = event.getX();
                    float y = event.getY();
                    endX = x;
                    endY = y;
                    //  Log.i(TAG, "拖拽的view在监听view中的位置:x =" + x + ",y=" + y);
                    break;

            }
            return true;
        });
    }

    //记录drag view在当前view的最后一次位置
    float endX, endY;

    /**
     * @param x x
     * @param y y
     * @return index of item
     * copy from #{@link RecyclerView#findChildViewUnder(float, float)}
     * 因为支持全局拖拽，如果到rv1里面 x就会<0 这里就不管x x<0的时候交给rv1去做处理。
     */
    private int findRVViewByXY(float x, float y) {
        int p = -1;
        for (int i = 0; i < mRv2.getAdapter().getItemCount(); i++) {
            View child = mRv2.getChildAt(i);
            final float translationX = child.getTranslationX();
            final float translationY = child.getTranslationY();
            if (y >= child.getTop() + translationY
                    && y <= child.getBottom() + translationY) {
                return i;
            }
        }
        return p;
    }

    public String TAG = "aaaa";

    private void setRV2() {
        List<DragItemInfo> dragItemInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DragItemInfo dragItemInfo = new DragItemInfo();
            dragItemInfo.setName("嘿嘿嘿嘿" + i);
            dragItemInfos.add(dragItemInfo);
        }
        DragAdapter dragAdapter = new DragAdapter(dragItemInfos);
        mRv2.setLayoutManager(new LinearLayoutManager(this));
        mRv2.setAdapter(dragAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(dragAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRv2);
        mRv2.setRecycledViewPool(mRecycledViewPool);

    }

}