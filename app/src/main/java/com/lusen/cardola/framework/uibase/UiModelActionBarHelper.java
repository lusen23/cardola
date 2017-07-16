package com.lusen.cardola.framework.uibase;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarLayout;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/8/15.
 * 操作栏UiModel帮助类
 */
public class UiModelActionBarHelper {

    private static final long ACTION_BAR_MEASURE_TIME = 1000;

    private boolean mEnable = false;
    private ActionBarHelper mActionBarHelper;

    private boolean isInit = false;

    public UiModelActionBarHelper(boolean enable) {
        mEnable = enable;
    }

    public View injectView(View contentView, ActionBarHelper.ActionBarUI actionBarUI, ActionBarHelper.ActionBarMode actionBarMode) {
        if (mEnable) {
            mActionBarHelper = new ActionBarHelper();
            contentView = mActionBarHelper.injectView(contentView, actionBarUI, actionBarMode);
            isInit = true;
        }
        return contentView;
    }

    public void setActionBarMode(ActionBarHelper.ActionBarMode mode, boolean autoShowActionBar) {
        if (mEnable && checkInitPermission("setActionBarMode")) {
            mActionBarHelper.setActionBarMode(mode, autoShowActionBar);
        }
    }

    public void setActionBarAlignStyle(ActionBarLayout.ActionAlignStyle actionAlignStyle, boolean updateActionBar) {
        if (mEnable && checkInitPermission("setActionBarAlignStyle")) {
            mActionBarHelper.getActionBarLayout().updateActionAlignStyle(actionAlignStyle, updateActionBar);
        }
    }

    public void setActionViewCallback(ActionBarLayout.ActionViewCallback callback) {
        if (mEnable && checkInitPermission("setActionViewCallback")) {
            mActionBarHelper.getActionBarLayout().setActionViewCallback(callback);
        }
    }

    public ActionBarHelper.ActionBarMode getActionBarMode() {
        if (mEnable && checkInitPermission("getActionBarMode")) {
            return mActionBarHelper.getActionBarMode();
        }
        return null;
    }

    public boolean addActionViewToContainer(ActionView actionView, ActionBarLayout.ActionContainer actionContainer, ActionView existActionView, boolean toLeft) {
        if (mEnable && checkInitPermission("addActionViewToContainer")) {
            return mActionBarHelper.getActionBarLayout().addActionViewToContainer(actionView, actionContainer, existActionView, toLeft);
        }
        return false;
    }

    public boolean addActionViewToContainer(ActionView actionView, ActionBarLayout.ActionContainer actionContainer, boolean append) {
        if (mEnable && checkInitPermission("addActionViewToContainer")) {
            return mActionBarHelper.getActionBarLayout().addActionViewToContainer(actionView, actionContainer, append);
        }
        return false;
    }

    public boolean addActionViewToContainer(ActionView actionView, ActionBarLayout.ActionContainer actionContainer, int position) {
        if (mEnable && checkInitPermission("addActionViewToContainer")) {
            return mActionBarHelper.getActionBarLayout().addActionViewToContainer(actionView, actionContainer, position);
        }
        return false;
    }

    public boolean removeActionViewFromContainer(ActionView actionView, ActionBarLayout.ActionContainer actionContainer) {
        if (mEnable) {
            checkInitPermission("removeActionViewFromContainer");
            return mActionBarHelper.getActionBarLayout().removeActionViewFromContainer(actionView, actionContainer);
        }
        return false;
    }

    public void clearActionBar(ActionBarLayout.ActionContainer actionContainer) {
        if (mEnable && checkInitPermission("clearActionBar")) {
            mActionBarHelper.getActionBarLayout().clearActionBar(actionContainer);
        }
    }

    public void setActionBarBackgroundResource(ActionBarLayout.ActionContainer actionContainer, int resid) {
        if (mEnable && checkInitPermission("setActionBarBackgroundResource")) {
            mActionBarHelper.getActionBarLayout().setActionBarBackgroundResource(actionContainer, resid);
        }
    }

    public void setActionBarBackgroundColor(ActionBarLayout.ActionContainer actionContainer, int color) {
        if (mEnable && checkInitPermission("setActionBarBackgroundColor")) {
            mActionBarHelper.getActionBarLayout().setActionBarBackgroundColor(actionContainer, color);
        }
    }

    public void setActionBarBackgroundDrawable(ActionBarLayout.ActionContainer actionContainer, Drawable drawable) {
        if (mEnable && checkInitPermission("setActionBarBackgroundDrawable")) {
            mActionBarHelper.getActionBarLayout().setActionBarBackgroundDrawable(actionContainer, drawable);
        }
    }

    public void updateActionBarAlpha(float alpha) {
        if (mEnable && checkInitPermission("updateActionBarAlpha")) {
            ActionBarLayout actionBarLayout = mActionBarHelper.getActionBarLayout();
            Drawable background = actionBarLayout.getBackground();
            if (null != background) {
                if (alpha < 0) {
                    alpha = 0;
                } else if (alpha > 1) {
                    alpha = 1;
                }
                // 修复5.0+版本同资源修改共享问题,使用mutate关闭共享.http://blog.csdn.net/myatlantis/article/details/49336587
                background.setAlpha((int) (alpha * 255));
                actionBarLayout.setActionBarBackgroundDrawable(null, background);
            }
        }
    }

    public void updateActionBar() {
        if (mEnable && checkInitPermission("updateActionBar")) {
            mActionBarHelper.getActionBarLayout().updateActionBar();
        }
    }

    public void setActionBarHeight(int pixel) {
        if (mEnable && checkInitPermission("setActionBarHeight")) {
            mActionBarHelper.setActionBarHeight(pixel);
        }
    }

    public boolean getActionBarMeasure(final Callback callback) {
        if (mEnable && checkInitPermission("getActionBarMeasure")) {
            mActionBarHelper.getActionBarContainer().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int width = mActionBarHelper.getActionBarContainer().getMeasuredWidth();
                    int height = mActionBarHelper.getActionBarContainer().getMeasuredHeight();
                    if (null != callback) {
                        callback.onActionBarMeasure(width, height);
                    }
                }
            }, ACTION_BAR_MEASURE_TIME);
            return true;
        }
        return false;
    }

    public void showActionBar() {
        if (mEnable && checkInitPermission("showActionBar")) {
            mActionBarHelper.showActionBar();
        }
    }

    public void hideActionBar() {
        if (mEnable && checkInitPermission("hideActionBar")) {
            mActionBarHelper.hideActionBar();
        }
    }

    public boolean isActionBarShow() {
        if (mEnable && checkInitPermission("isActionBarShow")) {
            return mActionBarHelper.isActionBarShow();
        }
        return false;
    }

    public void showActionBarLine() {
        if (mEnable && checkInitPermission("showActionBarLine")) {
            mActionBarHelper.showActionBarLine();
        }
    }

    public void hideActionBarLine() {
        if (mEnable && checkInitPermission("hideActionBarLine")) {
            mActionBarHelper.hideActionBarLine();
        }
    }

    public boolean isActionBarLineShow() {
        if (mEnable && checkInitPermission("isActionBarLineShow")) {
            return mActionBarHelper.isActionBarLineShow();
        }
        return false;
    }

    public FrameLayout getActionBarContainer() {
        if (mEnable && checkInitPermission("getActionBarContainer")) {
            return mActionBarHelper.getActionBarContainer();
        }
        return null;
    }

    public ActionBarLayout getActionBarLayout() {
        if (mEnable && checkInitPermission("getActionBarLayout")) {
            return mActionBarHelper.getActionBarLayout();
        }
        return null;
    }

    public FrameLayout getActionBarBelow() {
        if (mEnable && checkInitPermission("getActionBarBelow")) {
            return mActionBarHelper.getActionBarBelow();
        }
        return null;
    }

    public FrameLayout getActionBarCover() {
        if (mEnable && checkInitPermission("getActionBarCover")) {
            return mActionBarHelper.getActionBarCover();
        }
        return null;
    }

    public View getActionBarLine() {
        if (mEnable && checkInitPermission("getActionBarLine")) {
            return mActionBarHelper.getActionBarLine();
        }
        return null;
    }

    public View getContentView() {
        if (mEnable && checkInitPermission("getContentView")) {
            return mActionBarHelper.getContentView();
        }
        return null;
    }

    public interface Callback {
        void onActionBarMeasure(int height, int width);
    }

    private boolean checkInitPermission(String originCall) {
        if (!isInit) {
            UIBaseUtil.log(UiModelActionBarHelper.class.getSimpleName() + "##" + originCall + ">> must be called after inited");
            return false;
        }
        return true;
    }

}
