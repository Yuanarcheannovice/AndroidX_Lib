package com.archeanx.libx.tv.util.move;

import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * 焦点移动帮助类  基于view的绑定
 */
public class FocusMoveToViewHelper {
    private FocusMoveToViewHelper() {

    }

    private static FocusMoveToViewHelper instance;


    public static synchronized FocusMoveToViewHelper getInstance() {
        if (instance == null) {
            instance = new FocusMoveToViewHelper();
        }
        return instance;
    }

    /**
     * 不需要动画效果的view
     */
    private ArrayMap<View, View> mNoAnimViews = new ArrayMap<>();

    /**
     * 只有放大效果的view
     */
    private ArrayMap<View, View> mBigAnimViews = new ArrayMap<>();

    /**
     * 只移动效果的view
     */
    private ArrayMap<View, View> mMoveAnimViews = new ArrayMap<>();

    /**
     * fragment 最大的 viewGroup
     */
    private ArrayMap<View, View> mFragParentViews = new ArrayMap<>();

    /**
     * 绑定View的跳转
     */
    private ArrayMap<View, View> mAppointNextView = new ArrayMap<>();

    /**
     * 旧的焦点view
     */
    private View mOldFocusView = null;

    /**
     * 新的焦点view
     */
    private View mNewFocusView = null;

    /**
     * Tab栏 父布局view
     */
    private View mTabLayoutView = null;

    /**
     * viewpager的页码
     */
    private int mViewpageIndex = 0;

    /**
     * 被选中的tab栏id
     */
    private View mSelectTabView = null;


    public void release() {
        mNoAnimViews.clear();
        mBigAnimViews.clear();
        mMoveAnimViews.clear();
        mFragParentViews.clear();
        mAppointNextView.clear();
        mOldFocusView=null;
        mNewFocusView=null;
        mTabLayoutView=null;
        mSelectTabView=null;
    }


    public View getSelectTabViewId() {
        return mSelectTabView;
    }

    public void setSelectTabView(View selectTabView) {
        mSelectTabView = selectTabView;
    }

    public ArrayMap<View, View> getFragParentViews() {
        return mFragParentViews;
    }

    public void addFragParentViewIds(@NonNull View key, @NonNull View value) {
        mFragParentViews.put(key, value);
    }

    public int getViewpageIndex() {
        return mViewpageIndex;
    }

    public void setViewpageIndex(int viewpageIndex) {
        mViewpageIndex = viewpageIndex;
    }


    public View getOldFocusView() {
        return mOldFocusView;
    }

    public void setOldFocusView(View oldFocusView) {
        mOldFocusView = oldFocusView;
    }

    public View getNewFocusView() {
        return mNewFocusView;
    }

    public void setNewFocusView(View newFocusView) {
        mNewFocusView = newFocusView;
    }

    public View getTabLayoutView() {
        return mTabLayoutView;
    }

    /**
     * 设置 tab 栏 viewGroup
     */
    public void setTabLayoutView(View tabLayoutView) {
        mTabLayoutView = tabLayoutView;
    }

    public ArrayMap<View, View> getAppointNextView() {
        return mAppointNextView;
    }


    /**
     * @return 根据旧的view 获取下一个需要获取焦点的view
     */
    public View getCorrespondingView() {
        return mAppointNextView.get(mOldFocusView);
    }

    /**
     * 绑定vie的跳转
     *
     * @param oldView 旧的view
     * @param newView 新的view
     */
    public void addAppointNextView(@NonNull View oldView, @NonNull View newView) {
        mAppointNextView.put(oldView, newView);
    }

    public ArrayMap<View, View> getNoAnimViews() {
        return mNoAnimViews;
    }

    public void addNoAnimViews(@NonNull View key, @NonNull View value) {
        mNoAnimViews.put(key, value);
    }


    public ArrayMap<View, View> getBigAnimViews() {
        return mBigAnimViews;
    }

    public void addBigAnimViews(@NonNull View key, @NonNull View value) {
        mBigAnimViews.put(key, value);
    }

    public ArrayMap<View, View> getMoveAnimViews() {
        return mMoveAnimViews;
    }

    public void addMoveAnimViews(@NonNull View key, @NonNull View value) {
        mMoveAnimViews.put(key, value);
    }
}
