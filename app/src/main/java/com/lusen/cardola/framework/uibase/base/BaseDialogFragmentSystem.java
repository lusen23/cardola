package com.lusen.cardola.framework.uibase.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveHelper;
import com.lusen.cardola.framework.uibase.ui.statusbar.StatusBarHelper;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;
import com.lusen.cardola.framework.util.ContextUtil;

import java.util.List;


public class BaseDialogFragmentSystem extends DialogFragment {

    public final String mTag = UIBaseUtil.generateDialogFragmentTag(this);
    // 状态栏遮盖控件
    private View mStatusBarShadowView;
    private volatile boolean mStatusBarModeUpdateConsumed = false;

    private BaseDialogFragmentDelegate mUIDialogFragmentDelegate;

    public BaseDialogFragmentSystem() {
        mUIDialogFragmentDelegate = new BaseDialogFragmentDelegate(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 开启埋点
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onCreateDialog(dialog);
        }
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onCreate(savedInstanceState);
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onViewCreated(view, savedInstanceState);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onStart();
        }
        addStatusBarShadowView();
        internalUpdateStatusBarDark();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onDestroy();
        }
        removeStatusBarShadowView();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onCancel(dialog);
        }
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.onDismiss(dialog);
        }
        super.onDismiss(dialog);
    }

    /**
     * 获取指定dialog宽
     *
     * @return dialog宽
     */
    public int getDialogSpecifyWidthPixels() {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.getDialogSpecifyWidthPixels();
        }
        return 0;
    }

    /**
     * 设置指定dialog宽  (0表示默认,负值表示全屏,正值则采用具体正值)
     *
     * @param dialogSpecifyWidthPixels dialog宽
     */
    public void setDialogSpecifyWidthPixels(int dialogSpecifyWidthPixels) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setDialogSpecifyWidthPixels(dialogSpecifyWidthPixels);
        }
    }

    /**
     * 获取指定dialog高
     *
     * @return dialog高
     */
    public int getDialogSpecifyHeightPixels() {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.getDialogSpecifyHeightPixels();
        }
        return 0;
    }

    /**
     * 设置指定dialog高  (0表示默认,负值表示全屏,正值则采用具体正值)
     *
     * @param dialogSpecifyHeightPixels dialog高
     */
    public void setDialogSpecifyHeightPixels(int dialogSpecifyHeightPixels) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setDialogSpecifyHeightPixels(dialogSpecifyHeightPixels);
        }
    }

    /**
     * 设置dialog按键监听器
     *
     * @param dialogOnKeyListener 按键监听器
     */
    public void setDialogOnKeyListener(DialogInterface.OnKeyListener dialogOnKeyListener) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setDialogOnKeyListener(dialogOnKeyListener);
        }
    }

    /**
     * 设置dialog关闭监听器
     *
     * @param dialogOnDismissListener 关闭监听器
     */
    public void setDialogOnDismissListener(DialogInterface.OnDismissListener dialogOnDismissListener) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setDialogOnDismissListener(dialogOnDismissListener);
        }
    }

    /**
     * 设置dialog取消监听器
     *
     * @param dialogOnCancelListener 取消监听器
     */
    public void setDialogOnCancelListener(DialogInterface.OnCancelListener dialogOnCancelListener) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setDialogOnCancelListener(dialogOnCancelListener);
        }
    }

    /**
     * 设置dialog生命周期回调
     *
     * @param dialogLifeCycleCallback 回调
     */
    public void setDialogLifeCycleCallback(BaseDialogFragmentDelegate.DialogLifeCycleCallback dialogLifeCycleCallback) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setDialogLifeCycleCallback(dialogLifeCycleCallback);
        }
    }

    /**
     * onStop态下是否关闭dialog
     *
     * @return 是否关闭
     */
    public boolean isHideWhenStop() {
        if (mUIDialogFragmentDelegate != null) {
            return mUIDialogFragmentDelegate.isHideWhenStop();
        }
        return false;
    }

    /**
     * 设置Dialog是否在onStop态关闭
     *
     * @param dismissWhenStop onStop态是否关闭dialog
     */
    public void setHideWhenStop(boolean dismissWhenStop) {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.setHideWhenStop(dismissWhenStop);
        }
    }

    /**
     * dialg是否显示
     *
     * @return 是否显示
     */
    public boolean isDialogShowing() {
        if (mUIDialogFragmentDelegate != null) {
            mUIDialogFragmentDelegate.isDialogShowing();
        }
        return false;
    }

    /**
     * 开启自身dialog
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean showSelf(Fragment fragment) {
        if (mUIDialogFragmentDelegate != null) {
            return mUIDialogFragmentDelegate.showSelf(fragment);
        }
        return false;
    }

    /**
     * 开启自身dialog
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean showSelf(android.support.v4.app.Fragment fragment) {
        if (mUIDialogFragmentDelegate != null) {
            return mUIDialogFragmentDelegate.showSelf(fragment);
        }
        return false;
    }

    /**
     * 开启自身dialog
     *
     * @param activity activity对象
     * @return 是否成功
     */
    public boolean showSelf(Activity activity) {
        if (mUIDialogFragmentDelegate != null) {
            return mUIDialogFragmentDelegate.showSelf(activity);
        }
        return false;
    }

    /**
     * 关闭自身dialog
     *
     * @return 是否成功
     */
    public boolean hideSelf() {
        if (mUIDialogFragmentDelegate != null) {
            return mUIDialogFragmentDelegate.hideSelf();
        }
        return false;
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
        Window window = getDialog().getWindow();
        boolean isStatusBarImmersive = ImmersiveHelper.isStatusBarImmersive(window);
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
                    result = (StatusBarHelper.setStatusBarBackground(window, true)
                            && StatusBarHelper.setStatusBarFont(window, true));
                    UIBaseUtil.log("updateStatusBarDark#%s#最优 [透底/黑字]", mTag);
                }
            }
            // 业务页面背景【非白色系】
            else {
                // 校验:透底
                if (supportBackgroundTransparent) {
                    result = (StatusBarHelper.setStatusBarBackground(window, true)
                            && StatusBarHelper.setStatusBarFont(window, false));
                    UIBaseUtil.log("updateStatusBarDark#%s#最优 [透底/白字]", mTag);
                }
            }
            // 最优方案无效,切换至兼容性方案:灰底/白字
            // 由于Android系统分化问题，各ROM厂商在沉浸式模式下，未遵守官方效果(半透/渐变透)，而是全透明
            // 针对这种情况，统一保底方案在StatusBar下方添加灰色底衬托系统白色字
            if (!result) {
                StatusBarHelper.setStatusBarBackground(window, false);
                StatusBarHelper.setStatusBarFont(window, false);
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

    /**
     * 初始化UiModel
     *
     * @return UiModel {@link UiModel}
     */
    protected int initUiModel() {
        return UiModel.UI_MODEL_NONE;
    }

    /**
     * 初始化沉浸式TopView
     *
     * @return 沉浸式TopView
     */
    protected List<View> initImmersiveFitViewsAtTop() {
        return null;
    }

    /**
     * 初始化沉浸式BottomView
     *
     * @return 沉浸式BottomView
     */
    protected List<View> initImmersiveFitViewsAtBottom() {
        return null;
    }

    /**
     * 是否忽略状态栏背景及字体颜色方案
     * 只有布局延伸至状态栏顶部时，需要考虑。例如中心弹窗，可完全忽略掉系统状态栏变化
     *
     * @return 是否忽略
     */
    protected boolean ignoreStatusBarDark() {
        return true;
    }

    /**
     * 系统状态栏背景及字体的颜色方案(初始化指定)
     * <p>
     * 顶部非白色系页面:false(默认)
     * 顶部白色系页面:true
     *
     * @return 是否Dark
     */
    protected boolean initStatusBarDark() {
        return false;
    }

    private void internalUpdateStatusBarDark() {
        if (!mStatusBarModeUpdateConsumed) {
            mStatusBarModeUpdateConsumed = true;
            // 忽略系统状态栏兼容方案
            if (ignoreStatusBarDark())
                return;
            // 获取初始化状态栏模式
            boolean dark = initStatusBarDark();
            // 更新状态栏模式
            updateStatusBarDark(dark);
        }
    }

    private void addStatusBarShadowView() {
        if (null == mStatusBarShadowView) {
            Window window = getDialog().getWindow();
            mStatusBarShadowView = new View(window.getContext());
            window.addContentView(mStatusBarShadowView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ContextUtil.getContext().getResources().getDimensionPixelSize(R.dimen.uibase_immersive_custom_padding_top)));
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

}
