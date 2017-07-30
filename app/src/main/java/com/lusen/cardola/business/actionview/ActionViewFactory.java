package com.lusen.cardola.business.actionview;

import android.view.LayoutInflater;

import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;

/**
 * Created by leo on 2017/7/16.
 */

public class ActionViewFactory {

    public static final int TITLE = 10000;
    public static final int BACK = 10001;
    public static final int SEARCH = 10002;
    public static final int ADD = 10003;

    public static ActionView buildActionView(LayoutInflater inflater, int id) {
        ActionView actionView = null;
        switch (id) {
            case BACK:
                actionView = new ActionViewIcon(inflater, id, android.support.design.R.drawable.abc_ic_ab_back_material);
                break;
            case SEARCH:
                actionView = new ActionViewPlain(inflater, id, "搜索");
                break;
            case ADD:
                actionView = new ActionViewPlain(inflater, id, "添加");
                break;
        }
        return actionView;
    }

}
