package com.lusen.cardola.framework.uikit;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.util.UiUtil;

/**
 * Created by leo on 2017/7/30.
 */

public class StateView extends FrameLayout implements View.OnClickListener {

    private View mEmptyLayout;
    private TextView mEmptyTitleView;

    private View mLoadingLayout;
    private TextView mLoadingTitleView;

    @State
    private int mState = State.INIT;

    private StateCallback mStateCallback;

    public StateView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_state_view, this);
        mEmptyLayout = UiUtil.findViewById(this, R.id.empty_layout, View.class);
        mEmptyTitleView = UiUtil.findViewById(this, R.id.empty_title, TextView.class);
        mLoadingLayout = UiUtil.findViewById(this, R.id.loading_layout, View.class);
        mLoadingTitleView = UiUtil.findViewById(this, R.id.loading_title, TextView.class);
        UiUtil.bindClickListener(this, mLoadingLayout, mEmptyLayout);
        mLoadingLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
    }

    private void performStateClick(@State int state) {
        if (null != mStateCallback) {
            mStateCallback.onStateClick(state);
        }
    }

    public void setEmptyTitle(String title) {
        if (null != title) {
            mEmptyTitleView.setText(title);
        }
    }

    public void setLoadingTitle(String title) {
        if (null != title) {
            mLoadingTitleView.setText(title);
        }
    }

    public void changeState(@State int state) {
        if (state == mState) {
            return;
        }
        mState = state;
        switch (state) {
            case State.INIT:
                mLoadingLayout.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.GONE);
                break;
            case State.LOADING:
                mLoadingLayout.setVisibility(View.VISIBLE);
                mEmptyLayout.setVisibility(View.GONE);
                break;
            case State.EMPTY:
                mLoadingLayout.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLoadingLayout.getId()) {
            performStateClick(State.LOADING);
        } else if (id == mEmptyLayout.getId()) {
            performStateClick(State.EMPTY);
        }
    }

    @IntDef({State.INIT, State.LOADING, State.EMPTY})
    public @interface State {
        int INIT = 0;
        int LOADING = 1;
        int EMPTY = 2;
    }

    public void setStateCallback(StateCallback stateCallback) {
        mStateCallback = stateCallback;
    }

    public interface StateCallback {
        void onStateClick(@State int state);
    }

}
