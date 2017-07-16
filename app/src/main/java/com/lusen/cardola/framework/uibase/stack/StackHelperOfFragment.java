package com.lusen.cardola.framework.uibase.stack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManagerUtil;

import com.lusen.cardola.framework.uibase.base.BaseFragment;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

/**
 * Created by leo on 16/7/26.
 * Fragment内部Fragment栈管理帮助类
 */
public class StackHelperOfFragment {

    private Fragment mFragment;
    private FragmentStackManager mFragmentStackManager;

    private StackHelperOfFragment mParentStackHelperOfFragment;
    private StackHelperOfActivity mParentStackHelperOfActivity;

    public StackHelperOfFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public FragmentStackManager getFragmentStackManager() {
        return mFragmentStackManager;
    }

    /**
     * 初始化：务必在Fragment的有效生命周期之后,才可进行getChildFragmentManager方法调用,进行FragmentStackManager生成
     * 过早进行getChildFragmentManager调用会导致 {@link Fragment#instantiateChildFragmentManager} 生成时,mHost是Null值
     */
    public void initBeforeUse() {
        if (null == mFragmentStackManager) {
            mFragmentStackManager = new FragmentStackManager(mFragment.getChildFragmentManager(), UIBaseUtil.generateFragmentTag(mFragment));
        }
    }

    /**
     * 初始化默认Fragment的Stack属性
     *
     * @param containerResId 容器id
     * @param animEnterResId 进入动画资源id
     * @param animExitResId  退出动画资源id
     */
    public void initDefaultAttrs(int containerResId, int animEnterResId, int animExitResId) {
        if (null != mFragmentStackManager) {
            mFragmentStackManager.initDefaultAttrs(containerResId, animEnterResId, animExitResId);
        }
    }

    /**
     * 获取父Fragment的StackHelperOfFragment
     *
     * @return 父Fragment的StackHelperOfFragment
     */
    public StackHelperOfFragment getParentStackHelperOfFragment() {
        return mParentStackHelperOfFragment;
    }

    /**
     * 设置父Fragment的StackHelperOfFragment
     *
     * @param parentStackHelperOfFragment 父Fragment的StackHelperOfFragment
     */
    public void setParentStackHelperOfFragment(StackHelperOfFragment parentStackHelperOfFragment) {
        mParentStackHelperOfFragment = parentStackHelperOfFragment;
    }

    /**
     * 获取父Activity的StackHelperOfActivity
     *
     * @return 父Activity的StackHelperOfActivity
     */
    public StackHelperOfActivity getParentStackHelperOfActivity() {
        return mParentStackHelperOfActivity;
    }

    /**
     * 设置父Activity的StackHelperOfActivity
     *
     * @param parentStackHelperOfActivity 父Activity的StackHelperOfActivity
     */
    public void setParentStackHelperOfActivity(StackHelperOfActivity parentStackHelperOfActivity) {
        mParentStackHelperOfActivity = parentStackHelperOfActivity;
    }

    /**
     * 启动Fragment至自身Fragment
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean launchFragmentOfSelf(Fragment fragment) {
        return launchFragmentOfSelf(fragment, null);
    }

    /**
     * 启动Fragment至自身Fragment
     *
     * @param fragment fragment对象
     * @param param    fragment启动参数
     * @return 是否成功
     */
    public boolean launchFragmentOfSelf(Fragment fragment, FragmentLaunchParam param) {
        if (null == mFragmentStackManager) {
            return false;
        }
        if (null != fragment && fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            baseFragment.getStackHelperOfFragment().setParentStackHelperOfActivity(mParentStackHelperOfActivity);
            baseFragment.getStackHelperOfFragment().setParentStackHelperOfFragment(this);
        }
        return mFragmentStackManager.launchFragment(fragment, param);
    }

    /**
     * 退出Fragment从自身Fragment
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean finishFragmentOfSelf(Fragment fragment) {
        if (null == mFragmentStackManager) {
            return false;
        }
        return mFragmentStackManager.destroyFragment(fragment);
    }

    /**
     * 启动Fragment至父容器Activity
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean launchFragmentOfParentActivity(Fragment fragment) {
        return launchFragmentOfParentActivity(fragment, null);
    }

    /**
     * 启动Fragment至父容器Activity
     *
     * @param fragment fragment对象
     * @param param    fragment启动参数
     * @return 是否成功
     */
    public boolean launchFragmentOfParentActivity(Fragment fragment, FragmentLaunchParam param) {
        if (null != mParentStackHelperOfActivity) {
            return mParentStackHelperOfActivity.launchFragmentOfSelf(fragment, param);
        }
        return false;
    }

    /**
     * 退出Fragment从父容器Activity
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean finishFragmentOfParentActivity(Fragment fragment) {
        if (null != mParentStackHelperOfActivity) {
            return mParentStackHelperOfActivity.finishFragmentOfSelf(fragment);
        }
        return false;
    }

    /**
     * 启动Fragment至父容器Fragment
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean launchFragmentOfParentFragment(Fragment fragment) {
        return launchFragmentOfParentFragment(fragment, null);
    }

    /**
     * 启动Fragment至父容器Fragment
     *
     * @param fragment fragment对象
     * @param param    fragment启动参数
     * @return 是否成功
     */
    public boolean launchFragmentOfParentFragment(Fragment fragment, FragmentLaunchParam param) {
        if (null != mParentStackHelperOfFragment) {
            return mParentStackHelperOfFragment.launchFragmentOfSelf(fragment, param);
        }
        return false;
    }

    /**
     * 退出Fragment从父容器Fragment
     *
     * @param fragment fragment对象
     * @return 是否成功
     */
    public boolean finishFragmentOfParentFragment(Fragment fragment) {
        if (null != mParentStackHelperOfFragment) {
            return mParentStackHelperOfFragment.finishFragmentOfSelf(fragment);
        }
        return false;
    }

    /**
     * 退出自身Fragment
     *
     * @return 是否成功
     */
    public boolean finishSelf() {
        if (null != mParentStackHelperOfFragment) {
            return mParentStackHelperOfFragment.finishFragmentOfSelf(mFragment);
        } else if (null != mParentStackHelperOfActivity) {
            return mParentStackHelperOfActivity.finishFragmentOfSelf(mFragment);
        } else {
            // Fragment退出异常
            try {
                FragmentManagerUtil.finishFragmentByBackStackOrRemove(mFragment.getFragmentManager(), mFragment, 0, 0, 0, 0, UIBaseUtil.generateFragmentTag(mFragment));
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
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
     * 显示当前Fragment所在父容器id内的前一个Fragment从父容器(Fragment或Activity)
     *
     * @return 是否成功
     */
    public boolean showPreviousFragmentOfParent() {
        if (null != mParentStackHelperOfFragment) {
            Integer containerResId = mParentStackHelperOfFragment.getFragmentStackManager().findContainerResIdByFragment(mFragment);
            if (null != containerResId) {
                return mParentStackHelperOfFragment.showPreviousFragmentOfSelf(containerResId);
            }
        } else if (null != mParentStackHelperOfActivity) {
            Integer containerResId = mParentStackHelperOfActivity.getFragmentStackManager().findContainerResIdByFragment(mFragment);
            if (null != containerResId) {
                return mParentStackHelperOfActivity.showPreviousFragmentOfSelf(containerResId);
            }
        }
        return false;
    }

    /**
     * 隐藏当前Fragment所在父容器id内的前一个Fragment从父容器(Fragment或Activity)
     *
     * @return 是否成功
     */
    public boolean hidePreviousFragmentOfParent() {
        if (null != mParentStackHelperOfFragment) {
            Integer containerResId = mParentStackHelperOfFragment.getFragmentStackManager().findContainerResIdByFragment(mFragment);
            if (null != containerResId) {
                return mParentStackHelperOfFragment.hidePreviousFragmentOfSelf(containerResId);
            }
        } else if (null != mParentStackHelperOfActivity) {
            Integer containerResId = mParentStackHelperOfActivity.getFragmentStackManager().findContainerResIdByFragment(mFragment);
            if (null != containerResId) {
                return mParentStackHelperOfActivity.hidePreviousFragmentOfSelf(containerResId);
            }
        }
        return false;
    }

    /**
     * 显示指定容器id内前一个Fragment从自身Fragment
     *
     * @param containerResId 容器id,null则表示总队列
     * @return 是否成功
     */
    public boolean showPreviousFragmentOfSelf(Integer containerResId) {
        if (null == mFragmentStackManager) {
            return false;
        }
        return mFragmentStackManager.showPreviousFragment(containerResId);
    }

    /**
     * 隐藏指定容器id内前一个Fragment从自身Fragment
     *
     * @param containerResId 容器id,null则表示总队列
     * @return 是否成功
     */
    public boolean hidePreviousFragmentOfSelf(Integer containerResId) {
        if (null == mFragmentStackManager) {
            return false;
        }
        return mFragmentStackManager.hidePreviousFragment(containerResId);
    }

    /**
     * 获取自身Fragment
     *
     * @return 自身fragment
     */
    public Fragment getSelfFragment() {
        return mFragment;
    }

    /**
     * 处理BackPressed事件传递
     *
     * @param back back数据
     * @return true表示fragment层级树内已处理, 会finish掉目标元素fragment, false表示未处理,目标元素fragment拦截处理
     */
    public final boolean performBackPressed(Back back) {
        // 当前元素类型BaseFragment,且拦截处理,将交予自身onBaseBackPressed处理
        if (mFragment instanceof BaseFragment) {
            BaseFragment selfFragment = (BaseFragment) mFragment;
            if (selfFragment.onBaseInterceptBackPressed(back)) {
                boolean consumed = selfFragment.onBaseBackPressed(back);
                if (!consumed) {
                    return finishSelf();
                }
                return false;
            }
        }
        int stackCount = mFragmentStackManager.getStackCount(null);
        // 存在子栈顶部元素
        if (stackCount > 0) {
            Fragment childTopFragment = mFragmentStackManager.getTopFragment(null);
            if (null != childTopFragment) {
                // 子栈顶部元素类型为BaseFragment,则递归子元素
                if (childTopFragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) childTopFragment;
                    return baseFragment.getStackHelperOfFragment().performBackPressed(back);
                }
                // 子栈顶部元素类型非BaseFragment,则结束子元素
                else {
                    return finishFragmentOfSelf(childTopFragment);
                }
            }
        }
        // 不存在子栈顶部元素,当前元素已是路径树中叶子节点
        else if (mFragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) mFragment;
            // 当前元素类型为BaseFragment,则根据onBackPressed返回结果进行处理,false则结束当前元素
            boolean consumed = baseFragment.onBaseBackPressed(back);
            if (!consumed) {
                return finishSelf();
            }
            return false;
        }
        // 当前元素类型非BaseFragment,则结束当前元素
        return finishSelf();
    }

}
