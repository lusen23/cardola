package com.lusen.cardola.business.main.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.actionview.ActionViewFactory;
import com.lusen.cardola.business.base.CardolaBaseFragment;
import com.lusen.cardola.business.main.home.data.CustomerData;
import com.lusen.cardola.business.main.home.holderitem.HolderItemCustomer;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.business.network.resp.GetCustomerListResp;
import com.lusen.cardola.framework.adapter.HolderViewAdapter;
import com.lusen.cardola.framework.network.BaseSubscriber;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionBarLayout;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uikit.RefreshListView;
import com.lusen.cardola.framework.uikit.pulltorefresh.PullToRefreshBase;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.ToastUtil;
import com.lusen.cardola.framework.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/23.
 */

public class CustomerListFragment extends CardolaBaseFragment {

    private RefreshListView mRefreshListView;
    private HolderViewAdapter mHolderViewAdapter;
    private List<CustomerData> mDatas = new ArrayList<>();

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.fragment_customer_list, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mRefreshListView = UiUtil.findViewById(getView(), R.id.listview, RefreshListView.class);
        mHolderViewAdapter = new HolderViewAdapter(getHostActivityIfExist(), mDatas, HolderItemCustomer.class);
        mRefreshListView.setAdapter(mHolderViewAdapter);
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        loadData();
    }

    @Override
    public String initActionBarTitle() {
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_customer_list);
    }

    @Override
    public void onActionViewCreated(UiModelActionBarHelper helper) {
        super.onActionViewCreated(helper);
        ActionView actionViewSearch = ActionViewFactory.buildActionView(getLayoutInflater(), ActionViewFactory.SEARCH);
        helper.addActionViewToContainer(actionViewSearch, ActionBarLayout.ActionContainer.RIGHT, true);
//        mActionViewBack.hide(false);
    }

    @Override
    public void onActionViewClick(ActionView actionView) {
        super.onActionViewClick(actionView);
        int id = actionView.getId();
        if (id == ActionViewFactory.SEARCH) {
            search();
        }
    }

    private void search() {
        ToastUtil.toast("搜索");
    }

    private void loadData() {
        mRefreshListView.setRefreshing();
        CardolaApiManager.getInstance().getCustomerList(null, new BaseSubscriber<BaseResponse<GetCustomerListResp>>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRefreshListView.onRefreshComplete();
            }

            @Override
            public void onNext(BaseResponse<GetCustomerListResp> response) {
                super.onNext(response);
                mRefreshListView.onRefreshComplete();
                mDatas.clear();
                if (null != response && response.isSuccess()) {
                    List<CustomerData> datas = CustomerData.convert(response.data);
                    mDatas.addAll(datas);
                }
                mHolderViewAdapter.notifyDataSetChanged();
            }
        });
    }

}
