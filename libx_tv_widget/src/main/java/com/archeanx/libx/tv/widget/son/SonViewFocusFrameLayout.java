package com.archeanx.libx.tv.widget.son;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by DEV on 2018/5/8.
 */

public class SonViewFocusFrameLayout extends FrameLayout {
    public SonViewFocusFrameLayout(Context context) {
        super(context);
    }

    public SonViewFocusFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SonViewFocusFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SonViewFocusFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childAt.setFocusable(gainFocus);
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
       // setFocusable(gainFocus);
    }
}
