package com.lusen.cardola.framework.uibase.ui.immersive;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.lusen.cardola.framework.uibase.util.UISystemBarConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 16/8/15.
 */
public class ImmersiveHelper {

    private List<View> mFitViewsAtTop = new ArrayList<>();
    private List<View> mFitViewsAtBottom = new ArrayList<>();

    private Activity mActivity;
    private boolean mFromActivity;  // 来源Activity,只允许Activity操作沉浸式状态
    private Integer mCustomImmersivePaddingTop;
    private Integer mCustomImmersivePaddingBottom;

    private boolean mInited = false;

    public ImmersiveHelper(Activity activity, boolean fromActivity) {
        mActivity = activity;
        mFromActivity = fromActivity;
    }

    /**
     * 设置沉浸式视图
     *
     * @param fitViewAtTop    top沉浸式视图
     * @param fitViewAtBottom bottom沉浸式视图
     */
    public void setImmersiveFitView(View fitViewAtTop, View fitViewAtBottom) {
        List<View> fitViewsAtTop = new ArrayList<>();
        if (null != fitViewAtTop) {
            fitViewsAtTop.add(fitViewAtTop);
        }
        List<View> fitViewsAtBottom = new ArrayList<>();
        if (null != fitViewAtBottom) {
            fitViewsAtBottom.add(fitViewAtBottom);
        }
        setImmersiveFitView(fitViewsAtTop, fitViewsAtBottom);
    }

    /**
     * 设置沉浸式视图
     *
     * @param fitViewsAtTop    top沉浸式视图列表
     * @param fitViewsAtBottom bottom沉浸式视图列表
     */
    public void setImmersiveFitView(List<View> fitViewsAtTop, List<View> fitViewsAtBottom) {
        mFitViewsAtTop.clear();
        mFitViewsAtBottom.clear();
        if (null != fitViewsAtTop) {
            mFitViewsAtTop.addAll(fitViewsAtTop);
        }
        if (null != fitViewsAtBottom) {
            mFitViewsAtBottom.addAll(fitViewsAtBottom);
        }
    }

    /**
     * 更新沉浸式（提供给Fragment依据沉浸式更新视图）
     *
     * @param immersiveStatusBarCustom 自定义状态栏视图FitView是否沉浸,null则表示跟随宿主设置（Activity）
     * @param immersiveNavBarCustom    自定义导航栏视图FitView是否沉浸,null则表示跟随宿主设置（Activity）
     */
    public void updateImmersive(Boolean immersiveStatusBarCustom, Boolean immersiveNavBarCustom) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            boolean immersiveStatusBar = (null != immersiveStatusBarCustom ? immersiveStatusBarCustom : isStatusBarImmersive());
            boolean immersiveNavBar = (null != immersiveNavBarCustom ? immersiveNavBarCustom : isNavBarImmersive());
            final UISystemBarConfig uiSystemBarConfig = new UISystemBarConfig(mActivity, immersiveStatusBar, immersiveNavBar);
            if (!uiSystemBarConfig.hasNavigtionBar()) {
                immersiveNavBar = false;
            }
            final boolean immersiveStatusBarResult = immersiveStatusBar;
            final boolean immersiveNavBarResult = immersiveNavBar;
            mActivity.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    // 状态栏:沉浸式(修正padding值)
                    if (immersiveStatusBarResult) {
                        int paddingTop;
                        if (null != mCustomImmersivePaddingTop && mCustomImmersivePaddingTop > 0) {
                            paddingTop = mCustomImmersivePaddingTop;
                        } else {
                            paddingTop = uiSystemBarConfig.getPixelInsetTop(false);
                        }
                        for (View view : mFitViewsAtTop) {
                            if (null != view) {
                                // 已沉浸,则忽略
                                if (ImmersiveMode.isViewImmersiveStatusBar(view)) {
                                    continue;
                                }
                                // 标识沉浸
                                ImmersiveMode.updateViewImmersiveStatusBar(view, true);
                                int height = view.getMeasuredHeight();
                                ViewGroup.LayoutParams lp = view.getLayoutParams();
                                lp.height = height + paddingTop;
                                view.setLayoutParams(lp);
                                // 需要Padding
                                if (!ImmersiveMode.isViewImmersiveNoPadding(view)) {
                                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + paddingTop, view.getPaddingRight(), view.getPaddingBottom());
                                }
                            }
                        }
                    }
                    // 导航栏:沉浸式(修正padding值)
                    if (immersiveNavBarResult) {
                        int paddingBottom;
                        if (null != mCustomImmersivePaddingBottom && mCustomImmersivePaddingBottom > 0) {
                            paddingBottom = mCustomImmersivePaddingBottom;
                        } else {
                            paddingBottom = uiSystemBarConfig.getPixelInsetBottom();
                        }
                        for (View view : mFitViewsAtBottom) {
                            if (null != view) {
                                // 已沉浸,则忽略
                                if (ImmersiveMode.isViewImmersiveNavBar(view)) {
                                    continue;
                                }
                                // 标识沉浸
                                ImmersiveMode.updateViewImmersiveNavBar(view, true);
                                int height = view.getMeasuredHeight();
                                ViewGroup.LayoutParams lp = view.getLayoutParams();
                                lp.height = height + paddingBottom;
                                view.setLayoutParams(lp);
                                // 需要Padding
                                if (!ImmersiveMode.isViewImmersiveNoPadding(view)) {
                                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom() + paddingBottom);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 初始化沉浸式（仅Activity调用有效）
     *
     * @param immersiveStatusBar 状态栏沉浸
     * @param immersiveNavBar    导航栏沉浸
     */
    public void initImmersive(boolean immersiveStatusBar, boolean immersiveNavBar) {
        ImmersiveMode immersiveMode = new ImmersiveMode();
        immersiveMode.mImmersiveStatusBar = immersiveStatusBar;
        immersiveMode.mImmersiveNavBar = immersiveNavBar;
        initImmersive(immersiveMode);
    }

    /**
     * 初始化沉浸式（仅Activity调用有效）
     *
     * @param immersiveMode 沉浸模式
     */
    public void initImmersive(ImmersiveMode immersiveMode) {
        if (null != immersiveMode) {
            // 非Activity操作
            if (!mFromActivity) {
                return;
            }
            // 目前inited限制只能初始化一次沉浸式模式,后续待需求扩展可动态切换沉浸式模式(半沉浸、全沉浸、正常)
            if (mInited) {
                return;
            }
            mInited = true;
            boolean immersiveStatusBar = immersiveMode.mImmersiveStatusBar;
            boolean immersiveNavBar = immersiveMode.mImmersiveNavBar;
            boolean immersiveFeatureForV21 = immersiveMode.mImmersiveFeatureForV21;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final UISystemBarConfig uiSystemBarConfig = new UISystemBarConfig(mActivity, immersiveStatusBar, immersiveNavBar);
                if (!uiSystemBarConfig.hasNavigtionBar()) {
                    immersiveNavBar = false;
                }
                // 设置标志位
                setTranslucentStatus(immersiveStatusBar, immersiveNavBar, immersiveFeatureForV21);
                // 动态设置padding及height
                final boolean immersiveStatusBarResult = immersiveStatusBar;
                final boolean immersiveNavBarResult = immersiveNavBar;
                mActivity.getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        // 状态栏:沉浸式(修正padding值)
                        if (immersiveStatusBarResult) {
                            int paddingTop;
                            if (null != mCustomImmersivePaddingTop && mCustomImmersivePaddingTop > 0) {
                                paddingTop = mCustomImmersivePaddingTop;
                            } else {
                                paddingTop = uiSystemBarConfig.getPixelInsetTop(false);
                            }
                            for (View view : mFitViewsAtTop) {
                                if (null != view) {
                                    // 已沉浸,则忽略
                                    if (ImmersiveMode.isViewImmersiveStatusBar(view)) {
                                        continue;
                                    }
                                    // 标识沉浸
                                    ImmersiveMode.updateViewImmersiveStatusBar(view, true);
                                    int height = view.getMeasuredHeight();
                                    ViewGroup.LayoutParams lp = view.getLayoutParams();
                                    lp.height = height + paddingTop;
                                    view.setLayoutParams(lp);
                                    // 需要Padding
                                    if (!ImmersiveMode.isViewImmersiveNoPadding(view)) {
                                        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + paddingTop, view.getPaddingRight(), view.getPaddingBottom());
                                    }
                                }
                            }
                        }
                        // 导航栏:沉浸式(修正padding值)
                        if (immersiveNavBarResult) {
                            int paddingBottom;
                            if (null != mCustomImmersivePaddingBottom && mCustomImmersivePaddingBottom > 0) {
                                paddingBottom = mCustomImmersivePaddingBottom;
                            } else {
                                paddingBottom = uiSystemBarConfig.getPixelInsetBottom();
                            }
                            for (View view : mFitViewsAtBottom) {
                                if (null != view) {
                                    // 已沉浸,则忽略
                                    if (ImmersiveMode.isViewImmersiveNavBar(view)) {
                                        continue;
                                    }
                                    // 标识沉浸
                                    ImmersiveMode.updateViewImmersiveNavBar(view, true);
                                    int height = view.getMeasuredHeight();
                                    ViewGroup.LayoutParams lp = view.getLayoutParams();
                                    lp.height = height + paddingBottom;
                                    view.setLayoutParams(lp);
                                    // 需要Padding
                                    if (!ImmersiveMode.isViewImmersiveNoPadding(view)) {
                                        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom() + paddingBottom);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 设置自定义ImmersivePadding值,用于特定业务场景需求
     * 值不为null且大于0才有效
     *
     * @param immersivePaddingTop    顶部padding
     * @param immersivePaddingBottom 底部padding
     */
    public void setCustomImmersivePadding(Integer immersivePaddingTop, Integer immersivePaddingBottom) {
        mCustomImmersivePaddingTop = immersivePaddingTop;
        mCustomImmersivePaddingBottom = immersivePaddingBottom;
    }

    /**
     * 设置沉浸透明
     *
     * @param immersiveStatusBar     状态栏沉浸
     * @param immersiveNavBar        导航栏沉浸
     * @param immersiveFeatureForV21 是否采用V21系统半透明特性
     */
    private void setTranslucentStatus(boolean immersiveStatusBar, boolean immersiveNavBar, boolean immersiveFeatureForV21) {
        Window window = mActivity.getWindow();
        // 状态栏-透明
        if (immersiveStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 导航栏-透明
        if (immersiveNavBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 状态栏是否沉浸
     *
     * @return 是否沉浸
     */
    private boolean isStatusBarImmersive() {
        return isStatusBarImmersive(mActivity);
    }

    /**
     * 导航栏是否沉浸
     *
     * @return 是否沉浸
     */
    private boolean isNavBarImmersive() {
        return isNavBarImmersive(mActivity);
    }

    /**
     * 状态栏是否沉浸
     *
     * @param activity activity对象
     * @return 是否沉浸
     */
    public static boolean isStatusBarImmersive(Activity activity) {
        if (null != activity) {
            return isStatusBarImmersive(activity.getWindow());
        }
        return false;
    }

    /**
     * 状态栏是否沉浸
     *
     * @param window window对象
     * @return 是否沉浸
     */
    public static boolean isStatusBarImmersive(Window window) {
        if (null != window && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = window.getAttributes().flags;
            boolean hasFlagTranslucentStatus = (flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            boolean hasFlagDrawsSystemBarBackgrounds = (flags & WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) == WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            if (hasFlagTranslucentStatus || hasFlagDrawsSystemBarBackgrounds) {
                return true;
            }
        }
        return false;
    }

    /**
     * 导航栏是否沉浸
     *
     * @param activity activity对象
     * @return 是否沉浸
     */
    public static boolean isNavBarImmersive(Activity activity) {
        if (null != activity) {
            return isNavBarImmersive(activity.getWindow());
        }
        return false;
    }

    /**
     * 导航栏是否沉浸
     *
     * @param window window对象
     * @return 是否沉浸
     */
    public static boolean isNavBarImmersive(Window window) {
        if (null != window && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = window.getAttributes().flags;
            if ((flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) {
                return true;
            }
        }
        return false;
    }

}
