package com.lusen.cardola.framework.uibase;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lusen.cardola.framework.uibase.base.BaseActivity;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.slidingclose.SlidingCloseableHelper;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/8/15.
 * UiBase基类Activity,提供UiModel组合
 * >>侧滑关闭UiModel需要Activity在manifest内申明主题Theme.SlidingCloseableActivity
 */
public abstract class UiBaseActivity extends BaseActivity {

    private int mUiModel = UiModel.UI_MODEL_NONE;

    protected UiModelSlideCloseHelper mUiModelSlideCloseHelper = new UiModelSlideCloseHelper(false);
    protected UiModelActionBarHelper mUiModelActionBarHelper = new UiModelActionBarHelper(false);
    protected UiModelImmersiveHelper mUiModelImmersiveHelper = new UiModelImmersiveHelper(false);

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
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化Bundle
        Bundle bundle = null;
        if (null != getIntent()) {
            bundle = getIntent().getExtras();
        }
        initBundle(null == bundle ? new Bundle() : bundle);
        super.onCreate(savedInstanceState);
        // 因沉浸式模式效果,务必关闭Title栏,且必须在setContentView前调用
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch (Exception e) {
        }
        // 解析UiModel(准备构建UI样式模式:沉浸式、操作栏、侧滑)
        parseUiModel((null != getIntent() ? getIntent().getExtras() : null));
        // 生成用户UI视图
        View contentView = internalGetContentView(LayoutInflater.from(this), null, savedInstanceState, null);
        if (null != contentView) {
            // 构建UiModel视图,生成最终UI视图
            mUiModelImmersiveHelper.injectActivity(this, true);
            contentView = mUiModelActionBarHelper.injectView(contentView, initActionBarUI(), initActionBarMode());
            contentView = mUiModelSlideCloseHelper.injectView(contentView);
        } else {
            // 用户UI视图异常,重置UiModel操作类
            mUiModel = UiModel.UI_MODEL_NONE;
            mUiModelSlideCloseHelper = new UiModelSlideCloseHelper(false);
            mUiModelActionBarHelper = new UiModelActionBarHelper(false);
            mUiModelImmersiveHelper = new UiModelImmersiveHelper(false);
        }
        // 设置最终UI视图(防止contentView空导致setContentView异常,由上层后续自行调用)
        if (null != contentView) {
            setContentView(contentView);
        }
        // 回调通知UI视图创建完毕
        onContentViewCreated(contentView);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUiModelSlideCloseHelper.release();
    }

    @CallSuper
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mUiModelSlideCloseHelper.registerCallback(mSlideHelperSlidingScrollInterface, mSlideHelperSlidingCloseInterface);
    }

    /**
     * 调用系统onCreate方法,提供特殊需求直接调用,不走框架onCreate
     *
     * @param savedInstanceState bundle数据
     */
    public final void callSystemOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        }

        @Override
        public void onScrollEnded(boolean isCloseState) {

        }
    };

    private SlidingCloseableHelper.SlideHelperSlidingCloseInterface mSlideHelperSlidingCloseInterface = new SlidingCloseableHelper.SlideHelperSlidingCloseInterface() {
        @Override
        public void onSlidingClosed() {
            getStackHelperOfActivity().finishSelf();
        }
    };

}
