package com.archeanx.libx.tv.util.move;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.archeanx.libx.tv.util.R;
import com.archeanx.libx.tv.util.tv.FocusAnimationUtil;


/**
 * @创建者 xz
 * @创建时间
 * @描述 焦点移动工具类
 */
public class FocusMoveToViewUtil {

    public static View initFocusView(Activity activity) {
       return initFocusView(activity, R.drawable.ico_focus_border_fillet);
    }

    /**
     * 初始化 焦点框view
     *
     * @param activity
     * @param focusImg
     */
    public static View initFocusView(Activity activity, @DrawableRes int focusImg) {
        //设置view的焦点被选中监听
        final View focusView = new View(activity);
        focusView.setBackgroundResource(focusImg);
        ((ViewGroup) activity.getWindow().getDecorView()).addView(focusView);
        return focusView;
    }


    public static void initNoTabActivity(Activity activity) {
        initNoTabActivity(activity, 0);
    }


    /**
     * 在activity中初始化
     */
    public static void initNoTabActivity(Activity activity, @DrawableRes int focusImg) {
        final View focusView = initFocusView(activity, focusImg);
        activity.getWindow().getDecorView().getViewTreeObserver().
                addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {

                    @Override
                    public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                        if (oldFocus != null) {
                            FocusMoveToViewHelper.getInstance().setOldFocusView(oldFocus);
                            if (FocusMoveToViewHelper.getInstance().getMoveAnimViews().get(oldFocus) == null
                                    && FocusMoveToViewHelper.getInstance().getNoAnimViews().get(oldFocus) == null) {
                                //移除变大效果
                                FocusAnimationUtil.setViewAnimatorBig(oldFocus, false, 300, 1.1f);
                            }
                        }

                        if (newFocus == null) {
                            return;
                        }
                        FocusMoveToViewHelper.getInstance().setNewFocusView(newFocus);

                        if (FocusMoveToViewHelper.getInstance().getNoAnimViews().get(newFocus) != null) {
                            focusView.setVisibility(View.GONE);
                            return;
                        }
                        if (FocusMoveToViewHelper.getInstance().getBigAnimViews().get(newFocus) != null) {
                            //只能放大  焦点过去后，先放大view，然后焦点框渐变显示
                            newFocus.bringToFront();
                            FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, 1.1f);
                            FocusAnimationUtil.focusAlphaAnimator(newFocus, focusView, 1.1f);
                        } else if (FocusMoveToViewHelper.getInstance().getMoveAnimViews().get(newFocus) != null) {
                            //只能移动 没有放大效果，焦点框移动
                            FocusAnimationUtil.focusMoveAnimator(newFocus, focusView, -1, 43, 43);
                        } else if (oldFocus != null && FocusMoveToViewHelper.getInstance().getFragParentViews().get(oldFocus) != null) {
                            //如果oldFocus是fragment 的最大Viewgroup,//那么下一个view 则执行 渐变，放大动画
                            newFocus.bringToFront();
                            FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, 1.1f);
                            FocusAnimationUtil.focusAlphaAnimator(newFocus, focusView, 1.1f);
                        } else {
                            //放大和移动
                            newFocus.bringToFront();
                            FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, 1.1f);
                            FocusAnimationUtil.focusMoveAnimatorBig(newFocus, focusView, 1.1f);
                        }
                    }

                });
    }


    /**
     * 没有tab栏的tv fragment 界面
     *
     * @param activity         fragment 对应activity
     * @param parentView       fragment最大的Viewgroup
     * @param defaultFocusView 当焦点失去响应的时候，默认到这个焦点上
     */
    public static void initNoTabFragmentParentLayout(@NonNull final Activity activity, final ViewGroup parentView, final View defaultFocusView) {
        //标识fragment最大的布局的id
        FocusMoveToViewHelper.getInstance().addFragParentViewIds(parentView, parentView);
        //fragemnt最大的布局不应该有动画效果
        FocusMoveToViewHelper.getInstance().addNoAnimViews(parentView,parentView);
        //会优先其子类控件而获取到焦点
        parentView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        //设置焦点
        parentView.setFocusable(true);
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //禁用父布局所有键位事件，然后把焦点传递给 目标View，手动控制焦点移动
                return true;
            }
        });

        parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (FocusMoveToViewHelper.getInstance().getCorrespondingView() != null) {
                        final View targetView = FocusMoveToViewHelper.getInstance().getCorrespondingView();
                        if (targetView != null) {
                            parentView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    targetView.requestFocus();
                                }
                            }, 300);
                        } else {
                            parentView.post(new Runnable() {
                                @Override
                                public void run() {
                                    defaultFocusView.requestFocus();
                                }
                            });
                        }
                    } else {
                        parentView.post(new Runnable() {
                            @Override
                            public void run() {
                                defaultFocusView.requestFocus();
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * @param activity           fragment的activity
     * @param parentView         fragment中最大布局view
     * @param tabNextFcousView tab栏下来时，需要有焦点的view
     */
    public static void initTabFragmentParentLayout(@NonNull final Activity activity, final ViewGroup parentView, final View tabNextFcousView, final View defaultFocusView) {
        //标识fragment最大的布局的id
        FocusMoveToViewHelper.getInstance().addFragParentViewIds(parentView, parentView);
        //fragemnt最大的布局不应该有动画效果
        FocusMoveToViewHelper.getInstance().addNoAnimViews(parentView,parentView);
        //会优先其子类控件而获取到焦点
        parentView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        //设置焦点
        parentView.setFocusable(true);
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //禁用父布局所有键位事件，然后把焦点传递给 目标View，手动控制焦点移动
                return true;
            }
        });

        parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (tabNextFcousView != null && isOldViewToTab(activity)) {
                        tabNextFcousView.requestFocus();
                    } else if (FocusMoveToViewHelper.getInstance().getCorrespondingView() != null) {
                        final View targetView = FocusMoveToViewHelper.getInstance().getCorrespondingView();
                        if (targetView != null) {
                            parentView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    targetView.requestFocus();
                                }
                            }, 300);

                        } else {
                            parentView.post(new Runnable() {
                                @Override
                                public void run() {
                                    defaultFocusView.requestFocus();
                                }
                            });
                        }
                    } else {
                        parentView.post(new Runnable() {
                            @Override
                            public void run() {
                                defaultFocusView.requestFocus();
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * 判断 旧的焦点view的父view 是不是tab栏的
     */
    private static boolean isOldViewToTab(Activity activity) {
        if (FocusMoveToViewHelper.getInstance().getTabLayoutView() == null) {
            return false;
        }
        if (FocusMoveToViewHelper.getInstance().getOldFocusView() == null) {
            return false;
        }
        View tabLayout = FocusMoveToViewHelper.getInstance().getTabLayoutView();
        View oldFocusView = FocusMoveToViewHelper.getInstance().getOldFocusView();
        if (tabLayout == null) {
            return false;
        }
        if (oldFocusView == null) {
            return false;
        }
        if (oldFocusView.getParent() == null) {
            return false;
        }
        if (tabLayout == oldFocusView.getParent()) {
            return true;
        } else {
            return false;
        }
    }
}
