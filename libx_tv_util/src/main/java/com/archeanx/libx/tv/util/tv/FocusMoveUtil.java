package com.archeanx.libx.tv.util.tv;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.archeanx.libx.tv.util.R;


/**
 * @创建者 xz
 * @创建时间
 * @描述 焦点移动工具类
 */
public class FocusMoveUtil {

    public static View initFocusView(Activity activity) {
        return initFocusView(activity, 0);
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
        if (focusImg == 0) {
            focusImg = R.drawable.ico_focus_border_fillet;
        }
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
                            FocusMoveHelper.getInstance().setOldFocusViewId(oldFocus.getId());
                            if (FocusMoveHelper.getInstance().getMoveAnimViewIds().get(oldFocus.getId()) == null && FocusMoveHelper.getInstance().getNoAnimViewIds().get(oldFocus.getId()) == null) {
                                //移除变大效果
                                FocusAnimationUtil.setViewAnimatorBig(oldFocus, false, 300, 1.1f);
                            }
                        }

                        if (newFocus == null) {
                            return;
                        }
                        FocusMoveHelper.getInstance().setNewFocusViewId(newFocus.getId());

                        if (FocusMoveHelper.getInstance().getNoAnimViewIds().get(newFocus.getId()) != null) {
                            focusView.setVisibility(View.GONE);
                            return;
                        }
                        if (FocusMoveHelper.getInstance().getBigAnimViewIds().get(newFocus.getId()) != null) {
                            //只能放大  焦点过去后，先放大view，然后焦点框渐变显示
                            newFocus.bringToFront();
                            FocusAnimationUtil.setViewAnimatorBig(newFocus, true, 300, 1.1f);
                            FocusAnimationUtil.focusAlphaAnimator(newFocus, focusView, 1.1f);
                        } else if (FocusMoveHelper.getInstance().getMoveAnimViewIds().get(newFocus.getId()) != null) {
                            //只能移动 没有放大效果，焦点框移动
                            FocusAnimationUtil.focusMoveAnimator(newFocus, focusView, -1, 43, 43);
                        } else if (oldFocus != null && FocusMoveHelper.getInstance().getFragParentViewIds().get(oldFocus.getId()) != null) {
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
     * 没有tab栏的tv界面
     *
     * @param activity         fragment 对应activity
     * @param parentView       fragment最大的Viewgroup
     * @param defaultFocusView 当焦点失去响应的时候，默认到这个焦点上
     */
    public static void initNoTabFragmentParentLayout(@NonNull final Activity activity, final ViewGroup parentView, final View defaultFocusView) {
        //标识fragment最大的布局的id
        FocusMoveHelper.getInstance().addFragParentViewIds(parentView.getId(), parentView.getId());
        //fragemnt最大的布局不应该有动画效果
        FocusMoveHelper.getInstance().addNoAnimViews(parentView.getId(), parentView.getId());
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
                    if (FocusMoveHelper.getInstance().getCorrespondingView() != null) {
                        final View targetView = activity.findViewById(FocusMoveHelper.getInstance().getCorrespondingView());
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
     * @param tabNextFcousViewId tab栏下来时，需要有焦点的view
     */
    public static void initTabFragmentParentLayout(@NonNull final Activity activity, final ViewGroup parentView, final int tabNextFcousViewId, final View defaultFocusView) {
        //标识fragment最大的布局的id
        FocusMoveHelper.getInstance().addFragParentViewIds(parentView.getId(), parentView.getId());
        //fragemnt最大的布局不应该有动画效果
        FocusMoveHelper.getInstance().addNoAnimViews(parentView.getId(), parentView.getId());
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
                    if (tabNextFcousViewId != View.NO_ID && isOldViewToTab(activity)) {
                        activity.findViewById(tabNextFcousViewId).requestFocus();
                    } else if (FocusMoveHelper.getInstance().getCorrespondingView() != null) {
                        final View targetView = activity.findViewById(FocusMoveHelper.getInstance().getCorrespondingView());
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
        if (FocusMoveHelper.getInstance().getTabLayoutViewId() == View.NO_ID) {
            return false;
        }
        if (FocusMoveHelper.getInstance().getOldFocusViewId() == View.NO_ID) {
            return false;
        }
        View tabLayout = activity.findViewById(FocusMoveHelper.getInstance().getTabLayoutViewId());
        View oldFocusView = activity.findViewById(FocusMoveHelper.getInstance().getOldFocusViewId());
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


    /**
     * activity 的子view 只有放大动画
     *
     * @param activity act
     */
    public static void initActivityBigFocusView(Activity activity) {
        initActivityBigFocusView(activity, 300, 1.1f);
    }

    /**
     * activity 的子view 只有放大动画
     *
     * @param activity  act
     * @param duration  动画时间
     * @param bigNumber 动画放大系数
     */
    public static void initActivityBigFocusView(Activity activity, final int duration, final float bigNumber) {
        activity.getWindow().getDecorView().getViewTreeObserver().
                addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {

                    @Override
                    public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                        if (oldFocus != null) {
                            //移除变大效果
                            FocusAnimationUtil.setViewAnimatorBig(oldFocus, false, duration, bigNumber);
                        }

                        if (newFocus == null) {
                            return;
                        }
                        //放大和移动
                        newFocus.bringToFront();
                        FocusAnimationUtil.setViewAnimatorBig(newFocus, true, duration, bigNumber);
                    }

                });
    }
}
