package com.lusen.cardola.business.actionview;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uikit.RemoteImageView;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/16.
 */

public class ActionViewIcon extends ActionView {

    private RemoteImageView mRemoteImageView;

    public ActionViewIcon(LayoutInflater inflater, int id, String url) {
        super(inflater.inflate(R.layout.common_actionview_icon_layout, null, false), id);
        initInternal();
        setImage(url);
    }

    public ActionViewIcon(LayoutInflater inflater, int id, @DrawableRes int resId) {
        super(inflater.inflate(R.layout.common_actionview_icon_layout, null, false), id);
        initInternal();
        setImage(resId);
    }

    public ActionViewIcon(LayoutInflater inflater, int id) {
        super(inflater.inflate(R.layout.common_actionview_icon_layout, null, false), id);
        initInternal();
    }

    private void initInternal() {
        mRemoteImageView = UiUtil.findViewById(getView(), R.id.common_actionview_icon, RemoteImageView.class);
        mOnNormalPaddingLeft = sDefaultPaddingNormal;
        mOnNormalPaddingRight = sDefaultPaddingNormal;
        mOnLeftMostPaddingLeft = sDefaultPaddingEdgeMost;
        mOnLeftMostPaddingRight = sDefaultPaddingNormal;
        mOnRightMostPaddingLeft = sDefaultPaddingNormal;
        mOnRightMostPaddingRight = sDefaultPaddingEdgeMost;
    }

    public void setImage(String url) {
        mRemoteImageView.load(url);
    }

    public void setImage(@DrawableRes int resId) {
        mRemoteImageView.setImageResource(resId);
    }

    public void setImage(Drawable drawable) {
        mRemoteImageView.setImageDrawable(drawable);
    }

    public RemoteImageView getRemoteImageView() {
        return mRemoteImageView;
    }

}
