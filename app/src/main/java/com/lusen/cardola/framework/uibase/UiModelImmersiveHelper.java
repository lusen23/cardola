package com.lusen.cardola.framework.uibase;

import android.app.Activity;
import android.view.View;

import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveHelper;
import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveMode;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.util.List;

/**
 * Created by leo on 16/8/15.
 * 沉浸式UiModel帮助类
 */
public class UiModelImmersiveHelper {

    private boolean mEnable = false;
    private ImmersiveHelper mImmersiveHelper;

    private static Integer mGlobalImmersivePaddingTop;
    private static Integer mGlobalImmersivePaddingBottom;

    private boolean mInited = false;

    public UiModelImmersiveHelper(boolean enable) {
        mEnable = enable;
    }

    public void injectActivity(Activity activity, boolean fromActivity) {
        if (mEnable) {
            mImmersiveHelper = new ImmersiveHelper(activity, fromActivity);
            // 设置全局沉浸式padding值
            mImmersiveHelper.setCustomImmersivePadding(mGlobalImmersivePaddingTop, mGlobalImmersivePaddingBottom);
            mInited = true;
        }
    }

    /**
     * 设置沉浸式视图
     *
     * @param fitViewsAtTop    top沉浸式视图列表
     * @param fitViewsAtBottom bottom沉浸式视图列表
     */
    public void setImmersiveView(List<View> fitViewsAtTop, List<View> fitViewsAtBottom) {
        if (mEnable && checkInitPermission("setImmersiveView")) {
            mImmersiveHelper.setImmersiveFitView(fitViewsAtTop, fitViewsAtBottom);
        }
    }

    /**
     * 设置沉浸式视图
     *
     * @param fitViewAtTop    top沉浸式视图
     * @param fitViewAtBottom bottom沉浸式视图
     */
    public void setImmersiveView(View fitViewAtTop, View fitViewAtBottom) {
        if (mEnable && checkInitPermission("setImmersiveView")) {
            mImmersiveHelper.setImmersiveFitView(fitViewAtTop, fitViewAtBottom);
        }
    }

    /**
     * 更新沉浸式（提供给Fragment依据沉浸式更新视图）
     */
    public void updateImmersive() {
        if (mEnable && checkInitPermission("updateImmersive:follow")) {
            mImmersiveHelper.updateImmersive(null, null);
        }
    }

    /**
     * 更新沉浸式（提供给Fragment依据沉浸式更新视图）
     *
     * @param immersiveStatusBarCustom 自定义状态栏视图FitView是否沉浸,null则表示跟随宿主设置（Activity）
     * @param immersiveNavBarCustom    自定义导航栏视图FitView是否沉浸,null则表示跟随宿主设置（Activity）
     */
    public void updateImmersive(Boolean immersiveStatusBarCustom, Boolean immersiveNavBarCustom) {
        if (mEnable && checkInitPermission("updateImmersive:custom")) {
            mImmersiveHelper.updateImmersive(immersiveStatusBarCustom, immersiveNavBarCustom);
        }
    }

    /**
     * 初始化沉浸式
     *
     * @param immersiveMode 沉浸模式
     */
    public void initImmersive(ImmersiveMode immersiveMode) {
        if (mEnable && checkInitPermission("initImmersive")) {
            mImmersiveHelper.initImmersive(immersiveMode);
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
        if (mEnable && checkInitPermission("setCustomImmersivePadding")) {
            mImmersiveHelper.setCustomImmersivePadding(immersivePaddingTop, immersivePaddingBottom);
        }
    }

    /**
     * 设置全局ImmersivePadding值,用于特定业务场景需求
     * 值不为null且大于0才有效
     *
     * @param immersivePaddingTop    顶部padding
     * @param immersivePaddingBottom 底部padding
     */
    public static void setGlobalImmersivePadding(Integer immersivePaddingTop, Integer immersivePaddingBottom) {
        mGlobalImmersivePaddingTop = immersivePaddingTop;
        mGlobalImmersivePaddingBottom = immersivePaddingBottom;
    }

    private boolean checkInitPermission(String originCall) {
        if (!mInited) {
            UIBaseUtil.log(UiModelImmersiveHelper.class.getSimpleName() + "##" + originCall + ">> must be called after inited");
            return false;
        }
        return true;
    }

}
