package com.lusen.cardola.framework.uibase.stack;

/**
 * Created by leo on 16/8/11.
 * Activity启动参数(用于管理Activity内部启动和结束时,执行的参数)
 */
public class ActivityLaunchParam {

    /**
     * 启动Activity时，触发的动画属性(即startActivity或startActivityForResult)
     * (进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * 当前[A],A打开至B,A执行mLaunchExitAnim,B执行mLaunchEnterAnim
     */
    public Integer mLaunchEnterAnim;
    public Integer mLaunchExitAnim;
    /**
     * 结束Activity时，触发的动画属性(即finish)
     * (进入和退出均为null则采用系统默认，小于等于0采用框架默认，大于0采用传入的)
     * 当前[AB],B退出至A,A执行mFinishEnterAnim,B执行mFinishExitAnim
     */
    public Integer mFinishEnterAnim;
    public Integer mFinishExitAnim;

}
