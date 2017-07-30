package com.lusen.cardola.framework.uikit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.base.BaseDialogFragmentSystem;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.ThreadUtil;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class LoadingDialog extends BaseDialogFragmentSystem {

    private View mRootView;
    private ImageView mLoadingImage;
    private TextView mLoadingTitle;

    private String mCustomLoadingTitle;
    private long mDismissTimeout = 0;
    private static final long DEFAULT_DISMISS_TIMEOUT = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideWhenStop(false);
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LoadingDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.loading_dialog, null, false);
        mLoadingImage = UiUtil.findViewById(mRootView, R.id.loading_image, ImageView.class);
        mLoadingTitle = UiUtil.findViewById(mRootView, R.id.loading_title, TextView.class);
        mLoadingImage.startAnimation(AnimationUtils.loadAnimation(ContextUtil.getContext(), R.anim.loading_anim));
        if (null != mCustomLoadingTitle) {
            mLoadingTitle.setText(mCustomLoadingTitle);
        }
        executeDismissTimeout();
        return mRootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    public void setLoadingTitle(String title) {
        mCustomLoadingTitle = title;
    }

    public void setDismissTimeout(long timeout) {
        mDismissTimeout = timeout;
    }

    private void executeDismissTimeout() {
        if (mDismissTimeout > 0) {
            ThreadUtil.MAIN_THREAD_HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSelf();
                }
            }, mDismissTimeout);
        }
    }

}
