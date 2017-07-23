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
import com.lusen.cardola.business.network.resp.BaseResponse;
import com.lusen.cardola.business.network.resp.HomeResp;
import com.lusen.cardola.framework.adapter.HolderViewAdapter;
import com.lusen.cardola.framework.network.BaseSubscriber;
import com.lusen.cardola.framework.uibase.ui.actionbar.ActionView;
import com.lusen.cardola.framework.uikit.RefreshListView;
import com.lusen.cardola.framework.uikit.RemoteImageView;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.AccsDialog;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.PropertyConstants;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetButton;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetImage;
import com.lusen.cardola.framework.uikit.choicedialogxm.cmd.core.WidgetPlain;
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
            CardolaApiManager.getInstance().getOrderInfoList("", new BaseSubscriber<BaseResponse<HomeResp>>() {

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    ToastUtil.toast("onError");
                }

                @Override
                public void onNext(BaseResponse<HomeResp> homeRespBaseResponse) {
                    ToastUtil.toast("onNext");
                }
            });

            CardolaApiManager.getInstance().getOrderInfoList("", new BaseSubscriber<BaseResponse<HomeResp>>() {
                @Override
                public void onCompleted() {
                    super.onCompleted();
                }

                @Override
                public void onNext(BaseResponse<HomeResp> response) {
                    super.onNext(response);
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
            showAccsDialog();
        } else if (id == ActionViewFactory.SEARCH) {
            ToastUtil.toast("SEARCH");
        }
    }

    public void showAccsDialog() {
        AccsDialog dialog = new AccsDialog();
        dialog.closeWhenBack(true).timeout(60 * 1000);
        dialog.buildAreaMessage()
                .height(0.4f)
                .style(PropertyConstants.AreaMessageStyle.IMAGE_PLAIN_FROM_TOP_TO_BOTTOM)
                .plain(new WidgetPlain().height(0.6f).text("欢迎来到虾米音乐").schemeUrl("111111").closeWhenClick(false))
                .image(new WidgetImage().height(0.4f).url("http://pic61.nipic.com/file/20150309/615823_101434713000_2.jpg").schemeUrl("222222").closeWhenClick(false));
        dialog.buildAreaButton()
                .button(new WidgetButton().itemId("button_id_0").text("查看VIP信息").schemeUrl("xiami://album/101").closeWhenClick(true).bgColor("#EF4136").txColor("#FFFFFF"),
                        new WidgetButton().itemId("button_id_1").text("取消").schemeUrl("xiami://album/101").closeWhenClick(true).bgColor("#777777").txColor("#543545"));
        dialog.show();
    }

}
