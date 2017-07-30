package com.lusen.cardola.business.main.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseFragment;
import com.lusen.cardola.business.scheme.SchemeUrlConstant;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uikit.CommonItemView;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.UiUtil;

import navigator.Nav;

/**
 * Created by leo on 2017/7/23.
 */

public class MineFragment extends CardolaBaseFragment implements View.OnClickListener {

    private CommonItemView mItemMineAccount;
    private CommonItemView mItemMinePerformance;
    private CommonItemView mItemMineMessage;

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.fragment_mine, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mItemMineAccount = UiUtil.findViewById(view, R.id.item_mine_account, CommonItemView.class);
        mItemMinePerformance = UiUtil.findViewById(view, R.id.item_mine_performance, CommonItemView.class);
        mItemMineMessage = UiUtil.findViewById(view, R.id.item_mine_message, CommonItemView.class);
        UiUtil.bindClickListener(this, mItemMineAccount, mItemMinePerformance, mItemMineMessage);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mItemMineAccount.getId()) {
            Nav.fromHost(SchemeUrlConstant.Host.MINE_ACCOUNT).nav();
        } else if (id == mItemMinePerformance.getId()) {
            Nav.fromHost(SchemeUrlConstant.Host.MINE_PERFORMANCE).nav();
        } else if (id == mItemMineMessage.getId()) {
            Nav.fromHost(SchemeUrlConstant.Host.MINE_MESSAGE).nav();
        }
    }
}
