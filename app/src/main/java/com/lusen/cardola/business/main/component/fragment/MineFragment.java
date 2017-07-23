package com.lusen.cardola.business.main.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.R;
import com.lusen.cardola.business.actionview.ActionViewFactory;
import com.lusen.cardola.business.base.CardolaBaseFragment;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarLayout;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.ToastUtil;

/**
 * Created by leo on 2017/7/23.
 */

public class MineFragment extends CardolaBaseFragment {
    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.fragment_mine, container);
    }

    @Override
    public String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_mine);
    }

    @Override
    public void onActionViewCreated(UiModelActionBarHelper helper) {
        super.onActionViewCreated(helper);
        mActionViewBack.hide(false);
    }

}
