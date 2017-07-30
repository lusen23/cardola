package com.lusen.cardola.business.main.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.base.CardolaBaseFragment;
import com.lusen.cardola.business.main.home.data.ProductAssortBaseData;
import com.lusen.cardola.business.main.home.holderitem.HolderItemProductAssortDouble;
import com.lusen.cardola.business.main.home.holderitem.HolderItemProductAssortTriple;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.business.network.resp.GetProductAssortListResp;
import com.lusen.cardola.framework.adapter.HolderViewAdapter;
import com.lusen.cardola.framework.network.BaseSubscriber;
import com.lusen.cardola.framework.uibase.UiModelActionBarHelper;
import com.lusen.cardola.framework.uikit.RefreshListView;
import com.lusen.cardola.framework.uikit.pulltorefresh.PullToRefreshBase;
import com.lusen.cardola.framework.util.ContextUtil;
import com.lusen.cardola.framework.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/23.
 */

public class ProductAssortFragment extends CardolaBaseFragment {

    private RefreshListView mRefreshListView;
    private HolderViewAdapter mHolderViewAdapter;
    private List<ProductAssortBaseData> mDatas = new ArrayList<>();

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.fragment_product_assort, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mRefreshListView = UiUtil.findViewById(getView(), R.id.listview, RefreshListView.class);
        mHolderViewAdapter = new HolderViewAdapter(getHostActivityIfExist(), mDatas, HolderItemProductAssortDouble.class, HolderItemProductAssortTriple.class);
        mRefreshListView.setAdapter(mHolderViewAdapter);
        mRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
        return ContextUtil.getContext().getResources().getString(R.string.actionbar_title_product_assort);
    }

    @Override
    public void onActionViewCreated(UiModelActionBarHelper helper) {
        super.onActionViewCreated(helper);
        mActionViewBack.hide(false);
    }

    private void loadData() {
        mRefreshListView.setRefreshing();
        CardolaApiManager.getInstance().getProductAssortList(new BaseSubscriber<BaseResponse<GetProductAssortListResp>>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRefreshListView.onRefreshComplete();

                mDatas.clear();
                List<ProductAssortBaseData> datas = ProductAssortBaseData.convert(null);
                mDatas.addAll(datas);
                mHolderViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNext(BaseResponse<GetProductAssortListResp> response) {
                super.onNext(response);
                mRefreshListView.onRefreshComplete();
                mDatas.clear();
                if (null != response && response.isSuccess()) {
                    List<ProductAssortBaseData> datas = ProductAssortBaseData.convert(response.data);
                    mDatas.addAll(datas);
                }
                mHolderViewAdapter.notifyDataSetChanged();
            }
        });
    }

}
