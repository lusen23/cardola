package com.lusen.cardola.business.actionview;

import android.support.annotation.StringRes;
import android.view.LayoutInflater;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uikit.RemoteTextView;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/16.
 */

public class ActionViewPlain extends ActionView {

    private RemoteTextView mRemoteTextView;

    public ActionViewPlain(LayoutInflater inflater, int id, String text) {
        super(inflater.inflate(R.layout.common_actionview_plain_layout, null, false), id);
        initInternal();
        setText(text);
    }

    public ActionViewPlain(LayoutInflater inflater, int id, @StringRes int text) {
        super(inflater.inflate(R.layout.common_actionview_plain_layout, null, false), id);
        initInternal();
        setText(text);
    }

    public ActionViewPlain(LayoutInflater inflater, int id) {
        super(inflater.inflate(R.layout.common_actionview_plain_layout, null, false), id);
        initInternal();
    }

    private void initInternal() {
        mRemoteTextView = UiUtil.findViewById(getView(), R.id.common_actionview_plain, RemoteTextView.class);
        mOnNormalPaddingLeft = sDefaultPaddingNormal;
        mOnNormalPaddingRight = sDefaultPaddingNormal;
        mOnLeftMostPaddingLeft = sDefaultPaddingEdgeMost;
        mOnLeftMostPaddingRight = sDefaultPaddingNormal;
        mOnRightMostPaddingLeft = sDefaultPaddingNormal;
        mOnRightMostPaddingRight = sDefaultPaddingEdgeMost;
    }

    public void setText(String text) {
        mRemoteTextView.setText(text);
    }

    public void setText(@StringRes int text) {
        mRemoteTextView.setText(text);
    }

    public RemoteTextView getRemoteTextView() {
        return mRemoteTextView;
    }

}
