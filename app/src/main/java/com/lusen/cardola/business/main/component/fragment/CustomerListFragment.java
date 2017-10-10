package com.lusen.cardola.business.main.component.fragment;

import android.text.TextUtils;

import com.lusen.cardola.R;
import com.lusen.cardola.business.actionview.ActionViewFactory;
import com.lusen.cardola.business.base.CardolaBaseListPageFragment;
import com.lusen.cardola.business.main.home.data.CustomerData;
import com.lusen.cardola.business.main.home.holderitem.HolderItemCustomer;
import com.lusen.cardola.business.main.user.UserManager;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.GetCustomerListResp;
import com.lusen.cardola.business.scheme.SchemeUrlConstant;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarLayout;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.util.ContextUtil;

import java.util.List;

import navigator.Nav;

/**
 * Created by leo on 2017/7/23.
 */

public class CustomerListFragment extends CardolaBaseListPageFragment<CustomerData, List<GetCustomerListResp>> {

    @Override
    public String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_customer_list);
    }

    @Override
    public void onActionViewCreated(UiModelActionBarHelper helper) {
        super.onActionViewCreated(helper);
        ActionView actionViewAdd = ActionViewFactory.buildActionView(getLayoutInflater(), ActionViewFactory.ADD);
        helper.addActionViewToContainer(actionViewAdd, ActionBarLayout.ActionContainer.RIGHT, true);
        mActionViewBack.hide(false);
    }

    @Override
    public void onActionViewClick(ActionView actionView) {
        super.onActionViewClick(actionView);
        int id = actionView.getId();
        if (id == ActionViewFactory.ADD) {
            addCustomer();
        }
    }

    private void addCustomer() {
        Nav.fromHost(SchemeUrlConstant.Host.ADD_CUSTOMER).nav();
    }

    @Override
    public Class<? extends HolderViewItem>[] getHolderViews() {
        return new Class[]{HolderItemCustomer.class};
    }

    @Override
    public void onListItemClick(int position, CustomerData data) {
        if (null != data && !TextUtils.isEmpty(data.mId)) {
            Nav.fromHost(SchemeUrlConstant.Host.MINE_CUSTOMER).param(SchemeUrlConstant.ParamKey.ID, data.mId).nav();
        }
    }

    @Override
    public void requestData(int requestPage, ListPageSubscriber subscriber) {
        CardolaApiManager.getInstance().getCustomerList(UserManager.getInstance().getUserId(), requestPage, subscriber);
    }

    @Override
    public List<CustomerData> convertData(List<GetCustomerListResp> resp) {
        return CustomerData.convert(resp);
    }

}
