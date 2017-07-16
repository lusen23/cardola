package com.lusen.cardola.framework.uibase;

import android.view.View;

import com.lusen.cardola.framework.uibase.ui.slidingclose.SlidingCloseableHelper;
import com.lusen.cardola.framework.uibase.ui.slidingclose.SlidingCloseableRelativeLayout;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/8/4.
 * 侧滑关闭UiModel帮助类
 */
public class UiModelSlideCloseHelper {

    private static int DEFAULT_SLIDING_CLOSE_MODE = SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_HORIZONTAL_RIGHT;

    private SlidingCloseableHelper mSlidingCloseableHelper;
    private boolean mEnable = false;
    private int mPreSlideCloseMode = SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_NONE;
    private int mCurSlideCloseMode = SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_NONE;

    private boolean mInited = false;

    public UiModelSlideCloseHelper(boolean enable) {
        mEnable = enable;
    }

    View injectView(View contentView) {
        if (mEnable) {
            mSlidingCloseableHelper = new SlidingCloseableHelper();
            contentView = mSlidingCloseableHelper.injectView(contentView);
            mInited = true;
            // 预设:侧滑模式
            setSlideCloseMode(DEFAULT_SLIDING_CLOSE_MODE);
        }
        return contentView;
    }

    void registerCallback(SlidingCloseableHelper.SlideHelperSlidingScrollInterface slideHelperSlidingScrollInterface, SlidingCloseableHelper.SlideHelperSlidingCloseInterface slideHelperSlidingCloseInterface) {
        if (mEnable && checkInitPermission("registerCallback")) {
            mSlidingCloseableHelper.setSlideHelperSlidingScrollInterface(slideHelperSlidingScrollInterface);
            mSlidingCloseableHelper.setSlideHelperSlidingCloseInterface(slideHelperSlidingCloseInterface);
        }
    }

    void release() {
        if (mEnable && checkInitPermission("release")) {
            SlidingCloseableRelativeLayout slidingCloseableRelativeLayout = mSlidingCloseableHelper.getSlidingCloseableRelativeLayout();
            if (null != slidingCloseableRelativeLayout) {
                slidingCloseableRelativeLayout.setVisibility(View.GONE);
                slidingCloseableRelativeLayout.setOnSlidingCloseListener(null);
                slidingCloseableRelativeLayout.setOnSlidingScrollListener(null);
            }
        }
    }

    /**
     * 设置SlideClose侧滑模式
     *
     * @param slideCloseMode 侧滑模式
     */
    public void setSlideCloseMode(int slideCloseMode) {
        if (mEnable && checkInitPermission("setSlideCloseMode")) {
            SlidingCloseableRelativeLayout slidingCloseableRelativeLayout = mSlidingCloseableHelper.getSlidingCloseableRelativeLayout();
            if (null != slidingCloseableRelativeLayout) {
                mCurSlideCloseMode = slideCloseMode;
                slidingCloseableRelativeLayout.setSlidingCloseMode(slideCloseMode);
            }
        }
    }

    /**
     * 设置SlideClose能否侧滑
     *
     * @param canSlide 能否侧滑
     */
    public void setSlideCloseCanSlide(boolean canSlide) {
        if (mEnable && checkInitPermission("setSlideCloseCanSlide")) {
            if (canSlide) {
                if (mPreSlideCloseMode == SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_NONE) {
                    mPreSlideCloseMode = DEFAULT_SLIDING_CLOSE_MODE;
                }
                setSlideCloseMode(mPreSlideCloseMode);
            } else {
                mPreSlideCloseMode = mCurSlideCloseMode;
                setSlideCloseMode(SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_NONE);
            }
        }
    }

    /**
     * 获取SlideClose能否侧滑
     *
     * @return 能否侧滑
     */
    public boolean isSlideCloseCanSlide() {
        if (mEnable && checkInitPermission("isSlideCloseCanSlide")) {
            SlidingCloseableRelativeLayout slidingCloseableRelativeLayout = mSlidingCloseableHelper.getSlidingCloseableRelativeLayout();
            if (null != slidingCloseableRelativeLayout) {
                if (slidingCloseableRelativeLayout.getSlidingCloseMode() == SlidingCloseableRelativeLayout.SLIDING_CLOSE_MODE_NONE) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkInitPermission(String originCall) {
        if (!mInited) {
            UIBaseUtil.log(UiModelSlideCloseHelper.class.getSimpleName() + "##" + originCall + ">> must be called after inited");
            return false;
        }
        return true;
    }

}
