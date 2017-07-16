package com.lusen.cardola.framework.uibase.stack;

import android.support.v4.app.Fragment;

/**
 * Created by leo on 16/7/26.
 * Fragment栈实体，用于保存栈内Fragment属性及状态
 */
public class FragmentStackEntity {

    public Fragment mFragment;
    public int mContainerResId;
    public int mAnimEnterResId;
    public int mAnimExitResId;
    public int mLaunchFrag;
    public boolean mSpecifyContainer;

    public FragmentStackEntity() {
    }

    public FragmentStackEntity(Fragment fragment, int containerResId, int launchFrag, boolean specifyContainer) {
        mFragment = fragment;
        mContainerResId = containerResId;
        mLaunchFrag = launchFrag;
        mSpecifyContainer = specifyContainer;
    }

    public FragmentStackEntity(Fragment fragment, int containerResId, int animEnterResId, int animExitResId, int launchFrag, boolean specifyContainer) {
        mFragment = fragment;
        mContainerResId = containerResId;
        mAnimEnterResId = animEnterResId;
        mAnimExitResId = animExitResId;
        mLaunchFrag = launchFrag;
        mSpecifyContainer = specifyContainer;
    }

}
