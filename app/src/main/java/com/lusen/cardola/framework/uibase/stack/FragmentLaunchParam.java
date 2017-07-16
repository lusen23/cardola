package com.lusen.cardola.framework.uibase.stack;

/**
 * Created by leo on 16/7/26.
 * Fragment启动参数
 * >>fragment启动标识位,控制fragment栈内开启及结束方式
 * >>fragment动画,控制fragment开启及结束时动画效果
 * >>容器id,标识fragment即将开启的容器id
 * >>容器指定,标识fragment是否考虑针对单容器队列(大部分情况不需要,直接采用总栈)
 */
public class FragmentLaunchParam {

    /**
     * Fragment启动标识常量
     * >>standard:多个实例
     * >>clear_top:清除栈顶
     * >>clear_all:清除全栈
     * >>single_top:栈顶单例
     */
    public static final int FRAGMENT_LAUNCH_STANDARD = 1;
    public static final int FRAGMENT_LAUNCH_CLEAR_TOP = FRAGMENT_LAUNCH_STANDARD << 1;
    public static final int FRAGMENT_LAUNCH_CLEAR_ALL = FRAGMENT_LAUNCH_STANDARD << 2;
    public static final int FRAGMENT_LAUNCH_SINGLE_TOP = FRAGMENT_LAUNCH_STANDARD << 3;

    /**
     * 动画常量
     * >>anim_none:无动画
     * >>anim_parent:遵循父容器动画属性
     */
    public static final int ANIM_NONE = 0;
    public static final int ANIM_PARENT = -1;

    public Integer mContainerResId;     // 容器id
    public Integer mAnimEnterResId;     // 进入动画
    public Integer mAnimExitResId;      // 退出动画
    public Integer mLaunchFlag;         // 启动标识
    public Boolean mSpecifyContainer;   // 是否指定容器

    public static final boolean isFlagStandard(int flag) {
        return (flag & FRAGMENT_LAUNCH_STANDARD) != 0;
    }

    public static final boolean isFlagClearTop(int flag) {
        return (flag & FRAGMENT_LAUNCH_CLEAR_TOP) != 0;
    }

    public static final boolean isFlagClearAll(int flag) {
        return (flag & FRAGMENT_LAUNCH_CLEAR_ALL) != 0;
    }

    public static final boolean isFlagSingleTop(int flag) {
        return (flag & FRAGMENT_LAUNCH_SINGLE_TOP) != 0;
    }

}
