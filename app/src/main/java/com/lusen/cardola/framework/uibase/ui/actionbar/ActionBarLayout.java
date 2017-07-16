package com.lusen.cardola.framework.uibase.ui.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 16/10/14.
 * ActionBar控件
 */
public class ActionBarLayout extends LinearLayout {

    private LinearLayout mActionViewContainerLeft;
    private LinearLayout mActionViewContainerCenter;
    private LinearLayout mActionViewContainerRight;

    private Map<ActionView, Integer> mActionViewsLeft = new LinkedHashMap<>();
    private Map<ActionView, Integer> mActionViewsCenter = new LinkedHashMap<>();
    private Map<ActionView, Integer> mActionViewsRight = new LinkedHashMap<>();

    private View mEmptyFillView;
    private LayoutParams mEmptyFillViewLayoutParams;

    private ActionAlignStyle mActionAlignStyle;

    private ActionViewCallback mActionViewCallback;

    private Comparator mComparatorActionView = new Comparator<Map.Entry<ActionView, Integer>>() {

        @Override
        public int compare(Map.Entry<ActionView, Integer> lhs, Map.Entry<ActionView, Integer> rhs) {
            // 按照ActionView的Index索引升序排列
            return lhs.getValue().compareTo(rhs.getValue());
        }
    };

    public ActionBarLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ActionBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ActionBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // 初始化容器控件
        setClickable(true);
        setOrientation(LinearLayout.HORIZONTAL);
        mActionViewContainerLeft = new LinearLayout(context);
        mActionViewContainerLeft.setOrientation(LinearLayout.HORIZONTAL);
        mActionViewContainerCenter = new LinearLayout(context);
        mActionViewContainerCenter.setOrientation(LinearLayout.HORIZONTAL);
        mActionViewContainerRight = new LinearLayout(context);
        mActionViewContainerRight.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lpLeft = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutParams lpCenter = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lpCenter.weight = 1;
        LayoutParams lpRight = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 初始化右容器的占位控件
        mEmptyFillView = new View(context);
        mEmptyFillView.setBackgroundResource(android.R.color.transparent);
        mEmptyFillViewLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mActionViewContainerRight.addView(mEmptyFillView, mEmptyFillViewLayoutParams);
        // 加入容器控件至ActionBar
        addView(mActionViewContainerLeft, lpLeft);
        addView(mActionViewContainerCenter, lpCenter);
        addView(mActionViewContainerRight, lpRight);
    }

    /**
     * 增加ActionView
     *
     * @param actionView      ActionView控件
     * @param actionContainer 容器类型(left/center/right)
     * @param existActionView 已存在容器内的ActionView控件
     * @param toLeft          在已存在ActionView的左/右
     * @return 是否成功
     */
    public boolean addActionViewToContainer(ActionView actionView, ActionContainer actionContainer, ActionView existActionView, boolean toLeft) {
        int index = getActionViewIndex(existActionView, actionContainer);
        if (index >= 0) {
            if (toLeft) {
                index--;
                index = index < 0 ? 0 : index;
            } else {
                index++;
            }
            return addActionViewToContainer(actionView, actionContainer, index);
        }
        return false;
    }

    /**
     * 增加ActionView
     *
     * @param actionView      ActionView控件
     * @param actionContainer 容器类型(left/center/right)
     * @param append          追加/首位
     * @return 是否成功
     */
    public boolean addActionViewToContainer(ActionView actionView, ActionContainer actionContainer, boolean append) {
        int position = -1;
        if (!append) {
            position = 0;
        }
        return addActionViewToContainer(actionView, actionContainer, position);
    }

    /**
     * 增加ActionView
     *
     * @param actionView      ActionView控件
     * @param actionContainer 容器类型(left/center/right)
     * @param position        容器内position(小于0,表示追加;等于0,表示首位;大于0且小于等于容器内总数,表示对应position)
     * @return 是否成功
     */
    public boolean addActionViewToContainer(ActionView actionView, ActionContainer actionContainer, int position) {
        UIBaseUtil.checkUIThreadThrowException();
        if (null != actionView && null != actionView.getView() && null != actionContainer) {
            View view = actionView.getView();
            if (actionContainer == ActionContainer.LEFT) {
                if (!mActionViewsLeft.containsKey(actionView)) {
                    int count = mActionViewContainerLeft.getChildCount();
                    if (position <= count) {
                        callActionViewAddCallback(actionView, actionContainer);
                        LayoutParams lp = generateViewLayoutParam(view, false);
                        mActionViewContainerLeft.addView(view, position, lp);
                        mActionViewsLeft.put(actionView, -1);
                        actionView.mActionBarLayout = this;
                        updateIndex(actionContainer);
                        updateLayout();
                        return true;
                    }
                }
            } else if (actionContainer == ActionContainer.CENTER) {
                if (!mActionViewsCenter.containsKey(actionView)) {
                    int count = mActionViewContainerCenter.getChildCount();
                    if (position <= count) {
                        callActionViewAddCallback(actionView, actionContainer);
                        LayoutParams lp = generateViewLayoutParam(view, true);
                        mActionViewContainerCenter.addView(view, position, lp);
                        mActionViewsCenter.put(actionView, -1);
                        actionView.mActionBarLayout = this;
                        updateIndex(actionContainer);
                        updateLayout();
                        return true;
                    }
                }
            } else if (actionContainer == ActionContainer.RIGHT) {
                if (!mActionViewsRight.containsKey(actionView)) {
                    int count = mActionViewContainerRight.getChildCount() - 1;
                    if (position <= count) {
                        callActionViewAddCallback(actionView, actionContainer);
                        LayoutParams lp = generateViewLayoutParam(view, false);
                        position = (position >= 0 ? position + 1 : position);
                        mActionViewContainerRight.addView(view, position, lp);
                        mActionViewsRight.put(actionView, -1);
                        actionView.mActionBarLayout = this;
                        updateIndex(actionContainer);
                        updateLayout();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 删除ActionView
     *
     * @param actionView      ActionView控件
     * @param actionContainer 容器类型(left/center/right)
     * @return 是否成功
     */
    public boolean removeActionViewFromContainer(ActionView actionView, ActionContainer actionContainer) {
        UIBaseUtil.checkUIThreadThrowException();
        if (null != actionView && null != actionView.getView() && null != actionContainer) {
            View view = actionView.getView();
            if (actionContainer == ActionContainer.LEFT) {
                if (mActionViewsLeft.containsKey(actionView)) {
                    callActionViewRemoveCallback(actionView, actionContainer);
                    mActionViewContainerLeft.removeView(view);
                    mActionViewsLeft.remove(actionView);
                    updateIndex(actionContainer);
                    updateLayout();
                    return true;
                }
            } else if (actionContainer == ActionContainer.CENTER) {
                if (mActionViewsCenter.containsKey(actionView)) {
                    callActionViewRemoveCallback(actionView, actionContainer);
                    mActionViewContainerCenter.removeView(view);
                    mActionViewsCenter.remove(actionView);
                    updateIndex(actionContainer);
                    updateLayout();
                    return true;
                }
            } else if (actionContainer == ActionContainer.RIGHT) {
                if (mActionViewsRight.containsKey(actionView)) {
                    callActionViewRemoveCallback(actionView, actionContainer);
                    mActionViewContainerRight.removeView(view);
                    mActionViewsRight.remove(actionView);
                    updateIndex(actionContainer);
                    updateLayout();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 清除ActionBar容器
     *
     * @param actionContainer 容器类型(left/center/right/null),null表示所有容器类型
     */
    public void clearActionBar(ActionContainer actionContainer) {
        UIBaseUtil.checkUIThreadThrowException();
        if (actionContainer == ActionContainer.LEFT) {
            Iterator<Map.Entry<ActionView, Integer>> iteratorLeft = mActionViewsLeft.entrySet().iterator();
            while (iteratorLeft.hasNext()) {
                Map.Entry<ActionView, Integer> entry = iteratorLeft.next();
                mActionViewContainerLeft.removeView(entry.getKey().getView());
                iteratorLeft.remove();
            }
            updateLayout();
        } else if (actionContainer == ActionContainer.CENTER) {
            Iterator<Map.Entry<ActionView, Integer>> iteratorCenter = mActionViewsCenter.entrySet().iterator();
            while (iteratorCenter.hasNext()) {
                Map.Entry<ActionView, Integer> entry = iteratorCenter.next();
                mActionViewContainerCenter.removeView(entry.getKey().getView());
                iteratorCenter.remove();
            }
            updateLayout();
        } else if (actionContainer == ActionContainer.RIGHT) {
            Iterator<Map.Entry<ActionView, Integer>> iteratorRight = mActionViewsRight.entrySet().iterator();
            while (iteratorRight.hasNext()) {
                Map.Entry<ActionView, Integer> entry = iteratorRight.next();
                mActionViewContainerRight.removeView(entry.getKey().getView());
                iteratorRight.remove();
            }
            updateLayout();
        } else {
            Iterator<Map.Entry<ActionView, Integer>> iteratorLeft = mActionViewsLeft.entrySet().iterator();
            while (iteratorLeft.hasNext()) {
                Map.Entry<ActionView, Integer> entry = iteratorLeft.next();
                mActionViewContainerLeft.removeView(entry.getKey().getView());
                iteratorLeft.remove();
            }
            Iterator<Map.Entry<ActionView, Integer>> iteratorCenter = mActionViewsCenter.entrySet().iterator();
            while (iteratorCenter.hasNext()) {
                Map.Entry<ActionView, Integer> entry = iteratorCenter.next();
                mActionViewContainerCenter.removeView(entry.getKey().getView());
                iteratorCenter.remove();
            }
            Iterator<Map.Entry<ActionView, Integer>> iteratorRight = mActionViewsRight.entrySet().iterator();
            while (iteratorRight.hasNext()) {
                Map.Entry<ActionView, Integer> entry = iteratorRight.next();
                mActionViewContainerRight.removeView(entry.getKey().getView());
                iteratorRight.remove();
            }
            updateLayout();
        }
    }

    /**
     * 设置ActionBar背景
     *
     * @param actionContainer 容器类型(left/center/right/null),null表示ActionBar
     * @param resid           资源id
     */
    public void setActionBarBackgroundResource(ActionContainer actionContainer, int resid) {
        UIBaseUtil.checkUIThreadThrowException();
        if (actionContainer == ActionContainer.LEFT) {
            mActionViewContainerLeft.setBackgroundResource(resid);
        } else if (actionContainer == ActionContainer.CENTER) {
            mActionViewContainerCenter.setBackgroundResource(resid);
        } else if (actionContainer == ActionContainer.RIGHT) {
            mActionViewContainerRight.setBackgroundResource(resid);
        } else {
            setBackgroundResource(resid);
        }
    }

    /**
     * 设置ActionBar背景
     *
     * @param actionContainer 容器类型(left/center/right/null),null表示ActionBar
     * @param color           资源color
     */
    public void setActionBarBackgroundColor(ActionContainer actionContainer, int color) {
        UIBaseUtil.checkUIThreadThrowException();
        if (actionContainer == ActionContainer.LEFT) {
            mActionViewContainerLeft.setBackgroundColor(color);
        } else if (actionContainer == ActionContainer.CENTER) {
            mActionViewContainerCenter.setBackgroundColor(color);
        } else if (actionContainer == ActionContainer.RIGHT) {
            mActionViewContainerRight.setBackgroundColor(color);
        } else {
            setBackgroundColor(color);
        }
    }

    /**
     * 设置ActionBar背景
     *
     * @param actionContainer 容器类型(left/center/right/null),null表示ActionBar
     * @param drawable        资源drawable
     */
    public void setActionBarBackgroundDrawable(ActionContainer actionContainer, Drawable drawable) {
        UIBaseUtil.checkUIThreadThrowException();
        if (actionContainer == ActionContainer.LEFT) {
            mActionViewContainerLeft.setBackgroundDrawable(drawable);
        } else if (actionContainer == ActionContainer.CENTER) {
            mActionViewContainerCenter.setBackgroundDrawable(drawable);
        } else if (actionContainer == ActionContainer.RIGHT) {
            mActionViewContainerRight.setBackgroundDrawable(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    /**
     * 更新ActionBar中排列方式
     *
     * @param actionAlignStyle 排列方式
     * @param updateActionBar  是否更新ActionBar
     */
    public void updateActionAlignStyle(ActionAlignStyle actionAlignStyle, boolean updateActionBar) {
        mActionAlignStyle = actionAlignStyle;
        if (updateActionBar) {
            updateActionBar();
        }
    }

    /**
     * 更新ActionBar
     */
    public void updateActionBar() {
        UIBaseUtil.checkUIThreadThrowException();
        updateLayout();
    }

    /**
     * 设置Action高度
     *
     * @param pixel 高度像素值
     */
    public void setActionBarHeight(int pixel) {
        UIBaseUtil.checkUIThreadThrowException();
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (null != lp) {
            lp.height = pixel;
        } else {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixel);
        }
        setLayoutParams(lp);
    }

    /**
     * 显示ActionBar
     */
    public void showActionBar() {
        UIBaseUtil.checkUIThreadThrowException();
        setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏ActionBar
     */
    public void hideActionBar(boolean gone) {
        UIBaseUtil.checkUIThreadThrowException();
        setVisibility(gone ? View.GONE : View.INVISIBLE);
    }

    /**
     * ActionBar是否显示
     *
     * @return 是否显示
     */
    public boolean isActionBarShow() {
        return getVisibility() == View.VISIBLE ? true : false;
    }

    /**
     * 获取ActionView容器
     *
     * @param actionContainer 容器类型(left/center/right)
     * @return ActionView容器
     */
    public LinearLayout getActionViewContainer(ActionContainer actionContainer) {
        UIBaseUtil.checkUIThreadThrowException();
        if (actionContainer == ActionContainer.LEFT) {
            return mActionViewContainerLeft;
        } else if (actionContainer == ActionContainer.CENTER) {
            return mActionViewContainerCenter;
        } else if (actionContainer == ActionContainer.RIGHT) {
            return mActionViewContainerRight;
        }
        return null;
    }

    /**
     * 设置ActionView回调函数
     *
     * @param actionViewCallback 回调
     */
    public void setActionViewCallback(ActionViewCallback actionViewCallback) {
        mActionViewCallback = actionViewCallback;
    }

    private void updateLayout() {
        // 重置Right区左侧占位View宽度=0,设置Left、Right最小宽度=0
        mEmptyFillViewLayoutParams.width = 0;
        mEmptyFillView.setLayoutParams(mEmptyFillViewLayoutParams);
        mActionViewContainerLeft.setMinimumWidth(0);
        mActionViewContainerRight.setMinimumWidth(0);
        // AlignStyle:平分
        if (null == mActionAlignStyle || mActionAlignStyle == ActionAlignStyle.AVERAGE) {
            updateActionViewPadding();
            post(new Runnable() {
                @Override
                public void run() {
                    int actionViewContainerLeftWidth = mActionViewContainerLeft.getMeasuredWidth();
                    int actionViewContainerRightWidth = mActionViewContainerRight.getMeasuredWidth();
                    // 计算左右区域较大值
                    int maxWidth = Math.max(actionViewContainerLeftWidth, actionViewContainerRightWidth);
                    // 填充右区域空白占位
                    if (actionViewContainerLeftWidth > actionViewContainerRightWidth) {
                        int emptyFillViewWidth = actionViewContainerLeftWidth - actionViewContainerRightWidth;
                        mEmptyFillViewLayoutParams.width = emptyFillViewWidth;
                        mEmptyFillView.setLayoutParams(mEmptyFillViewLayoutParams);
                    }
                    // 设置左右区域宽度相等
                    mActionViewContainerLeft.setMinimumWidth(maxWidth);
                    mActionViewContainerRight.setMinimumWidth(maxWidth);
                }
            });
        }
        // AlignStyle:原始
        else if (mActionAlignStyle == ActionAlignStyle.ORIGINAL) {
            updateActionViewPadding();
            mActionViewContainerLeft.setMinimumWidth(0);
            mActionViewContainerRight.setMinimumWidth(0);
        }
    }

    private void updateActionViewPadding() {
        // 从左容器中的最左位置开始寻找首个占用视图空间的ActionView及剩余ActionView,设置适配padding
        List<Map.Entry<ActionView, Integer>> actionViewLeftContainer = new ArrayList<>(mActionViewsLeft.entrySet());
        Collections.sort(actionViewLeftContainer, mComparatorActionView);
        boolean findFirstUseSpaceActionViewAtLeftMost = false;
        for (int index = 0; index < actionViewLeftContainer.size(); index++) {
            ActionView actionView = actionViewLeftContainer.get(index).getKey();
            if (findFirstUseSpaceActionViewAtLeftMost) {
                actionView.getView().setPadding(actionView.mOnNormalPaddingLeft, actionView.getView().getPaddingTop(), actionView.mOnNormalPaddingRight, actionView.getView().getPaddingBottom());
            } else {
                if (actionView.isUseSpace()) {
                    findFirstUseSpaceActionViewAtLeftMost = true;
                    actionView.getView().setPadding(actionView.mOnLeftMostPaddingLeft, actionView.getView().getPaddingTop(), actionView.mOnLeftMostPaddingRight, actionView.getView().getPaddingBottom());
                }
            }
        }
        // 从右容器中的最右位置开始寻找首个占用视图空间的ActionView及剩余ActionView,设置适配padding
        List<Map.Entry<ActionView, Integer>> actionViewRightContainer = new ArrayList<>(mActionViewsRight.entrySet());
        Collections.sort(actionViewRightContainer, mComparatorActionView);
        boolean findFirstUseSpaceActionViewAtRightMost = false;
        for (int index = actionViewRightContainer.size() - 1; index >= 0; index--) {
            ActionView actionView = actionViewRightContainer.get(index).getKey();
            if (findFirstUseSpaceActionViewAtRightMost) {
                actionView.getView().setPadding(actionView.mOnNormalPaddingLeft, actionView.getView().getPaddingTop(), actionView.mOnNormalPaddingRight, actionView.getView().getPaddingBottom());
            } else {
                if (actionView.isUseSpace()) {
                    findFirstUseSpaceActionViewAtRightMost = true;
                    actionView.getView().setPadding(actionView.mOnRightMostPaddingLeft, actionView.getView().getPaddingTop(), actionView.mOnRightMostPaddingRight, actionView.getView().getPaddingBottom());
                }
            }
        }
    }

    private void updateIndex(ActionContainer actionContainer) {
        if (actionContainer == ActionContainer.LEFT) {
            for (Map.Entry<ActionView, Integer> entry : mActionViewsLeft.entrySet()) {
                ActionView actionView = entry.getKey();
                int index = mActionViewContainerLeft.indexOfChild(actionView.getView());
                mActionViewsLeft.put(actionView, index);
            }
        } else if (actionContainer == ActionContainer.CENTER) {
            for (Map.Entry<ActionView, Integer> entry : mActionViewsCenter.entrySet()) {
                ActionView actionView = entry.getKey();
                int index = mActionViewContainerCenter.indexOfChild(actionView.getView());
                mActionViewsCenter.put(actionView, index);
            }
        } else if (actionContainer == ActionContainer.RIGHT) {
            for (Map.Entry<ActionView, Integer> entry : mActionViewsRight.entrySet()) {
                ActionView actionView = entry.getKey();
                int index = mActionViewContainerRight.indexOfChild(actionView.getView());
                mActionViewsRight.put(actionView, index);
            }
        }
    }

    private LayoutParams generateViewLayoutParam(View view, boolean fromCenter) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        ViewGroup.LayoutParams originLp = view.getLayoutParams();
        LayoutParams targetLp;
        if (null == originLp) {
            targetLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            targetLp = new LayoutParams(originLp.width, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (fromCenter) {
            targetLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            targetLp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        return targetLp;
    }

    private boolean isActionViewExist(ActionView actionView, ActionContainer actionContainer) {
        if (null != actionView && null != actionContainer) {
            if (actionContainer == ActionContainer.LEFT) {
                return mActionViewsLeft.containsKey(actionView);
            } else if (actionContainer == ActionContainer.CENTER) {
                return mActionViewsCenter.containsKey(actionView);
            } else if (actionContainer == ActionContainer.RIGHT) {
                return mActionViewsRight.containsKey(actionView);
            }
        }
        return false;
    }

    private int getActionViewIndex(ActionView actionView, ActionContainer actionContainer) {
        if (null != actionView && null != actionContainer) {
            if (actionContainer == ActionContainer.LEFT) {
                if (mActionViewsLeft.containsKey(actionView)) {
                    return mActionViewsLeft.get(actionView);
                }
            } else if (actionContainer == ActionContainer.CENTER) {
                if (mActionViewsCenter.containsKey(actionView)) {
                    return mActionViewsCenter.get(actionView);
                }
            } else if (actionContainer == ActionContainer.RIGHT) {
                if (mActionViewsRight.containsKey(actionView)) {
                    return mActionViewsRight.get(actionView);
                }
            }
        }
        return -1;
    }

    private void callActionViewAddCallback(ActionView actionView, ActionContainer actionContainer) {
        if (null != mActionViewCallback) {
            mActionViewCallback.onActionViewAdd(actionView, actionContainer);
        }
    }

    private void callActionViewRemoveCallback(ActionView actionView, ActionContainer actionContainer) {
        if (null != mActionViewCallback) {
            mActionViewCallback.onActionViewRemove(actionView, actionContainer);
        }
    }

    public enum ActionContainer {
        LEFT, CENTER, RIGHT;
    }

    public enum ActionAlignStyle {
        AVERAGE, ORIGINAL;
    }

    public interface ActionViewCallback {
        void onActionViewAdd(ActionView actionView, ActionBarLayout.ActionContainer actionContainer);

        void onActionViewRemove(ActionView actionView, ActionBarLayout.ActionContainer actionContainer);
    }

}
