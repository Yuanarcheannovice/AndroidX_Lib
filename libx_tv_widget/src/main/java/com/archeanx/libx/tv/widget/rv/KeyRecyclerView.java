package com.archeanx.libx.tv.widget.rv;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by DEV on 2018/6/4.
 */

public class KeyRecyclerView extends RecyclerView {
    /**
     * 当前选中rv
     */
    private int mSelectedPosition = 0;
    /**
     * 是否启用子view变大
     */
    private boolean isEnableShowBigItem = false;

    public KeyRecyclerView(Context context) {
        super(context);
    }

    public KeyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }



    /**
     * 是否启用子view变大效果
     * 如果没有覆盖到其他view的时候，不要用这个方法，损耗性能
     *
     * @param isEnable true 启用
     */
    public void setEnableShowBigItem(boolean isEnable) {
        isEnableShowBigItem = isEnable;
        //启用子视图排序功能
        setChildrenDrawingOrderEnabled(isEnable);
    }


    @Override
    public void onDraw(Canvas c) {
        if (isEnableShowBigItem) {
//            mSelectedPosition = getChildAdapterPosition(getFocusedChild());
            mSelectedPosition = indexOfChild(getFocusedChild());
        }
        super.onDraw(c);
    }


    /**
     * 重新生成Rv的Item层级
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int position = mSelectedPosition;
        if (isEnableShowBigItem) {
            if (position < 0) {
                return i;
            } else {
                if (i == childCount - 1) {
                    if (position > i) {
                        position = i;
                    }
                    return position;
                }
                if (i == position) {
                    return childCount - 1;
                }
            }
            return i;
        } else {
            return super.getChildDrawingOrder(childCount, i);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (dispatchKeyEventListener != null) {
            if (dispatchKeyEventListener.dispatchKeyEvent(event, event.getAction(), event.getKeyCode())) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    //////
    private DispatchKeyEventListener dispatchKeyEventListener;

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    public interface DispatchKeyEventListener {
        boolean dispatchKeyEvent(KeyEvent event, int keyAction, int keyCode);
    }
}
