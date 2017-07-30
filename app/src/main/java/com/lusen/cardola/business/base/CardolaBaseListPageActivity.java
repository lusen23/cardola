package com.lusen.cardola.business.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.framework.adapter.HolderViewAdapter;
import com.lusen.cardola.framework.adapter.HolderViewItem;
import com.lusen.cardola.framework.adapter.IAdapterData;
import com.lusen.cardola.framework.network.BaseSubscriber;
import com.lusen.cardola.framework.uikit.RefreshListView;
import com.lusen.cardola.framework.uikit.StateView;
import com.lusen.cardola.framework.uikit.pulltorefresh.PullToRefreshBase;
import com.lusen.cardola.framework.util.ThreadUtil;
import com.lusen.cardola.framework.util.UiUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/30.
 */

public abstract class CardolaBaseListPageActivity<D extends IAdapterData, T extends Serializable> extends CardolaBaseActivity {

    private RefreshListView mRefreshListView;
    private StateView mStateView;

    private HolderViewAdapter mHolderViewAdapter;
    private List<D> mDatas = new ArrayList<>();
    private int mCurPage;

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.layout_base_list_page, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mRefreshListView = UiUtil.findViewById(this, R.id.listview, RefreshListView.class);
        mStateView = UiUtil.findViewById(this, R.id.stateview, StateView.class);
        mHolderViewAdapter = new HolderViewAdapter(this, mDatas, getHolderViews());
        mStateView.setStateCallback(new StateView.StateCallback() {
            @Override
            public void onStateClick(@StateView.State int state) {
                if (state == StateView.State.EMPTY) {
                    loadData(true);
                }
            }
        });
        mRefreshListView.setAdapter(mHolderViewAdapter);
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }
        });
        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position, mDatas.get(position));
            }
        });
        mStateView.changeState(StateView.State.LOADING);
        loadData(true);
    }

    private void loadData(final boolean refresh) {
        mRefreshListView.setRefreshing();
        int requestPage = 0;
        if (!refresh) {
            requestPage = mCurPage + 1;
        }
        requestData(requestPage, new ListPageSubscriber(requestPage));
    }

    public abstract Class<? extends HolderViewItem>[] getHolderViews();

    public abstract void onListItemClick(int position, D data);

    public abstract void requestData(int requestPage, ListPageSubscriber subscriber);

    public abstract List<D> convertData(T t);

    public class ListPageSubscriber extends BaseSubscriber<BaseResponse<T>> {

        private int mRequestPage;

        public ListPageSubscriber(int requestPage) {
            mRequestPage = requestPage;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
//            mRefreshListView.onRefreshComplete();
//            mStateView.changeState(StateView.State.INIT);

            // TODO 仅做测试
            ThreadUtil.MAIN_THREAD_HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseResponse<T> response = new BaseResponse<>();
                    response.success = 0;
                    onNext(response);
                }
            }, 5000);

        }

        @Override
        public void onNext(BaseResponse<T> response) {
            super.onNext(response);
            mRefreshListView.onRefreshComplete();
            // 接口成功
            if (null != response && response.isSuccess()) {
                List<D> datas = convertData(response.data);
                // 记录当前页
                mCurPage = mRequestPage;
                if (mRequestPage == 0) {
                    mDatas.clear();
                }
                mDatas.addAll(datas);
            }
            // 判断空视图
            mStateView.changeState(mDatas.isEmpty() ? StateView.State.EMPTY : StateView.State.INIT);
            // 刷新视图
            mHolderViewAdapter.notifyDataSetChanged();
        }

    }

}
