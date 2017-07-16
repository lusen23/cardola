package com.lusen.cardola.framework.uibase.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lusen.cardola.framework.manager.JumperManager;
import com.lusen.cardola.framework.uibase.UiModel;
import com.lusen.cardola.framework.uibase.UiModelImmersiveHelper;
import com.lusen.cardola.framework.uibase.ui.immersive.ImmersiveMode;


/**
 * Created by kabing on 2017/3/6.
 *
 * @author kabing
 * @date 2017/03/06
 */

public class BaseDialogFragmentDelegate {

    private BaseDialogFragmentSystem mDialogFragment;


    private int mUiModel = UiModel.UI_MODEL_NONE;

    private UiModelImmersiveHelper mUiModelImmersiveHelper = new UiModelImmersiveHelper(false);

    private boolean mIsUiModelImmersiveSupported;

    // Dialog 接口
    private DialogInterface.OnKeyListener mDialogOnKeyListener;
    private DialogInterface.OnDismissListener mDialogOnDismissListener;
    private DialogInterface.OnCancelListener mDialogOnCancelListener;
    private DialogLifeCycleCallback mDialogLifeCycleCallback;
    // Dialog指定宽高,0表示默认,负值表示全屏,正值则采用具体正值
    private int mDialogSpecifyWidthPixels;
    private int mDialogSpecifyHeightPixels;
    // onStop态下是否关闭dialog
    private boolean mHideWhenStop = false;

    public BaseDialogFragmentDelegate(BaseDialogFragmentSystem dialogFragment) {
        mDialogFragment = dialogFragment;
    }


    public void onCreateDialog(Dialog dialog) {
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (null != mDialogOnKeyListener) {
                    return mDialogOnKeyListener.onKey(dialogInterface, keyCode, keyEvent);
                }
                return false;
            }
        });
    }

    private Bundle getArguments() {
        return mDialogFragment.getArguments();
    }


    void onCreate(Bundle savedInstanceState) {
        // 初始化Bundle
        initBundle(getArguments());
        // 解析UiModel(准备构建UI样式模式:沉浸式、操作栏、侧滑)
        parseUiModel(getArguments());
    }

    private Activity getActivity() {
        return mDialogFragment.getActivity();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mIsUiModelImmersiveSupported && null != getDialog()) {
            // 沉浸式设置(设置padding及height)
            mUiModelImmersiveHelper.injectActivity(getActivity(), true);
            ImmersiveMode immersiveMode = new ImmersiveMode();
            immersiveMode.mImmersiveStatusBar = true;
            immersiveMode.mImmersiveNavBar = false;
            immersiveMode.mImmersiveFeatureForV21 = false;
            mUiModelImmersiveHelper.setImmersiveView(mDialogFragment.initImmersiveFitViewsAtTop(), mDialogFragment.initImmersiveFitViewsAtBottom());
            mUiModelImmersiveHelper.initImmersive(immersiveMode);
            // 设置沉浸式属性
            Window window = getDialog().getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (immersiveMode.mImmersiveFeatureForV21) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        if (null != mDialogLifeCycleCallback) {
            if (mDialogFragment != null) {
                mDialogLifeCycleCallback.onViewCreated(mDialogFragment, view, savedInstanceState);
            }
        }
    }

    void onStart() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int defaultDialagWidth = getDialog().getWindow().getAttributes().width;
            int defaultDialogHeight = getDialog().getWindow().getAttributes().height;
            int specifyDialogWidth = defaultDialagWidth;
            int specifyDialogHeight = defaultDialogHeight;
            if (mDialogSpecifyWidthPixels < 0) {
                specifyDialogWidth = dm.widthPixels;
            } else if (mDialogSpecifyWidthPixels > 0) {
                specifyDialogWidth = mDialogSpecifyWidthPixels;
            } else {
                specifyDialogWidth = defaultDialagWidth;
            }
            if (mDialogSpecifyHeightPixels < 0) {
                specifyDialogHeight = dm.heightPixels;
            } else if (mDialogSpecifyHeightPixels > 0) {
                specifyDialogHeight = mDialogSpecifyHeightPixels;
            } else {
                specifyDialogHeight = defaultDialogHeight;
            }
            getDialog().getWindow().setLayout(specifyDialogWidth, specifyDialogHeight);
        } catch (Exception e) {
        }
    }


    /**
     * 关闭自身dialog
     *
     * @return 是否成功
     */
    boolean hideSelf() {
        return JumperManager.hideDialog(mDialogFragment);
    }

    void onStop() {
        if (mHideWhenStop) {
            hideSelf();
        }
    }

    void onDestroy() {
        //do nothing
    }

    void onCancel(DialogInterface dialog) {
        if (null != mDialogOnCancelListener) {
            mDialogOnCancelListener.onCancel(dialog);
        }
    }

    void onDismiss(DialogInterface dialog) {
        if (null != mDialogOnDismissListener) {
            mDialogOnDismissListener.onDismiss(dialog);
        }
    }

    /**
     * 获取指定dialog宽
     *
     * @return dialog宽
     */
    int getDialogSpecifyWidthPixels() {
        return mDialogSpecifyWidthPixels;
    }

    /**
     * 设置指定dialog宽  (0表示默认,负值表示全屏,正值则采用具体正值)
     *
     * @param dialogSpecifyWidthPixels dialog宽
     */
    void setDialogSpecifyWidthPixels(int dialogSpecifyWidthPixels) {
        mDialogSpecifyWidthPixels = dialogSpecifyWidthPixels;
    }

    /**
     * 获取指定dialog高
     *
     * @return dialog高
     */
    int getDialogSpecifyHeightPixels() {
        return mDialogSpecifyHeightPixels;
    }

    /**
     * 设置指定dialog高  (0表示默认,负值表示全屏,正值则采用具体正值)
     *
     * @param dialogSpecifyHeightPixels dialog高
     */
    void setDialogSpecifyHeightPixels(int dialogSpecifyHeightPixels) {
        mDialogSpecifyHeightPixels = dialogSpecifyHeightPixels;
    }

    /**
     * 设置dialog按键监听器
     *
     * @param dialogOnKeyListener 按键监听器
     */
    void setDialogOnKeyListener(DialogInterface.OnKeyListener dialogOnKeyListener) {
        mDialogOnKeyListener = dialogOnKeyListener;
    }

    /**
     * 设置dialog关闭监听器
     *
     * @param dialogOnDismissListener 关闭监听器
     */
    void setDialogOnDismissListener(DialogInterface.OnDismissListener dialogOnDismissListener) {
        mDialogOnDismissListener = dialogOnDismissListener;
    }

    /**
     * 设置dialog取消监听器
     *
     * @param dialogOnCancelListener 取消监听器
     */
    void setDialogOnCancelListener(DialogInterface.OnCancelListener dialogOnCancelListener) {
        mDialogOnCancelListener = dialogOnCancelListener;
    }

    /**
     * 设置dialog生命周期回调
     *
     * @param dialogLifeCycleCallback 回调
     */
    void setDialogLifeCycleCallback(DialogLifeCycleCallback dialogLifeCycleCallback) {
        mDialogLifeCycleCallback = dialogLifeCycleCallback;
    }

    /**
     * onStop态下是否关闭dialog
     *
     * @return 是否关闭
     */
    boolean isHideWhenStop() {
        return mHideWhenStop;
    }

    /**
     * 设置Dialog是否在onStop态关闭
     *
     * @param dismissWhenStop onStop态是否关闭dialog
     */
    void setHideWhenStop(boolean dismissWhenStop) {
        mHideWhenStop = dismissWhenStop;
    }

    /**
     * dialg是否显示
     *
     * @return 是否显示
     */
    boolean isDialogShowing() {
        return JumperManager.isDialogShowing(mDialogFragment);
    }

    /**
     * 开启自身dialog
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean showSelf(android.support.v4.app.Fragment fragment) {
        if (null != fragment) {
            if (mDialogFragment != null) {
                return JumperManager.showDialog(fragment.getActivity(), mDialogFragment);
            }
        }
        return false;
    }


    /**
     * 开启自身dialog
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    boolean showSelf(Fragment fragment) {
        if (null != fragment) {
            if (mDialogFragment != null) {
                return JumperManager.showDialog(fragment.getActivity(), mDialogFragment);
            }
        }
        return false;
    }


    /**
     * 开启自身dialog
     *
     * @param activity activity对象
     * @return 是否成功
     */
    boolean showSelf(Activity activity) {
        if (mDialogFragment != null) {
            return JumperManager.showDialog(activity, mDialogFragment);
        }
        return false;
    }


    private Dialog getDialog() {

        return mDialogFragment.getDialog();
    }

    /**
     * 解析UiModel(用于Fragment最终uimodel解析获取)
     * 优先外部bundle,其次内部{@link BaseDialogFragmentSystem#initUiModel()}
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
            if (mDialogFragment != null) {
                mUiModel = mDialogFragment.initUiModel();
            }
        }
        mIsUiModelImmersiveSupported = UiModel.isUiModelImmersiveSupported(mUiModel);
        mUiModelImmersiveHelper = new UiModelImmersiveHelper(mIsUiModelImmersiveSupported);
    }


    /**
     * 初始化Bundle(用于数据解析,在框架生命周期最前期执行)
     *
     * @param bundle bundle数据
     */
    private void initBundle(Bundle bundle) {

    }


    public static class DialogLifeCycleCallback {
        public void onViewCreated(BaseDialogFragmentSystem dialog, View view, Bundle bundle) {
        }
    }
}
