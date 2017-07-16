package com.lusen.cardola.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lusen.cardola.R;
import com.lusen.cardola.business.actionview.ActionViewFactory;
import com.lusen.cardola.business.base.CardolaBaseActivity;
import com.lusen.cardola.business.network.CardolaApiManager;
import com.lusen.cardola.business.network.resp.HomeResp;
import com.lusen.cardola.framework.adapter.HolderViewAdapter;
import com.lusen.cardola.framework.network.BaseSubscriber;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uikit.RefreshListView;
import com.lusen.cardola.framework.uikit.RemoteImageView;
import com.lusen.cardola.framework.uikit.pulltorefresh.OnInterceptPullRefreshListener;
import com.lusen.cardola.framework.uikit.pulltorefresh.PullToRefreshBase;
import com.lusen.cardola.framework.util.ToastUtil;
import com.lusen.cardola.framework.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/13.
 */

public class DemoActivity extends CardolaBaseActivity implements View.OnClickListener {

    private TextView mTvDemo;
    private RemoteImageView mImgDemo;
    private RefreshListView mRefreshListView;

    private List<DemoData> mDatas = new ArrayList<>();
    private HolderViewAdapter mHolderViewAdapter;

    @Override
    protected View onContentViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflaterView(inflater, R.layout.activity_demo, container);
    }

    @Override
    protected void onContentViewCreated(View view) {
        super.onContentViewCreated(view);
        mTvDemo = UiUtil.findViewById(this, R.id.tv_demo, TextView.class);
        mImgDemo = UiUtil.findViewById(this, R.id.img_demo, RemoteImageView.class);
        mRefreshListView = UiUtil.findViewById(this, R.id.list, RefreshListView.class);
        mImgDemo.load("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg");

        UiUtil.bindClickListener(this, mTvDemo, mImgDemo);

        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题0"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题1"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题2"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题3"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题4"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题5"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题6"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题7"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题8"));
        mDatas.add(new DemoData("http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg", "标题9"));

        mHolderViewAdapter = new HolderViewAdapter(this, mDatas, DemoHolderView.class);
        mRefreshListView.setAdapter(mHolderViewAdapter);
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mRefreshListView.setOnPullBeforeRefreshListener(new OnInterceptPullRefreshListener.SampleOnInterceptPullRefreshListener());
        mRefreshListView.setAutoLoad(true, 3);

        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.toast("onItemClick position = " + position);
            }
        });


        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ToastUtil.toast("onPullDownToRefresh");
//                ThreadUtil.MAIN_THREAD_HANDLER.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.toast("onRefreshComplete");
//                        mRefreshListView.onRefreshComplete();
//                    }
//                }, 3000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ToastUtil.toast("onPullUpToRefresh");
//                mRefreshListView.onRefreshComplete();
            }
        });

        mHolderViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mTvDemo.getId()) {
            CardolaApiManager.getInstance().getOrderInfoList("18", new BaseSubscriber<HomeResp>() {

                @Override
                public void onError(Throwable e) {
                    ToastUtil.toast("onError");
                }

                @Override
                public void onNext(HomeResp homeResp) {
                    ToastUtil.toast("onNext");
                }
            });
        } else if (id == mImgDemo.getId()) {
        }
    }

    @Override
    public String initActionBarTitle() {
        return "卡多拉";
    }

    @Override
    public void onActionViewClick(ActionView actionView) {
        super.onActionViewClick(actionView);
        int id = actionView.getId();
        if (id == ActionViewFactory.BACK) {
            ToastUtil.toast("BACK");
        } else if (id == ActionViewFactory.TITLE) {
            ToastUtil.toast("TITLE");
        } else if (id == ActionViewFactory.SEARCH) {
            ToastUtil.toast("SEARCH");
        }
    }

}
