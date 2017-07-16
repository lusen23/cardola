package com.lusen.cardola.framework.uibase.stack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerUtil;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lusen.cardola.framework.uibase.base.BaseFragment;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 16/7/26.
 * Fragment栈管理器(核心类,主要用于Fragment栈的所有行为控制)
 */
public class FragmentStackManager {

    private FragmentManager mFragmentManager;
    private List<FragmentStackEntity> mFragmentStackEntities = Collections.synchronizedList(new ArrayList<FragmentStackEntity>());
    private Map<Integer, List<FragmentStackEntity>> mContainerStackEntities = Collections.synchronizedMap(new LinkedHashMap<Integer, List<FragmentStackEntity>>());
    private String mTag;

    /**
     * 默认预设值(提供给子fragment的缺省属性值)
     */
    private int mDefaultContainerResId;
    private int mDefaultAnimEnterResId = FragmentLaunchParam.ANIM_NONE;
    private int mDefaultAnimExitResId = FragmentLaunchParam.ANIM_NONE;

    public FragmentStackManager(FragmentManager fragmentManager, String tag) {
        mFragmentManager = fragmentManager;
        mTag = tag;
    }

    public void initDefaultAttrs(int containerResId, int animEnterResId, int animExitResId) {
        mDefaultContainerResId = containerResId;
        mDefaultAnimEnterResId = animEnterResId;
        mDefaultAnimExitResId = animExitResId;
        UIBaseUtil.log("FSM#%s#initDefaultAttrs (containerResId,animEnterResId,animExitResId) = %s,%s,%s", mTag, containerResId, animEnterResId, animExitResId);
    }

    public boolean launchFragment(Fragment fragment) {
        return launchFragment(fragment, null);
    }

    public boolean launchFragment(Fragment fragment, FragmentLaunchParam param) {
        if (!UIBaseUtil.isUIThread())
            throw new RuntimeException(String.format("FSM#%s#launchFragment must be called from main thread", mTag));
        try {
            UIBaseUtil.log(" \n");
            UIBaseUtil.log("FSM#%s#launchFragment begin ......", mTag);
            UIBaseUtil.log("FSM#%s#launchFragment (fragment) = %s", mTag, fragment);
            // Fragment为空
            if (null == fragment) {
                throw new RuntimeException(String.format("FSM#%s#launchFragment fragment must not be null", mTag));
            }
            // Fragment已存在
            if (isFragmentExist(fragment)) {
                throw new RuntimeException(String.format("FSM#%s#launchFragment fragment already exist", mTag));
            }
            // 解析获取最终LaunchParam,用于Fragment的启动控制
            LaunchParamResult result = parseLaunchParam(fragment, param);
            UIBaseUtil.log("FSM#%s#launchFragment (param) = %s", mTag, (null != result ? result.toString() : "NULL"));
            if (null != result) {
                // 判断LaunchFlag
                boolean flagClearAll = FragmentLaunchParam.isFlagClearAll(result.launchFlag);
                boolean flagClearTop = FragmentLaunchParam.isFlagClearTop(result.launchFlag);
                boolean flagSingleTop = FragmentLaunchParam.isFlagSingleTop(result.launchFlag);
                UIBaseUtil.log("FSM#%s#launchFragment (flagClearAll,flagClearTop,flagSingleTop) = %s,%s,%s", mTag, flagClearAll, flagClearTop, flagSingleTop);
                // 处理Flag:ClearAll
                if (flagClearAll) {
                    UIBaseUtil.log("FSM#%s#launchFragment 处理flagClearAll", mTag);
                    if (result.specifyContainer) {
                        handleClearAllFragment(result.containerResId);
                    } else {
                        handleClearAllFragment(null);
                    }
                }
                // 处理Flag:ClearTop
                else if (flagClearTop) {
                    UIBaseUtil.log("FSM#%s#launchFragment 处理flagClearTop", mTag);
                    if (result.specifyContainer) {
                        handleClearTopFragment(result.containerResId);
                    } else {
                        handleClearTopFragment(null);
                    }
                }
                // 处理Flag:SingleTop
                if (flagSingleTop) {
                    UIBaseUtil.log("FSM#%s#launchFragment 处理flagSingleTop", mTag);
                    if (result.specifyContainer) {
                        if (handleSingleTopFragment(result.containerResId, fragment)) {
                            UIBaseUtil.log("FSM#%s#launchFragment end-success-singletop ......", mTag);
                            return true;
                        }
                    } else {
                        if (handleSingleTopFragment(null, fragment)) {
                            UIBaseUtil.log("FSM#%s#launchFragment end-success-singletop ......", mTag);
                            return true;
                        }
                    }
                }
                // FragmentManager加入Fragment(确保真实FM操作无异常后,再执行后续生命周期逻辑)
                UIBaseUtil.log("FSM#%s#launchFragment FragmentManager操作!!!", mTag);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.setCustomAnimations(result.animEnterResId, result.animExitResId, result.animEnterResId, result.animExitResId);
                transaction.add(result.containerResId, fragment);
                transaction.addToBackStack(fragment.getTag());
                transaction.commitAllowingStateLoss();
                // 对当前TopFragment进行生命周期处理(onNewPause)
                if (result.specifyContainer) {
                    handleTopFragmentOnNewPause(result.containerResId);
                } else {
                    handleTopFragmentOnNewPause(null);
                }
                // 记录:加入FragmentStackEntity
                FragmentStackEntity entity = new FragmentStackEntity(fragment, result.containerResId, result.animEnterResId, result.animExitResId, result.launchFlag, result.specifyContainer);
                addFragmentEntityToStack(entity);
                // 隐藏PreviousFragment
//                hidePreviousFragment(entity.mContainerResId);
                UIBaseUtil.log("FSM#%s#launchFragment end-success ......", mTag);
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            UIBaseUtil.log("FSM#%s#launchFragment end-exception = %s", mTag, e.toString());
        }
        UIBaseUtil.log("FSM#%s#launchFragment end-failure ......", mTag);
        return false;
    }

    public boolean destroyFragment(Fragment fragment) {
        if (!UIBaseUtil.isUIThread())
            throw new RuntimeException(String.format("FSM#%s#destroyFragment must be called from main thread", mTag));
        UIBaseUtil.log(" \n");
        UIBaseUtil.log("FSM#%s#destroyFragment begin ......", mTag);
        UIBaseUtil.log("FSM#%s#destroyFragment (fragment) = %s", mTag, fragment);
        if (null != fragment && !mFragmentStackEntities.isEmpty()) {
            try {
                int size = mFragmentStackEntities.size();
                for (int index = size - 1; index >= 0; index--) {
                    FragmentStackEntity entity = mFragmentStackEntities.get(index);
                    if (entity.mFragment == fragment) {
                        // FragmentManager移除Fragment(确保真实FM操作无异常后,再执行后续生命周期逻辑)
                        UIBaseUtil.log("FSM#%s#destroyFragment FragmentManager操作!!!", mTag);
                        FragmentManagerUtil.finishFragmentByBackStackOrRemove(mFragmentManager, fragment, entity.mAnimEnterResId, entity.mAnimExitResId, entity.mAnimEnterResId, entity.mAnimExitResId, mTag);
                        // 判断当前CurFragment是否TopFragment
                        boolean curFragmentIsTopFragment;
                        if (entity.mSpecifyContainer) {
                            curFragmentIsTopFragment = isTopFragment(entity.mContainerResId, fragment);
                        } else {
                            curFragmentIsTopFragment = isTopFragment(null, fragment);
                        }
                        // 记录:移除FragmentStackEntity
                        removeFragmentEntityToStack(entity);
                        // 移除的是TopFragment,则触发当前TopFragment进行生命周期处理
                        if (curFragmentIsTopFragment) {
                            // 显示TopFragment
//                            showTopFragment(entity.mContainerResId);
                            // 对当前TopFragment进行生命周期处理(onNewResume)
                            if (entity.mSpecifyContainer) {
                                handleTopFragmentOnResume(entity.mContainerResId);
                            } else {
                                handleTopFragmentOnResume(null);
                            }
                        }
                        UIBaseUtil.log("FSM#%s#destroyFragment end-success ......", mTag);
                        return true;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                UIBaseUtil.log("FSM#%s#destroyFragment end-exception = %s", mTag, e.toString());
            }
        }
        UIBaseUtil.log("FSM#%s#destroyFragment end-failure ......", mTag);
        return false;
    }

    /**
     * 显示PreviousFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return 是否成功
     */
    public boolean showPreviousFragment(Integer containerResId) {
        Fragment fragment = getPreviousFragment(containerResId);
        return setFragmentVisibility(containerResId, fragment, View.VISIBLE);
    }

    /**
     * 隐藏PreviousFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return 是否成功
     */
    public boolean hidePreviousFragment(Integer containerResId) {
        Fragment fragment = getPreviousFragment(containerResId);
        return setFragmentVisibility(containerResId, fragment, View.GONE);
    }

    /**
     * 回退TopFragment
     *
     * @param containerResId containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return 是否成功
     */
    public boolean backTopFragment(Integer containerResId) {
        Fragment fragment = getTopFragment(containerResId);
        return destroyFragment(fragment);
    }

    /**
     * 清除AllFragment
     *
     * @param containerResId containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return 是否成功
     */
    public boolean clearAllFragment(Integer containerResId) {
        int stackCount = getStackCount(containerResId);
        while (stackCount > 0) {
            boolean result = backTopFragment(containerResId);
            if (!result) {
                return false;
            }
            stackCount--;
        }
        return true;
    }

    /**
     * 显示TopFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return 是否成功
     */
    public boolean showTopFragment(Integer containerResId) {
        Fragment fragment = getTopFragment(containerResId);
        return setFragmentVisibility(containerResId, fragment, View.VISIBLE);
    }

    /**
     * 隐藏TopFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return 是否成功
     */
    public boolean hideTopFragment(Integer containerResId) {
        Fragment fragment = getTopFragment(containerResId);
        return setFragmentVisibility(containerResId, fragment, View.GONE);
    }

    /**
     * fragment是否可见
     *
     * @param fragment fragment对象
     * @return fragment是否可见
     */
    public boolean isFragmentVisibile(Fragment fragment) {
        if (null != fragment) {
            View view = fragment.getView();
            if (null != view) {
                return view.getVisibility() == View.VISIBLE;
            }
        }
        return false;
    }

    /**
     * 设置fragment可见度
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @param fragment       fragment对象
     * @param visibility     可见度
     * @return 是否成功
     */
    public boolean setFragmentVisibility(Integer containerResId, Fragment fragment, int visibility) {
        // 全队列不为空
        if (null != fragment && !mFragmentStackEntities.isEmpty()) {
            // 指定队列
            if (null != containerResId) {
                List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(containerResId);
                if (null != fragmentStackEntities) {
                    for (FragmentStackEntity entity : fragmentStackEntities) {
                        if (entity.mFragment == fragment) {
                            View view = fragment.getView();
                            if (null != view) {
                                view.setVisibility(visibility);
                                return true;
                            }
                            break;
                        }
                    }
                }
            }
            // 全队列
            else {
                for (FragmentStackEntity entity : mFragmentStackEntities) {
                    if (entity.mFragment == fragment) {
                        View view = fragment.getView();
                        if (null != view) {
                            view.setVisibility(visibility);
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取PreviousFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return PreviousFragment
     */
    public Fragment getPreviousFragment(Integer containerResId) {
        // 全队列不为空
        if (!mFragmentStackEntities.isEmpty()) {
            // 指定队列
            if (null != containerResId) {
                List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(containerResId);
                if (null != fragmentStackEntities) {
                    int size = fragmentStackEntities.size();
                    if (size > 1) {
                        FragmentStackEntity entity = fragmentStackEntities.get(size - 2);
                        return entity.mFragment;
                    }
                }
            }
            // 全队列
            else {
                int size = mFragmentStackEntities.size();
                if (size > 1) {
                    FragmentStackEntity entity = mFragmentStackEntities.get(size - 2);
                    return entity.mFragment;
                }
            }
        }
        return null;
    }

    /**
     * 获取TopFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return TopFragment
     */
    public Fragment getTopFragment(Integer containerResId) {
        // 全队列不为空
        if (!mFragmentStackEntities.isEmpty()) {
            // 指定队列
            if (null != containerResId) {
                List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(containerResId);
                if (null != fragmentStackEntities) {
                    int size = fragmentStackEntities.size();
                    if (size > 0) {
                        FragmentStackEntity entity = fragmentStackEntities.get(size - 1);
                        return entity.mFragment;
                    }
                }
            }
            // 全队列
            else {
                int size = mFragmentStackEntities.size();
                if (size > 0) {
                    FragmentStackEntity entity = mFragmentStackEntities.get(size - 1);
                    return entity.mFragment;
                }
            }
        }
        return null;
    }

    /**
     * 是否TopFragment
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @param fragment       fragment对象
     * @return 是否TopFragment
     */
    public boolean isTopFragment(Integer containerResId, Fragment fragment) {
        // 全队列不为空,且fragment不为空
        if (!mFragmentStackEntities.isEmpty() && null != fragment) {
            // 指定队列
            if (null != containerResId) {
                List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(containerResId);
                if (null != fragmentStackEntities) {
                    int size = fragmentStackEntities.size();
                    if (size > 0) {
                        FragmentStackEntity entity = fragmentStackEntities.get(size - 1);
                        return entity.mFragment == fragment;
                    }
                }
            }
            // 全队列
            else {
                int size = mFragmentStackEntities.size();
                if (size > 0) {
                    FragmentStackEntity entity = mFragmentStackEntities.get(size - 1);
                    return entity.mFragment == fragment;
                }
            }
        }
        return false;
    }

    /**
     * 获取Stack总数
     *
     * @param containerResId 容器id 如果null,则全队列搜索;否则容器队列内搜索
     * @return Stack总数
     */
    public int getStackCount(Integer containerResId) {
        // 指定队列
        if (null != containerResId) {
            List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(containerResId);
            if (null != fragmentStackEntities) {
                return fragmentStackEntities.size();
            }
        }
        // 全队列
        else {
            return mFragmentStackEntities.size();
        }
        return 0;
    }

    /**
     * 获取Container总数
     *
     * @return Container总数
     */
    public int getContainerCount() {
        return mContainerStackEntities.size();
    }

    /**
     * Fragment是否存在
     *
     * @param fragment fragment对象
     * @return Fragment是否存在
     */
    public boolean isFragmentExist(Fragment fragment) {
        if (null != fragment && !mFragmentStackEntities.isEmpty()) {
            int size = mFragmentStackEntities.size();
            for (int index = size - 1; index >= 0; index--) {
                FragmentStackEntity entity = mFragmentStackEntities.get(index);
                if (entity.mFragment == fragment) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找容器id
     *
     * @param fragment fragment对象
     * @return 容器id
     */
    public Integer findContainerResIdByFragment(Fragment fragment) {
        if (null != fragment && !mFragmentStackEntities.isEmpty()) {
            int size = mFragmentStackEntities.size();
            for (int index = size - 1; index >= 0; index--) {
                FragmentStackEntity entity = mFragmentStackEntities.get(index);
                if (entity.mFragment == fragment) {
                    return entity.mContainerResId;
                }
            }
        }
        return null;
    }

    /************************************************************************************
     * 私有方法
     ************************************************************************************/

    private boolean handleSingleTopFragment(Integer containerResId, Fragment fragment) {
        Fragment topFragment = getTopFragment(containerResId);
        if (null != fragment && null != topFragment && fragment.getClass() == topFragment.getClass()) {
            if (topFragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) topFragment;
                baseFragment.onNewBundle(fragment.getArguments());
                return true;
            }
        }
        return false;
    }

    private void handleTopFragmentOnResume(Integer containerResId) {
        Fragment topFragment = getTopFragment(containerResId);
        if (null != topFragment && topFragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) topFragment;
            baseFragment.dispatchNewResume();
        }
    }

    private void handleTopFragmentOnNewPause(Integer containerResId) {
        Fragment topFragment = getTopFragment(containerResId);
        if (null != topFragment && topFragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) topFragment;
            baseFragment.dispatchNewPause();
        }
    }

    private void handleCurFragmentOnNewResume(Fragment fragment) {
        if (null != fragment && fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            baseFragment.dispatchNewResume();
        }
    }

    private void handleCurFragmentOnNewPause(Fragment fragment) {
        if (null != fragment && fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            baseFragment.dispatchNewPause();
        }
    }

    private void handleClearTopFragment(Integer containerResId) {
        Fragment topFragment = getTopFragment(containerResId);
        if (null != topFragment) {
            destroyFragment(topFragment);
        }
    }

    // TODO 待实现
    private void handleClearAllFragment(Integer containerResId) {
    }

    private void addFragmentEntityToStack(FragmentStackEntity entity) {
        if (null != entity) {
            mFragmentStackEntities.add(entity);
            List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(entity.mContainerResId);
            if (null == fragmentStackEntities) {
                fragmentStackEntities = new ArrayList<>();
            }
            fragmentStackEntities.add(entity);
            mContainerStackEntities.remove(entity.mContainerResId);
            mContainerStackEntities.put(entity.mContainerResId, fragmentStackEntities);
        }
    }

    private void removeFragmentEntityToStack(FragmentStackEntity entity) {
        if (null != entity) {
            mFragmentStackEntities.remove(entity);
            List<FragmentStackEntity> fragmentStackEntities = mContainerStackEntities.get(entity.mContainerResId);
            if (null != fragmentStackEntities) {
                fragmentStackEntities.remove(entity);
            }
        }
    }

    private LaunchParamResult parseLaunchParam(Fragment fragment, FragmentLaunchParam param) throws Exception {
        // 预设值
        int animEnterResId = FragmentLaunchParam.ANIM_NONE;
        int animExitResId = FragmentLaunchParam.ANIM_NONE;
        int launchFlag = FragmentLaunchParam.FRAGMENT_LAUNCH_STANDARD;
        boolean specifyContainer = false;
        int containerResId = 0;
        // 创建结果值
        LaunchParamResult result = new LaunchParamResult();
        // 从SelfFragment,读取启动属性【自身属性】
        if (fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            animEnterResId = baseFragment.getAnimEnterResId();
            animExitResId = baseFragment.getAnimExitResId();
            launchFlag = baseFragment.getLaunchFlag();
            specifyContainer = baseFragment.isSpecifyContainer();
        }
        // 从LaunchParam,读取启动属性【外部属性】
        if (null != param) {
            if (null != param.mAnimEnterResId) {
                animEnterResId = param.mAnimEnterResId;
            }
            if (null != param.mAnimExitResId) {
                animExitResId = param.mAnimExitResId;
            }
            if (null != param.mLaunchFlag) {
                launchFlag = param.mLaunchFlag;
            }
            if (null != param.mSpecifyContainer) {
                specifyContainer = param.mSpecifyContainer;
            }
            if (null != param.mContainerResId) {
                containerResId = param.mContainerResId;
            }
        }
        // 从ParentFragment,读取启动属性【父属性】
        if (containerResId <= 0) {
            containerResId = mDefaultContainerResId;
        }
        if (animEnterResId < 0) {
            if (animEnterResId == FragmentLaunchParam.ANIM_PARENT) {
                animEnterResId = mDefaultAnimEnterResId;
            }
        }
        if (animExitResId < 0) {
            if (animExitResId == FragmentLaunchParam.ANIM_PARENT) {
                animExitResId = mDefaultAnimExitResId;
            }
        }
        // 判断各启动属性有效性
        if (containerResId <= 0) {
            throw new RuntimeException(String.format("FSM#%s#launchFragment containerResId must > 0", mTag));
        }
        if (animEnterResId < 0) {
            throw new RuntimeException(String.format("FSM#%s#launchFragment animEnterResId must >= 0", mTag));
        }
        if (animExitResId < 0) {
            throw new RuntimeException(String.format("FSM#%s#launchFragment animExitResId must >= 0", mTag));
        }
        if (launchFlag <= 0) {
            launchFlag = FragmentLaunchParam.FRAGMENT_LAUNCH_STANDARD;
        }
        // 赋值
        result.animEnterResId = animEnterResId;
        result.animExitResId = animExitResId;
        result.launchFlag = launchFlag;
        result.specifyContainer = specifyContainer;
        result.containerResId = containerResId;
        return result;
    }

    class LaunchParamResult {
        int containerResId = 0;
        int animEnterResId = 0;
        int animExitResId = 0;
        int launchFlag = 0;
        boolean specifyContainer = false;

        @Override
        public String toString() {
            return "(containerResId,animEnterResId,animExitResId,launchFlag,specifyContainer) = "
                    + containerResId + "," + animEnterResId + "," + animExitResId + "," + launchFlag + "," + specifyContainer;
        }
    }

}
