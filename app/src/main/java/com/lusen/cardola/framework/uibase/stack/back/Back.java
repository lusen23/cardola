package com.lusen.cardola.framework.uibase.stack.back;

/**
 * Created by leo on 16/8/2.
 * Back返回
 */
public class Back {

    /**
     * Back返回-来源
     */
    public BackOrigin mBackOrigin = BackOrigin.BACK_FROM_SYSTEM;
    /**
     * 额外数据
     */
    public Object mExtra;

    public Back() {
    }

    public Back(BackOrigin backOrigin) {
        mBackOrigin = backOrigin;
    }

    public static Back system() {
        return system(null);
    }

    public static Back custom() {
        return custom(null);
    }

    public static Back system(Object extra) {
        Back back = new Back(BackOrigin.BACK_FROM_SYSTEM);
        back.mExtra = extra;
        return back;
    }

    public static Back custom(Object extra) {
        Back back = new Back(BackOrigin.BACK_FROM_CUSTOM);
        back.mExtra = extra;
        return back;
    }

}
