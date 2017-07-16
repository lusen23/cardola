package android.support.v4.app;

import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.util.ArrayList;

/**
 * Created by leo on 16/10/18.
 * FragmentManagerImpl内部工具类
 */
public class FragmentManagerUtil {

    /**
     * 获取所有added的fragment队列
     *
     * @param fragmentManager fm管理器
     * @return fragment队列
     */
    public static ArrayList<Fragment> getAddedAllFragment(FragmentManager fragmentManager) {
        return getAddedAllFragment(fragmentManager, null);
    }

    /**
     * 获取所有added的fragment队列
     *
     * @param fragmentManager fm管理器
     * @param containerId     容器id(null则全部)
     * @return fragment队列
     */
    public static ArrayList<Fragment> getAddedAllFragment(FragmentManager fragmentManager, Integer containerId) {
        ArrayList<Fragment> fragmentList = ((FragmentManagerImpl) fragmentManager).mAdded;
        if (null != containerId && null != fragmentList) {
            ArrayList<Fragment> containerFragmentList = new ArrayList<>();
            for (Fragment fragment : fragmentList) {
                if (fragment.mContainerId == containerId) {
                    containerFragmentList.add(fragment);
                }
            }
            return containerFragmentList;
        }
        return fragmentList;
    }

    /**
     * 获取顶部added的Fragment
     *
     * @param fragmentManager fm管理器
     * @return fragment对象
     */
    public static Fragment getAddedTopFragment(FragmentManager fragmentManager) {
        FragmentManagerImpl impl = ((FragmentManagerImpl) fragmentManager);
        if (null != impl && null != impl.mAdded && impl.mAdded.size() > 0) {
            int addCount = impl.mAdded.size();
            Fragment fragment = impl.mAdded.get(addCount - 1);
            return fragment;
        }
        return null;
    }

    /**
     * 是否added队列内Fragment
     *
     * @param fragmentManager fm管理器
     * @return 是否added队列内Fragment
     */
    public static boolean isAddedFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentManagerImpl impl = ((FragmentManagerImpl) fragmentManager);
        if (null != impl && null != impl.mAdded && impl.mAdded.size() > 0) {
            return impl.mAdded.contains(fragment);
        }
        return false;
    }

    /**
     * 获取顶部backstack的Fragment
     *
     * @param fragmentManager fm管理器
     * @return fragment对象
     */
    public static Fragment getStackTopFragment(FragmentManager fragmentManager) {
        FragmentManagerImpl impl = ((FragmentManagerImpl) fragmentManager);
        if (null != impl && null != impl.mBackStack && impl.mBackStack.size() > 0) {
            int stackCount = impl.mBackStack.size();
            final BackStackRecord bss = impl.mBackStack.get(stackCount - 1);
            BackStackRecord.Op option = bss.mOps.get(bss.mOps.size() - 1);
            Fragment fragment = option.fragment;
            return fragment;
        }
        return null;
    }

    /**
     * 获取backstack的fragment索引
     *
     * @param fragmentManager fm管理器
     * @param fragment        fragment对象
     * @return 索引index,-1表示不存在backstack中
     */
    public static int getStackFragmentIndex(FragmentManager fragmentManager, Fragment fragment) {
        int index = -1;
        FragmentManagerImpl impl = ((FragmentManagerImpl) fragmentManager);
        if (null != impl && null != impl.mBackStack && impl.mBackStack.size() > 0) {
            int stackCount = impl.mBackStack.size();
            for (int stackIndex = stackCount - 1; stackIndex >= 0; stackIndex--) {
                BackStackRecord backStackRecord = impl.mBackStack.get(stackIndex);
                BackStackRecord.Op option = backStackRecord.mOps.get(backStackRecord.mOps.size() - 1);
                Fragment stackFragment = option.fragment;
                if (stackFragment == fragment) {
                    index = stackIndex;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * 获取backstack的总数
     *
     * @param fragmentManager fm管理器
     * @return stack栈总数
     */
    public static int getStackFragmentCount(FragmentManager fragmentManager) {
        FragmentManagerImpl impl = ((FragmentManagerImpl) fragmentManager);
        if (null != impl && null != impl.mBackStack) {
            return impl.mBackStack.size();
        }
        return 0;
    }


    /**
     * 销毁Fragment通过pop或remove模式,解决BackStack栈内非顶部Fragment及非栈内Fragment的销毁兼容性问题
     *
     * @param fragmentManager fm管理器
     * @param fragment        fragment对象
     * @param enter           进入动画
     * @param exit            退出动画
     * @param popEnter        pop进入动画
     * @param popExit         pop退出动画
     * @param tag             tag标签
     */
    public static void finishFragmentByBackStackOrRemove(FragmentManager fragmentManager, Fragment fragment,
                                                         int enter, int exit, int popEnter, int popExit, String tag) {
        FragmentManagerImpl impl = ((FragmentManagerImpl) fragmentManager);
        int stackCount = 0;
        if (null != impl && null != impl.mBackStack) {
            stackCount = impl.mBackStack.size();
        }
        UIBaseUtil.log("FMU#%s#finishFragmentByBackStackOrRemove 准备 (fragment,count) = %s,%s", tag, fragment, stackCount);
        // 存在BackStack栈
        if (null != impl && null != impl.mBackStack && stackCount > 0) {
            for (int stackIndex = stackCount - 1; stackIndex >= 0; stackIndex--) {
                BackStackRecord backStackRecord = impl.mBackStack.get(stackIndex);
                BackStackRecord.Op option = backStackRecord.mOps.get(backStackRecord.mOps.size() - 1);
                Fragment stackFragment = option.fragment;
                // fragment命中
                if (stackFragment == fragment) {
                    // 顶部
                    if (stackIndex == stackCount - 1) {
                        UIBaseUtil.log("FMU#%s#finishFragmentByBackStackOrRemove 栈内命中,顶部,pop模式 (index,count) = %s,%s", tag, stackIndex, stackCount);
                        // pop模式
                        fragmentManager.popBackStackImmediate();
                    }
                    // 非顶部
                    else {
                        UIBaseUtil.log("FMU#%s#finishFragmentByBackStackOrRemove 栈内命中,非顶部,remove模式 (index,count) = %s,%s", tag, stackIndex, stackCount);
                        // 移除BackStack的记录,维护BackStack顺序正确性
                        impl.mBackStack.remove(backStackRecord);
                        // remove模式
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
                        transaction.remove(fragment);
                        transaction.commitAllowingStateLoss();
                    }
                    return;
                }
            }
        }
        UIBaseUtil.log("FMU#%s#finishFragmentByBackStackOrRemove 不存在栈内,remove模式", tag);
        // 不存在BackStack栈,则直接Remove
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

}
