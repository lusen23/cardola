package com.lusen.cardola.business.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.business.actionview.ActionViewFactory;
import com.lusen.cardola.business.actionview.ActionViewIcon;
import com.lusen.cardola.business.actionview.ActionViewTitle;
import com.lusen.cardola.framework.manager.JumperManager;
import com.lusen.cardola.framework.uibase.UiBaseFragment;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uibase.stack.back.BackOrigin;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarLayout;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/22.
 */

public abstract class CardolaBaseFragment extends UiBaseFragment {

    // 宿主Activity引用
    private WeakReference<? extends Activity> mWeakHostActivity;
    // ActionView
    public ActionViewIcon mActionViewBack = null;
    public ActionViewTitle mActionViewTitle = null;
    // 沉浸式自定义ImmersiveView
    private List<View> mImmersiveTopViews;

    private LayoutInflater mLayoutInflater;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mWeakHostActivity = new WeakReference<>(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mWeakHostActivity) {
            mWeakHostActivity.clear();
            mWeakHostActivity = null;
        }
    }

    @Override
    public final LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        // 重写系统getLayoutInflater,获取首次系统给予的LayoutInflater
        if (null == mLayoutInflater) {
            mLayoutInflater = getHostActivityIfExist().getLayoutInflater();
        }
        return mLayoutInflater;
    }

    @NonNull
    public final LayoutInflater getLayoutInflater() {
        return getLayoutInflater(null);
    }

    public final View inflaterView(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup container) {
        return UIBaseUtil.inflaterView(getLayoutInflater(), layoutRes, container);
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
        mUiModelImmersiveHelper.setImmersiveView(fitViewsAtTop, null);
        mUiModelImmersiveHelper.updateImmersive();
        // ActionView设置
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
    }

    @Override
    public int initUiModel() {
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

    public final Activity getHostActivityIfExist() {
        Activity activity = null;
        if (null != mWeakHostActivity) {
            activity = mWeakHostActivity.get();
        }
        if (null == activity) {
            activity = getActivity();
        }
        return activity;
    }

    public boolean showDialog(DialogFragment dialog) {
        return JumperManager.showDialog(getHostActivityIfExist(), dialog);
    }

    public boolean hideDialog(DialogFragment dialog) {
        return JumperManager.hideDialog(dialog);
    }

    protected boolean initStatusBarDark() {
        return false;
    }

    public boolean finishSelfFragment() {
        return getStackHelperOfFragment().finishSelf();
    }

    public void performBackPressed(Back back) {
        getStackHelperOfFragment().performBackPressed(back);
    }

    @Override
    public boolean onBaseInterceptBackPressed(Back back) {
        return super.onBaseInterceptBackPressed(back);
    }

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

    public void onActionViewCreated(UiModelActionBarHelper helper) {
        mActionViewBack = (ActionViewIcon) ActionViewFactory.buildActionView(getLayoutInflater(), ActionViewFactory.BACK);
        mActionViewTitle = new ActionViewTitle(getLayoutInflater(), initActionBarTitle());
        mActionViewTitle.enableTitlePrimaryEllipsizeScroll(true);
        helper.addActionViewToContainer(mActionViewBack, ActionBarLayout.ActionContainer.LEFT, true);
        helper.addActionViewToContainer(mActionViewTitle, ActionBarLayout.ActionContainer.CENTER, true);
    }

    public void onActionViewClick(ActionView actionView) {
        int id = actionView.getId();
        switch (id) {
            case ActionViewFactory.BACK:
                boolean result = onBaseBackPressed(new Back(BackOrigin.BACK_FROM_CUSTOM));
                if (!result) {
                    finishSelfFragment();
                }
                break;
            default:
                break;
        }
    }

    public String initActionBarTitle() {
        return null;
    }

    public final void updateActionBarTitle(String title) {
        if (null != mActionViewTitle) {
            mActionViewTitle.setTitlePrimary(title);
        }
    }

    /**
     * 初始化自定义沉浸式TopViews
     *
     * @param immersiveTopViews TopViews
     */
    public void initImmersiveTopViews(List<View> immersiveTopViews) {
        mImmersiveTopViews = immersiveTopViews;
    }

    /**
     * 初始化自定义沉浸式TopViews
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

    public final FragmentManager getOptimizedFragmentManager() {
        FragmentManager fragmentManager = null;
        try {
            fragmentManager = this.getChildFragmentManager();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return fragmentManager;
    }

    public final FragmentManager getParentFragmentManager() {
        FragmentManager fragmentManager = null;
        try {
            fragmentManager = this.getFragmentManager();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return fragmentManager;
    }

}
