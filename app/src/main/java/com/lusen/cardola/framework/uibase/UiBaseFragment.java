package com.lusen.cardola.framework.uibase;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.framework.uibase.base.BaseFragment;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.slidingclose.SlidingCloseableHelper;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/8/4.
 * UiBase基类Fragment,提供UiModel组合
 */
public abstract class UiBaseFragment extends BaseFragment {

    private String mTag = UIBaseUtil.generateFragmentTag(this);

    private int mUiModel = UiModel.UI_MODEL_NONE;

    protected UiModelSlideCloseHelper mUiModelSlideCloseHelper = new UiModelSlideCloseHelper(false);
    protected UiModelActionBarHelper mUiModelActionBarHelper = new UiModelActionBarHelper(false);
    protected UiModelImmersiveHelper mUiModelImmersiveHelper = new UiModelImmersiveHelper(false);

    // 延迟懒加载
    private boolean isLazyLoadPrepared = false;                     // 懒加载是否准备就绪,绕过首次setUserVisibleHint(true)导致的提前加载
    private boolean isFirstUserHintVisible = true;                  // 是否首次setUserVisibleHint(true)
    private boolean isFirstUserHintInvisible = true;                // 是否首次setUserVisibleHint(false)
    private boolean isUserHintInvisibleWhenFirstOnResume = false;   // 首次onResume时,UserHint=false

    protected abstract View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void onContentViewCreated(View view);

    protected View internalGetContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View contentView) {
        if (null != contentView) {
            return contentView;
        }
        return onContentViewInit(inflater, container, savedInstanceState);
    }

    public View inflaterView(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup container) {
        return UIBaseUtil.inflaterView(inflater, layoutRes, container);
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 初始化Bundle
        initBundle(null == getArguments() ? new Bundle() : getArguments());
        // 解析UiModel(准备构建UI样式模式:沉浸式、操作栏、侧滑)
        parseUiModel(getArguments());
        // 生成用户UI视图
        View contentView = internalGetContentView(inflater, container, savedInstanceState, null);
        if (null != contentView) {
            // 构建UiModel视图,生成最终UI视图
            mUiModelImmersiveHelper.injectActivity(getActivity(), false);
            contentView = mUiModelActionBarHelper.injectView(contentView, initActionBarUI(), initActionBarMode());
            contentView = mUiModelSlideCloseHelper.injectView(contentView);
        } else {
            // 用户UI视图异常,重置UiModel操作类
            mUiModel = UiModel.UI_MODEL_NONE;
            mUiModelSlideCloseHelper = new UiModelSlideCloseHelper(false);
            mUiModelActionBarHelper = new UiModelActionBarHelper(false);
            mUiModelImmersiveHelper = new UiModelImmersiveHelper(false);
        }
        // 设置最终UI视图
        return contentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        onNewLifeCycle(isVisibleToUser);
    }

    /**
     * 只适用于ViewPage，如果非ViewPage，需要自行实现onNewResume和onNewPause的生命周期
     *
     * @param isVisibleToUser setUserVisibleHint(isVisibleToUser)
     */
    protected void onNewLifeCycle(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // 首次可见(fragment加载时,处于可见态,系统会调用,如ViewPager当前页)
            if (isFirstUserHintVisible) {
                isFirstUserHintVisible = false;
                if (initLazyLoad()) {
                    prepareLazyLoad();
                }
                // 首次onResume时,UserHint=false,则在首次setUserVisibleHint(true)进行onNewResume补偿
                if (isUserHintInvisibleWhenFirstOnResume) {
                    dispatchNewResume();
                }
            } else {
                dispatchNewResume();
            }
        } else {
            // 首次不可见(fragment加载时,系统会调用,直接屏蔽)
            if (isFirstUserHintInvisible) {
                isFirstUserHintInvisible = false;
            } else {
                dispatchNewPause();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchNewPause();
        } else {
            dispatchNewResume();
        }
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUiModelSlideCloseHelper.registerCallback(mSlideHelperSlidingScrollInterface, mSlideHelperSlidingCloseInterface);
        if (initLazyLoad()) {
            // 懒加载
            prepareLazyLoad();
        } else {
            // 回调通知UI视图创建完毕
            onContentViewCreated(view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            dispatchNewResume();
        }
        // UserHint=false时,检测是否需要标识首次onResume时UserHint=false的状态,以便后续onNewResume补偿
        else if (!isUserHintInvisibleWhenFirstOnResume) {
            isUserHintInvisibleWhenFirstOnResume = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            dispatchNewPause();
        }
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUiModelSlideCloseHelper.release();
    }

    /**
     * 初始化Bundle(用于数据解析,在框架生命周期最前期执行)
     *
     * @param bundle bundle数据
     */
    @CallSuper
    protected void initBundle(@NonNull Bundle bundle) {

    }

    /**
     * 初始化UiModel
     *
     * @return UiModel {@link UiModel}
     */
    protected int initUiModel() {
        return UiModel.UI_MODEL_NONE;
    }

    /**
     * 初始化ActionBarUI
     *
     * @return ActionBarUI {@link ActionBarHelper.ActionBarUI}
     */
    protected ActionBarHelper.ActionBarUI initActionBarUI() {
        return ActionBarHelper.ActionBarUI.UI_ONLY_DIVIDE;
    }

    /**
     * 初始化ActionBarMode
     *
     * @return ActionBarMode {@link ActionBarHelper.ActionBarMode}
     */
    protected ActionBarHelper.ActionBarMode initActionBarMode() {
        return ActionBarHelper.ActionBarMode.MODE_DIVIDE;
    }

    /**
     * 是否需要懒加载
     *
     * @return 是否懒加载
     */
    protected boolean initLazyLoad() {
        return false;
    }

    /**
     * 懒加载回调
     */
    protected void onLazyLoad() {
        UIBaseUtil.log("LifeCycle#%s#onLazyLoad", mTag);
        onContentViewCreated(getView());
    }

    /**
     * 解析UiModel(用于Fragment最终uimodel解析获取)
     * 优先外部bundle,其次内部{@link UiBaseFragment#initUiModel()}
     *
     * @param bundle bundle数据
     */
    private void parseUiModel(Bundle bundle) {
        boolean bundleSpecify = false;
        if (null != bundle) {
            boolean existUiModel = bundle.containsKey(UiModel.PARAM_UI_MODEL);
            if (existUiModel) {
                mUiModel = bundle.getInt(UiModel.PARAM_UI_MODEL, UiModel.UI_MODEL_NONE);
                bundleSpecify = true;
            }
        }
        if (!bundleSpecify) {
            mUiModel = initUiModel();
        }
        boolean isUiModelActionBarSupported = UiModel.isUiModelActionBarSupported(mUiModel);
        boolean isUiModelSlideCloseSupported = UiModel.isUiModelSlideCloseSupported(mUiModel);
        boolean isUiModelImmersiveSupported = UiModel.isUiModelImmersiveSupported(mUiModel);
        mUiModelActionBarHelper = new UiModelActionBarHelper(isUiModelActionBarSupported);
        mUiModelSlideCloseHelper = new UiModelSlideCloseHelper(isUiModelSlideCloseSupported);
        mUiModelImmersiveHelper = new UiModelImmersiveHelper(isUiModelImmersiveSupported);
    }

    private SlidingCloseableHelper.SlideHelperSlidingScrollInterface mSlideHelperSlidingScrollInterface = new SlidingCloseableHelper.SlideHelperSlidingScrollInterface() {
        @Override
        public void onScrollStarted() {
            getStackHelperOfFragment().showPreviousFragmentOfParent();
        }

        @Override
        public void onScrollEnded(boolean isCloseState) {
            if (!isCloseState) {
                getStackHelperOfFragment().hidePreviousFragmentOfParent();
            }
        }
    };

    private SlidingCloseableHelper.SlideHelperSlidingCloseInterface mSlideHelperSlidingCloseInterface = new SlidingCloseableHelper.SlideHelperSlidingCloseInterface() {
        @Override
        public void onSlidingClosed() {
            getStackHelperOfFragment().finishSelf();
        }
    };

    private void prepareLazyLoad() {
        if (isLazyLoadPrepared) {
            onLazyLoad();
        } else {
            isLazyLoadPrepared = true;
        }
    }

}
