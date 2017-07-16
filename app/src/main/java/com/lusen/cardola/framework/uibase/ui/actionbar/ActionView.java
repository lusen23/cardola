package com.lusen.cardola.framework.uibase.ui.actionbar;

import android.view.View;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.util.ContextUtil;

/**
 * Created by leo on 16/10/14.
 */
public class ActionView {

    public static final int sDefaultPaddingNormal = ContextUtil.getContext().getResources().getDimensionPixelSize(R.dimen.common_actionview_default_padding_normal);
    public static final int sDefaultPaddingEdgeMost = ContextUtil.getContext().getResources().getDimensionPixelSize(R.dimen.common_actionview_default_padding_edge_most);

    private View mView;
    private int mId;

    public int mOnNormalPaddingLeft = 0;
    public int mOnNormalPaddingRight = 0;
    public int mOnLeftMostPaddingLeft = 0;
    public int mOnLeftMostPaddingRight = 0;
    public int mOnRightMostPaddingLeft = 0;
    public int mOnRightMostPaddingRight = 0;

    protected ActionBarLayout mActionBarLayout;

    public ActionView(View view) {
        mView = view;
    }

    public ActionView(View view, int id) {
        mView = view;
        mId = id;
    }

    public final View getView() {
        return mView;
    }

    public int getId() {
        return mId;
    }

    public final void setEnable(boolean enable) {
        mView.setEnabled(enable);
    }

    public final boolean isEnable() {
        return mView.isEnabled();
    }

    public final void show() {
        show(true);
    }

    public final void hide(boolean gone) {
        hide(gone, true);
    }

    public final void show(boolean update) {
        if (isShow()) {
            return;
        }
        mView.setVisibility(View.VISIBLE);
        if (update && null != mActionBarLayout) {
            mActionBarLayout.updateActionBar();
        }
    }

    public final void hide(boolean gone, boolean update) {
        if ((isGone() && gone) || (isInvisible() && !gone)) {
            return;
        }
        mView.setVisibility(gone ? View.GONE : View.INVISIBLE);
        if (update && null != mActionBarLayout) {
            mActionBarLayout.updateActionBar();
        }
    }

    public final boolean isShow() {
        return mView.getVisibility() == View.VISIBLE;
    }

    public final boolean isGone() {
        return mView.getVisibility() == View.GONE;
    }

    public final boolean isInvisible() {
        return mView.getVisibility() == View.INVISIBLE;
    }

    public final boolean isUseSpace() {
        return isShow() || isInvisible();
    }

    @Override
    public final boolean equals(Object other) {
        if (null != other && other instanceof ActionView) {
            ActionView otherActionView = (ActionView) other;
            if (this == otherActionView) {
                return true;
            }
            View otherView = otherActionView.getView();
            if (null != mView && null != otherView && otherView == mView) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}