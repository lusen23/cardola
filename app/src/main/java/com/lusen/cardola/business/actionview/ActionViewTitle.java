package com.lusen.cardola.business.actionview;

import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uikit.RemoteTextView;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/16.
 */

public class ActionViewTitle extends ActionView {

    private RemoteTextView mRemoteTextViewPrimary;
    private RemoteTextView mRemoteTextViewSecondary;

    public ActionViewTitle(LayoutInflater inflater, String text) {
        super(inflater.inflate(R.layout.common_actionview_title_layout, null, false), ActionViewFactory.TITLE);
        initInternal();
        setTitlePrimary(text);
        hideTitleSecondary(true);
    }

    public ActionViewTitle(LayoutInflater inflater, @StringRes int text) {
        super(inflater.inflate(R.layout.common_actionview_title_layout, null, false), ActionViewFactory.TITLE);
        initInternal();
        setTitlePrimary(text);
        hideTitleSecondary(true);
    }

    public ActionViewTitle(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.common_actionview_title_layout, null, false), ActionViewFactory.TITLE);
        initInternal();
        hideTitleSecondary(true);
    }

    private void initInternal() {
        mRemoteTextViewPrimary = UiUtil.findViewById(getView(), R.id.common_actionview_title_primary, RemoteTextView.class);
        mRemoteTextViewSecondary = UiUtil.findViewById(getView(), R.id.common_actionview_title_secondary, RemoteTextView.class);
        mOnNormalPaddingLeft = sDefaultPaddingNormal;
        mOnNormalPaddingRight = sDefaultPaddingNormal;
        mOnLeftMostPaddingLeft = sDefaultPaddingEdgeMost;
        mOnLeftMostPaddingRight = sDefaultPaddingNormal;
        mOnRightMostPaddingLeft = sDefaultPaddingNormal;
        mOnRightMostPaddingRight = sDefaultPaddingEdgeMost;
    }

    public void setTitlePrimary(String text) {
        mRemoteTextViewPrimary.setText(text);
    }

    public void setTitlePrimary(@StringRes int text) {
        mRemoteTextViewPrimary.setText(text);
    }

    public void setTitleSecondary(String text) {
        mRemoteTextViewSecondary.setText(text);
    }

    public void setTitleSecondary(@StringRes int text) {
        mRemoteTextViewSecondary.setText(text);
    }

    public void showTitlePrimary() {
        mRemoteTextViewPrimary.setVisibility(View.VISIBLE);
    }

    public void hideTitlePrimary(boolean gone) {
        mRemoteTextViewPrimary.setVisibility(gone ? View.GONE : View.INVISIBLE);
    }

    public void showTitleSecondary() {
        mRemoteTextViewSecondary.setVisibility(View.VISIBLE);
    }

    public void hideTitleSecondary(boolean gone) {
        mRemoteTextViewSecondary.setVisibility(gone ? View.GONE : View.INVISIBLE);
    }

    public void enableTitlePrimaryEllipsizeScroll(boolean enable) {
        if (enable) {
            mRemoteTextViewPrimary.setFocusable(true);
            mRemoteTextViewPrimary.setSelected(true);
        } else {
            mRemoteTextViewPrimary.setFocusable(false);
            mRemoteTextViewPrimary.setSelected(false);
        }
    }

    public void enableTitleSecondaryEllipsizeScroll(boolean enable) {
        if (enable) {
            mRemoteTextViewSecondary.setFocusable(true);
            mRemoteTextViewSecondary.setSelected(true);
        } else {
            mRemoteTextViewSecondary.setFocusable(false);
            mRemoteTextViewSecondary.setSelected(false);
        }
    }

    public RemoteTextView getRemoteTextViewPrimary() {
        return mRemoteTextViewPrimary;
    }

    public RemoteTextView getRemoteTextViewSecondary() {
        return mRemoteTextViewSecondary;
    }
}

