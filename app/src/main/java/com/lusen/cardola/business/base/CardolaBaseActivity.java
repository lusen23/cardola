package com.lusen.cardola.business.base;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.lusen.cardola.R;
import com.lusen.cardola.business.actionview.ActionViewFactory;
import com.lusen.cardola.business.actionview.ActionViewIcon;
import com.lusen.cardola.business.actionview.ActionViewTitle;
import com.lusen.cardola.framework.manager.AppManager;
import com.lusen.cardola.framework.manager.JumperManager;
import com.lusen.cardola.framework.uibase.UiBaseActivity;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uibase.stack.back.BackOrigin;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarLayout;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveHelper;
import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveMode;
import com.lusen.cardola.framework.uibase.ui.statusbar.StatusBarHelper;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;
import com.lusen.cardola.framework.util.ContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/16.
 */

public abstract class CardolaBaseActivity extends UiBaseActivity {

    // ActionView
    public ActionViewIcon mActionViewBack = null;
    public ActionViewTitle mActionViewTitle = null;
    // 状态栏遮盖控件
    private View mStatusBarShadowView;
    private volatile boolean mStatusBarModeUpdateConsumed = false;
    // 沉浸式自定义ImmersiveView
    private List<View> mImmersiveTopViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initBeforeOnCreate();
        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        internalUpdateStatusBarDark();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeStatusBarShadowView();
    }

    @Override
    protected void initBundle(@NonNull Bundle bundle) {
        super.initBundle(bundle);
    }

    @Override
    protected void onContentViewCreated(View view) {
        // 沉浸式设置
        List<View> fitViewsAtTop = new ArrayList<>();
        if (null != mImmersiveTopViews) {
            for (View immersiveTopView : mImmersiveTopViews) {
                if (null != immersiveTopView && !fitViewsAtTop.contains(immersiveTopView)) {
                    fitViewsAtTop.add(immersiveTopView);
                }
            }
        }
        ImmersiveMode immersiveMode = new ImmersiveMode();
        immersiveMode.mImmersiveStatusBar = true;
        immersiveMode.mImmersiveNavBar = false;
        immersiveMode.mImmersiveFeatureForV21 = false;
        mUiModelImmersiveHelper.setImmersiveView(fitViewsAtTop, null);
        mUiModelImmersiveHelper.initImmersive(immersiveMode);
        // ActionBar设置
        mUiModelActionBarHelper.setActionViewCallback(new ActionBarLayout.ActionViewCallback() {
            @Override
            public void onActionViewAdd(final ActionView actionView, ActionBarLayout.ActionContainer actionContainer) {
                actionView.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onActionViewClick(actionView);
                    }
                });
            }

            @Override
            public void onActionViewRemove(ActionView actionView, ActionBarLayout.ActionContainer actionContainer) {

            }
        });
        onActionViewCreated(mUiModelActionBarHelper);
        addStatusBarShadowView();
    }

    @Override
    protected int initUiModel() {
        return UiModel.UI_MODEL_ACTION_BAR | UiModel.UI_MODEL_IMMERSIVE;
    }

    @Override
    protected ActionBarHelper.ActionBarUI initActionBarUI() {
        return ActionBarHelper.ActionBarUI.UI_ONLY_DIVIDE;
    }

    @Override
    protected ActionBarHelper.ActionBarMode initActionBarMode() {
        return ActionBarHelper.ActionBarMode.MODE_DIVIDE;
    }

    @Override
    public boolean onBaseInterceptBackPressed(Back back) {
        return super.onBaseInterceptBackPressed(back);
    }

    @Override
    public void finish() {
        super.finish();
        UIBaseUtil.log("AppManager finish (activity) = %s", this);
        AppManager.getInstance().removeActivity(this);
    }

    /******************************************************************************************
     * 区块:系统方法
     ******************************************************************************************/

    @NonNull
    @Override
    public final LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }

    @Override
    public final View inflaterView(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup container) {
        return UIBaseUtil.inflaterView(getLayoutInflater(), layoutRes, container);
    }

    public FragmentManager getOptimizedFragmentManager() {
        FragmentManager fm = null;
        try {
            fm = getSupportFragmentManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fm;
    }

    public boolean showDialog(DialogFragment dialog) {
        return JumperManager.showDialog(this, dialog);
    }

    public boolean hideDialog(DialogFragment dialog) {
        return JumperManager.hideDialog(dialog);
    }

    /**
     * 直接退出Activity
     *
     * @return 是否成功
     */
    public boolean finishSelfActivity() {
        return getStackHelperOfActivity().finishSelf();
    }

    /**
     * 执行Back事件
     * 后续接管：当前Activity及栈内Fragment的各{@link com.lusen.cardola.framework.uibase.UiBaseFragment#onBaseBackPressed(Back)} 及 {@link com.lusen.cardola.framework.uibase.UiBaseFragment#onBaseInterceptBackPressed(Back)}所决定
     *
     * @param back back事件
     */
    public void performBackPressed(Back back) {
        getStackHelperOfActivity().performBackPressed(back);
    }

    /**
     * Back返回处理,返回true退出页面,否则消费处理(此行为由调用者进行决定,如Home页onKeyDown、左上角点击onClick)
     *
     * @param back back事件
     * @return 是否处理
     */
    @Override
    public boolean onBaseBackPressed(Back back) {
        if (back == null || null == back.mBackOrigin) {
            return false;
        }
        if (back.mBackOrigin == BackOrigin.BACK_FROM_SYSTEM) {
            return false;
        } else if (back.mBackOrigin == BackOrigin.BACK_FROM_CUSTOM) {
            return false;
        }
        return super.onBaseBackPressed(back);
    }

    /**
     * ActionView创建(业务层重写)
     *
     * @param helper ActionBar帮助类
     */
    public void onActionViewCreated(UiModelActionBarHelper helper) {
        mActionViewBack = (ActionViewIcon) ActionViewFactory.buildActionView(getLayoutInflater(), ActionViewFactory.BACK);
        mActionViewTitle = new ActionViewTitle(getLayoutInflater());
        mActionViewTitle.enableTitlePrimaryEllipsizeScroll(true);
        mActionViewTitle.setTitlePrimary(initActionBarTitle());
        helper.addActionViewToContainer(mActionViewBack, ActionBarLayout.ActionContainer.LEFT, true);
        helper.addActionViewToContainer(mActionViewTitle, ActionBarLayout.ActionContainer.CENTER, true);
    }

    /**
     * ActionView点击(业务层重写)
     *
     * @param actionView ActionView对象
     */
    public void onActionViewClick(ActionView actionView) {
        int id = actionView.getId();
        switch (id) {
            case ActionViewFactory.BACK:
                boolean result = onBaseBackPressed(new Back(BackOrigin.BACK_FROM_CUSTOM));
                if (!result) {
                    finishSelfActivity();
                }
                break;
            default:
                break;
        }
    }

    protected String initActionBarTitle() {
        return null;
    }

    protected boolean initStatusBarDark() {
        return false;
    }

    public final void updateActionBarTitle(String title) {
        if (null != mActionViewTitle) {
            mActionViewTitle.setTitlePrimary(title);
        }
    }

    public final void updateActionBarTitle(@StringRes int resId) {
        updateActionBarTitle(ContextUtil.getContext().getResources().getString(resId));
    }

    /**
     * 初始化自定义沉浸式TopViews
     * (务必在{@link CardolaBaseActivity#onContentViewCreated(View)})之前调用
     *
     * @param immersiveTopViews TopViews
     */
    public void initImmersiveTopViews(List<View> immersiveTopViews) {
        mImmersiveTopViews = immersiveTopViews;
    }

    /**
     * 初始化自定义沉浸式TopViews
     * (务必在{@link CardolaBaseActivity#onContentViewCreated(View)})之前调用
     *
     * @param immersiveTopViews TopViews
     */
    public void initImmersiveTopViews(View... immersiveTopViews) {
        if (null != immersiveTopViews && immersiveTopViews.length > 0) {
            mImmersiveTopViews = new ArrayList<>();
            for (View immersiveTopView : immersiveTopViews) {
                if (null != immersiveTopView) {
                    mImmersiveTopViews.add(immersiveTopView);
                }
            }
        } else {
            mImmersiveTopViews = null;
        }
    }

    public void addCoverView(View view) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void removeCoverView(View view) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        }
    }

    private void initBeforeOnCreate() {
        //硬件加速, 5.0上暂时关闭硬件加速，避免Unable to create layer错误。
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
    }

    private void addStatusBarShadowView() {
        if (null == mStatusBarShadowView) {
            mStatusBarShadowView = new View(this);
            addContentView(mStatusBarShadowView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ContextUtil.getContext().getResources().getDimensionPixelSize(R.dimen.uibase_immersive_custom_padding_top)));
        }
    }

    private void removeStatusBarShadowView() {
        try {
            if (null != mStatusBarShadowView) {
                ((ViewGroup) mStatusBarShadowView.getParent()).removeView(mStatusBarShadowView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatusBarShadowColor(boolean transparent) {
        if (null != mStatusBarShadowView) {
            if (transparent) {
                // 全透明
                mStatusBarShadowView.setBackgroundColor(0x00000000);
            } else {
                // 灰色底
                mStatusBarShadowView.setBackgroundColor(0x20000000);
            }
        }
    }

    private void internalUpdateStatusBarDark() {
        if (!mStatusBarModeUpdateConsumed) {
            mStatusBarModeUpdateConsumed = true;
            // 获取初始化状态栏模式
            boolean dark = initStatusBarDark();
            // 更新状态栏模式
            updateStatusBarDark(dark);
        }
    }

    /**
     * 系统状态栏背景及字体的颜色方案(动态更新,随时可调用)
     * <p>
     * 顶部非白色系页面:false
     * 顶部白色系页面:true
     *
     * @return 是否Dark
     */
    public void updateStatusBarDark(boolean dark) {
        boolean isStatusBarImmersive = ImmersiveHelper.isStatusBarImmersive(this);
        boolean supportBackgroundTransparent = StatusBarHelper.isStatusBarSupportBackgroundTransparent();
        boolean supportFontDark = StatusBarHelper.isStatusBarSupportFontDark();
        UIBaseUtil.log("updateStatusBarDark#%s#immersive,dark,supportBgTrans,supportFontDark=[%s,%s,%s,%s]", mTag, isStatusBarImmersive, dark, supportBackgroundTransparent, supportFontDark);
        // 当前状态栏沉浸式,才对状态栏背景及文字颜色进行适配优化
        if (isStatusBarImmersive) {
            boolean result = false;
            // 业务页面背景【白色系】
            if (dark) {
                // 检验:透底、黑字
                if (supportBackgroundTransparent && supportFontDark) {
                    result = (StatusBarHelper.setStatusBarBackground(this, true)
                            && StatusBarHelper.setStatusBarFont(this, true));
                    UIBaseUtil.log("updateStatusBarDark#%s#最优 [透底/黑字]", mTag);
                }
            }
            // 业务页面背景【非白色系】
            else {
                // 校验:透底
                if (supportBackgroundTransparent) {
                    result = (StatusBarHelper.setStatusBarBackground(this, true)
                            && StatusBarHelper.setStatusBarFont(this, false));
                    UIBaseUtil.log("updateStatusBarDark#%s#最优 [透底/白字]", mTag);
                }
            }
            // 最优方案无效,切换至兼容性方案:灰底/白字
            // 由于Android系统分化问题，各ROM厂商在沉浸式模式下，未遵守官方效果(半透/渐变透)，而是全透明
            // 针对这种情况，统一保底方案在StatusBar下方添加灰色底衬托系统白色字
            if (!result) {
                StatusBarHelper.setStatusBarBackground(this, false);
                StatusBarHelper.setStatusBarFont(this, false);
                // 优化:非白色系页面,自定义底透明
                boolean transparent = !dark;
                updateStatusBarShadowColor(transparent);
                if (transparent) {
                    UIBaseUtil.log("updateStatusBarDark#%s#保底 [默认/白字] %s", mTag, true);
                } else {
                    UIBaseUtil.log("updateStatusBarDark#%s#保底 [灰底/白字] %s", mTag, true);
                }
            } else {
                updateStatusBarShadowColor(true);
            }
        }
    }

}
