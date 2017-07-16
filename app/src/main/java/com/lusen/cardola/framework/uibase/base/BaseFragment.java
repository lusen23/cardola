package com.lusen.cardola.framework.uibase.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lusen.cardola.framework.uibase.stack.FragmentLaunchParam;
import com.lusen.cardola.framework.uibase.stack.StackHelperOfActivity;
import com.lusen.cardola.framework.uibase.stack.StackHelperOfFragment;
import com.lusen.cardola.framework.uibase.stack.back.Back;
import com.lusen.cardola.framework.uibase.util.UIBaseUtil;

import java.lang.reflect.Field;

/**
 * Created by leo on 16/7/22.
 * 基类Fragment(v4-support)
 * 具体生命周期参考 https://developer.android.com/guide/components/fragments.html
 */
public class BaseFragment extends Fragment {

    public final String mTag = UIBaseUtil.generateFragmentTag(this);

    /**
     * Fragment本身启动属性：启动Flag、是否指定Container、进入动画、退出动画
     */
    private int mLaunchFlag = FragmentLaunchParam.FRAGMENT_LAUNCH_STANDARD;
    private boolean mSpecifyContainer = false;
    private int mAnimEnterResId = FragmentLaunchParam.ANIM_PARENT;
    private int mAnimExitResId = FragmentLaunchParam.ANIM_PARENT;
    private boolean isVisibleToUser = false;//在onNewResume 和 onNewPause之间的状态既为可见，并且为确保这两个方法只会被执行一次

    private StackHelperOfFragment mStackHelperOfFragment;

    /************************************************************************************
     * 生命周期
     ************************************************************************************/

    /**
     * 请使用{@link #dispatchNewPause()} 确保不会被重复调用，否则会影响页面埋点
     */
    protected void onNewPause() {
        UIBaseUtil.log("LifeCycle#%s#onNewPause", mTag);
    }

    /**
     * 请使用{@link #dispatchNewResume()} 确保不会被重复调用，否则会影响页面埋点
     */
    protected void onNewResume() {
        UIBaseUtil.log("LifeCycle#%s#onNewResume", mTag);
    }

    public void dispatchNewResume() {
        if (!isVisibleToUser) {
            isVisibleToUser = true;
            onNewResume();
        }
    }

    public void dispatchNewPause() {
        if (isVisibleToUser) {
            isVisibleToUser = false;
            onNewPause();
        }
    }

    public void onNewBundle(Bundle bundle) {
        UIBaseUtil.log("LifeCycle#%s#onNewBundle", mTag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UIBaseUtil.log("LifeCycle#%s#onAttach", mTag);
        /**
         * 千万不要乱动!!!否则后果自负
         * 当真正Attach后,StackHelperOfFragment需要和Fragment生命周期绑定进行赋值
         * 1.从预置的mStackHelperOfFragment中获取必要参数重设(在Fragment初始化构建时传入的参数)
         * 2.初始化Fragment内部的childFragmentManager
         * 3.所有该StackHelperOfFragment的操作,如add/remove/show/hide的fragment操作,务必在onAttach之后操作,否则无效
         */
        StackHelperOfActivity stackHelperOfActivity = getStackHelperOfFragment().getParentStackHelperOfActivity();
        StackHelperOfFragment stackHelperOfFragment = getStackHelperOfFragment().getParentStackHelperOfFragment();
        mStackHelperOfFragment = new StackHelperOfFragment(this);
        mStackHelperOfFragment.setParentStackHelperOfActivity(stackHelperOfActivity);
        mStackHelperOfFragment.setParentStackHelperOfFragment(stackHelperOfFragment);
        mStackHelperOfFragment.initBeforeUse();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIBaseUtil.log("LifeCycle#%s#onCreate", mTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UIBaseUtil.log("LifeCycle#%s#onCreateView", mTag);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIBaseUtil.log("LifeCycle#%s#onViewCreated", mTag);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UIBaseUtil.log("LifeCycle#%s#onActivityCreated", mTag);
    }

    @Override
    public void onStart() {
        super.onStart();
        UIBaseUtil.log("LifeCycle#%s#onStart", mTag);
    }

    @Override
    public void onResume() {
        super.onResume();
        UIBaseUtil.log("LifeCycle#%s#onResume", mTag);
    }

    @Override
    public void onPause() {
        super.onPause();
        UIBaseUtil.log("LifeCycle#%s#onPause", mTag);
    }

    @Override
    public void onStop() {
        super.onStop();
        UIBaseUtil.log("LifeCycle#%s#onStop", mTag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UIBaseUtil.log("LifeCycle#%s#onDestroyView", mTag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UIBaseUtil.log("LifeCycle#%s#onDestroy", mTag);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UIBaseUtil.log("LifeCycle#%s#onDetach", mTag);
    }

    /**
     * BackPressed事件回调
     *
     * @param back back数据
     * @return true表示拦截处理, 不finish;false表示不处理,finish掉
     */
    public boolean onBaseBackPressed(Back back) {
        return false;
    }

    /**
     * BackPressed事件拦截
     *
     * @param back back数据
     * @return true表示拦截处理, 交予{@link this#onBaseBackPressed(Back)}处理;false表示不拦截,交予下层处理
     */
    public boolean onBaseInterceptBackPressed(Back back) {
        return false;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        /**
         * http://stackoverflow.com/questions/14900738/nested-fragments-disappear-during-transition-animation
         * 解决Fragment退出时,内嵌Fragment直接消失问题,Support包Bug
         * 目前父Fragment退出时,子Fragment做静态动画(无动画)
         * 后续考虑子Fragment动画动态从LaunchParam内计算得出
         */
        Fragment parentFragment = getParentFragment();
        if (!enter && null != parentFragment && parentFragment.isRemoving()) {
            Animation doNothingAnim = new AlphaAnimation(1.0f, 1.0f);
            doNothingAnim.setDuration(getNextAnimationDuration(parentFragment, 1000));
            return doNothingAnim;
        } else {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

    private long getNextAnimationDuration(Fragment fragment, long defValue) {
        try {
            Field nextAnimField = Fragment.class.getDeclaredField("mNextAnim");
            nextAnimField.setAccessible(true);
            int nextAnimResource = nextAnimField.getInt(fragment);
            Animation nextAnim = AnimationUtils.loadAnimation(fragment.getActivity(), nextAnimResource);
            return (null == nextAnim) ? defValue : nextAnim.getDuration();
        } catch (Exception e) {
            return defValue;
        }
    }

    public int getLaunchFlag() {
        return mLaunchFlag;
    }

    public void setLaunchFlag(int launchFlag) {
        mLaunchFlag = launchFlag;
    }

    public boolean isSpecifyContainer() {
        return mSpecifyContainer;
    }

    public void setSpecifyContainer(boolean specifyContainer) {
        mSpecifyContainer = specifyContainer;
    }

    public int getAnimEnterResId() {
        return mAnimEnterResId;
    }

    public void setAnimEnterResId(int animEnterResId) {
        mAnimEnterResId = animEnterResId;
    }

    public int getAnimExitResId() {
        return mAnimExitResId;
    }

    public void setAnimExitResId(int animExitResId) {
        mAnimExitResId = animExitResId;
    }

    public StackHelperOfFragment getStackHelperOfFragment() {
        // 运行时态去构建,避免类成员变量初始化时,会导致强转时数据丢失,目前无解!!!
        if (null == mStackHelperOfFragment) {
            synchronized (this) {
                if (null == mStackHelperOfFragment) {
                    mStackHelperOfFragment = new StackHelperOfFragment(this);
                }
            }
        }
        return mStackHelperOfFragment;
    }

}
