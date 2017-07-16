package com.lusen.cardola.framework.uibase.ui.slidingclose;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leo on 16/7/22.
 */
public class SlidingCloseableHelper implements SlidingCloseableRelativeLayout.OnSlidingCloseListener, SlidingCloseableRelativeLayout.OnSlidingScrollListener {

    private SlidingCloseableRelativeLayout mSlidingCloseableRelativeLayout;
    private SlideHelperSlidingScrollInterface mSlideHelperSlidingScrollInterface;
    private SlideHelperSlidingCloseInterface mSlideHelperSlidingCloseInterface;

    public SlidingCloseableRelativeLayout getSlidingCloseableRelativeLayout() {
        return mSlidingCloseableRelativeLayout;
    }

    public void setSlideHelperSlidingScrollInterface(SlideHelperSlidingScrollInterface slideHelperSlidingScrollInterface) {
        mSlideHelperSlidingScrollInterface = slideHelperSlidingScrollInterface;
    }

    public void setSlideHelperSlidingCloseInterface(SlideHelperSlidingCloseInterface slideHelperSlidingCloseInterface) {
        mSlideHelperSlidingCloseInterface = slideHelperSlidingCloseInterface;
    }

    public ViewGroup injectView(View contentView) {
        mSlidingCloseableRelativeLayout = new SlidingCloseableRelativeLayout(contentView.getContext());
        mSlidingCloseableRelativeLayout.setSlidingCloseMode(SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_HORIZONTAL);
        mSlidingCloseableRelativeLayout.setOnSlidingCloseListener(this);
        mSlidingCloseableRelativeLayout.setOnSlidingScrollListener(this);
        mSlidingCloseableRelativeLayout.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return mSlidingCloseableRelativeLayout;
    }

    @Override
    public void onSlidingClosed() {
        if (mSlidingCloseableRelativeLayout != null) {
            mSlidingCloseableRelativeLayout.setVisibility(View.GONE);
            if (mSlideHelperSlidingCloseInterface != null) {
                mSlideHelperSlidingCloseInterface.onSlidingClosed();
            }
        } else {
            if (mSlideHelperSlidingCloseInterface != null) {
                mSlideHelperSlidingCloseInterface.onSlidingClosed();
            }
        }
    }

    @Override
    public void onScrollStarted() {
        if (mSlideHelperSlidingScrollInterface != null) {
            mSlideHelperSlidingScrollInterface.onScrollStarted();
        }
    }

    @Override
    public void onScrollEnded(boolean isCloseState) {
        if (mSlideHelperSlidingScrollInterface != null) {
            mSlideHelperSlidingScrollInterface.onScrollEnded(isCloseState);
        }
    }

    public interface SlideHelperSlidingScrollInterface {

        public void onScrollStarted();

        public void onScrollEnded(boolean isCloseState);

    }

    public interface SlideHelperSlidingCloseInterface {

        public void onSlidingClosed();

    }
}
