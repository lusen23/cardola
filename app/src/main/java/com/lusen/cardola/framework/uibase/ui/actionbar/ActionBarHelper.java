package com.lusen.cardola.framework.uibase.ui.actionbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveMode;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/8/12.
 */
public class ActionBarHelper {

    private View mContentView;
    private ViewGroup mRootView;
    private FrameLayout mActionBarContainerCover;
    private FrameLayout mActionBarContainerDivide;
    private FrameLayout mActionBarContainer;
    private ActionBarLayout mActionBarLayout;
    private FrameLayout mActionBarBelow;
    private FrameLayout mActionBarCover;
    private View mActionBarLine;

    private ActionBarUI mActionBarUI;
    private ActionBarMode mActionBarMode;

    public View injectView(View contentView, ActionBarUI actionBarUI, ActionBarMode actionBarMode) {
        mContentView = contentView;
        mActionBarUI = actionBarUI;
        mActionBarMode = actionBarMode;
        // 初始化控件
        mContentView = contentView;
        mRootView = (ViewGroup) LayoutInflater.from(contentView.getContext()).inflate(R.layout.uibase_actionbar_container, null);
        mActionBarContainerCover = (FrameLayout) mRootView.findViewById(R.id.uibase_actionbar_container_cover);
        mActionBarContainerDivide = (FrameLayout) mRootView.findViewById(R.id.uibase_actionbar_container_divide);
        // 获取ActionBar
        mActionBarContainer = (FrameLayout) mRootView.findViewById(R.id.uibase_action_container);
        mActionBarLayout = (ActionBarLayout) mRootView.findViewById(R.id.uibase_action_bar);
        mActionBarBelow = (FrameLayout) mRootView.findViewById(R.id.uibase_action_below);
        mActionBarCover = (FrameLayout) mRootView.findViewById(R.id.uibase_action_cover);
        mActionBarLine = mRootView.findViewById(R.id.uibase_action_line);
        // 提取contentView的背景设置给根布局,防止ActionBarLayout区域透明
        mRootView.setBackgroundDrawable(contentView.getBackground());
        mActionBarCover.setClickable(false);
        // 设置below、cover沉浸式时,忽略padding值设置,实现全面渗透
        ImmersiveMode.updateViewImmersiveNoPadding(mActionBarBelow, true);
        ImmersiveMode.updateViewImmersiveNoPadding(mActionBarCover, true);
        // ActionBarUI不传则设置默认
        if (null == mActionBarUI) {
            mActionBarUI = ActionBarUI.UI_ONLY_DIVIDE;
        }
        // ActionBarMode不传则设置默认
        if (null == mActionBarMode) {
            mActionBarMode = ActionBarMode.MODE_DIVIDE;
        }
        // 生成转换后的根布局
        if (mActionBarUI == ActionBarUI.UI_ONLY_DIVIDE) {
            mActionBarMode = ActionBarMode.MODE_DIVIDE;
            attachContentView(true);
        } else if (mActionBarUI == ActionBarUI.UI_ONLY_OVERLAP) {
            mActionBarMode = ActionBarMode.MODE_OVERLAP;
            attachContentView(false);
        } else if (mActionBarUI == ActionBarUI.UI_BOTH) {
            if (mActionBarMode == ActionBarMode.MODE_DIVIDE) {
                attachContentView(true);
            } else if (mActionBarMode == ActionBarMode.MODE_OVERLAP) {
                attachContentView(false);
            }
        }
        // 返回转换后的根布局
        return mRootView;
    }

    public void setActionBarMode(ActionBarMode mode, final boolean autoShowActionBar) {
        UIBaseUtil.checkUIThreadThrowException();
        if (null != mode) {
            if (mActionBarUI == ActionBarUI.UI_ONLY_DIVIDE) {
                mActionBarMode = ActionBarMode.MODE_DIVIDE;
                if (autoShowActionBar) {
                    showActionBar();
                }
            } else if (mActionBarUI == ActionBarUI.UI_ONLY_OVERLAP) {
                mActionBarMode = ActionBarMode.MODE_OVERLAP;
                if (autoShowActionBar) {
                    showActionBar();
                }
            } else if (mActionBarUI == ActionBarUI.UI_BOTH) {
                if (mActionBarMode != mode) {
                    mActionBarMode = mode;
                    if (mActionBarMode == ActionBarMode.MODE_DIVIDE) {
                        detachContentView(false);
                        attachContentView(true);
                        if (autoShowActionBar) {
                            showActionBar();
                        }
                    } else if (mActionBarMode == ActionBarMode.MODE_OVERLAP) {
                        detachContentView(true);
                        attachContentView(false);
                        if (autoShowActionBar) {
                            showActionBar();
                        }
                    }
                }
            }
        }
    }

    public ActionBarMode getActionBarMode() {
        return mActionBarMode;
    }

    public FrameLayout getActionBarContainer() {
        return mActionBarContainer;
    }

    public ActionBarLayout getActionBarLayout() {
        return mActionBarLayout;
    }

    public FrameLayout getActionBarBelow() {
        return mActionBarBelow;
    }

    public FrameLayout getActionBarCover() {
        return mActionBarCover;
    }

    public View getActionBarLine() {
        return mActionBarLine;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setActionBarHeight(int pixel) {
        mActionBarLayout.setActionBarHeight(pixel);
        ViewGroup.LayoutParams lpBelow = mActionBarBelow.getLayoutParams();
        if (null != lpBelow) {
            lpBelow.height = pixel;
        } else {
            lpBelow = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixel);
        }
        mActionBarBelow.setLayoutParams(lpBelow);
        ViewGroup.LayoutParams lpCover = mActionBarCover.getLayoutParams();
        if (null != lpCover) {
            lpCover.height = pixel;
        } else {
            lpCover = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixel);
        }
        mActionBarCover.setLayoutParams(lpCover);
    }

    public void showActionBar() {
        if (mActionBarUI == ActionBarUI.UI_ONLY_DIVIDE) {
            mActionBarContainer.setVisibility(View.VISIBLE);
        } else if (mActionBarUI == ActionBarUI.UI_ONLY_OVERLAP) {
            mActionBarContainer.setVisibility(View.VISIBLE);
        } else if (mActionBarUI == ActionBarUI.UI_BOTH) {
            if (mActionBarMode == ActionBarMode.MODE_DIVIDE) {
                mActionBarContainer.setVisibility(View.VISIBLE);
            } else if (mActionBarMode == ActionBarMode.MODE_OVERLAP) {
                mActionBarContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideActionBar() {
        if (mActionBarUI == ActionBarUI.UI_ONLY_DIVIDE) {
            mActionBarContainer.setVisibility(View.GONE);
        } else if (mActionBarUI == ActionBarUI.UI_ONLY_OVERLAP) {
            mActionBarContainer.setVisibility(View.INVISIBLE);
        } else if (mActionBarUI == ActionBarUI.UI_BOTH) {
            if (mActionBarMode == ActionBarMode.MODE_DIVIDE) {
                mActionBarContainer.setVisibility(View.GONE);
            } else if (mActionBarMode == ActionBarMode.MODE_OVERLAP) {
                mActionBarContainer.setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean isActionBarShow() {
        return mActionBarContainer.getVisibility() == View.VISIBLE ? true : false;
    }

    public void showActionBarLine() {
        mActionBarLine.setVisibility(View.VISIBLE);
    }

    public void hideActionBarLine() {
        mActionBarLine.setVisibility(View.GONE);
    }

    public boolean isActionBarLineShow() {
        return mActionBarLine.getVisibility() == View.VISIBLE ? true : false;
    }

    public enum ActionBarMode {
        MODE_DIVIDE,
        MODE_OVERLAP;
    }

    public enum ActionBarUI {
        UI_ONLY_DIVIDE, UI_ONLY_OVERLAP, UI_BOTH;
    }

    private void attachContentView(boolean divide) {
        if (divide) {
            mActionBarContainerDivide.addView(mContentView, generateLayoutParam());
        } else {
            mActionBarContainerCover.addView(mContentView, 0, generateLayoutParam());
        }
    }

    private void detachContentView(boolean divide) {
        if (divide) {
            mActionBarContainerDivide.removeView(mContentView);
        } else {
            mActionBarContainerCover.removeView(mContentView);
        }
    }

    private FrameLayout.LayoutParams generateLayoutParam() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return lp;
    }

}
