package com.lusen.cardola.framework.uibase.stack;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.lusen.cardola.framework.manager.JumperManager;
import com.lusen.cardola.framework.uibase.base.BaseActivity;
import com.lusen.cardola.framework.uibase.base.BaseFragment;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/7/26.
 * Activity内部Fragment栈管理帮助类
 */
public class StackHelperOfActivity {

    private FragmentActivity mActivity;
    private FragmentStackManager mFragmentStackManager;

    private ActivityLaunchParam mActivityLaunchParam;

    /**
     * 设置当Activity内Container容器空时,是否将Back事件继续传递给Activity
     */
    private boolean mBackToActivityWhenContainerEmpty = false;

    public StackHelperOfActivity(FragmentActivity activity) {
        mActivity = activity;
        mFragmentStackManager = new FragmentStackManager(activity.getSupportFragmentManager(), UIBaseUtil.generateActivityTag(mActivity));
    }

    public FragmentStackManager getFragmentStackManager() {
        return mFragmentStackManager;
    }

    /**
     * 初始化默认Activity的Stack属性
     *
     * @param containerResId 容器id
     * @param animEnterResId 进入动画资源id
     * @param animExitResId  退出动画资源id
     */
    public void initDefaultAttrs(int containerResId, int animEnterResId, int animExitResId) {
        mFragmentStackManager.initDefaultAttrs(containerResId, animEnterResId, animExitResId);
    }

    /**
     * 启动Fragment至自身Activity
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean launchFragmentOfSelf(Fragment fragment) {
        return launchFragmentOfSelf(fragment, null);
    }

    /**
     * 启动Fragment至自身Activity
     *
     * @param fragment fragment对象
     * @param param    fragment启动参数
     * @return 是否成功
     */
    public boolean launchFragmentOfSelf(Fragment fragment, FragmentLaunchParam param) {
        if (null != fragment && fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            baseFragment.getStackHelperOfFragment().setParentStackHelperOfActivity(this);
        }
        return mFragmentStackManager.launchFragment(fragment, param);
    }

    /**
     * 退出Fragment从自身Activity
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean finishFragmentOfSelf(Fragment fragment) {
        if (mBackToActivityWhenContainerEmpty) {
            int stackCount = mFragmentStackManager.getStackCount(null);
            // 当前Activity栈内仅存在一个Fragment,则直接退出Activity
            if (stackCount == 1) {
                finishSelf();
                return true;
            }
        }
        return mFragmentStackManager.destroyFragment(fragment);
    }

    /**
     * 启动Activity
     *
     * @param intent intent意图
     * @return 是否成功
     */

    public boolean launchActivity(Intent intent) {
        if (null != mActivityLaunchParam) {
            return JumperManager.launchActivity(mActivity, intent, mActivityLaunchParam.mLaunchEnterAnim, mActivityLaunchParam.mLaunchExitAnim);
        }
        return JumperManager.launchActivity(mActivity, intent);
    }

    /**
     * 启动Activity
     *
     * @param intent      intent意图
     * @param requestCode 请求code
     * @return 是否成功
     */
    public boolean launchActivityForResult(Intent intent, int requestCode) {
        if (null != mActivityLaunchParam) {
            return JumperManager.launchActivityForResult(mActivity, intent, requestCode, mActivityLaunchParam.mLaunchEnterAnim, mActivityLaunchParam.mLaunchExitAnim);
        }
        return JumperManager.launchActivityForResult(mActivity, intent, requestCode);
    }

    /**
     * 结束Activity
     *
     * @param activity activity对象
     * @return 是否成功
     */
    public boolean finishActivity(Activity activity) {
        if (null != mActivityLaunchParam) {
            return JumperManager.finishActivity(activity, mActivityLaunchParam.mFinishEnterAnim, mActivityLaunchParam.mFinishExitAnim);
        }
        return JumperManager.finishActivity(activity);
    }

    /**
     * 结束自身Activity
     *
     * @return 是否成功
     */
    public boolean finishSelf() {
        if (null != mActivityLaunchParam) {
            return JumperManager.finishActivity(mActivity, mActivityLaunchParam.mFinishEnterAnim, mActivityLaunchParam.mFinishExitAnim);
        }
        return JumperManager.finishActivity(mActivity);
    }

    /**
     * 获取自身Activity
     *
     * @return 是否成功
     */
    public FragmentActivity getSelf() {
        return mActivity;
    }

    /**
     * 显示指定容器id内前一个Fragment从自身Activity
     *
     * @param containerResId 容器id,null则表示总队列
     * @return 是否成功
     */
    public boolean showPreviousFragmentOfSelf(Integer containerResId) {
        return mFragmentStackManager.showPreviousFragment(containerResId);
    }

    /**
     * 隐藏指定容器id内前一个Fragment从自身Activity
     *
     * @param containerResId 容器id,null则表示总队列
     * @return 是否成功
     */
    public boolean hidePreviousFragmentOfSelf(Integer containerResId) {
        return mFragmentStackManager.hidePreviousFragment(containerResId);
    }

    /**
     * 回退指定容器id内顶部Fragment
     *
     * @param containerResId 容器id,null则表示总队列
     * @return 是否成功
     */
    public boolean backTopFragmentOfSelf(Integer containerResId) {
        return mFragmentStackManager.backTopFragment(containerResId);
    }

    /**
     * 清除指定容器id内所有Fragment
     *
     * @param containerResId 容器id,null则表示总队列
     * @return 是否成功
     */
    public boolean clearAllFragmentOfSelf(Integer containerResId) {
        return mFragmentStackManager.clearAllFragment(containerResId);
    }

    /**
     * 设置Activity默认启动属性(控制Activity启动行为,如进入退出动画)
     *
     * @param activityLaunchParam 启动属性
     */
    public void setActivityLaunchParam(ActivityLaunchParam activityLaunchParam) {
        mActivityLaunchParam = activityLaunchParam;
    }

    /**
     * Activity容器空时,是否将Back事件传递给Activity处理
     *
     * @return 是否传递
     */
    public boolean isBackToActivityWhenContainerEmpty() {
        return mBackToActivityWhenContainerEmpty;
    }

    /**
     * 设置Activity容器空时,是否将Back事件传递给Activity处理
     *
     * @param backToActivityWhenContainerEmpty 是否传递
     */
    public void setBackToActivityWhenContainerEmpty(boolean backToActivityWhenContainerEmpty) {
        mBackToActivityWhenContainerEmpty = backToActivityWhenContainerEmpty;
    }

    /**
     * 处理BackPressed事件传递
     *
     * @param back back数据
     */
    public final void performBackPressed(Back back) {
        // 当前元素类型BaseActivity,且拦截处理,将交予自身onBaseBackPressed处理
        if (mActivity instanceof BaseActivity) {
            BaseActivity selfActivity = (BaseActivity) mActivity;
            if (selfActivity.onBaseInterceptBackPressed(back)) {
                boolean consumedByActivity = selfActivity.onBaseBackPressed(back);
                if (!consumedByActivity) {
                    finishSelf();
                }
                return;
            }
        }
        int stackCount = mFragmentStackManager.getStackCount(null);
        // 存在子栈元素
        if (stackCount > 0) {
            Fragment topFragment = mFragmentStackManager.getTopFragment(null);
            if (null != topFragment) {
                // 子栈顶部元素类型为BaseFragment,则递归子元素
                if (topFragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) topFragment;
                    baseFragment.getStackHelperOfFragment().performBackPressed(back);
                }
                // 子栈顶部元素类型非BaseFragment,则结束子元素
                else {
                    finishFragmentOfSelf(topFragment);
                }
                // 原始子栈内仅存在单元素,在进行Back操作后,继续判断下是否仍存在子元素,如不存在,Activity尝试退出
                if (mBackToActivityWhenContainerEmpty) {
                    if (stackCount == 1) {
                        Fragment curTopFragment = mFragmentStackManager.getTopFragment(null);
                        if (null == curTopFragment) {
                            if (mActivity instanceof BaseActivity) {
                                BaseActivity baseActivity = (BaseActivity) mActivity;
                                boolean consumedByActivity = baseActivity.onBaseBackPressed(back);
                                if (!consumedByActivity) {
                                    finishSelf();
                                }
                            } else {
                                finishSelf();
                            }
                        }
                    }
                }
            }
        }
        // 不存在子栈元素,Activity尝试退出
        else {
            if (mActivity instanceof BaseActivity) {
                BaseActivity baseActivity = (BaseActivity) mActivity;
                boolean consumedByActivity = baseActivity.onBaseBackPressed(back);
                if (!consumedByActivity) {
                    finishSelf();
                }
            } else {
                finishSelf();
            }
        }
    }

}
